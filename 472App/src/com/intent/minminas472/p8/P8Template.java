/*
 * Copyright (c) 2005 Grupo INTeNT de Colombia
 * www.intentgroup.com.co
 * All rights reserved.
 */
package com.intent.minminas472.p8;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import org.apache.log4j.Logger;

import com.filenet.api.collection.IndependentObjectSet;
import com.filenet.api.collection.PropertyDescriptionList;
import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.meta.PropertyDescription;
import com.filenet.api.query.SearchSQL;
import com.filenet.api.query.SearchScope;
import com.intent.minminas472.session.IntentSession;


/**
 * @author hospina
 * 
 */
public class P8Template {

	private static Logger log = Logger.getLogger(P8Template.class);

	private ObjectStore store;

	private Properties props;

	public void setSession(ObjectStore store) {

		this.store = store;
	}// setSession

	public void saveObject(Object object) {

	}// saveObject

	public static void deleteFolder(Folder folder) {

		Iterator it = folder.get_SubFolders().iterator();
		while (it.hasNext()) {
			Folder obj = (Folder) it.next();
			// if (obj.getObjectType() == BaseObject.TYPE_FOLDER) {
			Folder tmp = (Folder) obj;
			deleteFolder(tmp);
			// }// if
		}// while

		folder.delete();
		folder.save(RefreshMode.REFRESH);
	}// delete

	/**
	 * funcion borrar objeto para borrar custom objects Added by Jorge's
	 */
	public static void deleteObject(CustomObject obj) {
		obj.delete();
	}// delete

	/*
	 * public List getObjectList(Class objectClass) throws Exception { List
	 * properties = methodNameAsPropName(objectClass); //
	 * log.debug("Properties size: " + properties.size()+ " of // class " +
	 * objectClass.getName());
	 * 
	 * int type = Integer.parseInt(props.getProperty(objectClass.getName() +
	 * ".p8"));
	 * 
	 * //ObjectStore store = ObjectFactory.getObjectStore( //
	 * Constans.LIBRERIA_CORRESPONDENCIA, sess);
	 * 
	 * Iterator results = null;//
	 * getObjects(this.props.getProperty(objectClass.getName())).iterator();
	 * List ls = new LinkedList();
	 * 
	 * while (results.hasNext()) {
	 * 
	 * Iterator it = ((List) results.next()).iterator();
	 * 
	 * if (it.hasNext()) { Data data = (Data) it.next(); ReadableMetadataObject
	 * obj = (ReadableMetadataObject) store .getObject(type,
	 * data.getValue().toString());
	 * 
	 * Iterator propertiesIterator = properties.iterator(); Object instance =
	 * objectClass.newInstance(); while (propertiesIterator.hasNext()) { String
	 * prop = propertiesIterator.next().toString(); // log.debug(prop + "=" +
	 * objectClass.getName() + // "."+ prop); Object value =
	 * obj.getPropertyStringValue(props .getProperty(objectClass.getName() + "."
	 * + prop));
	 * 
	 * Method[] methods = objectClass.getMethods(); Object[] args = { value };
	 * for (int i = 0; i < methods.length; i++) { String buffer =
	 * methods[i].getName();
	 * 
	 * String lol = "set" + Character.toUpperCase(prop.charAt(0)) +
	 * prop.substring(1);
	 * 
	 * if (buffer.startsWith(lol)) { // log.debug("Calling: " + //
	 * methods[i].getName()+ " with " + args[0]); //
	 * log.debug(methods[i].getParameterTypes()[0].getName());
	 * methods[i].invoke(instance, args); i = methods.length; }// if-inner
	 * 
	 * }// for
	 * 
	 * }// while-innner ls.add(instance); }// if }// while
	 * 
	 * return ls; } // getObjectList
	 */

