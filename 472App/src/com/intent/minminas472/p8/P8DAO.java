package com.intent.minminas472.p8;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.filenet.api.collection.VersionableSet;
import com.filenet.api.constants.AutoUniqueName;
import com.filenet.api.constants.DefineSecurityParentage;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Document;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.core.ReferentialContainmentRelationship;
import com.filenet.api.property.FilterElement;
import com.filenet.api.property.PropertyFilter;
import com.intent.minminas472.utils.Constants;

/**
 * | Clase para la implementación de métodos para acceso a datos del sistema de
 * orrespondencia, aquí se encuentran las operaciones para la carga de datos,
 * los procesos para ingresar, obtener y modificar correspondencias, las
 * operaciones para trabajo con correos electrónicos, entre otras.
 * 
 * 
 * @author Carlos Montoya : carlos.montoya@mvm.com.co
 * @version 1.0
 * @see ######################################################### #modifica:
 *      #motivo: #fragmento: #fecha: Jul 25, 2005
 *      #########################################################
 */
public class P8DAO {

	private static Log log = LogFactory.getLog(P8DAO.class);

	private static final int MAX_RESULTS = 100;

	public static final String TYPE_FOLDER = "Folder";

	public static final String TYPE_DOCUMENT = "Document";

	private static final String DOCUMENT_TITLE = "DocumentTitle";

	public static final String TYPE_CUSTOMOBJECT = "CustomObject";

	public static final String ID = "Id";

	public static String getRotuloDespacho(ObjectStore store, String sNumRad)
			throws Exception { // , int cantidadRotulosDestino

		Hashtable t = a4XgetProperties(store.getObject("CorreoDespachado",
				sNumRad), new String[] { "Id", "InstitucionEmisorRemitente",
				"Radicado", "FechaRadicado", "Destinatario", "Direccion",
				"Peso", "Precio", "Ciudad" });
		// Id,Peso,Precio,Radicado,FechaRadicado,InstitucionEmisorRemitente,Destinatario,Estado,Ciudad,TipoDeCorreo
		String unRotuloDestino = "<guia alto='50' ancho='150'>\n"
				+ "<frase x='3' y='10'>"

				+ new SimpleDateFormat("dd/MM/yyyy").format(((Date) t
						.get("FechaRadicado")))
				+ "</frase>\n"
				+ "<frase x='25' y='10'></frase>\n"
				+ "<frase x='3' y='16'>"
				+ t.get("InstitucionEmisorRemitente")
				+ "</frase>\n"
				+ "<frase x='130' y='16'></frase>\n"
				+ "<frase x='3' y='24'>MINISTERIO DE MINAS Y ENERGIA</frase>\n"
				+ "<frase x='108' y='24'>BOGOTA DC</frase>\n"
				+ "<frase x='3' y='29'>"
				+ t.get("Destinatario")
				+ "</frase>\n"
				+ "<frase x='3' y='37'>"
				+ t.get("Direccion")
				+ "</frase>\n"
				+ "<frase x='110' y='37'>"
				+ t.get("Ciudad")
				+ "</frase>\n"
				+ "<frase x='110' y='42'>"
				+ t.get("Peso")
				+ "</frase>\n"
				+ "<frase x='132' y='42'>"
				+ t.get("Precio")
				+ "</frase>\n"
				+ "</guia>\n";
		return unRotuloDestino;
	}// getRotuloCorrespondencia

	private static String getRotulosCopias(String aRemite, String sNumRad,
			String sFecharad, String sDescAnexos, String[] rCopias) {
		StringBuffer sb = new StringBuffer();
		String sDato = null;
		int i = 0;
		while (i < rCopias.length) {
			sDato = rCopias[i];

			sb
					.append("<pagina>\n")
					.append("<linea alineacion='izquierda'>\n")
					.append("<frase>\n")
					.append(Constants.getProperty("application_rotulo_empresa"))
					.append("</frase>\n").append("</linea>\n").append(
							"<linea alineacion='izquierda'>\n").append(
							"<frase>\n").append("Origen: " + aRemite).append(
							"</frase>\n").append("</linea>\n").append(
							"<linea alineacion='izquierda'>\n").append(
							"<frase>\n").append(
							"Rad: " + sNumRad + " " + sFecharad).append(
							"</frase>\n").append("</linea>\n").append(
							"<linea alineacion='izquierda'>\n").append(
							"<frase>\n").append("Anexos: " + sDescAnexos) // iNumAnexos
					.append("</frase>\n").append("</linea>\n").append(
							"<linea alineacion='izquierda'>\n").append(
							"<frase>\n").append("Destino: " + sDato).append(
							"</frase>\n").append("</linea>\n").append(
							"</pagina>\n");

			i++;
		}// while
		return sb.toString();
	}

