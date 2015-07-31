/**
 * RotuloPDF.java
 *
 * Clase encargada de recibir las configuraciones del rotulo en PDF
 * y generar el archivo
 *
 * @author  Juan F. Medina
 * @version 1.0
 * @see  import com.lowagie.text
 * @see  org.jdom
 * fecha: 03/08/2005
 * #########################################################
 * #modifica: Juan F. Medina
 * #motivo: Se necesita tener la alineacion de cada parrafo y formato de las frases
 * #fragmento: Modifica set lineas para hacer parse de un xml y generar rotulo para la presentacion
 * #fecha: 25/08/2005
 * #########################################################
 */

package com.intent.minminas472.pdf;

/*
 * Referencia las clases utilizadas de itext
 */

import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.PageSize;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfTemplate;
import com.lowagie.text.pdf.PdfWriter;

public class RotuloPDF {
	/*
	 * Variables privadas de la clase
	 */
	private float xpos; // Ubicación vertical de la esquina superior izquierda

	// del rótulo

	private float ypos; // Ubicación horizontal de la esquina superior izquierda

	// del rótulo

	private Rectangle tamanoPagina; // Tamaño de la pagina (valor por defecto =

	// PageSize.LETTER)

	private String tipoLetra; // La familia de letra a utilizar TIMES-ROMAN,

	// HELVETICA o COURIER

	private String tipoNegrilla; // La familia de letra para frases

	// resaltadas TIMES-BOLD, HELVETICA-BOLD O
	// COURIER-BOLD

	private int tamanoLetra; // Tamaño de la letra (valor por defecto = 10)

	private float porcentajeInterlineado; // Porcentaje de tamaño de linea

	// para el interlineado (valor por
	// defecto = 0.5)

	private List lineas; // Lineas de texto a imprimir

	private float altoRotulo;

	private float anchoRotulo;

	private float altoInterlineado;

	private float anchoEspaceado;

	private BaseFont letraNormal;

	private BaseFont letraNegrilla;

	private List paginas; // Paginas incluidas en el documento

	private boolean vertical;

	/**
	 * Constructor sin argumentos, lleva las variables a su valores default
	 */
	public RotuloPDF() {
		lineas = null;
		setTamanoPagina(new Rectangle(PageSize.LETTER));
		setTamanoLetra(10);
		setTipoLetra(BaseFont.TIMES_ROMAN);
		setTipoNegrilla(BaseFont.TIMES_BOLD);
		setPorcentajeInterlineado(0.5f);
		setVertical(false);
		inicializarDimensionesRotulo();
	}

	/**
	 * Modifica la coordenada horizontal de la esquina superior izquierda del
	 * rótulo la coordenadas horizontal incrementa de izquierda a derecha
	 */
	public void setXpos(float unaX) {
		if (unaX >= 0) {
			xpos = unaX;
		} else {
			xpos = 0;
		}
	}

	/**
	 * Retorna la coordenada horizontal de la esquina superior izquierda del
	 * rótulo
	 */
	public float getXpos() {
		return xpos;
	}

	/**
	 * Modifica la coordenada vertical de la esquina superior izquierda del
	 * rótulo la coordenadas horizontal incrementa de abajo hacia arriba
	 */
	public void setYpos(float unaY) {
		if (unaY >= 0) {
			ypos = unaY;
		} else {
			ypos = getTamanoPagina().getHeight();
		}
	}

	/**
	 * Retorna la coordenada vertical de la esquina superior izquierda del
	 * rótulo
	 */
	public float getYpos() {
		return ypos;
	}

