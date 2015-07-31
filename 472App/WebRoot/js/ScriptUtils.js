/** ******************************************************************************************** */
var err_msg = "";
var err_campo = "";

/** esta funcion acumula los mensajes enviados desde la aplicacion */
function addMsg(pMsg) {	
	if (pMsg && pMsg != "") {
		err_msg = err_msg + pMsg + "\n";
	}
}
var iChars = "<>?";
function validateFieldsValues(){
	var field = "";
	var tmp ="";
	removeChars(document.getElementsByTagName("input"));
	removeChars(document.getElementsByTagName("textarea"));
}

function toUpper(field){
	field.value = field.value.toUpperCase();
}
function removeChars(fields){
	for(var x = 0; x<fields.length; x++){
		if(fields[x].type != "hidden" && fields[x].name != ""){
			field = fields[x].value;
			field = field.replace(/\</g,"");
			field = field.replace(/\?/g,"");
			field = field.replace(/\>/g,"");
			fields[x].value = field;
		}
	}
}

/** esta funcion guarda el primer que tiene error para poner el foco */
function ponerCampo(pcampo){
	if (pcampo && err_campo == ""){
		err_campo = pcampo;
      var tipo = err_campo.type;
      if (tipo && tipo != 'hidden' && !err_campo.readOnly && !err_campo.disabled) {
         err_campo.focus();
         if (tipo == 'text') {
            err_campo.select();
         }
      }
	}
 }

function ponerError(msg, campo) {
   addMsg(msg);
   ponerCampo(campo);
   return false;
}

/** muestra el mensaje y ejecuta el alert */
function showMsg() {
   if (err_msg != "") {
       alert("Verifique la siguiente información:\n\n" + err_msg);
       err_msg = "";
       err_campo = "";
       return false;
   }
   return true;
}

/** ************************************************************************************************ */
 /* esta funcion elimina los espacios a la derecha */
function rtrim(pcadena) {
    pcadena += "";
    for (var i = pcadena.length -1; (i >= 0) && ((pcadena.charAt(i) == ' ')); i--)
        ;
    return pcadena.substring(0, i+1);
}

/* esta funcion elimina espacios a la izquierda */
function ltrim(pcadena) {
    pcadena += "";
    for (var i = 0; (i < pcadena.length) && ((pcadena.charAt(i) == ' ')); i++)
        ;
    if (i == pcadena.length) {
        return "";
    }
    return pcadena.substring(i);
}

/* esta funcion elimina espacion a ambos lados */
function trim(pcadena) {
    return ltrim(rtrim(pcadena));
}

/** ************************************************************************************************ */
var expresionEnteros1 = new RegExp("(\,\,)|([0-9]{4}\,)|(^[0]{0,}\,)|([\,]+[0-9]{0,2}[\,$])");
var expresionEnteros2 = new RegExp("((\\[?)[0-9]\\,[0-9]{3}(\\]?)$)|(^[0-9]+)$")

function validarEmail(pemail) {
/**
 * Valida un email, tiene un error con nombre
 * 
 * @dominio.com
 */
// colocar split y join para que no valide vacios con una longitud minima
  var expresionEmail1 = new RegExp("(@.*@)|(\\.\\.)|(@\\.)|(^\\.)");
  var expresionEmail2 = new RegExp("^.+\\@(\\[?)[a-zA-Z0-9\\-\\.]+\\.([a-zA-Z]{2,3}|[0-9]{1,3})(\\]?)$");
  var email= pemail.split(' ').join('');
  if (email != pemail){
	return false;
  }
  return (!expresionEmail1.test(pemail) && expresionEmail2.test(pemail));
}

function validarCampoEmail(pcampo,pmsg) {
/** valida un campo email */
 if(!validarEmail(pcampo.value)) { 
     addMsg(pmsg);
     ponerCampo(pcampo);
     }	
}

/** Esta funcion valida un email opcional */
function validarCampoEmailOP(pcampo, pmsg){
	if (pcampo.value != ""){
		validarCampoEmail(pcampo,pmsg);
	}
}