	public static List getObjects(String className, ObjectStore sstore,
			String orderBy, String st[]) throws Exception {
		// id del centro de radicación

		// log.debug(store);
		// Set up the XML in a string variable
		StringBuffer xml = new StringBuffer("SELECT c.id ");
		if (st == null) {
			xml.append(", c." + orderBy);
		} else {
			for (int i = 0; i < st.length; i++) {
				if (i == 0) {
					xml.append(", c.");
				}// if
				xml.append(st[i]);
				if ((i + 1) < st.length) {
					xml.append(", c.");
				}// if
			}// for
		}//
		xml.append(" FROM  ");
		xml.append(className);
		xml.append(" c ");
		xml.append(" ORDER BY c.");
		xml.append(orderBy);
		xml.append("  ASC  ");
		// log.debug(xml.toString());
		// Execute the search
		/*
		 * com.filenet.wcm.api.Search oSearch = ObjectFactory.getSearch(sess);
		 * String resultString = oSearch.executeXML(xml.toString()); //
		 * log.debug(resultString);
		 * 
		 * try { results = SearchUtils.searchAsCollection(resultString, false);
		 * 
		 * } catch (JDOMException jdomEx) {
		 * 
		 * } catch (IOException ioEx) { }// try-catch
		 * 
		 * return results;
		 */
		return getObjectsWithSQL(xml.toString(), sstore, 1000);
	}// getObjects

	/**
	 * Metodo que retorna lista de objetos añadiendo sentencia where Added by
	 * Jorge´s
	 * 
	 * @throws Exception
	 */
	public static List getObjects(String className, ObjectStore sstore,
			String orderBy, String st[], String where) throws Exception {
		// id del centro de radicación
		StringBuffer xml = new StringBuffer("");
		xml.append("SELECT c.id ");
		if (st == null) {
			xml.append(", c." + orderBy);
		} else {
			for (int i = 0; i < st.length; i++) {
				if (i == 0) {
					xml.append(", c.");
				}// if
				xml.append(st[i]);
				if ((i + 1) < st.length) {
					xml.append(", c.");
				}// if
			}// for
		}//
		xml.append(" FROM  ");
		xml.append(className);
		xml.append(" c ");

		where = arreglaCadena(where);

		xml.append(" " + where + " ");

		xml.append(" ORDER BY c.");
		xml.append(orderBy);
		xml.append("  ASC  ");
		// xml.append("</querystatement>" + " <options maxrecords=\"99999\"/>"
		// + "</request>");
		log.debug(xml.toString());
		// Execute the search
		return getObjectsWithSQL(xml.toString(), sstore, 500);
		/*
		 * com.filenet.wcm.api.Search oSearch = ObjectFactory.getSearch(sess);
		 * String resultString = null; try {
		 * 
		 * resultString = oSearch.executeXML(xml.toString()); //
		 * log.debug(resultString); } catch (Exception Ex) {
		 * log.debug(Ex.getMessage()); return null;
		 * 
		 * } try { results = SearchUtils.searchAsCollection(resultString,
		 * false);
		 * 
		 * } catch (JDOMException jdomEx) { log.debug(jdomEx.getMessage());
		 * 
		 * } catch (IOException ioEx) { log.debug(ioEx.getMessage()); }//
		 * try-catch
		 * 
		 * return results;
		 */
	}// getObjects

	public static List getObjectsWithSQL(String sql, ObjectStore sstore,
			int maxRecords) throws Exception {

		// Execute the search
		/*
		 * com.filenet.wcm.api.Search oSearch = ObjectFactory.getSearch(sess);
		 * String resultString = null; try {
		 * 
		 * resultString = oSearch.executeXML(xml.toString()); //
		 * log.debug(resultString); } catch (Exception Ex) {
		 * log.debug(Ex.getMessage()); return null;
		 * 
		 * } try { results = SearchUtils.searchAsCollection(resultString,
		 * false);
		 * 
		 * } catch (JDOMException jdomEx) { log.debug(jdomEx.getMessage());
		 * 
		 * } catch (IOException ioEx) { log.debug(ioEx.getMessage()); }//
		 * try-catch
		 * 
		 * return results;
		 */
		sstore = IntentSession.vaidateConnection(sstore);
		int index = 6;
		if (sql.contains("SELECT DISTINCT"))
			sql = "SELECT DISTINCT TOP " + maxRecords + sql.substring(15);
		else {
			sql = "SELECT TOP " + maxRecords + sql.substring(6);
		}

		log.debug(sql);
		SearchSQL searchSQL = new SearchSQL();
		searchSQL.setMaxRecords(maxRecords);
		searchSQL.setQueryString(sql);

		log.debug("MaxRecords:" + maxRecords);
		SearchScope scope = new SearchScope(sstore);

		log.debug("Antes consulta:" + new Date());
		RepositoryRowSet set = scope.fetchRows(searchSQL, null, null,
				new Boolean(true));
		log.debug("IsEmpty:? " + set.isEmpty());
		List ht = new ArrayList();
		if (!set.isEmpty()) {
			log.debug("Despues consulta:" + new Date());
			ht = SearchUtils.searchAsCollection(set, false);
			log.debug("Ordenacion:" + new Date());
		}
		return ht;
	}// getObjects

