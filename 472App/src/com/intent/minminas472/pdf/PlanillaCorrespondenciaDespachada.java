/*
 * PlanillaCorrespondenciaRecibida.java
 *
 * Clase encargada de generar PDFs con con tablas como resultado
 * de consultas
 * @author  Juan F. Medina
 * @version 1.0
 * @see  import com.lowagie.text
 * @see com.intent.pdf.Tabla
 * fecha: 8/11/2005
 */

package com.intent.minminas472.pdf;

import java.awt.Color;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.intent.minminas472.utils.Constants;
import com.lowagie.text.Cell;
import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Table;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTemplate;

/**
 * 
 * @author Juan F. Medina
 */
public class PlanillaCorrespondenciaDespachada extends Tabla {

	/** Creates a new instance of PlanillaCorrespondenciaRecibida */
	public PlanillaCorrespondenciaDespachada() {
		super();
		setMargenIzquierda(10);
		setMargenDerecha(10);
		setMargenSuperior(10);
		setMargenInferior(10);
		setTamanoLetra(8);
		setTamanoLetraEncabezados(10);
		setTamanoLetraTitulo(9);
		crearEncabezadosColumna();
		setFecha(new Date());
		setNroPlanilla("0");
		// setTitulo("PLANILLA PARA CONSIGNACION\nENVIOS CON LICENCIA DE
		// CREDITO");
		setCodigoFormato("GD-AD-P-02-F-04");
		setVersionFormato("01");
		setFechaFormato("31/10/2005");
		cantRegistrosPagina = 100;
	}

	private java.util.Date fecha;

	private String nroPlanilla;

	private String nroContrato;

	private String codigoFormato;

	private String fechaFormato;

	private String versionFormato;

	private String tipoCorreo;

	private String urlLogo;

	private String urlFooter;

	private int[] agrupadores;

	private String ciudadSede;

	private String ciudadLocal;

	private String local;

	private String nacional;

	private int totalSobres;

	private int totalValor;

	private int cantRegistrosPagina;

	public void crearEncabezadosColumna() {
		List filas = (List) new ArrayList();
		Data unDato;

		/*
		 * unDato = new Data(); unDato.setDisplayName("Cantidad");
		 * filas.add(unDato);
		 * 
		 * unDato = new Data(); unDato.setDisplayName("Categoría Correo");
		 * filas.add(unDato);
		 * 
		 * unDato = new Data(); unDato.setDisplayName("Numero Registro");
		 * filas.add(unDato);
		 * 
		 * unDato = new Data(); unDato.setDisplayName("Destinatario");
		 * filas.add(unDato);
		 * 
		 * unDato = new Data(); unDato.setDisplayName("Pais, Dpto o Ciudad
		 * Destino"); filas.add(unDato);
		 * 
		 * unDato = new Data(); unDato.setDisplayName("Peso\n(gramos)");
		 * filas.add(unDato);
		 * 
		 * unDato = new Data(); unDato.setDisplayName("Valor\nportes");
		 * filas.add(unDato);
		 * 
		 * unDato = new Data(); unDato.setDisplayName("Valor\ncertif.");
		 * filas.add(unDato);
		 * 
		 * unDato = new Data(); unDato.setDisplayName("Valor\nasegurado");
		 * filas.add(unDato);
		 * 
		 * unDato = new Data(); unDato.setDisplayName("Tasa\nseguro");
		 * filas.add(unDato);
		 * 
		 * unDato = new Data(); unDato.setDisplayName("Valor\nreembolso");
		 * filas.add(unDato);
		 * 
		 * unDato = new Data(); unDato.setDisplayName("Aviso\nllegada");
		 * filas.add(unDato);
		 * 
		 * unDato = new Data(); unDato.setDisplayName("Servicios\nespeciales");
		 * filas.add(unDato);
		 * 
		 * unDato = new Data(); unDato.setDisplayName("Valor total\nporte
		 * tasa"); filas.add(unDato);
		 */

		unDato = new Data();
		unDato.setDisplayName("ORDINAL");
		filas.add(unDato);

		unDato = new Data();
		unDato.setDisplayName("DOCUMENTO");
		filas.add(unDato);
		unDato = new Data();
		unDato.setDisplayName("PAQUETERÍA");
		filas.add(unDato);
		unDato = new Data();
		unDato.setDisplayName("URBANO");
		filas.add(unDato);
		unDato = new Data();
		unDato.setDisplayName("NACIONAL");
		filas.add(unDato);
		unDato = new Data();
		unDato.setDisplayName("INTERNACIONAL");
		filas.add(unDato);
		unDato = new Data();
		unDato.setDisplayName("NOMBRE DESTINATARIO");
		filas.add(unDato);
		unDato = new Data();
		unDato.setDisplayName("DIRECCION DE DESTINO");
		filas.add(unDato);
		unDato = new Data();
		unDato.setDisplayName("CIUDAD DE DESTINO");
		filas.add(unDato);
		unDato = new Data();
		unDato.setDisplayName("DEPARTAMENTO / PAIS");
		filas.add(unDato);
		unDato = new Data();
		unDato.setDisplayName("PESO EN KG");
		filas.add(unDato);
		unDato = new Data();
		unDato.setDisplayName("VALOR DEL ENVIO");
		filas.add(unDato);
		// unDato = new Data();
		// unDato.setDisplayName("CANTIDAD");
		// filas.add(unDato);
		unDato = new Data();
		unDato
				.setDisplayName("VALOR DECLARADO (MINIMO $ 100,000 - MAXIMO $ 15,000,000)");
		filas.add(unDato);
		unDato = new Data();
		unDato.setDisplayName("VALOR DEL SEGURO (TASA 2%)");
		filas.add(unDato);
		unDato = new Data();
		unDato.setDisplayName("VALOR TOTAL DEL ENVIO");
		filas.add(unDato);
		unDato = new Data();
		unDato.setDisplayName("NUMERO DE SEGUIMIENTO 4-72");
		filas.add(unDato);
		unDato = new Data();
		unDato.setDisplayName("ESTADO DEL ENVIO");
		filas.add(unDato);
		// unDato = new Data();
		// unDato
		// .setDisplayName("NUMERO INTERNO DE RADICADO EXCLUSIVO DEL
		// CLIENTE***");
		// filas.add(unDato);
		setEncabezadosColumnas(filas);
	}

