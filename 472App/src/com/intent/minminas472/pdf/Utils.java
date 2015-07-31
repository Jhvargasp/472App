/*
 * Utils.java
 *
 * Utilidades para el manejo de las medidas en PDF
 * @author Juan Fernando Medina
 * Created 2005-09-10
 */

package com.intent.minminas472.pdf;

public class Utils {

	/** Creates a new instance of Utils */
	public Utils() {
	}

	public static final float puntosxMilimetro = 2.83464567f; // Numero de

	// puntos por
	// milimentro

	/**
	 * Convierte centimetros una medida en puntos
	 */
	public static float puntosAcms(float puntos) {
		return puntos / puntosxMilimetro;
	}

	/**
	 * Convierte puntos una medida en centimetros
	 */
	public static float mmsApuntos(float mms) {
		return mms * puntosxMilimetro;
	}

}
