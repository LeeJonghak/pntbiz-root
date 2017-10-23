var pnt = pnt || {};



pnt.map = pnt.map || {};
pnt.map.default = {};
pnt.map.default.zoom = 18;
pnt.map.default.usetile = true;
pnt.map.default.offline = false;
pnt.map.default.tileurl = '/tiles/{z}/{x}/{y}.png';
pnt.map.default.minZoom = 10;
pnt.map.default.maxZoom = 24;
pnt.map.default.center = [127.056710, 37.511114];
pnt.map.OfflineMap = function(elementId, options) {

    console.debug('pnt.map.OfflineMap constructor elementId:',elementId,' options:', JSON.stringify(options));

    if(typeof(options)=='undefined') {
        options = {};
    }

    this._elementId = elementId;
    this._options = options;

    /**
     *
     */
    this._mapControlElement = {};
    this._mapControlElement.left = document.createElement('div');
    this._mapControlElement.left.id = 'control-center-'+pnt.util.makeid(10);
    this._mapControlElement.left.className = 'ol-unselectable ol-control';
    this._mapControlElement.left.style.left = '3em';
    this._mapControlElement.left.style.top = '0.5em';

    this._mapControlElement.top = document.createElement('div');
    this._mapControlElement.top.id = 'control-top-'+pnt.util.makeid(10);
    this._mapControlElement.top.className = 'ol-unselectable ol-control';
    this._mapControlElement.top.style.right = '3em';
    this._mapControlElement.top.style.top = '0.5em';

    this._mapControlElement.right = document.createElement('div');
    this._mapControlElement.right.id = 'control-right-'+pnt.util.makeid(10);
    this._mapControlElement.right.className = 'ol-unselectable ol-control';
    this._mapControlElement.right.style.right = '3em';
    this._mapControlElement.right.style.top = '3em';
    this._mapControlElement.right.style.display = 'none';

    this._mapControlElement.button = document.createElement('div');
    this._mapControlElement.button.id = 'map-controls-button';
    this._mapControlElement.button.className = 'ol-unselectable ol-control';
    this._mapControlElement.button.style.right = '0.5em';
    this._mapControlElement.button.style.top = '3em';
    this._mapControlElement.button.style.display = 'none';


    var controls = [];
    if(typeof(options.control)=='undefined' || options.control!=false) {
        controls.push(new ol.control.Zoom());
		try {
        	controls.push(new ol.control.FullScreen());
		} catch(err) {
			console.error(err);
		}
        controls.push(new ol.control.ZoomSlider());
    } else {
        this._mapControlElement.left.style.display = 'none';
        this._mapControlElement.top.style.display = 'none';
        this._mapControlElement.right.style.display = 'none';
        this._mapControlElement.button.style.display = 'none';
    }

    controls.push(new ol.control.Control({element: this._mapControlElement.left}));
    controls.push(new ol.control.Control({element: this._mapControlElement.top}));
    controls.push(new ol.control.Control({element: this._mapControlElement.right}));
    controls.push(new ol.control.Control({element: this._mapControlElement.button}));


    if(typeof(options.controls)!='undefined') {
        for(var i=0; i<options.controls.length; i++) {
            controls[controls.length] = new ol.control.Control({
                element: document.getElementById(options.controls[i])
            });
        }
    }

    /**
     * OpenLayers3 ol.Map
     */
    this._pntmapDrag = new pnt.map.Drag(this);
	var center = ol.proj.transform(pnt.map.default.center, 'EPSG:4326', 'EPSG:3857');
	if(typeof(options.center)!='undefined') {
		center = ol.proj.transform(options.center, 'EPSG:4326', 'EPSG:3857');
	}
    var mapOption = {
        target: this._elementId,
        controls: controls,
        //overlays:[popupOverlay],
        view: new ol.View({
            //projection: 'EPSG:4326',
			projection: 'EPSG:3857',
            center: center,
            zoom: options.zoom || pnt.map.default.zoom,
            maxZoom: options.maxZoom || pnt.map.default.maxZoom,
            minZoom: options.minZoom || pnt.map.default.minZoom
        }),
		//renderer: 'webgl',
        interactions: ol.interaction.defaults({
            dragPan: options.dragPan==false?false:true,
            doubleClickZoom: options.doubleClickZoom==false?false:true,
            mouseWheelZoom: options.mouseWheelZoom==false?false:true
        }).extend([this._pntmapDrag])
    }

    this._olmap = new ol.Map(mapOption);
    this._readyrender = false;
    this._readyrenderCallbacks = [];
    this._olmap.once('postrender', pnt.util.bind(this, function() {
        this._readyrender = true;
        for(var i=0; i<this._readyrenderCallbacks.length; i++) {
            this._readyrenderCallbacks[i]();
        }
        this._readyrenderCallbacks = [];
    }));

    /*if((typeof(options.usetile)!='undefined'?options.usetile:pnt.map.default.usetile)==true) {
        /!**
         *
         *!/
        var tile = null;
        if((typeof(options.offline)!='undefined'?options.offline:pnt.map.default.offline)==true) {
            tile = new ol.layer.Tile({
                source: new ol.source.OSM({
                    url: options.tileurl || pnt.map.default.tileurl
                })
            });
        } else {
            tile = new ol.layer.Tile({
                source: new ol.source.OSM()
            });
        }
        this._olmap.addLayer(tile);
    }*/

	var tile = new ol.layer.Tile({
		source: new ol.source.OSM({
			url: 'http://mt{0-3}.google.com/vt/lyrs=m&x={x}&y={y}&z={z}',
			attributions: [
				new ol.Attribution({html: '© Google'}),
				new ol.Attribution({html: '<a href="https://developers.google.com/maps/terms">Terms of Use.</a>'})
			]
		})
	});
	this._olmap.addLayer(tile);

    this._objectManager = new pnt.map.ObjectManager(this);
    this._tooltip = new pnt.map.Tooltip(this);
    this._lineGenerator = new pnt.map.object.LineGenerator(this);
    this._polygonGenerator = new pnt.map.object.PolygonGenerator(this);

    /**
     * Cursor Label
     */
    this._cursorLabelText = null;
    this._cursorLabelElement = document.createElement('div');
    this._cursorLabelElement.id = 'pnt-map-cursor-label-'+pnt.util.makeid(5);
    this._cursorLabelElement.innerHTML = '';
    this._cursorLabelElement.style.display = 'none';
    this._cursorLabelElement.style.padding = '2px';
    this._cursorLabelElement.style.fontSize = '8pt';
    this._cursorLabelElement.style.color = '#ffffff';
    this._cursorLabelElement.style.backgroundColor = 'rgba(36, 40, 45, 0.7)';
    this._cursorLabelElement.style.border = 'solid 1px rgba(36, 40, 45, 1.00)';
    //options.submenuElement.style.boxShadow = '2px 2px 8px grey';
    this._cursorLabelElement.style.borderRadius = '3px';
    this._cursorLabelOverlay = new ol.Overlay({
        element: this._cursorLabelElement,
        positioning: 'top-left',
        offset: [13,-2],
        autoPan: true,
        autoPanAnimation: true
    });
    this._olmap.addOverlay(this._cursorLabelOverlay);

    /**
     * Context Menu
     */
    this._contextMenu = new pnt.map.ContextMenu(this);
    this.getOlMap().getViewport().addEventListener('contextmenu', pnt.util.bind(this, function(evt) {
        evt = evt?evt:window.event;

        if(evt.offsetX && evt.offsetY) {
            var feature = this.getOlMap().forEachFeatureAtPixel([evt.offsetX, evt.offsetY],
                function(feature, layer) {
                    return feature;
                });

            var coordinate = this.getOlMap().getCoordinateFromPixel([evt.offsetX, evt.offsetY]);
            if(typeof(feature)=='undefined' && evt.target.tagName=='CANVAS') {

                this._contextMenu.show(coordinate);
            } else if(typeof(feature)!='undefined') {
                var object = feature.get('object');
                if(typeof(object)!='undefined') {
                    this.getOlMap().dispatchEvent({
                        type: object.getObjectType()+'.rightclick',
                        coordinate: coordinate,
                        pixel: [evt.offsetX, evt.offsetY],
                        object: object,
                        objectType: object.getObjectType()
                    });
                }
            }
        }

        if (evt.preventDefault) evt.preventDefault();
        else return false;
    }));

    this.getOlMap().getViewport().addEventListener('mouseup', pnt.util.bind(this, function(evt) {
        evt = evt?evt:window.event;
        var coordinate = this.getOlMap().getCoordinateFromPixel([evt.offsetX, evt.offsetY]);
        evt.coordinate = coordinate;
        evt.type = 'pointerup';
        this.getOlMap().dispatchEvent({type:'pointerup', coordinate:coordinate});
    }));
    this.getOlMap().getView().on('change:resolution', pnt.util.bind(this, function(evt) {
        this._contextMenu.hide();
    }));
    this.on('moveend', function() {
        this._contextMenu.hide();
    });
    this.on('click', function(evt) {
        this._contextMenu.hide();

        if(evt.pixel) {
            var feature = this.getOlMap().forEachFeatureAtPixel(evt.pixel,
                function(feature, layer) {
                    return feature;
                });


            if(typeof(feature)!='undefined' &&
                !ol.events.condition.shiftKeyOnly(evt) ) {

                var coordinate = this.getOlMap().getCoordinateFromPixel(evt.pixel);
                var object = feature.get('object');
                if(typeof(object)!='undefined') {
                    this.getOlMap().dispatchEvent({
                        type: object.getObjectType()+'.click',
                        coordinate: coordinate,
                        pixel: evt.pixel,
                        object: object,
                        objectType: object.getObjectType()
                    });
                }
            }
        }
    });

    /**
     * Map Popup WIndow
     */
    this._popup = new pnt.map.Popup(this);

}
pnt.map.OfflineMap.prototype.getControlElement = function(id) {
    return this._mapControlElement[id];
}
pnt.map.OfflineMap.prototype.getObjectManager = function() {
    return this._objectManager;
}
pnt.map.OfflineMap.prototype.getLineGenerator = function() {
    return this._lineGenerator;
}
pnt.map.OfflineMap.prototype.getPolygonGenerator = function() {
    return this._polygonGenerator;
}
pnt.map.OfflineMap.prototype.getContextMenu = function() {
    return this._contextMenu;
}
pnt.map.OfflineMap.prototype.getTooltip = function() {
    return this._tooltip;
}
pnt.map.OfflineMap.prototype.getPopup = function() {
    return this._popup;
}
pnt.map.OfflineMap.prototype.getRootElement = function() {
	console.log('element', this._elementId, $('#'+this._elementId))
    return document.getElementById(this._elementId);
}
pnt.map.OfflineMap.prototype.getOlMap = function() {
    return this._olmap;
}
pnt.map.OfflineMap.prototype.setCenter = function(center) {
    console.debug('pnt.map.OfflineMap.setCenter center:', JSON.stringify(center));
    this._olmap.getView().setCenter(center);
}
pnt.map.OfflineMap.prototype.on = function(eventType, callback) {
    console.debug('pnt.map.OfflineMap.on eventType:',eventType);
    return this._olmap.on(eventType, callback, this);
}
pnt.map.OfflineMap.prototype.clear = function() {
    console.debug('pnt.map.OfflineMap.clear');
    this._objectManager.clear();
}
pnt.map.OfflineMap.prototype.setCursorLabel = function(text) {
    this._cursorLabelText = text;
    if(text!=null) {
        this._cursorLabelElement.innerHTML = text;
        this._cursorLabelElement.style.display = '';
    } else {
        this._cursorLabelElement.innerHTML = '';
        this._cursorLabelElement.style.display = 'none';
    }
}
pnt.map.OfflineMap.prototype._showCursorLabel = function() {
    if(this._cursorLabelText!=null) {
        this._cursorLabelElement.style.display = '';
    } else {
        this._cursorLabelElement.style.display = 'none';
    }
}
pnt.map.OfflineMap.prototype._hideCursorLabel = function() {
    this._cursorLabelElement.style.display = 'none';
}
pnt.map.OfflineMap.prototype.isReadyPostRender = function() {
    return this._readyrender;
}
pnt.map.OfflineMap.prototype.readyPostRender = function(callback) {
    if(this.isReadyPostRender()!=true) {
        this._readyrenderCallbacks.push(callback);
    } else {
        callback();
    }
}
// 1:nomal, 2:shift key(default)
pnt.map.OfflineMap.prototype.setDragMode = function(dragMode) {
    if(dragMode==2) {
        this._pntmapDrag.setShiftKey(true);
    } else {
        this._pntmapDrag.setShiftKey(false);
    }
}






pnt.map.Tooltip = function(offlineMap, options) {

    console.debug('pnt.map.Tooltip constructor');

    if(typeof(options)=='undefined') {
        options = {};
    }

    this._offlineMap = offlineMap;
    this._visibility = false;
    this._data = options.data || {};
    this._closeButtonClick = false;

    this._elementId = 'pnt-map-popup-'+pnt.util.makeid(10);

    this._element = {};
    this._element.root = document.createElement('div');
    this._element.root.id = this._elementId;
    this._element.root.classList.add('ol-popup');
    this._element.root.style.zIndex = '100';
    this._element.root.style.bottom = '12px';


    pnt.util.addEventListener(this._element.root, 'click', function(evt) {
        var event = {
            type: 'tooltip.click',
            eventType: 'click',
            objectType: 'tooltip',
            object: this
        };
        this._offlineMap.getOlMap().dispatchEvent(event);
    }, this);


    this._element.a = document.createElement('a');
    this._element.a.href = 'javascript:;';
    this._element.a.id = this._elementId+'-closer';
    this._element.a.classList.add('ol-popup-closer');

    this._element.content = document.createElement('div');
    this._element.content.id = this._elementId+'-content';
    this._element.content.style.fontSize = '12px';

    this._element.root.appendChild(this._element.a);
    this._element.root.appendChild(this._element.content);
    offlineMap.getRootElement().appendChild(this._element.root);

    this._element.arrow = document.createElement('div');
    this._element.arrow.innerHTML = ' ';
    this._element.arrow.style.top = '100%';
    this._element.arrow.style.border = 'solid transparent';
    this._element.arrow.style.height = '0px';
    this._element.arrow.style.width = '0px';
    this._element.arrow.style.position = 'absolute';
    this._element.arrow.style.pointerEvents = 'none';
    this._element.arrow.style.borderTopColor = 'white';
    this._element.arrow.style.borderWidth = '10px';
    this._element.arrow.style.left = '48px';
    this._element.arrow.style.marginLeft = '-10px';
    this._element.root.appendChild(this._element.arrow);

    var popupOverlay = new ol.Overlay(({
        element: this._element.root,
        autoPan: false,
        autoPanAnimation: {
            duration: 250
        }
    }));
    this._element.a.popup = this;
    this._element.a.onclick = function() {
        this.popup.hide(true);
        this.blur();
        return false;
    };
    this._overlay = popupOverlay;
    offlineMap.getOlMap().addOverlay(popupOverlay);
}
pnt.map.Tooltip.prototype.getOverlay = function() {
    return this._overlay;
}

pnt.map.Tooltip.prototype.setContent = function(text) {
    if(typeof(text)=='string') {
        this._element.content.innerHTML = text;
    } else {
        this._element.content.appendChild(text);
    }
}
pnt.map.Tooltip.prototype.getContent = function() {
	return this._element.content.innerHTML;
}
pnt.map.Tooltip.prototype.setZindex = function(index) {
    if(this._element.root) {
        this._element.root.style.zIndex = index;
    }
}
pnt.map.Tooltip.prototype.move = function(position) {
    if(this._visibility==true) {
        this._overlay.setPosition(position);
    }

    var mapSize = this._offlineMap.getOlMap().getSize();
    var pixel = this._offlineMap.getOlMap().getPixelFromCoordinate(position);
    var rootEl = this._element.root;
    var arrowEl = this._element.root.children[2];
    var leftRootLeft = -50;
    var leftArrowLeft = 48;
    var margin = 2;
    var right = pixel[0] + rootEl.offsetWidth + leftRootLeft + margin;
    if( mapSize[0] < right) {
        var ow = (right - mapSize[0]) + margin;
        if(ow>247) {
            ow = 247;
        }
        rootEl.style.left = (leftRootLeft - ow)+'px';
        arrowEl.style.left = (leftArrowLeft + ow)+'px';
    } else {
        rootEl.style.left = (leftRootLeft)+'px';
        arrowEl.style.left = (leftArrowLeft)+'px';
    }
    var top = pixel[1] - rootEl.offsetHeight - 14;;
    if(top<0) {

        rootEl.style.top = '12px';
        rootEl.style.bottom = '';
        arrowEl.style.top = '-20px';
        arrowEl.style.borderColor = 'transparent transparent white';

    } else {

        rootEl.style.top = '';
        rootEl.style.bottom = '12px';
        arrowEl.style.top = '100%'
        arrowEl.style.borderColor = 'white transparent transparent';

    }

}
pnt.map.Tooltip.prototype.show = function(position) {

    console.debug('pnt.map.Tooltip.show position:', position);

    this._closeButtonClick = false;

    if(typeof(position)!='undefined') {
        this._visibility = true;
        this.move(position);


        var event = {
            type: 'tooltip.show',
            eventType: 'show',
            objectType: 'tooltip',
            position: position,
            object: this
        };
        this._offlineMap.getOlMap().dispatchEvent(event);
    }
}
pnt.map.Tooltip.prototype.isCloseButtonClick = function() {
    return this._closeButtonClick;
}
pnt.map.Tooltip.prototype.hide = function(closeButton) {
    console.debug('pnt.map.Tooltip.hide');

    this._visibility = false;
    this._overlay.setPosition(undefined);
    if(closeButton==true) {
        this._closeButtonClick = true;
    }

    var event = {
        type: 'tooltip.hide',
        eventType: 'hide',
        objectType: 'tooltip',
        closeButton: typeof(closeButton)!='undefined' && closeButton==true?true:false,
        object: this
    };
    this._offlineMap.getOlMap().dispatchEvent(event);
}
pnt.map.Tooltip.prototype.getData = function(key) {
    if(typeof(key)=='undefined') {
        return this._data;
    } else {
        if (typeof(this._data[key]) != 'undefined') {
            return this._data[key];
        } else {
            return false;
        }
    }
}
pnt.map.Tooltip.prototype.setData = function(key, value) {

    this._data[key] = value;
}
pnt.map.Tooltip.prototype.remove = function() {
    if(this._element.root) {
        this._element.root.parentNode.removeChild(this._element.root);
        this._element.root = null;
    }
    this._offlineMap.getOlMap().removeOverlay(this._overlay);
}




pnt.map.ContextMenu = function(offlineMap) {

    console.debug('pnt.map.ContextMenu constructor');

    this._offlineMap = offlineMap;

    this._menuMap = {};
    this._elementId = 'pnt-map-context-menu-'+pnt.util.makeid(10);
    this._prevShowSubmenu = null;

    this._element = {};
    this._element.root = document.createElement('div');
    this._element.root.id = this._elementId;
    this._element.root.style.position = 'relative';
    this._element.root.style.paddingTop = '5px';
    this._element.root.style.paddingBottom = '5px';
    this._element.root.style.backgroundColor = 'rgba(36, 40, 45 ,1)';
    this._element.root.style.boxShadow = '2px 2px 8px grey';
    this._element.root.style.borderRadius = '5px';

    var popupOverlay = new ol.Overlay(({
        element: this._element.root,
        autoPan: false,
        positioning: 'top-left'
    }));
    this._overlay = popupOverlay;
    offlineMap.getOlMap().addOverlay(popupOverlay);


    this._defaultMenu = null;
}
pnt.map.ContextMenu.prototype.setDefaultMenu = function(menuInfo) {
    this._defaultMenu = menuInfo;
}
pnt.map.ContextMenu.prototype.getMenu = function(id) {
    return this._menuMap[id];
}
pnt.map.ContextMenu.prototype.registerMenuFunction = function(menu) {
    menu.setText = function(text) {
        this.element.children[0].innerHTML = text;
        this.text = text;
    };
    menu._checked = false;
    menu.setChecked = function(checked) {
        this._checked = true;
    }

}
pnt.map.ContextMenu.prototype.putMenu = function(options, menuid) {

    options._menu = this;
    if(typeof(options.id)=='undefined') {
        //options.id = pnt.util.makeid(20);
        options.id = menuid;
    }
    this.removeMenu(options.id);


    options.element = this._addMenu(options, options.callback);
    this._element.root.appendChild(options.element);

    if(typeof(options.submenu)!='undefined') {
        options.submenuElement = document.createElement('div');
        options.submenuElement.style.position = 'absolute';
        options.submenuElement.style.marginTop = '-30px';
        options.submenuElement.style.paddingTop = '5px';
        options.submenuElement.style.paddingBottom = '5px';
        options.submenuElement.style.backgroundColor = 'rgba(36, 40, 45, 1)';
        options.submenuElement.style.boxShadow = '2px 2px 8px grey';
        options.submenuElement.style.borderRadius = '5px';
        options.submenuElement.style.display = 'none';
        window.setTimeout(pnt.util.bind(options, function() {
            this.submenuElement.style.left = (this.element.offsetWidth-3)+'px';
        }),200);
        for(var id in options.submenu) {

            if(typeof(options.submenu[id].id)=='undefined') {
                //options.id = pnt.util.makeid(20);
                options.submenu[id].id = id;
            }
            this.removeMenu(options.submenu[id].id);

            options.submenu[id].element = this._addMenu(options.submenu[id], options.submenu[id].callback);
            options.submenu[id].element.style.whiteSpace = 'nowrap';
            options.submenuElement.appendChild(options.submenu[id].element);

			/*options.submenu[id].setText = pnt.util.bind(options.submenu[id], function(text) {
			 this.element.children[0].innerHTML = text;
			 this.text = text;
			 });*/
            this.registerMenuFunction(options.submenu[id]);

            this._menuMap[options.submenu[id].id] = options.submenu[id];
        }
        this._element.root.appendChild(options.submenuElement);

        /**
         * 서브메뉴 레이어 표시 이벤트
         */
        var mouseOverEventHandler = function(evt)  {

            this.submenuElement.style.display = '';
            this._visibilitySubmenu = true;

            if(this._menu._prevShowSubmenu!=null && this._menu._prevShowSubmenu.id!=this.id) {
                this._menu._prevShowSubmenu.submenuElement.style.display = 'none';
            }
            this._menu._prevShowSubmenu = this;
        }
        var mouseOutEventHandler = function(evt)  {
            this._visibilitySubmenu = false;
            if(typeof(this._waiting)=='undefined' || this._waiting!=true) {
                this._waiting = true;
                window.setTimeout(pnt.util.bind(this, function() {
                    this._waiting = false;
                    if(this._visibilitySubmenu!=true) {
                        this._visibilitySubmenu = false;
                        this.submenuElement.style.display = 'none';
                    }
                }), 300);
            }
        }
        pnt.util.addEventListener(options.element, 'mouseover', mouseOverEventHandler, options);
        pnt.util.addEventListener(options.element, 'mouseout', mouseOutEventHandler, options);
        pnt.util.addEventListener(options.submenuElement, 'mouseover', mouseOverEventHandler, options);
        pnt.util.addEventListener(options.submenuElement, 'mouseout', mouseOutEventHandler, options);
    }

	/*options.setText = pnt.util.bind(options, function(text) {
	 this.element.children[0].innerHTML = text;
	 this.text = text;
	 });*/
    this.registerMenuFunction(options);


    this._menuMap[options.id] = options;

    return options.id;
}
pnt.map.ContextMenu.prototype.removeMenu = function(id) {
    if(typeof(this._menuMap[id])!='undefined') {
        if(typeof(this._menuMap[id].submenu)!='undefined') {
            //this._menuMap[id].submenuElement.remove();
            if(this._menuMap[id].submenuElement.parentNode) {
                this._menuMap[id].submenuElement.parentNode.removeChild(this._menuMap[id].submenuElement);
            }
        }
        //this._menuMap[id].element.remove();
        if(this._menuMap[id].element.parentNode) {
            this._menuMap[id].element.parentNode.removeChild(this._menuMap[id].element);
        }
        delete this._menuMap[id];
    }
}
pnt.map.ContextMenu.prototype.clearMenu = function() {
    for(var id in this._menuMap) {
        //this._menuMap[id].element.remove();
        if(this._menuMap[id].element.parentNode) {
            this._menuMap[id].element.parentNode.removeChild(this._menuMap[id].element);
        }
    }
}
pnt.map.ContextMenu.prototype._addMenu = function(option, callback) {
    var menu = document.createElement('div');
    //menu.innerHTML = option.text;
    menu.style.color = 'rgb(220, 220, 220)';
    menu.style.fontSize = '9pt';
    menu.style.padding = '6px';
    menu.style.background = 'rgba(61, 58, 57, 0.7)';
    menu.dataset.menuid = option.id;

    var menuName = document.createElement('span');
    menuName.innerHTML = option.text;
    menuName.style.marginRight = '20px';
    //menuName.style.color = 'rgb(230, 230, 230)';
    menuName.style.fontSize = '9pt';
    menuName.style.whiteSpace = 'nowrap';
    menuName.dataset.menuid = option.id;
    menu.appendChild(menuName);

    pnt.util.addEventListener(menu, 'mouseover', function(evt)  {
        this.style.color = 'rgb(255,255,255)';
        //this.style.backgroundColor = 'rgb(218, 235, 243)';
    });
    pnt.util.addEventListener(menu, 'mouseout', function(evt)  {
        this.style.color = 'rgb(220,220,220)';
        //this.style.backgroundColor = 'rgb(255, 255, 255)';
    });

    if(typeof(option.submenu)!='undefined') {
        var arrow = document.createElement('div');
        arrow.style.float = 'right';
        //arrow.style.color = 'rgb(200, 200, 200)';
        arrow.style.fontSize = '7pt';
        arrow.innerHTML = '▶︎';
        menu.appendChild(arrow);
        menu.style.cursor = 'default';

    } else {
        menu.style.cursor = 'pointer';
        pnt.util.addEventListener(menu, 'click', pnt.util.bind(this, function(evt) {
            var menuid = evt.target.dataset.menuid;
            var menu = this.getMenu(menuid);
            menu.callback.call(this, menu, evt);
            this.hide();

            if (typeof evt.stopPropagation == "function") {
                evt.stopPropagation();
            } else {
                evt.cancelBubble = true;
            }
        }));
    }


    return menu;
}
pnt.map.ContextMenu.prototype.show = function(position, menuInfo) {

    this._offlineMap._hideCursorLabel();

    this.clearMenu();

    if(typeof(menuInfo)!='undefined') {
        for(var menuid in menuInfo) {
            var options = menuInfo[menuid];
            this.putMenu(options, menuid);
        }
    } else {
        for(var menuid in this._defaultMenu) {
            var options = this._defaultMenu[menuid];
            this.putMenu(options, menuid);
        }
    }



    console.debug('pnt.map.ContextMenu.show position:', position);
    this._position = position;
    if(typeof(position)!='undefined') {
        this._element.root.focus();
        this._overlay.setPosition(position);
    }

    /**
     * 메뉴가 지도를 벗어나지 않도록 처리
     */
    var pixel = this._offlineMap.getOlMap().getPixelFromCoordinate(position);
    var mapSize = this._offlineMap.getOlMap().getSize();
    var menuSize = [this._element.root.offsetWidth, this._element.root.offsetHeight];
    var positioning = ['top','left'];
    if(pixel[0]+menuSize[0]>mapSize[0]) {
        positioning[1] = 'right';
    }
    if(pixel[1]+menuSize[1]>mapSize[1]) {
        positioning[0] = 'bottom';
    }
    this._overlay.setPositioning(positioning[0]+'-'+positioning[1]);

}
pnt.map.ContextMenu.prototype.hide = function() {
    this._offlineMap._showCursorLabel();

    console.debug('pnt.map.ContextMenu.hide');
    this._overlay.setPosition(undefined);
}
pnt.map.ContextMenu.prototype.getPosition = function() {
    return this._position;
}


