/*
 * PaginaTabla.java
 *
 * Created on 21 de septiembre de 2005, 02:23 PM
 *
 * To change this template, choose Tools | Options and locate the template under
 * the Source Creation and Management node. Right-click the template and choose
 * Open. You can then make changes to the template in the Source Editor.
 */

package com.intent.minminas472.pdf;

/**
 * 
 * @author administrator
 */
public class PaginaTabla {

	private int posInicial;

	private int posFinal;

	private String valorRompimiento;

	/** Creates a new instance of PaginaTabla */
	public PaginaTabla() {
		setPosInicial(0);
		setPosFinal(0);
		setValorRompimiento("");
	}

	public int getPosInicial() {
		return posInicial;
	}

	public void setPosInicial(int posInicial) {
		this.posInicial = posInicial;
	}

	public int getPosFinal() {
		return posFinal;
	}

	public void setPosFinal(int posFinal) {
		this.posFinal = posFinal;
	}

	public String getValorRompimiento() {
		return valorRompimiento;
	}

	public void setValorRompimiento(String valorRompimiento) {
		this.valorRompimiento = valorRompimiento;
	}

}
