package com.grupointent.genericapp.servlets;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sourceforge.ajaxtags.servlets.BaseAjaxServlet;
import net.sourceforge.ajaxtags.xml.AjaxXmlBuilder;

import com.intent.minminas472.db.HibernateUtil;

public class DataBaseQuery extends BaseAjaxServlet {

	public String getXmlContent(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		AjaxXmlBuilder builder = new AjaxXmlBuilder();

		String tipo = request.getParameter("tipo");
		String[] args = request.getParameterValues("args");
		StringBuffer cad = new StringBuffer(
				"Select precio from Tbl_Tarifascorreos where TipoCorreo like '%Postexpres%' and 5>Peso and 5<PesoMaximo");

		if (tipo.equalsIgnoreCase("PesoValor")) {
			cad = new StringBuffer(
					"Select precio from Tbl_Tarifascorreos where TipoCorreo like '%"
							+ args[0] + "%' and " + args[1] + ">=Peso and "
							+ args[1] + "<=PesoMaximo");
		}
		if (tipo.equalsIgnoreCase("ciudad")) {
			cad = new StringBuffer(
					"Select top 7  ciudad,departamento,pais from Tbl_PaisDepartamentoCiudad where ciudad like '%"
							+ args[0] + "%' ");
		}
		if (tipo.equalsIgnoreCase("entidad")) {
			cad = new StringBuffer(
					"Select top 7 nombre from Tbl_instituciones where nombre like '%"
							+ args[0] + "%' and Estado like '%Activo%' ");
		}
		try {
			List ls = HibernateUtil.query(cad.toString());
			if (ls.size() == 0) {
				//builder.addItem("valor", "No data found");

			}

			for (Object object : ls) {
				if (tipo.equalsIgnoreCase("PesoValor")) {
					builder.addItem("valor", object.toString());
				} else if (tipo.equalsIgnoreCase("ciudad")) {
					Object[] p = (Object[]) object;
					builder.addItem(p[0].toString() + "_" + p[1].toString()
							+ "_" + p[2].toString(), p[0].toString() + "_"
							+ p[1].toString() + "_" + p[2].toString());
				} else if (tipo.equalsIgnoreCase("entidad")) {
					builder.addItem(object.toString(), object.toString());
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(builder.toString());
		return (builder.toString());
	}

}
