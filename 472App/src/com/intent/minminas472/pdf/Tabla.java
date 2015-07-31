/**
 * Tabla.java
 *
 * Clase encargada de generar PDFs con con tablas como resultado
 * de consultas
 * @author  Juan F. Medina
 * @version 1.0
 * @see  import com.lowagie.text
 * @see com.lowagie.text
 * fecha: 12/09/2005
 ***********************************************************************************
 * Modificacion
 *   Se adicio la funcionalidad de cambio de pagina por un campo
 ***********************************************************************************
 * Modificacion
 *   Se agrego un array de enteros para ingrear las columnas a ocultar
 * fecha: 12/10/2005
 */

package com.intent.minminas472.pdf;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

/**
 * 
 * @author Juan F. Medina
 */
public class Tabla {

	protected PdfWriter writer = null;

	private Rectangle tamanoPagina; // Tamaño de la pagina (valor por defecto =

	// PageSize.LETTER)

	private String tipoLetra; // La familia de letra a utilizar TIMES-ROMAN,

	// HELVETICA o COURIER

	private String tipoLetraEncabezados; // La familia de letra para frases

	// resaltadas TIMES-BOLD,
	// HELVETICA-BOLD O COURIER-BOLD

	private String tipoLetraTitulo; // La familia de letra para el titulo

	// TIMES-BOLD, HELVETICA-BOLD O COURIER-BOLD

	private int tamanoLetra; // Tamaño de la letra (valor por defecto = 10)

	private int tamanoLetraEncabezados; // Tamaño de la letra (valor por defecto

	// = 10)

	private int tamanoLetraTitulo; // Tamaño de la letra (valor por defecto =

	// 10)

	private List encabezadosColumnas;

	protected List filas;

	private float margenIzquierda;

	private float margenDerecha;

	private float margenSuperior;

	private float margenInferior;

	private boolean rotarPagina;

	private String titulo;

	private int alineacionTitulo;

	private String valorRompimiento;

	private int campoRompimiento;

	private String pie;

	private int[] columnasOcultas;

	/** Creates a new instance of Tabla */
	public Tabla() {
		setTamanoPagina(new Rectangle(PageSize.LEGAL.rotate()));
		setTamanoLetra(10);
		setTamanoLetraEncabezados(12);
		setTipoLetraEncabezados("HELVETICA");
		setTamanoLetraTitulo(12);
		setTipoLetra(BaseFont.COURIER);
		setTipoLetraEncabezados(BaseFont.COURIER_BOLD);
		setTipoLetraTitulo(BaseFont.TIMES_BOLD);
		setTitulo("");
		setPie("");
		setRotarPagina(false);
		setAlineacionTitulo(Element.ALIGN_CENTER);
		setMargenIzquierda(25);
		setMargenDerecha(15);
		setMargenSuperior(15);
		setMargenInferior(5);
		setCampoRompimiento(-1);
		setColumnasOcultas(null);
	}

	/**
	 * Obtiene el contenido de la tabla como una lista de filas, cada fila es a
	 * su vez una lista de celda
	 */
	protected List getFilas() {
		return filas;
	}

	/**
	 * Establece el contenido de la tabla como una lista de filas, cada fila es
	 * a su vez una lista de celdas
	 */
	public void setFilas(List valoresCeldas) {
		this.filas = valoresCeldas;
	}

	/**
	 * Instancia un objeto Document para referenciar todo el contenido del PDF
	 * 
	 * @param baos
	 *            Arreglo de bytes que para almacenar el documento PDF.
	 */
	protected Document crearDocumento(OutputStream baos) throws Exception {
		Document document = new Document(getTamanoPagina(),
				getMargenIzquierda(), getMargenDerecha(), getMargenSuperior(),
				getMargenInferior());
		try {
			// Creacion de write
			writer = PdfWriter.getInstance(document, baos);
			document.open();
		} catch (Exception e) {
			baos = null;
			document = null;
			throw e;
		}
		return document;

	}

