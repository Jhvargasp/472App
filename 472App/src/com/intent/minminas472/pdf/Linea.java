/*
 * Linea.java
 * Clase que almacena cada line de texto del rotulo
 *
 * @author  Juan F. Medina
 * @version 1.0
 * @see     import com.lowagie.text
 * fecha: 25/08/2005
 * #########################################################
 */

package com.intent.minminas472.pdf;

import java.util.ArrayList;
import java.util.List;

public class Linea {

	/** Creates a new instance of Linea */
	public Linea() {
		frases = new ArrayList();
		setAnchoTexto(0f);
		setAlineacion(ALINEACION_IZQUIERDA);
		setTexto("");
	}

	public static final String ALINEACION_IZQUIERDA = "IZQUIERDA";

	public static final String ALINEACION_CENTRO = "CENTRO";

	public static final String ALINEACION_DERECHA = "DERECHA";

	private int caracteres;

	private float anchoTexto;

	private float altoTexto;

	private String alineacion;

	private String texto;

	private List frases;

	// private

	public void adicionarFrase(Frase frase) {
		frases.add(frase);
		setTexto(getTexto() + " " + frase.getTexto());
	}

	public String getTexto() {
		return texto;
	}

	public float getAnchoTexto() {
		return anchoTexto;
	}

	public void setAnchoTexto(float tamano) {
		this.anchoTexto = tamano;
	}

	public String getAlineacion() {
		return alineacion;
	}

	public List getFrases() {
		return (List) (new ArrayList(frases));
	}

	public String toString() {
		return this.texto;
	}

	public void setAlineacion(String alineacion) {
		if (alineacion == null) {
			this.alineacion = ALINEACION_IZQUIERDA;
		} else {
			this.alineacion = alineacion.toUpperCase();
			this.alineacion = this.alineacion.trim();
			if (!this.alineacion.equals(ALINEACION_CENTRO)
					&& !this.alineacion.equals(ALINEACION_DERECHA)) {
				this.alineacion = ALINEACION_IZQUIERDA;
			}
		}
	}

	public void setTexto(String texto) {
		this.texto = texto;
	}

	public float getAltoTexto() {
		return altoTexto;
	}

	public void setAltoTexto(float altoTexto) {
		this.altoTexto = altoTexto;
	}

}
