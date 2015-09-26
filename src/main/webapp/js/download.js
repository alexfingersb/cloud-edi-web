/**
 * @author Alexandre Finger Sobirnho
 */

var downloadws = new WebSocket("ws://localhost:8080/cloud-edi-web/download");
downloadws.onmessage = onMessage;
var timeout;

function onMessage(event) {
	var data = JSON.parse(event.data);
	
	if (data.action === 'list') {
		downloadTable(data);
	}
}

function parseFileFormRoute(userto, filter) {
	console.log('parse file form route');
	var copy = $('#inputFileCopy').val();
	var options = '';
	
	if (copy) {
		options = copy;
	}
	
	var route = {
			userTo: userto,
			filter: filter,
			protocol: 'FILE',
			cpath: $('#inputFtpFileDirectory').val(),
			options: options
	};
	downloadToTable(route);
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
	row.append('<td>' + data.id + '</td>');
	row.append('<td>' + data.description   + '</td>');
	var action = '<a class="btn btn-primary" href="#" onclick="downloadSetup(' + data.id + ');"><i class="glyphicon glyphicon-cloud-download" data-i18n="actions.download"> </i></a>';
	row.append('<td class="text-center">' + action + '</td>');
	table.append(row);
	$('#inputSystem').selectpicker('val',getSystemArch());
	$('[data-i18n]').i18n();
}

function downloadSetup(id) {
	var system = $('#inputSystem :selected')
	alert('Downloading route ' + id + ' and SO ' + system );
}

function getSystemArch() {var OSName="unknown OS";
	if (navigator.appVersion.indexOf("Win")!=-1) OSName="windows";
	if (navigator.appVersion.indexOf("Mac")!=-1) OSName="mac";
	if (navigator.appVersion.indexOf("X11")!=-1) OSName="unix";
	if (navigator.appVersion.indexOf("Linux")!=-1) OSName="linux";
	return OSName;
}
