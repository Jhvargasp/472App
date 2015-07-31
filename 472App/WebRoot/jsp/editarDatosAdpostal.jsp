<%@ page language="java"%>
<%@ taglib uri="http://ajaxtags.sourceforge.net/tags/ajaxtags"
	prefix="ajax"%>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>


	<LINK href="../../css/Wcm.css" type=text/css rel=stylesheet>
	<script src='../../js/adpostal.js'></script>
	<script src='../../js/tabulation.js'></script>

	<!-- links for ajax -->

	<link type="text/css" rel="stylesheet"
		href="${pageContext.request.contextPath}/ajaxtags/css/ajaxtags.css"></link>
	<link type="text/css" rel="stylesheet"
		href="${pageContext.request.contextPath}/ajaxtags/css/displaytag.css"></link>

	<script type="text/javascript"
		src="${pageContext.request.contextPath}/ajaxtags/js/prototype.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/ajaxtags/js/scriptaculous/scriptaculous.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/ajaxtags/js/overlibmws/overlibmws.js"></script>
	<script type="text/javascript"
		src="${pageContext.request.contextPath}/ajaxtags/js/ajaxtags.js"></script>

	<script type="text/javascript">
	function listo() {
		alert("listo");
	}
	function armaCiudad() {
		if(document.getElementById("ciudad").value.length>0){
		args = document.getElementById("ciudad").value.split("_");
		document.getElementById("ciudad").value = args[0];
		document.getElementById("depto").value = args[1];
		document.getElementById("pais").value = args[2];
		}
	}
</script>
	<head>
		<title>Editar Datos 472</title>

	</head>
	<body>
		<span id="indicatorRegion" style="display: none;"> <img
				src="${contextPath}/images/busy-indicator.gif" /> Loading... </span>
		<TABLE cellSpacing=1 cellPadding=0 align=center border=0 width="80%">
			<TBODY>
				<TR>
					<table cellpadding='0' cellspacing='0' border='0' width='100%'>
						<tr>
							<BR>
							<td width="5%">
								<div class='wcmPathLabel' nowrap>
									472
								</div>
							</td>
						</tr>
					</table>
				</TR>
				<TR>
					<TD>
						${obj }
						<form action="guardarDespacho.html" method="post">
							<input type="hidden" name="id" value="${obj.Id}">
							<input type="hidden" name="radicado" value="${obj.Radicado}">
							<input type="hidden" name="fechaRadicado"
								value="${obj.FechaRadicado}">
							<TABLE class='wcmSearchFormBorder' cellSpacing=0 cellPadding=10
								align=center border=0>
								<TBODY>
									<TR>
										<TD vAlign=top align=right>
											<TABLE cellSpacing=0 cellPadding=2 border=0 valign="top">
												<TBODY>
													<TR>
														<TD class=wcmFormText align=right>
															Dependencia origen&nbsp;
														</TD>
														<TD>
															<input type="text" name="origen"
																value="${obj.InstitucionEmisorRemitente}"
																readonly="readonly">
														</TD>
													</TR>

													<TR>
														<TD class=wcmFormText align=right>
															TipoCorreo&nbsp;
														</TD>

														<TD>
															<select id="tipoCorreo" name="tipoCorreo">
																<c:forEach items="${TipoCorreos}" var="tipoCorreo">
																	<option value="${tipoCorreo}">
																		${tipoCorreo}
																	</option>
																</c:forEach>
															</select>
														</TD>
													</TR>
													<TR>
														<TD class=wcmFormText align=right>
															Entidad&nbsp;
														</TD>
														<TD>
															<input type="text" id="entidad" name="entidad"
																value="${obj.Destinatario}" size="70">
														</TD>
													</TR>
													<TR>
														<TD class=wcmFormText align=right>
															Ciudad&nbsp;
														</TD>
														<TD>
															<input type="text" name="ciudad" id="ciudad"
																value="${obj.Ciudad}" onblur="armaCiudad();" size="45">
														</TD>
													</TR>
													<TR>
														<TD class=wcmFormText align=right>
															Departamento&nbsp;
														</TD>
														<TD>
															<input type="text" name="depto" id="depto"
																value="${depto}">

														</TD>
													</TR>
													<TR>
														<TD class=wcmFormText align=right>
															Pais&nbsp;
														</TD>
														<TD>
															<input type="text" name="pais" id="pais" value="${pais}">

														</TD>
													</TR>
													<TR>
														<TD class=wcmFormText align=right>
															Direccion&nbsp;
														</TD>
														<TD>
															<textarea type="text" name="direccion">${obj.Direccion}</textarea>

														</TD>
													</TR>
													<TR>
														<TD class=wcmFormText align=right>
															Peso&nbsp;
														</TD>
														<TD>
															<input type="text" name="peso" id="peso"
																value="${obj.Peso}"
																onblur="document.getElementById('action1').click();">

														</TD>
													</TR>
													<TR>
														<TD class=wcmFormText align=right>
															Valor&nbsp;
														</TD>
														<TD>
															<input type="text" id="valor" name="valor"
																value="${obj.Precio}" class="form-autocomplete">
															<input name="action1" id="action1" type="button"
																value="Go" />
														</TD>
													</TR>


													<TR>
														<TD colspan="4" class=wcmFormText align=center>
															<BR>
															<A class=wcmLink id="actualizar"
																href="javascript:document.forms[0].submit();">Guardar&nbsp;</A>
															<A class=wcmLink id="cerrar" href="javascript:cerrar();">Cerrar&nbsp;</A>
														</TD>
													</TR>
												</TBODY>
											</TABLE>
										</TD>
									</TR>
								</TBODY>
							</TABLE>
						</form>
						<ajax:autocomplete source="ciudad" target="ciudad"
							baseUrl="${pageContext.request.contextPath}/servlet/DataBaseQuery"
							className="autocomplete" minimumCharacters="2"
							parameters="tipo=ciudad,args={ciudad}"
							indicator="indicatorRegion" />

						<ajax:autocomplete source="entidad" target="entidad"
							baseUrl="${pageContext.request.contextPath}/servlet/DataBaseQuery"
							className="autocomplete" minimumCharacters="2"
							parameters="tipo=entidad,args={entidad}"
							indicator="indicatorRegion" />


						<ajax:updateField
							baseUrl="${pageContext.request.contextPath}/servlet/DataBaseQuery"
							source="peso" target="valor"
							parameters="tipo=Pesovalor,args={tipoCorreo},args={peso}"
							action="action1" valueUpdateByName="true" />

						<ajax:select source="state" target="city"
							baseUrl="${contextPath}/servlet/DataBaseQuery"
							parameters="state={state}" />
						<script type="text/javascript">
	armaCiudad();
</script>
	</body>
</html>
