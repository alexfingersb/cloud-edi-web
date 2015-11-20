/**
 * @author Alexandre Finger Sobirnho
 */

window.onload = init;
var userws = new WebSocket("ws://192.168.56.1:8080/cloud-edi-web/user");
userws.onmessage = onMessage;

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
//        var node = document.getElementById(user.id);
//        var statusText = node.children[2];
//        if (device.status === "On") {
//            statusText.innerHTML = "Status: " + device.status + " (<a href=\"#\" OnClick=toggleDevice(" + device.id + ")>Turn off</a>)";
//        } else if (device.status === "Off") {
//            statusText.innerHTML = "Status: " + device.status + " (<a href=\"#\" OnClick=toggleDevice(" + device.id + ")>Turn on</a>)";
//        }
    }
    if (user.action === "list") {
    	parseJsonToTable(user);
    	$('[data-i18n]').i18n();
    }
    if (user.action === "edit") {
    	parseJsonToForm(user);
    }
    
    if (user.action == "loginOk") {
    	$("#menu-navbar").show();
    	$("#menu-about").show();
    	$("#menu-logout").show();
    	$("#content").load("pages/dashboard/dashboard.html");
    	
    } else {
    	$('#loginAlert').fadeIn();
    }
    
    if (!user.action) { // so is a json array
    	dashboardTableSender(user.sender);
    	dashboardTableReceiver(user.receiver);
    	$('[data-i18n]').i18n();
    }
}

function parseJsonToForm(user) {
	$('#inputId').val(user.id);
	$('#inputName').val(user.name);
	$('#inputEmail').val(user.email);
	$('#inputUsername').val(user.username);
	
	
	$('#inputProfile').selectpicker('val',(user.profile === 'Administrador' ? 'ADMINISTRADOR' : 'USUARIO'));
	$('#inputStatus').val(user.status);
	var checked = (user.status === 'on' ?  true : false);
	$('#inputStatus').prop('checked', checked);
	
	var editmode = (user.id !== "");
	if (editmode) {
		$('#divUserPassword').hide();
		$('#inputPassword').attr('data-validation-optional','true');
		$('#inputPassword_confirmation').attr('data-validation-optional','true');
	} else {
		$('#divUserPassword').show();
	}
}

function parseJsonToTable(user) {
	var table = $('#userTable').find('tbody');
	var row = $('<tr></tr>');
	row.attr('id', user.id);
	row.append('<td>' + user.name  	  + '</td>');
	row.append('<td>' + user.email    + '</td');
	row.append('<td>' + user.username +'</td>');
	row.append('<td>' + user.profile  +'</td>');
	var action = '<a href="#" onclick="editUser(' + user.id + ');"> <span  aria-hidden="true" data-i18n="actions.edit" class="glyphicon glyphicon-edit"></span></a>'; 
	action +=    '<a href="#" onclick="removeUser(' + user.id + ');"> <span aria-hidden="true" data-i18n="actions.remove" class="glyphicon glyphicon-remove"></span></a>';
	row.append('<td class="text-center">' + action + '<td>');
	table.append(row);
}

function submitUserWS(id, name, email, profile, username, password, status) {
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
	userws.send(JSON.stringify(UserAction));
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
        	userws.send(JSON.stringify(UserAction));
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
	userws.send(JSON.stringify(UserAction));
	$('#content').load('pages/users/user_form.html');
}

function listUsers() {
    var UserAction = {
        action: "list"
    };
    userws.send(JSON.stringify(UserAction));
    $('#content').load('pages/users/user_list.html');
}

function userFormSubmit(form) {
	var id 			= form.elements["inputId"].value;
    var name 		= form.elements["inputName"].value;
    var email 		= form.elements["inputEmail"].value;
    var profile 	= form.elements["inputProfile"].value;
    var username 	= form.elements["inputUsername"].value;
    var password 	= form.elements["inputPassword"].value;
    var status 		= ($('#inputStatus').prop('checked') ? "on" : "off");
   	submitUserWS(id, name, email, profile, username, password, status);
   	listUsers();
}

function validateAndSubmitUser(form) {
	   if (formValidate(form)) {
		   userFormSubmit(form);
	   }
}

function login(username, password) {
	var user = {
		action: "login",	
		username: username,
		password: password
	};
	
	userws.send(JSON.stringify(user));
}

function showMessage(msg) {
	$( '#dialog-login').dialog({
	      resizable: false,
	      height:180,
	      modal: true,
	      buttons: {
	        Ok: function() {
	          $( this ).dialog( "close" );
	        }
	      }
	    });
}

function dashboardTableSender(sender) {
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

function dashboardTableReceiver(receiver) {
	var tbody = $('#receiverDashboardTable').find('tbody');
	
	receiver.forEach(function(data) {
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
        action: "listDashboard"
    };
    userws.send(JSON.stringify(dashboard));
}

function init() {
	if ($.cookie("logged") === undefined) {
		$('#content').load("pages/users/login.html");
		$("#menu-navbar").hide();
		$("#menu-about").hide();
		$("#menu-logout").hide();
	}
	
	$.formUtils.loadModules('security, date');
}

function logout() {
	var action = {action: "logout"}
	userws.send(JSON.stringify(action));
	$.removeCookie("logged");
	$("#menu-navbar").hide();
	$("#menu-about").hide();
	$("#menu-logout").hide();
	location.href = location.protocol + "//" + location.host;
	
}


