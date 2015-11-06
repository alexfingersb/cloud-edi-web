/**
 * @author Alexandre Finger Sobrinho
 */

var downloadws = new WebSocket("ws://192.168.56.1:8080/cloud-edi-web/downloads");
downloadws.onmessage = onMessage;
downloadws.onopen = onOpen;
downloadws.onerror = onError;
var timeout;

function onError(event) {
	console.log(event);
}

function onOpen(event) {
	console.log('[download] connect websocket');
}

function onMessage(event) {
	var data = JSON.parse(event.data);
	
	if (data.action === 'list') {
		downloadTable(data);
	} if (data.action === 'download') {
		download(data);
	}
}


function listRoutesToDownload() {
	var route = {
	        action: "list"
	    };
	downloadws.send(JSON.stringify(route));
}

function downloadTable(data) {
	var table = $('#downloadTable').find('tbody');
	var row = $('<tr></tr>');
	row.attr('id', data.id);
	row.append('<td class="identify">' + data.id + '</td>');
	row.append('<td class="description">' + data.description   + '</td>');
	var action = '<a class="btn btn-primary" href="#" onclick="downloadSetup(' + data.id + ');"><i class="glyphicon glyphicon-cloud-download" data-i18n="actions.download"> </i></a>';
	row.append('<td class="text-center">' + action + '</td>');
	table.append(row);
	$('#inputSystem').selectpicker('val',getSystemArch());
	$('[data-i18n]').i18n();
}

function downloadSetup(id) {
	var system = $('#inputSystem :selected').val();
	
	var route = {
	        action: "download",
	        id: id,
	        system: system
	    };
	downloadws.send(JSON.stringify(route));
}

function getSystemArch() {var OSName="unknown OS";
	if (navigator.appVersion.indexOf("Win")!=-1) OSName="windows";
	if (navigator.appVersion.indexOf("Mac")!=-1) OSName="mac";
	if (navigator.appVersion.indexOf("X11")!=-1) OSName="unix";
	if (navigator.appVersion.indexOf("Linux")!=-1) OSName="linux";
	return OSName;
}

function download(data) {
    document.getElementById('iframe_down').src = "/cloud-edi-web/download/" + data.id + "_agent.zip";
};

function routeSearch(param) {
	var action = {
			action: "searchRoute",
			param: param
		};
		
	downloadws.send(JSON.stringify(action));
}

function searchRouteQuery(el) {
	var param = $(el).val();
	param = $.trim(param);
	
	$("#downloadTable tr:gt(0)").each(function(i, tr) {
		var $td = $(this).find('td');
		var id = $td.eq(0).text();
		var desc = $td.eq(1).text();
		if (desc.toLowerCase().indexOf(param.toLowerCase()) >= 0 || id.indexOf(param) >= 0) {
			$(this).show();
		} else {
			$(this).hide();
		}
	});
}