	/**
	 * Escribe el titulo del documento centrado en la pagina
	 * 
	 * @param doc
	 *            Documento PDF donde se va a adicionar el titulo
	 */
	protected void escribirEncabezado(Document doc, PaginaTabla pagina)
			throws Exception {
		String tit = getTitulo();
		if (tit.length() > 0) {
			Font letra = FontFactory.getFont(getTipoLetraTitulo(),
					getTamanoLetraTitulo());
			Paragraph p = new Paragraph(tit, letra);
			p.setAlignment(getAlineacionTitulo());
			doc.add(p);
		}
	}

	/**
	 * Escribe el titulo del documento centrado en la pagina
	 * 
	 * @param doc
	 *            Documento PDF donde se va a adicionar el titulo
	 */
	protected void escribirEncabezadoNo1Page(Document doc, PaginaTabla pagina)
			throws Exception {
		String tit = getTitulo();
		if (tit.length() > 0) {
			Font letra = FontFactory.getFont(getTipoLetraTitulo(),
					getTamanoLetraTitulo());
			Paragraph p = new Paragraph(tit, letra);
			p.setAlignment(getAlineacionTitulo());
			doc.add(p);
		}
	}

	/**
	 * Escribe el pie de pagina del documento aleineado a la derecha
	 * 
	 * @param doc
	 *            Documento PDF donde se va a adicionar el titulo
	 */
	protected void escribirPie(Document doc, PaginaTabla pagina)
			throws Exception {
		String piePagina = getPie();
		try {
			if (piePagina.length() > 0) {
				Font letra = FontFactory.getFont(getTipoLetraTitulo(),
						getTamanoLetraTitulo());
				Paragraph p = new Paragraph(piePagina, letra);
				p.setAlignment(getAlineacionTitulo());
				doc.add(p);
			}
		} catch (Exception e) {
			throw e;
		}
	}

	protected int numHoja = 0;

	/**
	 * Llena el arreglo de bytes con el contenido de un documento PDF
	 * 
	 * @param doc
	 *            Documento PDF donde se va a adicionar el titulo
	 */
	public void generarPdf(OutputStream baos) throws Exception {
		Document doc = crearDocumento(baos);
		List paginas = paginar();
		Iterator ipaginas = paginas.iterator();
		boolean esPrimeraPagina = true;
		String valorRompimiento = null;
		while (ipaginas.hasNext()) {
			PaginaTabla pagina = (PaginaTabla) ipaginas.next();
			numHoja++;
			if (esPrimeraPagina) {
				esPrimeraPagina = false;
				escribirEncabezado(doc, pagina);
				escribirTabla(doc, pagina, baos);
				escribirPie(doc, pagina);
			} else {
				doc.newPage();
				if (valorRompimiento != pagina.getValorRompimiento()) {
					escribirEncabezado(doc, pagina);
				} else {
					escribirEncabezadoNo1Page(doc, pagina);
				}
				escribirTabla(doc, pagina, baos);
			}
			valorRompimiento = pagina.getValorRompimiento();
		}
		doc.close();
	}

