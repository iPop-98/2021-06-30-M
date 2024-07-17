package it.polito.tdp.genes.model;

import java.util.Objects;

public class Edge {

	private Integer c1;
	private Integer c2;
	private Double peso;
	
	
	public Edge(Integer c1, Integer c2, Double peso) {
		super();
		this.c1 = c1;
		this.c2 = c2;
		this.peso = peso;
	}


	public Integer getC1() {
		return c1;
	}


	public Integer getC2() {
		return c2;
	}

	
	public void addPeso(double peso) {
		this.peso+= peso;
	}

	public Double getPeso() {
		return peso;
	}


	@Override
	public int hashCode() {
		return Objects.hash(c1, c2);
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Edge other = (Edge) obj;
		return Objects.equals(c1, other.c1) && Objects.equals(c2, other.c2);
	}


	@Override
	public String toString() {
		return "Edge [c1=" + c1 + ", c2=" + c2 + ", peso=" + peso + "]";
	}
	
	
	
}
