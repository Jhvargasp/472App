/**
 * 
 */
package com.intent.minminas472.controller;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.intent.minminas472.db.HibernateUtil;

/**
 * @author Jvargas
 * 
 */
@Controller
public class TarifasController {

	private static Logger log = Logger.getLogger(TarifasController.class);
	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

	public TarifasController() {

	}

	@RequestMapping("/listaTipoCorreo.html")
	public String lista(ModelMap model, HttpServletRequest request,
			HttpSession session) {
		String sql = "Select tipocorreo from Tbl_tipocorreo";
		log.debug(sql);
		List set;
		try {

			model.addAttribute("tariffs", HibernateUtil.query(sql));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/tariffmail";
	}

	@RequestMapping("/muestraTarifas.html")
	public String tarifas(@RequestParam("id") String id, ModelMap model,
			HttpServletRequest request, HttpSession session) {
		String sql = "select  peso, pesoMaximo,precio,orden from tbl_TarifasCorreos where tipocorreo ='"
				+ id + "'";
		log.debug(sql);
		model.addAttribute("tipoCorreo", id);
		List set;
		try {
			model.addAttribute("results", HibernateUtil.query(sql));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/saveOrUpdateTariffMail";
	}

	@RequestMapping("/guardaTarifas.html")
	public String savetarifas(@RequestParam("tipoCorre") String tipoCorreo,
			ModelMap model, HttpServletRequest request, HttpSession session) {

		Enumeration enums = request.getParameterNames();
		List valuesAlready = new ArrayList();
		while (enums.hasMoreElements()) {

			String name = (String) enums.nextElement();
			String id = name;
			if (name.indexOf("*") > -1) {
				id = name.substring(0, name.indexOf("*"));
			}

			if (!valuesAlready.contains(id)
					&& request.getParameter(id + "*min") != null) {
				valuesAlready.add(id);
				String min = request.getParameter(id + "*min");

				String max = request.getParameter(id + "*max");

				String value = request.getParameter(id + "*value");
				String sql = "select  peso from tbl_TarifasCorreos where tipocorreo ='"
						+ tipoCorreo + "' and orden=" + id;

				try {
					if (HibernateUtil.query(sql).size() > 0) {
						sql = "update tbl_TarifasCorreos set peso=" + min
								+ ",pesoMaximo=" + max + ",precio=" + value
								+ " where tipocorreo ='" + tipoCorreo
								+ "' and orden=" + id;
						HibernateUtil.execute(sql);
					} else {
						sql = "insert tbl_TarifasCorreos(tipocorreo,orden,peso,pesoMaximo,precio) values  ('"
								+ tipoCorreo
								+ "',"
								+ id
								+ ","
								+ min
								+ ","
								+ max + "," + value + ")";
						HibernateUtil.execute(sql);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

			}
		}

		return lista(model, request, session);
	}

	@RequestMapping("/borraTarifa.html")
	public String deletetarifas(@RequestParam("id") String id,
			@RequestParam("idT") String tipo, ModelMap model,
			HttpServletRequest request, HttpSession session) {
		String sql = "delete from tbl_TarifasCorreos where tipocorreo ='"
				+ tipo + "' and orden =" + id;
		log.debug(sql);
		List set;
		try {
			model.addAttribute("results", HibernateUtil.execute(sql));
			return lista(model, request, session);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/saveOrUpdateTariffMail";
	}

}
