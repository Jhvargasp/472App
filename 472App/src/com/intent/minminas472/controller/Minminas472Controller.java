/**
 * 
 */
package com.intent.minminas472.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.intent.minminas472.db.HibernateUtil;
import com.intent.minminas472.p8.P8DAO;
import com.intent.minminas472.p8.P8Template;
import com.intent.minminas472.p8.PlanillasManager;
import com.intent.minminas472.pdf.GuiaPDF;
import com.intent.minminas472.pdf.PlanillaCorrespondenciaDespachada;
import com.intent.minminas472.pdf.RotuloPDF;
import com.intent.minminas472.session.IntentSession;
import com.intent.minminas472.utils.Constants;

/**
 * @author Jvargas
 * 
 */
@Controller
public class Minminas472Controller {

	private static Logger log = Logger.getLogger(Minminas472Controller.class);
	SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
	SimpleDateFormat dfFull = new SimpleDateFormat("dd-MM-yyyy hh:mm aa");

	public Minminas472Controller() {

	}

	@RequestMapping("/buscaDespachos.html")
	public String buscarDespachos(@RequestParam("fechaInicio") String d1,
			@RequestParam("fechaFinal") String d2,
			@RequestParam("idRadica") String numRad,
			@RequestParam("idEstado") String estado, ModelMap model,
			HttpServletRequest request, HttpSession session) {
		model.addAttribute("fechaInicio", d1);
		model.addAttribute("fechaFinal", d2);
		model.addAttribute("idRadica", numRad);
		model.addAttribute("idEstado", estado);

		String sql = "Select Radicado,FechaRadicado,InstitucionEmisorRemitente,Destinatario,Estado from CorreoDespachado where ";
		if (d1 != null) {
			sql += "FechaRadicado >= " + d1 + "T00:00:00 AND ";
		}
		if (d2 != null) {
			sql += "FechaRadicado <= " + d2 + "T23:59:59 AND ";
		}
		if (numRad.length() > 0) {
			sql += "Radicado LIKE '%" + numRad + "%' AND ";
		}
		if (estado.length() > 0) {
			sql += "Estado LIKE '%" + estado + "%' AND ";
		}

		sql = sql.substring(0, sql.length() - 4);
		log.debug(sql);
		List set;
		try {
			set = P8Template.getObjectsWithSQL(sql, IntentSession
					.vaidateConnection(null), 1000);
			model.addAttribute("results", set);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/listarCorreoAdpostal";
	}

	@RequestMapping("/buscaDespachosNoReg.html")
	public String buscarDespachosNoReg(@RequestParam("fechaInicio") String d1,
			@RequestParam("tipoCorreo") String d2, ModelMap model,
			HttpServletRequest request, HttpSession session) {
		model.addAttribute("fechaInicio", d1);
		model.addAttribute("tipoCorreo", d2);
		if (d1.length() > 0 && d2.length() > 0) {
			String sql = "Select Id,Peso,Precio,Radicado,FechaRadicado,InstitucionEmisorRemitente,Destinatario,Estado,Ciudad,TipoDeCorreo,Direccion from CorreoDespachado where ";
			sql += "FechaDespacho >= " + d1 + "T00:00:00 AND ";
			sql += "FechaDespacho <= " + d1 + "T23:59:59 AND ";
			sql += "Estado = 'Registrado' AND CodigoPlanilla IS NULL AND "
					+ "TipoDeCorreo ='" + d2 + "' ";
			// sql = sql.substring(0, sql.length() - 4);
			log.debug(sql);
			List set;
			try {
				set = P8Template.getObjectsWithSQL(sql, IntentSession
						.vaidateConnection(null), 1000);
				model.addAttribute("results", set);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		try {
			model.addAttribute("TipoCorreos", HibernateUtil
					.query("SELECT TIPOCORREO FROM TBL_TIPOCORREO"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "/listarDespachosNoRegistrados";
	}

	@RequestMapping("/getDespachoXRadicado.html")
	public String listarDespachosXRadicado(
			@RequestParam("numRad") String numRad, ModelMap model,
			HttpServletRequest request, HttpSession session) {

		ObjectStore store = IntentSession.vaidateConnection(null);

		String sql = "Select Id from Comunicaciones where ";
		sql += "Radicado LIKE '%" + numRad + "%' AND VersionStatus =1 ";
		List set;
		try {
			set = P8Template.getObjectsWithSQL(sql, store, 1000);

			Document d = (Document) store.fetchObject("Document",
					((Hashtable) set.get(0)).get("Id").toString(), null);
			model.addAttribute("idCorrespondencia", d.get_Id().toString());
			model.addAttribute("numRad", numRad);
			model.addAttribute("dependencia", d.getProperties().getStringValue(
					"DependenciaRemitente"));

			sql = "Select Id,Peso,Precio,Radicado,FechaRadicado,InstitucionEmisorRemitente,Destinatario,Estado,Ciudad,TipoDeCorreo from CorreoDespachado where ";
			sql += "Radicado LIKE '%" + numRad + "%' ";

			set = P8Template.getObjectsWithSQL(sql, store, 1000);
			model.addAttribute("results", set);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/editarListarCorreo";
	}

	@RequestMapping("/editarDespacho.html")
	public String editarDespacho(@RequestParam("idDespacho") String idDespacho,
			@RequestParam("idCorrespondencia") String idcorrespondencia,
			ModelMap model, HttpServletRequest request, HttpSession session) {
		Hashtable map = new Hashtable();
		if (idDespacho.length() > 0) {
			map = P8DAO.a4XgetProperties(IntentSession.vaidateConnection(null)
					.getObject("CustomObject", idDespacho), new String[] {
					"Id", "InstitucionEmisorRemitente", "Radicado",
					"FechaRadicado", "Destinatario", "Direccion", "Peso",
					"Precio", "Ciudad" });
			Date d = (Date) map.get("FechaRadicado");
			map.put("FechaRadicado", dfFull.format(d));
		} else {
			map = P8DAO.a4XgetProperties(IntentSession.vaidateConnection(null)
					.getObject("Document", idcorrespondencia), new String[] {
					"Radicado", "FechaRadicado", "Destino",
					"DependenciaRemitente" });// "InstitucionEmisorRemitente","Destinatario"
			Date d = (Date) map.get("FechaRadicado");
			map.put("FechaRadicado", dfFull.format(d));
			//map.put("FechaRadicado", df.format(d));
			map.put("Destinatario", map.get("Destino"));
			map.put("InstitucionEmisorRemitente", map
					.get("DependenciaRemitente"));
		}
		if (!map.containsKey("Precio")) {
			map.put("Precio", "0");
		}
		if (map.containsKey("Ciudad")) {
			try {
				map
						.put(
								"Ciudad",
								HibernateUtil
										.query(
												"Select ciudad+'_'+departamento+'_'+pais from Tbl_PaisDepartamentoCiudad where ciudad like '%"
														+ map.get("Ciudad")
														+ "%' ").get(0));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		try {
			model.addAttribute("TipoCorreos", HibernateUtil
					.query("SELECT TIPOCORREO FROM TBL_TIPOCORREO"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		model.addAttribute("obj", map);

		return "/editarDatosAdpostal";
	}

	@RequestMapping("/guardarDespacho.html")
	public String guardarDespacho(@RequestParam("id") String idDespacho,
			@RequestParam("origen") String origen,
			@RequestParam("radicado") String radicado,
			@RequestParam("fechaRadicado") String fechaRadicado,
			@RequestParam("tipoCorreo") String tipoCorreo,
			@RequestParam("entidad") String entidad,
			@RequestParam("ciudad") String ciudad,
			@RequestParam("direccion") String direcion,
			@RequestParam("peso") float peso,
			@RequestParam("valor") float valor, ModelMap model,
			HttpServletRequest request, HttpSession session) {
		Hashtable map = new Hashtable();
		map.put("InstitucionEmisorRemitente", origen);
		map.put("TipoDeCorreo", tipoCorreo);
		map.put("Radicado", radicado);
		try {
			map.put("FechaRadicado", dfFull.parse(fechaRadicado));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		map.put("Destinatario", entidad);
		map.put("Peso", new Double(peso));
		map.put("Precio", new Double(valor));
		map.put("Direccion", direcion);
		map.put("Ciudad", ciudad);
		map.put("Estado", "Registrado");
		map.put("FechaDespacho", new Date());

		ObjectStore store = IntentSession.vaidateConnection(null);

		if (idDespacho.length() == 0) {
			CustomObject obj = (CustomObject) P8DAO.a4XcreateCustomObject(map,
					"CorreoDespachado", Constants
							.getProperty("FOLDER_CORREOS_DESPACHADOS"), store);
			obj.save(RefreshMode.REFRESH);
		}
		if (idDespacho.length() > 0) {
			CustomObject obj = (CustomObject) P8DAO.a4XsetProperties(store
					.getObject("CustomObject", idDespacho), map);
			obj.save(RefreshMode.REFRESH);
		}

		return listarDespachosXRadicado(radicado, model, request, session);
		// return "/editarListarCorreo";
	}

	@RequestMapping("/imprimirDespacho.html")
	public String imprimirDespacho(
			@RequestParam("idDespacho") String idDespacho, ModelMap model,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		try {
			String vertical = request.getParameter("vertical");
			RotuloPDF rotuloPdf = new RotuloPDF();
			if (vertical != null) {
				rotuloPdf.setVertical(true);
			}
			String xmlRotulo = P8DAO.getRotuloDespacho((IntentSession
					.vaidateConnection(null)), idDespacho);
			GuiaPDF x = new GuiaPDF();

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			x.generarGuia(xmlRotulo, baos);

			// setting some response headers
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			// setting the content type
			response.setContentType("application/pdf");
			// the contentlength is needed for MSIE!!!
			response.setContentLength(baos.size());
			// write ByteArrayOutputStream to the ServletOutputStream
			ServletOutputStream out = response.getOutputStream();
			baos.writeTo(out);
			out.flush();
			return null;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "/editarListarCorreo";
	}

	@RequestMapping("/marcarGenerarPlantilla.html")
	public String generarPlanilla(@RequestParam("fechaInicio") String d1,
			@RequestParam("tipoCorreo") String d2, ModelMap model,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		// obtiene listado
		response.setHeader("Expires", "0");
		response.setHeader("Cache-Control",
				"must-revalidate, post-check=0, pre-check=0");
		response.setHeader("Pragma", "public");
		// setting the content type
		response.setContentType("application/pdf");

		ObjectStore store = (IntentSession.vaidateConnection(null));
		List set = new ArrayList();

		if (d1.length() > 0 && d2.length() > 0) {
			String sql = "Select Id,Peso,Precio,Radicado,FechaRadicado,InstitucionEmisorRemitente,Destinatario,Estado,Ciudad,TipoDeCorreo,Direccion from CorreoDespachado where ";
			sql += "FechaDespacho >= " + d1 + "T00:00:00 AND ";
			sql += "FechaDespacho <= " + d1 + "T23:59:59 AND ";
			sql += "Estado = 'Registrado' AND CodigoPlanilla IS NULL AND "
					+ "TipoDeCorreo ='" + d2 + "' ";
			// sql = sql.substring(0, sql.length() - 4);

			log.debug(sql);
			try {
				set = P8Template.getObjectsWithSQL(sql, store, 1000);
				model.addAttribute("results", set);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		// genera pdf y guarda

		try {
			PlanillaCorrespondenciaDespachada planilla = new PlanillaCorrespondenciaDespachada();
			planilla.setFilas(set);

			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			planilla.setTitulo("Planilla Despachada");

			planilla.setTipoCorreo(d2);

			Object o = (HibernateUtil
					.query("SELECT top 1 plantillas472 from tbl_autonumericos")
					.get(0));
			int codigoPlanilla = (Integer) o;
			// int i = Integer.parseInt(codigoPlanilla);
			HibernateUtil.execute("update tbl_autonumericos set plantillas472="
					+ (codigoPlanilla + 1) + " where plantillas472="
					+ codigoPlanilla);
			planilla.setNroPlanilla(codigoPlanilla + "");
			planilla.setNroContrato(Constants
					.getProperty("POST_EXPRESS_PLANILLA_NUMERO_CONTRATO"));
			planilla.setCiudadSede(Constants.getProperty("CIUDAD_SEDE"));
			// TODO 472 logo planillas 472
			planilla.setUrlFooter(request.getSession().getServletContext()
					.getRealPath("/WEB-INF/PlanillaPostExpressFooter472.GIF"));
			planilla.setUrlLogo(request.getSession().getServletContext()
					.getRealPath("/WEB-INF/Logo472.GIF"));
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
			planilla.setFechaFormato(format.format(new Date()));
			planilla.setUsuario("ceadmin");

			planilla.generarPdf(baos);
			baos.flush();
			baos.close();
			response.getOutputStream().write(baos.toByteArray());
			savePDFDespachadas(store, baos.toByteArray(), set, codigoPlanilla
					+ "", d2);
			System.out.println("PDF " + codigoPlanilla + " Guardado!");
			PlanillasManager.marcarPlanillasDespachadas(set, codigoPlanilla
					+ "", store);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// muestra pdf

		return "/listaDespachos";
	}

	private void savePDFDespachadas(ObjectStore store, byte[] in,
			List dataList, String codigoPlanilla, String tipo) {
		Hashtable properties = new Hashtable();
		Date fechaDocumento = new Date();
		properties.put("DocumentTitle", codigoPlanilla);
		properties.put("CodigoPlanilla", codigoPlanilla);
		properties.put("TipoDeCorreo", tipo);
		properties.put("FechaDocumento", fechaDocumento);
		Document document = P8DAO.a4XcreateDocument(properties, Constants
				.getProperty("CLASE_PLANILLA_DESPACHADA"), Constants
				.getProperty("FOLDER_PLANILLAS_DESPACHADAS"), store);
		ContentElementList cl = Factory.ContentElement.createList();
		ContentTransfer ct = Factory.ContentTransfer.createInstance();
		ct.setCaptureSource(new ByteArrayInputStream(in));
		ct.set_ContentType("application/pdf");
		cl.add(ct);
		document.set_ContentElements(cl);
		document.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY,
				CheckinType.MAJOR_VERSION);
		document.save(RefreshMode.REFRESH);
	}

}
