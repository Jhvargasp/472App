/*
 * GuiaPDF.java
 * Clase encargada escribir texto en una forma preimpresa recibiendo en 
 * formato xml los texto y su ubicacion x, y en milimetros
 *
 * @author  Juan F. Medina
 * @version 1.0
 * @see     import com.lowagie.text
 * fecha: 5/09/2005
 * #########################################################
 */

package com.intent.minminas472.pdf;

import java.io.ByteArrayOutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

public class GuiaPDF {

	public GuiaPDF() {
	}

	/**
	 * generarGuia Genera el archivo pdf en una array de bytes
	 * 
	 * @param contenido
	 *            texto en formato xml con cada frase y su posicion absoluta
	 * @param baos
	 *            arreglo de bytes con los
	 */
	public void generarGuia(String contenido, ByteArrayOutputStream baos)
			throws Exception {
		String texto;
		if (contenido != null && contenido.length() >= 0) {
			try {
				// Carga el contenido en un doc xml
				org.jdom.Document doc = new org.jdom.input.SAXBuilder()
						.build((Reader) (new StringReader(contenido)));

				// Inicializa los valores

				float mmX = 0f;
				float mmY = 0f;
				float puntosX = 0f;
				float puntosY = 0f;
				float alto = 0f;
				float ancho = 0f;

				/**
				 * Lee el alto y el ancho de la guia
				 */
				org.jdom.Element guia = (org.jdom.Element) doc.getRootElement();
				List frases = guia.getChildren("frase");
				try {
					alto = Float.parseFloat(guia.getAttributeValue("alto"));
				} catch (Exception e) {
					alto = Utils.mmsApuntos(100f);
				}
				try {
					ancho = Float.parseFloat(guia.getAttributeValue("ancho"));
				} catch (Exception e) {
					ancho = Utils.mmsApuntos(50f);
				}

				// Crea el documento y el writer

				Rectangle pageSize = new Rectangle(Utils.mmsApuntos(ancho),
						Utils.mmsApuntos(alto));
				Document document = new Document(pageSize);
				PdfWriter writer = PdfWriter.getInstance(document, baos);
				document.open();

				PdfContentByte cb = writer.getDirectContent();
				cb.beginText();

				BaseFont bf = BaseFont.createFont(BaseFont.TIMES_ROMAN,
						BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
				cb.setFontAndSize(bf, 10);

				// Crea una lista para llenarla de objetos linea
				for (int i = 0; i < frases.size(); i++) { // Recorre el xml
					// Lee la informacion de la cada frase
					org.jdom.Element frase = (org.jdom.Element) frases.get(i);
					try {
						mmX = Float.parseFloat(frase.getAttributeValue("x"));
						puntosX = Utils.mmsApuntos(mmX);
					} catch (NumberFormatException e) {
						mmX = 0f;
						puntosX = 0f;
					}
					try {
						mmY = Float.parseFloat(frase.getAttributeValue("y"));
						puntosY = Utils.mmsApuntos(Math.abs(alto - mmY));
					} catch (NumberFormatException e) {
						mmY = 0f;
						puntosY = 0f;
					}

					// Lee el texto de la frase
					texto = frase.getTextTrim();
					// Escribe el texto en la posicion indicada por los
					// atributos de la frase
					cb.setTextMatrix(puntosX, puntosY);
					cb.showText(texto);

				}
				cb.endText();
				document.close();
			} catch (Exception e) {

				throw e;
			}

		}
	}
}
