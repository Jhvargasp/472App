package com.intent.minminas472.p8;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.jdom.JDOMException;

import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Document;
import com.filenet.api.core.ObjectStore;
import com.intent.minminas472.pdf.Data;

/**
 * Clase de negocio que manipula las planillas
 * 
 * @author hospina
 */
public class PlanillasManager {

	/**
	 * The logger for this class
	 */
	private static Logger log = Logger.getLogger(PlanillasManager.class);

	/**
	 * Lista las comunicaciones que tienen pendiente la generación de planilla.
	 */
	public static List planillaAdpostal(ObjectStore store, String fecha,
			String tipoCorreo) {
		List results = null;

		if (log.isDebugEnabled()) {
			StringBuffer buffer = new StringBuffer("Buscando Correspondencia:");
			// buffer.append(sess.toString());
			log.debug(buffer.toString());
		}// if
		StringBuffer where = new StringBuffer();
		if (fecha != null && !fecha.trim().equals("")) {
			String tmp[] = fecha.split("-");
			fecha = tmp[2] + "-" + tmp[1] + "-" + tmp[0];
			where.append(" AND a.fechaDespacho > " + fecha
					+ " AND a.fechaDespacho < " + fecha + "T23:59:59 \n");
		}
		if (tipoCorreo != null && !tipoCorreo.trim().equals("")) {
			where.append(" AND a.TipoCorreo = " + tipoCorreo + " \n");
		}
		// Set up the XML in a string variable

		// com.filenet.wcm.api.Search oSearch = ObjectFactory.getSearch(sess);
		// String resultString = oSearch.executeXML(searchStatement);

		try {
			// System.out.println("[PlanillasManager.planillaAdpostal]
			// resultString: " + resultString);
			// results = SearchUtils.searchAsCollection(resultString, true);
			results = P8Template
					.getObjectsWithSQL(
							"SELECT a.Id ID,  a.numRad, c.LabelDependencia as Destino, b.labelCiudad, e.LabelDepartamento, a.peso, a.precio \n"
									+ "FROM ( ( ( CorreoDespachado a LEFT JOIN Ciudad b on a.ciudad = b.Id ) \n"
									+ "LEFT JOIN Dependencia c on a.entidadDestino = c.ID ) \n"
									+ "LEFT JOIN TipoCorreo d on a.TipoCorreo = d.ID ) \n"
									+ "LEFT JOIN Departamento e on b.Departamento = e.ID \n"
									+ "WHERE a.estado <> 'SIN REGISTRAR' AND a.CodigoPlanilla IS NULL "
									+
									// "AND \n"
									// + "a.TipoCorreo &lt;&gt; "
									// +
									// Constans.CORREODESPACHADO_TIPOCORREO_POST_EXPRESS_NACIONAL
									// + " AND \n" + "a.TipoCorreo &lt;&gt; "
									// +
									// Constans.CORREODESPACHADO_TIPOCORREO_POST_EXPRESS_LOCAL
									" \n"
									+ where.toString()
									+ "ORDER BY a.numRad \n", store, 9999);
			if (log.isDebugEnabled()) {
				StringBuffer buffer = new StringBuffer(results.toString());
				log.debug(buffer.toString());
			}// if
		} catch (JDOMException jdomEx) {
			log.error("Problems Parsing Result", jdomEx);
		} catch (IOException ioEx) {
			log.error("Problems handling IO Buffer", ioEx);
		}// try-catch
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}// planillaAdpostal

	public static void actualizaPlanillaEnComunicacion(String id,
			ObjectStore store,
			// String storeName,
			boolean value) {
		// ObjectStore objStore = ObjectFactory.getObjectStore(storeName, sess);
		if (log.isDebugEnabled()) {
			StringBuffer buffer = new StringBuffer("Documento: ");
			buffer.append("ID=");
			buffer.append(id);
			log.debug(buffer.toString());
		}// if
		Document doc = (Document) store.getObject(P8DAO.TYPE_DOCUMENT, id);
		if (log.isDebugEnabled()) {
			StringBuffer buffer = new StringBuffer("Documento: ");
			buffer.append("ID=");
			buffer.append(id);
			buffer.append(doc);
			log.debug(buffer.toString());
		}// if
		// Properties props = ObjectFactory.getProperties();
		// Property prop = ObjectFactory
		// .getProperty(Constans.PROPIEDAD_PLANILLA_GENERADA);
		// prop.setValue(value);
		// props.add(prop);
		// doc.setProperties(props);
		doc.getProperties().putValue("Planilla", value);
		doc.save(RefreshMode.REFRESH);
	}// actualizaPlanillaEnComunicacion