	public static IndependentObjectSet getCEObjects(String sql,
			ObjectStore sstore) {

		// Execute the search
		/*
		 * com.filenet.wcm.api.Search oSearch = ObjectFactory.getSearch(sess);
		 * BaseObjects result = null; try { if (className.equals("Document"))
		 * result = oSearch.singleObjectTypeExecute(xml.toString(),
		 * BaseObject.TYPE_DOCUMENT); else result =
		 * oSearch.singleObjectTypeExecute(xml.toString(),
		 * BaseObject.TYPE_CUSTOMOBJECT); // log.debug(resultString); } catch
		 * (Exception Ex) { log.debug(Ex.getMessage()); return null;
		 * 
		 * }
		 */
		sstore = IntentSession.vaidateConnection(sstore);
		SearchSQL searchSQL = new SearchSQL(sql);
		SearchScope scope = new SearchScope(sstore);
		IndependentObjectSet set = scope.fetchObjects(searchSQL, null, null,
				new Boolean(true));

		return set;
	}// getObjects

	/*
	 * Hace un parse sobre las cadenas para evitar los errores en el xml
	 */
	private static String arreglaCadena(String cad) {
		cad = cad.replaceAll("&", "&amp;");
		cad = cad.replaceAll("<", "&lt;");
		cad = cad.replaceAll(">", "&gt;");
		return cad;
	}

	public List methodNameAsPropName(Class object) {
		// log.debug("Called with " + object);
		Method[] methods = object.getMethods();
		List properties = new LinkedList();
		for (int i = 0; i < methods.length; i++) {

			if (methods[i].getName().startsWith("set")) {
				String methodName = methods[i].getName().substring(3);
				char[] tmp = methodName.toCharArray();
				tmp[0] = Character.toLowerCase(tmp[0]);
				methodName = new String(tmp);

				properties.add(methodName);
			}// if

		}// for

		return properties;
	}// methodNameAsPropNames

	public Properties getProperties() throws Exception {

		InputStream in = new FileInputStream(new File(".").getCanonicalPath()
				+ "\\src\\mapping.properties");
		Properties p = new Properties();
		p.load(in);

		return p;

	}// getPropertiesForClass

	public void setProps(Properties props) {
		this.props = props;

	}// setProperties

	public static boolean isSystemGeneratedProperty(ObjectStore objectStore,
			String clase, String propertyName) {
		log.debug("saw:" + propertyName);
		ClassDescription cdesc = Factory.ClassDescription.fetchInstance(
				objectStore, clase, null);
		PropertyDescriptionList list = cdesc.get_PropertyDescriptions();
		for (int i = 0; i < list.size(); i++) {
			PropertyDescription pdesc = (PropertyDescription) list.get(i);
			if (pdesc.get_Name().equalsIgnoreCase(propertyName)
					&& (pdesc.get_IsSystemGenerated().booleanValue() || pdesc
							.get_IsReadOnly().booleanValue())) {
				return true;
			} else if (pdesc.get_Name().equalsIgnoreCase(propertyName)
					&& (!pdesc.get_IsSystemGenerated().booleanValue() && !pdesc
							.get_IsReadOnly().booleanValue())) {
				return false;

			}

		}
		return true;

	}

}// P8Temlate
