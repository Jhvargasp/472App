var nextfield = "";
var validfields = new Array();
var status = 0;
function onKeyPress(e) {
	var keycode;
	if (window.event)
		keycode = window.event.keyCode;
	else if (e)
		keycode = e.keyCode;
	else
		return true;

	if (keycode == 13) {
	
		//jvargas: click en boton radicar
		if (status + 1 >= validfields.length)
			status = -1;
		if(document.getElementsByName( validfields[status ])[0].id=="radicar"){
			document.getElementsByName( validfields[status ])[0].click();
		}
		
		
		setTimeout("document.getElementsByName('" + validfields[status + 1]
				+ "')[0].focus()", 0);
		status++;
		return false;
	}
}
function setNextToTab(next) {
	nextfield = next;
}

function setStatus(field) {
	for(var x = 0; x<validfields.length; x++){
		if(validfields[x]==field){
			status = x;
		}
	}
}
function initTabulation() {
	var fields = document.forms[0].elements;
	for ( var x = 0; x < fields.length; x++) {
		if (fields[x].type != "hidden" && fields[x].name != "") {
			validfields.push(fields[x].name)
			fields[x].onfocus = function() {
				setStatus(document.activeElement.name);
			};
		}
	}

	
	var links=document.getElementsByTagName('a');
	
	for ( var x = 0; x < links.length; x++) {
		
		if (links[x].type != "hidden" && links[x].name != "") {
			
			validfields.push(links[x].name);
			links[x].onfocus = function() {
				setStatus(links[x].name);
			};
		}
	}
	
}

