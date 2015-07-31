/*
 * Frase.java
 * Clase para alamacenar cada frase que conforma la linea y su formato
 *
 * @author  Juan F. Medina
 * @version 1.0
 * @see     import com.lowagie.text
 * fecha: 25/08/2005
 * #########################################################
 */
package com.intent.minminas472.pdf;

public class Frase {

	public static final String ESTILO_NORMAL = "NORMAL";

	public static final String ESTILO_NEGRILLA = "NEGRILLA";

	/** Crea una instancia de frase sin parametros */
	public Frase() {
		setTexto("");
		setEstilo("");
	}

	/** Crea una instancia de frase con texto y estilo */
	public Frase(String texto, String estilo) {
		setTexto(texto);
		setEstilo(estilo);
	}

	private String texto;

	private String estilo;

	private int nroCaracteres;

	private float ancho;

	public String toString() {
		return getTexto();
	}

	/**
	 * Retorna el estilo de la frase (NEGRILLA, NORMAL)
	 */
	public String getEstilo() {
		return estilo;
	}

	/**
	 * Establece el estilo de la frase (NEGRILLA, NORMAL)
	 */
	public void setEstilo(String estilo) {

		if (estilo == null) {
			this.estilo = ESTILO_NORMAL;
		} else {
			this.estilo = estilo.toUpperCase();
			this.estilo = this.estilo.trim();
			if (!this.estilo.equals(ESTILO_NEGRILLA)) {
				this.estilo = ESTILO_NORMAL;
			}

		}
	}

	/**
	 * Retorna el numero de caracteres de la frase
	 */
	public int getNroCaracteres() {
		return nroCaracteres;
	}

	/**
	 * Establece el numero de caracteres de la frase
	 */
	private void setNroCaracteres(int nroCaracteres) {
		this.nroCaracteres = nroCaracteres;
	}

	public float getAncho() {
		return ancho;
	}

	public void setAncho(float ancho) {
		this.ancho = ancho;
	}

	/** Estable el texto de la frase */
	public String getTexto() {
		return texto;
	}

	/** Retorna el texto de la frase */
	public void setTexto(String texto) {
		this.texto = texto;
		setNroCaracteres(this.texto.length());
	}

}