pnt.map.Popup = function(offlineMap) {

    console.debug('pnt.map.Popup constructor');

    this._offlineMap = offlineMap;
    this._visible = false;
    this._elementId = 'pnt-map-popup-'+pnt.util.makeid(10);

    this._element = {};
    this._element.root = document.createElement('div');
    this._element.root.id = this._elementId;
    this._element.root.style.top = '4em';
    this._element.root.style.right = '3em';
    this._element.root.style.width = '400px';
    this._element.root.style.display = 'none';
    this._element.root.classList.add('ol-control');
    this._element.root.classList.add('ol-unselectable');

    this._element.frame = document.createElement('div');
    this._element.frame.style.paddingTop = '5px';
    this._element.frame.style.paddingBottom = '5px';
    this._element.frame.style.backgroundColor = 'rgba(255,255,255,1)';
    this._element.frame.style.boxShadow = '2px 2px 8px grey';
    this._element.frame.style.borderRadius = '5px';
    this._element.frame.style.boxShadow = '2px 2px 8px grey';
    this._element.root.appendChild(this._element.frame);


    /**
     * Top
     */
    this._element.top = document.createElement('div');
    this._element.top.style.padding = '8px';
    this._element.top.style.borderBottom = '1px solid rgb(240, 240, 240)';
    this._element.top.innerHTML = 'Popup';
    this._element.frame.appendChild(this._element.top);

    this._element.closer = document.createElement('a');
    this._element.closer.href = 'javascript:;';
    this._element.closer.style.textDecoration = 'none';
    this._element.closer.style.position = 'absolute';
    this._element.closer.style.top = '8px';
    this._element.closer.style.right = '12px';
    this._element.closer.innerHTML = 'x';
    pnt.util.addEventListener(this._element.closer, 'click', pnt.util.bind(this, function() {
        this.hide();
    }));
    this._element.frame.appendChild(this._element.closer);

    /**
     * Body
     */
    this._element.body = document.createElement('div');
    this._element.body.style.maxHeight = '400px';
    this._element.body.style.overflowY = 'auto';
    this._element.body.style.overflowX = 'hidden';
    this._element.body.style.padding = '8px';
    this._element.frame.appendChild(this._element.body);

    /**
     * Bottom
     */
    this._element.bottom = document.createElement('div');
    this._element.bottom.style.borderTop = '1px solid rgb(240, 240, 240)';
    this._element.bottom.style.padding = '8px'
    this._element.bottom.style.textAlign = 'center';
    this._element.btnClose = document.createElement('input');
    this._element.btnClose.type = 'button';
    this._element.btnClose.value = 'close';
    pnt.util.addEventListener(this._element.btnClose, 'click', pnt.util.bind(this, function() {
        this.hide();
    }));
    //this._element.bottom.appendChild(this._element.btnClose);
    this._element.frame.appendChild(this._element.bottom);
    //this._element.frame.innerHTML = 'text popup';

    var control = new ol.control.Control({
        element: this._element.root
    });
    this._offlineMap.getOlMap().addControl(control);
}
pnt.map.Popup.prototype.isShown = function() {
    return this._visible;
}
pnt.map.Popup.prototype.title = function(title) {
    this._element.top.innerHTML = title;
}
pnt.map.Popup.prototype.body = function(element) {
    this._element.body.innerHTML = '';
    if(typeof(element)!='undefined') {
        this._element.body.appendChild(element);
    }
}
pnt.map.Popup.prototype.foot = function(element) {
    this._element.bottom.innerHTML = '';
    if(typeof(element)!='undefined') {
        this._element.bottom.appendChild(element);
    }
}
pnt.map.Popup.prototype.setBodyFoot = function(json) {
    if(typeof(json.body)!='undefined') {
        this.body(json.body);
    } else {
        this.body();
    }
    if(typeof(json.foot)!='undefined') {
        this.foot(json.foot);
    } else {
        this.foot();
    }
}
pnt.map.Popup.prototype.show = function(element) {
    if(typeof(element)!='undefined') {
        this._element.body.innerHTML = '';
        this._element.body.appendChild(element);
    }
    this._element.root.style.display = '';
    this._visible = true;

    return this;
}
pnt.map.Popup.prototype.hide = function() {
    this._element.root.style.display = 'none';
    this._visible = false;
}




pnt.map.object = pnt.map.object || {};
pnt.map.object.type = {};
pnt.map.object.type.MARKER = 'marker';
pnt.map.object.type.VMARKER = 'vmarker';
pnt.map.object.type.IMAGE = 'image';
pnt.map.object.type.LABEL = 'label';
pnt.map.object.type.POLYGON = 'polygon';
pnt.map.object.type.CIRCLE = 'circle';
pnt.map.object.type.LINE = 'line';
pnt.map.object.default = {};
pnt.map.object.default.markerurl = 'data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAoAAAAKCAYAAACNMs+9AAAABGdBTUEAAK/INwWK6QAAABl0RVh0U29mdHdhcmUAQWRvYmUgSW1hZ2VSZWFkeXHJZTwAAAGxSURBVHjaACkA1v8B2uTyAPLz+gDO0+1S//4AYfLz+zL5+/4BBAYEzw0LBJ0+NhWuCAgFAAIAKQDW/wT6+/4Ay8/rgRMPCX4ZGAwA7/f+AOTs+QDY4PQZ2NHuSt/p+YZmVCB7AgApANb/BNPX7lEMDAd+FxsOANXk+ADS5foA0Ob5AN7q/AD08QQAv9brehwYCc0CiOX529+pskBFx+9+Zzj74g/DyVdAE3+wMfzjYWNgYAHi3wwMa898SgMIIJYnb34bfGP7y3DmzX+Gkx8ZGZ6zsTMw8LEyMDAyMDB8B7JZmBguvPukBxBALI8//GG4x/CL4fwvdoYX7EAJkEmsIFVAwPGXgeHPfwYWoLEAAcTExMl26diTPwzPGFgZ/vNzAk0DKuZig2BukPXMDGZqPBcBAojJSV9gxqsvf4F6mBkY2FmApjGDJRmYgfgf0GSm/wzh+twzAAKIyduYb0OhE18z0/cfDAzvgfjbHwaGH0ArP/5kYP76naHUkLXZQ5V9I0AAMT5+9YXh/38Ghq03f/rufvEv7shbBgsmZkYGa1GmE54yTIs85Fk2/wM6FyDAANuRm7HdZI07AAAAAElFTkSuQmCC';
pnt.map.object.event = {};
pnt.map.object.event.EVENT_NAME = 'pntcustom';
pnt.map.ObjectManager = function(offlineMap) {

    console.debug('pnt.map.ObjectManager constructor');

    this._offlineMap = offlineMap;
    this._objects = {};
    this._events = {};
    this._tags = {};
    this._tagKeys = {};
    this._dragStartMarker = null;
    this._dragging = false;
    this._dragStartPositionString = null;

    /**
     * Layer �̺�Ʈ ó��
     */
    var callback = pnt.util.bind(this, function(evt) {
        var feature = this._offlineMap.getOlMap().forEachFeatureAtPixel(evt.pixel, function(feature, layer) {
            return feature;
        });
        if(typeof(feature)!='undefined') {
            var object = feature.get('object');
            if(typeof(object)!='undefined') {
                if(object.getObjectType()==pnt.map.object.type.POLYGON) {
                    object.dispatchEvent(evt.type, evt);
                }
            }
        }
    });
    this._offlineMap.getOlMap().on(['click'], callback);

    /**
     * ��Ŀ(Overlay) �巡ũ ó��
     */
	/*this._offlineMap.getOlMap().getViewport().addEventListener('mousemove', pnt.util.bind(this, function(evt) {
	 if(this._dragStartMarker!=null) {
	 console.log('mousemove', this, evt.offsetX , evt.offsetY, evt);

	 if(evt.offsetX && evt.offsetY & evt.target.nodeName=='CANVAS') {


	 var coordinate = this._offlineMap.getOlMap().getCoordinateFromPixel([evt.offsetX, evt.offsetY]);
	 this._dragStartMarker.move(coordinate);
	 }
	 }
	 }));*/


    this._offlineMap.on('pointermove', pnt.util.bind(this, function(evt) {
        //console.log('pointermove', evt)
        if(this._dragStartMarker!=null) {
            this._dragStartMarker.move(evt.coordinate);
        }

        if(this._offlineMap._cursorLabelElement.style.display == '') {
            this._offlineMap._cursorLabelOverlay.setPosition(evt.coordinate);
        }

		/*if (evt.dragging) {
		 return;
		 }

		 var feature = this._offlineMap.getOlMap().forEachFeatureAtPixel(evt.pixel, function(feature, layer) {
		 return feature;
		 });
		 if(typeof(feature)!='undefined') {
		 var object = feature.get('object');
		 if(typeof(object)!='undefined' && (typeof(this._mouseoverObject)=='undefined' || this._mouseoverObject!=null)) {


		 this._offlineMap.getOlMap().dispatchEvent({
		 type: object.getObjectType()+'.mouseover',
		 coordinate: evt.coordinate,
		 pixel: evt.pixel,
		 object: object,
		 objectType: object.getObjectType()
		 });
		 this._mouseoverObject = object;

		 }
		 } else if(typeof(this._mouseoverObject)!='undefined' && this._mouseoverObject!=null) {
		 this._offlineMap.getOlMap().dispatchEvent({
		 type: this._mouseoverObject.getObjectType()+'.mouseout',
		 coordinate: evt.coordinate,
		 pixel: evt.pixel,
		 object: this._mouseoverObject,
		 objectType: this._mouseoverObject.getObjectType()
		 });
		 this._mouseoverObject = null;
		 }*/
    }));
    this._offlineMap.on('pointerup', pnt.util.bind(this, function(evt) {
        if(this._dragStartMarker!=null) {
            //var coordinate = this._offlineMap.getOlMap().getCoordinateFromPixel([evt.offsetX, evt.offsetY]);
            this.endDragMarker();
        }
    }));

	/*this._offlineMap.getOlMap().getViewport().addEventListener('mouseup', pnt.util.bind(this, function(evt) {
	 if(this._dragStartMarker!=null) {
	 //var coordinate = this._offlineMap.getOlMap().getCoordinateFromPixel([evt.offsetX, evt.offsetY]);
	 pnt.util.dispatchEvent(this._dragStartMarker.getElement(), 'dragend', {position:this._dragStartMarker.getPosition()});
	 this.endDragMarker();
	 }
	 }));*/


}
pnt.map.ObjectManager.prototype.setOfflineMap = function(offlineMap) {
    this._offlineMap = offlineMap;
}
pnt.map.ObjectManager.prototype.startDragMarker = function(marker) {

    var position = marker.getPosition();
    this._dragStartPositionString = position[0].toString()+position[1];
    this._dragStartMarker = marker;
    this._dragging = true;
}
pnt.map.ObjectManager.prototype.endDragMarker = function() {
    if(this._dragStartMarker!=null) {
        var position = this._dragStartMarker.getPosition();
        var positionString = position[0].toString()+position[1];
        if(this._dragStartPositionString!=positionString) {
            window.setTimeout(pnt.util.bind(this, function() {
                this._dragging = false;
            }),300);
            pnt.util.dispatchEvent(this._dragStartMarker.getElement(), 'dragend', {position:this._dragStartMarker.getPosition()});
        } else {
            this._dragging = false;
        }
        this._dragStartMarker = null;
    }

}
pnt.map.ObjectManager.prototype.isDragging = function() {
    return this._dragging;
}
pnt.map.ObjectManager.prototype.create = function(objectType, id, options) {

    console.debug('pnt.map.ObjectManager.create objectType:',objectType, ' id:',id,' options:', options);

    if(typeof(options)=='undefined') {
        options = {};
    }
    var object = null;

    if(objectType==pnt.map.object.type.MARKER) {
        if(typeof(this._objects[pnt.map.object.type.MARKER])=='undefined') {
            this._objects[pnt.map.object.type.MARKER] = {};
        }
        if(typeof(this._objects[pnt.map.object.type.MARKER][id])=='undefined') {
            object = new pnt.map.object.Marker(this._offlineMap, id, options);
            if(typeof(options.label)!='undefined') {
                if(typeof(options.labelStyle)=='undefined') {
                    options.labelStyle = {};
                }
                object.showLabel(options.label, options.labelStyle);
            }
            this._objects[pnt.map.object.type.MARKER][id] = object;

            if(typeof(this._events[objectType])!='undefined'
                && Object.keys(this._events[objectType]).length>0) {

                for(var eventType in this._events[objectType]) {
                    object.onGlobalEvent(eventType);
                }
            }

            //
            object.on('mousedown', pnt.util.bind(object, function(evt) {
                if(this.getDraggable()==true && evt.which==1) {
                    this._offlineMap.getObjectManager().startDragMarker(this);
                }
            }));
            object.on('mouseup', pnt.util.bind(object, function(evt) {
                this._offlineMap.getObjectManager().endDragMarker();
            }));

        }
    }
    else if(objectType==pnt.map.object.type.VMARKER) {
        if(typeof(this._objects[pnt.map.object.type.VMARKER])=='undefined') {
            this._objects[pnt.map.object.type.VMARKER] = {};
        }
        if(typeof(this._objects[pnt.map.object.type.VMARKER][id])=='undefined') {
            object = new pnt.map.object.VectorMarker(this._offlineMap, id, options);
            if(typeof(options.label)!='undefined') {
                if(typeof(options.labelStyle)=='undefined') {
                    options.labelStyle = {};
                }
                object.showLabel(options.label, options.labelStyle);
            }
            this._objects[pnt.map.object.type.VMARKER][id] = object;

            if(typeof(this._events[objectType])!='undefined'
                && Object.keys(this._events[objectType]).length>0) {

                for(var eventType in this._events[objectType]) {
                    object.onGlobalEvent(eventType);
                }
            }

            //
            object.on('mousedown', pnt.util.bind(object, function(evt) {
                if(this.getDraggable()==true && evt.which==1) {
                    this._offlineMap.getObjectManager().startDragMarker(this);
                }
            }));
            object.on('mouseup', pnt.util.bind(object, function(evt) {
                this._offlineMap.getObjectManager().endDragMarker();
            }));

        }
    }
    else if(objectType==pnt.map.object.type.IMAGE) {
        if(typeof(this._objects[pnt.map.object.type.IMAGE])=='undefined') {
            this._objects[pnt.map.object.type.IMAGE] = {};
        }
        if(typeof(this._objects[pnt.map.object.type.IMAGE][id])=='undefined') {
            object = new pnt.map.object.Image(this._offlineMap, id, options);
            this._objects[pnt.map.object.type.IMAGE][id] = object;

            if(typeof(this._events[objectType])!='undefined'
                && Object.keys(this._events[objectType]).length>0) {

                for(var eventType in this._events[objectType]) {
                    object.onGlobalEvent(eventType);
                }
            }
        }
    }
    else if(objectType==pnt.map.object.type.POLYGON || objectType==pnt.map.object.type.CIRCLE) {

        if(typeof(this._objects[objectType])=='undefined') {
            this._objects[objectType] = {};
        }
        if(typeof(this._objects[objectType][id])=='undefined') {

            if(objectType==pnt.map.object.type.POLYGON) {
                object = new pnt.map.object.Polygon(this._offlineMap, id, options);
            }
            else if(objectType==pnt.map.object.type.CIRCLE) {
                object = new pnt.map.object.Circle(this._offlineMap, id, options);
            }

            if(typeof(options.label)!='undefined') {
                if(typeof(options.labelStyle)=='undefined') {
                    options.labelStyle = {};
                }
                object.showLabel(options.label, options.labelStyle);
            }
            this._objects[objectType][id] = object;

            if(typeof(this._events[objectType])!='undefined'
                && Object.keys(this._events[objectType]).length>0) {

                for(var eventType in this._events[objectType]) {
                    object.onGlobalEvent(eventType);
                }
            }
        }
    }
    else if(objectType==pnt.map.object.type.LINE) {
        if(typeof(this._objects[pnt.map.object.type.LINE])=='undefined') {
            this._objects[pnt.map.object.type.LINE] = {};
        }
        if(typeof(this._objects[pnt.map.object.type.LINE][id])=='undefined') {
            object = new pnt.map.object.Line(this._offlineMap, id, options);
            this._objects[pnt.map.object.type.LINE][id] = object;

            if(typeof(this._events[objectType])!='undefined'
                && Object.keys(this._events[objectType]).length>0) {

                for(var eventType in this._events[objectType]) {
                    object.onGlobalEvent(eventType);
                }
            }
        }
    }
    else if(objectType==pnt.map.object.type.LABEL) {

    }

    /**
     * Object TAG
     */
    if(object!=null && typeof(options.tag)!='undefined') {
        var arrtag = options.tag.split(',');
        this._tagKeys[id] = [];
        var tags = [];
        for(var i=0; i<arrtag.length; i++) {
            var tag = arrtag[i].trim();
            tags.push(tag);
            this._tagKeys[id].push(tag);
            if(typeof(this._tags[tag])=='undefined') {
                this._tags[tag] = [];
            }
            this._tags[tag][this._tags[tag].length] = object;
        }
        object._tags = tags;
    }


    return object;
}


pnt.map.ObjectManager.prototype.drawIterativeSimpleMarker = function(latlngCallback, milliseconds) {

    var object = {
        objectManager: this,
        latlngCallback: latlngCallback,
        milliseconds: milliseconds,
        intervalListener: null,
        markerId: 'simple-marker-'+pnt.util.makeid(10),
        remove: function() {
            window.setTimeout(pnt.util.bind(this, function() {
                window.clearInterval(this.intervalListener);
                this.intervalListener = null;
                var marker = this.objectManager.get(pnt.map.object.type.MARKER, this.markerId);
                if(marker) {
                    this.objectManager.remove(pnt.map.object.type.MARKER, this.markerId);
                }
            }), 1000);
        }
    }

    object.intervalListener = window.setInterval(pnt.util.bind(object, function() {
        this.latlngCallback(pnt.util.bind(this, function(coordinate) {

            if(this.intervalListener!=null) {
                var marker = this.objectManager.get(pnt.map.object.type.MARKER, this.markerId);
                if(marker) {
                    marker.move(coordinate);
                } else {
                    var options = {
                        position: coordinate,
                        tag: 'simple-marker'
                    };
                    this.objectManager.create(pnt.map.object.type.MARKER, this.markerId, options);
                }
            }
        }));
    }), milliseconds);

    return object;
}
pnt.map.ObjectManager.prototype.remove = function(objectType, id) {

    console.debug('pnt.map.ObjectManager.remove objectType:',objectType, ' id:',id);

    if(typeof(this._objects[objectType])!='undefined' && typeof(this._objects[objectType][id])!='undefined') {
        var object = this._objects[objectType][id];

        if(typeof(this._tagKeys[object.getId()])!='undefined') {
            for(var i=0; i<this._tagKeys[object.getId()].length; i++) {
                var tag = this._tagKeys[object.getId()][i];
                for(var j=0; j<this._tags[tag].length; j++) {
                    if(this._tags[tag][j].getId()==object.getId()) {
                        this._tags[tag].splice(j,1);
                    }
                }

            }
        }
        delete this._tagKeys[this._id];

        if(object) {
            object.remove();
        }
        delete this._objects[objectType][id];
    }

}
pnt.map.ObjectManager.prototype.on = function(objectType, eventType, callback) {

    console.debug('pnt.map.ObjectManager.on objectType:',objectType, ' eventType:',eventType);

    if(typeof(this._events[objectType])=='undefined') {
        this._events[objectType] = {};
    }
    if(typeof(this._events[objectType][eventType])=='undefined') {
        this._events[objectType][eventType] = {};
    }

    var eventid = pnt.util.makeid(20);
    this._events[objectType][eventType][eventid]
        = this._offlineMap.getOlMap().on(objectType+'.'+eventType, callback, this);

    if(typeof(this._objects[objectType])!='undefined' &&
        Object.keys(this._objects[objectType]).length>0 ) {

        for(var objectId in this._objects[objectType]) {
            this._objects[objectType][objectId].onGlobalEvent(eventType);
        }
    }

    return eventid;
}

pnt.map.ObjectManager.prototype.un = function(objectType, eventType, eventid) {

    console.debug('pnt.map.ObjectManager.on objectType:',objectType, ' eventType:',eventType, ' eventid:', eventid);

    if(typeof(this._events[objectType][eventType][eventid])!='undefined') {
        this._offlineMap.getOlMap().unByKey(this._events[objectType][eventType][eventid]);
        delete this._events[objectType][eventType][eventid];

        if(typeof(this._events[objectType])=='undefined'
            || Object.keys(this._events[objectType]).length<=0) {

            if(typeof(this._objects[objectType])!='undefined' &&
                Object.keys(this._objects[objectType]).length>0 ) {

                for(var objectId in this._objects[objectType]) {
                    this._objects[objectType][objectId].unGlobalEvent(eventType);
                }
            }
        }
    }
}
pnt.map.ObjectManager.prototype.each = function(objectType, callback) {

    console.debug('pnt.map.ObjectManager.each objectType:',objectType);

    if(typeof(this._objects[objectType])!='undefined' &&
        Object.keys(this._objects[objectType]).length>0 ) {

        for(var id in this._objects[objectType]) {
            callback(this._objects[objectType][id]);
        }
    }
}
pnt.map.ObjectManager.prototype.get = function(objectType, objectId) {

    if(typeof(this._objects[objectType])!='undefined') {
        var object = this._objects[objectType][objectId];
        if(typeof(object)!='undefined') {
            return object;
        } else {
            return false;
        }
    } else {
        return false;
    }
}
pnt.map.ObjectManager.prototype.find = function(objectType, keyword, callback) {

    console.debug('pnt.map.ObjectManager.find objectType:',objectType, ' keyword:', keyword);

    var objects = [];
    this.each(objectType, function(object) {

        if(typeof(keyword)=='undefined' && typeof(callback)=='undefined') {
            objects.push(object);
        }
        else if(typeof(keyword)=='function' && typeof(callback)=='undefined') {
            objects.push(object);
            keyword.call(this, object);
        } else {
            if(object.containKeyword(keyword)==true) {
                if(typeof(callback)=='undefined') {
                    objects.push(object);
                } else {
                    objects.push(object);
                    callback.call(this, object);
                }
            }
        }
    });

    if(objects.length>0) {
        return objects;
    } else {
        return false;
    }
}
pnt.map.ObjectManager.prototype.findTag = function(tag, callback) {

    console.debug('pnt.map.ObjectManager.findTag tag:',tag);

    var arrtag = tag.split(',');
    var objIdMap = {};
    var object = [];
    for(var i=0; i<arrtag.length; i++) {
        var tag = arrtag[i].trim();

        console.debug('pnt.map.ObjectManager.findTag tag:'+tag+' ', this._tags[tag]);
        if(typeof(this._tags[tag])!='undefined') {
            var length = this._tags[tag].length;
            for(var j=0; j<length; j++) {
                if(typeof(this._tags[tag][j])!='undefined') {
                    var objid = this._tags[tag][j].getId();
                    if(typeof(objIdMap[objid])=='undefined') {
                        objIdMap[objid] = 1;
                        if(typeof(callback)=='undefined') {
                            object.push(this._tags[tag][j]);
                        } else {
                            console.debug('pnt.map.ObjectManager.findTag callback:', this._tags[tag][j]);
                            callback.call(this, this._tags[tag][j]);
                        }

                    } else {
                        objIdMap[objid]++;
                    }
                }
            }
        }
    }

    if(typeof(callback)=='undefined') {
        console.debug('pnt.map.ObjectManager.findTag result:', object);
        return object;
    }
}
pnt.map.ObjectManager.prototype.clear = function(objectType) {

    console.debug('pnt.map.ObjectManager.clear objectType:',objectType);

    if(typeof(objectType)=='string'
        && typeof(this._objects[objectType])!='undefined'
        && Object.keys(this._objects[objectType]).length>0) {

        for(var id in this._objects[objectType]) {
            this.remove(objectType, id);
        }
    } else {
        for(var objectType in this._objects) {
            for(var id in this._objects[objectType]) {
                this.remove(objectType, id);
            }
        }
    }
}

pnt.map.ObjectManager.prototype.clearTag = function(tag) {
    console.debug('pnt.map.ObjectManager.clearTag tag:',tag);

    var objects = this.findTag(tag);
    for(var i=0; i<objects.length; i++) {
        this.remove(objects[i].getObjectType(), objects[i].getId());
    }
}