/*
 * function validarEntero(number) { //valida que el n?mero sea entero number =
 * trim(number); return (!expresionEnteros1.test(number) &&
 * expresionEnteros2.test(number)); }
 */

/** Esta funcion valida una fecha */
/** Recibe el formato de Dia/Mes/Ano */
function validarCampoDate(pcampo, pmsg){
        if (!validarDate(pcampo.value)){
		addMsg(pmsg);
		ponerCampo(pcampo);
	}
}

/** Esta funcion valida una fecha opcional */
function validarCampoDateOP(pfecha, pmsg){
	if (pfecha.value != ""){
		validarCampoDate(pfecha,pmsg);
	}
}

/** Esta funcion valida una fecha y retorna falso o verdadero */
function validarDate(pdate)   {
  var date = pdate;
  date = date.split(' ').join('');
  var FP = date.split("/");
  if (FP.length != 3) {
	  return false;
  }
  return isSplitDateYMD(FP[0],FP[1],FP[2]);
}

function validarFechaDDMMYYYY(fecha, separador) {
  fecha = fecha.split(' ').join('');
  var FP = fecha.split(separador);
  if (FP.length != 3) {
	  return false;
  }
  return isSplitDateYMD(FP[2],FP[1],FP[0]);
}

function validarFechaYYYYMMDD(fecha, separador) {
  fecha = fecha.split(' ').join('');
  var FP = fecha.split(separador);
  if (FP.length != 3) {
	  return false;
  }
  return isSplitDateYMD(FP[0],FP[1],FP[2]);
}

/**
 * Esta funcion divide una fecha para su comparacion con formato de fecha
 * correcta
 */
function isSplitDateYMD(year,month,day)  {
  var date = new Date(year,month-1,day);
  if (!validarEnteroInterno(day) || !validarEnteroInterno(month) || !validarEnteroInterno(year)) {
     return false;
  }
  return ((date.getFullYear() == year) && ((date.getMonth() + 1) == month) &&  date.getDate() == day);
}

/*
 * Funciones de validaci?n Author: msuarez
 */

// ------------- Funciones Simples, validan un dato diciendo si cumple o no
// ----------------
// valida si una cadena de caracteres es un entero (o contiene s?lo n?meros)
function validarEnteroInterno(dato) {
    var longitud = dato.length;
    var valido = "0123456789";
    if (!longitud || longitud == 0) {
      return false;
    }
    for (var i = 0; i < longitud; i++) {
        if (valido.indexOf(dato.charAt(i)) == -1) {
            return false;
        }
    }
    return true;
}

// Funci?n para validar caracteres no validos la inyecci?n de HTML y SQL
function validarCaracteres(dato) {
    var indy = 0;
    var tam = caracteres.length; // variable global definida al final
    for (var i = 0; i < tam; i++) {
        indy += dato.indexOf(caracteres.charAt(i));
    }
    return indy == -tam;
}
// ------------ Fin Funciones Simples ----------------

// ------------ Funciones Compuestas, utilizan las simples y generan mensaje de
// error --------------
/**
 * Valida un campo entero.
 * 
 * @param campo
 *            Campo que contiene el dato a validar.
 * @param msg
 *            Mensaje de error si el campo no cumple con ser entero, si no se
 *            pasa o es "" la validaci?n es opcional y el campo se deja en
 *            vac?o.
 * @param msgInvChars
 *            Mensaje por si el campo contiene caracteres no v?lidos.
 * @param limInf
 *            M?nimo valor que puede tomar el entero, es opcional y si se pasa
 *            tambi?n se debe pasar su mensaje de error.
 * @param msgInf
 *            Mensaje de error si el entero sobrepasa el l?mite inferior.
 * @param limSup
 *            M?ximo valor que puede tomar el entero, es opcional y si se indica
 *            tambi?n se debe indicar su mensaje de error.
 * @param msgSup
 *            Mensaje de error si el entero sobrepasa el l?mite superior.
 */
