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
		$('#inputUserDestination').empty();
		$('#inputUserSource').empty();
		data.forEach(function(obj) {
			$('#inputUserSource').append('<option data-subtext="' + obj.username + '">' + obj.name + '</option>');
			$('#inputUserDestination').append('<option data-subtext="' + obj.username + '">' + obj.name + '</option>');
		});
		$('.selectpicker').selectpicker('refresh');
	} else if (data.action === 'list') {
		dataTable(data);
	} if (data.action === "edit") {
		parseEditRoute(data);
    }
}

function parseEditRoute(data) {
	
	$("#inputRouteId").val(data.id);
    $("#inputRouteDescription").val(data.description);
    
    if (data.protocol === "FTP") {
    	$('#btn-ftp-out').click();
    	
    	setTimeout(function() {
    		$('#inputUserSource').selectpicker('val',data.user);
    		$('#div_out input[name="inputFtpSite"]').val(data.cpath);
    		
    		var idx1 = data.options.indexOf('?username=');
    		var idx2 = data.options.indexOf('&password=');
    		
    		var user = data.options.substring(idx1+10,idx2);
    		var pass = data.options.substring(idx2+10, data.options.length);
    		
    		$('#div_out input[name="inputFtpUser"]').val(user);
    		$('#div_out input[name="inputFtpPassword"]').val(pass);
    	},400);
    }
	
	if (data.protocol === "JMS") {
		$('#btn-jms-out').click();
		
		setTimeout(function() {
			$('#inputUserSource').selectpicker('val',data.user);
			$('#div_out input[name="inputJmsId"]').val(data.cpath);
		},400);
	}
	
	if (data.protocol === "FILE") {
		$('#btn-file-out').click();
		
		// ok, vamos dar um tempinho pro html renderizar
		setTimeout(function() {
			$('#inputUserSource').selectpicker('val',data.user);
			$('#div_out input[name="inputFileDirectory"]').val(data.cpath);
			$('#div_out input[name="inputFileFilter"]').val();
			$('#div_out input[name="inputFileCopy"]').val(data.options);
		},400);
	}

	data.context.route.forEach(function(obj) {
		routeToTable(obj);
	})
	
	
    
//    $("#div_out #inputFileRemoveAfter").val();
}


