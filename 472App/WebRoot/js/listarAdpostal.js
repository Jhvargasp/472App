	if (navigator.userAgent.indexOf("Safari") > 0)
	{
	  isSafari = true;
	  isMoz = false;
	  isIE = false;
	}
	else if (navigator.product == "Gecko")
	{
	  isSafari = false;
	  isMoz = true;
	  isIE = false;
    }
	else
	{
	  isSafari = false;
	  isMoz = false;
	  isIE = true;
	}	
   
   var RE_NUM = /^\-?\d+$/;
   var urls="";
   var busquedaDataID="";
   var current=0;
   var last=0;
   var fila=null;
   var nRadicado=null;
   var nTipo=null;
   var labels;
   var fondoOriginalColumnas = "#C7D4C0"; //b5c6d2
   var fondoNuevoColumnas = "#c0c0c0";
 
 /** @author David Ramírez Marín: david.ramirez@mvm.com.co
 * @version 1.0
 * @see 
 * #########################################################
 * #modifica: David Ramírez Marín: david.ramirez@mvm.com.co
 * #motivo:   Baja los datos del al seleccionar
 * #fragmento: 
 * #fecha: 28-jul-2005
 * #########################################################
 */ 
 function bajarDatos(numeroRadicacion,tipo,estadoVisualiza,valorVisualiza,idRol,rol)
   {
   		nRadicado=document.getElementById(numeroRadicacion);
   		nTipo=document.getElementById(tipo);
   		document.getElementById(estadoVisualiza).value=valorVisualiza;
   		document.getElementById(idRol).value=rol;
   }
   
 /** @author David Ramírez Marín: david.ramirez@mvm.com.co
 * @version 1.0
 * @see 
 * #########################################################
 * #modifica: David Ramírez Marín: david.ramirez@mvm.com.co
 * #motivo:   Crea los labesl para las direferentes listados de correspondencia
 * #fragmento: 
 * #fecha: 28-jul-2005
 * #########################################################
 */
 function crearlabels(tipo)
   {

      labels = new Array('Número radicado', 'Fecha de radicación', 'Origen', 'Destino', 'Estado Actual'); //'Asunto'
      
   }
  /** @author David Ramírez Marín: david.ramirez@mvm.com.co
 * @version 1.0
 * @see 
 * #########################################################
 * #modifica: David Ramírez Marín: david.ramirez@mvm.com.co
 * #motivo:   Busca un lista segun los filtros de budqueda
 * #fragmento: 
 * #fecha: 28-jul-2005
 * #########################################################
 * url: Accion a ejecutar
 * chooseTipo: tipo de radicacion
 * fechaInicio: fecha de inicio
 * fechaFinal: fecha final
 * idCorrespondencia: id de la correspondencia
 * idPopup: pop de la table para ubicar el contenido
 * operation: tipo de operacion
 */ 
 function buscar(url,chooseTipo,fechaInicio,fechaFinal,idCorrespondencia,idEstado,idPopup,operation) 
 {  
	 
    crearlabels();
       
 	var valiFechInic=document.getElementById(fechaInicio);
 	var valiFechFina=document.getElementById(fechaFinal);
 	var valida=true;
 	var mensaje="";
 	var number = "";
 	var resFechaInic=null;
 	var resFechaFina=null;
 	
 	var esNumeroTipo=document.getElementById(chooseTipo).value;
 	if(!RE_NUM.exec(esNumeroTipo))
 	{
 		
    	var box = document.getElementById(chooseTipo);
    	if (box.options) {
    		number = box.options[box.selectedIndex].value;
		}
    }
    else
     number=esNumeroTipo;	
     


 	if(getRealLength(valiFechInic.value)>0)
		{
			resFechaInic=cal_prs_date_rad(valiFechInic.value);
			if(resFechaInic==null)
			{
				valida=false;
				mensaje+=".\nFecha de inicio Invalida";
			}
		}	
		
	if(getRealLength(valiFechFina.value)>0)
		{
			resFechaFina=cal_prs_date_rad(valiFechFina.value);
			if(resFechaFina==null)
			{
				valida=false;
				mensaje+=".\nFecha Final Invalida";
			}
		}	
	if(resFechaInic!=null && resFechaFina!=null)
	{
		if(resFechaInic.getTime()>resFechaFina.getTime())
		{
			valida=false;
			mensaje+=".\nFecha inicial no puede ser mayor a la final";
		}
	}
 	if(valida==true)
 	 	retrieveListarAdpostal(url,fechaInicio,fechaFinal,idCorrespondencia,idEstado,idPopup,operation+"&tipo="+number+""); 
	else
	  alert(mensaje); 	
	 	
 }
   
   /** @author David Ramírez Marín: david.ramirez@mvm.com.co
 * @version 1.0
 * @see 
 * #########################################################
 * #modifica: David Ramírez Marín: david.ramirez@mvm.com.co
 * #motivo:   Al selecciona un elemento de la busqueda se ejecuta el form para editar los datos
 * #fragmento: 
 * #fecha: 28-jul-2005
 * #########################################################
 * pos: Posicion seleccionada
 */  
  function SeleccionClick(pos)
  {
		if(nRadicado!=null && nTipo!=null)
		{
		
				if(fila!=null)
				{
					var nodosFilas=fila[pos].childNodes;
					nRadicado.value=(nodosFilas[0].firstChild != null ? nodosFilas[0].firstChild.data : "");
					nTipo.value=(nodosFilas[1].firstChild != null ? nodosFilas[1].firstChild.data : "");
					alert("enviados T1");
					document.forms[0].submit();
			    }
			    else
			    {
					alert('No se han cargado datos');	    	
			    }
		}
		else
		{
		 	if(fila!=null)
			{
		 		//var nodosFilas=fila[pos].childNodes;
		 		var nodosFilas = fila[pos].getElementsByTagName("Column");
		 		
		 		var idRadicacion=(nodosFilas[0].firstChild != null ? nodosFilas[0].firstChild.data : "");
		 		var numeroRadicacion=(nodosFilas[1].firstChild != null ? nodosFilas[1].firstChild.data : "");
		 		var nombreDependencia=(nodosFilas[3].firstChild != null ? nodosFilas[3].firstChild.data : "");
		 		/*alert ("lenc: "+nodosFilas.length); 
		 		//aca voy..
		 		
		 		var msg="";
		 		var i=0;
		 		for( i=0; i<nodosFilas.length; i++)
		 		{
			 		msg+=" pos: "+i;
			 		msg+=" val: "+nodosFilas[i].firstChild.data;
		 		}
		 		
		 		alert (msg);*/
		 		var idDependencia=(nodosFilas[6].firstChild != null ? nodosFilas[6].firstChild.data : "");
		 		
		 		
		 		
		 		document.forms[0].action=document.forms[0].action+"?idRadicacion="+escape(idRadicacion)+"&numeroRadicacion="+escape(numeroRadicacion)+"&dependenciaNombre="+escape(nombreDependencia)+"&depdenciaId="+escape(idDependencia);
		 		//alert(document.forms[0].action);
		 		document.forms[0].submit();
		 	}
		 	else	
		 	 alert('No ha  determinado endonde guardar los datos de radicado y tipo');
		 }	
	
  }
 
    /** @author David Ramírez Marín: david.ramirez@mvm.com.co
 * @version 1.0
 * @see 
 * #########################################################
 * #modifica: David Ramírez Marín: david.ramirez@mvm.com.co
 * #motivo:  Limpia la forma
 * #fragmento: 
 * #fecha: 28-jul-2005
 * #########################################################
 */  
 function limpiarForma(url,fechaInicio,fechaFinal,idRadica,idEstado,idPopup,operation)
 {   
    crearlabels();
     
 	document.getElementById(fechaInicio).value="";
 	document.getElementById(fechaFinal).value="";
 	document.getElementById(idRadica).value="";
 	retrieveListarAdpostal(url,fechaInicio,fechaFinal,idRadica,idEstado,idPopup,operation); 
 }

  /** @author David Ramírez Marín: david.ramirez@mvm.com.co
 * @version 1.0
 * @see 
 * #########################################################
 * #modifica: David Ramírez Marín: david.ramirez@mvm.com.co
 * #motivo:   Cierra la ventana
 * #fragmento: 
 * #fecha: 28-jul-2005
 * #########################################################
 * pos: Posicion seleccionada
 */  
 function cerrar()
 {
 	window.opener.cerrarVentana();
 }
 
   /** @author David Ramírez Marín: david.ramirez@mvm.com.co
 * @version 1.0
 * @see 
 * #########################################################
 * #modifica: David Ramírez Marín: david.ramirez@mvm.com.co
 * #motivo:   AJAX para listar la correspondencia
 * #fragmento: 
 * #fecha: 28-jul-2005
 * #########################################################
 * url: Accion a ejecutar
 * fechaInicio: fecha de inicio filtro
 * fechaFinal: fecha final filtro
 * idCorrespondencia: id de la correspondencia
 */  
  function retrieveListarAdpostal(url,fechaInicio,fechaFinal,idCorrespondencia,idEstado,idPopup,operation) 
  {
		document.body.style.cursor='wait';
		var fInicio= document.getElementById(fechaInicio);
    	var fFin= 	 document.getElementById(fechaFinal);
    	var idCorr=  document.getElementById(idCorrespondencia);
		var estado=  document.getElementById(idEstado);

    	var popup=   document.getElementById(idPopup);
		document.getElementById(idPopup).innerHTML ="";
		
	    urls=url;
	    busquedaDataID=busquedaData;
	    current=0;
   		last=0;
	    if (url != "") 
	    {
		      if (window.XMLHttpRequest) 
		      { // Non-IE browsers
		      	 	req = new XMLHttpRequest();
		       		 req.onreadystatechange = processListarAdpostal;
		        	try {
		          			if (url.indexOf("?") == -1) {
			          			req.open("GET", url+"?operation="+operation+"&fechaInicio="+escape(fInicio.value)+"&fechaFin="+escape(fFin.value)+"&idCorrespondencia="+escape(idCorr.value) + "&estado="+escape(estado.value) + "&rnd=" + (new Date().getTime()), true);
		          			} else {
			          			req.open("GET", url+"&operation="+operation+"&fechaInicio="+escape(fInicio.value)+"&fechaFin="+escape(fFin.value)+"&idCorrespondencia="+escape(idCorr.value) +"&estado="+escape(estado.value) + "&rnd=" + (new Date().getTime()), true);
		          			}
		       			} catch (e) {
		          			alert(e);
		       						}
		        req.send(null);
		        
		      } 
		      else if (window.ActiveXObject) 
		      { // IE
			        req = new ActiveXObject("Microsoft.XMLHTTP");
			        if (req) 
			        {
			            req.onreadystatechange = processListarAdpostal;
			            //alert(url+"?operation="+operation+"&fechaInicio="+escape(fInicio.value)+"&fechaFin="+escape(fFin.value)+"&idCorrespondencia="+escape(idCorr.value));
	          			if (url.indexOf("?") == -1) {
			          			req.open("GET", url+"?operation="+operation+"&fechaInicio="+escape(fInicio.value)+"&fechaFin="+escape(fFin.value)+"&idCorrespondencia="+escape(idCorr.value) + "&estado="+escape(estado.value) + "&rnd=" + (new Date().getTime()), false);
		          			} else {
			          			req.open("GET", url+"&operation="+operation+"&fechaInicio="+escape(fInicio.value)+"&fechaFin="+escape(fFin.value)+"&idCorrespondencia="+escape(idCorr.value) +"&estado="+escape(estado.value) + "&rnd=" + (new Date().getTime()), false);
				         }
			          req.send();
			        }
		      }
	    }
	    else
	    {
	    	alert("la accion no puede estar vacia");
	    }
   function processListarAdpostal() 
   {      

	   	 if (req.readyState == 4) 
		 { // Complete
		      	if (req.status == 200) 
		      	{ // OK response
		        	//document.getElementById("busquedaData").innerHTML = req.responseText;
		        	//alert(req.responseText);
		        	var xml = req.responseXML;
		        	html = "<table cellPadding='3' cellSpacing='1' align='center' class='cssTable' width='100%'>";
		        	   		html += "<tr>";
		        	   		   		    
		        	   		   		     html += "<th class='wcmListViewHeader' width='10%' align='center' onClick=\"retrieveListarAdpostal('"+url+"','"+fechaInicio+"','"+fechaFinal+"','"+idCorrespondencia+"','"+idEstado+"','"+idPopup+"','1&orderby=1');\" onMouseOver=\"style.background='" + fondoNuevoColumnas + "';\" onMouseOut=\"style.background='" + fondoOriginalColumnas + "';\">"+labels[0]+"</th>";
		        	   		   		     html += "<th class='wcmListViewHeader' width='15%' align='center' onClick=\"retrieveListarAdpostal('"+url+"','"+fechaInicio+"','"+fechaFinal+"','"+idCorrespondencia+"','"+idEstado+"','"+idPopup+"','1&orderby=2');\" onMouseOver=\"style.background='" + fondoNuevoColumnas + "';\" onMouseOut=\"style.background='" + fondoOriginalColumnas + "';\">"+labels[1]+"</th>";
		        	   		   		     html += "<th class='wcmListViewHeader' width='25%' align='center' onClick=\"retrieveListarAdpostal('"+url+"','"+fechaInicio+"','"+fechaFinal+"','"+idCorrespondencia+"','"+idEstado+"','"+idPopup+"','1&orderby=3');\" onMouseOver=\"style.background='" + fondoNuevoColumnas + "';\" onMouseOut=\"style.background='" + fondoOriginalColumnas + "';\">"+labels[2]+"</th>";
		        	   		   		     html += "<th class='wcmListViewHeader' width='25%' align='center' onClick=\"retrieveListarAdpostal('"+url+"','"+fechaInicio+"','"+fechaFinal+"','"+idCorrespondencia+"','"+idEstado+"','"+idPopup+"','1&orderby=4');\" onMouseOver=\"style.background='" + fondoNuevoColumnas + "';\" onMouseOut=\"style.background='" + fondoOriginalColumnas + "';\">"+labels[3]+"</th>";
		        	   		   		     html += "<th class='wcmListViewHeader' width='12%' align='center' onClick=\"retrieveListarAdpostal('"+url+"','"+fechaInicio+"','"+fechaFinal+"','"+idCorrespondencia+"','"+idEstado+"','"+idPopup+"','1&orderby=5');\" onMouseOver=\"style.background='" + fondoNuevoColumnas + "';\" onMouseOut=\"style.background='" + fondoOriginalColumnas + "';\">"+labels[4]+"</th>";
		        	   		   		     
		        	   	 html += "</tr>";	
					   				
					        		fila = xml.getElementsByTagName("Row");   			        				
					        		if(fila!=null && fila.length>0)
					        		{
						        			for (var i=0;i<fila.length;i++)					        		
						        			{
						        				var nodosFilas = fila[i].getElementsByTagName("Column");

							        				if(nodosFilas.length>0)
							        				{
							        						if(i%2==0)
							        							strStyle="wcmFormRowEven";
							        						else
							        						    strStyle="wcmFormRowOdd";	
							        					
							        						html+="<tr class='"+strStyle+"'>";
							        								
							        								html += "<td class='cssTD' valign='top' align='center'>"+(nodosFilas[1].firstChild != null ? nodosFilas[1].firstChild.data : "")+"</td>";
							        								html += "<td class='cssTD' valign='top'align='center'><A class=wcmLink href='javascript:SeleccionClick("+i+")'>"+(nodosFilas[2].firstChild != null ? nodosFilas[2].firstChild.data : "")+"</A></td>";
							        								html += "<td class='cssTD' valign='top'align='center'>"+(nodosFilas[3].firstChild != null ? nodosFilas[3].firstChild.data : "")+"</td>";
							        								html += "<td class='cssTD' align='center' valign='top'>"+(nodosFilas[4].firstChild != null ? nodosFilas[4].firstChild.data : "")+"</td>";
							        								html += "<td class='cssTD' align='center' valign='top'>"+(nodosFilas[5].firstChild != null ? nodosFilas[5].firstChild.data : "")+"</td>";
							        						html += "</tr>";
							        						
							        				}//	cierra nodosFilas.length>0
						        			}//cierra for
				       		 		Result=xml.getElementsByTagName("Result");
				       		 		if(Result!=null && Result.length>0)
				       		 		{
				       		 			 current=Result[0].getAttribute('currentPage');
   										 last=Result[0].getAttribute('lastPage');
   										 html+="</table><BR>"
   										 
   										  html+="<TABLE cellSpacing=0 cellPadding=10 align='center' border=0 width='100%'>";
							        		  html+="<TBODY>";
											  		html+="<TR>";
											  		     html+="<td style='padding-top: 2px;' nowrap='true' class='wcmListViewText' align='right'>";
											  		    if( !(parseInt(current) == 1) )
											  		     html+="<a href=\"javascript:anterior('"+url+"','"+fechaInicio+"','"+fechaFinal+"','"+idCorrespondencia+"','"+idEstado+"','"+idPopup+"');\" class='pagingLink'>&lt; Anterior</a>";											  		    
											  		    if( !(current == last) ) 
											  		     html+="<a href=\"javascript:siguiente('"+url+"','"+fechaInicio+"','"+fechaFinal+"','"+idCorrespondencia+"','"+idEstado+"','"+idPopup+"');\" class='pagingLink'>Siguiente &gt;</a>";
   											  		          
   											  		     html+="<span class='pagingLabel'>|</span>"+
												  		       "<span class='pagingLabel'>p&aacute;gina</span>"+
												  		       "<input size='2' class='pagingInput' value='"+current+"' id='pageNumber'>"+
												  		       "<span class='pagingLabel'>&#160;de&#160;"+last+"</span>"+
												  		       "<a href=\"javascript: goTo(document.getElementById('pageNumber').value,'"+url+"','"+fechaInicio+"','"+fechaFinal+"','"+idCorrespondencia+"','"+idEstado+"','"+idPopup+"');\" class='pagingLink'>Ir</a></td>";
											  			                								    		 
	                							         html+="</TR>";
								              html+="</tbody>";
				       		 		}
				       		 	}//cierra if(fila!=null && fila>0)
				       		 	else
				       		 	{
				       		 		 html+="<TR>";
				       		 			 	html+="<td class='wcmSignInFormError' valign='top' COLSPAN='6'>No se encontraron registros";
									 		html+="</TD>";
									 html+="</TR>";	 	
				       		 	}	
				       		 		
				       		 		//xml.getElementsByTagName("Row");
				       		 		//nodes[i].getAttribute("id")
				       		 		//goTo

		        	html+="</table>";
		        	document.getElementById(idPopup).innerHTML =html;
		        	document.body.style.cursor='default';
		      	} // cierra req.status == 200
		      	else 
		      	{
		        	alert("Problem: " + req.statusText);
		      	}//cierra elese
	    }//cierra req.readyState == 4
  }//cierra funcion processStateChanges
} 
   /** @author David Ramírez Marín: david.ramirez@mvm.com.co
 * @version 1.0
 * @see 
 * #########################################################
 * #modifica: David Ramírez Marín: david.ramirez@mvm.com.co
 * #motivo:   Retorna el label segun el tipo de radicacion
 * #fragmento: 
 * #fecha: 28-jul-2005
 * #########################################################

 */ 
  function getTipoRadica(tipoRadica)
  {
  		if(tipoRadica=="1")
  			return "Recibida";
  		if(tipoRadica=="2")
  			return "Enviada";
  		return "Interna";
 }
 
 
  function goTo(value,url,fechaInicio,fechaFinal,idCorrespondencia,idEstado,idPopup,operation) 
  {
	//alert(value);
	
	//if(window.event.keyCode==13)
	//{
		if (!RE_NUM.exec(value))
		{
			alert('Solo son permitidos numeros');
		}
		else
		{
			if(parseInt(value) > parseInt(last) )
			{
				alert(value+' Es un numero mayor a la cantidad de paginas desponibles ('+last+')');
			}
			else
			{
				if(parseInt(value)<1)
				{
					alert("La página no puede ser menor de 1");	
				}
				else
				{
						if(parseInt(value)== parseInt(current) )
						{
							alert("Esta seleccionando la pagina que esta actualmente "+value+'='+current);	
						}
						else
						{
							//retrieveListarCorrespondencia(url,fechaInicio,fechaFinal,idCorrespondencia,idPopup,operation)
							retrieveListarAdpostal(url,fechaInicio,fechaFinal,idCorrespondencia,idEstado,idPopup,"3&goTo="+value); 	
						}
				}		
			}	
		}
	//}
}
   /** @author David Ramírez Marín: david.ramirez@mvm.com.co
 * @version 1.0
 * @see 
 * #########################################################
 * #modifica: David Ramírez Marín: david.ramirez@mvm.com.co
 * #motivo:   Operacion para ir a la siguiente pagina
 * #fragmento: 
 * #fecha: 28-jul-2005
 * #########################################################

 */ 
 function siguiente(url,fechaInicio,fechaFinal,idCorrespondencia,idEstado,idPopup,operation) 
  {
			if(parseInt(current)+parseInt('1') > parseInt(last) )
			{
				alert('Es un numero mayor a la cantidad de paginas desponibles ('+last+')');
			}
			else
			{
			  retrieveListarAdpostal(url,fechaInicio,fechaFinal,idCorrespondencia,idEstado,idPopup,"3&goTo="+(parseInt(current)+parseInt('1'))); 	
			}	
  }
  /** @author David Ramírez Marín: david.ramirez@mvm.com.co
 * @version 1.0
 * @see 
 * #########################################################
 * #modifica: David Ramírez Marín: david.ramirez@mvm.com.co
 * #motivo:   Operacion para ir a la pagina anterior
 * #fragmento: 
 * #fecha: 28-jul-2005
 * #########################################################

 */ 

  function anterior(url,fechaInicio,fechaFinal,idCorrespondencia,idEstado,idPopup,operation) 
  {
			if((parseInt(current)-parseInt('1'))<1)
			{
				alert('Esta en la primera pagina');
			}
			else
			{
			  retrieveListarAdpostal(url,fechaInicio,fechaFinal,idCorrespondencia,idEstado,idPopup,"3&goTo="+(parseInt(current)-parseInt('1'))); 	
			}	
  }	
   /** @author David Ramírez Marín: david.ramirez@mvm.com.co
 * @version 1.0
 * @see 
 * #########################################################
 * #modifica: David Ramírez Marín: david.ramirez@mvm.com.co
 * #motivo:   Operacion para el evento del teclado
 * #fragmento: 
 * #fecha: 28-jul-2005
 * #########################################################

 */ 
 function addKeyListener(element, listener)
 {
  	if (isSafari)
	    element.addEventListener("keydown",listener,false);
	  else if (isMoz)
		    element.addEventListener("keypress",listener,false);
	  else
	    element.attachEvent("onkeydown",listener);
} 



 var RE_NUM = /^\-?\d+$/;
    /** @author David Ramírez Marín: david.ramirez@mvm.com.co
 * @version 1.0
 * @see 
 * #########################################################
 * #modifica: David Ramírez Marín: david.ramirez@mvm.com.co
 * #motivo:   Validacion de fechas
 * #fragmento: 
 * #fecha: 28-jul-2005
 * #########################################################

 */ 
