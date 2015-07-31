<%@ page language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="tagdisplay"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>


	<head>
		<title></title>
		<script type="text/javascript" language="JavaScript"
			src='${pageContext.request.contextPath}/js/ScriptUtils.js'></script>
		<LINK href="${pageContext.request.contextPath}/css/Wcm.css"
			type=text/css rel=stylesheet>
		<LINK href="${pageContext.request.contextPath}/css/screen.css"
			type=text/css rel=stylesheet>
		<script src='${pageContext.request.contextPath}/js/listarAdpostal.js'></script>
		<script src='${pageContext.request.contextPath}/js/tabulation.js'></script>


		<!-- main calendar program -->
		<script type="text/javascript"
			src='${pageContext.request.contextPath}/js/jscalendar/calendar.js'></script>

		<!-- language for the calendar -->
		<script type="text/javascript"
			src='${pageContext.request.contextPath}/js/jscalendar/lang/calendar-es.js'></script>

		<!-- the following script defines the Calendar.setup helper function, which makes
       adding a calendar a matter of 1 or 2 lines of code. -->
		<script type="text/javascript"
			src='${pageContext.request.contextPath}/js/jscalendar/calendar-setup.js'></script>
		<script type="text/javascript">
	function editar(rad) {
		document.getElementById("idDespacho").value = rad;
		document.forms[0].submit();
	}
	function send(rad) {
		document.forms[0].action="imprimirDespacho.html";
		document.getElementById("idDespacho").value = rad;
		document.forms[0].submit();
	}
</script>

	</head>
	<body>
		<form action="editarDespacho.html" method="post">
			<input id="idDespacho" name="idDespacho">
			<input id="idCorrespondencia" name="idCorrespondencia" value="${idCorrespondencia}">
		</form>

		<table cellpadding='0' cellspacing='0' border='0' width='100%'>
			<tr>

				<td width='40%' colspan="2">
					<div class='wcmPathLabel' nowrap>
						Despachar correo
					</div>
				</td>
			</tr>
		</table>
		<BR>

		<TABLE cellSpacing=0 cellPadding=2 border=0 valign="top">
			<TBODY>
				<TR>
					<TD class=wcmPathLabel align=right>
						Número de radicacion&nbsp;
					</TD>
					<TD class="wcmFormText">
						${numRad}
					</TD>
				</TR>
				<TR>
					<TD class=wcmPathLabel align=right>
						Dependencia&nbsp;
					</TD>
					<TD class="wcmFormText">
						${dependencia}
					</TD>
				</TR>
			</TBODY>
		</TABLE>
		<BR>

		<A class=wcmLink href="javaScript:editar('')">Nuevo</A> |
		<A class=wcmLink id="listarActualizar" href="">Actualizar listado</A>
		<br>
		<br>
		<table width="80%" align="center">
			<tr>
				<td>
					<tagdisplay:table sort="list" name="${results}"
						requestURI="buscaDespachos.html" id="document" export="false"
						pagesize="20" cellpadding="5" excludedParams="">
						<tagdisplay:column media='html' title="Remite">
							<a href="javascript:editar('${document.Id}');">${document.InstitucionEmisorRemitente}</a>
						</tagdisplay:column>

						<tagdisplay:column property="Destinatario" title="Destino" />
						<tagdisplay:column property="TipoDeCorreo" title="Tipo Correo" />
						
						<tagdisplay:column property="Peso" title="Peso" />
						<tagdisplay:column property="Precio" title="Valor" />
						<tagdisplay:column property="Ciudad" title="Ciudad" />
						<tagdisplay:column>
							<c:if test="${document.Estado=='Registrado'}">
								<a href="javascript:send('${document.Id}');">Imprimir</a>
							</c:if>
		
						</tagdisplay:column>

					</tagdisplay:table>
				</td>
			</tr>
		</table>

		<script type="text/javascript">
	document.onkeypress = onKeyPress;
	initTabulation();
</script>
	</body>
</html>