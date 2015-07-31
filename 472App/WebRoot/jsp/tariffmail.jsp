<%@page contentType="text/html"%>
<%
	//@page pageEncoding="UTF-8"
%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://displaytag.sf.net" prefix="tagdisplay"%>


<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">

<html>
	<head>
		<title>Tarifas de Correo</title>
		<meta http-equiv="Content-Type" content="text/html">
		<!--; charset=UTF-8-->
		<meta http-equiv="pragma" content="no-cache">
		<meta http-equiv="cache-control" content="no-cache">
		<meta http-equiv="expires" content="0">
		<LINK href="${pageContext.request.contextPath}/css/Wcm.css"
			type=text/css rel=stylesheet>
		<LINK href="${pageContext.request.contextPath}/css/screen.css"
			type=text/css rel=stylesheet>
	</head>
	<script type="text/javascript" language="JavaScript"
		src='<html:rewrite page="/js/ScriptUtils.js"/>'></script>
	<script language="javascript">
	function go(id) {
		window.document.forms[0].id.value = id;
		window.document.forms[0].submit();
	}
</script>
	<body>
		<table width="700px" bgcolor="white">
			<tr>
				<td class=wcmFormText align=right>
					<div class=wcmFormText align=left>
						Administrar Tarifas de Correo
					</div>
					<br />
					<br />
					<br />
					<form action="./muestraTarifas.html">
						<input type="hidden" name="id" />

					</form>
					<tagdisplay:table export="true" name="${tariffs}" pagesize="20"
						id="tariffmail" requestURI="/listaTipoCorreo.html">
						<tagdisplay:column sortable="true" title="Tipo Mail">
							<a href="javascript:go('${tariffmail}');">${tariffmail}</a>
						</tagdisplay:column>

					</tagdisplay:table>


				</td>
			</tr>
		</table>
	</body>
</html>

