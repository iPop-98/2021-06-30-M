package it.polito.tdp.genes.model;

import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.genes.db.GenesDao;

public class Model {

	private GenesDao dao;
	private Graph<Integer, DefaultWeightedEdge> grafo;
	private Map<String, Genes> genesIdMap;
	
	private List<Integer> migliore;
	private double lungMax;
	
	
	public Model() {
		this.dao = new GenesDao();
		this.grafo = new SimpleDirectedWeightedGraph<Integer, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		this.genesIdMap = new HashMap<>();
		
		this.initializeMap();
		this.buildGraph();
	}


	private void initializeMap() {
		this.dao.initializeMap(this.genesIdMap);
	}
	

	private void buildGraph() {
		
		List<Integer> allVertex;
		List<Edge> allEdges;
		if(this.genesIdMap!=null) {
			try {
				allVertex = new ArrayList<>(this.dao.getAllVertex(this.genesIdMap));

				allEdges = new ArrayList<>(this.dao.getAllEdges(this.genesIdMap));

			}catch(RuntimeException re) {
				throw new RuntimeException("Database error", re) ;
			}
			
			if(allVertex.size()>0)
				Graphs.addAllVertices(this.grafo, allVertex);
			
			if(allEdges.size()>0)
				for(Edge e : allEdges)
					if(this.grafo.vertexSet().contains(e.getC1()) && this.grafo.vertexSet().contains(e.getC2())) {
						Graphs.addEdge(this.grafo, e.getC1(), e.getC2(), e.getPeso());
						System.out.println(this.grafo.getEdge(e.getC1(), e.getC2()) + " di peso " + this.grafo.getEdgeWeight(this.grafo.getEdge(e.getC1(), e.getC2())));
					}
	
		}else
			throw new RuntimeException("ERROR MODEL: assenza dati nella idMap.");
		
	}
	
	
	/**
	 * Metodo di controllo che serve a verificare se il grafo è stato inizializzato, con almeno un vertice
	 * @return true se ha almeno 1 vertice
	 * @return false altrimenti
	 */
	public boolean isGrafoLoaded() {
		if(this.grafo==null)
			return false;
		else
			return this.grafo.vertexSet().size()>0;
	}
	
	
	public Integer getNumberOfVertex() {
		if(this.isGrafoLoaded())
			return this.grafo.vertexSet().size();
		else 
			return null;
	}
	
	public Integer gerNumberOfEdges() {
		if(this.isGrafoLoaded())
			return this.grafo.edgeSet().size();
		else 
			return null;
	}
	
	public Double getPesoMinimo() {
		Double minimo = 100000.00;
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			Double peso = this.grafo.getEdgeWeight(e);
			if(peso<minimo)
				minimo = peso;
		}
		return minimo;
	}
	
	public Double getPesoMassimo() {
		Double massimo = 0.00;
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			Double peso = this.grafo.getEdgeWeight(e);
			if(peso>massimo)
				massimo = peso;
		}
		return massimo;
	}


	public Integer getNumArchiPesoMaggiore(double soglia) {
		List<DefaultWeightedEdge> maggiore = new ArrayList<>();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			Double peso = this.grafo.getEdgeWeight(e);
			if(peso>soglia)
				maggiore.add(e);
		}
		return maggiore.size();
	}


	public Integer getNumArchiPesoMinore(double soglia) {
		List<DefaultWeightedEdge> minore = new ArrayList<>();
		for(DefaultWeightedEdge e : this.grafo.edgeSet()) {
			Double peso = this.grafo.getEdgeWeight(e);
			if(peso<soglia)
				minore.add(e);
		}
		return minore.size();
	}

	
	public List<Integer> ricerca(double soglia) {
		this.migliore= new ArrayList<Integer>();
		this.lungMax = 0.00;
		List<Integer> parziale = new ArrayList<>();
		for(Integer v : this.grafo.vertexSet()) {
			parziale.add(v);
			this.cerca(parziale, 1, soglia);
			parziale.remove(0);
		}
		return this.migliore;
	}
	
//	private void cerca(List<Integer> parziale, int livello, double soglia) {
//		if(livello==this.grafo.vertexSet().size()) {
//			double lung = this.getLunghezza(parziale);
//			if(lung>this.lungMax) {
//				this.lungMax=lung;
//				this.migliore = new ArrayList<Integer>(parziale);
//			}
//		}else 
//			if(((this.grafo.vertexSet().size()-livello)*this.getPesoMassimo())+this.getLunghezza(parziale)<this.lungMax)
//				return;
//			else{
//				Integer ultimo = parziale.get(livello-1);
//				Set<Integer> check = new HashSet<Integer>(parziale);
//				Set<Integer> vicini = Graphs.neighborSetOf(this.grafo, ultimo);
//				vicini.removeAll(check);
//				for(Integer nodo : vicini) {
//					if(this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, nodo))>soglia)
//						if(!check.contains(nodo)) {
//							parziale.add(nodo);
//							this.cerca(parziale, livello+1, soglia);
//							parziale.remove(parziale.size()-1);
//						}
//				}
//			}
//			
//	}
	
	
	public void cerca(List<Integer> parziale, int livello, double soglia) {
		if(livello==this.grafo.vertexSet().size()) {
			double lung = this.getLunghezza(parziale);
			if(lung>this.lungMax) {
				this.lungMax=lung;
				this.migliore = new ArrayList<Integer>(parziale);
			}
			return;
		}
		double lung = this.getLunghezza(parziale);
		if(lung>this.lungMax) {
			this.lungMax=lung;
			this.migliore = new ArrayList<Integer>(parziale);
		}
		
		Integer ultimo = parziale.get(livello-1);
		Set<Integer> vicini = new HashSet<>(Graphs.successorListOf(this.grafo, ultimo));
		vicini.removeAll(parziale);
		if(vicini.size()==0)
			return;
		
		for(Integer nodo : vicini)
			if(this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, nodo))>soglia) {
				parziale.add(nodo);
				this.cerca(parziale, livello+1, soglia);
				parziale.remove(parziale.size()-1);
			}
	}


	public double getLunghezza(List<Integer> parziale) {
		
		double lunghezza= 0.00;
		for(int i = 0; i<parziale.size()-2; i++) {
			
			lunghezza += this.grafo.getEdgeWeight(this.grafo.getEdge(parziale.get(i), parziale.get(i+1)));
		}
		return lunghezza;
	}
}