	/**
	 * Recorre las lista de filas comparando el valor de rompimiento para hacer
	 * cambio de paginas
	 * 
	 * @return Lista de paginas cada pagina es tipo PaginaTabla
	 */
	protected List paginar() {
		List filas = getFilas();
		int tamanoTotal = filas.size();
		int colRompimiento = getCampoRompimiento();
		int nroFila = 0;
		List paginas = (List) new ArrayList();
		PaginaTabla pagina = new PaginaTabla();
		// Obtiene primera fila
		Hashtable fila = (Hashtable) filas.get(nroFila);
		nroFila++;
		Iterator it = fila.keySet().iterator();
		int x = 0;
		// String scolRompimiento="";
		Hashtable names = new Hashtable();
		names.put(new Integer(0), "Edificio");
		names.put(new Integer(4), "dependenciaRemite");
		// while (it.hasNext()) {
		// String key=it.next();
		// names.put(x, key);
		// // if(x==colRompimiento)
		// // scolRompimiento=key;
		// x++;
		//			
		// }

		if (colRompimiento >= 0 && colRompimiento < fila.size()) {
			// Si la columna de rompimiento es valido
//			String valor = (fila.get(names.get(new Integer(colRompimiento))))
//					.toString().trim();
			//0 piso
			//4 dependencia
			String colRompimientoStr="Destinatario";
			if(colRompimiento==0){
				colRompimientoStr="Piso";
			}
			String valor=fila.get(colRompimientoStr).toString();
			pagina.setValorRompimiento(valor);
			for (; nroFila < tamanoTotal; nroFila++) {
				fila = (Hashtable) filas.get(nroFila);
				String comparador = fila.get(colRompimientoStr).toString().trim();
				if (!valor.equalsIgnoreCase(comparador)) {
					pagina.setPosFinal(nroFila - 1);
					paginas.add(pagina);
					pagina = new PaginaTabla();
					pagina.setPosInicial(nroFila);
					pagina.setPosFinal(nroFila);
					valor = comparador;
					pagina.setValorRompimiento(comparador);
				}
			}
		}
		pagina.setPosFinal(tamanoTotal - 1);
		paginas.add(pagina);
		return paginas;
	}

	public String getValorRompimiento() {
		return valorRompimiento;
	}

	public void setValorRompimiento(String valorRompimiento) {
		this.valorRompimiento = valorRompimiento;
	}

	/**
	 * escribirTabla Adiciona al documento PDF un elemento tabla con el
	 * contenido de la lista filas
	 * 
	 * @param doc
	 *            Documento PDF al que se va a adcionar la tabla
	 */
	protected void escribirTabla(Document doc, PaginaTabla pagina,
			OutputStream baos) throws Exception {
		int numColumnas = getEncabezadosColumnas().size();
		/*
		 * int numColumnasReales = numColumnas; if (columnasOcultas != null) {
		 * numColumnasReales = numColumnas - columnasOcultas.length; }
		 */

		PdfPTable datatable = null;
		try {
			/** Definie estilos para la tabla */

			datatable = new PdfPTable(numColumnas - 1);
			// Data texto = null;
			String texto = null;
			Phrase valorCelda;
			// datatable.setDefaultCellBorderWidth(2);
			// datatable.setBorder(0);
			// datatable.setBorderColor(Color.GRAY);
			// datatable.setWidth(100f);
			// datatable.setVerticalAlignment(Element.ALIGN_MIDDLE);
			// datatable.setSpaceInsideCell(2);

			/** Llenar datos en tabla */
			Iterator iEncabezados = getEncabezadosColumnas().iterator();
			Font letraEncabezado = FontFactory.getFont(
					getTipoLetraEncabezados(), getTamanoLetraEncabezados());

			// datatable.setHorizontalAlignment(Element.ALIGN_CENTER);
			// datatable.setLastHeaderRow(0);
			Font letra = FontFactory.getFont(getTipoLetra(), getTamanoLetra());
			/** Llena lo titulos de las columnas */
			List ocultas = new ArrayList();
			for (int i = 0; i < numColumnas && iEncabezados.hasNext(); i++) {
				String valida = ((Data) iEncabezados.next()).getDisplayName();
				if (isOculta(valida)) {
					texto = valida;
					ocultas.add(new Integer(i));
					continue;
				}

				texto = valida;
				valorCelda = new Phrase(texto, letraEncabezado);
				// encabezados.add(texto);
				PdfPCell celda = new PdfPCell(valorCelda);
				datatable.addCell(celda);
			}// while encabezados
			// datatable.setLastHeaderRow(0);
			/** Llena las Celdas */
			List subFilas;
			/** Machetazo porque sublist no funciona bien con un solo elemento */
			if (pagina.getPosInicial() == pagina.getPosFinal()) {
				subFilas = new ArrayList();
				subFilas.add(getFilas().get(pagina.getPosInicial()));
			} else {
				subFilas = getFilas().subList(pagina.getPosInicial(),
						pagina.getPosFinal() + 1); // Se le suma porque es
				// exclusivo al final,
				// entonces para que no bote
				// la última fila.
			}
			Iterator iFilas = subFilas.iterator();
			boolean x = iFilas.hasNext();
			while (iFilas.hasNext()) {
				Object o = iFilas.next();
				if (o instanceof Hashtable) {
					Hashtable celdas = ((Hashtable) o);
					// Iterator namesColumns = celdas.keySet().iterator();
//					String[] columnNames = new String[] { "dependenciaRemite",
//							"numRad", "descripcionAnexos", "dependenciaRecibe",
//							"fechaRad", "" };
					String[] columnNames = new String[] { "Origen",
							"Radicado", "Anexos", "Destinatario",
							"FechaRadicado", "" };

					
					for (int i = 0; i < columnNames.length; i++) { // i <
						// numColumnas
						String valida = columnNames[i];
						if (isOculta(valida)) {
							texto = valida;
							continue;
						}
						// + if (namesColumns.hasNext()) {
						if (valida.length() > 0) {
							texto = valida;
							valorCelda = null;
							if (celdas.get(texto) instanceof Date) {
								String fecha = ((Date) celdas.get(texto))
										.toGMTString();
								valorCelda = new Phrase(fecha, letra);
							} else if (texto.length() > 0) {
								if (celdas.get(texto) != null)
									valorCelda = new Phrase(celdas.get(texto)
											.toString(), letra);
								else
									valorCelda = null;
							}
							if (valorCelda != null)
								datatable.addCell(valorCelda);

						} else {
							datatable.addCell("");
						}

					}

				}// for i
			}// while iFilas.hasNext()
			doc.add(datatable);
		} catch (Exception e) {
			datatable = null;
			throw e;
		}
	}