pnt.map.object.Marker = function(offlineMap, id, options) {

    console.debug('pnt.map.object.Marker constructor id:', this._id, ' options:', options);

    this._id = id;
    this._objectType = pnt.map.object.type.MARKER;
    this._elementId = 'pntmap-'+pnt.map.object.type.MARKER+'-'+id;
    this._position = options.position;
    this._offlineMap = offlineMap;
    this._events = {};
    this._globalEventId = {};
    this._labelOverlay = null;
    this._visible = true;
    this._data = options.data || {};
    this._keyword = [];
    this._draggable = false;
    this._tooltip = null;
    this._timerMap = {};

    this._focusStyle = {
        'border':'2px solid red',
        'borderRadius':'6px'
    }

    this._unfocusStyle = {
        'border':''
    }

    if(typeof(options.label)!='undefined') {
        this._keyword[this._keyword.length] = options.label;
    }

    if(typeof(options.timer)!='undefined') {
        this.setTimer(options.timer);
    }

    if(typeof(options.keyword)!='undefined') {
        var arrkey = options.keyword.split(',');
        for(var i=0; i<arrkey.length; i++) {
            var keyword = typeof(arrkey[i])=='string'?arrkey[i].trim():arrkey[i];
            this._keyword[this._keyword.length] = keyword;
        }
    }

    this._element = document.createElement('img');
    this._element.id = this._elementId;
    this._element.style.zIndex = '90';
    this._element.style.verticalAlign = 'middle';
    this._element.style.transitionTimingFunction = 'left, top';
    this._element.style.transitionProperty = 'linear';
    this._element.style.transitionDuration = '0.25s';
    this._element.src = options.url || pnt.map.object.default.markerurl;
    this._element.draggable = false;
    this._element.onerror = function() {
        this.src = pnt.map.object.default.markerurl;
        this.style.width = '10px';
        this.style.height = '10px';
    }
    this._element.onclick = pnt.util.bind(this, function() {
        var event = {
            type: pnt.map.object.type.MARKER+'.click',
            eventType: 'click',
            objectType: pnt.map.object.type.MARKER,
            object: this
        };
        this._offlineMap.getOlMap().dispatchEvent(event);
    });

    if(typeof(options.style)!='undefined') {
        for(var key in options.style) {
            this._element.style[key] = options.style[key];
        }
    }

    this._overlay = new ol.Overlay({
        element: this._element,
        positioning: 'center-center',
        position: this._position,
        autoPan: false
    });
    offlineMap.getOlMap().addOverlay(this._overlay);

    if(typeof(options.autoPan)=='undefined' || options.autoPan != false) {
        this._element.parentElement.style.transitionDuration = '0.25s';
    }
}
pnt.map.object.Marker.prototype.getObjectType = function() {
    return this._objectType;
}
pnt.map.object.Marker.prototype.getElement = function() {
    return this._element;
}
pnt.map.object.Marker.prototype.containKeyword = function(keyword) {
    for(var i=0; i<this._keyword.length; i++) {
        if(this._keyword[i]==keyword) {
            return true;
        }
    }
}
pnt.map.object.Marker.prototype.getLabelElement = function() {
    var element = document.getElementById(this._elementId+'-label');
    return element;
}
pnt.map.object.Marker.prototype.setLabelText = function(text) {
    var element = this.getLabelElement();
    element.innerHTML = text;
}
pnt.map.object.Marker.prototype.showLabel = function(text, style) {

    if(typeof(style)=='undefined') {
        style = {};
    }

    if(this._labelOverlay==null) {
        var elementId = this._elementId+'-label';
        var textElement = document.createElement('div');
        textElement.id = elementId;
        //textElement.className = 'marker-label';
        textElement.style.fontSize = style.fontSize || '7pt';
        textElement.style.backgroundColor = style.backgroundColor ||  'rgba(255, 255, 255, 0.8)';
        textElement.style.color = style.color || '#000000';
        textElement.style.marginTop = style.marginTop || '0px';
        textElement.innerHTML = text;
        textElement.draggable = false;
        this._offlineMap.getRootElement().appendChild(textElement);

        this._labelOverlay =  new ol.Overlay({
            element: textElement,
            offset: [0,12],
            positioning: 'center-center',
            position: this._position,
            autoPan: false
        });
        this._offlineMap.getOlMap().addOverlay(this._labelOverlay);
    }
}
pnt.map.object.Marker.prototype.getId = function() {
    return this._id;
}
pnt.map.object.Marker.prototype.getData = function(key) {
    if(typeof(key)=='undefined') {
        return this._data;
    } else {
        if (typeof(this._data[key]) != 'undefined') {
            return this._data[key];
        } else {
            return false;
        }
    }
}
pnt.map.object.Marker.prototype.setData = function(key, value) {

    this._data[key] = value;
}
pnt.map.object.Marker.prototype.move = function(position) {
    this._position = position;
    this._overlay.setPosition(position);
    if(this._labelOverlay!=null) {
        this._labelOverlay.setPosition(position);
    }

    if(this._tooltip!=null) {
        this._tooltip.move(position);
    }
}
pnt.map.object.Marker.prototype.onGlobalEvent = function(eventType) {
    if(typeof(this._globalEventId[eventType])=='undefined') {
        this.on(eventType, function(event) {
            this._globalEventId[eventType] = pnt.util.makeid(20);
            var event = {
                type: pnt.map.object.type.MARKER+'.'+eventType,
                eventType: eventType,
                objectType: pnt.map.object.type.MARKER,
                object: this
            };
            this._offlineMap.getOlMap().dispatchEvent(event);
        });
    }
}
pnt.map.object.Marker.prototype.unGlobalEvent = function(eventType) {
    if(typeof(this._globalEventId[eventType])!='undefined') {
        this.un(this._globalEventId[eventType]);
        delete this._globalEventId[eventType];
    }
}
pnt.map.object.Marker.prototype.on = function(eventType, callback) {
    var eventid = pnt.util.makeid(20);

    this._events[eventid] = {
        eventType: eventType,
        listener: pnt.util.bind(this, callback)
    };

    var element = document.getElementById(this._elementId);
    pnt.util.addEventListener(element, eventType, this._events[eventid].listener);

    return eventid;
}
pnt.map.object.Marker.prototype.un = function(eventid) {

    if(typeof(this._events[eventid])!='undefined') {
        var event = this._events[eventid];

        var element = document.getElementById(this._elementId);
        pnt.util.removeEventListener(element, event.eventType, event.listener);
        delete this._events[eventid];
    }
}
pnt.map.object.Marker.prototype.remove = function() {

    console.debug('pnt.map.object.Marker.remove id:', this._id);

    var el = document.getElementById(this._elementId);
    if(el) {
        el.parentElement.removeChild(el);
        this._offlineMap.getOlMap().removeLayer(this._overlay);
        if(this._labelOverlay!=null) {
            var el2 = document.getElementById(this._elementId+'-label');
            if(el2) {
                el2.parentElement.removeChild(el2);
                this._offlineMap.getOlMap().removeLayer(this._labelOverlay);
            }
        }
    }

    // delete tooltip
    if(this._tooltip!=null) {
        this._tooltip.remove();
        this._tooltip = null;
    }
}
pnt.map.object.Marker.prototype.getPosition = function() {
    return this._position;
}
pnt.map.object.Marker.prototype.getVisible = function() {
    return this._visible;
}
pnt.map.object.Marker.prototype.show = function() {
    this._visible = true;
    this._overlay.setPosition(this.getPosition());
    if(this._labelOverlay!=null) {
        var el = document.getElementById(this._elementId+'-label');
        if(el) {
            el.style.display = '';
        }
    }
}
pnt.map.object.Marker.prototype.hide = function() {
    this._visible = false;
    this._overlay.setPosition(undefined);
    if(this._labelOverlay!=null) {
        var el = document.getElementById(this._elementId+'-label');
        if(el) {
            el.style.display = 'none';
        }
    }
}
pnt.map.object.Marker.prototype.setDraggable = function(draggable) {
    this._draggable = draggable;
}
pnt.map.object.Marker.prototype.getDraggable = function() {
    return this._draggable;
}
pnt.map.object.Marker.prototype.getTooltip = function() {
    if(this._tooltip==null) {
        this._tooltip = new pnt.map.Tooltip(this._offlineMap);
        this._tooltip.setData('object', this);
    }
    return this._tooltip;
}
pnt.map.object.Marker.prototype.showTooltip = function() {
    this.getTooltip().show(this._position);
}
pnt.map.object.Marker.prototype.hideTooltip = function() {
    this.getTooltip().hide();
}
pnt.map.object.Marker.prototype.changeIcon = function(url) {
    this._element.src = url;
}
pnt.map.object.Marker.prototype.focus = function() {

    var focus = this._overlay.get('focus');
    if(typeof(focus)!='undefined' && focus!=true) {
        this._overlay.set('focus', true);
        var element = this._overlay.getElement();

        for(var key in this._focusStyle) {
            element.style[key] = this._focusStyle[key];
        }
    }

}
pnt.map.object.Marker.prototype.unfocus = function() {

    this._overlay.set('focus', false);
    var element = this._overlay.getElement();

    for(var key in this._unfocusStyle) {
        element.style[key] = this._unfocusStyle[key];
    }

}
pnt.map.object.Marker.prototype.containsTag = function(tag) {
    for(var i=0; i<this._tags.length; i++) {
        if(this._tags[i]==tag) {
            return true;
        }
    }
    return false;
}
pnt.map.object.Marker.prototype.setTimer = function(id, milliseconds) {

    this.removeTimer(id);

    this._timerMap[id] = window.setTimeout(pnt.util.bind(this, function() {
        var event = {
            type: pnt.map.object.type.MARKER+'.'+'timer',
            eventType: 'timer',
            objectType: pnt.map.object.type.MARKER,
            object: this,
            timerId: id,
            milliseconds: milliseconds
        };
        this._offlineMap.getOlMap().dispatchEvent(event);
        this._timer = null;
    }), milliseconds);
}
pnt.map.object.Marker.prototype.removeTimer = function(id) {
    if(typeof(this._timerMap[id])!='undefined' && this._timerMap[id]!=null) {
        window.clearTimeout(this._timerMap[id]);
    }
}
pnt.map.object.Marker.prototype.setFocusStyle = function(style) {
    this._focusStyle = style;
}
pnt.map.object.Marker.prototype.setUnfocusStyle = function(style) {
    this._unfocusStyle = style;
}





pnt.map.object.VectorMarker = function(offlineMap, id, options) {

    console.debug('pnt.map.object.VectorMarker constructor id:', id, ' options:', options);

    this._id = id;
    this._objectType = pnt.map.object.type.VMARKER;
    this._elementId = 'pntmap-'+pnt.map.object.type.VMARKER+'-'+id;
    this._position = options.position;
    this._offlineMap = offlineMap;
    this._events = {};
    this._globalEventId = {};
    this._labelOverlay = null;
    this._data = options.data || {};
    this._keyword = [];
    this._draggable = false;
    this._tags;

    if(typeof(options.label)!='undefined') {
        this._keyword[this._keyword.length] = options.label;
    }

    if(typeof(options.keyword)!='undefined') {
        var arrkey = options.keyword.split(',');
        for(var i=0; i<arrkey.length; i++) {
            var keyword = typeof(arrkey[i])=='string'?arrkey[i].trim():arrkey[i];
            this._keyword[this._keyword.length] = keyword;
        }
    }

    this._feature = new ol.Feature({
        geometry: new ol.geom.Point(this._position),
        labelPoint: new ol.geom.Point(this._position)
    });

    this._feature.set('object', this);
    this._feature.set('objectType', this._objectType);
    this._styleJson = {};
    this._styleJson.image = new ol.style.Icon(({
        anchorOrigin: options.anchorOrigin || 'top-left',
        anchor: options.anchor || [5, 5],
        anchorXUnits: 'pixels',
        anchorYUnits: 'pixels',
        opacity: 1,
        src: options.url || pnt.map.object.default.markerurl
    }));
    this._feature.setStyle(new ol.style.Style(this._styleJson));

    var vectorSource = new ol.source.Vector();
    vectorSource.addFeature(this._feature);
    this._layer = new ol.layer.Vector({
        source: vectorSource,
        zIndex: 800
    });

    this._offlineMap.getOlMap().addLayer(this._layer);
}
pnt.map.object.VectorMarker.prototype.setTag = function(tags) {
    this._tags = tags;
}
pnt.map.object.VectorMarker.prototype.getTag = function() {
    return this._tags;
}
pnt.map.object.VectorMarker.prototype.containsTag = function(tag) {
    for(var i=0; i<this._tags.length; i++) {
        if(this._tags[i]==tag) {
            return true;
        }
    }
    return false;
}
pnt.map.object.VectorMarker.prototype.getObjectType = function() {
    return this._objectType;
}
pnt.map.object.VectorMarker.prototype.getElement = function() {
    return this._element;
}
pnt.map.object.VectorMarker.prototype.containKeyword = function(keyword) {
    for(var i=0; i<this._keyword.length; i++) {
        if(this._keyword[i]==keyword) {
            return true;
        }
    }
}
pnt.map.object.VectorMarker.prototype.setLabelText = function(text) {
	/*var element = document.getElementById(this._elementId+'-label');
	 element.innerHTML = text;*/
    this.showLabel(text);
}
pnt.map.object.VectorMarker.prototype.showLabel = function(text, style) {
    var text = new ol.style.Text({
        textAlign: 'center',
        textBaseline: 'middle',
        //font: 'normal 12px Courier New',
        font: 'normal 8pt Gulim',
        text: text,
        fill: new ol.style.Fill({color: 'rgba(0, 0, 0, 1)'}),
        stroke: new ol.style.Stroke({color: 'rgba(255, 255, 255, 0.4)', width: 4}),
        offsetX: 5,
        offsetY: 15,
        rotation: 0
    });
    this._styleJson.text = text;
    this._feature.setStyle(new ol.style.Style(this._styleJson));
}
pnt.map.object.VectorMarker.prototype.getId = function() {
    return this._id;
}
pnt.map.object.VectorMarker.prototype.getData = function(key) {
    if(typeof(key)=='undefined') {
        return this._data;
    } else {
        if (typeof(this._data[key]) != 'undefined') {
            return this._data[key];
        } else {
            return false;
        }
    }
}
pnt.map.object.VectorMarker.prototype.setData = function(key, value) {

    this._data[key] = value;
}
pnt.map.object.VectorMarker.prototype.move = function(position) {
    this._position = position;
    this._feature.setGeometry(new ol.geom.Point(this._position));
}
pnt.map.object.VectorMarker.prototype.changeIcon = function(url) {
    this._styleJson.image = new ol.style.Icon(({
        anchorOrigin: options.anchorOrigin || 'top-left',
        anchor: options.anchor || [5, 5],
        anchorXUnits: 'pixels',
        anchorYUnits: 'pixels',
        opacity: 1,
        src: url || pnt.map.object.default.markerurl
    }));
    this._feature.setStyle(new ol.style.Style(this._styleJson));
}
pnt.map.object.VectorMarker.prototype.onGlobalEvent = function(eventType) {
    if(typeof(this._globalEventId[eventType])=='undefined') {
        this.on(eventType, function(event) {
            this._globalEventId[eventType] = pnt.util.makeid(20);
            var event = {
                type: pnt.map.object.type.VMARKER+'.'+eventType,
                eventType: eventType,
                objectType: pnt.map.object.type.VMARKER,
                object: this
            };
            this._offlineMap.getOlMap().dispatchEvent(event);
        });
    }
}
pnt.map.object.VectorMarker.prototype.unGlobalEvent = function(eventType) {
    if(typeof(this._globalEventId[eventType])!='undefined') {
        this.un(this._globalEventId[eventType]);
        delete this._globalEventId[eventType];
    }
}
pnt.map.object.VectorMarker.prototype.on = function(eventType, callback) {
    var eventid = pnt.util.makeid(20);

    this._events[eventid] = {
        eventType: eventType,
        listener: pnt.util.bind(this, callback)
    };

    return eventid;
}
pnt.map.object.VectorMarker.prototype.un = function(eventid) {

    if(typeof(this._events[eventid])!='undefined') {
        var event = this._events[eventid];

        delete this._events[eventid];
    }
}
pnt.map.object.VectorMarker.prototype.dispatchEvent = function(eventType, evt) {
    for(var eventId in this._events) {
        var eventInfo = this._events[eventId];
        if(eventInfo.eventType==eventType) {
            eventInfo.listener(evt);
        }
    }
}
pnt.map.object.VectorMarker.prototype.remove = function() {

    console.debug('pnt.map.object.VectorMarker.remove id:', this._id);

    this._offlineMap.getOlMap().removeLayer(this._layer);
}
pnt.map.object.VectorMarker.prototype.getPosition = function() {
    return this._feature.getGeometry().getFirstCoordinate();
}
pnt.map.object.VectorMarker.prototype.show = function() {
    this._layer.setVisible(true);
    if(this._labelOverlay!=null) {
        var el = document.getElementById('pntmap-'+pnt.map.object.type.POLYGON+'-'+this._id+'-label');
        if(el) {
            el.style.display = '';
        }
    }
}
pnt.map.object.VectorMarker.prototype.hide = function() {
    this._layer.setVisible(false);
    if(this._labelOverlay!=null) {
        var el = document.getElementById('pntmap-'+pnt.map.object.type.POLYGON+'-'+this._id+'-label');
        if(el) {
            el.style.display = 'none';
        }
    }
}
pnt.map.object.VectorMarker.prototype.setDraggable = function(draggable) {
    this._feature.set('draggable', draggable);
}
pnt.map.object.VectorMarker.prototype.enableVmc = function(enable) {
    this._feature.set('draggable-vmc', enable);
}


pnt.map.object.Label = function() {

}

pnt.map.object.Line = function(offlineMap, id, options) {

    console.debug('pnt.map.object.Line constructor id:', id, ' options:', options);

    this._id = id;
    this._objectType = pnt.map.object.type.LINE;
    this.coords = options.coords;
    this._offlineMap = offlineMap;
    this._events = {};
    this._globalEventId = {};
    this._keyword = [];
    this._data = options.data || {};
    this._labelOverlay = null;
    this._center = pnt.util.centroidOfPolygon(options.coords);

    if(typeof(options.keyword)!='undefined') {
        var arrkey = options.keyword.split(',');
        for(var i=0; i<arrkey.length; i++) {
            var keyword = typeof(arrkey[i])=='string'?arrkey[i].trim():arrkey[i];
            this._keyword[this._keyword.length] = keyword;
        }
    }

    this._lineString = new ol.geom.LineString(options.coords);
    this._feature = new ol.Feature({
        geometry: this._lineString
    });

    this.styleJson = {};
    if(typeof(options.stroke)!='undefined') {
        this.styleJson.stroke = new ol.style.Stroke(options.stroke);
    }
    var style = new ol.style.Style(this.styleJson);
    this._feature.setStyle(style);

    this._vectorSource = new ol.source.Vector();
    this._vectorSource.addFeature(this._feature);


    this._layer = new ol.layer.Vector({
        source: this._vectorSource,
        zIndex: 600
    });

    this._offlineMap.getOlMap().addLayer(this._layer);
}
pnt.map.object.Line.prototype.setStyle = function(strokeStyle) {
    this.styleJson.stroke = new ol.style.Stroke(strokeStyle);
    var style = new ol.style.Style(this.styleJson);
    this._feature.setStyle(style);
}
pnt.map.object.Line.prototype.getFeature = function() {
    return this._feature;
}
pnt.map.object.Line.prototype.getObjectType = function() {
    return this._objectType;
}
pnt.map.object.Line.prototype.getId = function() {
    return this._id;
}
pnt.map.object.Line.prototype.getData = function(key) {
    if(typeof(key)=='undefined') {
        return this._data;
    } else {
        if (typeof(this._data[key]) != 'undefined') {
            return this._data[key];
        } else {
            return false;
        }
    }
}
pnt.map.object.Line.prototype.setPosition = function(startPosition, endPosition) {
    this._lineString.setCoordinates([startPosition, endPosition]);
}
pnt.map.object.Line.prototype.remove = function() {
    console.debug('pnt.map.object.Line.remove id:', this._id);

    this._offlineMap.getOlMap().removeLayer(this._layer);
}
pnt.map.object.Line.prototype.show = function() {
    this._layer.setVisible(true);
}
pnt.map.object.Line.prototype.hide = function() {
    this._layer.setVisible(false);
}