	public java.util.Date getFecha() {
		return fecha;
	}

	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * Encabezado 472
	 * 
	 * @param doc
	 */
	private void encabezado(Document doc) {
		Table datatable = null;
		Cell cell = null;
		// Tabla de 3 Columanas
		try {
			/*
			 * datatable = new Table(3);
			 * 
			 * datatable.setDefaultCellBorderWidth(1); Font letraEncabezado =
			 * FontFactory.getFont( getTipoLetraEncabezados(),
			 * getTamanoLetraEncabezados()); datatable.setWidth(100f);
			 * datatable.setWidths(new float[] { 14.6f, 60.4f, 25 });
			 * datatable.setBorderWidth(1f);
			 * datatable.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
			 * datatable.setSpaceInsideCell(2f);
			 * datatable.setDefaultHorizontalAlignment(Table.ALIGN_LEFT); //
			 * Columna izquierda Logo cell = new Cell((Element)
			 * Image.getInstance(getUrlLogo()));
			 * cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			 * datatable.addCell(cell);
			 * datatable.setDefaultHorizontalAlignment(Table.ALIGN_CENTER); //
			 * Columna de centro titulo y rempimiento Phrase p = new Phrase(
			 * "PLANILLAS PARA CORREO " + getTipoCorreo() + "\nPLANILLA PARA
			 * CONSIGNACION\nENVIOS CON LICENCIA DE CREDITO", letraEncabezado);
			 * datatable.addCell(p); // Coloca los datos de la planilla Table
			 * subtable2 = new Table(1); subtable2.setBorderWidth(1f);
			 * subtable2.setWidth(100f);
			 * subtable2.setDefaultVerticalAlignment(Table.ALIGN_TOP);
			 * subtable2.setSpaceInsideCell(2f); subtable2.addCell(new Phrase("
			 * PLANILLA NUMERO: " + getNroPlanilla(), letraEncabezado)); //
			 * subtable2.addCell(new Phrase("Versión: " + getVersionFormato(),
			 * // letraEncabezado)); // subtable2.addCell(new Phrase("Código: "
			 * + getCodigoFormato(), // letraEncabezado)); subtable2.addCell(new
			 * Phrase(" CIUDAD: " + getCiudadSede(), letraEncabezado));
			 * subtable2.addCell(new Phrase(" FECHA: " + getFechaFormato(),
			 * letraEncabezado)); cell = new Cell(new Phrase("CONTRATO " +
			 * getNroContrato(), letraEncabezado));
			 * cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			 * subtable2.addCell(cell); datatable.addCell(new Cell((Element)
			 * subtable2)); doc.add(datatable);
			 */
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private PdfPCell getPdfPCell(String message, Color bckColor, int sizeFont,
			Color fontColor, int colspan) {
		Phrase phrase = new Phrase(message, FontFactory.getFont(
				FontFactory.TIMES_ROMAN, sizeFont + 2, Font.NORMAL, fontColor));

		PdfPCell cell = new PdfPCell(phrase);
		if (bckColor.equals(new Color(0, 102, 204)))
			cell.setBorderColor(Color.WHITE);
		else
			cell.setBorderColor(new Color(0, 102, 204));
		cell.setBackgroundColor(bckColor);
		cell.setHorizontalAlignment(Element.ALIGN_CENTER);
		cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
		cell.setColspan(colspan);
		// cell.
		// cell.set
		return cell;
	}

	/**
	 * Encabezado 472
	 * 
	 * @param doc
	 */
	private void encabezado472(Document doc) {
		PdfPTable datatable = null;

		// Tabla de 3 Columanas
		try {
			datatable = new PdfPTable(4);
			// datatable.setDefaultCellBorderWidth(1);
			// datatable.setTotalWidth(100f);
			datatable.setWidths(new float[] { 7.8f, 49.0f, 17.6f, 25.4f });
			datatable.setWidthPercentage(90);
			/*
			 * datatable.setWidthPercentage(new float[] { 7.8f, 49.0f, 17.6f,
			 * 25.4f }, new Rectangle(PageSize.LEGAL.rotate()));
			 */
			// datatable.setWidths(new float[] { 14.6f, 60.4f, 25 });
			// datatable.setBorderWidth(1f);
			// datatable
			datatable.setHorizontalAlignment(Element.ALIGN_LEFT);

			// datatable.setSpaceInsideCell(2f);
			// datatable.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);

			// Columna izquierda Logo
			PdfPCell cell = null;
			cell = new PdfPCell(Image.getInstance(getUrlLogo()));
			cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			cell.setVerticalAlignment(Cell.ALIGN_MIDDLE);
			cell.setBorderColor(new Color(0, 102, 204));
			datatable.addCell(cell);

			datatable.setHorizontalAlignment(Element.ALIGN_CENTER);

			// columna 2 planilla imposicion envios
			PdfPTable subtableC2 = new PdfPTable(8);
			subtableC2.setWidths(new float[] { 17.76f, 17.76f, 13.16f, 11.84f,
					11.84f, 3.95f, 7.89f, 3.95f });

			// subtableC2.setBorderWidth(1f);
			// subtableC2.setWidth(100f);

			subtableC2.setHorizontalAlignment(Element.ALIGN_CENTER);
			// subtableC2.setSpaceInsideCell(2f);
			subtableC2.addCell(getPdfPCell(
					"PLANILLA PARA LA IMPOSICION DE ENVIOS ", new Color(0, 102,
							204), 8, Color.WHITE, 8));

			subtableC2.addCell(getPdfPCell("NOMBRE O RAZON SOCIAL", new Color(
					0, 102, 204), 8, Color.WHITE, 2));
			subtableC2.addCell(getPdfPCell(Constants
					.getProperty("plantillas.nombreorazonsocial"), Color.WHITE,
					8, new Color(0, 102, 204), 6));

			// subtableC2.addCell(getPdfPCell(
			// "DIRECCION DE LA ENTIDAD \n (Retorno de planillas)",
			// new Color(0, 102, 204), 8, Color.WHITE, 2));
			// subtableC2.addCell(getPdfPCell("", Color.WHITE, 8, Color.WHITE,
			// 6));

			subtableC2.addCell(getPdfPCell("NUMERO DE CONTRATO", new Color(0,
					102, 204), 8, Color.WHITE, 2));
			subtableC2
					.addCell(getPdfPCell(
							Constants
									.getProperty("application_planillas_post_express_numero_contrato"),
							Color.WHITE, 8, new Color(0, 102, 204), 6));

			subtableC2.addCell(getPdfPCell("NIT", new Color(0, 102, 204), 8,
					Color.WHITE, 2));
			subtableC2.addCell(getPdfPCell(Constants
					.getProperty("plantillas.nit"), Color.WHITE, 8, new Color(
					0, 102, 204), 6));
			subtableC2.addCell(getPdfPCell("FECHA DE IMPOSICION", new Color(0,
					102, 204), 8, Color.WHITE, 2));
			subtableC2.addCell(getPdfPCell(new SimpleDateFormat("dd MM yyyy")
					.format(new Date()), Color.WHITE, 8,
					new Color(0, 102, 204), 2));
			subtableC2.addCell(getPdfPCell("FORMA DE PAGO (Marque X)",
					new Color(0, 102, 204), 8, Color.WHITE, 4));

			subtableC2.addCell(getPdfPCell("CIUDAD DE IMPOSICION", new Color(0,
					102, 204), 8, Color.WHITE, 2));
			subtableC2.addCell(getPdfPCell(Constants
					.getProperty("plantillas.ciudadimposicion"), Color.WHITE,
					7, new Color(0, 102, 204), 2));
			subtableC2.addCell(getPdfPCell("CREDITO", new Color(204, 204, 255),
					5, new Color(0, 102, 204), 1));
			subtableC2.addCell(getPdfPCell(" X ", Color.WHITE, 7, new Color(0,
					102, 204), 1));
			subtableC2.addCell(getPdfPCell("FRANQUICIA", new Color(204, 204,
					255), 2, new Color(0, 102, 204), 1));
			subtableC2.addCell(getPdfPCell("", Color.WHITE, 8, Color.WHITE, 1));
			subtableC2.setWidthPercentage(50);
			PdfPCell cell2 = new PdfPCell(subtableC2);
			cell2.setBorderColor(new Color(0, 102, 204));
			datatable.addCell(cell2);

			//
			PdfPTable subtableC3 = new PdfPTable(5);
			subtableC3
					.setWidths(new float[] { 4.6f, 6.80f, 4.6f, 4.6f, 4.54f });
			// subtableC2.setBorderWidth(1f);
			// subtableC2.setWidth(100f);
			subtableC3.setHorizontalAlignment(Element.ALIGN_CENTER);
			// subtableC2.setSpaceInsideCell(2f);
			subtableC3.addCell(getPdfPCell(
					"TIPOS DE SERVICIO (Marque con una 'X')", new Color(0, 102,
							204), 8, Color.WHITE, 5));

			subtableC3.addCell(getPdfPCell("Normal", new Color(204, 204, 255),
					5, new Color(0, 102, 204), 1));
			subtableC3.addCell(getPdfPCell("Certificado", new Color(204, 204,
					255), 5, new Color(0, 102, 204), 1));
			subtableC3.addCell(getPdfPCell("Post \nExpress", new Color(204,
					204, 255), 5, new Color(0, 102, 204), 1));
			subtableC3.addCell(getPdfPCell("Sacas M", new Color(204, 204, 255),
					7, new Color(0, 102, 204), 1));
			subtableC3.addCell(getPdfPCell("Correo \nMasivo", new Color(204,
					204, 255), 5, new Color(0, 102, 204), 1));

			subtableC3.addCell(getPdfPCell(isPlanillaTipo(1), Color.WHITE, 8,
					new Color(0, 102, 204), 1));
			subtableC3.addCell(getPdfPCell(isPlanillaTipo(2), Color.WHITE, 8,
					new Color(0, 102, 204), 1));
			subtableC3.addCell(getPdfPCell(isPlanillaTipo(3), Color.WHITE, 8,
					new Color(0, 102, 204), 1));
			subtableC3.addCell(getPdfPCell(isPlanillaTipo(4), Color.WHITE, 8,
					new Color(0, 102, 204), 1));
			subtableC3.addCell(getPdfPCell(isPlanillaTipo(5), Color.WHITE, 8,
					new Color(0, 102, 204), 1));

			subtableC3.addCell(getPdfPCell("EMS", new Color(204, 204, 255), 7,
					new Color(0, 102, 204), 1));
			subtableC3.addCell(getPdfPCell("Prioritario", new Color(204, 204,
					255), 7, new Color(0, 102, 204), 1));
			subtableC3.addCell(getPdfPCell("Correo \nDirigido", new Color(204,
					204, 255), 5, new Color(0, 102, 204), 1));
			subtableC3.addCell(getPdfPCell("Noti \nExpress", new Color(204,
					204, 255), 5, new Color(0, 102, 204), 1));
			subtableC3.addCell(getPdfPCell("Al dia", new Color(204, 204, 255),
					7, new Color(0, 102, 204), 1));

			subtableC3.addCell(getPdfPCell(isPlanillaTipo(6), Color.WHITE, 8,
					new Color(0, 102, 204), 1));

			subtableC3.addCell(getPdfPCell(isPlanillaTipo(7), Color.WHITE, 8,
					new Color(0, 102, 204), 1));
			subtableC3.addCell(getPdfPCell(isPlanillaTipo(8), Color.WHITE, 8,
					new Color(0, 102, 204), 1));
			subtableC3.addCell(getPdfPCell(isPlanillaTipo(9), Color.WHITE, 8,
					new Color(0, 102, 204), 1));
			subtableC3.addCell(getPdfPCell(isPlanillaTipo(10), Color.WHITE, 8,
					new Color(0, 102, 204), 1));

			subtableC3.setWidthPercentage(18);
			PdfPCell cell3 = new PdfPCell(subtableC3);
			cell3.setBorderColor(new Color(0, 102, 204));
			datatable.addCell(cell3);

			PdfPTable subtableC4 = new PdfPTable(7);
			subtableC4.setWidths(new float[] { 1.96f, 3.53f, 7.45f, 2.35f,
					3.14f, 2.35f, 5.10f });
			// subtableC2.setBorderWidth(1f);
			// subtableC2.setWidth(100f);
			subtableC4.setHorizontalAlignment(Element.ALIGN_CENTER);
			// subtableC2.setSpaceInsideCell(2f);
			subtableC4.addCell(getPdfPCell("CODIGO DE IDENTIFICACION",
					new Color(0, 102, 204), 8, Color.WHITE, 5));
			subtableC4.addCell(getPdfPCell(Constants
					.getProperty("plantillas.codigoidentificacion"), new Color(
					204, 204, 255), 8, new Color(0, 102, 204), 2));

			subtableC4.addCell(getPdfPCell(
					"RELACION DE RANGO DE GUIAS REMISION", new Color(0, 102,
							204), 7, Color.WHITE, 7));

			subtableC4.addCell(getPdfPCell("DEL", new Color(0, 102, 204), 4,
					Color.WHITE, 1));
			subtableC4.addCell(getPdfPCell("", Color.WHITE, 8, Color.WHITE, 2));
			subtableC4.addCell(getPdfPCell("HASTA\n", new Color(0, 102, 204),
					4, Color.WHITE, 1));
			subtableC4.addCell(getPdfPCell("", Color.WHITE, 8, Color.WHITE, 3));

			subtableC4.addCell(getPdfPCell(
					"SALTOS DE CONSECUTIVO DEL \nRANGO DE GUIAS REMISIÓN	",
					new Color(0, 102, 204), 2, Color.WHITE, 2));
			subtableC4.addCell(getPdfPCell("", Color.WHITE, 8, Color.WHITE, 5));

			subtableC4.addCell(getPdfPCell("NÚMERO DE PLANILLA", new Color(0,
					102, 204), 4, Color.WHITE, 2));
			subtableC4.addCell(getPdfPCell(nroPlanilla + "\n", Color.WHITE, 8,
					new Color(0, 102, 204), 1));
			subtableC4.addCell(getPdfPCell("HOJA", new Color(0, 102, 204), 4,
					Color.WHITE, 1));

			subtableC4.addCell(getPdfPCell("1", Color.WHITE, 8, new Color(0,
					102, 204), 1));
			subtableC4.addCell(getPdfPCell("DE", new Color(0, 102, 204), 6,
					Color.WHITE, 1));

			subtableC4.addCell(getPdfPCell(Integer.toString(paginar().size()),
					Color.WHITE, 8, new Color(0, 102, 204), 1));
			subtableC4.setWidthPercentage(25);
			PdfPCell cell4 = new PdfPCell(subtableC4);
			cell4.setBorderColor(new Color(0, 102, 204));
			datatable.addCell(cell4);

			/*
			 * // Columna de centro titulo y rempimiento Phrase p = new Phrase(
			 * "PLANILLAS PARA CORREO " + getTipoCorreo() + "\nPLANILLA PARA
			 * CONSIGNACION\nENVIOS CON LICENCIA DE CREDITO", letraEncabezado);
			 * datatable.addCell(p); // Coloca los datos de la planilla
			 * PdfPTable subtable2 = new PdfPTable(1); //
			 * subtable2.setBorderWidth(1f); // subtable2.setWidth(100f); //
			 * subtable2.setDefaultVerticalAlignment(Table.ALIGN_TOP); //
			 * subtable2.setSpaceInsideCell(2f); subtable2.addCell(new Phrase("
			 * PLANILLA NUMERO: " + getNroPlanilla(), letraEncabezado)); //
			 * subtable2.addCell(new Phrase("Versión: " + getVersionFormato(),
			 * // letraEncabezado)); // subtable2.addCell(new Phrase("Código: "
			 * + getCodigoFormato(), // letraEncabezado)); subtable2.addCell(new
			 * Phrase(" CIUDAD: " + getCiudadSede(), letraEncabezado));
			 * subtable2.addCell(new Phrase(" FECHA: " + getFechaFormato(),
			 * letraEncabezado)); cell = new PdfPCell(new Phrase("CONTRATO " +
			 * getNroContrato(), letraEncabezado));
			 * cell.setHorizontalAlignment(Cell.ALIGN_CENTER);
			 * subtable2.addCell(cell); datatable.addCell(subtable2);
			 */
			doc.add(datatable);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * Encabezado 472
	 * 
	 * @param doc
	 */
	private void encabezado472No1Page(Document doc) {
		PdfPTable datatable = null;

		// Tabla de 3 Columanas
		try {
			datatable = new PdfPTable(12);
			datatable.setWidths(new float[] { 14.45f, 19.92f, 16.41f, 3.52f,
					3.52f, 3.52f, 10.94f, 12.50f, 2.73f, 3.52f, 1.95f, 4.69f });
			datatable.setWidthPercentage(90);
			datatable.setHorizontalAlignment(Element.ALIGN_CENTER);

			datatable.addCell(getPdfPCell("NOMBRE O RAZON SOCIAL ", new Color(
					0, 102, 204), 8, Color.WHITE, 1));

			datatable.addCell(getPdfPCell(Constants
					.getProperty("plantillas.nombreorazonsocial"), Color.WHITE,
					8, new Color(0, 102, 204), 1));

			datatable.addCell(getPdfPCell("FECHA DE IMPOSICION", new Color(0,
					102, 204), 8, Color.WHITE, 1));
			datatable.addCell(getPdfPCell(new SimpleDateFormat("dd")
					.format(new Date()), Color.WHITE, 8,
					new Color(0, 102, 204), 1));
			datatable.addCell(getPdfPCell(new SimpleDateFormat("MM")
					.format(new Date()), Color.WHITE, 8,
					new Color(0, 102, 204), 1));
			datatable.addCell(getPdfPCell(new SimpleDateFormat("yyyy")
					.format(new Date()), Color.WHITE, 8,
					new Color(0, 102, 204), 1));
			datatable.addCell(getPdfPCell("NUMERO CONTRATO", new Color(0, 102,
					204), 8, Color.WHITE, 1));
			datatable
					.addCell(getPdfPCell(
							Constants
									.getProperty("application_planillas_post_express_numero_contrato"),
							Color.WHITE, 8, new Color(0, 102, 204), 1));
			datatable.addCell(getPdfPCell("HOJA", new Color(0, 102, 204), 6,
					Color.WHITE, 1));

			datatable.addCell(getPdfPCell(Integer.toString(numHoja),
					Color.WHITE, 8, new Color(0, 102, 204), 1));
			datatable.addCell(getPdfPCell("DE", new Color(0, 102, 204), 6,
					Color.WHITE, 1));
			datatable.addCell(getPdfPCell(Integer.toString(paginar().size()),
					Color.WHITE, 8, new Color(0, 102, 204), 1));
			doc.add(datatable);
		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	/**
	 * Busca el tipo de correo que corresponda 1 a 12
	 * 
	 * @param i
	 * @return
	 */
	private String isPlanillaTipo(int i) {
		Hashtable celdas = (Hashtable) filas.get(0);

		String tipoCorreo = celdas.get("TipoDeCorreo").toString();
		tipoCorreo = tipoCorreo.trim().toUpperCase();
		String retorno = "\n\n";
		switch (i) {
		case 1:// normal
			if (tipoCorreo.indexOf("NORMAL") >= 0)
				retorno = "X";
			break;
		case 2:// CERTIFICADO
			if (tipoCorreo.indexOf("CERTIFICADO") >= 0)
				retorno = "X";
			break;
		case 3:// normal
			if (tipoCorreo.indexOf("POSTEXPRESS") >= 0)
				retorno = "X";
			break;
		case 4:// normal
			if (tipoCorreo.indexOf("SACASM") >= 0)
				retorno = "X";
			break;
		case 5:// normal
			if (tipoCorreo.indexOf("CORREOMASIVO") >= 0)
				retorno = "X";
			break;
		case 6:// normal
			if (tipoCorreo.indexOf("EMS") >= 0)
				retorno = "X";
			break;
		case 7:// normal
			if (tipoCorreo.indexOf("PRIORITARIO") >= 0)
				retorno = "X";
			break;
		case 8:// normal
			if (tipoCorreo.indexOf("CORREODIRIGIDO") >= 0)
				retorno = "X";
			break;
		case 9:// normal
			if (tipoCorreo.indexOf("NOTIEXPRESS") >= 0)
				retorno = "X";
			break;
		case 10:// normal
			if (tipoCorreo.indexOf("ALDIA") >= 0)
				retorno = "X";
			break;
		default:
			break;
		}

		return retorno;
	}

	/**
	 * Sobreescribe el metodo escribir en cabezado de la clase Tabla
	 */
	protected void escribirEncabezado(Document doc, PaginaTabla pagina)
			throws Exception {
		Table datatable = null;

		try {
			encabezado472(doc);
			// encabezado(doc);

		} catch (Exception e) {
			datatable = null;
			e.printStackTrace();
			throw e;
		}

	}

	/**
	 * Sobreescribe el metodo escribir en cabezado de la clase Tabla
	 */
	protected void escribirEncabezadoNo1Page(Document doc, PaginaTabla pagina)
			throws Exception {
		Table datatable = null;

		try {
			encabezado472No1Page(doc);
			// encabezado(doc);

		} catch (Exception e) {
			datatable = null;
			e.printStackTrace();
			throw e;
		}

	}

	public String getNroPlanilla() {
		return nroPlanilla;
	}

	public void setNroPlanilla(String nroPlanilla) {
		this.nroPlanilla = nroPlanilla;
	}

	public String getUrlLogo() {
		return urlLogo;
	}

	public void setUrlLogo(String urlLogo) {
		this.urlLogo = urlLogo;
	}

	/**
	 * Sobreescribe el metodo escribir en cabezado de la clase Tabla
	 */
	protected void escribirPie(Document doc, PaginaTabla pagina)
			throws Exception {
		PdfPTable datatable = null;
		try {
			calculaTotales();
			doc.add(new Phrase(" "));
			datatable = new PdfPTable(4);
			datatable.setWidths(new float[] { 33.6f, 23.05f, 21.48f, 21.48f, });
			datatable.setWidthPercentage(90);
			PdfPTable datatable2 = new PdfPTable(5);
			datatable2.setWidths(new float[] { 5.47f, 7.81f, 5.47f, 5.47f,
					5.47f });

			datatable2.addCell(getPdfPCell("OFICINA", new Color(0, 102, 204),
					9, Color.WHITE, 5));
			datatable2.addCell(getPdfPCell(" OFICINA DE IMPOSICION:",
					new Color(204, 204, 255), 7, new Color(0, 102, 204), 2));
			datatable2.addCell(getPdfPCell(" \n\n", Color.WHITE, 6, new Color(
					204, 204, 255), 3));
			datatable2.addCell(getPdfPCell(" ", new Color(204, 204, 255), 7,
					new Color(0, 102, 204), 2));
			datatable2.addCell(getPdfPCell(
					" NOMBRE Y SELLO DE LA OFICINA DE 472", Color.WHITE, 6,
					new Color(204, 204, 255), 3));
			datatable2.addCell(getPdfPCell("RELIQUIDACION DE LA PLANILLA",
					new Color(0, 102, 204), 9, Color.WHITE, 5));
			datatable2.addCell(getPdfPCell("Nª TOTAL DE ENVÍOS", new Color(204,
					204, 255), 6, new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell(
					"VALOR  TOTAL DE LOS ENVÍOS (SIN VALOR DECLARADO)",
					new Color(204, 204, 255), 6, new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell("VALOR TOTAL DECLARADO", new Color(
					204, 204, 255), 6, new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell("VALOR TOTAL SEGURO (TASA 2%)",
					new Color(204, 204, 255), 6, new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell("VALOR TOTAL DE LOS ENVIOS",
					new Color(204, 204, 255), 6, new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell(Integer.toString(filas.size()),
					Color.WHITE, 6, new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell(Float.toString(this.tvalor),
					Color.WHITE, 6, new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell("0", Color.WHITE, 6, new Color(0,
					102, 204), 1));
			datatable2.addCell(getPdfPCell(
					Float.toString(this.tvalorSeguro2Px), Color.WHITE, 6,
					new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell(Float.toString(this.tvalorEnvios),
					Color.WHITE, 6, new Color(0, 102, 204), 1));

			PdfPCell cell = new PdfPCell(datatable2);
			cell.setBorderColor(new Color(0, 102, 204));
			datatable.addCell(cell);

			datatable2 = new PdfPTable(2);
			datatable2.setWidths(new float[] { 7.81f, 12.5f });
			datatable2.addCell(getPdfPCell("CLIENTE", new Color(0, 102, 204),
					9, Color.WHITE, 2));
			datatable2.addCell(getPdfPCell("Nombre completo del impostor",
					new Color(204, 204, 255), 6, new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell(usuario + " \n\n", Color.WHITE, 6,
					new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell("Firma del impostor", new Color(204,
					204, 255), 6, new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell(" \n\n", Color.WHITE, 6, new Color(
					204, 204, 255), 1));
			datatable2.addCell(getPdfPCell("Numero de identificacion o Nit",
					new Color(204, 204, 255), 6, new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell(Constants
					.getProperty("plantillas.nit")
					+ "\n\n", Color.WHITE, 6, new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell("Telefono",
					new Color(204, 204, 255), 6, new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell("", Color.WHITE, 5, Color.WHITE, 1));
			cell = new PdfPCell(datatable2);
			cell.setBorderColor(new Color(0, 102, 204));
			datatable.addCell(cell);

			datatable2 = new PdfPTable(2);
			datatable2.setWidths(new float[] { 7.03f, 14.45f });
			datatable2.addCell(getPdfPCell("TRANSPORTISTA", new Color(0, 102,
					204), 9, Color.WHITE, 2));
			datatable2.addCell(getPdfPCell("Nombre completo del transportista",
					new Color(204, 204, 255), 6, new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell(" \n\n", Color.WHITE, 6, new Color(
					204, 204, 255), 1));
			datatable2.addCell(getPdfPCell("Firma del transportista",
					new Color(204, 204, 255), 6, new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell(" \n\n", Color.WHITE, 6, new Color(
					204, 204, 255), 1));
			datatable2.addCell(getPdfPCell("Numero de identificacion:",
					new Color(204, 204, 255), 6, new Color(0, 102, 204), 1));

			datatable2.addCell(getPdfPCell("\n\n", Color.WHITE, 5, Color.WHITE,
					1));
			datatable2.addCell(getPdfPCell("Fecha: ", Color.WHITE, 6,
					new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell("Hora", Color.WHITE, 6, new Color(0,
					102, 204), 1));
			cell = new PdfPCell(datatable2);
			cell.setBorderColor(new Color(0, 102, 204));
			datatable.addCell(cell);

			datatable2 = new PdfPTable(2);
			datatable2.setWidths(new float[] { 8.59f, 13.28f });
			datatable2.addCell(getPdfPCell("OFICINA", new Color(0, 102, 204),
					9, Color.WHITE, 2));
			datatable2.addCell(getPdfPCell(
					"Nombre completo de la persona de admision", new Color(204,
							204, 255), 6, new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell(" \n\n", Color.WHITE, 6, new Color(
					204, 204, 255), 1));
			datatable2.addCell(getPdfPCell("Firma de la persona de admision",
					new Color(204, 204, 255), 6, new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell(" \n\n", Color.WHITE, 6, new Color(
					204, 204, 255), 1));
			datatable2.addCell(getPdfPCell("Numero de identificacion",
					new Color(204, 204, 255), 5, new Color(0, 102, 204), 1));
			datatable2.addCell(getPdfPCell("\n\n", Color.WHITE, 6, Color.WHITE,
					1));
			datatable2.addCell(getPdfPCell("Fecha", Color.WHITE, 6, new Color(
					0, 102, 204), 1));
			datatable2.addCell(getPdfPCell("Hora", Color.WHITE, 6, new Color(0,
					102, 204), 1));
			cell = new PdfPCell(datatable2);
			cell.setBorderColor(new Color(0, 102, 204));
			datatable.addCell(cell);

			/*
			 * datatable .addCell(getPdfPCell( "OBSERVACIONES (Exclusivo 4-72 LA
			 * RED POSTAL DE \n COLOMBIA)", new Color(0, 102, 204), 10,
			 * Color.WHITE, 8)); datatable.addCell(getPdfPCell("", Color.WHITE,
			 * 9, Color.WHITE, 11));
			 */

			doc.add(datatable);
		} catch (Exception e) {
			throw e;
		}
	}

	/**
	 * Add jvargas 19/02/2009 Calcula los valores totales, para la primera
	 * ventana.
	 * 
	 */
	private void calculaTotales() {
		tvalor = 0;
		tvalorEnvios = 0;
		tvalorSeguro2Px = 0;
		for (int i = 0; i < filas.size(); i++) {
			Hashtable celdas = (Hashtable) filas.get(i);
			float peso = Float.parseFloat((celdas.get("Peso")).toString());
			float valor = Float.parseFloat((celdas.get("Precio")).toString());
			tvalorSeguro2Px += (getTasa2Px(Float.toString(peso), Float
					.toString(valor)));
			tvalor += valor;
		}
		tvalorEnvios = tvalor + tvalorSeguro2Px;

	}

	public String getNroContrato() {
		return nroContrato;
	}

	public void setNroContrato(String nroContrato) {
		this.nroContrato = nroContrato;
	}

	public String getCodigoFormato() {
		return codigoFormato;
	}

	public void setCodigoFormato(String codigoFormato) {
		this.codigoFormato = codigoFormato;
	}

	public String getFechaFormato() {
		return fechaFormato;
	}

	public void setFechaFormato(String fechaFormato) {
		this.fechaFormato = fechaFormato;
	}

	public String getTipoCorreo() {
		return tipoCorreo;
	}

	public void setTipoCorreo(String tipoCorreo) {
		this.tipoCorreo = tipoCorreo;
	}

	public String getVersionFormato() {
		return versionFormato;
	}

	public void setVersionFormato(String versionFormato) {
		this.versionFormato = versionFormato;
	}

	float tvalor = 0;

	float tvalorEnvios = 0;

	float tvalorSeguro2Px = 0;

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
			datatable = new PdfPTable(numColumnas);
			/*
			 * float[] anchosCuerpo = new float[] { 1.71f, 1.41f, 1.41f, 1.41f,
			 * 1.41f, 1.41f, 15.32f, 11.69f, 8.06f, 7.26f, 3.63f, 8.47f, 8.06f,
			 * 6.45f, 8.47f, 8.06f, 5.65f };
			 */
			float[] anchosCuerpo = new float[] { 1.71f, 1.41f, 1.41f, 1.41f,
					1.41f, 1.41f, 22f, 17f, 8.06f, 7.26f, 3.63f, 5.65f, 5.65f,
					5.65f, 5.65f, 5.65f, 5.65f };

			datatable.setWidths(anchosCuerpo);
			datatable.setWidthPercentage(90);
			// datatable.setDefaultCellBorderWidth(2);
			// datatable.setBorder(1);
			// datatable.setBorderColor(Color.GRAY);
			// datatable.setWidth(100f);
			// datatable.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
			// datatable.setSpaceInsideCell(2);
			Phrase valorCelda;
			Data texto = null;
			int tsobres = 0;
			float tvalor = 0;
			float tvalorEnvios = 0;
			float tvalorSeguro2Px = 0;
			/** Llenar datos en tabla */
			Iterator iEncabezados = getEncabezadosColumnas().iterator();
			Font letraTitulo = FontFactory.getFont(getTipoLetraTitulo(),
					getTamanoLetraTitulo());
			Font letra = FontFactory.getFont(getTipoLetra(), getTamanoLetra());
			datatable.setHorizontalAlignment(Element.ALIGN_CENTER);
			// datatable.setLastHeaderRow(0);
			/** Llena lo titulos de las columnas */

			// PdfWriter writer = PdfWriter.getInstance(doc, baos);
			// doc.open();
			BaseFont bf = BaseFont.createFont("Helvetica", "winansi", false);

			for (int i = 0; i < numColumnas && iEncabezados.hasNext(); i++) {
				texto = (Data) iEncabezados.next();
				int size = 4;
				Color backColor = new Color(0, 102, 204);
				Color fontColor = Color.WHITE;

				if (i == 0) {
					backColor = new Color(204, 204, 255);
					fontColor = new Color(0, 102, 204);

				}
				PdfPCell cell = null;
				if (i <= 5) {
					size = 4;
					String text = texto.getDisplayName();
					// float
					// size = 16;
					float width = bf.getWidthPoint(text, size);
					PdfTemplate template = writer.getDirectContent()
							.createTemplate(20, 20);
					template.beginText();
					// template.setRGBColorFillF(1, 1, 1);
					template.setColorFill(fontColor);
					template.setFontAndSize(bf, size);
					// template.setTextMatrix(0, 2);
					template.showText(text);
					template.endText();
					template.setWidth(width);
					template.setHeight(size + 2);
					// make an Image object from the template
					Image img = Image.getInstance(template);
					img.setRotationDegrees(90);
					cell = new PdfPCell(img);
					// cell.setPadding(4);
					cell.setBackgroundColor(backColor);
					cell.setHorizontalAlignment(Element.ALIGN_CENTER);
					cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
					if (backColor.equals(new Color(0, 102, 204)))
						cell.setBorderColor(Color.WHITE);
					else
						cell.setBorderColor(new Color(0, 102, 204));

				} else {
					cell = getPdfPCell(texto.getDisplayName(), backColor, size,
							fontColor, 1);
				}

				datatable.addCell(cell);
			}// while encabezados
			// datatable.setLastHeaderRow(0);
			/** Llena las Celdas */
			int i = 1;
			List filas = getFilas();
			for (int k = pagina.getPosInicial(); k < pagina.getPosFinal(); k++) {
				Hashtable celdas = (Hashtable) filas.get(k);
				datatable.addCell(getPdfPCell(String.valueOf(k + 1),
						Color.WHITE, 7, new Color(0, 102, 204), 1));
				datatable
						.addCell(getPdfPCell(isDocumento((celdas.get("Peso"))
								.toString()), Color.WHITE, 7, new Color(0, 102,
								204), 1));
				datatable
						.addCell(getPdfPCell(isPaquete((celdas.get("Peso"))
								.toString()), Color.WHITE, 7, new Color(0, 102,
								204), 1));
				datatable.addCell(getPdfPCell(isLocal((celdas
						.get("TipoDeCorreo")).toString()), Color.WHITE, 7,
						new Color(0, 102, 204), 1));
				datatable.addCell(getPdfPCell(isNacional((celdas
						.get("TipoDeCorreo")).toString()), Color.WHITE, 7,
						new Color(0, 102, 204), 1));
				datatable.addCell(getPdfPCell(isInternacional((celdas
						.get("TipoDeCorreo")).toString()), Color.WHITE, 7,
						new Color(0, 102, 204), 1));

				datatable
						.addCell(getPdfPCell(
								(celdas.get("Destinatario")).toString(),
								Color.WHITE, 5, new Color(0, 102, 204), 1));
				datatable
						.addCell(getPdfPCell((celdas.get("Direccion"))
								.toString(), Color.WHITE, 5, new Color(0, 102,
								204), 1));
				datatable
						.addCell(getPdfPCell((celdas.get("Ciudad"))
								.toString(), Color.WHITE, 5, new Color(0, 102,
								204), 1));
				datatable.addCell(getPdfPCell(
						(celdas.get("labelDepartamento")) == null ? "" : celdas
								.get("labelDepartamento").toString(),
						Color.WHITE, 5, new Color(0, 102, 204), 1));
				datatable.addCell(getPdfPCell((celdas.get("Peso")).toString(),
						Color.WHITE, 5, new Color(0, 102, 204), 1));
				float vlEnvio = Float.parseFloat((celdas.get("Precio"))
						.toString());
				tvalorEnvios += vlEnvio;
				datatable.addCell(getPdfPCell(Float.toString(vlEnvio),
						Color.WHITE, 5, new Color(0, 102, 204), 1));
				// datatable.addCell(getPdfPCell("1", Color.WHITE, 7, new
				// Color(0,
				// 102, 204), 1));
				datatable.addCell(getPdfPCell("", Color.WHITE, 5, new Color(0,
						102, 204), 1));
				// 2%
				float tasa2Px = getTasa2Px((celdas.get("Peso")).toString(),
						(celdas.get("Precio")).toString());
				tvalorSeguro2Px += tasa2Px;
				datatable.addCell(getPdfPCell(Float.toString(tasa2Px),
						Color.WHITE, 5, new Color(0, 102, 204), 1));
				datatable.addCell(getPdfPCell(
						Float.toString(vlEnvio + tasa2Px), Color.WHITE, 5,
						new Color(0, 102, 204), 1));
				datatable.addCell(getPdfPCell("", Color.WHITE, 5, new Color(0,
						102, 204), 1));
				datatable.addCell(getPdfPCell("", Color.WHITE, 5, new Color(0,
						102, 204), 1));
				/*
				 * datatable .addCell(getPdfPCell(((Data)
				 * celdas.get(0)).getProperty() .toString(), Color.WHITE, 7, new
				 * Color(0, 102, 204), 1));
				 */
				/*
				 * // datatable.addCell(new Phrase("1", letra), new Point(i,
				 * 0)); datatable.addCell(new Phrase("1", letra)); //
				 * datatable.addCell(new Phrase("L.C", letra), new Point(i, 1));
				 * datatable.addCell(new Phrase("L.C", letra)); // Columna 3:
				 * Numero de radicado texto = (Data) celdas.get(0); valorCelda =
				 * new Phrase(texto.getProperty().toString(), letra); //
				 * datatable.addCell(valorCelda, new Point(i, 2));
				 * datatable.addCell(valorCelda); tsobres++; // Columna 4:
				 * Destinatario texto = (Data) celdas.get(1); valorCelda = new
				 * Phrase(texto.getProperty().toString(), letra); //
				 * datatable.addCell(valorCelda, new Point(i, 3));
				 * datatable.addCell(valorCelda); // Columna 5: Ciudad texto =
				 * (Data) celdas.get(2); String ciudad =
				 * texto.getProperty().toString(); texto = (Data) celdas.get(3);
				 * ciudad = ciudad + " - " + texto.getProperty().toString();
				 * valorCelda = new Phrase(ciudad, letra); //
				 * datatable.addCell(valorCelda, new Point(i, 4));
				 * datatable.addCell(valorCelda); // Columna 6: Peso texto =
				 * (Data) celdas.get(4); valorCelda = new
				 * Phrase(texto.getProperty().toString(), letra); //
				 * datatable.addCell(valorCelda, new Point(i, 5));
				 * datatable.addCell(valorCelda); // Columna 7 y 14: Valor texto
				 * = (Data) celdas.get(5); try { int valor =
				 * Integer.parseInt(texto.getProperty().toString()); tvalor =
				 * tvalor + valor; } catch (Exception etotal) { } valorCelda =
				 * new Phrase(texto.getProperty().toString(), letra); //
				 * datatable.addCell("", new Point(i, 6));
				 * datatable.addCell(""); // datatable.addCell(valorCelda, new
				 * Point(i, 7)); datatable.addCell(valorCelda);
				 * 
				 * for (int j = 8; j < 13; j++) { // datatable.addCell("", new
				 * Point(i, j)); datatable.addCell(""); } //
				 * datatable.addCell(valorCelda, new Point(i, 13));
				 * datatable.addCell(valorCelda);
				 */
				i++;
				try {
					/*
					 * int valor = Integer.parseInt(((Data) celdas.get(5))
					 * .getProperty().toString());
					 */
					tvalor = tvalor + vlEnvio + tasa2Px;
				} catch (Exception etotal) {

				}

			} // for

			doc.add(datatable);

			datatable = null;
			datatable = new PdfPTable(numColumnas);
			datatable.setWidths(anchosCuerpo);
			datatable.setWidthPercentage(90);
			// datatable.setBorderWidth(1f);
			// datatable.setWidth(100f);
			// datatable.setSpaceInsideCell(2);
			datatable.setHorizontalAlignment(Element.ALIGN_CENTER);

			/*
			 * datatable.addCell(new Phrase("Sobres: " +
			 * String.valueOf(tsobres), letraTitulo)); //
			 * datatable.setDefaultHorizontalAlignment(Table.ALIGN_RIGHT);
			 * datatable.addCell(new Phrase("Total: " + String.valueOf(tvalor),
			 * letraTitulo));
			 */
			datatable.addCell(getPdfPCell("No TOTAL DE ENVIOS:", new Color(0,
					102, 204), 9, Color.WHITE, 7));
			// TODO total envios
			datatable.addCell(getPdfPCell(Integer.toString(pagina.getPosFinal()
					- pagina.getPosInicial()), Color.WHITE, 9, new Color(0,
					102, 204), 1));
			datatable.addCell(getPdfPCell("TOTAL:", new Color(0, 102, 204), 9,
					Color.WHITE, 3));
			datatable.addCell(getPdfPCell("$  " + String.valueOf(tvalorEnvios),
					new Color(204, 204, 255), 7, new Color(0, 102, 204), 1));
			/*
			 * datatable.addCell(getPdfPCell(String.valueOf(tsobres), new Color(
			 * 204, 204, 255), 7, new Color(0, 102, 204), 1));
			 */
			datatable.addCell(getPdfPCell("$  ", new Color(204, 204, 255), 7,
					new Color(0, 102, 204), 1));
			datatable.addCell(getPdfPCell("$  "
					+ String.valueOf(tvalorSeguro2Px),
					new Color(204, 204, 255), 7, new Color(0, 102, 204), 1));
			datatable.addCell(getPdfPCell("$  " + String.valueOf(tvalor),
					new Color(204, 204, 255), 7, new Color(0, 102, 204), 1));
			datatable.addCell(getPdfPCell("", new Color(0, 102, 204), 9,
					Color.WHITE, 2));
			doc.add(datatable);
			this.tvalor += tvalor;
			this.tvalorEnvios += tvalorEnvios;
			this.tvalorSeguro2Px += tvalorSeguro2Px;
		} catch (Exception e) {
			datatable = null;
			throw e;
		}
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	private float getTasa2Px(String peso, String valor) {
		try {
			float f = Float.parseFloat(peso);
			float fValor = Float.parseFloat(valor);
			float limite = Float.parseFloat(Constants
					.getProperty("plantillas.limitecalculotasa2Px"));
			if (f > limite) {
				return fValor * 0.02f;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return 0;
	}

	private String isInternacional(String string) {
		if (string.toUpperCase().indexOf("INTERNACIONAL") >= 0) {
			return "X";
		} else
			return "";
	}

	private String isNacional(String string) {
		if (string.toUpperCase().indexOf("INTERNACIONAL") < 0
				&& string.toUpperCase().indexOf("NACIONAL") >= 0) {
			return "X";
		} else
			return "";
	}

	private String isLocal(String string) {
		if (string.toUpperCase().indexOf("LOCAL") >= 0) {
			return "X";
		} else
			return "";
	}

	private String isPaquete(String string) {
		try {
			float f = Float.parseFloat(string);
			float limite = Float.parseFloat(Constants
					.getProperty("plantillas.limitedocumentopaquete"));
			if (f > limite) {
				return "X";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 
	 * @param string
	 * @return
	 */
	private String isDocumento(String string) {
		try {
			float f = Float.parseFloat(string);
			float limite = Float.parseFloat(Constants
					.getProperty("plantillas.limitedocumentopaquete"));
			if (f <= limite) {
				return "X";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * Modify Jvargas19/02/2009 first page 18 others pages 36 ?"
	 */
	public List paginar() {
		List filas = getFilas();
		PaginaTabla pagina = null;
		List newList = new LinkedList();
		int tam = filas.size();
		// pag1 cantRegistrosPagina
		// pag post cantRegistrosPagina*2
		/*
		 * int paginas = tam / cantRegistrosPagina; if (tam %
		 * cantRegistrosPagina > 0) { paginas++; }
		 */
		int startIndex = 0;
		int endIndex = 0;
		int page = 1;
		int cantidadRegistros = cantRegistrosPagina;
		while (endIndex < tam) {
			if (page > 1) {
				cantidadRegistros = cantRegistrosPagina * 2;
			}
			endIndex = startIndex + (cantidadRegistros);
			if (endIndex > tam) {
				endIndex = tam;
			}
			pagina = new PaginaTabla();
			pagina.setPosInicial(startIndex);
			pagina.setPosFinal(endIndex);
			newList.add(pagina);
			startIndex = endIndex;
			page++;
		}
		return newList;

		/*
		 * List filas = getFilas(); PaginaTabla pagina = null; List newList =
		 * new LinkedList(); int tam = filas.size();
		 * 
		 * int paginas = tam / cantRegistrosPagina; // jvargas 19/02/2009 if
		 * (tam > cantRegistrosPagina) { tam = tam - 18; paginas = tam /
		 * (cantRegistrosPagina * 2); } // end jvargas
		 * 
		 * if (tam % cantRegistrosPagina > 0) { paginas++; }
		 * 
		 * int startIndex = 0; int endIndex = 0; for (int i = 0; i < paginas;
		 * i++) { // 1 0-17 // 2 18-55 if (i > 0) { // startIndex = i *
		 * (cantRegistrosPagina * 2); startIndex = endIndex; } else { startIndex
		 * = i * cantRegistrosPagina; } // jvargas 19/02/2009 if (i > 0) {
		 * endIndex = startIndex + (cantRegistrosPagina * 2); }
		 * 
		 * else { endIndex = startIndex + (cantRegistrosPagina); }
		 * 
		 * if (endIndex > tam) {
		 * 
		 * endIndex = tam; } pagina = new PaginaTabla();
		 * pagina.setPosInicial(startIndex); pagina.setPosFinal(endIndex);
		 * newList.add(pagina); // newList.add(filas.subList(startIndex,
		 * endIndex)); } return newList;
		 */
	}

	public int[] getAgrupadores() {
		return agrupadores;
	}

	public void setAgrupadores(int[] agrupadores) {
		this.agrupadores = agrupadores;
	}

	public String getCiudadLocal() {
		return ciudadLocal;
	}

	public void setCiudadLocal(String ciudadLocal) {
		this.ciudadLocal = ciudadLocal;
	}

	public String getLocal() {
		return local;
	}

	public void setLocal(String local) {
		this.local = local;
	}

	public String getNacional() {
		return nacional;
	}

	public void setNacional(String nacional) {
		this.nacional = nacional;
	}

	public int getTotalSobres() {
		return totalSobres;
	}

	public void setTotalSobres(int totalSobres) {
		this.totalSobres = totalSobres;
	}

	public int getTotalValor() {
		return totalValor;
	}

	public void setTotalValor(int totalValor) {
		this.totalValor = totalValor;
	}

	/**
	 * @return Returns the urlFooter.
	 */
	public String getUrlFooter() {
		return urlFooter;
	}

	/**
	 * @param urlFooter
	 *            The urlFooter to set.
	 */
	public void setUrlFooter(String urlFooter) {
		this.urlFooter = urlFooter;
	}

	/**
	 * @return Returns the ciudadSede.
	 */
	public String getCiudadSede() {
		return ciudadSede;
	}

	/**
	 * @param ciudadSede
	 *            The ciudadSede to set.
	 */
	public void setCiudadSede(String ciudadSede) {
		this.ciudadSede = ciudadSede;
	}

	/**
	 * @return Returns the cantRegistrosPagina.
	 */
	public int getCantRegistrosPagina() {
		return cantRegistrosPagina;
	}

	/**
	 * @param cantRegistrosPagina
	 *            The cantRegistrosPagina to set.
	 */
	public void setCantRegistrosPagina(int cantRegistrosPagina) {
		this.cantRegistrosPagina = cantRegistrosPagina;
	}

	private String usuario;

	public void setUsuario(String userFullName) {
		usuario = userFullName;

	}

}
