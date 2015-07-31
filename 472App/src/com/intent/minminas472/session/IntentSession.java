package com.intent.minminas472.session;

import java.util.Collection;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.Vector;

import javax.security.auth.Subject;

import com.filenet.api.core.Connection;
import com.filenet.api.core.Domain;
import com.filenet.api.core.Factory;
import com.filenet.api.core.ObjectStore;
import com.filenet.api.util.UserContext;
import com.intent.minminas472.utils.Constants;

/**
 * Implementación para el manejo de Sesiones con P8
 * 
 * @author Carlos Montoya : carlos.montoya@mvm.com.co
 * @version 1.0
 * @see
 * 
 */

public class IntentSession {

	// private Persona persona;

	// private Session p8Session;
	private ObjectStore objectStore;

	public ObjectStore getObjectStore() {
		return objectStore;
	}

	public void setObjectStore(ObjectStore objectStore) {
		this.objectStore = objectStore;
	}

	private HashMap credentials;

	private Vector dependenciasGestor;

	private static String configFile;

	/**
	 * Crea una sesión
	 * 
	 * @param sAppName
	 *            Nombre de la aplicación
	 * @param sUsuario
	 *            Nombre de Usuario
	 * @param sPassword
	 *            Password del usuario
	 * @param sConfFile
	 *            Path del archivo de configuración WcmApiConfig.properties
	 * @throws Exception
	 */
	public IntentSession(String sAppName, String sUsuario, String sPassword,
			String sConfFile, ObjectStore store) throws Exception {
		this.configFile = sConfFile;
		/*
		 * p8Session = ObjectFactory.getSession(sAppName, Session.DEFAULT,
		 * sUsuario, sPassword); p8Session.setConfiguration(new
		 * FileInputStream(sConfFile)); p8Session.verify(); credentials =
		 * p8Session.fromToken(p8Session.getToken());
		 */
		if (store == null)
			objectStore = initialize(sUsuario, sPassword, sConfFile);
		else {
			objectStore = vaidateConnection(objectStore);
		}

	}// IntentSession

	/**
	 * jvargas p845 migration.
	 * 
	 * @param user
	 * @param password
	 */
	public static ObjectStore initialize(String user, String password,
			String sConfFile) {
		try {
			ResourceBundle bundle = ResourceBundle.getBundle("WcmApiConfig");
			java.util.Properties props = System.getProperties();
			props
					.setProperty("wasp.location", bundle
							.getString("waspLocation"));

			Connection conn = Factory.Connection.getConnection(bundle
					.getString("RemoteServerUrl"));

			UserContext uc = UserContext.get();
			if (uc.getSubject() == null) {
				System.out.println("subject is null");
			} else {
				System.out.println(uc.getSubject().getPublicCredentials());
			}
			System.out.println("Sess:" + user);
			Subject subject = uc.createSubject(conn, user, password, bundle
					.getString("jaasConfigurationName"));
			uc.pushSubject(subject);

			Domain domain = Factory.Domain.fetchInstance(conn, null, null);
			ObjectStore os = Factory.ObjectStore.fetchInstance(domain,
					Constants.getProperty("LIBRERIA_CORRESPONDENCIA"), null);

			System.out.println(os.get_Id().toString());
			System.out.println("FileNet CE Conected!!");
			return os;

		} catch (Exception ex) {
			ex.printStackTrace();

		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.intent.components.session.ISession#getUserPassword()
	 */
	public String getUserPassword() {
		// TODO Auto-generated method stub
		return null;
	}// getUserPassword

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.intent.components.session.ISession#getRoles()
	 */
	public Collection getRoles() {
		// TODO Auto-generated method stub
		return null;
	}// getRoles

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.intent.correspondencia.logic.session.ISession#getDependenciasGestor()
	 */
	public Vector getDependenciasGestor() {

		return dependenciasGestor;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.intent.correspondencia.logic.session.ISession#isGestor()
	 */
	public boolean isGestor() {
		return dependenciasGestor != null && dependenciasGestor.size() > 0;
	}

	public static ObjectStore vaidateConnection(ObjectStore store) {

		UserContext uc = UserContext.get();
		if (uc.getSubject() == null) {
			return initialize(Constants
					.getProperty("application_p8_usuario"), Constants
					.getProperty("application_p8_clave"), configFile);
		} else {

			return initialize(Constants
					.getProperty("application_p8_usuario"), Constants
					.getProperty("application_p8_clave"), configFile);
			// return store;
		}

	}

}// IntentSession
