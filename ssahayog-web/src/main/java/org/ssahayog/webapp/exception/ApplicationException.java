package org.ssahayog.webapp.exception;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.json.JSONObject;
import org.ssahayog.webapp.json.JsonProcessor;

public class ApplicationException extends Throwable{
	/**
	 * 
	 */
	public ApplicationException(final String reason) {
		super(reason);
	}
	
	public ApplicationException(final String reason, final Throwable cause) {
		super(reason, cause);
	}
	
	public JSONObject getJsonObject(){
		JSONObject resultObject = new JSONObject();
		resultObject.put(JsonProcessor.RECORD_COUNT, 0);
		resultObject.put(JsonProcessor.EXCEPTION_CAUSE, this.getMessage());
		final StringWriter s = new StringWriter();
		final PrintWriter traceWriter = new PrintWriter(s,true) ;
		printStackTrace(traceWriter);
		resultObject.put(JsonProcessor.EXCEPTION_TRACE, escape(s.toString()));
		
		return resultObject;
	}
	
	private final String escape(final String exceptionTrace){
		return exceptionTrace.toString().replace("\\","\\\\");
	}
}