pnt.map.object.Image = function(offlineMap, id, options) {

    console.debug('pnt.map.object.Image constructor id:', id, ' options:', JSON.stringify(options));

    this._id = id;
    this._objectType = pnt.map.object.type.IMAGE;
    this._elementId = 'pntmap-'+pnt.map.object.type.IMAGE+'-'+id;
    this._extent = options.extent;
    this._offlineMap = offlineMap;
    this._events = {};
    this._globalEventId = {};
    this._keyword = [];
    this._data = options.data || {};
    this._imageUrl = options.url;
    this._angle = options.angle || 0;
    this._scale = 1;
    this._opacity = 1;

    if(typeof(options.keyword)!='undefined') {
        var arrkey = options.keyword.split(',');
        for(var i=0; i<arrkey.length; i++) {
            var keyword = typeof(arrkey[i])=='string'?arrkey[i].trim():arrkey[i];
            this._keyword[this._keyword.length] = keyword;
        }
    }

    var modifyCoordinates = [[this._extent[0],this._extent[1]],[this._extent[0],this._extent[3]],
        [this._extent[2],this._extent[3]], [this._extent[2],this._extent[1]]];


	this._layer = new ol.layer.Image({
		zIndex: 400
	});
	this._offlineMap.getOlMap().addLayer(this._layer);
	var preLoadImage = new Image();
	var that = this;
	preLoadImage.onload = function() {
		var pixelRatio = 3;
		if((2000*2000)<(this.width*this.height)) {
			pixelRatio = 2;
		}
		if((5000*5000)<(this.width*this.height)) {
			pixelRatio = 1;
		}
		else if((7000*7000)<(this.width*this.height)) {
			pixelRatio = ((8000*8000)/(this.width*this.height))/2
		}

		that._source = new ol.source.ImageStatic({
			imageLoadFunction: function(image, src) {
				image.getImage().src = src;
			},
			url: options.url,
			imageExtent: that._extent,
			olmap: that._offlineMap.getOlMap(),
			rotate: that._angle,
			pixelRatio: pixelRatio
		});
		that._layer.setSource(that._source);
	}
	preLoadImage.src = options.url;


    // 수정 가능한 polygon 생성
    this._modifyPolygon = new ol.geom.Polygon([modifyCoordinates]);
    this._modifyFeature = new ol.Feature({
        geometry: this._modifyPolygon,
        style: new ol.style.Style({
            stroke: new ol.style.Stroke({
                color: 'blue',
                width: 3
            }),
            fill: new ol.style.Fill({
                color: 'rgba(0, 0, 255, 0.1)'
            })
        })
    });
    this._modifyFeature.set('object', this);
    this._modifyFeature.set('objectType', this._objectType);
    this._modifyFeature.on('propertychange', pnt.util.bind(this, function(event) {
        if(event.key=='dragend') {
            var geometry = event.target.getGeometry();
            var coordinates = geometry.getCoordinates();
            var extent = [coordinates[0][0][0], coordinates[0][0][1], coordinates[0][2][0], coordinates[0][2][1]];
            this._extent = extent;
            this._draw();

            this._offlineMap.getOlMap().dispatchEvent({
                type: this.getObjectType()+'.modify',
                coordinates: coordinates[0],
                object: this,
                objectType: this.getObjectType()
            });
        }
    }));

    var vectorSource = new ol.source.Vector();
    vectorSource.addFeature(this._modifyFeature);
    this._modifyLayer = new ol.layer.Vector({
        source: vectorSource,
        zIndex: 500
    });
    this._modifyLayer.setVisible(false);
    this._offlineMap.getOlMap().addLayer(this._modifyLayer);

    this._modify = new ol.interaction.Modify({
        features: new ol.Collection([this._modifyFeature]),
        deleteCondition: function(evt) {
            return false;
        }
    });
    this._modify.on('modifystart', function(evt) {
        var geometry = evt.features.getArray()[0].getGeometry();
        var coordinates = geometry.getCoordinates();
        evt.target.set('prev.coordinates', coordinates);
    });
    this._modify.on('modifyend', pnt.util.bind(this, function(evt) {

        var geometry = evt.features.getArray()[0].getGeometry();
        var coordinates = geometry.getCoordinates();
        var prevCoordinates = evt.target.get('prev.coordinates');
        if(coordinates[0].length>4) {
            geometry.setCoordinates(prevCoordinates);
        } else {

            if(prevCoordinates[0][0][0]!=coordinates[0][0][0] || prevCoordinates[0][0][1]!=coordinates[0][0][1]) {
                coordinates[0][1][0] = coordinates[0][0][0];
                coordinates[0][3][1] = coordinates[0][0][1];
            }
            if(prevCoordinates[0][1][0]!=coordinates[0][1][0] || prevCoordinates[0][1][1]!=coordinates[0][1][1]) {
                coordinates[0][0][0] = coordinates[0][1][0];
                coordinates[0][2][1] = coordinates[0][1][1];
            }
            if(prevCoordinates[0][2][0]!=coordinates[0][2][0] || prevCoordinates[0][2][1]!=coordinates[0][2][1]) {
                coordinates[0][1][1] = coordinates[0][2][1];
                coordinates[0][3][0] = coordinates[0][2][0];
            }
            if(prevCoordinates[0][3][0]!=coordinates[0][3][0] || prevCoordinates[0][3][1]!=coordinates[0][3][1]) {
                coordinates[0][2][0] = coordinates[0][3][0];
                coordinates[0][0][1] = coordinates[0][3][1];
            }
            geometry.setCoordinates(coordinates);

            var extent = [coordinates[0][0][0], coordinates[0][0][1], coordinates[0][2][0], coordinates[0][2][1]];
            this._extent = extent;
            this._draw();

            this._offlineMap.getOlMap().dispatchEvent({
                type: this.getObjectType()+'.modify',
                coordinates: coordinates[0],
                object: this,
                objectType: this.getObjectType()
            });
        }
    }));
    this._modify.setActive(false);
    this._offlineMap.getOlMap().addInteraction(this._modify);

    this._rangeElement = {};
    this._rangeElement.root = document.createElement('div');
    this._rangeElement.root.style.border = '1px solid #cccccc';
    this._rangeElement.root.style.backgroundColor = '#ffffff';
    this._rangeElement.root.style.borderRadius = '8px';
    this._rangeElement.root.style.padding = '5px';
    this._rangeElement.root.style.display = 'inline';
    this._rangeElement.root.style.height = '26px';
    this._rangeElement.root.style.display = 'block';
    this._rangeElement.input = document.createElement('input');
    this._rangeElement.input.style.width = '200px';
    this._rangeElement.input.style.float = 'left';
    this._rangeElement.input.type = 'range';
    this._rangeElement.input.min = '0';
    this._rangeElement.input.max = '360';
    this._rangeElement.input.step = '0.1';
    this._rangeElement.input.value = this._angle;
    this._rangeElement.input.onchange = pnt.util.bind(this, function(event) {
        this.rotate(event.target.value);
    })
    this._rangeElement.root.appendChild(this._rangeElement.input);
    this._rangeElement.input2 = document.createElement('input');
    this._rangeElement.input2.style.width = '50px';
    this._rangeElement.input2.style.fontSize = '6pt';
    this._rangeElement.input2.style.float = 'left';
    this._rangeElement.input2.style.border = '0px';
    this._rangeElement.input2.type = 'number';
    this._rangeElement.input2.min = '0.0';
    this._rangeElement.input2.max = '360.0';
    this._rangeElement.input2.step = '0.1';
    this._rangeElement.input2.value = this._angle;
    this._rangeElement.input2.onchange = pnt.util.bind(this, function(event) {
        this.rotate(event.target.value);
    })
    this._rangeElement.root.appendChild(this._rangeElement.input2);
    this._rangeElement.input3 = document.createElement('input');
    this._rangeElement.input3.style.width = '50px';
    this._rangeElement.input3.style.fontSize = '6pt';
    this._rangeElement.input3.style.float = 'left';
    this._rangeElement.input3.style.border = '0px';
    this._rangeElement.input3.type = 'number';
    this._rangeElement.input3.min = '0.01';
    this._rangeElement.input3.max = '1.00';
    this._rangeElement.input3.step = '0.01';
    this._rangeElement.input3.onchange = pnt.util.bind(this, function(event) {
        this.setScale(event.target.value);
    });
    // zoom 조절 입력 비활성
    //this._rangeElement.root.appendChild(this._rangeElement.input3);

    this._rangeOverlay = new ol.Overlay({
        element: this._rangeElement.root,
        positioning: 'top-left',
        position: undefined,
        offset: [0, -30],
        autoPan: false
    });

	/*this._extentElement = {};
	this._extentElement.root = document.createElement('div');
	this._extentElement.root.style.border = '1px solid #cccccc';
	this._extentElement.root.style.backgroundColor = '#ffffff';
	this._extentElement.root.style.borderRadius = '8px';
	this._extentElement.root.style.padding = '5px';
	this._extentElement.root.style.display = 'inline';
	this._extentElement.root.style.height = '26px';
	this._extentElement.root.style.display = 'block';
	this._extentElement.input = document.createElement('input');
	this._extentElement.input.style.width = '20px';
	this._extentElement.input.style.float = 'left';
	this._extentElement.input.type = 'number';
	this._extentElement.input.step = '0.000001';
	this._extentElement.input.value = '0';
	this._extentElement.input.onchange = pnt.util.bind(this, function(event) {

	});
	this._extentOverlay = new ol.Overlay({
		element: this._extentElement.root,
		positioning: 'top-left',
		position: undefined,
		offset: [0, -30],
		autoPan: false
	});*/


    this._offlineMap.getOlMap().addOverlay(this._rangeOverlay);

}
pnt.map.object.Image.prototype.getObjectType = function() {
    return this._objectType;
}
pnt.map.object.Image.prototype.getAngle = function() {
    return this._angle;
}
pnt.map.object.Image.prototype._draw = function() {
    this._source = new ol.source.ImageStatic({
        url: this._imageUrl,
        imageExtent: this._extent,
        olmap: this._offlineMap.getOlMap()
    });

    var angle = this._angle;
    var scale = this._scale;
    this._layer.setSource(this._source);
    this._source.rotate(this._angle);


	this._rangeOverlay.setPosition([this._extent[0], this._extent[3]]);

}
pnt.map.object.Image.prototype.rotate = function(angle) {
    this._angle = angle;
    this._source.rotate(angle);
    this._offlineMap.getOlMap().dispatchEvent({
        type: this.getObjectType()+'.rotate',
        angle: angle,
        object: this,
        objectType: this.getObjectType()
    });

    if(this.isEditable()==true) {
        var geometry = this._modifyFeature.getGeometry();
        var coordinates = geometry.getCoordinates();
        this._rangeOverlay.setPosition(coordinates[0][1]);

        if(angle!=this._rangeElement.input.value) {
            this._rangeElement.input.value = angle;
        }
        if(angle!=this._rangeElement.input2.value) {
            this._rangeElement.input2.value = angle;
        }

    }
}
pnt.map.object.Image.prototype.setScale = function(scale) {
    this._scale = scale;
    this._draw();

    this._offlineMap.getOlMap().dispatchEvent({
        type: this.getObjectType()+'.scale',
        scale: scale,
        object: this,
        objectType: this.getObjectType()
    });
}
pnt.map.object.Image.prototype.setOpacity = function(opacity) {
    this._opacity = opacity;
    this._layer.setOpacity(this._opacity);
}
pnt.map.object.Image.prototype.getId = function() {
    return this._id;
}
pnt.map.object.Image.prototype.getData = function(key) {
    if(typeof(this._data[key])!='undefined') {
        return this._data[key];
    } else {
        return false;
    }
}
pnt.map.object.Image.prototype.getExtent = function() {
    return this._extent;
}
pnt.map.object.Image.prototype.remove = function() {
    console.debug('pnt.map.object.Image.remove id:', this._id);
    this._offlineMap.getOlMap().removeLayer(this._layer);

    this._offlineMap.getOlMap().removeLayer(this._modifyLayer);
    this._offlineMap.getOlMap().removeOverlay(this._rangeOverlay);
    this._offlineMap.getOlMap().removeInteraction(this._modify);

}
pnt.map.object.Image.prototype.isEditable = function() {
    return this._modify.getActive();
}
pnt.map.object.Image.prototype.setEditable = function(editable) {
    this._modify.setActive(editable);
    this._modifyLayer.setVisible(editable);

    this._modifyFeature.set('draggable', editable);
    if(editable==true) {
        var geometry = this._modifyFeature.getGeometry();
        var coordinates = geometry.getCoordinates();
        this._rangeOverlay.setPosition(coordinates[0][1]);
    } else {
        this._rangeOverlay.setPosition(undefined);
    }
}
pnt.map.object.Image.prototype.show = function() {
    this._layer.setVisible(true);
}
pnt.map.object.Image.prototype.hide = function() {
    this._layer.setVisible(false);
}


pnt.map.object.Polygon = function(offlineMap, id, options) {

    console.debug('pnt.map.object.Polygon constructor id:', id, ' options:', options);

    this._id = id;
    this._objectType = pnt.map.object.type.POLYGON;
    this.coords = options.coords;
    this._offlineMap = offlineMap;
    this._events = {};
    this._globalEventId = {};
    this._keyword = [];
    this._data = options.data || {};
    this._labelOverlay = null;
    this._center = pnt.util.centroidOfPolygon(options.coords);

    if(typeof(options.label)!='undefined') {
        this._keyword[this._keyword.length] = options.label;
    }

    if(typeof(options.keyword)!='undefined') {
        var arrkey = options.keyword.split(',');
        for(var i=0; i<arrkey.length; i++) {
            var keyword = typeof(arrkey[i])=='string'?arrkey[i].trim():arrkey[i];
            this._keyword[this._keyword.length] = keyword;
        }
    }

    this._polygon = new ol.geom.Polygon([options.coords]);
    this._feature = new ol.Feature({
        geometry: this._polygon
    });
    this._feature.set('object', this);
    this._feature.set('objectType', this._objectType);
    this._styleJson = {};
    if(typeof(options.fill)!='undefined') {
        this._styleJson.fill = new ol.style.Fill(options.fill);
    }
    if(typeof(options.stroke)!='undefined') {
        this._styleJson.stroke = new ol.style.Stroke(options.stroke);
    }

    var style = new ol.style.Style(this._styleJson);
    this._feature.setStyle(style);

    var vectorSource = new ol.source.Vector();
    vectorSource.addFeature(this._feature);
    this._layer = new ol.layer.Vector({
        source: vectorSource,
        zIndex: 500
    });

    this._offlineMap.getOlMap().addLayer(this._layer);



    this._modify = new ol.interaction.Modify({
        features: new ol.Collection([this._feature]),
        deleteCondition: function(evt) {
            return ol.events.condition.shiftKeyOnly(evt) &&
                ol.events.condition.singleClick(evt);
        }
    });
    this._modify.on('modifystart', function(evt) {
        console.debug('pnt.map.object.Polygon modifystart', evt);
    });
    this._modify.on('modifyend', pnt.util.bind(this, function(evt) {
        console.debug('pnt.map.object.Polygon modifyend', evt);
        this._offlineMap.getOlMap().dispatchEvent({
            type: this.getObjectType()+'.modifyend',
            coordinate: evt.mapBrowserEvent.coordinate,
            pixel: evt.mapBrowserEvent.pixel,
            object: this,
            objectType: this.getObjectType(),
            features: evt.features
        });
    }));
    this._modify.setActive(false);
    this._offlineMap.getOlMap().addInteraction(this._modify);

}
pnt.map.object.Polygon.prototype.setStyle = function(style) {

    if(typeof(style.stroke)!='undefined') {
        this._styleJson.stroke = new ol.style.Stroke(style.stroke);
    }
    if(typeof(style.fill)!='undefined') {
        this._styleJson.fill = new ol.style.Fill(style.fill);
    }

    var style = new ol.style.Style(this._styleJson);
    this._feature.setStyle(style);
}
pnt.map.object.Polygon.prototype.getFeature = function() {
    return this._feature;
}
pnt.map.object.Polygon.prototype.getCoordinates = function() {
    return this._polygon.getCoordinates();
}
pnt.map.object.Polygon.prototype.setCoordinates = function(coordinates) {
    return this._polygon.setCoordinates([coordinates]);
}
pnt.map.object.Polygon.prototype.setTag = function(tags) {
    this._tags = tags;
}
pnt.map.object.Polygon.prototype.getTag = function() {
    return this._tags;
}
pnt.map.object.Polygon.prototype.containsTag = function(tag) {
    for(var i=0; i<this._tags.length; i++) {
        if(this._tags[i]==tag) {
            return true;
        }
    }
    return false;
}
pnt.map.object.Polygon.prototype.getObjectType = function() {
    return this._objectType;
}
pnt.map.object.Polygon.prototype.getId = function() {
    return this._id;
}
pnt.map.object.Polygon.prototype.getData = function(key) {
    if(typeof(key)=='undefined') {
        return this._data;
    } else {
        if (typeof(this._data[key]) != 'undefined') {
            return this._data[key];
        } else {
            return false;
        }
    }
}
pnt.map.object.Polygon.prototype.setData = function(key, value) {

    this._data[key] = value;
}
pnt.map.object.Polygon.prototype.onGlobalEvent = function(eventType) {
    if(typeof(this._globalEventId[eventType])=='undefined') {
        this.on(eventType, function(event) {
            this._globalEventId[eventType] = pnt.util.makeid(20);
            var event = {
                type: pnt.map.object.type.POLYGON+'.'+eventType,
                eventType: eventType,
                objectType: pnt.map.object.type.POLYGON,
                object: this
            };
            this._offlineMap.getOlMap().dispatchEvent(event);
        });
    }
}
pnt.map.object.Polygon.prototype.unGlobalEvent = function(eventType) {
    if(typeof(this._globalEventId[eventType])!='undefined') {
        this.un(this._globalEventId[eventType]);
        delete this._globalEventId[eventType];
    }
}
pnt.map.object.Polygon.prototype.on = function(eventType, callback) {
    var eventid = pnt.util.makeid(20);

    this._events[eventid] = {
        eventType: eventType,
        listener: pnt.util.bind(this, callback)
    };

    return eventid;
}
pnt.map.object.Polygon.prototype.un = function(eventid) {

    if(typeof(this._events[eventid])!='undefined') {
        var event = this._events[eventid];

        delete this._events[eventid];
    }
}
pnt.map.object.Polygon.prototype.dispatchEvent = function(eventType, evt) {
    for(var eventId in this._events) {
        var eventInfo = this._events[eventId];
        if(eventInfo.eventType==eventType) {
            eventInfo.listener(evt);
        }
    }
}
pnt.map.object.Polygon.prototype.setLabelText = function(text) {
	/*var element = document.getElementById(this._elementId+'-label');
	 element.innerHTML = text;*/
    this.showLabel(text);
}
pnt.map.object.Polygon.prototype.showLabel = function(text) {
    var text = new ol.style.Text({
        textAlign: 'center',
        textBaseline: 'middle',
        //font: 'normal 12px Courier New',
        font: 'normal 11px Gulim',
        text: text,
        fill: new ol.style.Fill({color: 'rgba(0, 0, 0, 1)'}),
        stroke: new ol.style.Stroke({color: 'rgba(255, 255, 255, 0.4)', width: 4}),
        offsetX: 0,
        offsetY: 0,
        rotation: 0
    });
    this._styleJson.text = text;
    this._feature.setStyle(new ol.style.Style(this._styleJson));


	/*if(this._labelOverlay==null) {
	 var elementId = 'pntmap-'+pnt.map.object.type.POLYGON+'-'+this._id+'-label';
	 var textElement = document.createElement('div');
	 textElement.id = elementId;
	 textElement.innerHTML = text;
	 textElement.style.fontSize = '9pt';
	 textElement.style.backgroundColor = 'rgba(255, 255, 255, 0.8)';
	 this._offlineMap.getRootElement().appendChild(textElement);

	 this._labelOverlay =  new ol.Overlay({
	 element: textElement,
	 offset: [0,0],
	 positioning: 'center-center',
	 position: this._center,
	 autoPanAnimation: false
	 });
	 this._offlineMap.getOlMap().addOverlay(this._labelOverlay);
	 }*/
}
pnt.map.object.Polygon.prototype.remove = function() {
    console.debug('pnt.map.object.Polygon.remove id:', this._id);
    if(this._modify!=null) {
        this._offlineMap.getOlMap().removeInteraction(this._modify);
    }

    this._offlineMap.getOlMap().removeLayer(this._layer);
	/*if(this._labelOverlay!=null) {
	 var el = document.getElementById('pntmap-'+pnt.map.object.type.POLYGON+'-'+this._id+'-label');
	 if(el) {
	 el.parentElement.removeChild(el);
	 this._offlineMap.getOlMap().removeLayer(this._labelOverlay);
	 }
	 }*/
}
pnt.map.object.Polygon.prototype.show = function() {
    this._layer.setVisible(true);
    if(this._labelOverlay!=null) {
        var el = document.getElementById('pntmap-'+pnt.map.object.type.POLYGON+'-'+this._id+'-label');
        if(el) {
            el.style.display = '';
        }
    }
}
pnt.map.object.Polygon.prototype.hide = function() {
    this._layer.setVisible(false);
    if(this._labelOverlay!=null) {
        var el = document.getElementById('pntmap-'+pnt.map.object.type.POLYGON+'-'+this._id+'-label');
        if(el) {
            el.style.display = 'none';
        }
    }
}
pnt.map.object.Polygon.prototype.setDraggable = function(draggable) {
    this._feature.set('draggable', draggable);
}
pnt.map.object.Polygon.prototype.isEditable = function() {
    return this._modify.getActive();
}
pnt.map.object.Polygon.prototype.setEditable = function(editable) {
    this._modify.setActive(editable);
}
pnt.map.object.Polygon.prototype.getGeometry = function() {
	var features = this._layer.getSource().getFeatures();
	if(typeof(features)!='undefined' && features!=null && features.length>0) {
		return features[0].getGeometry();
	} else {
		return false;
	}
}


pnt.map.object.Circle = function(offlineMap, id, options) {

    console.debug('pnt.map.object.Circle constructor id:', id, ' options:', options);

    this._id = id;
    this._objectType = pnt.map.object.type.CIRCLE;
    this._offlineMap = offlineMap;
    this._events = {};
    this._globalEventId = {};
    this._keyword = [];
    this._data = options.data || {};

    if(typeof(options.label)!='undefined') {
        this._keyword[this._keyword.length] = options.label;
    }

    if(typeof(options.keyword)!='undefined') {
        var arrkey = options.keyword.split(',');
        for(var i=0; i<arrkey.length; i++) {
            var keyword = typeof(arrkey[i])=='string'?arrkey[i].trim():arrkey[i];
            this._keyword[this._keyword.length] = keyword;
        }
    }

    //this._circle = new ol.geom.Circle(options.center, (options.radius || 1)*ol.proj.METERS_PER_UNIT['degrees']);
    var wgs84Sphere = new ol.Sphere(6378137);
    this._circle = ol.geom.Polygon.circular(wgs84Sphere, options.center, (options.radius || 1));

    this._feature = new ol.Feature({
        geometry: this._circle
    });
    this._feature.set('object', this);
    this._feature.set('objectType', this._objectType);
    this._styleJson = {};
    if(typeof(options.fill)!='undefined') {
        this._styleJson.fill = new ol.style.Fill(options.fill);
    }
    if(typeof(options.stroke)!='undefined') {
        this._styleJson.stroke = new ol.style.Stroke(options.stroke);
    }

    var style = new ol.style.Style(this._styleJson);
    this._feature.setStyle(style);

    var vectorSource = new ol.source.Vector();
    vectorSource.addFeature(this._feature);
    this._layer = new ol.layer.Vector({
        source: vectorSource,
        zIndex: 510
    });

    this._offlineMap.getOlMap().addLayer(this._layer);

    this._modify = new ol.interaction.Modify({
        features: new ol.Collection([this._feature]),
        deleteCondition: function(evt) {
            return ol.events.condition.shiftKeyOnly(evt) &&
                ol.events.condition.singleClick(evt);
        }
    });
    this._modify.on('modifystart', function(evt) {
        console.debug('pnt.map.object.Circle modifystart', evt);
    });
    this._modify.on('modifyend', pnt.util.bind(this, function(evt) {
        console.debug('pnt.map.object.Circle modifyend', evt);

        this._offlineMap.getOlMap().dispatchEvent({
            type: this.getObjectType()+'.modifyend',
            coordinate: evt.mapBrowserEvent.coordinate,
            pixel: evt.mapBrowserEvent.pixel,
            object: this,
            objectType: this.getObjectType(),
            features: evt.features
        });
    }));
    this._modify.setActive(false);
    this._offlineMap.getOlMap().addInteraction(this._modify);

}
pnt.map.object.Circle.prototype.getGeometry = function() {
    var features = this._layer.getSource().getFeatures();
    if(typeof(features)!='undefined' && features!=null && features.length>0) {
        return features[0].getGeometry();
    } else {
        return false;
    }
}
pnt.map.object.Circle.prototype.setGeometry = function(geometry) {
    var features = this._layer.getSource().getFeatures();
    if(typeof(features)!='undefined' && features!=null && features.length>0) {
        features[0].setGeometry(geometry);
    }
}
/*pnt.map.object.Circle.prototype.getCenter = function() {
 return this._circle.getCenter();
 }
 pnt.map.object.Circle.prototype.setCenter = function(coordinate) {
 this._circle.setCenter(coordinate);
 }
 pnt.map.object.Circle.prototype.getRadius = function() {
 return this._circle.getRadius();
 }
 pnt.map.object.Circle.prototype.setRadius = function(radius) {
 this._circle.setRadius(radius);
 }*/
pnt.map.object.Circle.prototype.setCenterRadius = function(coordinate, radius) {
    var wgs84Sphere = new ol.Sphere(6378137);
    var circle = ol.geom.Polygon.circular(wgs84Sphere, coordinate, radius);
    this._feature.setGeometry(circle);
}
pnt.map.object.Circle.prototype.setTag = function(tags) {
    this._tags = tags;
}
pnt.map.object.Circle.prototype.getTag = function() {
    return this._tags;
}
pnt.map.object.Circle.prototype.containsTag = function(tag) {
    for(var i=0; i<this._tags.length; i++) {
        if(this._tags[i]==tag) {
            return true;
        }
    }
    return false;
}
pnt.map.object.Circle.prototype.getObjectType = function() {
    return this._objectType;
}
pnt.map.object.Circle.prototype.getId = function() {
    return this._id;
}
pnt.map.object.Circle.prototype.getData = function(key) {
    if(typeof(key)=='undefined') {
        return this._data;
    } else {
        if (typeof(this._data[key]) != 'undefined') {
            return this._data[key];
        } else {
            return false;
        }
    }
}
pnt.map.object.Circle.prototype.setData = function(key, value) {

    this._data[key] = value;
}
pnt.map.object.Circle.prototype.onGlobalEvent = function(eventType) {
    if(typeof(this._globalEventId[eventType])=='undefined') {
        this.on(eventType, function(event) {
            this._globalEventId[eventType] = pnt.util.makeid(20);
            var event = {
                type: pnt.map.object.type.POLYGON+'.'+eventType,
                eventType: eventType,
                objectType: pnt.map.object.type.POLYGON,
                object: this
            };
            this._offlineMap.getOlMap().dispatchEvent(event);
        });
    }
}
pnt.map.object.Circle.prototype.unGlobalEvent = function(eventType) {
    if(typeof(this._globalEventId[eventType])!='undefined') {
        this.un(this._globalEventId[eventType]);
        delete this._globalEventId[eventType];
    }
}
pnt.map.object.Circle.prototype.on = function(eventType, callback) {
    var eventid = pnt.util.makeid(20);

    this._events[eventid] = {
        eventType: eventType,
        listener: pnt.util.bind(this, callback)
    };

    return eventid;
}
pnt.map.object.Circle.prototype.un = function(eventid) {

    if(typeof(this._events[eventid])!='undefined') {
        var event = this._events[eventid];

        delete this._events[eventid];
    }
}
pnt.map.object.Circle.prototype.dispatchEvent = function(eventType, evt) {
    for(var eventId in this._events) {
        var eventInfo = this._events[eventId];
        if(eventInfo.eventType==eventType) {
            eventInfo.listener(evt);
        }
    }
}
pnt.map.object.Circle.prototype.setLabelText = function(text) {
    this.showLabel(text);
}
pnt.map.object.Circle.prototype.showLabel = function(text) {
    var text = new ol.style.Text({
        textAlign: 'center',
        textBaseline: 'middle',
        //font: 'normal 12px Courier New',
        font: 'normal 11px Gulim',
        text: text,
        fill: new ol.style.Fill({color: 'rgba(0, 0, 0, 1)'}),
        stroke: new ol.style.Stroke({color: 'rgba(255, 255, 255, 0.4)', width: 4}),
        offsetX: 0,
        offsetY: 0,
        rotation: 0
    });
    this._styleJson.text = text;
    this._feature.setStyle(new ol.style.Style(this._styleJson));
}
pnt.map.object.Circle.prototype.remove = function() {
    console.debug('pnt.map.object.Circle.remove id:', this._id);
    if(this._modify!=null) {
        this._offlineMap.getOlMap().removeInteraction(this._modify);
    }

    this._offlineMap.getOlMap().removeLayer(this._layer);
}
pnt.map.object.Circle.prototype.show = function() {
    this._layer.setVisible(true);
}
pnt.map.object.Circle.prototype.hide = function() {
    this._layer.setVisible(false);
}
pnt.map.object.Circle.prototype.setDraggable = function(draggable) {
    this._feature.set('draggable', draggable);
}
pnt.map.object.Circle.prototype.isEditable = function() {
    return this._modify.getActive();
}
pnt.map.object.Circle.prototype.setEditable = function(editable) {
    this._modify.setActive(editable);
}





pnt.map.object.PolygonGenerator = function(offlineMap) {
    this._offlineMap = offlineMap;
    this._callback = null;
    this._draw = null;
    this._coordinates = null;
}
pnt.map.object.PolygonGenerator.prototype.start = function(callback) {
    this._callback = callback;

    this._draw = new ol.interaction.Draw({
		/*features: features,*/
        type: 'Polygon',
		/*geometryFunction: geometryFunction,*/
        condition: function(evt) {
            if(evt.originalEvent.which!=1) {
                return false;
            } else {
                return true;
            }
        },
        maxPoints: 100
    });
    this._draw.on('drawstart', function(evt) {
        console.debug('pnt.map.object.PolygonGenerator drawstart', evt);
    });
    this._draw.on('drawend', pnt.util.bind(this, function(evt) {
        console.debug('pnt.map.object.PolygonGenerator drawend', evt);
        var feature = evt.feature;
        var geometry = feature.getGeometry();
        this.end(geometry.getCoordinates()[0]);
    }));
    this._offlineMap.getOlMap().addInteraction(this._draw);

}
pnt.map.object.PolygonGenerator.prototype.end = function(coordinates) {
    this._offlineMap.getOlMap().removeInteraction(this._draw);
    this._coordinates = coordinates;

    this._callback(coordinates);

}
pnt.map.object.PolygonGenerator.prototype.getCoordinates = function() {
    return this._coordinates;
}


pnt.map.object.LineGenerator = function(offlineMap) {
    this._offlineMap = offlineMap;
    this._startPosition = null;
    this._endPosition = null;
    this._callback = null;
    this._count = null;
}
pnt.map.object.LineGenerator.prototype.start = function(callback, count) {

    this._startPosition = null;
    this._endPosition = null;
    this._count = count || null;
    this._callback = callback;

    this._draw = new ol.interaction.Draw({
		/*features: new ol.Collection([featureLine]),*/
        type: 'LineString',
		/*geometryFunction: function(coords, geom) {
		 console.log('geometryFunction', geom);
		 return geom;
		 },*/
        finishCondition: function(evt) {
            console.debug('pnt.map.object.LineGenerator.start finishCondition', evt);
        },
        condition: function(evt) {
            console.debug('pnt.map.object.LineGenerator.start condition', evt);
            if(evt.originalEvent.which!=1) {
                var lineGenerator = this.get('lineGenerator');
                this.getMap().removeInteraction(this);
                lineGenerator.start(lineGenerator._callback, lineGenerator._count);
                return false;
            } else {
                return true;
            }
        },
        maxPoints: 2
    });
    this._draw.set('lineGenerator', this);
    this._draw.on('drawstart', function(evt) {
        var feature = evt.feature;
        var geometry = feature.getGeometry();
        console.debug('pnt.map.object.LineGenerator.start drawstart', evt);
    });
    this._draw.on('drawend', pnt.util.bind(this, function(evt) {
        console.debug('pnt.map.object.LineGenerator.start drawend', evt);
        var feature = evt.feature;
        var geometry = feature.getGeometry();
        this.end(geometry.getCoordinates());
    }));
    this._offlineMap.getOlMap().addInteraction(this._draw);

}
pnt.map.object.LineGenerator.prototype.end = function(coordinates) {
    this._offlineMap.getOlMap().removeInteraction(this._draw);

    this._startPosition = coordinates[0];
    this._endPosition = coordinates[1];

    if(this._count>2) {
        var points = [];
        points.push(this._startPosition);
        var p1 = (this._endPosition[0]-this._startPosition[0])/(this._count-1);
        var p2 = (this._endPosition[1]-this._startPosition[1])/(this._count-1);
        for(var i=1; i<this._count-1; i++) {
            points.push([this._startPosition[0]+(p1*i),this._startPosition[1]+(p2*i)]);
        }
        points.push(this._endPosition);
        this._callback(points);
    } else {
        this._callback([this._startPosition, this._endPosition]);
    }

}
pnt.map.object.LineGenerator.prototype.getCoordinates = function() {
    return [this._startPosition, this._endPosition];
}



