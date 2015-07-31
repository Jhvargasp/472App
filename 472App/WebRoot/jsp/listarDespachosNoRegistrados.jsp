<%@ page language="java"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="tagdisplay"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
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

	function generarPlanilla(rad) {
		document.forms[0].action="./marcarGenerarPlantilla.html";
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

	}
</script>

	</head>
	<body>
		<form action="buscaDespachosNoReg.html" method="post">
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
											Fecha

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
												Tipo Correo
											</TD>
											<TD>
												<select id="tipoCorreo" name="tipoCorreo">
													<c:forEach items="${TipoCorreos}" var="tipoCorreo1">
														<option value="${tipoCorreo1}">
															${tipoCorreo1}
														</option>
													</c:forEach>
												</select>
												<script type="text/javascript">
												var e=document.getElementById("tipoCorreo");
												for(var i=0;i<e.options.length;i++){
													if(e.optionsp[i].value=='${tipoCorreo}'){
														e.selectedIndex=i;
													}
												}
												</script>
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
					<c:if test="${fn:length(results)>0}">
						<input type="button" value="Generar Planilla"
							onclick=
	generarPlanilla();
>
						<br>
					</c:if>
					<tagdisplay:table sort="list" name="${results}"
						requestURI="buscaDespachosNoReg.html" id="document" export="false"
						pagesize="20" cellpadding="5" excludedParams="">
						<tagdisplay:column media='html' title="Radicado">
							${document.Radicado}
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