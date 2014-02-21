package org.ssahayog.webapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.ssahayog.webapp.dao.DBQuerierDAO;
import org.ssahayog.webapp.exception.ApplicationException;
import org.ssahayog.webapp.json.JsonProcessor;

/**
 * Servlet implementation class TestServlet
 */
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public TestServlet() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		JSONObject json;
		final String query = request.getParameter("query");
		if(query != null){
			try {
				json =  new DBQuerierDAO().getQueryResult(query);
			} catch (ApplicationException e) {
				json = JsonProcessor.createErrorMessage(e);
			}			
		} else {
			json = JsonProcessor.createStatusMessage(false, "No Query provided in the request");
		}
		response.setContentType("text/json");
		final PrintWriter outputWriter =  response.getWriter();
		outputWriter.write(json.toString());
		outputWriter.flush();
	}

}