pnt.map.Drag = function(pntmap) {

    ol.interaction.Pointer.call(this, {
        handleDownEvent: pnt.map.Drag.prototype.handleDownEvent,
        handleDragEvent: pnt.map.Drag.prototype.handleDragEvent,
        handleMoveEvent: pnt.map.Drag.prototype.handleMoveEvent,
        handleUpEvent: pnt.map.Drag.prototype.handleUpEvent
    });

    this._pntmap = pntmap;
    this._startCoordinate = null;
    this._coordinate = null;
    this._feature = null;
    this._dragging = false;
    this._draggingMode = 1; // 1: drag object, 2: connect
    this._shiftkey = false; // 1:nomal, 2:hold shiftkey
    this._previousCursor = undefined;
    this._cursor = 'pointer';
};
ol.inherits(pnt.map.Drag, ol.interaction.Pointer);
pnt.map.Drag.prototype.handleDownEvent = function(evt) {

    console.debug('pnt.map.Drag.handleDownEvent', evt);

    var map = evt.map;
    var feature = map.forEachFeatureAtPixel(evt.pixel,
        function(feature, layer) {
            return feature;
        });


    if (feature) {
        var object = feature.get('object');
        if(typeof(object)!='undefined') {
            if(object.getObjectType()==pnt.map.object.type.VMARKER
                || object.getObjectType()==pnt.map.object.type.POLYGON
                || object.getObjectType()==pnt.map.object.type.IMAGE) {

                var draggableVmc = feature.get('draggable-vmc');
                var draggable = feature.get('draggable');

                var shiftkey = false;
                if(this._shiftkey==true || ol.events.condition.shiftKeyOnly(evt)) {
                    shiftkey = true;
                }

                if(typeof(draggable)!='undefined' && draggable==true &&
                    evt.originalEvent.which==1 && shiftkey==true) {

                    this._coordinate = evt.coordinate;
                    this._startCoordinate = [evt.coordinate[0], evt.coordinate[1]];
                    this._feature = feature;

                    this._draggingMode = 1;
                }
                else if(typeof(draggableVmc)!='undefined' && draggableVmc==true &&
                    evt.originalEvent.which==1 && shiftkey==false) {

                    this._coordinate = evt.coordinate;
                    this._startCoordinate = [evt.coordinate[0], evt.coordinate[1]];
                    this._feature = feature;

                    this._lineString = new ol.geom.LineString([[evt.coordinate[0], evt.coordinate[1]]]);
                    this._lineFeature = new ol.Feature({
                        geometry: this._lineString
                    });
                    this._lineStyle = {
                        stroke: new ol.style.Stroke({
                            color: '#333333',
                            width: 3
                        })
                    }
                    this._lineFeature.setStyle(new ol.style.Style(this._lineStyle));
                    this._lineVectorSource = new ol.source.Vector();
                    this._lineVectorSource.addFeature(this._lineFeature);
                    this._lineLayer = new ol.layer.Vector({
                        source: this._lineVectorSource,
                        zIndex: 700
                    });
                    map.addLayer(this._lineLayer);

                    this._draggingMode = 2;
                }
            }
        }
    }
    return !!feature;
}
pnt.map.Drag.prototype.handleDragEvent = function(evt) {

    console.debug('pnt.map.Drag.handleDragEvent', evt);

    if(this._feature) {
        var map = evt.map;
        this._dragging = true;

        var feature = map.forEachFeatureAtPixel(evt.pixel,
            function(feature, layer) {
                return feature;
            });

        if(this._draggingMode==1) {

            var deltaX = evt.coordinate[0] - this._coordinate[0];
            var deltaY = evt.coordinate[1] - this._coordinate[1];

            var geometry = (this._feature.getGeometry());
            geometry.translate(deltaX, deltaY);
        }
        else if(this._draggingMode==2 && this._lineString) {

            this._lineString.setCoordinates([this._startCoordinate, evt.coordinate]);
            var draggableVmc = false;
            if(typeof(feature)!='undefined') {
                var vmcOption = feature.get('draggable-vmc');
                if(typeof(vmcOption)!='undefined' && vmcOption==true) {
                    draggableVmc = true;
                }
            }

            if(draggableVmc == true) {
                var text = new ol.style.Text({
                    textAlign: 'center',
                    textBaseline: 'middle',
                    font: 'normal 11px Gulim',
                    text: '연결가능',
                    fill: new ol.style.Fill({color: 'rgba(0, 0, 0, 1)'}),
                    stroke: new ol.style.Stroke({color: 'rgba(255, 255, 255, 0.8)', width: 1}),
                    offsetX: 0,
                    offsetY: 0,
                    rotation: 0
                });
                this._lineStyle.text = text;
                this._lineFeature.setStyle(new ol.style.Style(this._lineStyle));
            } else {
                delete this._lineStyle['text'];
                this._lineFeature.setStyle(new ol.style.Style(this._lineStyle));
            }
        }

        this._coordinate[0] = evt.coordinate[0];
        this._coordinate[1] = evt.coordinate[1];
    }
}
pnt.map.Drag.prototype.handleMoveEvent = function(evt) {
    if (this._cursor) {
        var map = evt.map;
        var feature = map.forEachFeatureAtPixel(evt.pixel,
            function (feature, layer) {
                return feature;
            });

        var element = evt.map.getTargetElement();
        if (feature) {
            var draggable = feature.get('draggable');
            var draggableVmc = feature.get('draggable-vmc');
            if ( (typeof(draggable) != 'undefined' && draggable == true)
                || (typeof(draggableVmc) != 'undefined' && draggableVmc == true)) {

                if (element.style.cursor != this._cursor) {
                    this._previousCursor = element.style.cursor;
                    element.style.cursor = this._cursor;
                }
            }
        } else if (this._previousCursor !== undefined) {
            element.style.cursor = this._previousCursor;
            this._previousCursor = undefined;
        }
    }
};
pnt.map.Drag.prototype.handleUpEvent = function(evt) {
    console.debug('pnt.map.Drag.handleUpEvent', evt);
    var map = evt.map;
    if(this._feature!=null && typeof(this._feature)!='undefined' && this._dragging==true) {
        var object = this._feature.get('object');

        if(this._draggingMode==1) {
            var move = [evt.coordinate[0] - this._startCoordinate[0], evt.coordinate[1] - this._startCoordinate[1]];
            this._pntmap.getOlMap().dispatchEvent({
                type: object.getObjectType()+'.dragend',
                coordinate: evt.coordinate,
                move: move,
                pixel: evt.pixel,
                object: object,
                objectType: object.getObjectType()
            });

            this._feature.set('dragend', {
                type: object.getObjectType()+'.dragend',
                coordinate: evt.coordinate,
                move: move,
                pixel: evt.pixel,
                object: object,
                objectType: object.getObjectType()
            });
        }
        else if(this._draggingMode==2) {

            var feature = map.forEachFeatureAtPixel(evt.pixel,
                function (feature, layer) {
                    return feature;
                });

            if(feature) {
                var endObject = feature.get('object');
                var draggableVmc = feature.get('draggable-vmc');

                if(typeof(object)!='undefined' && typeof(endObject)!='undefined'
                    && typeof(draggableVmc)!='undefined' && draggableVmc==true) {

                    var move = [evt.coordinate[0] - this._startCoordinate[0], evt.coordinate[1] - this._startCoordinate[1]];
                    this._pntmap.getOlMap().dispatchEvent({
                        type: object.getObjectType()+'.connect',
                        coordinate: evt.coordinate,
                        pixel: evt.pixel,
                        startObject: object,
                        endObject: endObject
                    });
                }
            }
        }
    }
    if(this._lineLayer!=null) {
        map.removeLayer(this._lineLayer);
        this._lineLayer = null;
        this._lineString = null;
    }

    this._dragging = false;
    this._coordinate = null;
    this._feature = null;

    return false;
};
pnt.map.Drag.prototype.setShiftKey = function(enabled) {
    this._shiftkey = enabled;
}





pnt.map.AreaManager = function(offlineMap, option) {

    console.debug('pnt.map.AreaManager constructor');

    if(typeof(option)=='undefined') {
        option = {};
    }

    this._offlineMap = offlineMap;
    this._floorManager = null;
    this._areaData = null;
    this._bindElements = {};

    this._element = {};
    this._element.root = document.createElement('div');
    this._element.root.style.float = 'left';
    this._element.root.style.display = 'none';
    this._element.root.style.padding = '5px';

    this._element.label = document.createElement('div');
    this._element.label.innerHTML = '구역';
    this._element.label.style.float = 'left';
    this._element.label.style.marginTop = '3px';
    this._element.label.style.marginLeft = '5px';
    this._element.label.style.fontSize = '9pt';
    this._element.root.appendChild(this._element.label);

    this._element.area = document.createElement('select');
    this._element.area.id = 'map-control-area-'+pnt.util.makeid(10);
    this._element.area.style.float = 'left';
    this._element.area.style.marginLeft = '5px';
    this._element.root.appendChild(this._element.area);

    var topEl = this._offlineMap.getControlElement('top');
    topEl.appendChild(this._element.root);
}
pnt.map.AreaManager.prototype.load = function(areaData, option) {
    if(typeof(option)=='undefined') {
        option = {};
    }

    this.setAreaData(areaData);
    this._element.root.style.display = '';
    this.bindElement(this._element.area);
}
pnt.map.AreaManager.prototype.setFloorManager = function(floorManager) {
    this._floorManager = floorManager;
}
pnt.map.AreaManager.prototype.getFloorManager = function() {
    return this._floorManager;
}
pnt.map.AreaManager.prototype.setAreaData = function(areaData) {

    console.debug('pnt.map.AreaManager.setAreaData');

    this._areaData = {};
    for(var i=0; i<areaData.length; i++) {
        var item = areaData[i];

        if(typeof(this._areaData[item.areaName])=='undefined') {
            this._areaData[item.areaName] = item;
        }
        if(typeof(this._areaData[item.areaName].floors)=='undefined') {
            this._areaData[item.areaName].floors = [];
            this._areaData[item.areaName].floorCenters = {};
        }
        this._areaData[item.areaName].floors.push(item.floor);
        if(typeof(this._areaData[item.areaName].floorCenters[item.floor])=='undefined') {
            this._areaData[item.areaName].floorCenters[item.floor] = {};
        }
        this._areaData[item.areaName].floorCenters[item.floor] = item.center;

    }
}
pnt.map.AreaManager.prototype.bindElement = function(element) {

    console.debug('pnt.map.AreaManager.bindElement elementId:');

    if(typeof(this._bindElements[element.id])=='undefined') {
        element.innerHTML = '';

        for(var areaName in this._areaData) {
            var areaInfo = this._areaData[areaName];

            var option = window.document.createElement('option');
            option.innerHTML = areaInfo.areaName;
            option.value = areaInfo.floors.join(',');
            element.appendChild(option);
        }
        this._bindElements[element.id] = {
            id: element.id,
            element: element,
            listener: pnt.util.bind(this, function(event) {
                var optionIndex = event.target.selectedIndex;
                var floors = event.target.value.split(',');
                var floorCenters = this._areaData[event.target.options[optionIndex].innerHTML].floorCenters;
                if(this._floorManager!=null) {
                    this._floorManager.showFloorList(floors, floorCenters);
                }
            })
        };

        var optionIndex = element.selectedIndex;
        var floors = element.value.split(',');
        var floorCenters = this._areaData[element.options[optionIndex].innerHTML].floorCenters;
        if(this._floorManager!=null) {
            this.getFloorManager().showFloorList(floors, floorCenters);
        }

        pnt.util.addEventListener(element, 'change', this._bindElements[element.id].listener, this);
    }
}



pnt.map.FloorManager = function(offlineMap, option) {

    console.debug('pnt.map.FloorManager constructor');

    if(typeof(option)=='undefined') {
        option = {};
    }

    this._offlineMap = offlineMap;
    this._objectManager = offlineMap.getObjectManager();
    this._floorData = {};
    this._defaultFloor =  option.defaultFloor || null;
    this._currentFloor = null;
    this._EVENT_NAME_CHANGE = 'floor.change';
    this._events = {};
    this._imageObject = null;
    this._bindElements = {};
    this._autofit = typeof(option.autofit)!='undefined' && option.autofit==true?true:false;

    this._element = {};
    this._element.root = document.createElement('div');
    this._element.root.style.display = 'none';
    this._element.root.style.float = 'left';
    this._element.root.style.fontSize = '9pt';

    this._element.label = document.createElement('div');
    this._element.label.innerHTML = '층';
    this._element.label.style.float = 'left';
    this._element.label.style.marginTop = '7px';
    this._element.label.style.marginLeft = '5px';
    this._element.label.style.fontSize = '12px';
    this._element.root.appendChild(this._element.label);

    this._element.floor = document.createElement('select');
    this._element.floor.id = 'map-control-floor-'+pnt.util.makeid(10);
    this._element.floor.style.float = 'left';
    this._element.floor.style.marginLeft = '5px';
    this._element.floor.style.fontSize = '12px';
    this._element.floor.style.height = '30px';
    this._element.floor.style.minWidth = '90px';
    this._element.floor.style.lineHeight = '1.5';
    this._element.floor.style.border = '1px solid #ddddd';
    this._element.floor.style.borderRadius = '2px';
    this._element.floor.style.padding = '5px 10px';
    this._element.root.appendChild(this._element.floor);

    var topEl = this._offlineMap.getControlElement('top');
    topEl.appendChild(this._element.root);
}
pnt.map.FloorManager.prototype.getCurrentFloor = function() {
    return this._currentFloor;
}
pnt.map.FloorManager.prototype.getImage = function() {
    return this._imageObject[0];
}
pnt.map.FloorManager.prototype.load = function(floorData, option) {
    if(typeof(option)=='undefined') {
        option = {};
    }

    if(typeof(option.defaultFloor)!='undefined') {
        this._defaultFloor =  option.defaultFloor;
    }
    this.setFloorData(floorData);
    this._element.root.style.display = '';
    this.bindElement(this._element.floor);
}
pnt.map.FloorManager.prototype.setFloorData = function(floorData) {

    console.debug('pnt.map.FloorManager.setFloorData');

    for(var i=0; i<floorData.length; i++) {
        var item = floorData[i];
        if(typeof(this._floorData[item.floor])=='undefined') {
            this._floorData[item.floor] = [];
        }
        this._floorData[item.floor].push(item);
    }
}
pnt.map.FloorManager.prototype.bindElement = function(element) {

    console.debug('pnt.map.FloorManager.bindElement', element.id);

    if(typeof(this._bindElements[element.id])=='undefined') {
        element.innerHTML = '';
        for(var floor in this._floorData) {
            if(this._defaultFloor==null) {
                this._defaultFloor = floor;
            }
            var floorInfo = this._floorData[floor][0];
            var option = window.document.createElement('option');
            option.innerHTML = floorInfo.floorName;
            option.value = floorInfo.floor;
            element.appendChild(option);
        }
        this._bindElements[element.id] = {
            id: element.id,
            element: element,
            listener: function(event) {
                var optionIndex = event.target.selectedIndex;
                console.debug('pnt.map.FloorManager.bindElement Event Listerner(CHANGE): center:', event.target.options[optionIndex].dataset.center)
                if(typeof(event.target.options[optionIndex].dataset.center)!='undefined'
                    && event.target.options[optionIndex].dataset.center!=null) {

                    this.changeFloor(event.target.value, event.target.options[optionIndex].dataset.center.split(','));
                } else {
                    this.changeFloor(event.target.value);
                }
            }
        };
        pnt.util.addEventListener(element, 'change', this._bindElements[element.id].listener, this);
    }

}
pnt.map.FloorManager.prototype.setDefaultFloor = function(floor) {
    this._defaultFloor = floor;
}
pnt.map.FloorManager.prototype.showFloorList = function(floors, centers) {

    console.debug('pnt.map.FloorManager.showFloorList floors:',JSON.stringify(floors),' centers:',JSON.stringify(centers));

    var firstFloor = null;
    var firstIndex = null;
    var center = null;
    for(var elementId in this._bindElements) {
        var element = window.document.getElementById(elementId);

        for(var i=0; i<element.children.length; i++) {
            var flag = false;
            element.children[i].dataset.center = null;
            for(var j=0; j<floors.length; j++) {
                if(element.children[i].value == floors[j]) {
                    flag = true;
                    if(firstFloor==null) {
                        firstFloor = floors[j];
                        firstIndex = i;
                    }
                    if(typeof(centers)!='undefined') {
                        element.children[i].dataset.center = centers[floors[j]].lng+','+centers[floors[j]].lat;
                    }
                    break;
                }
            }
            if(flag==true) {
                element.children[i].style.display = '';
            } else {
                element.children[i].style.display = 'none';
            }
        }
        element.selectedIndex = firstIndex;
        //console.log('element.selectedIndex', element, firstIndex);
        if(firstIndex!=null && center==null &&
            typeof(element.children[firstIndex].dataset.center)!='undefined') {

            center = element.children[firstIndex].dataset.center.split(',');
        }
    }
    if(center!=null) {
        this.changeFloor(firstFloor, center);
    } else {
        this.changeFloor(firstFloor);
    }
}

pnt.map.FloorManager.prototype.changeDefault = function() {
    this._offlineMap.readyPostRender(pnt.util.bind(this, function() {
        console.debug('pnt.map.FloorManager.changeDefault defaultFloor:' + this._defaultFloor);
        this.changeFloor(this._defaultFloor);
    }));
}
pnt.map.FloorManager.prototype.onChange = function(callback) {
    var eventid = pnt.util.makeid(20);
    this._events[eventid] = this._offlineMap.getOlMap().on(this._EVENT_NAME_CHANGE, callback, this);
    return eventid;
}
pnt.map.FloorManager.prototype.unChange = function(eventid) {
    this._offlineMap.getOlMap().unByKey(this._events[eventid]);
    delete this._events[eventid];
}
pnt.map.FloorManager.prototype.changeFloor = function(floor, center) {
	console.debug('pnt.map.FloorManager.changeFloor floor:',floor,' center:'+center)
	this._currentFloor = floor;
	var flagCallCenter = false;

	if(this._imageObject!=null) {
		for(var i=0; i<this._imageObject.length; i++) {
			this._objectManager.remove(pnt.map.object.type.IMAGE, this._imageObject[i].getId());
		}
	}

	if(typeof(this._floorData[floor])!='undefined') {
		this._imageObject = [];

		var minSwLat = null;
		var minSwLng = null;
		var maxNeLat = null;
		var maxNeLng = null;

		for(var i=0; i<this._floorData[floor].length; i++) {
			var floorInfo = this._floorData[floor][i];
			var extent = [floorInfo.swLng, floorInfo.swLat, floorInfo.neLng, floorInfo.neLat];

			if(minSwLat==null || minSwLat>parseFloat(floorInfo.swLat)) {
				minSwLat = parseFloat(floorInfo.swLat);
			}
			if(minSwLng==null || minSwLng>parseFloat(floorInfo.swLng)) {
				minSwLng = parseFloat(floorInfo.swLng);
			}
			if(maxNeLat==null || maxNeLat<parseFloat(floorInfo.neLat)) {
				maxNeLat = parseFloat(floorInfo.neLat);
			}
			if(maxNeLng==null || maxNeLng<parseFloat(floorInfo.neLng)) {
				maxNeLng = parseFloat(floorInfo.neLng);
			}

			var option = {tag:'floor', extent: pnt.util.transformExtent(extent),
				angle:floorInfo.deg, url: floorInfo.imgUrl};
			this._imageObject.push(this._objectManager.create(pnt.map.object.type.IMAGE, 'floor-'+i, option));

			if(typeof(center)=='undefined') {
				if(flagCallCenter==false && typeof(floorInfo.neLng)=='number' && typeof(floorInfo.neLat)=='number'
					&& typeof(floorInfo.swLng)=='number' && typeof(floorInfo.swLat)=='number') {

					var tocenter = [parseFloat(floorInfo.neLng)+(parseFloat(floorInfo.swLng) - parseFloat(floorInfo.neLng))/2,
						parseFloat(floorInfo.neLat)+(parseFloat(floorInfo.swLat) - parseFloat(floorInfo.neLat))/2]
					this._offlineMap.setCenter(pnt.util.transformCoordinates(tocenter));
					flagCallCenter= true;
				}
			} else if(flagCallCenter==false && this._autofit!=true) {

				this._offlineMap.setCenter(
					pnt.util.transformCoordinates([parseFloat(center[0]+'00001'), parseFloat(center[1]+'00001')])
				);
				flagCallCenter = true;
			}
		}

		if(this._autofit==true) {
			var autofitExtent = [minSwLng, minSwLat, maxNeLng, maxNeLat];
			var size = this._offlineMap.getOlMap().getSize();
			this._offlineMap.getOlMap().getView().fit(pnt.util.transformExtent(autofitExtent), size);
		}

		for(var elementId in this._bindElements) {
			var element = this._bindElements[elementId].element;
			if(element.value!=floor) {
				for(var i=0; i<element.children.length; i++) {
					var option = element.children[i];
					if(option.value==floor) {
						element.selectedIndex = i;
						break;
					}
				}
			}
		}

		var event = {
			type: this._EVENT_NAME_CHANGE,
			floor: this._currentFloor
		};
		this._offlineMap.getOlMap().dispatchEvent(event);
	}
}
pnt.map.FloorManager.prototype.getElement = function(key) {
	if(typeof(this._element[key])!='undefined') {
		return this._element[key];
	} else {
		return false;
	}
}





