/**
 * 
 */
window.onload = init;
var socket = new WebSocket("ws://localhost:8080/cloud-edi-web/user");
socket.onmessage = onMessage;

function onMessage(event) {
    var user = JSON.parse(event.data);
    if (user.action === "add") {
    	console.log("message received from server:",user.name);
        //prinUserElement(user);
    }
    if (user.action === "remove") {
        document.getElementById(user.id).remove();
        //device.parentNode.removeChild(device);
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
    }
}

function parseJsonToTable(user) {
	var table = $("#userTable").find('tbody');
	console.log('parseJsonToTable', user);
	var row = $('<tr></tr>');
	row.append('<td>' + user.name  	  + '</td>');
	row.append('<td>' + user.email    + '</td');
	row.append('<td>' + user.username +'</td>');
	row.append('<td>' + user.profile  +'</td>');
	row.append('<td>Editar, Excluir</td>');
	table.append(row);
}

function addUser(name, email, profile, username, password, status) {
    var UserAction = {
        action: "add",
        name: name,
        email: email,
        profile: profile,
        login: username,
        password: password,
        status: status
    };
    socket.send(JSON.stringify(UserAction));
}

function removeUser(element) {
	var id = element;
	var UserAction = {
			action: "remove",
			id: id
	};
	socket.send(JSON.stringify(UserAction));
}

function listUsers() {
    var UserAction = {
        action: "list"
    };
    socket.send(JSON.stringify(UserAction));
}

function toggleDevice(element) {
    var id = element;
    var DeviceAction = {
        action: "toggle",
        id: id
    };
    socket.send(JSON.stringify(DeviceAction));
}

function printUserElement(user) {
    var content = document.getElementById("content");
    console.log("usuario");
    alert("Usuario " + user.name);
    
    
}

function formSubmit() {
	console.log("form submit");
    var form = document.getElementById("userForm");
    var name = form.elements["inputName"].value;
    var email = form.elements["inputEmail"].value;
    var profile = form.elements["inputProfile"].value;
    var username = form.elements["inputLogin"].value;
    var password = form.elements["inputPassword"].value;
    var status = "on";
    //hideForm();
    //document.getElementById("userForm").reset();
    addUser(name, email, profile, username, password, status);
}

function init() {
    //hideForm();
	
}