function routeListUsers() {
	var action = {
			action: "listUsers",
			name: name
		};
		
	routews.send(JSON.stringify(action));
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

function routeAddTable(form) {
	if (!formValidate(form)) {
		return;
	}
	var protocol = $('#div_in .btn-info').text();
	var userto = $('#inputUserDestination :selected').attr('data-subtext');
	var filter = $('#inputWhenFilter').val();
	
	console.log('userto=',userto);
	
	if (protocol === "JMS") {
		parseJmsFormRoute(userto, filter);
	} else if (protocol === "FTP") {
		parseFtpFormRoute(userto, filter);
	} else if (protocol === "FILE") {
		parseFileFormRoute(userto, filter);
	}
	
	cleanInputFields();
	
}

function cleanInputFields() {
	
	// FTP
	$('#div_in input[name="inputFtpSite"]').val('');
	$('#div_in input[name="inputFtpUser"]').val('');
	$('#div_in input[name="inputFtpPassword"]').val('');
	
	// JMS
	$('#div_in input[name="inputJmsId"]').val('');
	
	// file
	$('#div_in input[name="inputFileDirectory"]').val('');
	$('#div_in input[name="inputFileFilter"]').val('');
	$('#div_in input[name="inputFileCopy"]').val('');
}

function routeToTable(route) {
	var table = $('#routeTable').find('tbody');
	var row = $('<tr></tr>');
	var id = $('#routeTable tbody tr:last').attr('id');
	
	if (id) {
		++id;
	} else {
		id = 1;
	}
	
	row.attr('id', id);
	var str = "";
	
	str = str + route.protocol;
	str = str + route.url;

	if (route.user)
		str = str + '?username=' + route.user;

	if (route.password) {
		str = str + '&password=$' + route.password + '$';
	}
	
	
	row.append('<td>' + route.user	   + '</td>');
	row.append('<td>' + route.filter   + '</td>');
	row.append('<td>' + route.protocol + '</td>');
	row.append('<td>' + route.cpath	   + '</td>');
	row.append('<td>' + route.options  + '</td>');
	var action = '<a href="#" onclick="removeRoute(' + id + ');"> <span aria-hidden="true" data-i18n="actions.remove" class="glyphicon glyphicon-remove"></span></a>';
	row.append('<td class="text-center">' + action + '<td>');
	table.append(row);
	$('[data-i18n]').i18n();
}

function parseJmsFormRoute(userto, filter) {
	var route = {
			user: userto,
			filter: filter,
			protocol: "JMS",
			cpath: $('#div_in input[name="inputJmsId"]').val(),
			options: ''
	};
	routeToTable(route);
}

function parseFtpFormRoute(userto, filter) {
	var route = {
			user: userto,
			filter: filter,
			protocol: 'FTP',
			cpath: $('#div_in input[name="inputFtpSite"]').val(),
			options: 'username=' + $('#div_in input[name="inputFtpUser"]').val() +
				'&password=' + $('#div_in input[name="inputFtpPassword"]').val()
	};
	routeToTable(route);
}

function parseFileFormRoute(userto, filter) {
	var copy = $('#inputFileCopy').val();
	var options = '';
	
	if (copy) {
		options = copy;
	}
	
	var route = {
			user: userto,
			filter: filter,
			protocol: 'FILE',
			cpath: $('#inputFileDirectory').val(),
			options: options
	};
	routeToTable(route);
}

function editRoute(id) {
	var route = {
			action: "edit",
			id: id
	};
	routews.send(JSON.stringify(route));
	$('#content').load('pages/context/route_form.html');
}

function removeRoute(id) {
	$( '#dialog-confirm').dialog({
      resizable: false,
      height:180,
      modal: true,
      buttons: {
        Yes: function() {
        	var route = {
        			action: "remove",
        			id: id
        	};
        	routews.send(JSON.stringify(route));
        	$('#'+id).remove();
        	$( this ).dialog( "close" );
        },
        Cancel: function() {
          $( this ).dialog( "close" );
        }
      }
    });
}

function routeFormSubmit() {
	
	var id 			= $("#inputRouteId").val();
    var description = $("#inputRouteDescription").val();
    var user 		= $('#inputUserSource option:selected').attr('data-subtext');
    var protocol 	= $('#div_out .btn-info').text();
    var removeAfter = $("#div_out #inputFileRemoveAfter").val();
    var filter, cpath, options;
    
    if (protocol === "FTP") {
		cpath = $('#div_out input[name="inputFtpSite"]').val();
		options  = '?username=' + $('#div_out input[name="inputFtpUser"]').val();
		options += '&password=' + $('#div_out input[name="inputFtpPassword"]').val();
    }
	
	if (protocol === "JMS") {
		cpath = $('#div_out input[name="inputJmsId"]').val();
		options = ""; 
	}
	
	if (protocol === "FILE") {
		cpath 	= $('#div_out input[name="inputFileDirectory"]').val();
		filter 	= $('#div_out input[name="inputFileFilter"]').val();
		options	= $('#div_out input[name="inputFileCopy"]').val();
	}
	
   	submitRouteWS(id, description, user, protocol, removeAfter, filter, cpath, options);
   	listRoutes();
}

function validateAndSubmitRoute(form) {
   routeFormSubmit();
}

function routeTableJson() {
	var data =  {
			route: []
	};
	
	$('#routeTable').find('tr').not(":first").each(function (i, el) {
        var $tds = $(this).find('td');
        
        data.route.push({
        	"user"	 	: $tds.eq(0).text(),
        	"filter" 	: $tds.eq(1).text(),
        	"protocol"  : $tds.eq(2).text(),
        	"cpath"		: $tds.eq(3).text(),
        	"options"	: $tds.eq(4).text(),
        });
    });
    return data;
}


function submitRouteWS(id, description, user, protocol, removeAfter, filter, cpath, options) {
    var action = "add";
    if (id)	{
    	action = "update";
    }
    
	var route = {
        action		: action,
        id			: id,
        description	: description,
        user		: user,
        protocol	: protocol,
        removeAfter	: removeAfter,
        filter		: filter,
        cpath		: cpath,
        options		: options,
        context		: routeTableJson()
    };
	routews.send(JSON.stringify(route));
}

function listRoutes() {
	var route = {
	        action: "list"
	    };
	    routews.send(JSON.stringify(route));
	    $('#content').load('pages/context/route_list.html');
}

function dataTable(data) {
	var table = $('#routeTableList').find('tbody');
	var row = $('<tr></tr>');
	row.attr('id', data.id);
	
	row.append('<td>' + data.description   + '</td>');
	row.append('<td>' + data.user   + '</td>');
	row.append('<td>' + data.protocol + '</td>');
	row.append('<td>' + data.cpath	   + '</td>');
	row.append('<td>' + data.options  + '</td>');
	
	var action = '<a href="#" onclick="editRoute(' + data.id + ');"> <span  aria-hidden="true" data-i18n="actions.edit" class="glyphicon glyphicon-edit"></span></a>';
	action += '<a href="#" onclick="removeRoute(' + data.id + ');"> <span aria-hidden="true" data-i18n="actions.remove" class="glyphicon glyphicon-remove"></span></a>';
	row.append('<td class="text-center">' + action + '<td>');
	table.append(row);
	$('[data-i18n]').i18n();
}
