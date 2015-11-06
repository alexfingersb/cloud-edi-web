/**
 * @author Alexandre Finger Sobirnho
 */

window.onload = init;
var dashboardws = new WebSocket("ws://192.168.56.1:8080/cloud-edi-web/dashboard");
dashboardws.onmessage = onMessage;

function onMessage(event) {
    var data = JSON.parse(event.data);

    if (!data.action) { // so is a json array
    	jsonTableSender(data.sender);
    	jsonTableReceiver(data.receiver);
    	$('[data-i18n]').i18n();
    }
}

function jsonTableSender(sender) {
	var tbody = $('#senderDashboardTable').find('tbody');
	
	sender.forEach(function(data) {
		var row = $('<tr></tr>');
		row.attr('id', data.id);
		row.append('<td>' + data.FileName + '</td>');
		row.append('<td>' + data.FileLength + '</td');
		row.append('<td>' + data.FileCrc + '</td>');
		row.append('<td>' + data.SentDate +'</td>');
		row.append('<td>' + data.ReceivedDate   + '</td>');
		tbody.append(row);
	});
}

function jsonTableReceiver(receiver) {
	var tbody = $('#receiverDashboardTable').find('tbody');
	
	receiver.forEach(function(data) {
		console.log('receiver=',data);
		var row = $('<tr></tr>');
		row.attr('id', data.id);
		row.append('<td>' + data.FileName + '</td>');
		row.append('<td>' + data.FileLength + '</td');
		row.append('<td>' + data.FileCrc + '</td>');
		row.append('<td>' + data.SentDate + '</td>');
		row.append('<td>' + data.ReceivedDate +'</td>');
		tbody.append(row);
	});
}

function listDashboard() {
    var dashboard = {
        action: "list"
    };
    dashboardws.send(JSON.stringify(dashboard));
}


function init() {
	$.formUtils.loadModules('security, date');
}