pnt.map.Tracking = function(offlineMap, floorManager, option) {
    if(typeof(option)=='undefined') {
        option = {};
    }
    this._option = option;

    this._offlineMap = offlineMap;
    this._floorManager = floorManager;
    this._objectManager = offlineMap.getObjectManager();
    this._currentFloor = this._floorManager.getCurrentFloor();

    this._currentTime = 0;
    this._startTime = 0;
    this._endTime = 0;
    this._period = option.period || 0;
    this._periodLastTime = 0;
    this._mode = 0; // 0:stop, 1:play
    this._speed = option.speed || 10; //
    this._maxAfterimage = option.maxAfterimage || 30; // 잔상 갯수
    this._afterimageRandomColor = option.afterimageRandomColor==true?true:false;
    this._lineColor = option.afterimageColor || [210, 41, 41]; // rgb
    this._lineWidth = option.afterimageWidth || 2;
    this._data = {};
    this._timer = null;
    this._removeMarkerSec = option.removeMarkerSec || 20;
    this._showLabel = option.showLabel==true?true:false;
    this._showMarker = option.showMarker==true?true:false;
    this._autoChangeFloor = option.autoChangeFloor==true?true:false;
    this._noRemove = typeof(option.keepTrace)!='undefined' && option.keepTrace==true?true:false; // 라인제거 유무
    this._timerIndex = 0;

    this._visibleLine = false;
    this._visibleDot = false;
    this._visibleHeatMap = true;

    this._floorDotSource = {};

    this._markers = {};
    this._removeMarkerSecMap = {};
    this._randomColorMap = {};

    this._afterimageLine = {};
    this._dotMarker = {};

    this._heatMapVector = {};
    this._heatMapVector[this._currentFloor] = new ol.source.Vector();
    this._heatMapLayer = new ol.layer.Heatmap({
        source: this._heatMapVector[this._currentFloor],
        opacity: 0.2,
        radius: 3,
        zIndex: 410
    });
    this._offlineMap.getOlMap().addLayer(this._heatMapLayer);


    // 층 변환에 대한 처리
    this._floorManager.onChange(pnt.util.bind(this, function(evt) {
        // 층이 바뀐경우 기존 점들은 숨기고 선택된 층의 점들 표시
        if(typeof(this._dotMarker[evt.floor])=='undefined') {
            this._dotMarker[evt.floor] = [];
        }
        for(var i=0; i<this._dotMarker[evt.floor].length; i++) {
            this._dotMarker[evt.floor][i].show();
        }
        for(var f in this._dotMarker) {
            if(f!=evt.floor) {
                for(var i=0; i<this._dotMarker[f].length; i++) {
                    this._dotMarker[f][i].hide();
                }
            }
        }

        // 층이바뀐 경우 벡터 교체
        if(typeof(this._heatMapVector[evt.floor])=='undefined') {
            this._heatMapVector[evt.floor] = new ol.source.Vector();
        }
        this._heatMapLayer.setSource(this._heatMapVector[evt.floor]);


    }));



    if(typeof(option.timeController)!='undefined' && option.timeController==true) {
        this._element = {};
        this._element.root = document.createElement('div');
        this._element.root.style.padding = '5px';
        this._element.root.style.fontSize = '10pt';

        var topEl = this._offlineMap.getControlElement('left');
        topEl.appendChild(this._element.root);
    }

}
pnt.map.Tracking.prototype.loadData = function(logData) {

    for(var i=0; i<logData.length; i++) {
        var item = logData[i];
        var uuid = item.uuid || item.UUID;
        var id = uuid+'_'+item.majorVer+'_'+item.minorVer;
        var time = item.regDate;
        if(typeof(this._data[id])=='undefined') {
            this._data[id] = {};
        }
        this._data[id][time] = item;
    }
}
pnt.map.Tracking.prototype.getPeriod = function() {
    return this._period;
}
pnt.map.Tracking.prototype.draw = function(time, additional) {

    var currentFloor = this._floorManager.getCurrentFloor();
    var dt = new Date(time*1000);
    //var dt = new Date((time*1000)-(60*60*9*1000));

    var dateString = (dt.getFullYear())+'-'+((dt.getMonth()+1)<10?'0'+(dt.getMonth()+1):(dt.getMonth()+1))+'-'
        +(dt.getDate()<10?'0'+dt.getDate():dt.getDate());

    var timestring = (dt.getHours()<10?'0'+dt.getHours():dt.getHours())+':'
        +(dt.getMinutes()<10?'0'+dt.getMinutes():dt.getMinutes())+':'
        +(dt.getSeconds()<10?'0'+dt.getSeconds():dt.getSeconds());

    if(this._period>0) {
        if(this._periodLastTime + this._period < time) {

            this._offlineMap.getOlMap().dispatchEvent({
                type: 'tracking.period',
                period: this._period,
                time: time,
                object: this,
                objectType: 'tracking'
            });

            this._periodLastTime = time;
        }
    }

    if(typeof(this._option.timeController)!='undefined' && this._option.timeController==true) {
        this._element.root.innerHTML = dateString+' '+timestring;
    }

    for(var id in this._data) {

        var data = this._data[id][time];
        var prevData = null;
        var allowTimeData = 10;
        if(typeof(data)=='undefined' || data == null) {
            var check1 = false;
            var check2 = false;
            for(var checkTime=time-allowTimeData; checkTime<=time+allowTimeData; checkTime++) {
                if(typeof(this._data[id][checkTime])!='undefined')  {
                    if(check1!=true && checkTime<time) {
                        check1 = true;
                        checkTime = time+1;
                    } else if(checkTime>time) {
                        check2 = true;
                        break;
                    }
                }
            }

            if(check1==true && check2==true) {
                for(var i = 1; i<allowTimeData; i++) {
                    if(typeof(this._data[id][time-i])!='undefined') {
                        this._data[id][time] = this._data[id][time-i];
                        data = this._data[id][time];
                        break;
                    }
                }
            }
        }

        for(var i = 1; i<allowTimeData; i++) {
            if(typeof(this._data[id][time-i])!='undefined') {
                prevData = this._data[id][time-i];
                break;
            }
        }

        if(typeof(this._markers[id])=='undefined') {
            this._markers[id] = [];
        }

        /**
         * 층 이동 처리
         */
        if(typeof(data)!='undefined' && this._autoChangeFloor==true && Object.keys(this._data).length==1) {
            if(currentFloor!=data.floor) {
                this._floorManager.changeFloor(data.floor);
            }
        }

        if(typeof(data)!='undefined' && currentFloor==data.floor) {

            var latlng = [data['lng'], data['lat']];
            if(typeof(additional)!='undefined' && additional>0) {
                for(var lindex=additional-1; lindex>=0; lindex--) {
                    var addData = this._data[id][time+lindex+1];
                    if(typeof(addData)!='undefined') {
						latlng = [addData.lng, addData.lat];
                        break;
                    }
                }
            }

            if(typeof(this._markers[id][0])=='undefined') {
                var display = '';
                if(this._showMarker!=true) {
                    display = 'none';
                }
                var options = {
                    data: data,
                    position: pnt.util.transformCoordinates(latlng),
                    tag:'tracking.marker',
                    style: {
                        height:'7px', width:'7px', display: display
                    },
                    autoPan: false
                };

                this._markers[id][0] = this._objectManager.create(pnt.map.object.type.MARKER, 'tracking.marker-'+id, options);
                this._markers[id][0].on('click', function() {
                    console.log('marker data', this.getData())
                });
                if(this._showMarker!=true) {
                    this._markers[id][0].hide();
                }
                if(this._showLabel==true) {
                    //this._markers[id][0].showLabel(timestring+'('+data.minorVer+')');
                    this._markers[id][0].showLabel(timestring);
                }

                this._offlineMap.getOlMap().dispatchEvent({
                    type: 'tracking.newmarker',
                    time: time,
                    beaconid : id,
                    object: this._markers[id][0],
                    objectType: 'tracking'
                });

            } else {

                this._markers[id][0].move(pnt.util.transformCoordinates(latlng));
                if(this._showLabel==true) {
                    //this._markers[id][0].setLabelText(timestring+'('+data.minorVer+')');
                    this._markers[id][0].setLabelText(timestring);
                }
            }

        } else {

            if(typeof(this._removeMarkerSecMap[id])=='undefined' || this._removeMarkerSecMap[id]==null) {
                this._removeMarkerSecMap[id] = 0;
            }

            if(typeof(this._markers[id][0])!='undefined' && this._markers[id][0]!=null) {

                if(this._removeMarkerSecMap[id]>this._removeMarkerSec) {
                    this._objectManager.remove(pnt.map.object.type.MARKER, 'tracking.marker-'+id);
                    this._markers[id].splice(0,1);
                    this._removeMarkerSecMap[id] = 0;
                }

                this._removeMarkerSecMap[id]++;
            }

        }

        if(typeof(data)!='undefined') {

            var dotLatlng = [[data.lng, data.lat, data.floor]];
            if(typeof(additional)!='undefined' && additional>0) {
                for(var lindex=0; lindex<additional; lindex++) {
                    var addData = this._data[id][time+lindex+1];
                    if(typeof(addData)!='undefined') {
						dotLatlng.push([addData.lng, addData.lat, addData.floor]);
                    }
                }
            }


            for(var dindex=0; dindex<dotLatlng.length; dindex++) {
                var latlng = [dotLatlng[dindex][0],dotLatlng[dindex][1]];
                var floor = dotLatlng[dindex][2];

                // 점으로 표시
                if(this._visibleDot==true) {

                    // 점 생성
                    var options = {
                        position: pnt.util.transformCoordinates(latlng),
                        tag: 'tracking.dot',
                        style: {
                            height: '4px', width: '4px', display: display
                        },
                        autoPan: false
                    };
                    var dotMarker = this._objectManager.create(pnt.map.object.type.MARKER, 'tracking.dot-' + pnt.util.makeid(20), options);
                    if(currentFloor!=floor) {
                        dotMarker.hide();
                    }
                    if(typeof(this._dotMarker[floor])=='undefined') {
                        this._dotMarker[floor] = [];
                    }
                    this._dotMarker[floor].push(dotMarker);

                }


                // 히트맵
                if(this._visibleHeatMap==true) {
                    // 히트맵
                    var pointFeature = new ol.Feature({
                        geometry: new ol.geom.Point(pnt.util.transformCoordinates(latlng)), weight: 3
                    });
                    if(typeof(this._heatMapVector[floor])=='undefined') {
                        this._heatMapVector[floor] = new ol.source.Vector();
                    }
                    this._heatMapVector[floor].addFeature(pointFeature);
                }

            }

        }


        if(this._visibleLine==true && typeof(data)!='undefined' && prevData!=null) {

            if (typeof(this._afterimageLine[id]) == 'undefined') {
                this._afterimageLine[id] = {};
            }

            if(this._noRemove!=true) {

                var color = [];
                if (currentFloor != data.floor) {
                    color[0] = 90;
                    color[1] = 90;
                    color[2] = 90;
                } else if (this._afterimageRandomColor == true) {
                    if (typeof(this._randomColorMap[id]) == 'undefined') {
                        this._randomColorMap[id] = pnt.util.randomColor();
                    }
                    color = this._randomColorMap[id];
                } else {
                    color = this._lineColor;
                }

                var lineLatlng = [[prevData.lng, prevData.lat], [data.lng, data.lat]];
                if(typeof(additional)!='undefined' && additional>0) {
                    for(var lindex=0; lindex<additional; lindex++) {
                        var addData = this._data[id][time+lindex+1];
                        if(typeof(addData)!='undefined') {
                            lineLatlng.push([addData.lng, addData.lat]);
                        }
                    }
                }
                var options = {
                    coords: [
						pnt.util.transformCoordinates(lineLatlng[0]),
						pnt.util.transformCoordinates(lineLatlng[1])
					],
                    tag: 'tracking.line2',
                    stroke: {
                        color: [color[0], color[1], color[2], 0.8],
                        width: this._lineWidth
                    }
                }

                if (typeof(this._afterimageLine[id][data.floor]) == 'undefined') {
                    this._afterimageLine[id][data.floor] = {};
                }
                this._afterimageLine[id][data.floor][time] = this._objectManager.create(pnt.map.object.type.LINE, 'tracking.line2-' + id + '-' + (time), options);

            } else {

                if(typeof(this._afterimageLine[id][data.floor])=='undefined' || this._afterimageLine[id][data.floor]==null) {
                    var color = [];
                    if (currentFloor != data.floor) {
                        color[0] = 90;
                        color[1] = 90;
                        color[2] = 90;
                    } else if (this._afterimageRandomColor == true) {
                        if (typeof(this._randomColorMap[id]) == 'undefined') {
                            this._randomColorMap[id] = pnt.util.randomColor();
                        }
                        color = this._randomColorMap[id];
                    } else {
                        color = this._lineColor;
                    }
                    var lineLatlng = [[prevData.lng, prevData.lat], [data.lng, data.lat]];
                    if(typeof(additional)!='undefined' && additional>0) {
                        for(var lindex=0; lindex<additional; lindex++) {
                            var addData = this._data[id][time+lindex+1];
                            if(typeof(addData)!='undefined') {
								lineLatlng.push([addData.lng, addData.lat]);
                            }
                        }
                    }

                    var options = {
                        coords: [
							pnt.util.transformCoordinates(lineLatlng[0]),
							pnt.util.transformCoordinates(lineLatlng[1])
						],
                        tag: 'tracking.line2',
                        stroke: {
                            color: typeof(color)=='string'?color:[color[0], color[1], color[2], 0.8],
                            width: this._lineWidth
                        }
                    }
                    this._afterimageLine[id][data.floor] = this._objectManager.create(pnt.map.object.type.LINE, 'tracking.line2-' + id + '-' + (time), options);

                } else {

                    var lineLatlng = [[prevData.lng, prevData.lat], [data.lng, data.lat]];
                    if(typeof(additional)!='undefined' && additional>0) {
                        for(var lindex=0; lindex<additional; lindex++) {
                            var addData = this._data[id][time+lindex+1];
                            if(typeof(addData)!='undefined') {
								lineLatlng.push([addData.lng, addData.lat]);
                            }
                        }
                    }

                    var source = this._afterimageLine[id][data.floor]._vectorSource;
                    var feature = new ol.Feature({
                        geometry: new ol.geom.LineString(lineLatlng)
                    });
                    feature.setStyle(new ol.style.Style(this._afterimageLine[id][data.floor].styleJson));
                    source.addFeature(feature);
                }

            }

        }

    }


    if(this._noRemove!=true) {
        for (var lineBeaconId in this._afterimageLine) {
            for (var lineFloor in this._afterimageLine[lineBeaconId]) {
                for (var lineTime in this._afterimageLine[lineBeaconId][lineFloor]) {
                    var line = this._afterimageLine[lineBeaconId][lineFloor][lineTime];

                    if ((time - lineTime) >= this._maxAfterimage || lineTime > time) {
                        // 삭제
                        this._objectManager.remove(pnt.map.object.type.LINE, line.getId());
                        delete this._afterimageLine[lineBeaconId][lineFloor][lineTime];
                    } else {
                        if (line) {
                            // 투명도 조정
                            var transparency = 1 - ((time - lineTime) / this._maxAfterimage);
                            var color = [];
                            if (currentFloor != lineFloor) {
                                color[0] = 180;
                                color[1] = 180;
                                color[2] = 180;
                            } else if (this._afterimageRandomColor == true) {
                                if (typeof(this._randomColorMap[lineBeaconId]) == 'undefined') {
                                    this._randomColorMap[lineBeaconId] = pnt.util.randomColor();
                                }
                                color = this._randomColorMap[lineBeaconId];
                            } else {
                                color = this._lineColor;
                            }
                            line.setStyle({
                                color: typeof(color)=='string'?color:[color[0], color[1], color[2], transparency * 0.8],
                                width: this._lineWidth
                            });
                        }
                    }
                }
            }
        }
    }

    this._currentFloor = currentFloor;
}
pnt.map.Tracking.prototype.clear = function() {
    this.stop();

    for(var lineBeaconId in this._afterimageLine) {
        for(var lineFloor in this._afterimageLine[lineBeaconId]) {

            if(this._afterimageLine[lineBeaconId][lineFloor]._vectorSource instanceof ol.source.Vector) {

                var line = this._afterimageLine[lineBeaconId][lineFloor];
                this._objectManager.remove(pnt.map.object.type.LINE, line.getId());

            } else {

                for(var lineTime in this._afterimageLine[lineBeaconId][lineFloor]) {
                    var line = this._afterimageLine[lineBeaconId][lineFloor][lineTime];
                    this._objectManager.remove(pnt.map.object.type.LINE, line.getId());
                }
            }
        }
    }

    for(var id in this._markers) {
        for(var key in this._markers[id]) {
            if(typeof(this._markers[id][key])!='undefined' && this._markers[id][key]!=null) {
                this._objectManager.remove(pnt.map.object.type.MARKER, this._markers[id][key].getId());
            }
        }
    }

    var dots = this._objectManager.findTag('tracking.dot');
    for(var i=0;i <dots.length; i++) {
        this._objectManager.remove(pnt.map.object.type.MARKER, dots[i].getId());
    }
    for(var f in this._heatMapVector) {
        this._heatMapVector[f].clear();
    }

    this._markers = {};
    this._removeMarkerSecMap = {};
    this._afterimageLine = {};
    this._periodLastTime = 0;
    this._timerIndex = 0;
    this._data = {};
}
pnt.map.Tracking.prototype.nextFrame = function(exectime) {

    this._timerIndex++;

    if(typeof(exectime)=='undefined') {
        exectime = 0;
    }
    var millsec;
    if(this._speed>10) {
        millsec = ((1/10) * 1000)-exectime;
    } else {
        millsec = ((1/this._speed) * 1000)-exectime;
    }
    this._timer = window.setTimeout(pnt.util.bind(this, function(evt) {

        this._timer = null;
        var stime = (new Date()).getTime();


        // 속도가 10이상이면 화면 재생율을 고정하고 추가적인 속도 만큼 더 많은 좌표를 처리하도록
        var additional = 0;
        if(this._speed>10) {
            additional = Math.floor((this._speed-1)/10)-1;

            var ratio = (this._speed-((additional+1)*10))/10;
            if(ratio*10>this._timerIndex-Math.floor(this._timerIndex*0.1)*10) {
                additional += 1;
            }
        }

        /*if(this._timerIndex%10==0) {
            if(typeof(this._dtime)!='undefined') {
                console.log('_dtime', (this._currentTime)-this._dtime);
            }
            this._dtime = this._currentTime;
        }*/

        this.draw(this._currentTime, additional);
        this._currentTime += additional;

        var etime = (new Date()).getTime()-stime;

        this._currentTime = this._currentTime + 1;
        if(this._mode==1) {
            if( (this._endTime>0 && this._endTime<this._currentTime) || (( (stime/1000)+(60*60*9))<=this._currentTime) ) {

                this.stop();

				/*if(this._startTime>0) {
				 this._currentTime = this._startTime;
				 }*/
            } else {
                this.nextFrame(etime);
            }
        }
    }), millsec>0?millsec:0);
}
pnt.map.Tracking.prototype.play = function(option) {
    this._mode = 1;

    if(typeof(option)=='undefined') {
        option = {};
    }

    if(typeof(option.startTime)!='undefined') {
        this._startTime = option.startTime;
        this._currentTime = option.startTime;
    }
    if(typeof(option.endTime)!='undefined') {
        this._endTime = option.endTime;
    }
    if(typeof(option.period)!='undefined') {
        this._period = option.period;
    }

    this.nextFrame();
}
pnt.map.Tracking.prototype.stop = function() {
    this._mode = 0;
    if(this._timer!=null) {
        window.clearTimeout(this._timer);
        this._timer = null;
    }
}
pnt.map.Tracking.prototype.setDisplayMode = function(mode) { // line(default), dot, heatmap,
    if(mode=='line') {
        this._visibleLine = true;
        this._visibleDot = false;
        this._visibleHeatMap = false;
    }
    else if(mode=='dot') {
        this._visibleLine = false;
        this._visibleDot = true;
        this._visibleHeatMap = false;
    }
    else if(mode=='heatmap') {
        this._visibleLine = false;
        this._visibleDot = false;
        this._visibleHeatMap = true
    }
}
pnt.map.Tracking.prototype.setLineColor = function(color) {
    this._lineColor = color;
}
pnt.map.Tracking.prototype.setSpeed = function(speed) {
    this._speed = speed;
}





pnt.map.CoordinateBot = function(id, extent, data, callback, period, offlineMap) {
    this._id = id;
    this._callback = callback;
    this._intervalListener = null;
    this._currentCoordinate = null;
    this._data = data;
    this._period = period;
    this._targetCoordinate = null;
    this._nextCoordinates = [];
    this._extent = extent; // [[127.05655645337298,37.51097520552254],[127.05678980555727,37.51109188161469]]
    this._started = false;
    this._autoTarget = true; // 자동 목적지 생성 여부
    this._errorRangeMeter = 5;
    this._offlineMap = offlineMap || null;
    this._lineColor = pnt.util.randomColor();
    this._playCoordinateIndex = 0;
}
pnt.map.CoordinateBot.prototype.generateTargetCoordinate = function(targetCoordinate, speed, stay, data, doNotCallback) {
    var speed = speed || 1;

    if(this._currentCoordinate==null) {
        this._currentCoordinate = targetCoordinate;
        return;
    }
    var lastCoordinate = this._currentCoordinate;
    if(this._nextCoordinates.length>0) {
        lastCoordinate = this._nextCoordinates[this._nextCoordinates.length-1].coordinate;
    }

    var distance = pnt.util.distance(pnt.util.transformLonLat(lastCoordinate), pnt.util.transformLonLat(targetCoordinate), 'meter');
    var count = Math.round(distance*(1/speed));
    if(count<3) {
        count = 3;
    }

    if(typeof(this._offlineMap)!='undefined' && this._offlineMap!=null) {
        var om = this._offlineMap.getObjectManager();
        var options = {
            coords:[lastCoordinate, targetCoordinate],
            tag:'bot-line', stroke:{color:[this._lineColor[0], this._lineColor[1], this._lineColor[2], 0.4], width:1}
        }
        om.create(pnt.map.object.type.LINE, 'bot-line-'+this._id+'-'+pnt.util.makeid(5), options);
    }

    var p1 = (targetCoordinate[0]-lastCoordinate[0])/(count-1);
    var p2 = (targetCoordinate[1]-lastCoordinate[1])/(count-1);
    for(var i=1; i<count-1; i++) {
        this._nextCoordinates.push({
            coordinate: [lastCoordinate[0]+(p1*i),lastCoordinate[1]+(p2*i)],
            doNotCallback: typeof(doNotCallback)!='undefined' && doNotCallback==true?true:false,
            data: data
        });
    }
    this._nextCoordinates.push({
        coordinate: targetCoordinate,
        doNotCallback: typeof(doNotCallback)!='undefined' && doNotCallback==true?true:false,
        data: data
    });

    this.generateStayCoordinate(this._nextCoordinates[this._nextCoordinates.length-1].coordinate, stay, data, doNotCallback);

}
pnt.map.CoordinateBot.prototype.generateTargetRandomCoordinate = function() {

    var diffLng, diffLat;
    if(this._extent[0]>this._extent[2]) {
        diffLng = this._extent[2]-this._extent[0];
        diffLat = this._extent[3]-this._extent[1];
    } else {
        diffLng = this._extent[0]-this._extent[2];
        diffLat = this._extent[1]-this._extent[3];
    }

    if(this._currentCoordinate==null) {
        this._currentCoordinate = [this._extent[2]+(Math.random()*diffLng),
            this._extent[3]+(Math.random()*diffLat)];
    }

    this._targetCoordinate = [this._extent[2]+(Math.random()*diffLng),
        this._extent[3]+(Math.random()*diffLat)];

    this.generateTargetCoordinate(this._targetCoordinate, 1);

}
pnt.map.CoordinateBot.prototype.generateStayCoordinate = function(coordinate, staySec, data, doNotCallback) {
    // 목적지 도착후 유지 시간
    var sec = staySec;
    if(typeof(staySec)=='undefined') {
        var max = [10,10,10,10,30,30,120];
        sec = Math.round(Math.random()*max[Math.round(Math.random()*(max.length-1))]);
    }
    for(var i=0; i<sec; i++) {
        this._nextCoordinates.push({
            coordinate: coordinate,
            doNotCallback: typeof(doNotCallback)!='undefined' && doNotCallback==true?true:false,
            data: data
        });
    }
}
pnt.map.CoordinateBot.prototype.nextCoordinate = function() {

	/*if(this._nextCoordinates.length==0) {
	 if(this._autoTarget==true) {
	 this.generateTargetRandomCoordinate();
	 } else {
	 this.stop();
	 return this._currentCoordinate;
	 }
	 }*/
    //this._currentCoordinate = this._nextCoordinates.shift();

    if(typeof(this._nextCoordinates[this._playCoordinateIndex])=='undefined') {
        if(this._autoTarget==true) {
            this.generateTargetRandomCoordinate();
        } else {
            this.stop();
            return {
                coordinate: this._currentCoordinate,
                doNotCallback: true,
                data: {}
            };
        }
    }

    var nextCoordinate = this._nextCoordinates[this._playCoordinateIndex];
    this._playCoordinateIndex++;
    this._currentCoordinate = nextCoordinate.coordinate;

    // 좌표 오차 생성
	var lonlat = pnt.util.transformLonLat(this._currentCoordinate);
    var lngDiff  = Math.random()*(0.000007*this._errorRangeMeter);
    var latDiff  = Math.random()*(0.000007*this._errorRangeMeter);
    var toLng = Math.random()>0.5?lonlat[0]-lngDiff:lonlat[0]+lngDiff;
    var toLat = Math.random()>0.5?lonlat[1]-latDiff:lonlat[1]+latDiff;

    this._currentCoordinate = pnt.util.transformCoordinates([toLng, toLat]);
    return {coordinate: this._currentCoordinate,
        doNotCallback: nextCoordinate.doNotCallback,
        data: nextCoordinate.data || {}
    };
}
pnt.map.CoordinateBot.prototype.start = function() {
    this._started = true;
    if(this._intervalListener==null) {
        this._intervalListener = window.setInterval(pnt.util.bind(this, function() {
            var nextCoordinate = this.nextCoordinate();
            //console.log('nextCoordinate', nextCoordinate);
            if(typeof(nextCoordinate.doNotCallback)=='undefined' || nextCoordinate.doNotCallback!=true) {
                this._callback(this._id, nextCoordinate, this._data, this);
            }
        }), this._period);
    }
}
pnt.map.CoordinateBot.prototype.stop = function() {
    window.clearInterval(this._intervalListener);
    this._intervalListener = null;
    this._started = false;
}
pnt.map.CoordinateBot.prototype.restart = function() {
    if(this.started()==true) {
        this.stop();
    }
    this._playCoordinateIndex = 0;
    this.start();
}
pnt.map.CoordinateBot.prototype.started = function() {
    return this._started;
}
pnt.map.CoordinateBot.prototype.setAutoTarget = function(enabled) {
    this._autoTarget = enabled;
}
pnt.map.CoordinateBot.prototype.setErrorRange = function(errorRange) {
    this._errorRangeMeter = errorRange;
}





pnt.util = pnt.util || {};

