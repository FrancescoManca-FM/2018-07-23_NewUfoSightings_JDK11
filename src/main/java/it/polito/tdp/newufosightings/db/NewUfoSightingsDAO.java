package it.polito.tdp.newufosightings.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.newufosightings.model.Adiacenza;
import it.polito.tdp.newufosightings.model.Sighting;
import it.polito.tdp.newufosightings.model.State;

public class NewUfoSightingsDAO {

	public List<Sighting> loadAvvenimenti(int anno, String shape) {
		String sql = "SELECT * FROM sighting WHERE YEAR(datetime)=? AND shape=? ";
		List<Sighting> list = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);	
			st.setInt(1, anno);
			st.setString(2, shape);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				list.add(new Sighting(res.getInt("id"), res.getTimestamp("datetime").toLocalDateTime(),
						res.getString("city"), res.getString("state"), res.getString("country"), res.getString("shape"),
						res.getInt("duration"), res.getString("duration_hm"), res.getString("comments"),
						res.getDate("date_posted").toLocalDate(), res.getDouble("latitude"),
						res.getDouble("longitude")));
			}

			conn.close();

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}

		return list;
	}

	public List<State> loadAllStates(Map<String, State> idMap) {
		String sql = "SELECT * FROM state";
		List<State> result = new ArrayList<State>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet rs = st.executeQuery();

			while (rs.next()) {
				State state = new State(rs.getString("id"), rs.getString("Name"), rs.getString("Capital"),
						rs.getDouble("Lat"), rs.getDouble("Lng"), rs.getInt("Area"), rs.getInt("Population"),
						rs.getString("Neighbors"));
				if(!idMap.containsKey(rs.getString("id"))) {
					idMap.put(rs.getString("id"), state);
				}
				result.add(state);
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			e.printStackTrace();
			System.out.println("Errore connessione al database");
			throw new RuntimeException("Error Connection Database");
		}
	}
	
	public List<String> getShapeAnno(int anno){
		
		String sql = "SELECT DISTINCT shape " + 
				"FROM sighting " + 
				"WHERE YEAR(DATETIME)=?";
		List<String> risultato = new ArrayList<>();
		
		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				risultato.add(rs.getString("shape"));
			}
			conn.close();
			return risultato;
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore nel caricamento dati dal database");
		}
	}
	
	public List<Adiacenza> getAdiacenze(int anno, String shape, Map<String, State>idMap) {
		
		String sql = "SELECT DISTINCT n1.state1 AS s1 ,n1.state2 AS s2, COUNT(s.id) AS peso " + 
				"FROM sighting AS s, neighbor AS n1 " + 
				"WHERE s.shape=? AND YEAR(s.datetime)=? " + 
				"AND (s.state=n1.state1 || s.state=n1.state2) " + 
				"GROUP BY n1.state1, n1.state2";
		
		List<Adiacenza> risultato = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, shape);
			st.setInt(2, anno);
			
			ResultSet rs = st.executeQuery();
			
			while(rs.next()) {
				State s1 = idMap.get(rs.getString("s1"));
				State s2 = idMap.get(rs.getString("s2"));
				risultato.add(new Adiacenza(s1, s2, rs.getInt("peso")));
			}
			
			conn.close();
			return risultato;
			
		}catch(SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Errore nel caricamento dati dal database");
		}
	}

}

