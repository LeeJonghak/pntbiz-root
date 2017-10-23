var profileName = (process.argv.length > 2) ? process.argv[2] : "local";
var siteName = (process.argv.length > 3) ? process.argv[3] : "common";

console.log("# Profile : ", profileName);
console.log("# Site : ", siteName);

var configLoader = require('./config/config');

var appConfig = configLoader.get("app", profileName, siteName);
console.log(appConfig);
/*---------------------------- Socket Server 설정 시작-----------------------------------*/
var io;
var web;
var webConfig;
var server;
var app = function (req, res, io) {
    console.log(req.url)
    if (req.url == "/presence/send/UpdateMarker") {
        var jsonData = "";
        req.on('data', function (chunk) {
            jsonData += chunk;
        });
        req.on('end', function () {
            var reqObj = JSON.parse(jsonData);
            var resObj = {
                result: "success",
                code: 0
            };
            jsonMsg = JSON.parse(jsonData);
            console.log('updateMarker', "presence-"+reqObj.UUID, reqObj);
            IO_GLOBAL.to("presence-"+reqObj.UUID).emit('updateMarker', reqObj);
            res.writeHead(200);
            res.end(JSON.stringify(resObj));
        });
    } else if (req.url == "/presence/send/Notification") {
        var jsonData = "";
        req.on('data', function (chunk) {
            jsonData += chunk;
        });
        req.on('end', function () {
            var reqObj = JSON.parse(jsonData);
            var resObj = {
                result: "success",
                code: 0
            };
            console.log('notification', "presence-"+reqObj.beaconInfo.suuid, reqObj);
            IO_GLOBAL.to("presence-"+reqObj.beaconInfo.suuid).emit('notification', reqObj);
            res.writeHead(200);
            res.end(JSON.stringify(resObj));
        });
    } else {
        res.end("404");
    }
}
if (appConfig.ssl == false) {
    web        = require('http');
    server = web.createServer(app).listen(appConfig.port);
} else {
    fs = require('fs');
    web        = require('https');
    webConfig  = {
        key: fs.readFileSync(appConfig.cert.privateKey),
        cert: fs.readFileSync(appConfig.cert.cerificate),
        ca: fs.readFileSync(appConfig.cert.ca),
        requestCert: false,
        rejectUnauthorized: false
    };
    server = web.createServer(webConfig, app).listen(appConfig.port);
}

io = require("socket.io").listen(server);

if (appConfig.redisCluster == true) {
    var redisConfig = configLoader.get('redis-cluster', profileName, siteName);
    var Redis = require('ioredis');
    var cluster = new Redis.Cluster(redisConfig);
    var adapter = require('socket.io-redis');
    io.adapter(adapter({ pubClient: cluster, subClient: cluster }));
} else {
    var redisConfig = configLoader.get('redis-socket', profileName, siteName);
    var redis = require('redis').createClient;
    var adapter = require('socket.io-redis');
    var pub = redis(redisConfig.port, redisConfig.host, {return_buffers: true, auth_pass: redisConfig.password });
    var sub = redis(redisConfig.port, redisConfig.host, {return_buffers: true, auth_pass: redisConfig.password });
    io.adapter(adapter({ pubClient: pub, subClient: sub }));
}

var IO_GLOBAL = io.sockets.on('connection', function (socket){
    var roomID;
    socket.on('join', function(id){
        roomID = id;
        socket.join(roomID);
    });
    socket.on('leave', function(){
        socket.leave(roomID);
    });
    socket.on('sendMsg',function(msg){
        socket.to(roomID).emit('msg', msg);
    });
    // 새로고침 또는 창 닫기
    socket.on('disconnect', function(){
        socket.leave(roomID);
    });

    socket.on('UpdateMarker', function(msg){
        jsonMsg = JSON.parse(msg);
        console.log('updateMarker', "presence-"+jsonMsg.UUID, jsonMsg);
        socket.to("presence-"+jsonMsg.UUID).emit('updateMarker', jsonMsg);
    });

    socket.on('Notification', function(msg){
        jsonMsg = JSON.parse(msg);
        console.log('notification', "presence-"+jsonMsg.beaconInfo.suuid, jsonMsg);
        socket.to("presence-"+jsonMsg.beaconInfo.suuid).emit('notification', jsonMsg);
    });
});



/*---------------------------- Socket Server 설정 끝-----------------------------------*/


/*---------------------------- TCP Socket Server 설정 -----------------------------------*/
var tcpSocketConfig = configLoader.get("tcp-socket", profileName, siteName);
console.log(tcpSocketConfig);
if (tcpSocketConfig != null) {

    var sleep = require('sleep');
    var net = require('net');
    var server = net.createServer(function(socket) {
        socket.pipe(socket, {end:false});

        var timeout=1000;
        socket.setTimeout(timeout);
        socket.on('timeout',function(){
            //var buffer = new Buffer(68);
            var STX = String.fromCharCode(2);
            var ETX = String.fromCharCode(3);
            var NULL = String.fromCharCode(0);

            var cardNo = setString("8009011027813", 13);
            var issusCount = setString("001", 3);
            var sabeon = setString("A1234567", 20);
            var dummy = setString("fasdfasdfa",30);

            var string = STX + cardNo + issusCount + sabeon + dummy + ETX;

            var buffer = new Buffer(string, "ascii");
            socket.write(buffer);
        });
    });

    server.listen(tcpSocketConfig.port, tcpSocketConfig.host);

    var client = new net.Socket();

    client.connect(tcpSocketConfig.port, tcpSocketConfig.host, function() {
        console.log('Connected');
    });

    var count = 0;
    client.on('data', function(data) {
        if (data.length != 68) {
            console.log("error", "data length is not correct");
            return;
        }
        if (data[0] != 2 && data[67] != 3 ){
            console.log("error", "data STX & ETX is not correct");
            return;
        }

        count ++;

        var cardNo = getString(data, 1,1+13);
        var issusCount = getString(data, 1+13, 1+13+3);
        var sabeon = getString(data, 1+13+3, 1+13+3+20);
        var dummy = getString(data, 1+13+3+20, 1+13+3+20+30);
        var object = new Object();
        object.cardNo = cardNo;
        object.issusCount = issusCount;
        object.sabeon = sabeon;
        object.dummy = dummy;

        console.log('Received: ' + data.length + ' ' + data);

        var http = require("http");
        var options = {
            hostname: 'localhost',
            port: 8002,
            path: '/beacon/external/set',
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            }
        };

        var req = http.request(options, function(res) {
            console.log('Status: ' + res.statusCode);
            console.log('Headers: ' + JSON.stringify(res.headers));
            res.setEncoding('utf8');
            res.on('data', function (body) {
                console.log('Body: ' + body);
            });
        });
        req.on('error', function(e) {
            console.log('problem with request: ' + e.message);
        });

        req.write(JSON.stringify(object));
        req.end();
    });

    client.on('close', function() {

    });

    var setString = function(string, totalLength) {
        var length = string.length;
        var result = string;
        for (i = length ; i < totalLength ; i ++) {
            result = result + String.fromCharCode(0);
        }
        return result;
    }

    var getString = function (buffer, start, end) {
        console.log(buffer);
        buffer.slice(start, end);
        var result = "";
        for (i = start ; i < end ; i ++) {
            if (buffer[i] == 0) break;
            result = result + String.fromCharCode(buffer[i]);
        }
        return result;
    }
}
/*---------------------------- TCP Socket Server 설정 -----------------------------------*/