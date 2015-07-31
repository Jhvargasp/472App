/**
 * 
 */
package com.intent.minminas472.p8;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;

import com.filenet.api.collection.ClassDescriptionSet;
import com.filenet.api.collection.PropertyDescriptionList;
import com.filenet.api.constants.RefreshMode;
import com.filenet.api.core.CustomObject;
import com.filenet.api.core.Factory;
import com.filenet.api.core.Folder;
import com.filenet.api.core.IndependentObject;
import com.filenet.api.core.IndependentlyPersistableObject;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.meta.ClassDescription;
import com.filenet.api.meta.PropertyDescription;

/**
 * @author msuarez
 * 
 */
public class P8Utils {
	private static Logger log = Logger.getLogger(P8Utils.class);

	private static Hashtable tipos = new Hashtable();

	static {
		tipos.put("TYPE_BOOLEAN", "Boolean");
		tipos.put("TYPE_DATE", "Date");
		tipos.put("TYPE_DOUBLE", "Double");
		tipos.put("TYPE_INTEGER", "Integer");
		tipos.put("TYPE_OBJECT", "Object");
		tipos.put("TYPE_STRING", "String");

		// Especiales
		tipos.put("TYPE_BINARY", "BINARY");
		tipos.put("TYPE_GUID", "String");
		tipos.put("TYPE_ID", "ID");
		tipos.put("TYPE_UNSPECIFIED", "UNSPECIFIED");
	}

	/*
	 * public static Session getSession(String user, String password) { Session
	 * session = null; try { session = ObjectFactory.getSession("default",
	 * Session.DEFAULT, user, password); File file = new
	 * File("WcmApiConfig.properties"); //
	 * print("Ruta Absoluta del archivo a buscar: " + //
	 * file.getAbsolutePath()); session.setConfiguration(new
	 * FileInputStream(file)); session.verify(); } catch (Exception e) {
	 * e.printStackTrace(); } return session; }
	 */

	/*
	 * public static Collection explorar(String repository, String path,
	 * ObjectStore store) throws Exception {
	 * 
	 * if (path == null || path.trim().equals("")) { path = "/"; } ObjectStore
	 * objectStore = store; Folder folder = (Folder)
	 * objectStore.getObject(P8DAO.TYPE_FOLDER, path); BaseObjects objetos =
	 * folder.getContainees(); return objetos; }
	 */

	/*
	 * public static Collection explorar(String application, String user, String
	 * password, String repository, String path, Session session) throws
	 * BadReferenceException, Exception { return explorar(repository, path,
	 * getSession(user, password)); }
	 */

	/*
	 * public static Object getProperty(Session session, String repository,
	 * String pathObject, String property) throws PropertyNotFoundException {
	 * Object retorno = null; ObjectStore objectStore =
	 * ObjectFactory.getObjectStore(repository, session); CustomObject
	 * customObject = (CustomObject) objectStore.getObject( BaseObject.TYPE_ANY,
	 * pathObject); print("Object Type: " +
	 * Name.objectTypeAsName(customObject.getObjectType()));
	 * print("Value of property " + property + ": " +
	 * customObject.getPropertyStringValue(property)); BaseObject baseObject =
	 * (BaseObject) customObject .getPropertyValue(property); print("Nombre: " +
	 * baseObject.getName()); return retorno; }
	 */

	public static ClassDescriptionSet getClassDescriptions(ObjectStore store) {
		// ObjectStore objectStore = ObjectFactory.getObjectStore(repository,
		// // session);
		// Factory.ClassDescription
		// int[] tipos = new int[] { ClassDescription.TYPE_CUSTOMOBJECT,
		// ClassDescription.TYPE_DOCUMENT, ClassDescription.TYPE_FOLDER };
		return store.get_ClassDescriptions();// .getClassDescriptions(tipos);
		// return objectStore.getClassDescriptions();
	}

	/*
	 * public static String executeQuery(Session session, String repository,
	 * String queryStatement) { return executeQuery(session, repository,
	 * queryStatement, 0); }
	 */

