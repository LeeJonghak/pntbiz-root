$(document).ready( function() {	
	if($('#codeActionKeyword').val() == "") $('#codeActionKeyword').focus();	
	$('#codeActionKeyword').bind('keyup', function(e) { if(e.keyCode==13) codeAction.search(); });	
	$('#codeActionFormBtn').bind('click', function() { codeAction.form(); });
	$('#codeActionMFormBtn').bind('click', function() { codeAction.mform(); });
});
$(window).load(function() {
	dashboard.getScannerStatus("init");
//	$("#map-frame").attr('src', dashboard._scannerURL + "/login/force/" + $("#userID").val() + "/" + encodeURIComponent("/presence/frame"));
});

var dashboard = {	
	_scannerURL: "https://beacon.pntbiz.com:10000", // 외부url config 뺄 예정
	_scannerStatusPath: "/api/scanner/status", 
	_scannerCPUChart: [],
	_scannerMEMChart: [],
	_scannerData: [[]],
	_scannerDataPoints: 50,
	_scannerDataUpdateInterval: 10000,
	getScannerStatus: function(type) {
		if(type == "init") {
			$.ajax({ type: "GET",
				contentType: 'application/json',
				processData: false,
				url: this._scannerURL + this._scannerStatusPath + "/" + $("#comNum").val(),
				//data: $(this._form).serialize(),
				success: this.getScannerStatusResult 
			});
		} else {
			$.ajax({ type: "GET",
				contentType: 'application/json',
				processData: false,
				url: this._scannerURL +  this._scannerStatusPath + "/" + $("#comNum").val(),
				//data: $(this._form).serialize(),
				success: this.updateScannerStatusResult
			});
		}
	},
	getScannerStatusResult: function(result) {
		switch(result.code) {
			case "0" :				
				dashboard.setScannerChart(result.data);
				break;
			default : break;
		}
	},
	updateScannerStatusResult: function(result) {
		switch(result.code) {
			case "0" : 
				var data = result.data;
				for(var idx in data) {
					var macAddr = data[idx].macAddr.replace(/:/g, "");					
					var ping = (data[idx].pingStatus == "U") ? "UP" : "DOWN";
					var cpuval = (data[idx].pingStatus == "U") ? Number(data[idx].cpu).toFixed(8) : 0.00000000;
					var memval = (data[idx].pingStatus == "U") ? Number(data[idx].mem).toFixed(8) : 0.00000000;
					var style = (data[idx].pingStatus == "U") ? "bg-success" : "bg-danger";
					var cpu = dashboard._scannerData[macAddr]["cpu"];
					var mem = dashboard._scannerData[macAddr]["mem"];					
					$("#scanner-ping-badge-"+macAddr).removeClass("bg-success bg-danger").addClass(style);
					$("#scanner-ping-"+macAddr).html(ping);
					$("#scanner-cpuval-"+macAddr).html(cpuval);
					$("#scanner-memval-"+macAddr).html(memval);
//					$("#scanner-updtime-"+macAddr).html(data[idx].updtime);
//					$("#scanner-downdtime-"+macAddr).html(data[idx].downdtime);
//					$("#scanner-moddtime-"+macAddr).html(data[idx].moddtime);
					if(cpu.length < dashboard._scannerDataPoints) {
						cpu.push([cpu.length, data[idx].cpu]);
					} else {
						cpu.splice(0, 1);
						for(var kidx in cpu) {
							cpu[kidx][0] = kidx;
						}
						cpu.push([dashboard._scannerDataPoints, data[idx].cpu]);
					}
					if(mem.length < dashboard._scannerDataPoints) {
						mem.push([mem.length, data[idx].mem]);
					} else {
						mem.splice(0, 1);						
						for(var kidx in mem) {
							mem[kidx][0] = kidx;
						}
						mem.push([dashboard._scannerDataPoints, data[idx].mem]);
					}					
					dashboard._scannerCPUChart[macAddr].setData([cpu]);
					dashboard._scannerCPUChart[macAddr].draw();
					dashboard._scannerMEMChart[macAddr].setData([mem]);
					dashboard._scannerMEMChart[macAddr].draw();
				}
				break;
			default : break;
		}
	},
	getScannerChartHtml: function(macAddr, data) {
		data.ping = (data.pingStatus == "U") ? "UP" : "DOWN";
		var html = "";
//		html+= '<tr id="scanner-'+macAddr+'">';
//		html+= '<td class="p15 mnw150">';
//		html+= '	<h4 class="mb15 text-muted">'+data.scannerName+'</h4>';
//		html+= '	<p><b class="text-info">'+data.macAddr+'</b><span class="pull-right text-muted"></span></p>';
//		html+= '	<p><b class="text-warning">'+data.ip+'</b><span class="pull-right text-muted"></span></p>';
//		html+= '</td>';
//		html+= '<td class="p15">';
//		html+= '	<h4 id="scanner-ping-'+macAddr+'" class="mb25 text-muted">'+data.ping+'</h4>';
//		html+= '</td>';
//		html+= '<td class="p15">';
//		html+= '	<div class="realtime-flotchart-container">';
//		html+= '		<div id="scanner-cpu-'+macAddr+'" class="realtime-flotchart-placeholder"></div>';
//		html+= '	</div>';
//		html+= '</td>';
//		html+= '<td class="p15">';
//		html+= '	<div class="realtime-flotchart-container">';
//		html+= '		<div id="scanner-mem-'+macAddr+'" class="realtime-flotchart-placeholder"></div>';
//		html+= '	</div>';
//		html+= '</td>';
//		html+= '<td class="p15">';
//		html+= '	<span id="scanner-updtime-'+macAddr+'" class="label bg-success"></span><br />';
//		html+= '	<span id="scanner-downdtime-'+macAddr+'" class="label bg-danger"></span><br />';
//		html+= '	<span id="scanner-moddtime-'+macAddr+'" class="label bg-info"></span>';
//		html+= '</td>';
//		html+= '</tr>';
		html+= '<tr id="scanner-'+macAddr+'">';
//		html+= '<td>Name</td>';
//		html+= '<td>CPU</td>';
//		html+= '<td>MEM</td>';
//		html+= '</tr>';
//		html+= '<tr>';
		html+= '<td width="20%">';
		html+= '	<h4 class="mb15 text-muted">'+data.scannerName+'</h4>';
		html+= '</td>';
		html+= '<td width="40%">';
		html+= '	<span class="label bg-warning">CPU : <span id="scanner-cpuval-'+macAddr+'">' + Number(data.cpu).toFixed(8) + '</span></span>';
		html+= '	<div class="realtime-flotchart-container">';
		html+= '		<div id="scanner-cpu-'+macAddr+'" class="realtime-flotchart-placeholder"></div>';
		html+= '	</div>';
		html+= '</td>';
		html+= '<td width="40%">';
		html+= '	<span class="label bg-system">MEM : <span id="scanner-memval-'+macAddr+'">' + Number(data.mem).toFixed(8) + '</span></span>';
		html+= '	<div class="realtime-flotchart-container">';
		html+= '		<div id="scanner-mem-'+macAddr+'" class="realtime-flotchart-placeholder"></div>';
		html+= '	</div>';
		html+= '</td>';
		html+= '</tr>';
		return html;
	},	
	getScannerPingHTML: function(macAddr, data) {
		var style = (data.pingStatus == "U") ? "bg-success" : "bg-danger";
		var html = '';
		//html+= '<a onclick="dashboard.gotoAnchor(\'scanner-'+macAddr+'\');"><span id="scanner-ping-badge-'+macAddr+'" class="label mr5 '+style+'">'+data.scannerName+'</span></a>';
		html+= '<a href="###" onclick="dashboard.gotoAnchor(\'scanner-'+macAddr+'\');"><span id="scanner-ping-badge-'+macAddr+'" class="label mr5 '+style+'">'+data.scannerName+'</span></a>';
		return html;
	},
	gotoAnchor: function(anchorName) {
		location.href = '#' + anchorName;
		$(document).scrollTop($("#scanner-monitor-anchor").offset().top);
	},
	initScannerChartData: function(macAddr) {		
		var data = [];
		for(var i=0; i<this._scannerDataPoints; i++) {
			data.push([i, 0]);
		}
		//var data = [[0,Math.random()*(10-1)+1],[1,Math.random()*(10-1)+1],[2,Math.random()*(10-1)+1],[3,Math.random()*(10-1)+1],[4,Math.random()*(10-1)+1],[5,Math.random()*(10-1)+1],[6,Math.random()*(10-1)+1],[7,Math.random()*(10-1)+1],[8,Math.random()*(10-1)+1],[9,Math.random()*(10-1)+1]];
		return data;
	},
	setScannerChart: function(data) {
		if(data.length > 0) {
			for(var idx in data) {
				var macAddr = data[idx].macAddr.replace(/:/g, "");	
				var pingHtml = dashboard.getScannerPingHTML(macAddr, data[idx]);
				$("#scannerStatusList").append($(pingHtml));
				var chartHtml = dashboard.getScannerChartHtml(macAddr, data[idx]);
				$("#scannerStatusPanel > tbody").append($(chartHtml));
				
				this._scannerData[macAddr] = [];			
				this._scannerData[macAddr]["cpu"] = [];
				this._scannerData[macAddr]["mem"] = [];			
				this._scannerData[macAddr]["cpu"].push([0, data[idx].cpu]);
				this._scannerData[macAddr]["mem"].push([0, data[idx].mem]);
				this.initScannerChartData(macAddr);
	//			$("#scanner-updtime-"+macAddr).html(data[idx].updtime);
	//			$("#scanner-downdtime-"+macAddr).html(data[idx].downdtime);
	//			$("#scanner-moddtime-"+macAddr).html(data[idx].moddtime);			
				/*
				series: { shadowSize: 1 },
				lines: { show: true, lineWidth: 2, fill: true, fillColor: { colors: [ { opacity: 0.9 }, { opacity: 0.9 } ] }},
				yaxis: { min: 0, max: 100, tickFormatter: function (v) { return v + "%"; }, color: "rgba(255,255,255,0.8)"},
				xaxis: { show: false, color: "rgba(255,255,255,0.8)" },
				colors: ["rgba(255,255,255,0.95)"],
				grid: {	tickColor: "rgba(255,255,255,0.15)", borderWidth: 0}
				*/			
				this._scannerCPUChart[macAddr] = $.plot("#scanner-cpu-"+macAddr, [ this.initScannerChartData() ], {
					series: { shadowSize: 0 },
					lines: { show: true, lineWidth: 2, fill: true, fillColor: { colors: [ { opacity: 0.9 }, { opacity: 0.9 } ] }},
					yaxis: { min: 0, max: 100 },
					xaxis: { show: false }
				});		
				this._scannerMEMChart[macAddr] = $.plot("#scanner-mem-"+macAddr, [ this.initScannerChartData() ], {
					series: { shadowSize: 0 },
					lines: { show: true, lineWidth: 2, fill: true, fillColor: { colors: [ { opacity: 0.9 }, { opacity: 0.9 } ] }},
					yaxis: { min: 0, max: 100 },
					xaxis: { show: false }
				});	
			}
			(function() {
		        setInterval(function() {
		        	dashboard.getScannerStatus();
		        }, dashboard._scannerDataUpdateInterval)
		    })();
		} else {
			var html = '<tr><td>no data....</td></tr>';
			$("#scannerStatusList").append($(html));
			$("#scannerStatusPanel > tbody").append($(html));
			
		}
	}
};