	/**
	 * Crear un areglo con las lineas de rotulo a partir de un String con la
	 * definicion el rotulo en xml
	 */
	public void setLineas(String contenido) throws Exception {
		String texto;
		String estilo;
		String alineacion;
		contenido = contenido.replaceAll("&", "&amp;");
		if (contenido.length() >= 0) {
			try {
				// Carga el contenido en un doc xml
				org.jdom.Document doc = new org.jdom.input.SAXBuilder()
						.build((Reader) (new StringReader(contenido)));

				// Crea las paginas como segmento de la lista de lineas
				List xmlPagina = getListHijos(doc.getRootElement(), "pagina");
				lineas = new ArrayList();
				paginas = new ArrayList();

				int posLinea = 0;

				Iterator iPaginas = xmlPagina.iterator();

				while (iPaginas.hasNext()) {
					PaginaTabla pagina = new PaginaTabla();

					List xmllineas = getListHijos((org.jdom.Element) iPaginas
							.next(), "linea");
					pagina.setPosInicial(posLinea);
					posLinea = posLinea + xmllineas.size();
					pagina.setPosFinal(posLinea - 1);
					paginas.add((Object) pagina);

					// Crea una lista para llenarla de objetos linea

					for (int i = 0; i < xmllineas.size(); i++) { // Recorre
						// el xml y
						// llena la
						// lista
						// lineas
						// Lee la informacion de la linea del xml
						Object xmllinea = xmllineas.get(i);
						Iterator frases = getIteratorHijos(xmllinea, "frase");
						alineacion = ((org.jdom.Element) xmllinea)
								.getAttributeValue("alineacion");
						// crea una nueva linea y le establece la alineacion
						Linea linea = new Linea();
						linea.setAlineacion(alineacion);
						while (frases.hasNext()) {
							// leer una frase
							org.jdom.Element xmlfrase = (org.jdom.Element) frases
									.next();
							texto = getTextoElemento(xmlfrase);
							estilo = xmlfrase.getAttributeValue("estilo");
							// Adiciona la frase
							Frase frase = new Frase(texto, estilo);
							linea.adicionarFrase(frase);

						}
						lineas.add(linea);
					}
				}
			} catch (Exception e) {

				lineas = null;
				throw e;
			}

		} else {
			lineas = null;
		}
		inicializarDimensionesRotulo();
	}

	/**
	 * Crea un iterador sobre los hijos de un elemento del xml por facilidad
	 * para no tener que hacer el cast en diferentes lugare del codigo
	 */
	public Iterator getIteratorHijos(Object raiz, String tipo) {
		Iterator hijos = ((org.jdom.Element) raiz).getChildren(tipo).iterator();
		return hijos;
	}

	/**
	 * Retorna una lita con los hijos de un elemento del xml por facilidad para
	 * no tener que hacer el cast en diferentes lugare del codigo
	 */
	public List getListHijos(Object raiz, String tipo) {
		List hijos = ((org.jdom.Element) raiz).getChildren(tipo);
		return hijos;
	}

	/**
	 * Modifica el tamaño de la letra
	 */
	public void setTamanoLetra(int unTamanoLetra) {
		tamanoLetra = unTamanoLetra;
		inicializarDimensionesRotulo();
	}

	/**
	 * Modifica el nombre del tipo de letra
	 */
	public void setTipoLetra(String unTipoLetra) {
		tipoLetra = new String(unTipoLetra);
		inicializarDimensionesRotulo();
	}

	/**
	 * Retorna el ancho total de la pagina en puntos
	 */
	public float getAnchoPagina() {
		return getTamanoPagina().getWidth();
	}

	/**
	 * Retorna el alto total de la pagina en puntos
	 */
	public float getAltoPagina() {
		return getTamanoPagina().getHeight();
	}

	/**
	 * Retorna el ancho del rotulo en puntos
	 */
	public float getAnchoRotulo() {
		if (isVertical()) {
			return altoRotulo;
		} else {
			return anchoRotulo;
		}

	}

	/**
	 * Retorna el texto de un elemento xml
	 */
	private String getTextoElemento(Object elemento) {
		return ((org.jdom.Element) elemento).getTextTrim();
	}

	/**
	 * Retorna el ancho del rotulo en puntos Si el rotulo esta en posicion
	 * vertical intercambia el alto por el ancho
	 */
	public float getAltoRotulo() {
		if (isVertical()) {
			return anchoRotulo;
		} else {
			return altoRotulo;
		}
	}

