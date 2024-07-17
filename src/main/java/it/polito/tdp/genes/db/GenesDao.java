package it.polito.tdp.genes.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.genes.model.Edge;
import it.polito.tdp.genes.model.Genes;
import it.polito.tdp.genes.model.Interactions;


public class GenesDao {
	
	public List<Genes> getAllGenes(){
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";
		List<Genes> result = new ArrayList<Genes>();

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Genes genes = new Genes(res.getString("GeneID"), 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				result.add(genes);
			}
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}
	
	public void initializeMap(Map<String, Genes> idMap){
		
		String sql = "SELECT DISTINCT GeneID, Essential, Chromosome FROM Genes";

		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				String id = res.getString("GeneID");
				Genes genes = new Genes( id, 
						res.getString("Essential"), 
						res.getInt("Chromosome"));
				idMap.put(id, genes);
			}
			res.close();
			st.close();
			conn.close();
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}

	public List<Integer> getAllVertex(Map<String, Genes> idMap) {
		String sql = "SELECT DISTINCT g.Chromosome "
				+ "FROM genes g "
				+ "WHERE g.Chromosome>0 "
				+ "ORDER BY g.Chromosome ";
		
		List<Integer> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				Integer c = res.getInt("Chromosome");
				result.add(c);
			
			}
			
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}

	
	public List<Edge> getAllEdges(Map<String, Genes> idMap) {
		String sql = "SELECT g1.Chromosome as chro1, i.GeneID1, g2.Chromosome as chro2, i.GeneID2, i.Expression_Corr "
				+ "FROM genes g1 , interactions i , genes g2 "
				+ "WHERE i.GeneID1 = g1.GeneID AND i.GeneID2 = g2.GeneID AND g1.Chromosome <> g2.Chromosome "
				+ "GROUP BY g1.Chromosome, i.GeneID1, g2.Chromosome, i.GeneID2,  i.Expression_Corr "
				+ "ORDER BY g1.Chromosome, g2.Chromosome ";
		
		List<Edge> result = new ArrayList<>();
		
		try {
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {
				if(result.size()>0) {
					Edge ultimo = result.get(result.size()-1);
					Edge e = new Edge(res.getInt("chro1"), res.getInt("chro2"), res.getDouble("Expression_Corr"));
					if(e.equals(ultimo))
						ultimo.addPeso(res.getDouble("Expression_Corr"));
					else
						result.add(e);
				}else
					result.add(new Edge(res.getInt("chro1"), res.getInt("chro2"), res.getDouble("Expression_Corr")));
			}
			
			res.close();
			st.close();
			conn.close();
			return result;
			
		} catch (SQLException e) {
			throw new RuntimeException("Database error", e) ;
		}
	}

	
}