// Mozilla, Opera, Webkit
if (document.addEventListener) {
    document.addEventListener("DOMContentLoaded", function () {
        document.removeEventListener("DOMContentLoaded", arguments.callee, false);
        pnt.util._domReady();
    }, false);


}
// Internet Explorer
else if (document.attachEvent) {
    document.attachEvent("onreadystatechange", function () {
        if (document.readyState === "complete") {
            document.detachEvent("onreadystatechange", arguments.callee);
            pnt.util._domReady();
        }
    });
}
pnt.util._onreadyCallbacks = [];
pnt.util.makeid = function(length) {
    var text = "";
    var possible = "abcdefghijklmnopqrstuvwxyz0123456789";

    for( var i=0; i < length; i++ )
        text += possible.charAt(Math.floor(Math.random() * possible.length));
    return text;
}
pnt.util.randomColor = function() {
    var r = Math.floor(Math.random() * 3);
    var p1 = 0;
    var p2 = 0;
    var p3 = 0;
    if(r==0) {
        p1 = 30;
    }
    else if(r==0) {
        p2 = 30;
    }
    else if(r==0) {
        p3 = 30;
    }

    var v1 = Math.floor(Math.random() * (128+p1));
    if(Math.floor(Math.random() * 2)==1) {
        v1+=(128-p1);
    }
    var v2 = Math.floor(Math.random() * (128+p2));
    if(Math.floor(Math.random() * 2)==1) {
        v2+=(128-p2);
    }
    var v3 = Math.floor(Math.random() * (128+p3));
    if(Math.floor(Math.random() * 2)==1) {
        v3+=(128-p3);
    }

    return [v1, v2, v3];
}
pnt.util.findArrayData = function(array, condition) {
    var result = [];
    for(var i=0; i<array.length; i++) {
        for(var column in condition) {
            if(array[i][column]==condition[column]) {
                result.push(array[i]);
                continue;
            }
        }
    }
    return result;
}
pnt.util.getUrlParameterMap = function() {

    var parameterMap = {};
    var url = location.href;
    if(url.indexOf('?')>-1) {
        if(url.indexOf('#')>-1) {
            url = url.substring(0, url.indexOf('#'));
        }
        var parameters = (url.slice(url.indexOf('?') + 1, url.length)).split('&');
        for (var i = 0; i < parameters.length; i++) {
            var varName = parameters[i].split('=')[0];
            parameterMap[varName] = pnt.util.trim(decodeURIComponent(parameters[i].split('=')[1]));
        }
    }

    return parameterMap;
}
pnt.util.getUrlParameter = function(param) {
    var params = pnt.util.getUrlParameterMap();
    if(typeof(params[param])!='undefined') {
        return params[param];
    } else {
        return false;
    }
}
pnt.util.split = function(str, separator) {
    if(typeof(str)=='undefined' || str==null || str==false) {
        return [];
    }  else {
        return str.split(separator);
    }
}
pnt.util.trim = function(str) {
    if(typeof(str)=='undefined' || str==null || str==false) {
        return '';
    } else {
        return str.replace(/(^\s*)|(\s*$)/gi, "");
    }
}
pnt.util.replaceAll = function(str, searchStr, replaceStr) {
    return str.split(searchStr).join(replaceStr);
}
pnt.util.inArray = function(value, array) {
    if(typeof(array)=='undefined' || array==null || array==false) {
        array = [];
    }
    for(var i=0; i<array.length; i++) {
        if(pnt.util.trim(array[i])==value) {
            return true;
        }
    }
    return false;
}
pnt.util.parseDate = function(datetime) {
    datetime = pnt.util.replaceAll(datetime, '-','');
    datetime = pnt.util.replaceAll(datetime, '/','');
    datetime = pnt.util.replaceAll(datetime, ':','');
    datetime = pnt.util.replaceAll(datetime, ' ','');
    var year = datetime.substring(0,4);
    var month = parseInt(datetime.substring(4,6))-1;
    if(month<10) {
        month = '0'+month;
    }
    var day = datetime.substring(6,8);
    var hour = datetime.substring(8,10);
    var minute = datetime.substring(10,12);
    var sec = datetime.substring(12,14);

    var date = new Date(year, month, day, hour, minute, sec);
    return date;
}
pnt.util.dateformat = function(time, format) {

    var weekName = ["일요일", "월요일", "화요일", "수요일", "목요일", "금요일", "토요일"];

    var stuff = function(str, len) {
        var s = '', i = 0;
        while (i++ < len) {
            s += str;
        }
        return s;
    };

    var zf = function(str, len){
        if(typeof(str)=='number') {
            str = str.toString();
        }

        return stuff("0", len - str.length)+str;
    };


    var d = new Date(time);
    return format.replace(/(yyyy|yy|MM|dd|E|hh|mm|ss|a\/p)/gi, function($1) {
        switch ($1) {
            case "yyyy": return d.getFullYear();
            case "yy": return zf((d.getFullYear() % 1000),2);
            case "MM": return zf((d.getMonth() + 1), 2);
            case "dd": return zf(d.getDate(), 2);
            case "E": return weekName[d.getDay()];
            case "HH": return zf(d.getHours(), 2);
            case "hh": return zf(((h = d.getHours() % 12) ? h : 12),2);
            case "mm": return zf(d.getMinutes(), 2);
            case "ss": return zf(d.getSeconds(), 2);
            case "a/p": return d.getHours() < 12 ? "오전" : "오후";
            default: return $1;
        }
    });
}
pnt.util.bind = function(scope, fn) {
    return function () {
        fn.apply(scope, arguments);
    };
}
pnt.util._domReady = function() {
    for(var i=0; i<pnt.util._onreadyCallbacks.length; i++) {
        pnt.util._onreadyCallbacks[i]();
    }
}
pnt.util.onReady = function(callback) {
    pnt.util._onreadyCallbacks.push(callback);
}
pnt.util.addEventListener = function(element, eventType, callback, _this, useCapture) {

    if(typeof(capture)=='undefined') {
        useCapture = false;
    }
    if(element.addEventListener) {
        if(typeof(_this)!='undefined') {
            return element.addEventListener(eventType, pnt.util.bind(_this, callback), useCapture);
        } else {
            return element.addEventListener(eventType, callback, useCapture);
        }

    } else if(element.attachEvent) {
        if(typeof(_this)!='undefined') {
            return element.attachEvent('on'+eventType, pnt.util.bind(_this, callback), useCapture);
        } else {
            return element.attachEvent('on'+eventType, callback, useCapture);
        }
    }
}
pnt.util.removeEventListener = function(element, eventType, listener) {
    if(element.removeEventListener) {
        element.removeEventListener(eventType, listener);
    } else if(element.detachEvent) {
        element.detachEvent('on'+eventType, listener);
    }
}
pnt.util.dispatchEvent = function(target, eventType, eventExtends) {
    if (window.document.createEvent) {
        var event = new Event(eventType);
        for(var key in eventExtends) {
            event[key] = eventExtends[key];
        }
        target.dispatchEvent(event);
    } else {
        var event = window.document.createEventObject();
        for(var key in eventExtends) {
            event[key] = eventExtends[key];
        }
        target.fireEvent('on' + eventType, event);
    }
}
pnt.util.centroidOfPolygon = function(coordinates) {
    var lngsum = 0.0;
    var latsum = 0.0;

    for(var i=0;i<coordinates.length; i++) {
        var position = coordinates[i];
        lngsum += position[0];
        latsum += position[1];
    }

    return [lngsum/coordinates.length, latsum/coordinates.length];
}
pnt.util.transformCoordinates = function(lonlat) {
	return ol.proj.transform(lonlat, 'EPSG:4326', 'EPSG:3857');
}
pnt.util.transformLonLat = function(coordinates) {
	return ol.proj.transform(coordinates, 'EPSG:3857', 'EPSG:4326');
}
pnt.util.transformExtent = function(extent) {
	return pnt.util.transformExtentCoordinates(extent);
}
pnt.util.transformExtentCoordinates = function(extent) {
	var e1 = ol.proj.transform([extent[0], extent[1]], 'EPSG:4326', 'EPSG:3857');
	var e2 = ol.proj.transform([extent[2], extent[3]], 'EPSG:4326', 'EPSG:3857');
	return [e1[0], e1[1], e2[0], e2[1]];
}
pnt.util.transformExtentLonLat = function(extent) {
	var e1 = ol.proj.transform([extent[0], extent[1]], 'EPSG:3857', 'EPSG:4326');
	var e2 = ol.proj.transform([extent[2], extent[3]], 'EPSG:3857', 'EPSG:4326');

	return [e1[0], e1[1], e2[0], e2[1]];
}
pnt.util.sortPolygonCoordinates = function(coordinates) {
    var id = pnt.util.makeid(6);
    var coordsSort = coordinates.sort(function(a, b) {
        return a[1]-b[1];
    });
    var resultCoordinates = [coordsSort[0]];
    for(var i=1; i<coordsSort.length; i++) {
        var r = (coordsSort[coordsSort.length-1][1]-coordsSort[i][1])/(coordsSort[coordsSort.length-1][1]-coordsSort[0][1]);
        var middle = null;
        if(coordsSort[0][0]<coordsSort[coordsSort.length-1][0]) {
            middle = coordsSort[coordsSort.length-1][0]-((coordsSort[coordsSort.length-1][0]-coordsSort[0][0])*r);
        } else {
            middle = coordsSort[coordsSort.length-1][0]+((coordsSort[0][0]-coordsSort[coordsSort.length-1][0])*r);
        }
        if(coordsSort[i][0]<middle) {
            resultCoordinates.unshift(coordsSort[i]);
        } else {
            resultCoordinates.push(coordsSort[i]);
        }
    }
    return resultCoordinates;
}
pnt.util.distance = function(coordinate1, coordinate2, unit) {

    var deg2rad = function(deg) {
        return (deg * Math.PI / 180.0);
    }
    var rad2deg = function(rad) {
        return (rad * 180 / Math.PI);
    }

    var theta = coordinate1[0] - coordinate2[0];
    var dist = Math.sin(deg2rad(coordinate1[1])) * Math.sin(deg2rad(coordinate2[1]))
        + Math.cos(deg2rad(coordinate1[1])) * Math.cos(deg2rad(coordinate2[1]))
        * Math.cos(deg2rad(theta));
    dist = Math.acos(dist);
    dist = rad2deg(dist);
    var mile = dist * 60 * 1.1515; // mile

    if(unit=='kilometer') { // kilometer
        return mile * 1.609344;
    } else if(unit=='mile') { // mile
        return mile;
    } else { // meter
        return mile * 1609.344;
    }
}
pnt.util.fetch = function(option) {
    if(typeof(option.method)=='undefined') {
        option.method = 'get';
    }
    if(typeof(option.type)!='undefined' && option.type=='fetch' && typeof(fetch)!='undefined') {
        option.type = 'fetch';
    } else {
        option.type = 'basic';
    }

    var data = '';

    if(typeof(option.data)!='undefined') {
        if(typeof(option.data)=='string') {
            if(option.data.substring(0,1)=='?') {
                data = option.data.substring(1);
            } else {
                data = option.data;
            }
        } else {
            var paramArray = [];
            for(var key in option.data) {
                var value = option.data[key];
                paramArray.push(key+'='+value);
            }
            data = paramArray.join('&');
        }
    }

    if(option.url.indexOf('?')>-1) {
        if(data!='') {
            data += '&';
        }
        data += option.url.substring(option.url.indexOf('?')+1);
        option.url = option.url.substring(0, option.url.indexOf('?'));
    }

    if(option.type=='fetch') {

        var detail = {};
        var header = {};

        if(typeof(option.contentType)!='undefined') {
            header['Content-type'] = option.contentType;
        }

        if(typeof(option.accept)!='undefined') {
            header['Accep'] = option.accept;
        }


        if(option.method=='post') {
            detail = {
                method: 'post',
                body: data,
                headers: header
            };
        } else {
            detail = {
                headers: header
            };
            option.url = option.url+'?'+data;
        }

        fetch(option.url, detail).then(function(response) {
            if (response.status !== 200) {
                console.error('pnt.util.fetch error', 'Looks like there was a problem. Status Code: ' + response.status);
                return;
            }

            if(typeof(option.success)!='undefined') {
                if (option.responseType == 'json') {
                    response.json().then(function (data) {
                        console.debug('pnt.util.fetch onload', data);
						if(typeof(data)=='string') {
							var json = JSON.parse(data);
							if(typeof(json)=='string') {
								option.success(JSON.parse(json));
							} else {
								option.success(json);
							}
						} else {
							option.success(data);
						}
                    });
                } else {
                    response.text().then(function (data) {
                        console.debug('pnt.util.fetch onload', data);
                        option.success(data);
                    });
                }
            }
            if(typeof(option.complete)!='undefined') {
                console.debug('pnt.util.fetch complete');
                option.complete();
            }
        }).catch(function(error) {
            console.error('pnt.util.fetch error', error);
            if(typeof(option.fail)!='undefined') {
                option.fail(error);
            }
            if(typeof(option.complete)!='undefined') {
                console.debug('pnt.util.fetch complete');
                option.complete();
            }
        });

    } else {
        var request = new XMLHttpRequest();

        request.onload = function(evt) {

            if(this.status==200) {
                console.debug('pnt.util.fetch onload', this.response);
                if (typeof(option.success) != 'undefined') {
                    var response = this.responseText;
                    if (option.responseType == 'json') {
                        var error = false;
						if(typeof(response)=='string') {
							var json = JSON.parse(response);
							if(typeof(json)=='string') {
								option.success(JSON.parse(json));
							} else {
								option.success(json);
							}
						} else {
							option.success(response);
						}
                    } else {
                        option.success(response);
                    }
                }
            } else {
                console.error('pnt.util.fetch error', this.statusText+'('+this.status+')');
                if (typeof(option.fail) != 'undefined') {
                    option.fail(this.statusText+'('+this.status+')');
                }
            }
            if(typeof(option.complete)!='undefined') {
                console.debug('pnt.util.fetch complete');
                option.complete();
            }
        };
        request.onerror = function(evt) {
            console.debug('pnt.util.fetch onerror', this.statusText);
            if(typeof(option.fail)!='undefined') {
                option.fail(evt);
            }
            if(typeof(option.complete)!='undefined') {
                console.debug('pnt.util.fetch complete');
                option.complete();
            }
        };

        try {
            if(option.method=='post') {
                request.open(option.method, option.url, true);
                var bodyJson = false;
                if(typeof(option.bodyType)!='undefined' && option.bodyType=='json') {
                    bodyJson = true;
                }
                if(typeof(option.contentType)!='undefined') {
                    request.setRequestHeader('Content-type', option.contentType);
                }
                if(typeof(option.accept)!='undefined') {
                    request.setRequestHeader('Accept', option.accept);
                }

                if(bodyJson==true) {
                    request.send(JSON.stringify(option.data));
                } else {
                    request.send(data);
                }
            } else {
                request.open(option.method, option.url+'?'+data, true);
                if(typeof(option.contentType)!='undefined') {
                    request.setRequestHeader('Content-type', option.contentType);
                }
                if(typeof(option.accept)!='undefined') {
                    request.setRequestHeader('Accept', option.accept);
                }
                request.send();
            }
        } catch(error) {
            console.error('pnt.util.fetch error', this, error);
            if(typeof(option.fail)!='undefined') {
                option.fail(error);
            }
        }
    }
}
pnt.util.get = function(url, data, handler) {
    pnt.util.fetch({
        url: url,
        data: data,
        method: 'get',
        contentType: 'application/x-www-form-urlencoded',
        responseType: 'json',
        success: handler.success || undefined,
        error: handler.error || undefined,
        complete: handler.complete || undefined
    });
}
pnt.util.post = function(url, data, handler) {
    pnt.util.fetch({
        url: url,
        data: data,
        method: 'post',
        contentType: 'application/x-www-form-urlencoded',
        responseType: 'json',
        success: handler.success || undefined,
        error: handler.error || undefined,
        complete: handler.complete || undefined
    });
}
pnt.util.loadJavascript = function(url, callback) {
    var head = document.getElementsByTagName('head')[0];
    var script= document.createElement('script');
    script.type = 'text/javascript';
    script.src = url;
    script.onload = callback;
    head.appendChild(script);
}






pnt.util.DataLoader = function(urls) {

    console.debug('pnt.util.DataLoader urls:', JSON.stringify(urls));

    this._urls = urls || {};
    this._data = {};
    this._urlCount = 0;
    this._completeCount = 0;
    this._errorCount = 0;
    this._callback = null;
    for(var key in urls) {
        this._urls[key] = {
            url: urls[key],
            complete: false,
            error: null
        }
        this._urlCount++;
    }
}
pnt.util.DataLoader.prototype.addUrl = function(id, url, param) {
    if(typeof(param)=='undefined') {
        param = {};
    }
    console.debug('pnt.util.DataLoader.addUrl id:', id, ' url:'+url);
    this._urls[id] = {url:url, param:param, complete: false, error: null};
    this._urlCount++;
}
pnt.util.DataLoader.prototype.setCompleteData = function(id, data) {
    console.debug('pnt.util.DataLoader.setCompleteData id:', id);
    this._data[id] = data;
    this._urls[id].complete = true;
    this._completeCount++;
}
pnt.util.DataLoader.prototype.setError = function(id, error) {
    console.debug('pnt.util.DataLoader.setError id:', id, ' error:', error);
    this._errorCount++;
    this._completeCount++;
    this._urls[id].error = error;
}
pnt.util.DataLoader.prototype.isComplete = function() {
    if(this._completeCount>=this._urlCount) {
        return true;
    } else {
        return false;
    }
}
pnt.util.DataLoader.prototype.isError = function() {

    if(this._errorCount>0) {
        return true;
    } else {
        return false;
    }
}
pnt.util.DataLoader.prototype.getData = function(id) {
    if(typeof(id)!='undefined') {
        if(typeof(this._data[id])!='undefined') {
            return this._data[id];
        } else {
            return false;
        }
    } else {
        return this._data;
    }
}
pnt.util.DataLoader.prototype.clearData = function() {
    this._completeCount = 0;
    this._errorCount = 0;
    for(var id in this._data) {
        delete this._data[id];
    }
}
pnt.util.DataLoader.prototype.onLoad = function(callback) {
    this._callback = callback;
}
pnt.util.DataLoader.prototype.loadRemoteData = function() {

    this.clearData();

    console.debug('pnt.util.DataLoader.loadRemoteData start');
    var callback = this._callback;
    for(var id in this._urls) {
        var url = this._urls[id].url;
        var param = this._urls[id].param;
        (function(id, url, param, loader) {

            pnt.util.fetch({
                url: url,
                data: param,
                responseType: 'json',
                acceptCharset: 'UTF-8',
                contentType: 'applicatoin/json;charset=UTF-8',
                success: function(response) {
                    console.debug('pnt.util.DataLoader.loadRemoteData complete id:', id);

                    loader.setCompleteData(id, response);
                    if(typeof(callback)!='undefined' && callback!=null) {
                        callback.call(loader, id, response, loader.isComplete(), null);
                    }
                },
                fail: function(error) {
                    console.debug('pnt.util.DataLoader.loadRemoteData id:',id,' error:', error);

                    loader.setError(id, error);
                    if(typeof(callback)!='undefined' && callback!=null) {
                        callback.call(loader, id, null, loader.isComplete(), error);
                    }
                }
            });

        })(id, url, param, this);
    }
}
pnt.util.DataLoader.prototype.reload = function(param) {
    for(var id in this._urls) {

        for(var pid in param) {
            this._urls[id].param[pid] = param[pid];
        }
    }
    this.loadRemoteData();
}
pnt.util.DataLoader.prototype.load = function(callback) {
    if(typeof(callback)!='undefined') {
        this._callback = callback;
    }
    this.loadRemoteData();
}






// event type : create, modify, remove
pnt.util.Properties = function() {
    this._prop = {};
    this._events = {};
    this._eventIdMap = {};
}
pnt.util.Properties.eventType = {}
pnt.util.Properties.eventType.CREATE = 'create';
pnt.util.Properties.eventType.MODIFY = 'modify';
pnt.util.Properties.eventType.REMOVE = 'remove';
pnt.util.Properties.eventType.PUT = 'put';
pnt.util.Properties.prototype.put = function(id, value) {
    if(typeof(this._prop[id])=='undefined') {
        this._execCallback(id, pnt.util.Properties.eventType.CREATE, value);
    } else {
        if(this._prop[id]!=value) {
            this._execCallback(id, pnt.util.Properties.eventType.MODIFY, value);
        }
    }
    this._execCallback(id, pnt.util.Properties.eventType.PUT, value);
    this._prop[id] = value;
}
pnt.util.Properties.prototype._execCallback = function(id, eventType, value) {
    if(typeof(this._events[id])!='undefined' && typeof(this._events[id][eventType])!='undefined') {
        for(var i=0; i<this._events[id][eventType].length; i++) {
            var eventId = this._events[id][eventType][i];
            if(typeof(value)!='undefined') {
                this._eventIdMap[eventId].callback.call(this, id, value);
            } else {
                this._eventIdMap[eventId].callback.call(this, id);
            }
        }
    }
}
pnt.util.Properties.prototype.has = function(id) {
	if(typeof(this._prop[id])!='undefined' && this._prop[id]!=null) {
		return true;
	} else {
		return false;
	}
}
pnt.util.Properties.prototype.get = function(id) {
    return this._prop[id];
}
pnt.util.Properties.prototype.remove = function(id) {
    this._execCallback(id, pnt.util.Properties.eventType.REMOVE);

    for(var eventType in this._events[id]) {
        if(typeof(this._events[id][eventType])!='undefined') {
            for(var i=0; i<this._events[id][eventType].length; i++) {
                var eventId = this._events[id][eventType][i];
                delete this._eventIdMap[eventId];
            }
            delete this._events[id][eventType];
        }
    }

    if(typeof(this._prop[id])!='undefined') {
        // remove
        delete this._prop[id];
    }
}
pnt.util.Properties.prototype.on = function(id, eventType, callback) {
    var eventId = pnt.util.makeid(20);

    if(typeof(this._events[id])=='undefined') {
        this._events[id] = {};
    }
    if(typeof(this._events[id][eventType])=='undefined') {
        this._events[id][eventType] = [];
    }
    this._events[id][eventType].push(eventId);

    this._eventIdMap[eventId] = {
        id: id, eventType: eventType, callback: callback
    }

    return eventId;
}
pnt.util.Properties.prototype.un = function(eventId) {

    var eventInfo = this._eventIdMap[eventId];
    if(typeof(this._events[eventInfo.id])!='undefined'
        && typeof(this._events[eventInfo.id][eventInfo.eventType])!='undefined') {

        for(var i=0; i<this._events[eventInfo.id][eventInfo.eventType].length; i++) {
            var eid = this._events[eventInfo.id][eventInfo.eventType][i];
            if(eid==eventId) {
                this._events[eventInfo.id][eventInfo.eventType].splice(i, 1);
            }
        }
    }

    delete this._eventIdMap[eventId];
}
pnt.util.Properties.prototype.trigger = function(id, eventType) {
	if(typeof(this._prop[id])!='undefined') {
		this._execCallback(id, eventType, this._prop[id]);
	}
}




pnt.util.TimerManager = function() {
    this._timer = {};
}
pnt.util.TimerManager.prototype.create = function(callback, milliseconds) {
    var timerKey = pnt.util.makeid(20);
    this.put(timerKey, callback, milliseconds);
    return timerKey;
}
pnt.util.TimerManager.prototype.put = function(timerKey, callback, milliseconds) {
    this.remove(timerKey);

    this._timer[timerKey] = {
        callback: callback,
        milliseconds: milliseconds
    };

    this._timer[timerKey].timerId = window.setTimeout(callback, milliseconds);
}

pnt.util.TimerManager.prototype.remove = function(timerKey) {
    if(typeof(this._timer[timerKey])!='undefined') {
        if(typeof(this._timer[timerKey].callback)!='undefined') {
            window.clearTimeout(this._timer[timerKey].timerId);
        }
        delete this._timer[timerKey];
    }
}




pnt.util.DataEvent = function(id) {
    this._id = id;
    this._dataCls = {};
    this._events = {};
    this._eventIdMap = {};
}
pnt.util.DataEvent.prototype.getId = function() {
    return this._id;
}
pnt.util.DataEvent.prototype._generateDataClass = function(data) {
    var dataClass = {};

    for(var key in data) {
        if(typeof(data[key])=='string') {
            data[key] = data[key].trim();
        }
    }

    dataClass._data = data;
    dataClass._id = data.id || pnt.util.makeid(20);
    dataClass._dataEvent = this;
    dataClass.get = function(column, defaultValue) {
        var val = this._data[column];
        if(typeof(val)=='undefined' && typeof(defaultValue)!='undefined') {
            this._data[column] = defaultValue;
            return this._data[column];
        } else {
            return this._data[column];
        }
    }
    dataClass.setId = function(id) {
        this._id = id;
    }
    dataClass.getId = function() {
        return this._id;
    }
    dataClass.getOriginalData = function() {
        return this._data;
    }
    dataClass.setData = function(data) {
        for(var column in data) {
            this._data[column] = data[column];
        }
    }
    dataClass.modify = function(data, callback) {
        this.setData(data);
        this._dataEvent.dispatchEvent('modify', this, callback);
    }
    dataClass.remove = function(callback) {
        this._dataEvent.dispatchEvent('remove', this, callback);
    }
    dataClass.unload = function() {
        this._dataEvent.unload(this);
    }
    return dataClass;
}
pnt.util.DataEvent.prototype.loadData = function(data) {
    var result = [];
    for(var i=0; i<data.length; i++) {
        var dataClass = this._generateDataClass(data[i]);
        this._dataCls[dataClass.getId()] = dataClass;

        this.dispatchEvent('load', dataClass);
        result.push(dataClass);
    }
    return result;
}
pnt.util.DataEvent.prototype.unload = function(dataCls) {
    if(typeof(dataCls)!='undefined') {
        this.dispatchEvent('unload', dataCls);
        delete this._dataCls[dataCls.getId()];
    } else {
        for(var _id in this._dataCls) {
            this.dispatchEvent('unload', this._dataCls[_id]);
            delete this._dataCls[_id];
        }
    }
}
pnt.util.DataEvent.prototype.create = function(data, callback) {
    this.dispatchEvent('create', data, callback);
}
pnt.util.DataEvent.prototype.dispatchEvent = function(eventType, dataClass, callback) {
    if(typeof(this._events[eventType])!='undefined') {

        for(var eventId in this._events[eventType]) {
            this._events[eventType][eventId].call(this, dataClass, callback);
        }
    }
}
pnt.util.DataEvent.prototype.on = function(eventType, callback) {
    var eventId = pnt.util.makeid(20);

    if(typeof(this._events[eventType])=='undefined') {
        this._events[eventType] = {};
    }
    this._events[eventType][eventId] = callback;
    return eventId;
}
/**
 * 조건과 일치하는 데이터셋을 반환(배열)
 */
pnt.util.DataEvent.prototype.find = function(condition) {
    var result = [];

    for(var id in this._dataCls) {
        var obj = this._dataCls[id];

        if(typeof(condition)!=null) {
            var originalData = obj.getOriginalData();
            var discord = false;
            for(var column in condition) {
                if(typeof(originalData[column])=='undefined'
                    || originalData[column]!=condition[column]) {

                    discord = true;
                    break;
                }
            }
            if(discord==false) {
                result.push(obj);
            }
        } else {
            result.push(obj);
        }
    }
    return result;
}



pnt.util.SocketIo = function(server, option) {

    if(typeof(option)=='undefined') {
        option = {};
    }

    this._server = server;
    this._option = option;
    this._jsUrl = this._server+'/socket.io/socket.io.js';

    this._readyScript = false;
    this.loadJavascript();
}
pnt.util.SocketIo.prototype.loadJavascript = function() {
    if(typeof(io)=='undefined') {
        var head= document.getElementsByTagName('head')[0];

        pnt.util.loadJavascript(this._jsUrl, pnt.util.bind(this, function() {
            console.debug('pnt.util.SocketIo.loadJavascript load script success', this._jsUrl);
            this._readyScript = true;
        }));

    } else {
        this._readyScript = true;
    }
}
pnt.util.SocketIo.prototype.isReadyScript = function() {
    return this._readyScript;
}
pnt.util.SocketIo.prototype.createSocket = function() {
    console.log('this._server', this._server)
    var socket = io.connect(this._server, {
        transports: ['websocket'],
        forceNew: true,
        reconnection: true,
        reconnectionAttempts: 5,
        reconnectionDelay: 5000
    });
    return socket;
}
pnt.util.SocketIo.prototype.channel = function(channelName) {
    var channel = new pnt.util.SocketIoChannel(channelName, this);

    return channel;
}

