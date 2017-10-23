var monitor = {
	listData : [],
	idArr: [],
	div:"",
	uuid:"",
	url:"",
    hostCheckTimeInterval: 10000,
	init: function(div){
		monitor.div = div;
		$.when(
		$(".trLoop").each(function(idx){
			monitor.idArr[idx] = $(this).attr("id").replace(/:/g, "");
		})).done(function() {
			monitor.chkStatus();
		});
		setInterval(monitor.chkStatus, monitor.hostCheckTimeInterval);
	},
	chkStatus: function(){
		$.ajax ({
	       url: (monitor.div == "scanner")? "/monitoring/scanner/info_ajax.do": "/monitoring/server/info_ajax.do"
	       , dataType: "json"
	       , data: {data:monitor.idArr}
	       , method: "post"
	       , success: function(data){
				if (typeof data == "string")
					data = JSON.parse(data);
	    	    monitor.checkHostResult(data);
	       }

	   });
	},
	checkHostResult: function(data){
		var id, tempId, td3;
		$(".trLoop").each(function(){
			id = $(this).attr("id");
			var newData = data.filter(function (value) {
				 return (value.id == id);
			});

			if(newData != null && newData.length >0){
				//console.log(newData[0]);

				monitor.setHost($(this).find("td:eq(0) .spHost"), monitor.div, newData[0]);
                monitor.setStatus($(this).find("td:eq(1)>span"), newData[0].pingStatus);
				td3 = $(this).find("td:eq(2)>span");
				$(td3).removeClass();
				$(td3).addClass("label");

				var idx = 0;
				if(monitor.div != "scanner"){
                	monitor.setServiceProc($(td3).eq(idx++), newData[0].procStatus, newData[0].process);
				}
				monitor.setServicePing($(td3).eq(idx++), newData[0].pingStatus);
				monitor.setServiceCPU($(td3).eq(idx++), newData[0].cpuStatus, newData[0].cpu);
				monitor.setServiceMem($(td3).eq(idx++), newData[0].memStatus, newData[0].mem);
				monitor.setServiceUptime($(td3).eq(idx++), newData[0].pingStatus, newData[0].uptime);
				monitor.setServiceDowntime($(td3).eq(idx++), newData[0].pingStatus, newData[0].downtime);
			}
		})
	},
	setHost: function(obj, div, data) {
		var hostTxt = (data.ip)? data.ip: "-";

		if(div =="scanner" && data.fwVer){
			hostTxt += " / " + data.fwVer;
		}
		$(obj).text(hostTxt);
	},
	setStatus: function(obj, type) {
		var txt, css;
		switch(type) {
			case "U" : txt ="UP"; css="status-up"; break;
			case "C" : txt ="DOWN"; css="status-down"; break;
			default: txt = "INIT"; css="status-init"; break;
		}
		$(obj).text(txt);
        $(obj).addClass(css);
	},
	setServicePing: function(obj, status, time) {
		$(obj).addClass(monitor.getStatusCss(status));
	},
	setServiceCPU: function(obj, status, cpu) {
		$(obj).addClass(monitor.getStatusCss(status));
		$(obj).find("span.spCpuVal").text(cpu);
	},
	setServiceMem: function(obj, status, mem) {
		$(obj).addClass(monitor.getStatusCss(status));
		$(obj).find("span.spMemVal").text(mem);
	},
	setServiceUptime: function(obj, status, uptime) {
		$(obj).addClass(monitor.getStatusCss(status));
		$(obj).find("span.spUpVal").text((uptime == 0)?uptime:common.time2str(uptime*1000));
	},
	setServiceDowntime: function(obj, status, downtime) {
		$(obj).addClass(monitor.getStatusCss(status));
		$(obj).find("span.spDownVal").text((downtime == 0)?downtime:common.time2str(downtime*1000));
	},
    setServiceProc: function(obj, status, proc) {
    	$(obj).addClass(monitor.getStatusCss(status));
        $(obj).find("span.spProcVal").text(proc);
    },
	getStatusCss: function(status){
		var rtnTxt;
		switch(status) {
			case "U" : rtnTxt = "success"; break;
			case "W" : rtnTxt = "warning"; break;
			case "C" : rtnTxt = "danger"; break;
			default: rtnTxt = "dark"; break;
		}
		return "bg-"+rtnTxt;
	}
}