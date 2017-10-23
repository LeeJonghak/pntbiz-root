pnt.module.create('tracking.map', function(module, param) {
	this.pntmap = new pnt.map.OfflineMap('map-canvas', {
		usetile: true
	});

	var areaManager = new pnt.map.AreaManager(this.pntmap);
	this.floorManager = new pnt.map.FloorManager(this.pntmap, {
		autofit: true
	});
	areaManager.setFloorManager(this.floorManager);

	var floorDataLoader = new pnt.util.DataLoader();
	floorDataLoader.addUrl('floorList', param.prop.get('url.floor'));
	floorDataLoader.addUrl('areaList', param.prop.get('url.area'));
	floorDataLoader.load(function(id, data, complete, error) {
		if(complete==true) {
			var floorData = this.getData('floorList').data;
			module.floorManager.load(floorData);
			module.floorManager.onChange(function(event) {

				/*var floorCondition = {floor: event.floor};
				 if(floorDataLoader.getData('areaList').result=='1') {
				 console.log('floorDataLoader', floorDataLoader.getData('areaList'))
				 var areaData = pnt.util.findArrayData(floorDataLoader.getData('areaList').data, floorCondition);
				 dataEvent.area.loadData(areaData);
				 }*/
			});

			/*var areaData = this.getData('areaList').data;
			if(typeof(areaData)!='undefined' && areaData!=null && areaData.length>0) {
				areaManager.load(areaData);
			} else {
				module.floorManager.changeDefault();
			}*/
			module.floorManager.changeDefault();
		}
	});


	var tracking = new pnt.map.Tracking(this.pntmap, this.floorManager, {
		speed: 10, // 재생속도
		maxAfterimage: 60*2, // 잔상갯수
		afterimageRandomColor: false,
		removeMarkerSec: 10, // 신호데이터가 없어도 마커를 유지할 횟수
		//showLabel: this.prop.get('map.debug'), // 마커 라벨 표시 여부 (재생시간, minorVer표시)
		showLabel: true,
		showMarker: true,
		period: 120, // tracking.period 이벤트 발생 주기
		timeController: true, // 재생시간 컨트롤러에 표시 여부
		afterimageColor:[229,76,118],
		afterimageWidth: 5
	});
	this.tracking = tracking;
	this.trackingParam = {comNum: param.prop.get('comNum')};
	this.playTrackingTimestamp = function(beaconNum, phoneNumber, startTimestamp, endTimestamp) {

		if(typeof(startTimestamp)=='string') {
			startTimestamp = parseInt(startTimestamp);
		}
		if(typeof(endTimestamp)=='string') {
			endTimestamp = parseInt(endTimestamp);
		}


		if(typeof(beaconNum)=='undefined' || beaconNum==null || beaconNum=='') {
			delete this.trackingParam.beaconNum;
		} else {
			this.trackingParam.beaconNum = beaconNum;
		}

		if(typeof(phoneNumber)=='undefined' || phoneNumber==null || phoneNumber=='') {
			delete this.trackingParam.phoneNumber;
		} else {
			this.trackingParam.phoneNumber = phoneNumber;
		}


		this.trackingParam.startRegDate = startTimestamp;
		this.trackingParam.endRegDate = startTimestamp+this.tracking.getPeriod();

		if(this.trackingParam.minorVer=='') {
			delete this.trackingParam['minorVer'];
		}

		this.tracking.clear();
		pnt.util.fetch({
			url: param.prop.get('url.tracking.log'),
			data: this.trackingParam,
			responseType:'json',
			success: pnt.util.bind(this.tracking, function(response) {
				this.loadData(response.data);

				if(typeof(endTimestamp)!='undefined') {
					this.play({startTime:startTimestamp, endTime:endTimestamp, period:this.getPeriod()});
				} else {
					this.play({startTime:startTimestamp, period:this.getPeriod()});
				}

				var createTd = function(text) {
					var td = document.createElement('td');
					td.innerHTML = text;
					return td;
				}

				var createTr = function(textArray) {
					var tr = document.createElement('tr');
					for(var i=0; i<textArray.length; i++) {
						var text = textArray[i];
						if(i==7) {
							try {
								var m = new moment(parseInt(text)*1000);
								text = m.format('YYYY-MM-DD HH:mm:ss')
							} catch(error) {
								console.error(error);
							}
						}

						var td = createTd(text||'');
						tr.appendChild(td);
					}
					return tr;
				}

				for(var i=0; i<response.data.length; i++) {
					var data = response.data[i];
					var textArray = [];
					var columns = param.prop.get('export.data.column');
					for(var cindex=0; cindex<columns.length; cindex++) {
						textArray.push(data[columns[cindex]]);
					}
					$('#presenceLogTable>tbody').append(createTr(textArray));
				}
				var cnt = $('#presenceLogTable>tbody>').length;
				$('#logBadge').html(cnt);
			})
		});

	}

	this.pntmap.on('tracking.period', pnt.util.bind(this, function(evt) {
		this.trackingParam.startRegDate = evt.time+evt.period;
		this.trackingParam.endRegDate = evt.time+evt.period+evt.period;

		pnt.util.fetch({
			url: param.prop.get('url.tracking.log'),
			data: this.trackingParam,
			responseType:'json',
			success: pnt.util.bind(evt.object, function(response) {
				this.loadData(response.data);
				var createTd = function(text) {
					var td = document.createElement('td');

					td.innerHTML = text;
					return td;
				}

				var createTr = function(textArray) {
					var tr = document.createElement('tr');
					for(var i=0; i<textArray.length; i++) {
						var text = textArray[i];
						if(i==7) {
							try {
								var m = new moment(parseInt(text)*1000);
								text = m.format('YYYY-MM-DD HH:mm:ss')
							} catch(error) {
								console.error(error);
							}
						}
						var td = createTd(text||'');
						tr.appendChild(td);
					}
					return tr;
				}

				for(var i=0; i<response.data.length; i++) {
					var data = response.data[i];
					var textArray = [];
					var columns = param.prop.get('export.data.column');
					for(var cindex=0; cindex<columns.length; cindex++) {
						textArray.push(data[columns[cindex]]);
					}
					$('#presenceLogTable>tbody').append(createTr(textArray));
				}
				var cnt = $('#presenceLogTable>tbody>').length;
				$('#logBadge').html(cnt);
			})
		});
	}));


	this.beaconMap = {};
	this.beaconList = function(query, process) {
		$.ajax({
			url: param.prop.get('url.beacon'),
			type: 'POST',
			data: 'opt=beaconName' + '&keyword=' + $('#beaconName').val(),
			success: function(data) {

				var result = {};
				if(typeof(data)=='string') {
					result = $.parseJSON(data);
				} else {
					result = data;
				}
				var list = [];

				for(var i=0; i<result.data.length; i++) {
					module.beaconMap[result.data[i].beaconName] = result.data[i].beaconNum;
					list.push(result.data[i].beaconName);
				}
				process(list);
			}
		});
	}

	this.exportCsv = function() {

		var iframe = document.getElementById('iframe-presence-log-table');
		if(iframe==null) {
			iframe = document.createElement('iframe');
			iframe.id = 'iframe-presence-log-table';
			$('body').append(iframe);
		}

		var iframeDocument = null;
		if(iframe.contentDocument) {
			iframeDocument = iframe.contentDocument;
		} else {
			iframeDocument = iframe.contentWindow.document;
		}
		$(iframeDocument.body).html('<table id="iframe-presence-log-table">'+$('#presenceLogTable').html()+'</table>');
		iframe.style.width = '0px';
		iframe.style.height = '0px';

		var filename = "target";
		/*var beaconName = $("#beaconName").val();
		var date1 = $("#sDate").val();
		var date2 = $("#eDate").val();
		var sDate = date1.substring(0,4) + date1.substring(5,7) +  date1.substring(8,10) + date1.substring(11,13) + date1.substring(14,16) + date1.substring(17,19);
		var eDate = date2.substring(0,4) + date2.substring(5,7) +  date2.substring(8,10) + date2.substring(11,13) + date2.substring(14,16) + date2.substring(17,19);

		filename = filename + "_" + beaconName + "_" + sDate + "_" + eDate;*/
		filename = filename + "_" + (new Date().getTime());
		$(iframeDocument.getElementById('iframe-presence-log-table')).tableExport({
			separator: ',',
			//ignoreColumn: [2,3],
			tableName:filename,
			type:'csv',
			//pdfFontSize:14,
			//pdfLeftMargin:20,
			escape:'false',
			htmlContent:'false',
			consoleLog:'true'
		});

	}
});