	/**
	 * Retorna el alto del rotulo sin tener en cuenta al posicion vertical u
	 * horizontal
	 */
	private float getAltoRotuloSinRotar() {
		return altoRotulo;
	}

	/**
	 * Retorna el ancho del rotulo sin tener en cuenta al posicion vertical u
	 * horizontal
	 */
	private float getAnchoRotuloSinRotar() {
		return anchoRotulo;
	}

	/**
	 * generarRotuloPdf
	 * 
	 * Funcion encargada de generar el PDF en forma de arreglo de bytes
	 * 
	 * @param baos
	 *            arreglo de bytes donde se escribira en pdf resultante
	 * 
	 * @author Juan F. Medina
	 * @version 1.0 fecha: 03/08/2005
	 */
	public void generarRotuloPdf(OutputStream baos) throws Exception {
		try {

			/*
			 * Crear un documento direccionado al Arreglo de bytes recibido como
			 * parametro
			 */
			Document document = new Document(getTamanoPagina(), 0, 0, 0, 0); // Tamanno
			// del
			// papel
			// sin
			// margenes
			PdfWriter writer = PdfWriter.getInstance(document, baos);
			document.open();
			PdfContentByte cb = null;
			calcularDimensionesRotulo();
			float altoLinea = getAltoLinea();
			float valorInterlineado = getAltoInterlineado();
			float espaceado = getAnchoEspaceado();

			/*
			 * inicializa el desplazamiento para escribir la lineas de arriba
			 * hacia abajo
			 */
			float desplazamientoVertical = 0f;
			if (lineas != null && lineas.size() > 0) { // Si existen líneas
				// para escribir
				/*
				 * Recorre el arreglo de lineas incrementando el desplazamiento
				 * vertical para ubicar cada linea bajo la anterior dejando
				 * entre ellas el interlineado configurado
				 */
				Iterator iPaginas = paginas.iterator();
				boolean esPrimeraPagina = true;
				while (iPaginas.hasNext()) {
					PaginaTabla pagina = (PaginaTabla) iPaginas.next();
					if (esPrimeraPagina) {
						esPrimeraPagina = false;
					} else {
						document.newPage();
					}
					cb = writer.getDirectContent();
					desplazamientoVertical = 0f;
					PdfTemplate template = cb.createTemplate(
							getAnchoRotuloSinRotar(), getAltoRotuloSinRotar());
					template.setFontAndSize(letraNormal, getTamanoLetra());
					template.beginText();
					for (int i = pagina.getPosInicial(); i <= pagina
							.getPosFinal(); i++) {
						/*
						 * Deplaza el alto de linea para ubicar el texto en la
						 * linea base y no sobre la linea superior del texto
						 */
						Linea linea = (Linea) lineas.get(i);
						float desplazamientoHorizontal = calcularDesplazamientoHorizontal(linea);
						desplazamientoVertical = desplazamientoVertical
								+ altoLinea;
						Iterator frases = linea.getFrases().iterator();
						// Cada frase se escribe en al coordenada x,y que le
						// corresponde
						// como es la misma linea solo se modifica el
						// desplazamiento horizontal
						while (frases.hasNext()) {
							Frase frase = (Frase) frases.next();
							if (frase.getEstilo() == Frase.ESTILO_NORMAL) {
								template.setFontAndSize(letraNormal,
										getTamanoLetra());
							} else {
								template.setFontAndSize(letraNegrilla,
										getTamanoLetra());
							}
							// Ubica el texto en las coordenadas x,y
							template.setTextMatrix(desplazamientoHorizontal,
									getAltoRotuloSinRotar()
											- desplazamientoVertical);
							template.showText(frase.getTexto());

							// Se desplaza horizontalmente el tamano del texto
							// mas un espacio
							desplazamientoHorizontal = desplazamientoHorizontal
									+ frase.getAncho();
							desplazamientoHorizontal = desplazamientoHorizontal
									+ espaceado;

						}
						// Desplaza el tamano del interlineado
						desplazamientoVertical = desplazamientoVertical
								+ valorInterlineado;
					}
					template.endText(); // cierra de el area del documento
					if (isVertical()) {
						cb.addTemplate(template, 0, 1, -1, 0, // Rota 90 sobre
								// la esquina
								// inferior
								// izquierda
								getXpos() + getAltoRotuloSinRotar(), // desplaza
								// el
								// rotulo
								// para
								// corregir
								// la
								// rotacion
								getYpos() - getAnchoRotuloSinRotar()); // Convierte
						// las
						// coordenadas
						// horizontales
					} else {
						// No hay rotacion
						cb.addTemplate(template, getXpos(), getYpos()
								- getAltoRotulo());
					}
				}
			}
			document.close(); // cierra el documento
		} catch (Exception e2) {
			throw e2;
		}

	}

