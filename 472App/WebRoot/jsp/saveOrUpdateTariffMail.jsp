<%@page contentType="text/html"%>
<%
//@page pageEncoding="UTF-8"
%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
	<LINK href="${pageContext.request.contextPath}/css/Wcm.css"
			type=text/css rel=stylesheet>
		<LINK href="${pageContext.request.contextPath}/css/screen.css"
			type=text/css rel=stylesheet>
			<script language="javascript">
        var countDiv=0;
        var maximo=0;
		var minimo=0;
        function updateMax(max){
        	countDiv=max;
		}
        function saveOrUpdate(method){
        	window.document.forms[0].action.value =method;            
            	window.document.forms[0].submit();
        }//saveOrUpdate  
        
        function updateMinMax(){
	        maximo=0;
			minimo=0;
	        var elems=document.forms[0].elements;
	         for (var i=0;i<elems.length;i++){
				var	element=elems[i];
				if (element.type=="text"){
					if(element.name.indexOf("*max")>=0 && (maximo < element.value)){
						maximo=element.value;
						alert("Max:"+maximo);
					}
					if(element.name.indexOf("*min")>=0 && (minimo > element.value)){
						minimo=element.value;
						alert("Min:"+minimo);
					}
				}
			}
        }
        
        function nuevo(idTitle)  {
			var elems=document.forms[0].elements;
			for (var i=0;i<elems.length;i++){
				var	element=elems[i];
				if (element.type=="text"){
					if(element.value.length==0){
						element.focus();
						alert("Hacen falta datos necesarios");
						return;
					}
				}
			}
            var foo = document.getElementById("fooBar");  
            var text=getText(idTitle,countDiv);
            countDiv++;
	    	foo.innerHTML+=text;
        }
        
        function validateMin (name){
        	/*updateMinMax();
        	var val=document.forms[0].elements[name].value;
        	if(val<=maximo){
        		alert('Error en datos, debe ser mayor que el maximo establecido previamente');
        		document.forms[0].elements[name].focus();
        		return;
        	}*/
        }
        
        function validateMax (name){
           /* updateMinMax();
        	var val=document.forms[0].elements[name].value;
        	if(val<=minimo){
        		alert('Error en datos, debe ser mayor que el minimo establecido previamente');
        		document.forms[0].elements[name].focus();
        		return;
        	}*/
        }
        function Borrar(id){
        	 var val=document.forms[1].elements['id'].value=id;
	        document.forms[1].submit();
        	var foo = document.getElementById(divId);  
		    foo.innerHTML="";
	       
        }
        
        function remove(divId){
         var foo = document.getElementById(divId);  
		     foo.innerHTML="";
        }
        
        function getText(name,divId){
        return "<div id='div"+divId+"'><table align='center' border='1'><tr>"+
		"					<td class=wcmFormText align=right>"+name+
		"					</td>"+
		"					<td class=wcmFormText align=right>"+
		"						<input class='wcmFormInput' type='text'"+
		"							name='"+divId+"*min' value=''"+
		"							size='10' onblur='validateMin(\""+divId+"-min\");' />"+
		"					</td>"+
		"					<td class=wcmFormText align=right>"+
		"						<input class='wcmFormInput' type='text'"+
		"							name='"+divId+"*max' value=''"+
		"							size='10' onblur='validateMax(\""+divId+"-min\");' />"+
		"					</td>"+
		"					<td>"+
		"						<input class='wcmFormInput' type='text' name='"+divId+"*value'"+
		"							value='' size='20' />"+
		"					</td>"+
		"					<td>"+
		"						<input class='wcmFormInput' type='button' "+
		"							value='Del.' onclick='remove(\"div"+divId+"\");'/>"+
		"					</td>"+
		"				</tr></table></div>";
        }
       
    </script>
	</head>
	<body class=wcmBody bgColor=white>
		<form action="./guardaTarifas.html">
			<br>
			<div class=wcmFormText align=left>
				MODIFICACIONES A TARIFAS DE CORREO
			</div>
			<br>
			<table align="center" border="1">
				<tbody>
					<tr class="sortable">

						<td class=wcmFormText align=right>
							Tipo Mail
						</td>
						<td class=wcmFormText align=right>
							Peso Min
						</td>
						<td class=wcmFormText align=right>
							Peso Max
						</td>

						<td class=wcmFormText align=right>
							Valor
						</td>
						<td class=wcmFormText align=right>
							.
						</td>

					</tr>
					<c:forEach items="${results}" var="tariffmail">
						<tr>
							<div id="${tariffmail[3]}">
								<td class=wcmFormText align=right>
									${tipoCorreo}
								</td>
								<td class=wcmFormText align=right>
									<input class="wcmFormInput" type="text"
										name="${tariffmail[3]}*min" value="${tariffmail[0]}" size="10" />
								</td>
								<td class=wcmFormText align=right>
									<input class="wcmFormInput" type="text"
										name="${tariffmail[3]}*max" value="${tariffmail[1]}" size="10" />
								</td>

								<td>
									<input class="wcmFormInput" type="text"
										name="${tariffmail[3]}*value" value="${tariffmail[2]}"
										size="20" />
								</td>
								<td>
									<input class="wcmFormInput" type="button"
										onclick="javascript:Borrar('${tariffmail[3]}');"
										value="Borrar" size="20" />
									<script type="text/javascript">
										updateMax('${tariffmail[3]+1}');
										</script>
								</td>
						</tr>



					</c:forEach>
					<tr>
						<td colspan="5">
							<span id="fooBar"></span>
						</td>
					</tr>
					<tr>
						<td colspan="5" align="center">
							<input type="button" class="wcmFormInput"
								onclick="javascript:saveOrUpdate('update')" name="Update"
								value="Actualizar Datos" />
						</td>
					</tr>
					<tr>
						<td colspan="5" align="center">
							<input type="button" class="wcmFormInput"
								onclick="javascript: nuevo('${tipoCorreo}');" value="Nuevo" />
						</td>
					</tr>
					<tr>
						<td colspan="5" align="center">
							<input type="button" class="wcmFormInput"
								onclick="javascript: history.go(-1)" value="Atras" />
						</td>
					</tr>
				</tbody>
			</table>
			<input type="hidden" name="tipoCorre" value="${tipoCorreo}" />

		</form>

		<form action="./borraTarifa.html">
			<input type="hidden" name="id" value="" />
			<input type="hidden" name="idT" value="${tipoCorreo}" />
		</form>
	</body>
</html>
