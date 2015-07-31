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
		document.forms[0].action = "./generaPdfPlanilla.html";
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
		<form action="buscaCorrespondenciasPlanillas.html" method="post">
			<input type="option" name="tipo" value="${tipo}"> 
			<table cellpadding='0' cellspacing='0' border='0' width='100%'>
				<tr>
					<TD width="1%">
						<td width='40%'>
							<div class='wcmPathLabel' nowrap>
								Planillas de comunicacion oficial ${tipoCorrespondencia}
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
											Recorrido

										</TD>
										<TD>
											<select name="recorrido" class="wcmFormInput">
												<option value="PRIMERO">
													PRIMERO
												</option>

												<option value="SEGUNDO">
													SEGUNDO
												</option>

												<option value="TERCERO">
													TERCERO
												</option>

												<option value="CUARTO">
													CUARTO
												</option>

												<option value="URGENTE">
													URGENTE
												</option>
											</select>


										</TD>
										<TD width="1%" />
											<TD class=wcmFormText align=right>
												Generar por
											</TD>
											<TD>
												<select name="rompimiento" class="wcmFormInput">
													<option value="0">
														PISO
													</option>

													<option value="4">
														DEPENDENCIA
													</option>
												</select>
											</TD>
											<TD width="1%" />
												<TD>
													Mensajero

												</TD>

												<td>
													<input type="text" id="mensajero" name="mensajero" value="">
												</td>
									</TR>
									<!--  	<TR>
										<TD class=wcmFormText align="center" colspan="4">
											<input type="submit" value="Buscar">
										</TD>
									</TR> -->

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
						requestURI="buscaCorrespondenciasPlanillas.html" id="document"
						export="false" pagesize="20" cellpadding="5" excludedParams="">
						<tagdisplay:column media='html' title="Remite">
							${document.Origen}
						</tagdisplay:column>
						<tagdisplay:column property="Destinatario"
							title="Destinatario" />
						<tagdisplay:column property="Radicado" title="Radicado" />
						<tagdisplay:column property="Anexos" title="Anexos" />


					</tagdisplay:table>
				</td>
			</tr>
		</table>


	</body>
</html>