	public static void actualizarPlanillaEnCorreosDespachados(String id,
			ObjectStore store, String codigoPlanillaDespachada) {
		// ObjectStore objStore = ObjectFactory.getObjectStore(storeName, sess);
		if (log.isDebugEnabled()) {
			StringBuffer buffer = new StringBuffer("CustomObject: ");
			buffer.append("ID=");
			buffer.append(id);
			log.debug(buffer.toString());
		}// if
		CustomObject obj = (CustomObject) store.getObject(
				P8DAO.TYPE_CUSTOMOBJECT, id);
		if (log.isDebugEnabled()) {
			StringBuffer buffer = new StringBuffer("CustomObject: ");
			buffer.append("ID=");
			buffer.append(id);
			buffer.append(obj);
			log.debug(buffer.toString());
		}// if
		// Properties props = ObjectFactory.getProperties();
		// Property prop = ObjectFactory.getProperty(Constans.CODIGO_PLANILLA);
		// prop.setValue(codigoPlanillaDespachada);
		// props.add(prop);
		// obj.setProperties(props);
		obj.getProperties().putValue("CodigoPlanilla", codigoPlanillaDespachada);
		obj.save(RefreshMode.REFRESH);
	}// actualizarPlanillaEnCorreosDespachados

	public static void marcarPlanillas(List ls, ObjectStore store) {
		Iterator rows = ls.iterator();
		while (rows.hasNext()) {
			// Sacamos el id de la comunicación
			Hashtable data = (Hashtable) rows.next();
			String id = data.get("ID").toString();
			actualizaPlanillaEnComunicacion(id, store, true);
		}// while

	}// marcarPlanilla

	public static void marcarPlanillasDespachadas(List ls,
			String codigoPlanillaDespachada, ObjectStore store) {
		Iterator rows = ls.iterator();
		Data data = null;
		String id = null;
		while (rows.hasNext()) {
			Hashtable t = (Hashtable) rows.next();
			// Sacamos el id de la comunicación

			id = t.get("Id").toString();
			System.out
					.println("[PlanillasManager.marcarPlanillasDespachadas] id: "
							+ id);
			actualizarPlanillaEnCorreosDespachados(id, store,
					codigoPlanillaDespachada);
		}// while

	}// marcarPlanilla

}// class

class Comparador implements java.util.Comparator {
	private int columnIndex;

	public Comparador(int indiceColumna) {
		columnIndex = indiceColumna;
	}

	public int compare(Object obj1, Object obj2) {
		if (obj1 != null && obj1 instanceof List && obj2 != null
				&& obj2 instanceof List) {
			try {

				Data tmp1 = (Data) ((List) obj1).get(columnIndex);
				Data tmp2 = (Data) ((List) obj2).get(columnIndex);
				return tmp2.getValue().toString().compareTo(
						tmp1.getValue().toString());
			} catch (Exception e) {
			}
		}
		return 0;
	}
}

class ComparadorReales implements java.util.Comparator {
	private int columnIndex;

	private boolean ascendente;

	public ComparadorReales(int indiceColumna, boolean ascendente) {
		columnIndex = indiceColumna;
		this.ascendente = ascendente;
	}

	public int compare(Object obj1, Object obj2) {
		if (obj1 != null && obj1 instanceof List && obj2 != null
				&& obj2 instanceof List) {
			try {
				Data tmp1 = (Data) ((List) obj1).get(columnIndex);
				Data tmp2 = (Data) ((List) obj2).get(columnIndex);
				Float valor1 = Float.valueOf(tmp1.getValue().toString());
				Float valor2 = Float.valueOf(tmp2.getValue().toString());
				int retorno = valor1.compareTo(valor2);
				if (!ascendente && retorno != 0) {
					if (retorno > 0) {
						retorno = -1;
					} else {
						retorno = 1;
					}
				}
				return retorno;
			} catch (Exception e) {
			}
		}
		return 0;
	}
}