pnt.util.SocketIoChannel = function(channelName, socketIo) {
    this._connected = false;
    this._retry = 0;
    this._callback = [];

    this._channelName = channelName;
    this._socketIo = socketIo;
    if(socketIo.isReadyScript()!=true) {
        this.retryReady();
    } else {
        this.ready();
    }
}
pnt.util.SocketIoChannel.prototype.retryReady = function () {
    if(this._retry++>10) {
        console.debug('pnt.util.SocketIoChannel.retryReady connect fail', this._channelName);
        //window.alert('socket.io 연결 실패');
    } else {
        this._retry++;
        window.setTimeout(pnt.util.bind(this, function() {
            if(this._socketIo.isReadyScript()==true) {
                this.ready();
            } else {
                this.retryReady();
            }
        }), 200);
    }
}
pnt.util.SocketIoChannel.prototype.ready = function() {
    this._socket = this._socketIo.createSocket();

    this._socket.on('connect', pnt.util.bind(this, function() {
        console.debug('pnt.util.SocketIoChannel connect', this._channelName);
        this._connected = true;
        this._socket.emit('join', this._channelName);

        for(var i=0; i<this._callback.length; i++) {
            this._socket.on(this._callback[i].method, pnt.util.bind(this, this._callback[i].callback));
        }
        this._callback = [];
    }));
    this._socket.on('disconnect', pnt.util.bind(this, function() {
        console.debug('pnt.util.SocketIoChannel disconnect', this._channelName);
        this._connected = false;
        this._socket.emit('leave', this._channelName);
        this._socket.io.reconnect();
    }));
    this._socket.on('reconnect', pnt.util.bind(this, function() {
        console.debug('pnt.util.SocketIoChannel reconnect', this._channelName);
        this._connected = true;
        window.setTimeout(pnt.util.bind(this, function() {
            this._socket.emit('join', this._channelName);
        }), 500);
    }));
    this._socket.on('reconnecting', pnt.util.bind(this, function(evt) {
        console.debug('pnt.util.SocketIoChannel reconnecting', evt);
    }));
    pnt.util.addEventListener(window,'beforeunload', function() {
        this.close();
    }, this);
}
pnt.util.SocketIoChannel.prototype.on = function(method, callback) {
    if(this._connected==true) {
        this._socket.on(method, callback);
    } else {
        this._callback.push({method:method, callback:callback});
    }
}
pnt.util.SocketIoChannel.prototype.close = function() {
    if(this._connected==true) {
        this._connected = false;
        this._socket.emit('leave', this._channelName);
        this._socket.close();
    }
}




pnt.util.DataSync = function(dataEvent) {
    this._dataEvent = dataEvent;
    this._url = {
        load: '/maptool/beacon/list.json',
        create: '/maptool/beacon/reg.do',
        modify: '/maptool/beacon/mod.do',
        remove: '/maptool/beacon/del.do'
    }

    dataEvent.on('create', this.onCreate);
    dataEvent.on('modify', this.modify);
    dataEvent.on('remove', this.remove);
}
pnt.util.DataSync.prototype.onCreate = function(data) {
    var data = data.getOriginalData();
    var url = this._url.create;
    $.ajax({ type: "POST",
        contentType: 'application/x-www-form-urlencoded',
        url: url,
        data: data,
        success: pnt.util.bind(this, function(response) {
            console.debug('pnt.util.DataSync.prototype.onCreate response', response);
            data.createSuccess();
        }),
        error: function(arg1, arg2, arg3) {
            console.error('pnt.util.DataSync.prototype.onCreate error', arg1, arg2, arg3);
        },
        complete: function() {
        }
    });
}
pnt.util.DataSync.prototype.onModify = function() {
}
pnt.util.DataSync.prototype.onRemove = function() {

}

pnt.util.onReady(function() {
    var sheet = document.createElement('style');
    sheet.innerHTML = 'a.skiplink { '
        +' position: absolute;'
        +'clip: rect(1px, 1px, 1px, 1px);'
        +'padding: 0;'
        +'border: 0;'
        +'height: 1px;'
        +'width: 1px;'
        +'overflow: hidden;'
        +'}'
        +'a.skiplink:focus {'
        +'clip: auto;'
        +'height: auto;'
        +'width: auto;'
        +'background-color: #fff;'
        +'padding: 0.3em;'
        +'}'
        +'.map {'
        +'position: absolute;'
        +'height:800px;'
        +'width:100%;'
        +'/*top:0px;'
        +'bottom:0px;'
        +'left:0px;'
        +'right:0px;*/'
        +'}'
        +'.map:focus {'
        +'outline: #4A74A8 solid 0.15em;'
        +'}'

        +'.marker-label {'
        +'color: #000000;'
        +'font-size:9pt;'
        +'text-shadow: -0.5px 0 #ffffff, 0 0.5px #ffffff, 0.5px 0 #ffffff, 0 -0.5px #ffffff;'
        +'-moz-text-shadow: -0.5px 0 #ffffff, 0 0.5px #ffffff, 0.5px 0 #ffffff, 0 -0.5px #ffffff;'
        +'-webkit-text-shadow: -0.5px 0 #ffffff, 0 0.5px #ffffff, 0.5px 0 #ffffff, 0 -0.5px #ffffff;'
        +'border: 1px solid black;'
        +'background-color: #eeeeee;'
        +'}'
        +'.ol-overlay-container {'
        +'transition-property: left, top;'
        //+'transition-duration: 0.25s;'
        +'transition-timing-function: linear;'
        +'}'

        +'#map-controls {'
        +'right: 3em;'
        +'top: 0.5em;'
        +'}'

        +'#map-controls-button {'
        +'right: 0.5em;'
        +'top: 3em;'
        +'}'

        +'#map-controls-button>div:not(:last-child) {'
        +'margin-bottom: 2px;'
        +'}'

        +'#map-summary {'
        +'right: 3em;'
        +'top: 3em;'
        +'}'

        +'.ol-popup {'
        +'position: absolute;'
        +'background-color: white;'
        +'-webkit-filter: drop-shadow(0 1px 4px rgba(0,0,0,0.2));'
        +'filter: drop-shadow(0 1px 4px rgba(0,0,0,0.2));'
        +'padding: 15px;'
		/*+'border-radius: 10px;'*/
        +'border: 1px solid #cccccc;'
		/*+'bottom: 12px;'*/
        +'left: -50px;'
        +'min-width: 280px;'
        +'}'

        +'.ol-popup-closer {'
        +'text-decoration: none;'
        +'position: absolute;'
        +'top: 4px;'
        +'right: 8px;'
        +'color: #3498db;'
        +'font-size: 12pt;'
        +'}'
        +'.ol-popup-closer:after {'
        //+'content:"✖";'
        +'content:"✕";'
        +'}';

    document.body.appendChild(sheet);
});




pnt.module = pnt.module || {};
pnt.module._modules = {};
pnt.module._global = {};
pnt.module.Module = function() {
    this._eventHandler = {'init':[], 'prev':[], 'after':[]};
    this.global = pnt.module.global;
}
pnt.module.Module.prototype.on = function(type, func) {

    var types = [];
    if(type.indexOf(',')>-1) {
        var arr = type.split(',');
        for(var i=0; i<arr.length; i++) {
            if(typeof(arr[i])=='string') {
                types.push(arr[i].trim());
            }
        }
    } else {
        types.push(type.trim());
    }

    for(var i=0; i<types.length; i++) {
        if(typeof(this._eventHandler[types[i]])=='undefined') {
            this._eventHandler[types[i]] = [];
        }

        this._eventHandler[types[i]].push(func);
    }
}
pnt.module.Module.prototype.dispatch = function(type, param) {

    if(typeof(this._eventHandler[type])!='undefined') {
        for(var i=0; i<this._eventHandler[type].length; i++) {
            this._eventHandler[type][i].call(this, this, param);
        }
    }
}
pnt.module.Module.prototype.global = function(key, value) {
    if(typeof(value)=='undefined') {
        return pnt.module._global[key];
    } else {
        pnt.module._global[key] = value;
    }
}



pnt.module.create = function(id, init) {
    var module = new pnt.module.Module();
    module.on('init', init);
    pnt.module._modules[id] = module;

    return module;
}
pnt.module.get = function(id) {
    return pnt.module._modules[id];
}
pnt.module.run = function(id, param) {
    if(typeof(pnt.module._modules[id])!='undefined') {
        var module = pnt.module._modules[id];

        for(var i=0; i<module._eventHandler.prev.length; i++) {
            module._eventHandler.prev[i].call(module, module, param);
        }

        for(var i=0; i<module._eventHandler.init.length; i++) {
            module._eventHandler.init[i].call(module, module, param);
        }

        for(var i=0; i<module._eventHandler.after.length; i++) {
            module._eventHandler.after[i].call(module, module, param);
        }

        return module;
    }
}







pnt.ui = pnt.ui || {};
pnt.ui.builder = pnt.ui.builder || {};
pnt.ui.builder.Form = function(option) {

    if(typeof(option)=='undefined') {
        option = {};
    }

    this._elements = {};
    this._elements.root = document.createElement('div');

    this._elements.form = document.createElement('form');
    this._elements.form.className = 'form-horizontal';

    if(typeof(option.id)!='undefined') {
        this._elements.form.id = option.id;
    }
    this._elements.root.appendChild(this._elements.form);

    this._data = {};
    if(typeof(option.data)!='undefined') {
        for(var dataKey in option.data) {
            this._data[dataKey] = option.data[dataKey];
        }
    }

    if(typeof(option.field)!='undefined') {
        this.setField(option.field);
    }

}
pnt.ui.builder.Form.prototype.set = function(key, value) {
    this._data[key] = value;
}
pnt.ui.builder.Form.prototype.get = function(key) {
    return this._data[key];
}
pnt.ui.builder.Form.prototype.setField = function(json) {
    for(var elementName in json) {
        var info = json[elementName];

        if(elementName=='nodeName') {
            elementName = 'nodeName_';
        }

        if(info.type=='hidden') {
            var element = document.createElement('input');
            element.name = elementName;
            element.type = 'hidden';
            element.value = String(info.value);
            this._elements.form.appendChild(element);
            continue;
        } else if(info.type=='file') {
            var element = document.createElement('input');
            element.name = elementName;
            element.type = 'file';
            element.style.display = 'none';
            if(typeof(info.onChange)!='undefined') {
                pnt.util.addEventListener(element, 'change', pnt.util.bind(this, info.onChange));
            }
            this._elements.form.appendChild(element);
            continue;
        }

        var formGroup = document.createElement('div');
        formGroup.className = 'form-group form-group-sm';

        var label = document.createElement('label');
        label.innerHTML = info.label || '';
        label.className = 'col-sm-3 control-label';
        formGroup.appendChild(label);

        var col2 = document.createElement('div');
        col2.className = 'col-sm-9';
        formGroup.appendChild(col2);

        var element = null;

        if(info.type=='text') {
            element = document.createElement('input');
            element.name = String(elementName);
            element.type = 'text';
            element.value = String(info.value);
            element.className = 'form-control';
            if(typeof(info.required)!='undefined' && info.required==true) {
                element.required = 'required';
                element.setAttribute('required', 'required');
            }
            if(typeof(info.readonly)!='undefined' && info.readonly==true) {
                element.setAttribute('readonly', 'readonly');
            }
        }
        else if(info.type=='label') {
            element = document.createElement('input');
            element.name = elementName;
            element.type = 'text';
            element.value = String(info.value);
            element.className = 'form-control';
        }
        else if(info.type=='select') {
            element = document.createElement('select');
            element.name = elementName;
            element.value = String(info.value);
            element.className = 'form-control';

            if(typeof(info.readonly)!='undefined' && info.readonly==true) {
                element.setAttribute('disabled', 'disabled');
            }

            if(typeof(info.onChange)!='undefined') {
                pnt.util.addEventListener(element, 'change', info.onChange);
            }


            if(info.options instanceof Array) {
                for(var i=0; i<info.options.length; i++) {
                    var option = document.createElement('option');
                    option.value = String(info.options[i].value);
                    option.innerHTML = String(info.options[i].text);
                    if(info.options[i].value==String(info.value)) {
                        option.selected = 'selected';
                    }
                    element.appendChild(option);
                }
            } else {

                for(var val in info.options) {
                    var option = document.createElement('option');
                    option.value = String(val);
                    option.innerHTML = String(info.options[val]);
                    if(val==String(info.value)) {
                        option.selected = 'selected';
                    }
                    element.appendChild(option);
                }
            }
            if(typeof(info.required)!='undefined' && info.required==true) {
                element.setAttribute('required', 'required');
            }
        }
        else if(info.type=='textarea') {
            element = document.createElement('textarea');
            element.name = elementName;
            element.innerHTML = String(info.value);
            element.className = 'form-control';
            element.style.height = '60px';
            if(typeof(info.required)!='undefined' && info.required==true) {
                element.setAttribute('required', 'required');
            }
            if(typeof(info.readonly)!='undefined' && info.readonly==true) {
                element.setAttribute('readonly', 'readonly');
            }
        }
        else if(info.type=='button') {
            var element = document.createElement('input');
            element.type = 'button';
            element.value = info.label;
            //element.innerHTML = String(info.value);
            element.className = 'btn btn-primary btn-sm';
            pnt.util.addEventListener(element, 'click', pnt.util.bind(this, info.callback));
        }
        else if(info.type=='img') {
            var element = document.createElement('img');
            element.src = typeof(info.value)=='undefined'?'': String(info.value);
            element.style.maxWidth = '160px';
            element.name = elementName;
        }
        else if(info.type=='button.group') {
            var element = document.createElement('div');
            element.className = 'form-inline';
            for(var btnId in info.buttons) {
                var button = document.createElement('input');
                button.type = 'button';
                button.value = String(info.buttons[btnId].label);
                button.style.marginRight = '5px';
                button.className = 'btn btn-sm btn-xs';
                pnt.util.addEventListener(button, 'click', pnt.util.bind(this, info.buttons[btnId].callback));

                // text input button
                if(info.buttons[btnId].type=='text') {
                    var inputGroup = document.createElement('div');
                    inputGroup.className = 'input-group';
                    inputGroup.style.width = '120px';
                    var inputText = document.createElement('input');
                    inputText.name = String(info.buttons[btnId].textName);
                    inputText.type = 'text';
                    inputText.className = 'form-control';

                    inputGroup.appendChild(inputText);

                    var span = document.createElement('span');
                    span.className = 'input-group-btn';
                    span.appendChild(button);
                    inputGroup.appendChild(span);

                    element.appendChild(inputGroup);
                } else {
                    element.appendChild(button);
                }


            }
        }
        else if(info.type=='element') {
            var element = document.createElement('div');
            element.appendChild(info.element);
        }
        col2.appendChild(element);
        this._elements.form.appendChild(formGroup);

    }
}
pnt.ui.builder.Form.prototype.generate = function(json) {

    if(typeof(json)!='undefined') {
        this.setField(json);
    }

    return this._elements.root;
}
pnt.ui.builder.Form.prototype.getFormData = function() {
    var data = {};
    var elements = document.getElementById(this._elements.form.id);
    for(var i=0; i<elements.length; i++) {
        if(typeof(elements[i].name)!='undefined' && elements[i].name!='') {
            var elName = elements[i].name=='nodeName_' ? 'nodeName' : elements[i].name;
            data[elName] = elements[i].value;
        }
    }
    return data;
}
pnt.ui.builder.Form.prototype.getElement = function() {
    return this._elements.form;
}

pnt.ui.builder.Button = function(option) {
    if(typeof(option)=='undefined') {
        option = {};
    }

    this._elements = {};
    this._elements.button = document.createElement('input');
    if(typeof(option.id)!='undefined') {
        this._elements.button.id = id;
    }
    this._elements.button.type = 'button';
    this._elements.button.value = '';
    this._elements.button.className = 'btn btn-primary btn-sm';

    if(typeof(option.text)!='undefined') {
        this.setText(option.text);
    }

    this._data = {};
    if(typeof(option.data)!='undefined') {
        for(var dataKey in option.data) {
            this._data[dataKey] = option.data[dataKey];
        }
    }

    if(typeof(option.click)!='undefined') {
        this.on('click', option.click);
    }

}
pnt.ui.builder.Button.prototype.set = function(key, value) {
    this._data[key] = value;
}
pnt.ui.builder.Button.prototype.get = function(key) {
    return this._data[key];
}
pnt.ui.builder.Button.prototype.setText = function(text) {
    this._elements.button.value = text;
}
pnt.ui.builder.Button.prototype.on = function(eventType, callback) {
    pnt.util.addEventListener(this._elements.button, eventType, callback, this);
}
pnt.ui.builder.Button.prototype.setStyle = function(json) {
    for(var id in json) {
        this._elements.button.style[id] = json[id];
    }
}
pnt.ui.builder.Button.prototype.generate = function() {
    return this._elements.button;
}
pnt.ui.builder.Button.prototype.getElement = function() {
    return this._elements.button;
}


pnt.ui.builder.Input = function(option) {
    if(typeof(option)=='undefined') {
        option = {};
    }

    this._elements = {};
    this._elements.input = document.createElement('input');
    if(typeof(option.id)!='undefined') {
        this._elements.input.id = option.id;
    }
    this._elements.input.type = option.type || 'text';
    this._elements.input.value = option.value || '';
    this._elements.input.className = 'form-control input-sm';

    if(typeof(option.style)!='undefined') {
        for(var key in option.style) {
            this._elements.input.style[key] = option.style[key];
        }
    }

    this._data = {};
    if(typeof(option.data)!='undefined') {
        for(var dataKey in option.data) {
            this._data[dataKey] = option.data[dataKey];
        }
    }

    if(typeof(option.change)!='undefined') {
        this.on('change', option.change);
    }

}
pnt.ui.builder.Input.prototype.set = function(key, value) {
    this._data[key] = value;
}
pnt.ui.builder.Input.prototype.get = function(key) {
    return this._data[key];
}
pnt.ui.builder.Input.prototype.setValue = function(text) {
    this._elements.input.value = text;
}
pnt.ui.builder.Input.prototype.getValue = function() {
    return this._elements.input.value;
}
pnt.ui.builder.Input.prototype.on = function(eventType, callback) {
    pnt.util.addEventListener(this._elements.input, eventType, callback, this);
}
pnt.ui.builder.Input.prototype.setInputStyle = function(json) {
    for(var id in json) {
        this._elements.input.style[id] = json[id];
    }
}
pnt.ui.builder.Input.prototype.generate = function() {
    return this._elements.input;
}
pnt.ui.builder.Input.prototype.getElement = function() {
    return this._elements.input;
}

pnt.ui.builder.InputButton = function(option) {
    if(typeof(option)=='undefined') {
        option = {};
    }

    this._elements = {};
    this._elements.button = document.createElement('input');
    if(typeof(option.id)!='undefined') {
        this._elements.button.id = id;
    }
    this._elements.button.type = 'button';
    this._elements.button.value = '';
    this._elements.button.className = 'btn btn-primary btn-sm';

    if(typeof(option.text)!='undefined') {
        this.setText(option.text);
    }

    this._elements.inputGroup = document.createElement('div');
    this._elements.inputGroup.className = 'input-group';

    this._elements.input = document.createElement('input');
    this._elements.input.type = 'text';
    this._elements.input.className = 'form-control input-sm';

    this._elements.span = document.createElement('span');
    this._elements.span.className = 'input-group-btn';
    this._elements.span.appendChild(this._elements.button);

    this._elements.inputGroup.appendChild(this._elements.input);
    this._elements.inputGroup.appendChild(this._elements.span);

    this._data = {};
    if(typeof(option.data)!='undefined') {
        for(var dataKey in option.data) {
            this._data[dataKey] = option.data[dataKey];
        }
    }

    if(typeof(option.value)!='undefined') {
        this.setValue(option.value);
    }

    if(typeof(option.click)!='undefined') {
        this.on('click', option.click);
    }

}
pnt.ui.builder.InputButton.prototype.set = function(key, value) {
    this._data[key] = value;
}
pnt.ui.builder.InputButton.prototype.get = function(key) {
    return this._data[key];
}
pnt.ui.builder.InputButton.prototype.setText = function(text) {
    this._elements.button.value = text;
}
pnt.ui.builder.InputButton.prototype.setValue = function(text) {
    this._elements.input.value = text;
}
pnt.ui.builder.InputButton.prototype.getValue = function() {
    return this._elements.input.value;
}
pnt.ui.builder.InputButton.prototype.on = function(eventType, callback) {
    pnt.util.addEventListener(this._elements.button, eventType, callback, this);
}
pnt.ui.builder.InputButton.prototype.setButtonStyle = function(json) {
    for(var id in json) {
        this._elements.button.style[id] = json[id];
    }
}
pnt.ui.builder.InputButton.prototype.setInputStyle = function(json) {
    for(var id in json) {
        this._elements.input.style[id] = json[id];
    }
}
pnt.ui.builder.InputButton.prototype.generate = function() {
    return this._elements.inputGroup;
}
pnt.ui.builder.InputButton.prototype.getElement = function() {
    return this._elements.inputGroup;
}

pnt.ui.builder.ButtonGroup = function(option) {
    if(typeof(option)=='undefined') {
        option = {};
    }

    this._elements = {};
    this._elements.inline = document.createElement('div');
    this._elements.inline.className = 'form-inline';


    if(typeof(option.buttons)!='undefined') {
        for(var i=0; i<option.buttons.length; i++) {
            this.addButton(option.buttons[i]);
        }
    }
}
pnt.ui.builder.ButtonGroup.prototype.set = function(key, value) {
    this._data[key] = value;
}
pnt.ui.builder.ButtonGroup.prototype.get = function(key) {
    return this._data[key];
}
pnt.ui.builder.ButtonGroup.prototype.addButton = function(button) {
    var inputGroup = document.createElement('div');
    inputGroup.className = 'input-group';
    inputGroup.appendChild(button.getElement());

    this._elements.inline.appendChild(inputGroup);
}
pnt.ui.builder.ButtonGroup.prototype.getElement = function() {
    return this._elements.inline;
}

pnt.ui.builder.Checkbox = function(option) {

    if(typeof(option)=='undefined') {
        option = {};
    }

    this._data = {};
    if(typeof(option.data)!='undefined') {
        for(var dataKey in option.data) {
            this._data[dataKey] = option.data[dataKey];
        }
    }

    this._elements = {};
    this._elements.root = document.createElement('input');
    this._elements.root.type = 'checkbox';
    if(typeof(option.id)!='undefined') {
        this._elements.root.id = option.id;
    }
    this._elements.root.className = '';

    if(typeof(option.value)!='undefined') {
        this.setValue(option.value);
    }
    if(typeof(option.checked)!='undefined' && option.checked==true) {
        this._elements.root.checked = 'checked';
    }

}
pnt.ui.builder.Checkbox.prototype.set = function(key, value) {
    this._data[key] = value;
}
pnt.ui.builder.Checkbox.prototype.get = function(key) {
    return this._data[key];
}
pnt.ui.builder.Checkbox.prototype.setValue = function(text) {
    this._elements.root.value = text;
}
pnt.ui.builder.Checkbox.prototype.getValue = function() {
    return this._elements.root.value;
}
pnt.ui.builder.Checkbox.prototype.getChecked = function() {
    if(this._elements.root.checked) {
        return true;
    } else {
        return false;
    }
}
pnt.ui.builder.Checkbox.prototype.setChecked = function(checked) {
    if(checked==true) {
        //this._elements.root.setAttribute('checked', true);
        this._elements.root.checked = true;
    } else {
        //this._elements.root.removeAttribute('checked');
        this._elements.root.checked = false;
    }
}
pnt.ui.builder.Checkbox.prototype.getEnabled = function() {
    if(this._elements.root.disabled) {
        return false;
    } else {
        return true;
    }
}
pnt.ui.builder.Checkbox.prototype.setEnabled = function(enabled) {
    if(enabled==true) {
        this._elements.root.disabled = false;
    } else {
        this._elements.root.disabled = true;
    }
}
pnt.ui.builder.Checkbox.prototype.on = function(eventType, callback) {
    pnt.util.addEventListener(this._elements.root, eventType, callback, this);
}
pnt.ui.builder.Checkbox.prototype.getElement = function() {
    return this._elements.root;
}


pnt.ui.builder.Table = function(option) {

    if(typeof(option)=='undefined') {
        option = {};
    }

    this._data = {};
    if(typeof(option.data)!='undefined') {
        for(var dataKey in option.data) {
            this._data[dataKey] = option.data[dataKey];
        }
    }

    this._elements = {};
    this._elements.root = document.createElement('table');
    if(typeof(option.id)!='undefined') {
        this._elements.root.id = option.id;
    }
    this._elements.root.className = 'table table-bordered table-striped'
        + ' table-hover table-heading table-datatable small';

    this._elements.thead = document.createElement('thead');
    this._elements.root.appendChild(this._elements.thead);

    this._elements.tbody = document.createElement('tbody');
    this._elements.root.appendChild(this._elements.tbody);
    this._baseid = pnt.util.makeid(10);

    this._header = null;
    this._rows = {};
    this._rowKeys = [];
}
pnt.ui.builder.Table.prototype.set = function(key, value) {
    this._data[key] = value;
}
pnt.ui.builder.Table.prototype.get = function(key) {
    return this._data[key];
}
pnt.ui.builder.Table.prototype.setHeader = function(columns) {
    this._header = columns;

    this._elements.thead.innerHTML = '';

    var tr = document.createElement('tr');
    for(var i=0; i<columns.length; i++) {
        var td = document.createElement('td');
        if(typeof(columns[i])=='string' || typeof(columns[i])=='number') {
            td.innerHTML = columns[i];
        } else {
            td.appendChild(columns[i]);
        }

        tr.appendChild(td);
    }
    this._elements.thead.appendChild(tr);
}
pnt.ui.builder.Table.prototype.addRow = function(row) {
    var id = pnt.util.makeid(10)+(new Date()).getTime();
    this.putRow(id, row);
}
pnt.ui.builder.Table.prototype.putRow = function(id, row) {
    var tr = null;
    if(typeof(this._rows[id])=='undefined') {
        this._rowKeys.push(id);
        this._rows[id] = row;

        tr = document.createElement('tr');
        tr.id = this._baseid+id;
        this._elements.tbody.appendChild(tr);
    } else {
        tr = document.getElementById(this._baseid+id);
    }
    tr.innerHTML = '';
    for(var i=0; i<row.length; i++) {
        var td = document.createElement('td');
        if(typeof(row[i])=='string' || typeof(row[i])=='number') {
            td.innerHTML = row[i];
        } else {
            td.appendChild(row[i]);
        }
        tr.appendChild(td);
    }
}
pnt.ui.builder.Table.prototype.getElement = function() {
    return this._elements.root;
}