	/**
	 * Calcula el desplazamiento horizontal para poner el texto centrado o
	 * alineado a izquierda o a derecha
	 */
	private float calcularDesplazamientoHorizontal(Linea linea) {
		float anchoRotulo = getAnchoRotulo();
		float desplazamiento = 0f;
		String alineacion = linea.getAlineacion();
		if (alineacion.equals(Linea.ALINEACION_CENTRO)) {
			desplazamiento = (anchoRotulo - linea.getAnchoTexto()) / 2;
		}
		if (alineacion.equals(Linea.ALINEACION_DERECHA)) {
			desplazamiento = (anchoRotulo - linea.getAnchoTexto());
		}
		if (alineacion.equals(Linea.ALINEACION_IZQUIERDA)) {
			desplazamiento = 0f;
		}
		return desplazamiento;
	}

	/**
	 * Retorna un rectangulo que representa el tamano de pagina return Rectangle
	 */
	public Rectangle getTamanoPagina() {
		return tamanoPagina;
	}

	/**
	 * Establece el tamano de pagina
	 */
	public void setTamanoPagina(Rectangle tamanoPagina) {
		this.tamanoPagina = tamanoPagina;
	}

	/**
	 * Establece el nombre de la letra a utilizar
	 */
	public String getTipoLetra() {
		return tipoLetra;
	}

	/**
	 * retorna el nombre de la letra a utilizar cuando se requiera negrilla
	 */
	public String getTipoNegrilla() {
		return tipoNegrilla;
	}

	/**
	 * Establece el tipo de letra para las frase en Negrilla
	 */
	public void setTipoNegrilla(String tipoNegrilla) {
		this.tipoNegrilla = tipoNegrilla;
	}

	/**
	 * Retorna el tamano de letra utilizado
	 */
	public int getTamanoLetra() {
		return tamanoLetra;
	}

	/**
	 * Retorna la separacion entre lineas como un procentaje del alto de la
	 * linea
	 */
	public float getPorcentajeInterlineado() {
		return porcentajeInterlineado;
	}

	/**
	 * Establece la separacion entre lineas como un procentaje del alto de la
	 * linea
	 */
	public void setPorcentajeInterlineado(float porcentajeInterlineado) {
		this.porcentajeInterlineado = porcentajeInterlineado;
		inicializarDimensionesRotulo();
	}

	/**
	 * Calcula el tamano que ocupa la linea mas larga del rotulo
	 */
	private void calcularAnchoRotulo() {
		float curAnchoLinea;
		float curAnchoFrase;
		float maxAnchoLinea = 0f;
		float espaciado;
		String texto;
		try {

			/*
			 * Recorre las lineas del texto para encontra la mas larga
			 */
			for (int i = 0; i < lineas.size(); i++) {
				Linea linea = (Linea) lineas.get(i);
				Iterator frases = linea.getFrases().iterator();
				curAnchoLinea = 0f;
				while (frases.hasNext()) {
					Frase frase = (Frase) frases.next();
					texto = frase.getTexto();
					/* Espacio entre frases */
					if (curAnchoLinea > 0) {
						curAnchoLinea = curAnchoLinea + getAnchoEspaceado();
					}
					/* tamano de la frase */
					frase.setAncho(letraNormal.getWidthPoint(texto,
							getTamanoLetra()));
					curAnchoLinea = curAnchoLinea + frase.getAncho();

				}
				linea.setAnchoTexto(curAnchoLinea);
				maxAnchoLinea = Math.max(maxAnchoLinea, curAnchoLinea);
			}
			this.anchoRotulo = maxAnchoLinea;
		} catch (Exception e) {

		}

	}

