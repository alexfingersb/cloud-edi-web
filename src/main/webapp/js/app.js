/**
 * Author: Alexandre Finger Sobrinho E-Mail: alex.fingersb@gmail.com
 */

function configureLocale(element) {
	var lng = "";
	var data = "";

	if (element) {
		lng = element.attr('id');
		data = element.attr('data-lang');
	} else {
		lng = i18n.lng();
		data = lng;
	}

	configureValidationLocale(data);

	i18n.setLng(lng, {
		fixLng : true
	}, function(translation) {
		$('[data-i18n]').i18n();
	});
}

function configureValidationLocale(lng) {
	var lng = parseValidationLang(lng);
	$.validate({
		lang : lng,
		validateOnBlur : false,
		showHelpOnFocus : false,
		addSuggestions : false
	});
}

function parseValidationLang(lng) {
	if (lng === 'pt-BR') {
		lng = 'pt';
	} else if (lng === 'en-US') {
		lng = 'en';
	}
	return lng;
}

// Validation configuration
var errors = [];
var conf = {
	onElementValidate : function(valid, $el, $form, errorMess) {
		if (!valid) {
			errors.push({
				el : $el,
				error : errorMess
			});
		}
	}
};

/**
 * Validate the form inputs to required and email fields
 */
function formValidate($form) {
	// reset error array
	errors = [];
	if (!$($form).isValid(conf, false)) {
		return false;
	} else {
		return true;
	}
}