        var mapCurrent;
		var mapPrediction;

		var geoJsonLayerCurrent;
		var geoJsonLayerPrediction;

		var mapDataCurrent;
		var mapDataPrediction;

		function onEachFeature(feature, layer) {
			var popupContent = "";

			if (feature.properties && feature.properties.popupContent) {
				popupContent += feature.properties.popupContent;
			}
			layer.bindPopup(popupContent);
		}

		function drawMapCurrent() {
			L.tileLayer('https://{s}.tiles.mapbox.com/v3/{id}/{z}/{x}/{y}.png', {
				maxZoom: 18,
				attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
					'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
					'Imagery © <a href="http://mapbox.com">Mapbox</a>',
				id: 'examples.map-20v6611k'
			}).addTo(mapCurrent);

			geoJsonLayerCurrent = L.geoJson(mapDataCurrent, {
				style: function (feature) {
					return feature.properties && feature.properties.style;
				},
				onEachFeature: onEachFeature,
				pointToLayer: function (feature, latlng) {
					return L.circleMarker(latlng, {
						radius: 8,
						fillColor: "#ff7800",
						color: "#000",
						weight: 1,
						opacity: 1,
						fillOpacity: 0.8
					});

				}
			}).addTo(mapCurrent);
		}

		function drawMapPrediction() {
			L.tileLayer('https://{s}.tiles.mapbox.com/v3/{id}/{z}/{x}/{y}.png', {
				maxZoom: 18,
				attribution: 'Map data &copy; <a href="http://openstreetmap.org">OpenStreetMap</a> contributors, ' +
					'<a href="http://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, ' +
					'Imagery © <a href="http://mapbox.com">Mapbox</a>',
				id: 'examples.map-20v6611k'
			}).addTo(mapPrediction);

			geoJsonLayerPrediction = L.geoJson(mapDataPrediction, {
				style: function (feature) {
					return feature.properties && feature.properties.style;
				},
				onEachFeature: onEachFeature,
				pointToLayer: function (feature, latlng) {
					return L.circleMarker(latlng, {
						radius: 8,
						fillColor: "#ff7800",
						color: "#000",
						weight: 1,
						opacity: 1,
						fillOpacity: 0.8
					});

				}
			}).addTo(mapPrediction);
		}

		window.onload = function() {

        	mapCurrent = L.map('mapCurrent').setView([59.896312, 30.423419], 16);
        	mapPrediction = L.map('mapPrediction').setView([59.896312, 30.423419], 16);
			drawMaps(true);
        	setInterval(drawMaps, 1000);
		}

		function drawMaps(first) {

			if(!first) mapCurrent.removeLayer(geoJsonLayerCurrent);
			if(!first) mapPrediction.removeLayer(geoJsonLayerPrediction);

			$.getJSON("/webapp-main/api?action=time", function(json) {
                var elem = document.getElementById("time");
                elem.innerHTML="<h3>Current time: " + json.time + " </h3>";
            });

        	$.getJSON("/webapp-main/api?action=current", function(json) {
        		console.log(json); // this will show the info it in firebug console
        		mapDataCurrent = json;
        		drawMapCurrent();
        	});

        	$.getJSON("/webapp-main/api?action=prediction", function(json) {
        		console.log(json); // this will show the info it in firebug console
        		mapDataPrediction = json;
        		drawMapPrediction();
        	});


        }