function validarEntero(campo, msg, msgInvChars, limInf, msgInf, limSup, msgSup) {
    campo.value = trim(campo.value);
    var dato = campo.value;
    if (!validarCaracteres(dato)) {
       ponerError(msgInvChars, campo);
    }
    if (msg && msg != "") { // si la validaci?n es obligatoria
       if (validarEnteroInterno(campo.value)) {
          dato = parseInt(campo.value);
          campo.value = dato;
          if (limInf && dato < limInf) {
             return ponerError(msgInf, campo);
          }
          if (limSup && dato > limSup) {
             return ponerError(msgSup, campo);
          }
       } else {
         return ponerError(msg, campo);
       }
    } // else { //validaci?n opcional pero el campo debe ser vac?o.
      // campo.value = "";
    // }
    return true;
}

function validarReal(campo, msg, msgInvChars, limInf, msgInf, limSup, msgSup) {
    campo.value = trim(campo.value);
    var dato = campo.value;
    if (!validarCaracteres(dato)) {
       ponerError(msgInvChars, campo);
    }
    if (msg && msg != "") { // si la validaci?n es obligatoria
       if (dato != "" && !isNaN(dato)) {
          dato = parseFloat(campo.value);
          campo.value = dato;
          if (limInf && dato < limInf) {
             return ponerError(msgInf, campo);
          }
          if (limSup && dato > limSup) {
             return ponerError(msgSup, campo);
          }
       } else {
         return ponerError(msg, campo);
       }
    } // else { //validaci?n opcional pero el campo debe ser vac?o.
      // campo.value = "";
    // }
    return true;
}

function validarFecha(campo, msg, msgInvChars, campoLimInf, msgInf, campoLimSup, msgSup) {
    campo.value = trim(campo.value);
    var dato = campo.value;
    var tmp = null;
    if (!validarCaracteres(dato)) {
       ponerError(msgInvChars, campo);
    }
    if (msg && msg != "") { // si la validaci?n es obligatoria
       if (validarDate(campo.value)) {
          tmp = campo.value.split('/');
          var fecha = new Date(tmp[0], tmp[1] - 1,  tmp[2]);
          var fechaTmp = null;
          if (campoLimInf && campoLimInf != null) {
            if (typeof(campoLimInf) == "string") {
               tmp = campoLimInf.split('/');
            } else {
               tmp = campoLimInf.value.split('/');
            }
            fechaTmp = new Date(tmp[0], tmp[1] - 1,  tmp[2]);
            if (fecha < fechaTmp) {
               return ponerError(msgInf, campo);
            }
          }
          if (campoLimSup && campoLimSup != null) {
            if (typeof(campoLimSup) == "string") {
               tmp = campoLimSup.split('/');
            } else {
               tmp = campoLimSup.value.split('/');
            }
            fechaTmp = new Date(tmp[0], tmp[1] - 1,  tmp[2]);
            if (fecha > fechaTmp) {
               return ponerError(msgSup, campo);
            }
          }
       } else {
         return ponerError(msg, campo);
       }
    } // else { //validaci?n opcional pero el campo debe ser vac?o.
      // campo.value = "";
    // }
    return true;
}

function validarCampo(campo, msg, msgInvChars, cantMin, msgCantMin) {
    campo.value = trim(campo.value);
    var dato = campo.value;
    var longitud = dato.length;
    if (!validarCaracteres(dato)) {
       ponerError(msgInvChars, campo);
    }
    if (msg && msg != "") { // si la validaci?n es obligatoria
       if (dato == "") {
          return ponerError(msg, campo);
       } else if (cantMin && longitud < cantMin) {
          return ponerError(msgCantMin, campo);
       }
    } // else { //validaci?n opcional pero el campo debe ser vac?o.
      // campo.value = "";
    // }
    return true;
}
// ------------ Fin Funciones Compuestas ----------------------

/** valida la m?nima longitud de un texto */
function validarText(pvalor,pmin) {
   if (pvalor.split(' ').join('').length < pmin ) {
      return false;
   }
   return true;
}


function validarCampoText(pcampo,pmin,pmsg) {
/** valida la m?nima longitud de un campo obligatorio */
 if(!validarText(pcampo.value,pmin)) { 
      addMsg(pmsg);
      ponerCampo(pcampo);
     }	
}

