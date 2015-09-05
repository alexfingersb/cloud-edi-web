/**
 * @author Alexandre Finger Sobirnho
 */

window.onload = init;
var routews = new WebSocket("ws://localhost:8080/cloud-edi-web/context");
routews.onmessage = onMessage;
var timeout;

function onMessage(event) {
	var data = JSON.parse(event.data);
	
	if (!data.action) { // so is an json array
		$('#datalistUsers').empty();
		data.forEach(function(obj) { 
			console.log(obj.id, obj.name);
			$('#datalistUsers').append('<option value="' + obj.name + '">');
		});
	}
	
}

function search(name) {
	var action = {
			action: "searchUser",
			name: name
		};
		
	routews.send(JSON.stringify(action));
}

function searchUser(el) {
	var name = $(el).val();
	name = $.trim(name);
	
	if (name) {
		clearTimeout(timeout); // Cancela o timer anterior (para nao ficar executando varias vezes)
		timeout = setTimeout(function() {search(name);}, 1500);
	}
}

function init() {
}

function routeAdd(form) {
	//var protocol = 
	console.log(form);
}