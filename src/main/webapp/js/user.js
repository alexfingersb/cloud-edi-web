/**
 * 
 */
window.onload = init;
var socket = new WebSocket("ws://localhost:8080/cloud-edi-web/user");
socket.onmessage = onMessage;

function onMessage(event) {
    var user = JSON.parse(event.data);
    if (user.action === "add") {
    	parseJsonToTable(user);
    }
    if (user.action === "remove") {
        var row = document.getElementById(user.id);
        row.parentNode.removeChild(row);
    }
    if (user.action === "update") {
        var node = document.getElementById(user.id);
        var statusText = node.children[2];
        if (device.status === "On") {
            statusText.innerHTML = "Status: " + device.status + " (<a href=\"#\" OnClick=toggleDevice(" + device.id + ")>Turn off</a>)";
        } else if (device.status === "Off") {
            statusText.innerHTML = "Status: " + device.status + " (<a href=\"#\" OnClick=toggleDevice(" + device.id + ")>Turn on</a>)";
        }
    }
    if (user.action === "list") {
    	parseJsonToTable(user);
    	$('[data-i18n]').i18n();
    }
    if (user.action === "edit") {
    	console.log("Receive message: " + event.data);
    	parseJsonToForm(user);
    }
}

function parseJsonToForm(user) {
	
	$('#inputId').val(user.id);
	$('#inputName').val(user.name);
	$('#inputEmail').val(user.email);
	$('#inputUsername').val(user.username);
	console.log('profile=', user.profile);
	$('#inputProfile').selectpicker('val',user.profile);
	$('#inputStatus').val(user.status);
}

function parseJsonToTable(user) {
	var table = $('#userTable').find('tbody');
	var row = $('<tr></tr>');
	row.attr('id', user.id);
	row.append('<td>' + user.name  	  + '</td>');
	row.append('<td>' + user.email    + '</td');
	row.append('<td>' + user.username +'</td>');
	row.append('<td>' + user.profile  +'</td>');
	var action = '<a href="#" onclick="editUser(' + user.id + ');"> <span  aria-hidden="true" data-i18n="actions.edit" class="glyphicon glyphicon-pencil"></span></a>'; 
	action +=    '<a href="#" onclick="removeUser(' + user.id + ');"> <span aria-hidden="true" data-i18n="actions.remove" class="glyphicon glyphicon-remove-sign"></span></a>';
	row.append('<td>' + action + '<td>');
	table.append(row);
}

function submit(id, name, email, profile, username, password, status) {
    var action = "add";
    if (id)	{
    	action = "update";
    }
    
	var UserAction = {
        action: action,
        id: id,
        name: name,
        email: email,
        profile: profile,
        username: username,
        password: password,
        status: status
    };
    socket.send(JSON.stringify(UserAction));
}

function removeUser(id) {
	$( '#dialog-confirm').dialog({
      resizable: false,
      height:180,
      modal: true,
      buttons: {
        Yes: function() {
        	var UserAction = {
        			action: "remove",
        			id: id
        	};
        	socket.send(JSON.stringify(UserAction));
        	$( this ).dialog( "close" );
        },
        Cancel: function() {
          $( this ).dialog( "close" );
        }
      }
    });
}

function editUser(id) {
	var UserAction = {
			action: "edit",
			id: id
	};
	socket.send(JSON.stringify(UserAction));
	$('#content').load('pages/users/user_form.html');
}

function listUsers() {
    var UserAction = {
        action: "list"
    };
    socket.send(JSON.stringify(UserAction));
    $('#content').load('pages/users/user_list.html');
}

function toggleDevice(element) {
    var id = element;
    var DeviceAction = {
        action: "toggle",
        id: id
    };
    socket.send(JSON.stringify(DeviceAction));
}

function formSubmit() {
    var form 		= document.getElementById("userForm");
    var id 			= form.elements["inputId"].value;
    var name 		= form.elements["inputName"].value;
    var email 		= form.elements["inputEmail"].value;
    var profile 	= form.elements["inputProfile"].value;
    var username 	= form.elements["inputUsername"].value;
    var password 	= form.elements["inputPassword"].value;
    var status 		= form.elements["inputStatus"].value;
   	submit(id, name, email, profile, username, password, status);
   	listUsers();
}

function init() {
    //hideForm();
	
}