	public static List executeQuery(ObjectStore store, String repository,
			String queryStatement, int maxRegistros) throws Exception {
		// ObjectStore objectStore = ObjectFactory.getObjectStore(repository,
		// session);

		// String repositoryId = ObjectFactory.getObjectStore(repository,
		// session)
		// .getId();
		// String adhocSearchXML = "<request>"
		// + "  <objectstores mergeoption=\"union\">\n"
		// + "    <objectstore id=\"" + repositoryId + "\" name=\""
		// + repository + "\"/>\n" + "  </objectstores>\n"
		// + "  <querystatement>\n" + queryStatement
		// + "  </querystatement>\n";
		// if (maxRegistros > 0) {
		// adhocSearchXML += "  <options maxrecords=\"" + maxRegistros
		// + "\"/>\n";
		// }
		// adhocSearchXML += "</request>";
		// Search searchObject = ObjectFactory.getSearch(session);
		return P8Template.getObjectsWithSQL(queryStatement, store, 999);
		// return searchObject.executeXML(adhocSearchXML);
	}

	/*
	 * public static String executeQuery(String user, String password, String
	 * repository, String queryStatement) { return executeQuery(getSession(user,
	 * password), repository, queryStatement); }
	 * 
	 * public static ClassDescription getClassDescriptionById(Session session,
	 * String repository, String classId) { return getClassDescription(session,
	 * repository, Property.ID, classId); }
	 */
	public static ClassDescription getClassDescriptionByName(ObjectStore store,
			String className) {
		return getClassDescription(store, true, className);
	}

	public static ClassDescription getClassDescriptionById(ObjectStore store,
			ClassDescriptionSet classDescriptions, String classId) {
		return getClassDescription(store, classDescriptions, false, classId);
	}

	public static ClassDescription getClassDescriptionByName(ObjectStore store,
			ClassDescriptionSet classDescriptions, String className) {
		return getClassDescription(store, classDescriptions, true, className);
	}