	public static Hashtable a4XgetProperties(IndependentObject document,
			String[] props) {
		PropertyFilter filter = new PropertyFilter();

		String cad = "";
		for (int i = 0; i < props.length; i++) {
			String string = props[i];
			cad += string + " ";

		}
		filter.addIncludeProperty(new FilterElement(null, null, Boolean.FALSE,
				cad));
		filter.setMaxRecursion(0);
		document.fetchProperties(props);
		// Iterator it=document.getProperties().iterator();
		Hashtable hashtable = new Hashtable();

		for (int i = 0; i < props.length; i++) {
			String string = props[i];
			// Property property = (Property) it.next();
			if (document.getProperties().getObjectValue(string) != null) {
				hashtable.put(string, document.getProperties().getObjectValue(
						string));
			}
			// hashtable.put(string,
			// document.getProperties().getObjectValue(string)==null?"":document.getProperties().getObjectValue(string));
		}
		return hashtable;

	}

	/**
	 * CALL SAVE METHOD
	 * 
	 * @param object
	 * @param properties
	 * @return
	 */
	public static IndependentObject a4XsetProperties(IndependentObject object,
			Hashtable properties) {
		// IntentUtilities.set4XTheProperties(properties, hData, p8ObjectStore,
		// p8FolderAtachments, className)
		if (properties != null) {
			Iterator it = properties.keySet().iterator();

			while (it.hasNext()) {
				String string = (String) it.next();
				log.debug(string + ":" + properties.get(string));
				{
//					if (object.getProperties().isPropertyPresent(string))
						object.getProperties().putObjectValue(string,
								properties.get(string));
				}
			}
		}
		return object;
	}

	public static Document a4XcreateDocument(Hashtable properties,
			String className, String folderDestination, ObjectStore store) {
		Document doc = Factory.Document.createInstance(store, className);
		Folder folder = Factory.Folder.fetchInstance(store, folderDestination,
				null);
		doc = (Document) a4XsetProperties(doc, properties);
		doc.save(RefreshMode.NO_REFRESH);
		ReferentialContainmentRelationship rfc = folder.file(doc,
				AutoUniqueName.AUTO_UNIQUE, null,
				DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
		rfc.save(RefreshMode.NO_REFRESH);

		return doc;
	}

	public static Folder a4XcreateFolder(Hashtable properties,
			String className, String folderDestination, ObjectStore store) {
		Folder doc = Factory.Folder.createInstance(store, className);

		Folder folder = Factory.Folder.fetchInstance(store, folderDestination,
				null);
		doc.set_Parent(folder);
		doc = (Folder) a4XsetProperties(doc, properties);
		folder.file(doc, AutoUniqueName.AUTO_UNIQUE, null,
				DefineSecurityParentage.DEFINE_SECURITY_PARENTAGE);
		doc.save(RefreshMode.REFRESH);
		return doc;
	}

	public static CustomObject a4XcreateCustomObject(Hashtable properties,
			String className, String folderDestination, ObjectStore store) {
		CustomObject obj = Factory.CustomObject
				.createInstance(store, className);
		Folder folder = Factory.Folder.fetchInstance(store, folderDestination,
				null);
		obj = (CustomObject) a4XsetProperties(obj, properties);
		obj.save(RefreshMode.REFRESH);
		// obj.fetchProperties(new String[]{PropertyNames.NAME});
		ReferentialContainmentRelationship r = folder.file(obj,
				AutoUniqueName.AUTO_UNIQUE, null,
				DefineSecurityParentage.DO_NOT_DEFINE_SECURITY_PARENTAGE);
		r.save(RefreshMode.REFRESH);
		return obj;
	}

	private static List a4XSortByDateCreated(VersionableSet versiones) {
		List fechas = new ArrayList();
		List documentosOrdenados = new ArrayList();
		Iterator it = versiones.iterator();
		while (it.hasNext()) {
			Document document = (Document) it.next();
			fechas.add(document.get_DateCreated());
		}
		Collections.sort(fechas);

		for (int i = 0; i < fechas.size(); i++) {
			Date date = (Date) fechas.get(i);
			it = versiones.iterator();
			while (it.hasNext()) {
				Document document = (Document) it.next();
				if (date.equals(document.get_DateCreated())) {
					documentosOrdenados.add(document);
					break;
				}
			}
		}
		return documentosOrdenados;
	}

}// P8DAO
