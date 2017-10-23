'use strict'; 
/*! main.js - v0.1.1
* http://admindesigns.com/
* Copyright (c) 2015 Admin Designs;*/


/* Demo theme functions. Required for
 * Settings Pane and misc functions */
var highCharts = function () {

    // Define chart color patterns
    var highColors = [bgWarning, bgPrimary, bgInfo, bgAlert,
        bgDanger, bgSuccess, bgSystem, bgDark
    ];
    
    var highColors1 = [bgPrimary, bgWarning, bgInfo, bgAlert,
          bgDanger, bgSuccess, bgSystem, bgDark
    ];

	var _presenceGfList;
	var _lossBeChartList;
	var _data1;
	var _data2;
	
	 // Circle Graphs (주요 Zone별 진출입 횟수)
    var circleGraphs = function() {
        var infoCircle = $('.info-circle');
        if (infoCircle.length) {
            // Color Library we used to grab a random color
            var colors = {
                "primary": [bgPrimary, bgPrimaryLr,
                    bgPrimaryDr
                ],
                "info": [bgInfo, bgInfoLr, bgInfoDr],
                "warning": [bgWarning, bgWarningLr,
                    bgWarningDr
                ],
                "success": [bgSuccess, bgSuccessLr,
                    bgSuccessDr
                ],
                "alert": [bgAlert, bgAlertLr, bgAlertDr]
            };
            // Store all circles
            var circles = [];
            infoCircle.each(function(i, e) {
                // Define default color
                var color = ['#DDD', bgPrimary];
                // Modify color if user has defined one
                var targetColor = $(e).data(
                    'circle-color');
                if (targetColor) {
                    var color = ['#DDD', colors[
                        targetColor][0]]
                }
                // Create all circles
                var circle = Circles.create({
                    id: $(e).attr('id'),
                    value: $(e).attr('value'),
                    radius: $(e).width() / 2,
                    width: 30,
                    colors: color,
                    text: function(value) {
                        var title = $(e).attr('title');
                        if (title) {
                            return '<h2 class="circle-text-value">' + value + '</h2><p>' + title + '</p>' 
                        } 
                        else {
                            return '<h2 class="circle-text-value mb5">' + value + '</h2>'
                        }
                    }
                });
                circles.push(circle);
            });

            // Add debounced responsive functionality
            var rescale = function() { 
                infoCircle.each(function(i, e) {
                    var getWidth = $(e).width() / 2;
                    circles[i].updateRadius(
                        getWidth);
                });
                setTimeout(function() {
                    // Add responsive font sizing functionality
                    $('.info-circle').find('.circle-text-value').fitText(0.4);
                },50);
            } 
            var lazyLayout = _.debounce(rescale, 300);
            $(window).resize(lazyLayout);
          
        }

    } // End Circle Graphs Demo
    
    // High Charts
    var highCharts = function() {

    	// 오늘기준으로 일주일 날짜
        var dt		= new Date();
        var mDate	= [];
        var month	= dt.getMonth() + 1;
        var day		= dt.getDate();
        var year	= dt.getFullYear();

        for(var i=6; i>=0; i--) {
        	if(i == 6) {
        		mDate[i] = year + '-' + month + '-' + day ;
        	}else {
        		dt.setDate(dt.getDate() - 1);

                month = dt.getMonth()+1;
                day = dt.getDate();
                year = dt.getFullYear();

                mDate[i] = year + '-' + month + '-' + day ;
        	}
        }
            
        // Column Charts
    	// 망실 가능성이 높은 비콘 관제 현황 차트
        var highColumns = function() {
        	
	         var column3 = $('#high-column3');
       
	         var arrData = [0,0,0,0,0,0,0];
	        
	         if(_lossBeChartList != null) {
		         arrData[6] = parseInt(_lossBeChartList[0].chartCount1);
		         arrData[5] = parseInt(_lossBeChartList[0].chartCount2);
		         arrData[4] = parseInt(_lossBeChartList[0].chartCount3);
		         arrData[3] = parseInt(_lossBeChartList[0].chartCount4);
		         arrData[2] = parseInt(_lossBeChartList[0].chartCount5);
		         arrData[1] = parseInt(_lossBeChartList[0].chartCount6);
		         arrData[0] = parseInt(_lossBeChartList[0].chartCount7);

	         }

	         if (column3.length) {
	
	            // Column Chart3
	            $('#high-column3').highcharts({
	                credits: false,
	                colors: highColors,
	                chart: {
	                    type: 'column',
	                    padding: 0,
	                    spacingTop: 10,
	                    marginTop: 0,
	                    marginLeft: 30,
	                    marginRight: 30
	                },
	                legend: { 
	                    enabled: false
	                },
	                title: {
	                    text: '',
	                    style: {
	                        fontSize: 20,
	                        fontWeight: 600
	                    }
	                },
	                subtitle: {
	                    text: '',
	                    style: {
	                        color: '#AAA'
	                    }
	                },
	                xAxis: {
	                    lineWidth: 0,
	                    tickLength: 0,
	                    title: {
	                        text: null
	                    },
	                    labels: {
	                        enabled: true,
	                        formatter: function() {
	                        	var str = '';
	                        	if(this.value == 0) {
	                        		str = '1주일전';
	                        		
	                        	}else if(this.value == 1) {
	                        		str = '3일전';
	                        		
	                        	}else if(this.value == 2) {
	                        		str = '1일전';
	                        		
	                        	}else if(this.value == 3) {
	                        		str = '12시간전';
	                        		
	                        	}else if(this.value == 4) {
	                        		str = '6시간전';
	                        		
	                        	}else if(this.value == 5) {
	                        		str = '3시간전';
	                        		
	                        	}else if(this.value == 6) {
	                        		str = '1시간전';
	                        		
	                        	}
	                        	//this.value + 1;
	                            return str; // clean, unformatted number for year
	                        }
	                    }
	                },
	                yAxis: {
	                    gridLineWidth: 0,
	                    title: {
	                        text: null
	                    },
	                    labels: {
	                        enabled: false
	                    }
	                },
	                tooltip: {
	                    headerFormat: '<span style="font-size:10px"></span><table>',
	                    pointFormat: '<tr><td style="color:{series.color};padding:0"></td>' +
	                        '<td style="padding:0"><b>{point.y}</b></td></tr>',
	                    footerFormat: '</table>',
	                    shared: true,
	                    useHTML: true
	                },
	                plotOptions: {
	                    column: {
	                        colorByPoint: true,
	                        colors: [bgPrimary, bgPrimary,
	                            bgInfo, bgInfo
	                        ],
	                        groupPadding: 0,
	                        pointPadding: 0.24,
	                        borderWidth: 0
	                    }
	                },
	                series: [{
	                    name: '',
	                    data: arrData
	                }],
	                dataLabels: {
	                    enabled: true,
	                    rotation: 0,
	                    color: '#AAA',
	                    align: 'center',
	                    x: 0,
	                    y: -8
	                }
	            });
	        }
	
	    } // End High Columns
        
        var highLines = function() {
        	
        	// 스캐너 위치측위 현황
        	var line1 = $('#high-line');

            if (line1.length) {
            	var arrData = [1324, 3949, 2210, 3521, 5879, 1399, 2593]
                
            	if(_data1 != null) {
                    arrData[6] = _data1[0].chartCount1;
                    arrData[5] = _data1[0].chartCount2;
                    arrData[4] = _data1[0].chartCount3;
                    arrData[3] = _data1[0].chartCount4;
                    arrData[2] = _data1[0].chartCount5;
                    arrData[1] = _data1[0].chartCount6;
                    arrData[0] = _data1[0].chartCount7;
            	}

                // High Line 1
                $('#high-line').highcharts({
                    credits: false,
                    colors: highColors1,
                    chart: {
                        type: 'column',
                        zoomType: 'x',
                        panning: true,
                        panKey: 'shift',
                        marginRight: 50,
                        marginTop: -5
                    },
                    title: {
                        text: null
                    },
                    xAxis: {
                        gridLineColor: '#EEE',
                        lineColor: '#EEE',
                        tickColor: '#EEE',
                        categories: mDate
                
                    },
                    yAxis: {
                        min: 0,
                        tickInterval: 1000,
                        gridLineColor: '#EEE',
                        title: {
                            text: 'Traffic',
                            style: {
                                color: bgInfo,
                                fontWeight: '600'
                            }
                        }
                    },
                    plotOptions: {
                        spline: {
                            lineWidth: 3
                        },
                        area: {
                            fillOpacity: 0.2
                        }
                    },
                    legend: {
                        enabled: false
                    },
                    series: [{
                        name: '',
                        data: arrData
                    }]
                });

            }
                
            // 비콘 위치측위 현황
            var line1 = $('#high-line-1');
            
            if (line1.length) {
            	var arrData = [2654, 1543, 1432, 2431, 5329, 3249, 1293];
                
            	if(_data2 != null) {
                    arrData[6] = _data2[0].chartCount1;
                    arrData[5] = _data2[0].chartCount2;
                    arrData[4] = _data2[0].chartCount3;
                    arrData[3] = _data2[0].chartCount4;
                    arrData[2] = _data2[0].chartCount5;
                    arrData[1] = _data2[0].chartCount6;
                    arrData[0] = _data2[0].chartCount7;
            	}

                // High Line 1
                $('#high-line-1').highcharts({
                    credits: false,
                    colors: highColors,
                    chart: {
                        type: 'column',
                        zoomType: 'x',
                        panning: true,
                        panKey: 'shift',
                        marginRight: 50,
                        marginTop: -5
                    },
                    title: {
                        text: null
                    },
                    xAxis: {
                        gridLineColor: '#EEE',
                        lineColor: '#EEE',
                        tickColor: '#EEE',
                        categories: mDate
                    },
                    yAxis: {
                        min: 0,
                        tickInterval: 1000,
                        gridLineColor: '#EEE',
                        title: {
                            text: 'Traffic',
                            style: {
                                color: bgInfo,
                                fontWeight: '600'
                            }
                        }
                    },
                    plotOptions: {
                        spline: {
                            lineWidth: 3
                        },
                        area: {
                            fillOpacity: 0.2
                        }
                    },
                    legend: {
                        enabled: false
                    },
                    /*series: [{
                        name: '',
                        data: arrData
                    }]*/
                    series: [{
                        name: '',
                        data: arrData
                    }]
                });
            }
            
            // 마이크로펜스 관제 현황
            var line3 = $('#high-line3');
           
            if (line3.length) {
                var buttonText = '';
                
                var logArr = [];

                if(_presenceGfList.length > 0)  {
                	// fcNum 중복 체크
                    var fcNumArr = [];
                    for (var i = 0; i < _presenceGfList.length; i++) {
                    	fcNumArr[i] = _presenceGfList[i].fcNum;
                    }

                    var uniqueFcNum = fcNumArr.filter(function(itm,b,fcNumArr) {
                		return b == fcNumArr.indexOf(itm);
                	});
                     
                    for (var c = 0; c < uniqueFcNum.length; c++) {
                		var arrdata = [];
                        var fcName = '';
                         
                    	for( var d = 0; d < mDate.length; d++) {     
                    		var fcNumCnt = 0;
                    		for (var e = 0; e < _presenceGfList.length; e++) {
	                         	if(uniqueFcNum[c] == _presenceGfList[e].fcNum) {
	                         		if(mDate[d] == _presenceGfList[e].viewDate) {
	                         			fcNumCnt = parseInt(_presenceGfList[e].fcNumCnt); 
	                     			}
	                         		fcName = _presenceGfList[e].fcName;
	                    		}
                    		}
                    		arrdata.push(fcNumCnt);
                    	}

                		var re = {};
                 		re['name'] = fcName;
                     	re['data'] = arrdata;
                     	
                 		logArr.push(re);
                	 }
                     
                     for (var ab = 0; ab < uniqueFcNum.length; ab++) {
                     	buttonText =  buttonText + "<a type='button' data-chart-id="+ab+" class='legend-item btn btn-sm btn-primary mr10'></a>";
                     }

                }else {
                	// data값 0으로 세팅
                	var arrdata = [];
                	var fcName = '';
                	
                	for( var d = 0; d < mDate.length; d++) {     
                		var fcNumCnt = 0;
                		for (var e = 0; e < _presenceGfList.length; e++) {
                			if(uniqueFcNum[c] == _presenceGfList[e].fcNum) {
                				if(mDate[d] == _presenceGfList[e].viewDate) {
                					fcNumCnt = parseInt(_presenceGfList[e].fcNumCnt); 
                				}
                				fcName = _presenceGfList[e].fcName;
                			}
                		}
                		arrdata.push(fcNumCnt);
                	}
                	
                	var re = {};
                	re['name'] = fcName;
                	re['data'] = arrdata;
                     	
                	logArr.push(re);
                	
                	buttonText = "<div style='margin:33px;'></div>"
                }
 
                $('.div-high-line3').html(buttonText);
                
                // High Line 3
                $('#high-line3').highcharts({
                    credits: false,
                    colors: highColors,
                    chart: {
                        backgroundColor: '#f9f9f9',
                        className: 'br-r',
                        type: 'line',
                        zoomType: 'x',
                        panning: true,
                        panKey: 'shift',
                        marginTop: 25,
                        marginRight: 1
                    },
                    title: {
                        text: null
                    },
                    xAxis: {
                        gridLineColor: '#EEE',
                        lineColor: '#EEE',
                        tickColor: '#EEE',
                        categories: mDate
                    },
                    yAxis: {
                        min: 0,
                        tickInterval: 2,
                        gridLineColor: '#EEE',
                        title: {
                            text: null
                        }
                    },
                    plotOptions: {
                        spline: {
                            lineWidth: 3
                        },
                        area: {
                            fillOpacity: 0.2
                        }
                    },
                    legend: {
                        enabled: false
                    },
                    series: logArr
                });

            }

        } // End High Line Charts Demo

        // Init Chart Types
        highColumns();
        highLines();

    } // End Demo HighCharts

    
    // High Charts Demo
    var highChartMenus = function() {

       // Create custom menus for charts associated
       // with the ".chart-legend" element
       var chartLegend = $('.chart-legend');
             
        if (chartLegend.length) {

            $('.chart-legend').each(function(i, ele) {
                var legendID = $(ele).data('chart-id');
                $(ele).find('a.legend-item').each(function(
                    i, e) {
                    var This = $(e);
                    var itemID = This.data(
                        'chart-id');
                    // Use ID of menu to find what chart it belongs to
                    // Then use ID of its child menu items to find out what
                    // data on the chart it is connected to
                    var legend = $(legendID).highcharts()
                        .series[itemID];
                    // pull legend name from chart and populate menu buttons
                    var legendName = legend.name;
                    This.html(legendName);
                    // assign click handler which toggles legend data 
                    This.click(function(e) {
                        if (This.attr(
                            'href')) {
                            e.preventDefault();
                        }
                        if (legend.visible) {
                            legend.hide();
                            This.toggleClass(
                                'active'
                            );
                        } else {
                            legend.show();
                            This.toggleClass(
                                'active'
                            );
                        }
                    });
                });
            });
        }

        // Create custom menus for table charts
        var tableLegend = $('.table-legend');
             
        if (tableLegend.length) {

            $('.table-legend').each(function(i, e) {
                var legendID = $(e).data('chart-id');
                $(e).find('input.legend-switch').each(
                    function(i, e) {
                        var This = $(e);
                        var itemID = This.val();
                        // Use ID of menu to find what chart it belongs to
                        // Then use ID of its child menu items to find out what
                        // data on the chart it is connected to
                        var legend = $(legendID).highcharts()
                            .series[itemID];
                        // pull legend name from chart and populate menu buttons
                        var legendName = legend.name;
                        This.html(legendName);
                        // Toggle checkbox state based on series visability
                        if (legend.visible) {
                            This.attr('checked', true);
                        } else {
                            This.attr('checked', false);
                        }
                        // assign click handler which toggles legend data 
                        This.on('click', function(i, e) {
                            if (legend.visible) {
                                legend.hide();
                                This.attr(
                                    'checked',
                                    false);
                            } else {
                                legend.show();
                                This.attr(
                                    'checked',
                                    true);
                            }
                        });
                });
            });
        }

    } // End Demo HighChart Menus

	return {
        init: function (presenceGfList, lossBeChartList, data1, data2) {
        	_presenceGfList		= presenceGfList;
        	_lossBeChartList	= lossBeChartList;
        	
        	circleGraphs();
            highCharts();
            highChartMenus();
        }
	} 
}();