/* valida la seleccion unica de una lista */
function validarLista(plista, pmsg, pindiceInicial) {
	pindiceInicial = pindiceInicial || 1;
    if (plista.selectedIndex < pindiceInicial)  {
        addMsg(pmsg);
        ponerCampo(plista);
        return false;
    }
    return true;
}

function validarCampoTextOpcional(pcampo,pmin,pmsg ) {
/** valida la m?nima longitud de un campo opcional */
 if (pcampo.value != "") {
     validarCampoText (pcampo, pmin, pmsg);
     }	
}

function validarMaxLength(campo, maxLength) {
	if (campo) {
		var valor = trim(campo.value);
		if (valor && maxLength && valor.length > maxLength) {
			campo.value = valor.substring(0, maxLength);
		}
	}
}

/** Deshabilitar enter */
var NS = (window.Event) ? 1 : 0;

function numeroReal(e) {
   // 0 al 9, backspace y punto
   var code = (NS) ? e.which : event.keyCode;
   return code >= 48 && code <= 57 || code == 8 || code == 46;
}

function numeroEntero(e) {
   // 0 al 9 y backspace
   var code = (NS) ? e.which : event.keyCode;
   return code >= 48 && code <= 57 || code == 8;
}

function checkKey(e) {
   var code = (NS) ? e.which : event.keyCode;
   if (code == "13") {
      return false;
   }
}

function keyToUpperCase(field, evt) {
	if (document.all) {
	    var c = event.keyCode;
	    var C = quitarAcento(String.fromCharCode(c));
	    C = C.charCodeAt();
		event.keyCode = C;
	    return true;
	} else if (document.layers) {
	    var c = evt.which;
	    var C = String.fromCharCode(c).toUpperCase().charCodeAt();
	    netscape.security.PrivilegeManager.enablePrivilege('UniversalBrowserWrite');
	    evt.which = C;
	    return true;
	} else {
		return true;
	}
}

function camposMayusculas(form) {
	for (var i = 0; i < form.elements.length; i++) {
		if (form.elements[i].type && form.elements[i].type == "text" || form.elements[i].type == "textarea" && !form.elements[i].readOnly) {
			form.elements[i].onkeypress = keyToUpperCase;
		}
	}
}

function quitarAcento(caracter) {
   caracter = caracter.toUpperCase();
   var indice = vocalesAcento.indexOf(caracter);
   if (indice != -1) {
      indice = indice % 5; // 5 vocales
      return vocales.charAt(indice);
   }
   return caracter;
}

function addDocumentListener(eventType, eventListener) {
   eventType = eventType.toLowerCase();
   var eh = 'document.on' + eventType + '= eventListener';
   if (document.addEventListener) {
      document.addEventListener(eventType, eventListener, true); // useCapture
																	// = false
   } else if (document.attachEvent) {
      document.attachEvent('on' + eventType, eventListener);
   } else {
      eval(eh);
   }
}

function checkCtrlAlt(evt) {
   var code = (NS) ? evt.which : event.keyCode;
   manageSessionVerifier();
	if (window.opener && window.opener.name == "mainFrame") {
		window.opener.manageSessionVerifier();
	}
   switch (0) {
   // switch (code) {
      case 116: // F5
         // alert("No se permite el uso de la tecla F5.");
         if (event) {
         	event.keyCode = 0;
			event.returnValue = false; 
         }
         return false;
      case 114: // F3
         // alert("No se permite el uso de la tecla F3.");
         if (event) {
         	event.keyCode = 0;
			event.returnValue = false; 
         }
         return false;
      case 122: // F11
         // alert("No se permite el uso de la tecla F11.");
         if (event) {
         	event.keyCode = 0;
			event.returnValue = false; 
         }
         return false;
      case 27: // esc
      	 if (window.opener && window.opener.name == "mainFrame") {
  	 		window.close();
      	 }
         return false;
      case 8: // backspace
         if (event) {
            if (event.srcElement.type == "text" ||
                  event.srcElement.type == "textarea" ||
                  event.srcElement.type == "password") {
         	   return true;
         	}
			event.keyCode = 0;
			event.returnValue = false; 
         }
         // alert("No se permite el uso de la tecla Backspace.");
         return false;
      case 17: // Ctrl
         /*
			 * if (event) { //event.cancelBubble = true; event.keyCode = 0;
			 * event.returnValue = false; }
			 */
         alert("No se permite el uso de la tecla Ctrl.");
         return false;
      case 18: // Alt
         /*
			 * if (event) { event.keyCode = 0; event.returnValue = false; }
			 */
         alert("No se permite el uso de la tecla Alt.");
         return false;
   }
   return true;
}

