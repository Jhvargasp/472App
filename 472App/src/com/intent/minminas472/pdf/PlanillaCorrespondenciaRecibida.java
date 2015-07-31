/*
 * PlanillaCorrespondenciaRecibida.java
 *
 * Clase encargada de generar PDFs con con tablas como resultado
 * de consultas
 * @author  Juan F. Medina
 * @version 1.0
 * @see  import com.lowagie.text
 * @see com.intent.pdf.Tabla
 * fecha: 12/09/2005
 *****************************************************************************
 * Modificacion
 * Fecha: 21/09/2005
 * Se agrego el encabezado segun lo solicitado por ECOPETROL
 */

package com.intent.minminas472.pdf;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;

/**
 * 
 * @author Juan F. Medina
 */
public class PlanillaCorrespondenciaRecibida extends Tabla {

	/** Creates a new instance of PlanillaCorrespondenciaRecibida */
	public PlanillaCorrespondenciaRecibida() {
		super();
		crearEncabezadosColumna();
		setCiudad("Bogota D.C.");
		setCgc("");
		setEdificio("");
		setRecorrido("");
		setMensajero("");
		setFecha(new Date());
		setNroPlanilla("0");
		setCampoRompimiento(0);
		setTitulo("Planillas Recorrido");
		int[] ocultas = { 0 };
		setColumnasOcultas(ocultas);
		List ocultasL = new ArrayList();
		ocultasL.add("ID");
		ocultasL.add("Dependencia");
		ocultasL.add("Edificio");
		setColumnasOcultasList(ocultasL);
	}

	private String codigoFormato;

	private String fechaFormato;

	private String versionFormato;

	private String ciudad;

	private String cgc;

	private String edificio;

	private String recorrido;

	private String mensajero;

	private java.util.Date fecha;

	private String nroPlanilla;

	private String urlLogo;

	public void crearEncabezadosColumna() {
		List filas = (List) new ArrayList();
		Data unDato;

		unDato = new Data();
		unDato.setDisplayName("Dependencia");
		filas.add(unDato);

		unDato = new Data();
		unDato.setDisplayName("Remite");
		filas.add(unDato);

		unDato = new Data();
		unDato.setDisplayName("No Radicacion");
		filas.add(unDato);

		unDato = new Data();
		unDato.setDisplayName("Anexos");
		filas.add(unDato);

		unDato = new Data();
		unDato.setDisplayName("Destino Interno");
		filas.add(unDato);

		unDato = new Data();
		unDato.setDisplayName("Fecha Registro");
		filas.add(unDato);

		unDato = new Data();
		unDato.setDisplayName("Recibe");
		filas.add(unDato);

		setEncabezadosColumnas(filas);
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getCgc() {
		return cgc;
	}

	public void setCgc(String cgc) {
		this.cgc = cgc;
	}

	public String getEdificio() {
		return edificio;
	}

	public void setEdificio(String edificio) {
		this.edificio = edificio;
	}

	public String getRecorrido() {
		return recorrido;
	}

	public void setRecorrido(String recorrido) {
		this.recorrido = recorrido;
	}

	public String getMensajero() {
		return mensajero;
	}

	public void setMensajero(String mensajero) {
		this.mensajero = mensajero;
	}

	public java.util.Date getFecha() {
		return fecha;
	}

	public void setFecha(java.util.Date fecha) {
		this.fecha = fecha;
	}

	/**
	 * Sobreescribe el metodo escribir en cabezado de la clase Tabla
	 */
	protected void escribirEncabezado(Document doc, PaginaTabla pagina)
			throws Exception {
		PdfPTable datatable = null;
		try {

			// Tabla de 3 Columanas
			datatable = new PdfPTable(3);
			/** Definie estilos para la tabla */
			// datatable.setDefaultCellBorderWidth(1);
			Font letraEncabezado = FontFactory.getFont(
					getTipoLetraEncabezados(), getTamanoLetraEncabezados());
			Font letraTitulo = FontFactory.getFont(getTipoLetraTitulo(),
					getTamanoLetraTitulo());
			int[] anchos = { 15, 60, 25 };
			// datatable.setWidth(100f);
			datatable.setWidths(anchos);
			// datatable.setDefaultVerticalAlignment(Table.ALIGN_MIDDLE);
			// datatable.setSpaceInsideCell(0);
			datatable.setHorizontalAlignment(Element.ALIGN_CENTER);
			// Columna izquierda Logo
			PdfPCell cellLogo = new PdfPCell(Image.getInstance(getUrlLogo()));
			cellLogo.setHorizontalAlignment(Element.ALIGN_CENTER);
			datatable.addCell(cellLogo);
			// Columna de centro titulo y rempimiento
			PdfPTable subtable = new PdfPTable(1);
			// subtable.setBorder(0);
			// subtable.setWidth(100f);
			// subtable.setBorderWidth(0);
			// subtable.setDefaultCellBorderWidth(0);

			subtable.setHorizontalAlignment(Element.ALIGN_CENTER);
			PdfPCell cell1 = new PdfPCell(new Phrase(getTitulo(), letraTitulo));
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			subtable.addCell(cell1);
			// subtable.setDefaultHorizontalAlignment(Table.ALIGN_LEFT);
			Phrase p = new Phrase(pagina.getValorRompimiento()
					+ "    Fecha Generación: "
					+ new SimpleDateFormat("dd/MM/yyyy").format(new Date()),
					letraEncabezado);
			subtable.addCell(p);
			datatable.addCell(new PdfPCell(subtable));
			// Coloca los datos de la planilla
			PdfPTable subtable2 = new PdfPTable(1);
			// subtable2.setWidth(100f);

			subtable2.addCell(new Phrase("Código: " + getCodigoFormato(),
					letraEncabezado));
			subtable2.addCell(new Phrase("Versión: " + getVersionFormato(),
					letraEncabezado));
			subtable2.addCell(new Phrase("Fecha: " + getFechaFormato(),
					letraEncabezado));
			datatable.addCell(new PdfPCell(subtable2));

			doc.add(datatable);

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

	public String getVersionFormato() {
		return versionFormato;
	}

	public void setVersionFormato(String versionFormato) {
		this.versionFormato = versionFormato;
	}

}
