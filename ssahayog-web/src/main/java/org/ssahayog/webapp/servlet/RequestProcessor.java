package org.ssahayog.webapp.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.ssahayog.webapp.dao.DBQuerierDAO;
import org.ssahayog.webapp.dao.IDao;
import org.ssahayog.webapp.dao.vo.Volunteer;
import org.ssahayog.webapp.exception.ApplicationException;
import org.ssahayog.webapp.json.JsonProcessor;

import com.fasterxml.jackson.databind.ObjectMapper;

public class RequestProcessor {

	private IDao dao;

	public RequestProcessor() {
		try {
			dao = new DBQuerierDAO();
		} catch (ApplicationException e) {
			e.printStackTrace();
		}
	}

	public void processRequest(final HttpServletRequest request,
			final HttpServletResponse response) {
		final String action = request.getParameter("action");
		String result = null;
		PrintWriter responseWriter = null;
		try {
			response.setContentType("application/json");
			responseWriter = response.getWriter();
			if (action == null || "".equals(action)) {
				result = JsonProcessor.createStatusMessage(false,
						"No action attribute defined in the request.!").toString();
			} else {
				if ("getVolunteerDtls".equals(action)) {
					final String givenName = request.getParameter("givenName");
					result = dao.getVolunteerDetails(givenName).toString();
				} else if("addVolunteer".equals(action)){
					final String volunteerData = request.getParameter("volunteer");
					if(volunteerData != null && !"".equals(volunteerData)){
						ObjectMapper mapper = new ObjectMapper();
						final Volunteer volunteer = mapper.readValue(volunteerData, Volunteer.class);
						volunteer.setLeavingDate(null);
						volunteer.setActiveSince(new Date());
						volunteer.setModifiedBy("siyer");
//						volunteer.setMo
						final int rowsUpdated = dao.insert(volunteer);
						if(rowsUpdated == 1){
							result = JsonProcessor.createStatusMessage(true, "Volunteer Updated successfully").toString();
						} else {
							result = JsonProcessor.createStatusMessage(false, "Records Updated: "+rowsUpdated+". Verify logs for issues").toString();							
						}
					} else {
						result = JsonProcessor.createStatusMessage(false, "Volunteer information not provided correctly.").toString();
					}
				} else if ("getVolunteer".equals(action)){
					final String id = request.getParameter("id");
					final String name = request.getParameter("name");
					if(name == null && id == null){
						result = JsonProcessor.createStatusMessage(false, "Need to provide atleast name or id to search").toString();
					} else {
						String jsonResult;
						if(name != null){
							List<Volunteer> vals = dao.getByName(name, Volunteer.class);
							jsonResult = JsonProcessor.toJson(vals, false);
						} else {
							Volunteer v = dao.getById(Integer.parseInt(id), Volunteer.class);
							jsonResult = JsonProcessor.toJson(v, false);
						}
						JSONObject obj = new JSONObject();
						obj.put("result", jsonResult);
						result = obj.toString();
					}
				}
				else {
					result = JsonProcessor.createStatusMessage(false, "Unhandled Action attribute: "+action).toString();
				}
			}
		} catch (ApplicationException e) {
			e.printStackTrace();
			result = JsonProcessor.createErrorMessage("Exception while processing request..", e).toString();
		} catch (IOException e) {
			e.printStackTrace();
			result = JsonProcessor.createErrorMessage(new ApplicationException("Exception while processing request..", e)).toString();
		}
		responseWriter.write(result.toString());
		responseWriter.flush();
		responseWriter.close();

	}

	public JSONObject getDonorName(final String sampledDonorName) {
		// dao.get
		return null;
	}
}
