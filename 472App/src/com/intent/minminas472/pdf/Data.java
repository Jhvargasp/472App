/*
 * Data.java
 *
 * Created on September 11, 2005, 12:10 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.intent.minminas472.pdf;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * 
 * @author hospina
 */
public class Data {
	private Logger log = Logger.getLogger(Data.class);

	public static final int TYPE_BINARY = 1;

	public static final int TYPE_BOOLEAN = 2;

	public static final int TYPE_DATETIME = 3;

	public static final int TYPE_FLOAT = 4;

	public static final int TYPE_ID = 5;

	public static final int TYPE_INTEGER = 6;

	public static final int TYPE_OBJECT = 7;

	public static final int TYPE_STRING = 8;

	private String link;

	private String tipo;

	private int type;

	private int rsNumber;

	private String name;

	private String value;

	private String displayName;

	/** Creates a new instance of Data */
	private Data(int type, int rsNumber, String name, String displayName) {
		this.type = type;
		this.rsNumber = rsNumber;
		this.name = name;
		this.setDisplayName(displayName);
	}// special constructor

	/** Creates a new instance of Data */
	public Data() {
	}// default constructor

	public int getType() {
		return type;
	}// getType

	public void setType(int type) {
		this.type = type;
	}// setType

	public void setType(String typeValue) {
		if (typeValue.equals("string")) {
			this.type = Data.TYPE_STRING;
		} else if (typeValue.equals("dateTime")) {
			this.type = Data.TYPE_DATETIME;
		} else if (typeValue.equals("binary")) {
			this.type = Data.TYPE_BINARY;
		} else if (typeValue.equals("boolean")) {
			this.type = Data.TYPE_BOOLEAN;
		} else if (typeValue.equals("float")) {
			this.type = Data.TYPE_FLOAT;
		} else if (typeValue.equals("id")) {
			this.type = Data.TYPE_ID;
		} else if (typeValue.equals("int")) {
			this.type = Data.TYPE_INTEGER;
		} else if (typeValue.equals("object")) {
			this.type = Data.TYPE_OBJECT;
		}// if-else
	}// setType

	public int getRsNumber() {
		return rsNumber;
	}// getRsNumber

	public void setRsNumber(int rsNumber) {
		this.rsNumber = rsNumber;
	}// setRsNumber

	public String getName() {
		return name;
	}// getName

	public void setName(String name) {
		this.name = name;
	}// setName

	public Object getValue() {
		if (getType() == 3) {
			try {
				value = value.replace('T', ' ');
				SimpleDateFormat format = new SimpleDateFormat(
						"yyyy-MM-dd hh:mm:ss");
				Date date = format.parse(this.value);
				return date;
			} catch (Exception ex) {
				if (log.isDebugEnabled()) {
					StringBuffer buffer = new StringBuffer();
					buffer.append("Problems parsing the date: ");
					buffer.append(value);
					log.debug(buffer.toString(), ex);
				}// if
				return value;
			}// try-catch

		}// if
		return value;
	}// getValue

	public void setValue(String value) {
		this.value = value;
	}// setValue

	public String getDisplayName() {
		return displayName;
	}// getDisplayName

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}// setDisplayName

	public Data autoClone() {
		return new Data(getType(), getRsNumber(),
				getName() != null ? new String(getName()) : null,
				getDisplayName() != null ? new String(getDisplayName()) : null);
	}// clone

	public String toString() {
		return new StringBuffer(" <FILENET_RESULT_DATA> { DisplayName='")
				.append(getDisplayName()).append("' ; name='")
				.append(getName()).append("' ; value='").append(getValue())
				.append("' ; type='").append(getType())
				.append("' ; rsNumber='").append(getRsNumber()).append(
						"' ; link='").append(getLink()).append(
						"' } </FILENET_RESULT_DATA>").toString();
	}// toString

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

}// class
