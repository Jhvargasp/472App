/**
 * 
 */
package com.intent.minminas472.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.filenet.api.collection.ContentElementList;
import com.filenet.api.collection.StringList;
import com.filenet.api.constants.AutoClassify;
import com.filenet.api.constants.CheckinType;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.ContentTransfer;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.Id;
import com.intent.minminas472.db.HibernateUtil;
import com.intent.minminas472.p8.P8DAO;
import com.intent.minminas472.p8.P8Template;
import com.intent.minminas472.pdf.PlanillaCorrespondenciaRecibida;
import com.intent.minminas472.session.IntentSession;
import com.intent.minminas472.utils.Constants;

/**
 * @author Jvargas
 * 
 */
@Controller
public class MinminasPlantillasController {

	private static Logger log = Logger
			.getLogger(MinminasPlantillasController.class);
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");

	private static Hashtable LABELS_PLANILLAS = new Hashtable();

	private final String CLASE_INTERNA = "ComunicacionInterna"; // ComunicacionMemorando

	static {
		LABELS_PLANILLAS.put(
				Constants.getProperty("CORRESPONDENCIA_TIPO_ENTRANTE"),
				Constants.getProperty("LABEL_PLANILLA_TIPO_ENTRANTE"));
		LABELS_PLANILLAS.put(
				Constants.getProperty("CORRESPONDENCIA_TIPO_INTERNA"),
				Constants.getProperty("LABEL_PLANILLA_TIPO_INTERNA"));
		LABELS_PLANILLAS.put(
				Constants.getProperty("CORRESPONDENCIA_TIPO_SALIENTE"),
				Constants.getProperty("LABEL_PLANILLA_TIPO_SALIENTE"));
	}

	public MinminasPlantillasController() {

	}