function contextMenu() {
	if (event) {
		event.cancelBubble = true;
		event.returnValue = false; 
	}
    // alert("No se permite el men? de contexto.");
	return false;
}

var MILLIS_MINUTO = 60000; // 1000;
var cantMinutosVerificar = 2;
var cantMinutosTerminar = 5;
var idVerificador = null;
var idTerminador = null;
var urlLogOut = null;

function manageSessionVerifier(event) {
   if (idTerminador != null && idVerificador != null) {
		clearInterval(idVerificador);
		idVerificador = setInterval("verificarSession()", cantMinutosVerificar * MILLIS_MINUTO);
		clearInterval(idTerminador);
		idTerminador = null;
	} else if (idVerificador != null) {
		clearInterval(idVerificador);
		idVerificador = setInterval("verificarSession()", cantMinutosVerificar * MILLIS_MINUTO);
	}
}

function noCtrlAlt(logOutDir) {
	// Control de tiempo de sesi?n.
	if (logOutDir) { // si pasaron direcci?n logout
		if (window.opener && window.opener.name == "mainFrame") { // si es pop
																	// up
			addDocumentListener('mousedown', window.opener.manageSessionVerifier); // MSUAREZ:
																					// como
																					// se
																					// est?
																					// usando
																					// un
																					// keep
																					// alive,
																					// no
																					// debemos
																					// reiniciar
																					// su
																					// contador.
		} else {
			urlLogOut = logOutDir;
			idVerificador = setInterval("verificarSession()", cantMinutosVerificar * MILLIS_MINUTO);
		    addDocumentListener('mousedown', manageSessionVerifier); // MSUAREZ:
																		// como
																		// se
																		// est?
																		// usando
																		// un
																		// keep
																		// alive,
																		// no
																		// debemos
																		// reiniciar
																		// su
																		// contador.
	    }
	}
   addDocumentListener('keydown', checkCtrlAlt);
   // addDocumentListener('contextmenu', contextMenu); //Desactiva men? del
	// contexto del mouse.
}

function verificarSession() {
	keepAlive(urlLogOut);
	// clearInterval(idVerificador);
	// idTerminador = setInterval("terminarSession()", cantMinutosTerminar *
	// MILLIS_MINUTO);
	// alert("No se ha detectado actividad en los últimos " +
	// cantMinutosVerificar + " minutos, su sesión se desconectará en " +
	// cantMinutosTerminar + " minutos.");
}

function terminarSession() {
	alert("Su sesion en Correspondencia ha terminado.");
	clearInterval(idVerificador);
	clearInterval(idTerminador);
	idVerificador = null;
	idTerminador = null;
	window.parent.location.href = urlLogOut;
}

/* Caracteres especiales */
/*
 * function validarCharEsp(campo){ var lst = false; var expReg = new
 * RegExp("[\=\<\>\'\"\?]"); lst = expReg.exec(campo.value); if (lst){ addMsg
 * ("campo con caracteres no válidos"); ponerCampo(campo); } }
 */

function openerHistoryBack(){
    if(window.opener.closed == false && window.opener.document != null) {
        var blnConfirmacion  = confirm("Si acepta regresar perderá los cambios que no haya guardado.");
        if (blnConfirmacion) {
            window.opener.focus();
            window.opener.history.back();
            window.close();
        }
    }
}

function waitSubmit(formulario, targetWindow) {
   formulario.target = targetWindow;
   formulario.submit();
}