	private static ClassDescription getClassDescription(ObjectStore store,
			boolean isName, String propertyValue) {
		/*
		 * ClassDescription retorno = null; try { ClassDescriptions
		 * classDescriptions = (ClassDescriptions)getClassDescriptions(session,
		 * repository).filterByProperty(propertyFilter,
		 * ReadableMetadataObjects.IS_EQUAL, propertyValue);
		 * //log.debug("propertyValue: " + propertyValue + ", classDescriptions:
		 * " + classDescriptions); Iterator itera =
		 * classDescriptions.iterator(); if (itera.hasNext()) { retorno =
		 * (ClassDescription)itera.next(); }
		 * //printPropertyDescriptions(retorno); } catch (Exception e) {
		 * e.printStackTrace(); } return retorno;
		 */
		ClassDescriptionSet classDescriptions = null;
		try {
			classDescriptions = (ClassDescriptionSet) getClassDescriptions(store);
			Iterator it = classDescriptions.iterator();
			while (it.hasNext()) {
				ClassDescription cd = (ClassDescription) it.next();
				if (cd.get_Name().equals(propertyValue))
					return cd;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		// return getClassDescription(store, classDescriptions,
		// propertyFilter, propertyValue);
		return null;
	}

	private static ClassDescription getClassDescription(ObjectStore store,
			ClassDescriptionSet classDescriptions, boolean isName,
			String propertyValue) {
		ClassDescription retorno = null;

		try {
			//			
			retorno = Factory.ClassDescription.fetchInstance(store,
					propertyValue, null);
			// classDescriptions = (ClassDescriptionSet) classDescriptions
			// .filterByProperty(propertyFilter,
			// ReadableMetadataObjects.IS_EQUAL, propertyValue);
			// // log.debug("propertyValue: " + propertyValue + ",
			// classDescriptions: " + classDescriptions);
			// Iterator itera = classDescriptions.iterator();
			// while (itera.hasNext()) {
			// retorno = (ClassDescription) itera.next();
			// //log.debug("Clase: "+retorno.get_Name());
			// if(isName&&retorno.get_Name().equalsIgnoreCase(propertyValue)){
			// return retorno;
			// }
			// if(!isName&&retorno.get_Id().toString().equals(propertyValue)){
			// return retorno;
			// }
			// }
			// printPropertyDescriptions(retorno);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return retorno;
	}

	/*
	 * public static PropertyDescription getPropertyDescriptionById(
	 * ClassDescription classDescription, String propertyId) { return
	 * getPropertyDescription(classDescription, Property.ID, propertyId); }
	 * 
	 * public static PropertyDescription getPropertyDescriptionByName(
	 * ClassDescription classDescription, String propertyName) { return
	 * getPropertyDescription(classDescription, Property.SYMBOLIC_NAME,
	 * propertyName); }
	 * 
	 * public static PropertyDescription getPropertyDescriptionById(
	 * PropertyDescriptions propertyDescriptions, String propertyId) { return
	 * getPropertyDescription(propertyDescriptions, Property.ID, propertyId); }
	 */
	public static PropertyDescription getPropertyDescriptionByName(
			PropertyDescriptionList propertyDescriptions, String propertyName) {
		// return getPropertyDescription(propertyDescriptions,
		// Property.SYMBOLIC_NAME, propertyName);
		Iterator it = propertyDescriptions.iterator();
		while (it.hasNext()) {
			PropertyDescription propertyDescription = (PropertyDescription) it
					.next();
			if (propertyDescription.get_SymbolicName().equals(propertyName))
				return propertyDescription;

		}
		return null;
	}

	private static Object internalGet(IndependentObject ceObject, Object bean) {
		try {
			Method[] metodos = bean.getClass().getMethods();
			Method act = null;
			String nombreMetodo = null;
			int tam = metodos.length;
			for (int i = 0; i < tam; i++) {
				act = metodos[i];
				nombreMetodo = act.getName();
				if (nombreMetodo.startsWith("set")
						&& act.getParameterTypes() != null
						&& act.getParameterTypes().length == 1) {

					Class claseParam = act.getParameterTypes()[0];
					if (!claseParam.equals(Collection.class)
							&& nombreMetodo.length() > "set".length()) {
						String propiedad = nombreMetodo.substring("set"
								.length());
						propiedad = propiedad.toLowerCase().charAt(0)
								+ propiedad.substring(1);
						if (claseParam.equals(Boolean.class)) {
							act.invoke(bean,
									new Object[] { (ceObject.getProperties()
											.getBooleanValue(propiedad)) });
						} else if (claseParam.equals(Date.class)) {
							act.invoke(bean, new Object[] { ceObject
									.getProperties()
									.getDateTimeValue(propiedad) });
						} else if (claseParam.equals(Double.class)) {
							act.invoke(bean,
									new Object[] { (ceObject.getProperties()
											.getFloat64Value(propiedad)) });
						} else if (claseParam.equals(Integer.class)) {
							act.invoke(bean, new Object[] { (ceObject
									.getProperties()
									.getInteger32Value(propiedad)) });
						} else if (claseParam.equals(String.class)) {
							try {
								log.debug("Get Propiedad:" + propiedad);
								act.invoke(bean, new Object[] { ceObject
										.getProperties().getStringValue(
												propiedad) });
							} catch (Exception e) {
								try {
									log.debug("NO STRING Propiedad:"
											+ propiedad);
									act.invoke(bean, new Object[] { ceObject
											.getProperties().getIdValue(
													propiedad).toString() });
								} catch (Exception x) {
									System.out
											.println("Error: on " + propiedad);
								}
							}
						} else { // if (claseParam.equals(Object.class)) {
							log.debug("[P8Utils.internalGet] Propiedad "
									+ propiedad + " Tipo Objeto");
							IndependentObject ceObjectProperty = (IndependentObject) ceObject
									.getProperties().getObjectValue(propiedad);
							String id = ceObjectProperty.getProperties()
									.getIdValue("Id").toString();
							log.debug("[P8Utils.internalGet] " + propiedad
									+ ".ID : " + id);
							// TODO ???
							// Object javaProperty = get(
							// store, claseParam, id);
							// System.out
							// .println("[P8Utils.internalGet] javaProperty: "
							// + javaProperty);
							// act.invoke(bean, new Object[] { javaProperty });
						} // if - else - if
					} // if
				} // if
			} // for
		} catch (Exception e) {
			bean = null;
			e.printStackTrace();
		}// try - catch
		return bean;
	}

	private static Hashtable internalSet(ObjectStore store, Object bean) {
		try {
			Method[] metodos = bean.getClass().getMethods();
			Method act = null;
			String nombreMetodo = null;
			Object dato = null;
			// Properties properties = ObjectFactory.getProperties();
			Hashtable properties = new Hashtable();
			// Property property = null;
			Class returnClass = null;
			int tam = metodos.length;
			for (int i = 0; i < tam; i++) {
				act = metodos[i];
				nombreMetodo = act.getName();
				returnClass = act.getReturnType();
				if (!nombreMetodo.equals("getClass")
						&& !nombreMetodo.equals("getId")
						&& nombreMetodo.startsWith("get")
						&& (act.getParameterTypes() == null || act
								.getParameterTypes().length == 0)
						&& !returnClass.equals(void.class)
						&& !returnClass.equals(Collection.class)) {

					if (nombreMetodo.length() > "get".length()) {
						String propiedad = nombreMetodo.substring("get"
								.length());
						propiedad = propiedad.toLowerCase().charAt(0)
								+ propiedad.substring(1);
						dato = act.invoke(bean, null);
						// property = ObjectFactory.getProperty(propiedad);
						if (returnClass.equals(Boolean.class)) {
							// property.setValue((Boolean) dato);
							properties.put(propiedad, dato);
						} else if (returnClass.equals(Date.class)) {
							// property.setValue((Date) dato);
							properties.put(propiedad, dato);
						} else if (returnClass.equals(Double.class)) {
							// property.setValue((Double) dato);
							properties.put(propiedad, dato);
						} else if (returnClass.equals(Integer.class)) {
							// property.setValue((Integer) dato);
							properties.put(propiedad, dato);
						} else if (returnClass.equals(String.class)) {
							// property.setValue((String) dato)
							properties.put(propiedad, dato);
							;
						} else { // if (returnClass.equals(Object.class)) {
							// // ObjectStore objectStore = ObjectFactory
							// // .getObjectStore(repository, session);
							// if (!ponerCEObject(objectStore, property,
							// BaseObject.TYPE_CUSTOMOBJECT, getId(dato))) { //
							// Si
							// // no
							// // es
							// // CUSTOMOBJECT
							// if (!ponerCEObject(objectStore, property,
							// BaseObject.TYPE_DOCUMENT, getId(dato))) { // Y si
							// // no
							// // es
							// // DOCUMENT
							// ponerCEObject(objectStore, property,
							// BaseObject.TYPE_FOLDER, getId(dato)); // Entonces
							// // es
							// // FOLDER
							// }
							// }

						} // if - else - if
						// properties.add(property);
					} // if
				} // if
			} // for
			return properties;
		} catch (Exception e) {
			e.printStackTrace();
		}// try - catch
		return null;
	}

	/*
	 * private static boolean ponerCEObject(ObjectStore objectStore, Property
	 * property, int type, String id) { try { BaseObject result = (BaseObject)
	 * objectStore.getObject(type, id); property.setValue(result); return true;
	 * } catch (Exception e) { e.printStackTrace(); } return false; }
	 */
	private static String getId(Object bean) {
		return (String) getMethod(bean, "getId");
	}

	private static void setId(Object bean, String id) {
		setMethod(bean, "setId", id);
	}

	private static void setMethod(Object bean, String method, Object value) {
		try {
			if (value != null) {
				Method setMethod = bean.getClass().getMethod(method,
						new Class[] { value.getClass() });
				setMethod.invoke(bean, new Object[] { value });
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static Object getMethod(Object bean, String method) {
		try {
			Class clase = bean.getClass();
			Method getMethod = clase.getMethod(method, null);
			return getMethod.invoke(bean, null);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	private static String getNombreClase(Object objeto) {
		String nombreClase = null;
		if (objeto instanceof String) {
			nombreClase = (String) objeto;
		} else if (objeto instanceof Class) {
			nombreClase = ((Class) objeto).getName();
		} else {
			nombreClase = objeto.getClass().getName();
		}
		int index = nombreClase.lastIndexOf(".");
		if (index != -1) {
			nombreClase = nombreClase.substring(index + 1);
		}
		return nombreClase;
	}

	/*
	 * private static Object get(Session session, String repository, Class
	 * beanClass, String id) { try { String nombreBean =
	 * getNombreClase(beanClass); String nombreClase =
	 * "com.intent.mail.data.p8.P8" + nombreBean + "DAO"; Class clase =
	 * Class.forName(nombreClase); Constructor constructor =
	 * clase.getConstructor(new Class[] { Session.class, String.class }); Object
	 * dao = constructor.newInstance(new Object[] { session, repository });
	 * Method getBean = clase.getMethod("get" + nombreBean, new Class[] {
	 * String.class }); return getBean.invoke(dao, new Object[] { id }); } catch
	 * (Exception e) { e.printStackTrace(); } return null; }
	 * 
	 * /** (non-Javadoc)
	 * 
	 * @see com.intent.mail.patterns.dao.NoticiaDAO#getNoticia(java.lang.String)
	 */
	public static Object get(ObjectStore store, String type, String id,
			Object bean) {
		// ReadableMetadataObject result = null;
		// ObjectStore objectStore = ObjectFactory.getObjectStore(repository,
		// session);
		IndependentObject result = store.fetchObject(type, id, null);
		return internalGet(result, bean);
	}// getNoticia

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.intent.mail.patterns.dao.NoticiaDAO#saveNoticia(com.intent.mail.bean.Noticia)
	 */
	public static boolean save(ObjectStore store, String folder, String type,
			String title, Object bean) {
		try {
			String id = getId(bean);
			// ObjectStore objectStore =
			// ObjectFactory.getObjectStore(repository,
			// session);
			if (id == null) { // create
				String nombreClase = getNombreClase(bean);
				Hashtable properties = internalSet(store, bean);
				// log.debug("[P8Utils.save] nombreClase: " +
				// nombreClase);
				// ContainableObject creado = (ContainableObject) objectStore
				// .createObject(nombreClase, properties, null);
				// Folder ceFolder = (Folder) objectStore.getObject(
				// BaseObject.TYPE_FOLDER, folder);
				// creado.file(ceFolder, true, title);
				IndependentObject creado = null;
				try {
					creado = P8DAO.a4XcreateCustomObject(properties,
							nombreClase, folder, store);
					((CustomObject) creado).save(RefreshMode.REFRESH);
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				try {
					creado = P8DAO.a4XcreateFolder(properties, nombreClase,
							folder, store);
					((Folder) creado).save(RefreshMode.REFRESH);
				} catch (Exception e) {
					// TODO: handle exception
				}
				try {
					creado = P8DAO.a4XcreateDocument(properties, nombreClase,
							folder, store);
					((com.filenet.api.core.Document) creado)
							.save(RefreshMode.REFRESH);
				} catch (Exception e) {
					// TODO: handle exception
				}
				creado.fetchProperties(new String[] { "Id" });
				setId(bean, creado.getProperties().getIdValue("Id").toString());
				log
						.debug("[P8Utils.save] Id del objeto creado: "
								+ getId(bean));
				return true;
			} else { // update
				IndependentObject result = (IndependentObject) store.getObject(
						type, id);
				// Properties properties = null;
				Hashtable properties = new Hashtable();
				properties = internalSet(store, bean);
				// result.setProperties(properties);
				result = P8DAO.a4XsetProperties(result, properties);
				((IndependentlyPersistableObject) result)
						.save(RefreshMode.REFRESH);
				return true;
			} // if - else
		} catch (Exception e) {
			e.printStackTrace();
		} // try - catch
		return false;
	}// save*/

	/**
	 * (non-Javadoc)
	 * 
	 * @see com.intent.mail.patterns.dao.NoticiaDAO#saveNoticia(com.intent.mail.bean.Noticia)
	 */
	/*
	 * public static boolean saveFolder(Session session, String repository,
	 * String folder, String title, Object bean) { try { String id =
	 * getId(bean); ObjectStore objectStore =
	 * ObjectFactory.getObjectStore(repository, session); if (id == null) { //
	 * create String label = (String) getMethod(bean, "getLabel"); Folder padre
	 * = (Folder) objectStore.getObject( BaseObject.TYPE_FOLDER, folder);
	 * Properties properties = ObjectFactory.getProperties(); Property property
	 * = ObjectFactory.getProperty("label"); property.setValue(label);
	 * properties.add(property); padre.addSubFolder(label, properties, null);
	 * log.debug("[P8Utils.save] subfolder creado!"); return true; } else { //
	 * update WriteableMetadataObject result = (WriteableMetadataObject)
	 * objectStore .getObject(BaseObject.TYPE_FOLDER, id); Properties properties
	 * = null; properties = ObjectFactory.getProperties(); Property property =
	 * ObjectFactory.getProperty("label"); String label = (String)
	 * getMethod(bean, "getLabel"); property.setValue(label);
	 * properties.add(property); result.setProperties(properties); return true;
	 * } // if - else } catch (Exception e) { e.printStackTrace(); } // try -
	 * catch return false; }// saveFolder
	 */
	public static boolean delete(ObjectStore store, String type, String id) {
		try {
			// ObjectStore repository =
			// ObjectFactory.getObjectStore(objectStore,
			// session);
			Object result = store.getObject(type, id);
			Method delete = result.getClass().getMethod("delete", null);
			delete.invoke(result, null);

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} // try - catch
		return false;
	}// deleteNoticia
	/*
	 * private static String capitalizar(String cadena) { if (cadena.length() >
	 * 0) { return cadena.toUpperCase().charAt(0) + cadena.substring(1); }
	 * return cadena.toUpperCase(); }
	 * 
	 * private static void identar(int cantidad, StringBuffer sb) { final String
	 * ESPACIOS = "    "; for (int i = 0; i < cantidad; i++) {
	 * sb.append(ESPACIOS); } }
	 * 
	 * private static String getJavaType(String ceType) { return (String)
	 * tipos.get(ceType); }
	 * 
	 * private static void print(Object toPrint) { log.debug("[Explorer] " +
	 * toPrint); }
	 */

}
