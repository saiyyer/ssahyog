package org.ssahayog.webapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONObject;
import org.ssahayog.webapp.dao.DBQuerierDAO;
import org.ssahayog.webapp.exception.ApplicationException;
import org.ssahayog.webapp.json.JsonProcessor;

/**
 * Servlet implementation class TestServlet
 */
public class TestServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	private final Logger thisLogger = LogManager.getLogger(getClass());

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
		
		thisLogger.trace("doGet Method...");
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		thisLogger.trace("doPostMethod");
		thisLogger.info("Hello World!!!");
		JSONObject json;
		final String query = request.getParameter("query");
		final String action = request.getParameter("action");
		if(query != null){
			try {
				json =  new DBQuerierDAO().getQueryResult(query);
			} catch (ApplicationException e) {
				json = JsonProcessor.createErrorMessage(e);
			}
		} else if  (action != null){
			new RequestProcessor().processRequest(request, response);
			return;
		}
		else {
			json = JsonProcessor.createStatusMessage(false, "No query/action parameter provided in the request");
		}
		response.setContentType("application/json");
		final PrintWriter outputWriter =  response.getWriter();
		outputWriter.write(json.toString());
		outputWriter.flush();
	}

}
