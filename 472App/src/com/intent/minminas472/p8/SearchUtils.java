package com.intent.minminas472.p8;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import com.filenet.api.collection.RepositoryRowSet;
import com.filenet.api.property.Properties;
import com.filenet.api.property.Property;
import com.filenet.api.query.RepositoryRow;

/**
 * Clase para optimizar el manejo de búsquedas en P8
 * #########################################################
 * 
 * @author Hans Ospina Gómez: hospina@intentgroup.com.co
 * @version 1.0 #########################################################
 *          #modifica: #motivo: #fragmento: #fecha: Jul 25, 2005
 *          #########################################################
 */
public class SearchUtils {

	/**
	 * El logger para esta clase
	 */
	private static Logger log = Logger.getLogger(SearchUtils.class);

	public static List searchAsCollection(RepositoryRowSet set,
			boolean linkProperty) throws IOException {
		List ls = new LinkedList();
		Iterator it = set.iterator();

		while (it.hasNext()/* &&cant<100 */) {
			// cant++;

			RepositoryRow row = (RepositoryRow) it.next();
			Properties en = row.getProperties();
			Iterator properties = en.iterator();
			Hashtable cols = new Hashtable();
			List columnNames = new ArrayList();
			while (properties.hasNext()) {
				Property property = (Property) properties.next();

				// Data data = new Data();
				// data.setValue( (property.getObjectValue()!=null) ?
				// property.getObjectValue().toString() : "" );
				// data.setName(property.getPropertyName());
				// data.setDisplayName(property.getPropertyName());
				// //data.setLink(link);
				// //data.setTipo(property.get);
				Object value = "";

				if (property.getObjectValue() != null
						&& property.getObjectValue() instanceof Date) {
					SimpleDateFormat sdf = new SimpleDateFormat(
							"dd-MM-yyyy hh:mm aaa");
					value = sdf.format((Date) property.getObjectValue());
				} else if (property.getObjectValue() != null)
					value = property.getObjectValue();
				String cName = property.getPropertyName();
				if (!columnNames.contains(property.getPropertyName())) {
					columnNames.add(property.getPropertyName());
				} else {
					cName = property.getPropertyName() + "--"
							+ columnNames.size();
					columnNames.add(cName);

				}
				cols.put(cName, value);
			}// inner-while
			// java.util.Collections.sort(cols, new DataComparator());
			ls.add(cols);
		}// while
		return ls;
	}// searchToObject

}// class