function abrirPopUp(formulario, targetWindow) {
    window.open("", targetWindow, "toolbar=no,location=no,width=730,height=440,scrollbars=yes,status=yes,resizable=no").focus();
    setTimeout("waitSubmit(document." + formulario.name + ", '" + targetWindow + "')", 50);
}

function execute(accion, popUp, form) {
   if (!form) {
      form = document.frm;
   }
   form.accion.value = accion;
   if (popUp) {
      abrirPopUp(form, popUp);
   } else {
      form.target = "_self";
      form.submit();
   }
}

function verificarChecks(checks, index) {
   if (checks.length) {
      var tam = checks.length;
      for (var i = 0; i < tam; i++) {
         if (i != index) {
            checks[i].checked = false;
         }
      }
   }
   return true;
}

/**
 * Author: msuarez
 * 
 * Nota: s?lo se prob? en Internet Explorer.
 */
function moveLayer(layer, x, y) {
	var layerX, layerY;
	if(document.getElementById) {  // si se trata de Netscape 6??
		layerX ='document.getElementById("' + layer + '").style.left = ' + x;
		layerY ='document.getElementById("' + layer + '").style.top = ' + y;
	} else if(document.layers) {  // si se trata de Netscape 4
		layerX ='document.' + layer + '.left = ' + x;
		layerY ='document.' + layer + '.top = ' + y;
	} else if(document.all) { // si se trata de Internet Explorer 4
		layerX = layer + '.style.pixelLeft = ' + x;
		layerY = layer + '.style.pixelTop = ' + y;
	}
	eval(layerX);
	eval(layerY);
}

function moveToElement(layer, elemento) {
   if (document.getElementById) {
      elemento = document.getElementById(elemento);
      moveLayer(layer, elemento.style.left, elemento.style.top);
   }
}

/**
 * Author: msuarez
 * 
 * Nota: s?lo se prob? en Internet Explorer.
 */
function resizeLayer(layer, width, height) {
	var layerW, layerH;
	if(document.getElementById) {  // si se trata de Netscape 6??
		layerW ='document.getElementById("' + layer + '").style.width = ' + width;
		layerH ='document.getElementById("' + layer + '").style.height = ' + height;
	} else if(document.layers) {  // si se trata de Netscape 4
		layerW ='document.' + layer + '.width = ' + width;
		layerH ='document.' + layer + '.height = ' + height;
	} else if(document.all) { // si se trata de Internet Explorer 4
		layerW = layer + '.style.width = ' + width;
		layerH = layer + '.style.height = ' + height;
	}
	eval(layerW);
	eval(layerH);
}

function keepAlive(url) {
	if (url != "") {
      if (window.XMLHttpRequest) { // Non-IE browsers
      	 	req = new XMLHttpRequest();
       		// req.onreadystatechange = processListarCorrespondencia;
        	try {
          		if (url.indexOf("?") == -1) {
	          		req.open("GET", url + "?rnd=" + (new Date().getTime()), true);
          		} else {
	          		req.open("GET", url + "&rnd=" + (new Date().getTime()), true);
          		}
   			} catch (e) {
          		alert(e);
       		}
        	req.send(null);
      } else if (window.ActiveXObject) { // IE
	        req = new ActiveXObject("Microsoft.XMLHTTP");
	        if (req) {
	            // req.onreadystatechange = processListarCorrespondencia;
      			if (url.indexOf("?") == -1) {
		          req.open("GET", url + "?rnd=" + (new Date().getTime()), false);
      			} else {
		          req.open("GET", url + "&rnd=" + (new Date().getTime()), false);
		        }
	            req.send();
	        }
	  }
	}
}

var caracteresalf = "abcdefghijklmnopqrstuvwxyz";
var vocales = "AEIOU";
var vocalesAcento = "ÁÉÍÓÚÄËÏÖÜ";
var mensajeCaracteres="Por favor no ingrese ninguno de los siguientes caracteres: \' \' , \" \" , \< , \>";
var fechaHoy; // Para validar fechas con respecto al dia de hoy
var caracteres="'\"><";