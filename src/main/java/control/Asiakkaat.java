package control;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

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

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doGet()");
		String hakusana = request.getParameter("hakusana"); //otetaan hakusana vastaan
		//System.out.println(hakusana); // tulostetaan konsoliin, tarkistus
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
		}
		response.setContentType("application/json; charset=UTF-8");
		PrintWriter out = response.getWriter();
		out.println(strJSON);
		
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doPost()");
	}


	protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doPut()");
	}

	
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Asiakkaat.doDelete()");
	}

}