	/**
	 * Escribe todas las filas del la tabla
	 */
	protected void escribirTabla(Document doc, OutputStream baos)
			throws Exception {
		PaginaTabla pagina = new PaginaTabla();
		pagina.setPosInicial(0);
		pagina.setPosFinal(filas.size() - 1);
		escribirTabla(doc, pagina, baos);
	}

	/**
	 * Genera el archivo PDF con la tabla que recibe como una lista de filas
	 * 
	 * @param lasFilas
	 *            Una lista cuyos elementos son a su vez listas de valores de
	 *            tipo Data
	 * @param baos
	 *            Arreglo de bytes que para almacenar el documento PDF
	 */
	public void generarPdf(List lasFilas, OutputStream baos) throws Exception {
		setFilas(lasFilas);
		generarPdf(baos);
	}

	/**
	 * Obtiene el tipo de lentra para el llenado de la tabla
	 */
	public String getTipoLetra() {
		return tipoLetra;
	}

	/**
	 * Establece el tipo de letra para las celdas de la tabla
	 */
	public void setTipoLetra(String tipoLetra) {
		this.tipoLetra = tipoLetra;
	}

	/**
	 * Retorna el tamano de la letra para el llenado de las celdas
	 */
	public int getTamanoLetra() {
		return tamanoLetra;
	}

	/**
	 * Establece el tamano de la letra para el llenado de las celdas
	 */
	public void setTamanoLetra(int tamanoLetra) {
		this.tamanoLetra = tamanoLetra;
	}

	/**
	 * Retorna el tamano de la pagina
	 */
	public Rectangle getTamanoPagina() {
		Rectangle pagina;
		if (isRotarPagina()) {
			return tamanoPagina.rotate();
		} else {
			return tamanoPagina;
		}
	}

	/**
	 * Establece el tamano de la tabla
	 */
	public void setTamanoPagina(Rectangle tamanoPagina) {
		this.tamanoPagina = tamanoPagina;
	}

