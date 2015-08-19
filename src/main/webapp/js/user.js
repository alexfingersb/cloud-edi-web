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
}

function addUser(name, email, profile, login, password, status) {
    var UserAction = {
        action: "add",
        name: name,
        email: email,
        profile: profile,
        login: login,
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
    var login = form.elements["inputLogin"].value;
    var password = form.elements["inputPassword"].value;
    //hideForm();
    //document.getElementById("userForm").reset();
    var status = "on";
    addUser(name, email, profile, login, password, status);
}

function init() {
    //hideForm();
}