	private List buscarNoPlanillas(String tipo) {
		String campoDestinatario = "DependenciaDestino";// Destinatario
		if (tipo.equalsIgnoreCase(CLASE_INTERNA)) { //
			// campoDestinatario = "";//"Destino";
		}
		// saliente

		String sql = "Select  Id,  Origen, FechaRadicado,  "
				+ campoDestinatario
				+ ",  NombreCreador,  Radicado,Anexos "
				+ "  From "
				+ tipo
				+ " 		   Where VersionStatus=1 and CodigoPlanilla is NULL Order By  "
				+ campoDestinatario;
		log.debug(sql);

		List set = null;
		try {
			set = P8Template.getObjectsWithSQL(sql,
					IntentSession.vaidateConnection(null), 1000);
			set = validaCopias(tipo, set);

			if (tipo.equalsIgnoreCase(CLASE_INTERNA)) { // ComunicacionMemorando
				tipo = "ComunicacionSaliente"; // PersonaDestinatario
				sql = "Select  Id,  Origen, FechaRadicado,  PersonaDestino , EntidadDestinatario"
						+ ",  NombreCreador,  Radicado,Anexos "
						+ "  From "
						+ tipo
						+ " 		   Where VersionStatus=1 and CodigoPlanilla is NULL Order By Origen ";
				log.debug(sql);
				List set2 = P8Template.getObjectsWithSQL(sql,
						IntentSession.vaidateConnection(null), 1000);
				set2 = validaCopias(tipo, set2);
				set.addAll(set2);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return set;
	}

	@RequestMapping("/buscaCorrespondenciasPlanillas.html")
	public String buscarCorrespondenciaPlanilla(
			@RequestParam("tipo") String tipo, ModelMap model,
			HttpServletRequest request, HttpSession session) {
		// ComunicacionEntrante

		List set = buscarNoPlanillas(tipo);

		model.addAttribute("results", set);
		model.addAttribute("tipoCorrespondencia", LABELS_PLANILLAS.get(tipo));
		model.addAttribute("tipo", tipo);
		//System.out.println(set);
		return "listarCorreosNoPlanilla";
	}

	private List validaCopias(String tipo, List set) {
		ObjectStore store = IntentSession.vaidateConnection(null);
		List nSet = new ArrayList();
		// list hashmap
		if (tipo.equalsIgnoreCase(CLASE_INTERNA)) {
			for (Object object : set) {
				Hashtable<String, Object> map = (Hashtable) object;

				String des = map.get("DependenciaDestino").toString();
				map.put("DependenciaDestino", des);
				// nSet.add(map);

				Id id = (Id) map.get("Id");
				Document d = (Document) store.fetchObject("Document", id, null);
				// DependenciasDestinatarios NombresDestinatarios
				validateAddCopy(
						nSet,
						d.getProperties().getStringListValue(
								"ConCopiaExternaA"), map);//
				validateAddCopy(nSet,
						d.getProperties()
								.getStringListValue("ConCopiaInternaA"), map);// DependenciasConCopiaInterna
			}

		} else if (tipo.equalsIgnoreCase("ComunicacionSaliente")) {
			for (Object object : set) {
				Hashtable<String, Object> map = (Hashtable) object;
				String des = map.get("PersonaDestino").toString();// PersonaDestinatario
				if (des.length() >= 0) {
					des += " ";
				}
				des = map.get("EntidadDestinatario").toString();// InstitucionDestinatario
				map.put("DependenciaDestino", des);
				nSet.add(map);

				Id id = (Id) map.get("Id");
				Document d = (Document) store.fetchObject("Document", id, null);
				// DependenciasDestinatarios NombresDestinatarios
				validateAddCopy(nSet,
						d.getProperties()
								.getStringListValue("ConCopiaExternaA"), map);
				validateAddCopy(nSet,
						d.getProperties()
								.getStringListValue("ConCopiaInternaA"), map);// ConCopiaA
			}

		} else {
			nSet.addAll(set);
			for (Object object : set) {
				Hashtable<String, Object> map = (Hashtable) object;
				Id id = (Id) map.get("Id");
				Document d = (Document) store.fetchObject("Document", id, null);
				validateAddCopy(nSet,
						d.getProperties()
								.getStringListValue("ConCopiaInternaA"), map);// ConCopiaA
			}
		}

		return nSet;
	}

	/**
	 * 
	 * @param nSet
	 * @param list
	 */
	private void validateAddCopy(List nSet, List list, Hashtable map) {
		if (list != null && list.size() > 0) {
			for (Object object2 : list) {
				if (!object2.toString().equalsIgnoreCase("\"\"")) {
					Hashtable newMap = new Hashtable(map);
					newMap.put("Destinatario", object2);
					newMap.put("Radicado", "C " + map.get("Radicado"));
					nSet.add(newMap);
				}
			}
		}
	}

	@RequestMapping("/generaPdfPlanilla.html")
	public String generaPdf(@RequestParam("tipo") String tipo,
			@RequestParam("rompimiento") int rompimiento,
			@RequestParam("recorrido") String recorrido,
			@RequestParam("mensajero") String mensajero, ModelMap model,
			HttpServletRequest request, HttpServletResponse response,
			HttpSession session) {
		// ComunicacionEntrante
		String pisoDepStr = "SELECT DISTINCT Dependencia,Piso FROM Tbl_Dependencias";
		HashMap pisoDepMap = new HashMap();

		try {
			List ls = (HibernateUtil.query(pisoDepStr));
			for (Object object : ls) {
				Object rta[] = (Object[]) object;
				pisoDepMap.put(rta[0], rta[1]);
			}

		} catch (Exception e1) {
			e1.printStackTrace();
		}

		try {
			List set = buscarNoPlanillas(tipo);
			String colDes = "DependenciaDestino";// Destinatario
			if (tipo.equalsIgnoreCase(CLASE_INTERNA)) {
				tipo = "ComunicacionInterna";
				colDes = "Origen";

			}

			for (Object object : set) {
				String dest = ((Hashtable) object).get(colDes).toString();
				if (pisoDepMap.get(dest) != null
						&& pisoDepMap.get(dest).toString().length() > 0) {
					((Hashtable) object).put("Piso",
							"Piso " + pisoDepMap.get(dest));
				}
			}
			set = sortListMap(set, rompimiento);
			//System.out.println("PDF " + set);
			PlanillaCorrespondenciaRecibida planilla = new PlanillaCorrespondenciaRecibida();
			planilla.setCampoRompimiento(rompimiento);
			planilla.setCodigoFormato(Constants
					.getProperty("PLANILLA_CODIGO_FORMATO"));
			planilla.setVersionFormato(Constants
					.getProperty("PLANILLA_VERSION_FORMATO"));
			planilla.setFechaFormato(Constants
					.getProperty("PLANILLA_FECHA_FORMATO"));
			planilla.setFilas(set);
			// setting some response headers
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			// setting the content type
			response.setContentType("application/pdf");
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			String tituloPlanilla = (String) LABELS_PLANILLAS.get(tipo);
			if (tituloPlanilla != null) {
				planilla.setTitulo(tituloPlanilla);
			}
			planilla.setRecorrido(recorrido);
			planilla.setMensajero(mensajero);

			Object o = (HibernateUtil.query("SELECT top 1 planillas" + tipo
					+ " from tbl_autonumericos").get(0));
			int codigoPlanilla = (Integer) o;
			// int i = Integer.parseInt(codigoPlanilla);
			HibernateUtil.execute("update tbl_autonumericos set planillas"
					+ tipo + "=" + (codigoPlanilla + 1) + " where planillas"
					+ tipo + "=" + codigoPlanilla);

			planilla.setNroPlanilla(codigoPlanilla + "");
			// TODO 472 logo aca pasa interna y externa
			planilla.setUrlLogo(session.getServletContext().getRealPath(
					"/WEB-INF")
					+ "/" + Constants.getProperty("IMAGEN_LOGO"));
			planilla.generarPdf(baos);
			baos.flush();
			baos.close();
			response.getOutputStream().write(baos.toByteArray());
			// jvargas jun 29 2010 bug no salva planilla
			savePDF(IntentSession.vaidateConnection(null), baos.toByteArray(),
					set, codigoPlanilla + "", planilla);
			actualizarCorrespondencia(IntentSession.vaidateConnection(null),
					set, codigoPlanilla + "");
			response.getOutputStream().flush();
			response.getOutputStream().close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/listarCorreoPlanillas";
	}

	private void actualizarCorrespondencia(ObjectStore store, List dataList,
			String codigoPlanilla) {
		try {
			Iterator itera = dataList.iterator();
			Hashtable row = null;
			// Data data = null;
			String id = null;
			Document doc = null;
			// ObjectStore objectStore = ObjectFactory
			// .getObjectStore(
			// com.intent.correspondencia.logic.Constans.LIBRERIA_CORRESPONDENCIA,
			// session);
			// Properties properties = ObjectFactory.getProperties();
			// Property property = ObjectFactory
			// .getProperty(com.intent.correspondencia.logic.Constans.CODIGO_PLANILLA);
			// property.setValue(codigoPlanilla);
			// properties.add(property);
			while (itera.hasNext()) {
				row = (Hashtable) itera.next();
				// data = (Data) row.get(0);
				id = row.get("Id").toString();
				doc = Factory.Document.fetchInstance(store, id, null);
				// doc.setProperties(properties);
				doc.getProperties().putValue(
						Constants.getProperty("CODIGO_PLANILLA"),
						codigoPlanilla);
				doc.save(RefreshMode.REFRESH);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void savePDF(ObjectStore store, byte[] in, List dataList,
			String codigoPlanilla, PlanillaCorrespondenciaRecibida forma) {
		Iterator itera = dataList.iterator();
		StringBuffer buffer = new StringBuffer();
		Hashtable row = null;
		String data = null;
		String dependencia = null;
		while (itera.hasNext()) {
			row = (Hashtable) itera.next();
			// Iterator<String> it=row.keySet().iterator();
			String k0 = "Edificio";
			// int x=0;
			// while (it.hasNext()) {
			// String string = (String) it.next();
			// x++
			// }
			data = (String) row.get(k0);
			if (dependencia == null || !dependencia.equals(data)) {
				if (data != null) {
					dependencia = data;
					buffer.append(dependencia).append(" ");
				}
			}
		}
		// com.filenet.wcm.api.Properties properties = ObjectFactory
		// .getProperties();
		Hashtable properties = new Hashtable();
		// Property property =
		// ObjectFactory.getProperty(Property.DOCUMENT_TITLE);
		Date fechaDocumento = new Date();
		// property.setValue(codigoPlanilla);
		// properties.add(property);
		properties.put("DocumentTitle", codigoPlanilla);
		//
		// property = ObjectFactory
		// .getProperty(com.intent.correspondencia.logic.Constans.CODIGO_PLANILLA);
		// property.setValue(codigoPlanilla);
		// properties.add(property);
		properties
				.put(Constants.getProperty("CODIGO_PLANILLA"), codigoPlanilla);
		//
		// property = ObjectFactory
		// .getProperty(com.intent.correspondencia.logic.Constans.RECORRIDO_PLANILLA);
		// property.setValue(forma.getRecorrido());
		// properties.add(property);
		// properties.put(Constants.getProperty("RECORRIDO_PLANILLA"), forma
		// .getRecorrido());
		//
		// property = ObjectFactory
		// .getProperty(com.intent.correspondencia.logic.Constans.MENSAJERO_PLANILLA);
		// property.setValue(forma.getMensajero());
		// properties.add(property);
		// properties.put(Constants.getProperty("MENSAJERO_PLANILLA"), forma
		// .getMensajero());
		//
		// property = ObjectFactory
		// .getProperty(com.intent.correspondencia.logic.Constans.EDIFICIO_PLANILLA);
		// property.setValue(forma.getEdificio());
		// properties.add(property);
		// properties.put(Constants.getProperty("EDIFICIO_PLANILLA"), forma
		// .getEdificio());

		// property = ObjectFactory
		// .getProperty(com.intent.correspondencia.logic.Constans.CGC_PLANILLA);
		// property.setValue(forma.getCgc());
		// properties.add(property);
		// properties.put(Constants.getProperty("CGC_PLANILLA"),
		// forma.getCgc());

		//
		// property = ObjectFactory
		// .getProperty(com.intent.correspondencia.logic.Constans.CORRESPONDENCIA_FECHADOCUMENTO);
		// property.setValue(fechaDocumento);
		// properties.add(property);
		properties.put(Constants.getProperty("CORRESPONDENCIA_FECHADOCUMENTO"),
				fechaDocumento);

		//
		// property = ObjectFactory
		// .getProperty(com.intent.correspondencia.logic.Constans.DEPENDENCIAS_PLANILLA);
		// property.setValue(buffer.toString());
		// properties.add(property);
		// properties.put(Constants.getProperty("DEPENDENCIAS_PLANILLA"), buffer
		// .toString());

		// ObjectStore objectStore = ObjectFactory
		// .getObjectStore(
		// com.intent.correspondencia.logic.Constans.LIBRERIA_CORRESPONDENCIA,
		// session);
		//
		// Document document = (Document) objectStore.createObject(
		// com.intent.correspondencia.logic.Constans.CLASE_PLANILLA, //
		// ClassDescription.DOCUMENT
		// properties, null);

		Document document = P8DAO.a4XcreateDocument(properties,
				Constants.getProperty("CLASE_PLANILLA"),
				Constants.getProperty("FOLDER_PLANILLAS"), store);
		// Folder folder = (Folder)
		// objectStore.getObject(BaseObject.TYPE_FOLDER,
		// com.intent.correspondencia.logic.Constans.FOLDER_PLANILLAS);
		// document.file(folder, false);
		// TransportInputStream content = new TransportInputStream(
		// new ByteArrayInputStream(in));
		// document.setContent(content, false, false);
		// document.checkin(false);
		ContentElementList cl = Factory.ContentElement.createList();
		ContentTransfer ct = Factory.ContentTransfer.createInstance();
		ct.setCaptureSource(new ByteArrayInputStream(in));
		ct.set_ContentType("application/pdf");
		cl.add(ct);
		document.set_ContentElements(cl);
		// 24 aug jvargas, no autoclasify
		document.checkin(AutoClassify.DO_NOT_AUTO_CLASSIFY,
				CheckinType.MAJOR_VERSION);
		document.save(RefreshMode.REFRESH);
		// System.out.println("Documento creado. Id: " + document.getId());
	}

	
	public static List sortListMap(List<Hashtable<String, Object>> set, int rompimiento) {
	System.out.println("in:"+set.size());
		String colRompimientoStr = "DependenciaDestino";
		if (rompimiento == 0) {
			colRompimientoStr = "Piso";
		}
		TreeMap sortTable = new TreeMap();
		for (Object object : set) {
			sortTable.put(((Hashtable) object).get(colRompimientoStr) + ", "
							+ ((Hashtable) object).get("DependenciaDestino") + ", "
							+ ((Hashtable) object).get("Radicado")
					, object);
		}

		List s = new ArrayList();
		Iterator it = sortTable.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry entry = (Map.Entry) it.next();
			String key = (String) entry.getKey();
			s.add(entry.getValue());
			//System.out.println("Sorted Key ..." + key);
		}// while
		/*
		String[] columnNames = new String[] { "Origen",
				"Radicado", "Anexos", "Destinatario",
				"FechaRadicado", "" };

			valida= (i!=3)?valida:"DependenciaDestino";
		*/
		System.out.println("Out:"+s.size());
		return s;
	}// printMap

}
