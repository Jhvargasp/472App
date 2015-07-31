/**
 * 
 */
package com.intent.minminas472.db;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Hibernate;
import org.hibernate.HibernateException;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.context.ApplicationContext;

import com.grupointent.genericapp.context.ApplicationContextProvider;

/**
 * @author Jorge
 * 
 */
public class HibernateUtil {
	private static Logger log = Logger.getLogger(HibernateUtil.class);

	public static int quantitySessionsOpened = 0;

	private static SessionFactory getBean() {
		Object objBean = null;
		ApplicationContext actx = ApplicationContextProvider.appContext;
		objBean = actx.getBean("sessionFactory");
		return (SessionFactory) objBean;
	}

	/**
	 * 
	 * @param query
	 * @return
	 */
	public static String[] getColumnNames(String query) {
		query = query.substring(0, query.toLowerCase().indexOf("from "));

		String cad = validaParentesis(query);
		while (cad != null) {
			query = cad;
			cad = validaParentesis(query);
		}
		// Pattern patron = Pattern.compile("\\(.[\\w, '._\\/:-]*+\\)");
		// Matcher encaja = patron.matcher(query);
		// // List <String>al = new ArrayList<String>();
		// while (encaja.find()) {
		// int inicio = encaja.start();
		// String auxCad = query.substring(inicio);
		// int fin = auxCad.indexOf(")");
		// ;
		// cad = query.substring(inicio, inicio + fin + 1);
		// // al.add(cad);
		// query = query.replaceAll("\\(" + cad + "\\)", "");
		// if(cad.startsWith("(")&&cad.endsWith(")")){
		// query = query.replaceAll( cad , "");
		// }
		// encaja = patron.matcher(query);
		// }

		/*
		 * for (String object : al) { query=query.replaceAll(object, ""); }
		 */

		/*
		 * while (query.contains("(")) { query = query.replaceAll("\\(.*\\)",
		 * ""); }
		 */

		String cads[] = query.split(",");
		// System.out.println(query);
		String values[] = new String[cads.length];
		for (int i = 0; i < cads.length; i++) {
			String string = cads[i];
			String otherValues[] = string.split(" ");
			values[i] = otherValues[otherValues.length - 1];
			values[i] = values[i].replaceAll("[|]", "");
			// System.out.println(values[i]);
		}
		return values;
	}

	public static void main(String[] args) {
		// validaParentesis("SELECT distinct A.RADICADO AS Radicado,A.RADICADO AS Radicado,      TO_CHAR(TO_DATE() + (A.FECHARADICACION/(60*1440)),'YYYY-MM-DD HH:MI:SS AM') AS FechaRadicacion,      A.ASUNTO AS Asunto,A.CONTACTO AS Contacto,A.RESPONSABLE AS Responsable,A.EQUIPOTRABAJO AS EquipoTrabajo,      A.ESTADO AS Estado,A.USUARIOTRAMITADOR AS UsuarioTramitador,B.F_USERNAME AS Usuario       FROM VW_DEFAULTLOGCORRESPONDENCIA A, VWUSER B       WHERE      TO_DATE() + (A.FECHARADICACION/(60*1440)) >= TO_DATE('01-01-2010 00:00:00','DD-MM-YYYY HH24:MI:SS') AND      TO_DATE() + (A.FECHARADICACION/(60*1440)) <= TO_DATE('12-11-2010 00:00:00','DD-MM-YYYY HH24:MI:SS') AND        A.RADICADO LIKE 'E-10-0000059'        AND A.F_USERID = B.F_USERID       AND (B.F_USERNAME = lower('ACE_ContAdmin') OR          A.USUARIOTRAMITADOR = lower('ACE_ContAdmin'))");
		String a[] = getColumnNames("Select Radicado, Radicado, FechaComunicacion, Contacto, NumeroComunicacion"
				+ "        From   vw_radicados"
				+ " Where  Radicado LIKE 'R%' OR "
				+ "               FechaComunicacion = TO_DATE('','dd-mm-yyyy') OR "
				+ "               Contacto LIKE '' OR "
				+ "               NumeroComunicacion LIKE ''");
		System.out.println(a.toString());
	}

	private static String validaParentesis(String query) {
		int index = query.indexOf("(");
		if (index >= 0) {
			boolean esta = true;
			int inicio = index;
			int fin = -1;
			while (esta) {
				index++;
				if (query.charAt(index) == ')') {
					fin = index;
					esta = false;
				} else if (query.charAt(index) == '(') {
					inicio = index;
				} else if (index == query.length()) {
					esta = false;
				} else {

				}
			}
			if (fin > 0) {
				String cad = query.substring(inicio, fin + 1);

				if (cad.length() > 0)
					if (cad.equals("()")) {
						return query.replaceAll("\\(\\)", "");
					} else {

						return query.substring(0, inicio)
								+ query.substring(fin + 1);
					}
				// return query.replaceAll(cad, "");
				else
					return null;
			}
		}
		return null;
	}

	public static boolean execute(String sql) {
		SessionFactory factory = getBean();

		Session session = factory.openSession();
		session.getTransaction().setTimeout(3);
		int i = session.createSQLQuery(sql).executeUpdate();
		if (i > 0) {
			session.close();
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 
	 * @param query
	 * @return
	 */
	public static List<String> query(String query, SessionFactory factory,
			String var) throws Exception {

		List list = new ArrayList();

		long l = new Date().getTime();
		// factory.getCollectionMetadata(query);
		// log.debug("Opened Seassons: " + quantitySessionsOpened);
		log.debug("Before open session: " + query);
		Session session = factory.openSession();
		session.getTransaction().setTimeout(3);

		quantitySessionsOpened++;
		log.debug("After open session " + query);
		// log.debug("Ajax autocomplete: " + query);

		try {
			log.debug("Query to:"
					+ session.connection().getMetaData()
							.getDatabaseProductName());
			log
					.debug("Query to:"
							+ session.connection().getMetaData().getURL());

			if (session.connection().getMetaData().getDatabaseProductName()
					.equalsIgnoreCase("Oracle")) {
				session.createSQLQuery("alter session set nls_comp=LINGUISTIC")
						.executeUpdate();
				session.createSQLQuery("alter session set nls_sort=BINARY_CI")
						.executeUpdate();
			}
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e);
		}
		try {
			SQLQuery sqlQuery = session.createSQLQuery(query.trim());
			list = sqlQuery.list();
		} catch (HibernateException e) {
			e.printStackTrace();
			session.close();
			quantitySessionsOpened--;
			log.debug(e);
			throw e;
		}
		session.close();
		quantitySessionsOpened--;
		log.debug("Results:" + list.size());
		log.debug("Time Query:" + query + " ----- "
				+ (new Date().getTime() - l));
		return list;
	}

	/**
	 * 
	 * @param query
	 * @return
	 */
	public static List query(String query) throws Exception {
		log.debug(query);
		SessionFactory factory = getBean();

		return query(query, factory, null);
	}

}