function cal_prs_date_rad (str_date) 
{

	var arr_date = str_date.split('-');
	if (arr_date.length != 3) return null;//cal_error ("Fecha con formato invalido: '" + str_date + "'.\nFormato Valido dd-mm-yyyy.");
	if (!arr_date[0]) return null;//cal_error ("Fecha con formato invalido: '" + str_date + "'.\nNo se ha encontrado el dia del mes.");
	if (!RE_NUM.exec(arr_date[0])) return null;//cal_error ("Dia del mes invalido: '" + arr_date[0] + "'.\nSolo es permitido enteros.");
	if (!arr_date[1]) return null;//cal_error ("Fecha invalida: '" + str_date + "'.\nNo se encontro dia del mes.");
	if (!RE_NUM.exec(arr_date[1])) return null;//cal_error ("Mes invalido: '" + arr_date[1] + "'.\nSolo es permitido enteros.");
	if (!arr_date[2]) return null;//cal_error ("Fecha con formato invalido: '" + str_date + "'.\nNo se puede encontrar el año.");
	if (!RE_NUM.exec(arr_date[2])) return null;//cal_error ("Invalido numero del año: '" + arr_date[2] + "'.\nSolo es permitidos enteros.");

	var dt_date = new Date();
	dt_date.setDate(1);

	if (arr_date[1] < 1 || arr_date[1] > 12) return null;//cal_error ("Mes invalido: '" + arr_date[1] + "'.\nRango permitido 01-12.");
	dt_date.setMonth(arr_date[1]-1);
	 
	if (arr_date[2] < 100) arr_date[2] = Number(arr_date[2]) + (arr_date[2] < NUM_CENTYEAR ? 2000 : 1900);
	dt_date.setFullYear(arr_date[2]);

	var dt_numdays = new Date(arr_date[2], arr_date[1], 0);
	dt_date.setDate(arr_date[0]);
	if (dt_date.getMonth() != (arr_date[1]-1)) return null;//cal_error ("Mes Invalido: '" + arr_date[0] + "'.\nRango permitido  01-"+dt_numdays.getDate()+".");

	return new Date(dt_date.getFullYear(), dt_date.getMonth(), dt_date.getDate());
}

function getRealLength(value)
		{
			return value.trim().length;
		}
		/**
		 * Funcion trim para quitar espacios
		 */	
		 String.prototype.trim=function()
		 {
		   return this.replace(/^\s*|\s*$/g,'');
  		 }

