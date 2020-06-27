package it.polito.tdp.newufosightings.model;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.newufosightings.model.Event.EventType;

public class Simulator {

	//Coda ad eventi
	private PriorityQueue<Event> coda;
	
	//parametri inseriti dall'utente
	private int T1 = 10;
	private int alfa = 50;
	private Model model;
	private Map<String, State> idMap;
	//stato del sistema
	private Map<State, Double> defconStati;
	
	
	public void init(int t1, int alfa, Model model, int anno, String shape, Map<String, State>idMap) {
		this.T1 = t1;
		this.alfa = alfa;
		this.model = model;
		this.idMap = idMap;
		
		this.coda = new PriorityQueue<>();
		this.defconStati = new HashMap<>();
		List<State> stati = this.model.getStati();
		List<Sighting> avvenimenti = this.model.getAvvenimenti(anno, shape);
		for(State s : stati) {
			this.defconStati.put(s, 5.00);
		}
		
		for(Sighting avvenimento : avvenimenti) {
			coda.add(new Event(idMap.get(avvenimento.getState().toUpperCase()),avvenimento.getDatetime(),EventType.AVVISTAMENTO));
		}
	}
	
	public void run() {
		while(!this.coda.isEmpty()) {
			processEvent(coda.poll());
		}
	}
	
	public Map<State, Double> getStatiDefcon(){
		return this.defconStati;
	}
	
	public void processEvent(Event e) {
		System.out.println(e);
		switch(e.getTipo()) {
		
		case AVVISTAMENTO:
			if(defconStati.get(e.getStato())>=1 && defconStati.get(e.getStato())<=5) {
				defconStati.put(e.getStato(), defconStati.get(e.getStato())-1.0);
				System.out.println(defconStati.get(e.getStato()));
				double random = Math.random()*100;
				if(random<=this.alfa) {
					for(State s : this.model.getVicini(e.getStato())) {
						coda.add(new Event(s, e.getData(), EventType.AVVISTAMENTO_VICINI));
					}
				}
				coda.add(new Event(e.getStato(), e.getData().plus(Duration.of(T1, ChronoUnit.DAYS)), EventType.CESSATA_ALLERTA));
			}else {
				break;
			}
			break;
			
		case AVVISTAMENTO_VICINI:
			if(defconStati.get(e.getStato())>=1 && defconStati.get(e.getStato())<=5) {
				defconStati.put(e.getStato(), defconStati.get(e.getStato())-0.5);
				coda.add(new Event(e.getStato(), e.getData().plus(Duration.of(T1, ChronoUnit.DAYS)), EventType.CESSATA_ALLERTA_VICINI));
			}
			break;
			
		case CESSATA_ALLERTA:
			if(defconStati.get(e.getStato())>=1 && defconStati.get(e.getStato())<=5) {
				defconStati.put(e.getStato(), defconStati.get(e.getStato())+1.0);
			}
			break;		
		
		case CESSATA_ALLERTA_VICINI:
			if(defconStati.get(e.getStato())>=1 && defconStati.get(e.getStato())<=5) {
				defconStati.put(e.getStato(), defconStati.get(e.getStato())+0.5);
			}
			break;
		}
	}
}