	public List getEncabezadosColumnas() {
		return encabezadosColumnas;
	}

	public void setEncabezadosColumnas(List encabezadosColumnas) {
		// encabezadosColumnas.remove(0);
		this.encabezadosColumnas = encabezadosColumnas;

	}

	public float getMargenIzquierda() {
		return margenIzquierda;
	}

	public void setMargenIzquierda(float margenIzquierda) {
		this.margenIzquierda = margenIzquierda;
	}

	public float getMargenDerecha() {
		return margenDerecha;
	}

	public void setMargenDerecha(float margenDerecha) {
		this.margenDerecha = margenDerecha;
	}

	public float getMargenSuperior() {
		return margenSuperior;
	}

	public void setMargenSuperior(float margenSuperior) {
		this.margenSuperior = margenSuperior;
	}

	public float getMargenInferior() {
		return margenInferior;
	}

	public void setMargenInferior(float margenInferior) {
		this.margenInferior = margenInferior;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getTipoLetraEncabezados() {
		return tipoLetraEncabezados;
	}

	public void setTipoLetraEncabezados(String tipoLetraEncabezados) {
		this.tipoLetraEncabezados = tipoLetraEncabezados;
	}

	public String getTipoLetraTitulo() {
		return tipoLetraTitulo;
	}

	public void setTipoLetraTitulo(String tipoLetraTitulo) {
		this.tipoLetraTitulo = tipoLetraTitulo;
	}

	public int getTamanoLetraEncabezados() {
		return tamanoLetraEncabezados;
	}

	public void setTamanoLetraEncabezados(int tamanoLetraEncabezados) {
		this.tamanoLetraEncabezados = tamanoLetraEncabezados;
	}

	public int getTamanoLetraTitulo() {
		return tamanoLetraTitulo;
	}

	public void setTamanoLetraTitulo(int tamanoLetraTitulo) {
		this.tamanoLetraTitulo = tamanoLetraTitulo;
	}

	public boolean isRotarPagina() {
		return rotarPagina;
	}

	public void setRotarPagina(boolean rotarPagina) {
		this.rotarPagina = rotarPagina;
	}

	public int getAlineacionTitulo() {
		return alineacionTitulo;
	}

	public void setAlineacionTitulo(int alineacionTitulo) {
		this.alineacionTitulo = alineacionTitulo;
	}

	public int getCampoRompimiento() {
		return campoRompimiento;
	}

	public void setCampoRompimiento(int campoRompimiento) {
		this.campoRompimiento = campoRompimiento;
	}

	public String getPie() {
		return pie;
	}

	public void setPie(String pie) {
		this.pie = pie;
	}

	public int[] getColumnasOcultas() {
		return columnasOcultas;
	}

	public void setColumnasOcultas(int[] columnasOcultas) {
		this.columnasOcultas = columnasOcultas;
	}

	private List columnasOcultasList = new ArrayList();

	public List getColumnasOcultasList() {

		return columnasOcultasList;
	}

	public void setColumnasOcultasList(List columnasOcultas) {
		this.columnasOcultasList = columnasOcultas;
	}

	/**
	 * Recibe un numero de columna y verifica si esta en la lista de ocultas
	 * 
	 * @param colnum
	 *            numero de la columna a evaluar
	 * @return true - si esta en la lista de ocultas, false - si no esta
	 */
	private boolean isOculta(int colnum) {
		boolean oculta = false;
		int[] ocultas = getColumnasOcultas();
		if (ocultas != null) {
			for (int i = 0; i < ocultas.length; i++) {
				if (ocultas[i] == colnum) {
					oculta = true;
					break;
				}
			}
		}
		return oculta;
	}

	private boolean isOculta(String columnName) {
		boolean oculta = false;

		if (getColumnasOcultasList() != null) {
			if (getColumnasOcultasList().contains(columnName)) {
				oculta = true;
			}

		}
		return oculta;
	}

}
