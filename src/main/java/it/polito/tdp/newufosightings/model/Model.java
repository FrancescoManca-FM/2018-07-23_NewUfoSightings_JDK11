package it.polito.tdp.newufosightings.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.db.NewUfoSightingsDAO;

public class Model {

	private NewUfoSightingsDAO dao;
	private SimpleWeightedGraph<State, DefaultWeightedEdge> grafo;
	private Map<String, State> idMap;
	private Simulator sim;
	
	public Model() {
		this.dao = new NewUfoSightingsDAO();
		this.grafo = new SimpleWeightedGraph<State, DefaultWeightedEdge>(DefaultWeightedEdge.class);
	}
	
	public void creaGrafo(int anno, String shape) {
		
		idMap = new HashMap<>();
		List<State> vertici = this.dao.loadAllStates(idMap);
		List<Adiacenza> archi = this.dao.getAdiacenze(anno, shape, idMap);
		
		Graphs.addAllVertices(this.grafo, vertici);
		
		for(Adiacenza a : archi) {
			Graphs.addEdge(this.grafo, a.getStato1(), a.getStato2(), a.getPeso());
		}
	}
	
	public List<String> getShapeAnno(int anno){
		return this.dao.getShapeAnno(anno);
	}
	
	public List<Sighting> getAvvenimenti(int anno, String shape){
		return this.dao.loadAvvenimenti(anno, shape);
	}
	
	public List<State> getVicini(State origine){
		return Graphs.neighborListOf(this.grafo, origine);
	}
	
	public List<State> getStati(){
		return this.dao.loadAllStates(idMap);
	}
	
	public int getVerticiSize() {
		return this.grafo.vertexSet().size();
	}
	
	public int getArchiSize() {
		return this.grafo.edgeSet().size();
	}
	
	public List<State> setPesoTotale(){
		
		List<State> risultato = new ArrayList<>();
		
		for(State s : this.grafo.vertexSet()) {
			int pesoTotale = 0;
			List<State> vicini = Graphs.neighborListOf(this.grafo, s);
			for(State stato : vicini) {
				pesoTotale += this.grafo.getEdgeWeight(this.grafo.getEdge(s, stato));
			}
			s.setTotalePesi(pesoTotale);
			risultato.add(s);
		}
		return risultato;
		
	}
	
	public void init(int t1, int alfa, int anno, String shape) {
		this.sim = new Simulator();
		this.sim.init(t1, alfa, this, anno, shape, idMap);
	}
	
	public void run() {
		this.sim.run();
	}
	
	public Map<State, Double> getMappa() {
		return this.sim.getStatiDefcon();
	}
}
