<%@ page language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="tagdisplay"%>

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
	function send(rad) {
		document.getElementById("numRad").value = rad;
		document.forms[0].submit();
	}

	function calendario() {
		Calendar.setup( {
			inputField : "fechaInicio", // id of the input field
			ifFormat : "%Y-%m-%d", // format of the input field
			button : "calInicio", // trigger for the calendar (button ID)
			align : "B1", // alignment (defaults to "Bl")
			singleClick : true
		});
		Calendar.setup( {
			inputField : "fechaFinal", // id of the input field
			ifFormat : "%Y-%m-%d", // format of the input field
			button : "calFinal", // trigger for the calendar (button ID)
			align : "B1", // alignment (defaults to "Bl")
			singleClick : true
		});

	}
</script>

	</head>
	<body>
		<form action="getDespachoXRadicado.html" method="post">
			<input id="numRad" name="numRad">
		</form>
		<form action="buscaDespachos.html" method="post">
			<table cellpadding='0' cellspacing='0' border='0' width='100%'>
				<tr>
					<TD width="1%">
						<td width='40%'>
							<div class='wcmPathLabel' nowrap>
								472
							</div>
						</td>
				</tr>
			</table>

			<TABLE align="center">
				<TBODY>
					<TR>
						<TD vAlign=top>
							<IMG height=1
								src="${pageContext.request.contextPath}/images/Spacer.gif"
								width=35 />
						</TD>
						<TD vAlign=top align=right>
							<BR>
							<TABLE>
								<TBODY>
									<TR>
										<TD class=wcmFormText align=right>
											Rango Fecha de inicio radicacion

										</TD>
										<TD>
											<input class="wcmFormInput" maxlength="10" size="15"
												type="text" name="fechaInicio" id="fechaInicio"
												value="${fechaInicio}">
											<img style="cursor: pointer;"
												src='${pageContext.request.contextPath}/images/cal.gif'
												id="calInicio" width="16" height="16" border="0"
												alt="Seleccionar Fecha">

										</TD>
										<TD width="1%" />
											<TD class=wcmFormText align=right>
												Numero radicacion
											</TD>
											<TD>
												<input class="wcmFormInput" maxlength="12" size="15"
													type="text" name="idRadica" id="idRadica"
													value="${idRadica}">
											</TD>
									</TR>
									<TR>
										<TD class=wcmFormText align=right>
											Rango Fecha final radicacion

										</TD>
										<TD class="wcmFormInput">
											<input class="wcmFormInput" maxlength="10" size="15"
												type="text" name="fechaFinal" id="fechaFinal"
												value="${fechaFinal}">
											<img style="cursor: pointer;"
												src='${pageContext.request.contextPath}/images/cal.gif'
												id="calFinal" width="16" height="16" border="0"
												alt="Seleccionar Fecha">

										</TD>
										<TD width="1%" />
											<TD class=wcmFormText align=right>
												Estado
											</TD>
											<TD>
												<input class="wcmFormInput" maxlength="12" size="15"
													type="text" name="idEstado" id="idEstado"
													value="${idEstado}">
											</TD>
									</TR>
									<TR>
										<TD>
											<input type="hidden" id="chooseTipo" value="">
										</TD>
										<TD class=wcmFormText align="center" colspan="4">
											<input type="submit" value="Buscar">
										</TD>
									</TR>

								</TBODY>
							</TABLE>
						</TD>
					</TR>
				</TBODY>
			</TABLE>
		</form>
		<table width="80%" align="center">
			<tr>
				<td>
					<tagdisplay:table sort="list" name="${results}"
						requestURI="buscaDespachos.html" id="document" export="false"
						pagesize="20" cellpadding="5" excludedParams="">
						<tagdisplay:column media='html' title="Radicado">
							<a href="javascript:send('${document.Radicado}');">${document.Radicado}</a>
						</tagdisplay:column>
						<tagdisplay:column property="InstitucionEmisorRemitente"
							title="Origen" />
						<tagdisplay:column property="Destinatario" title="Destino" />
						<tagdisplay:column property="Estado" title="Estado" />


					</tagdisplay:table>
				</td>
			</tr>
		</table>

		<script type="text/javascript">
	calendario();
</script>
	</body>
</html>