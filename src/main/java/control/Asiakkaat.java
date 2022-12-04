package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import model.Asiakas;
import model.dao.Dao;
/**
 * Servlet implementation class Asiakkaat
 */
@WebServlet("/asiakkaat/*") //Muuta alkukirjain pieneksi ja * perään --->/Asiakkaat --> /asiakkaat/*
public class Asiakkaat extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    
    public Asiakkaat() {
        System.out.println("Asiakkaat.Asiakkaat()");
      //"systr" ja CTRL + välilyönti
    }

	//Tietojen hakeminen
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doGet()");
		String hakusana = request.getParameter("hakusana"); //otetaan hakusana vastaan
		//System.out.println(hakusana); // tulostetaan konsoliin, tarkistus
		String asiakas_id = request.getParameter("asiakas_id")	;				
		Dao dao = new Dao();
		ArrayList<Asiakas> asiakkaat;
		String strJSON="";
		
		if (hakusana != null) { //Jos hakusana on olemassa
			if(!hakusana.equals("")) { //Jos hakusana ei ole tyhjä
				asiakkaat = dao.getAllitems(hakusana); //haetaan kaikki hakusanan mukaiset asiakkaat
			} else {
				asiakkaat = dao.getAllItems(); //haetaan kaikki asiakkaat
			}
			strJSON = new Gson().toJson(asiakkaat);			
		} else if (asiakas_id!=null) {
			Asiakas asiakas = dao.getItem(Integer.parseInt(asiakas_id));
			strJSON = new Gson().toJson(asiakas);
		}
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(strJSON);
		
	}

	//Tietojen lisääminen
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doPost()");
		//Luetaan JSON-tiedot POST-pyynnön bodysta ja luodaan niiden perusteella uusi asiakas
		String strJSONInput = request.getReader().lines().collect(Collectors.joining());
		//System.out.println(strJSONInput);
		Asiakas asiakas = new Gson().fromJson(strJSONInput, Asiakas.class);	//model Asiakas
		//System.out.println(asiakas);
		Dao dao = new Dao();
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
			if(dao.addItem(asiakas)) { //jos arvo on true
				out.println("{\"response\":1}");  //Asiakkaan lisääminen onnistui {"response":1}
			}else { //jos false
				out.println("{\"response\":0}");  //Asiakkaan lisääminen epäonnistui {"response":0}
			}
	}

	//Tietojen muuttaminen
	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doPut()");
		//Luetaan JSON-tiedot PUT-pyynn�n bodysta ja luodaan niiden perusteella uusi asiakas
		String strJSONInput = request.getReader().lines().collect(Collectors.joining());
		//System.out.println("strJSONInput " + strJSONInput);		
		Asiakas asiakas = new Gson().fromJson(strJSONInput, Asiakas.class);		
		//System.out.println(asiakas);		
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		Dao dao = new Dao();			
		if(dao.changeItem(asiakas)){ //metodi palauttaa true/false
			out.println("{\"response\":1}");  //Asiakkaan muuttaminen onnistui {"response":1}
			}else{
				out.println("{\"response\":0}");  //Asiakkaan muuttaminen ep�onnistui {"response":0}
				}
	}

	//Tietojen poistaminen
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doDelete()");
		int asiakas_id = Integer.parseInt(request.getParameter("asiakas_id")); //Kutsujen mukana kulkevat arvot ovat AINA merkkejä, myös numeraaliset arvot	
		Dao dao = new Dao();
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		if(dao.removeItem(asiakas_id)) {
			out.println("{\"response\":1}");  //Asiakkaan poistaminen onnistui {"response":1}
		}else {
			out.println("{\"response\":0}");  //Asiakkaan poistaminen epäonnistui {"response":0}
		}
	}

}
