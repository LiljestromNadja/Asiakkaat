package model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Asiakas;

public class Dao {
	private Connection con = null;
	private ResultSet rs = null;
	private PreparedStatement stmtPrep = null;
	private String sql;
	private String db = "Myynti.sqlite";

	private Connection yhdista() {
		Connection con = null;
		String path = System.getProperty("catalina.base");
		path = path.substring(0, path.indexOf(".metadata")).replace("\\", "/"); // Eclipsessa
		//System.out.println(path); //Tästä näet mihin kansioon laitat tietokanta-tiedostosi
		// path += "/webapps/"; //Tuotannossa. Laita tietokanta webapps-kansioon
		String url = "jdbc:sqlite:" + path + db;
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection(url);
			System.out.println("Yhteys avattu.");
		} catch (Exception e) {
			System.out.println("Yhteyden avaus epäonnistui.");
			e.printStackTrace();
		}
		return con;
	}

	private void sulje() {		
		if (stmtPrep != null) {
			try {
				stmtPrep.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		if (con != null) {
			try {
				con.close();
				System.out.println("Yhteys suljettu.");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	

	public ArrayList<Asiakas> getAllItems() {
		ArrayList<Asiakas> asiakkaat = new ArrayList<Asiakas>();
		sql = "SELECT * FROM asiakkaat ORDER BY asiakas_id DESC"; //Suurin id tulee ensimmäisenä
		try {
			con = yhdista();
			if (con != null) { // jos yhteys onnistui
				stmtPrep = con.prepareStatement(sql);
				rs = stmtPrep.executeQuery();
				if (rs != null) { // jos kysely onnistui
					while (rs.next()) {
						Asiakas asiakas = new Asiakas();
						asiakas.setAsiakas_id(rs.getInt(1));//1=sarakenumero, voisi myös lukea sarakkeen nimi lainausmerkeissä
						asiakas.setEtunimi(rs.getString(2));//2=sarakenumero, voisi myös lukea sarakkeen nimi lainausmerkeissä
						asiakas.setSukunimi(rs.getString(3));
						asiakas.setPuhelin(rs.getString(4));
						asiakas.setSposti(rs.getString(5));
						asiakkaat.add(asiakas);//asiakas lisätään asiakkaat arraylistiin
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			sulje();//Joka tapauksessa yhteys suljetaan!! 
		}
		return asiakkaat;
	}
	
	public ArrayList<Asiakas> getAllitems(String searchStr) {//Metodeja voi kuormittaa kunhan parametreissa on eroja 
		ArrayList<Asiakas> asiakkaat = new ArrayList<Asiakas>();
				sql = "SELECT * FROM asiakkaat WHERE etunimi LIKE ? OR sukunimi LIKE ? OR puhelin LIKE ? OR sposti LIKE ? ORDER BY asiakas_id DESC";
				try {
					con = yhdista();
					if (con != null) { // jos yhteys onnistui
						stmtPrep = con.prepareStatement(sql);
						stmtPrep.setString(1,"%" + searchStr + "%"); //Kohdistuu ekaan kysymysmerkkiin sql-lauseessa
						stmtPrep.setString(2,"%" + searchStr + "%");//Toka kysymysmerkkki
						stmtPrep.setString(3,"%" + searchStr + "%");//Kolmas HUOM!! % -merkki on "laaja jokeri" eli edessä ja takana voi olla mitä vain 
						rs = stmtPrep.executeQuery();
						if (rs != null) { // jos kysely onnistui
							while (rs.next()) {
								Asiakas asiakas = new Asiakas();
								asiakas.setAsiakas_id(rs.getInt(1));//1=sarakenumero, voisi myös lukea sarakkeen nimi lainausmerkeissä
								asiakas.setEtunimi(rs.getString(2));//2=sarakenumero, voisi myös lukea sarakkeen nimi lainausmerkeissä
								asiakas.setSukunimi(rs.getString(3));
								asiakas.setPuhelin(rs.getString(4));
								asiakas.setSposti(rs.getString(5));
								asiakkaat.add(asiakas);//asiakas lisätään asiakkaat arraylistiin
							}
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				} finally {
					sulje();//Joka tapauksessa yhteys suljetaan!! 
				}
				return asiakkaat;
				
	}
}