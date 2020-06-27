package it.polito.tdp.newufosightings.model;

public class Adiacenza {

	private State stato1;
	private State stato2;
	private int peso;
	
	
	public Adiacenza(State stato1, State stato2, int peso) {
		super();
		this.stato1 = stato1;
		this.stato2 = stato2;
		this.peso = peso;
	}


	public State getStato1() {
		return stato1;
	}


	public void setStato1(State stato1) {
		this.stato1 = stato1;
	}


	public State getStato2() {
		return stato2;
	}


	public void setStato2(State stato2) {
		this.stato2 = stato2;
	}


	public int getPeso() {
		return peso;
	}


	public void setPeso(int peso) {
		this.peso = peso;
	}
	
	
}