	/**
	 * Calcula el alto del rotulo multiplicando el tamano de linea y el
	 * interlineado por el numero total de lineas
	 */
	private void calcularAltoRotulo() {
		if (paginas == null || paginas.size() <= 0) {
			altoRotulo = 0f;
		} else {

			float curAnchoLinea;
			PaginaTabla pagina = (PaginaTabla) paginas.get(0);
			float lineasxPaginas = pagina.getPosFinal()
					- pagina.getPosInicial() + 1;
			float altoLinea = getAltoLinea();
			float valorInterlineado = getAltoInterlineado();
			float altoTotal = altoLinea * lineasxPaginas;
			altoRotulo = altoTotal + valorInterlineado * (lineasxPaginas - 1);
		}
	}

	/**
	 * Retorna el valor en puntos del espacio de interlineado
	 */
	private float getAltoInterlineado() {
		return altoInterlineado;
	}

	/**
	 * Retona el ancho en punto del espacio en blanco entre frases
	 */
	private float getAnchoEspaceado() {
		return anchoEspaceado;
	}

	/**
	 * Crea la letras segun el nombre y el tamano definidos
	 */
	private void crearLetras() {
		try {
			letraNormal = BaseFont.createFont(getTipoLetra(), BaseFont.CP1252,
					BaseFont.NOT_EMBEDDED);
			letraNegrilla = BaseFont.createFont(getTipoNegrilla(),
					BaseFont.CP1252, BaseFont.NOT_EMBEDDED);
		} catch (Exception e) {
			letraNormal = null;
			letraNormal = null;
		}
	}

	/**
	 * Calcula el tamano del espacion blanco para separar las frases
	 */
	private void calcularEspaceado() {
		this.anchoEspaceado = letraNormal.getWidthPoint(" ", getTamanoLetra());
	}

	/**
	 * Retorna el alto, en puntos, de una linea de texto
	 */
	private float getAltoLinea() {
		return letraNormal.getAscentPoint("A", getTamanoLetra());
	}

	/**
	 * Calcula el alto, en puntos, del interlineado
	 */
	private void calcularInterlineado() {

		this.altoInterlineado = getAltoLinea() * getPorcentajeInterlineado();

	}

	/**
	 * Coloca en creo los altos y anchos por cambio de letra o de textos
	 */
	private void inicializarDimensionesRotulo() {
		setAltoRotulo(0f);
		setAnchoRotulo(0f);
		setAltoInterlineado(0f);
		setAnchoEspaceado(0f);
	}

	/**
	 * Calcula los altos y anchos
	 */
	public void calcularDimensionesRotulo() {

		crearLetras();
		calcularEspaceado();
		calcularInterlineado();
		calcularAnchoRotulo();
		calcularAltoRotulo();

	}

	private void setAltoRotulo(float altoRotulo) {
		this.altoRotulo = altoRotulo;
	}

	private void setAnchoRotulo(float anchoRotulo) {
		this.anchoRotulo = anchoRotulo;
	}

	private void setAltoInterlineado(float altoInterlineado) {
		this.altoInterlineado = altoInterlineado;
	}

	private void setAnchoEspaceado(float anchoEspaceado) {
		this.anchoEspaceado = anchoEspaceado;
	}

	public boolean isVertical() {
		return vertical;
	}

	public void setVertical(boolean vertical) {
		this.vertical = vertical;
	}

}
