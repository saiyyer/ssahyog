package org.ssahayog.webapp.json;

import java.io.IOException;
import java.io.StringWriter;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.SimpleDateFormat;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.ssahayog.webapp.dao.vo.BasicVO;
import org.ssahayog.webapp.exception.ApplicationException;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonProcessor {

	public static final SimpleDateFormat dateFormatter = new SimpleDateFormat(
			"yyyyMMdd HH:mm:ss.SSS");
	private static ObjectMapper objectMapper = new ObjectMapper();
	private static JsonFactory jsonFactory = new JsonFactory();

	public static final String ROOT = "queryresult";

	public static final String STATUS = "status";
	public static final String STATUS_SUCCESS = "success";
	public static final String STATUS_EMPTY = "empty";
	public static final String STATUS_ERROR = "error";

	public static final String RESULT = "result";
	public static final String RECORD_COUNT = "recordcount";

	public static final String MESSAGE = "message";

	public static final String RECORDS = "records";

	public static final String EXCEPTION = "exception";
	public static final String EXCEPTION_CAUSE = "cause";
	public static final String EXCEPTION_TRACE = "trace";
	
	@Deprecated
	public static final JSONObject createResultSet(final ResultSet resultSet) {
		final JSONObject rootObject = new JSONObject();
		rootObject.put(STATUS, STATUS_EMPTY);
		final JSONObject resultObject = new JSONObject();
		rootObject.put(RESULT, resultObject);
		try {
			if (resultSet != null && !resultSet.isAfterLast()) {

				final ResultSetMetaData metaData = resultSet.getMetaData();
				final int columnCount = metaData.getColumnCount();
				final int[] columnTypes = new int[columnCount];
				rootObject.put(STATUS, STATUS_SUCCESS);

				int recordCount = 0;
				final String[] columnNames = new String[columnCount];
				for (int i = 1; i <= columnCount; i++) {
					columnNames[i - 1] = metaData.getColumnLabel(i);
					columnTypes[i - 1] = metaData.getColumnType(i);
				}
				final JSONArray resultSetArray = new JSONArray();
				while (resultSet.next()) {
					recordCount++;
					JSONObject record = new JSONObject();
					for (int i = 0; i < columnCount; i++) {
						switch (columnTypes[i]) {
						case Types.INTEGER:
						case Types.SMALLINT:
						case Types.BIGINT:
						case Types.BIT:
							record.put(columnNames[i],
									resultSet.getInt(columnNames[i]));
							break;
						case Types.DOUBLE:
						case Types.FLOAT:
						case Types.DECIMAL:
							record.put(columnNames[i],
									resultSet.getDouble(columnNames[i]));
							break;
						case Types.BOOLEAN:
							record.put(columnNames[i],
									resultSet.getBoolean(columnNames[i]));
							break;
						case Types.DATE:
							record.put(columnNames[i], formattedDate(resultSet
									.getDate(columnNames[i])));
							break;
						case Types.TIME:
						case Types.TIMESTAMP:
							record.put(columnNames[i], formattedDate(resultSet
									.getTimestamp(columnNames[i])));
							break;
						case Types.CHAR:
						case Types.VARCHAR:
							record.put(columnNames[i],
									resultSet.getString(columnNames[i]));
							break;
						}
					}
					resultSetArray.put(record);
				}
				resultObject.put(RECORD_COUNT, recordCount);
				resultObject.put(RECORDS, resultSetArray);
			}
		} catch (JSONException e) {
			rootObject.put(STATUS, STATUS_ERROR);
			resultObject.put(RECORD_COUNT, 0);
			rootObject.put(
					EXCEPTION,
					new ApplicationException(
							"Json Exception while populating recordset - "
									+ e.getMessage(), e).getJsonObject());
		} catch (SQLException e) {
			rootObject.put(STATUS, STATUS_ERROR);
			resultObject.put(RECORD_COUNT, 0);
			rootObject.put(
					EXCEPTION,
					new ApplicationException(
							"SQL Exception while populating recordset - "
									+ e.getMessage(), e).getJsonObject());
		}
		return rootObject;
	}

	private static final String formattedDate(final Date date) {
		return date == null ? "null" : formattedDate(new Timestamp(
				date.getTime()));
	}

	private static final String formattedDate(final Timestamp dateStamp) {
		String strDate = "null";
		if (dateStamp != null) {
			strDate = dateFormatter.format(dateStamp);
		}
		return strDate;
	}

	public static final <T extends BasicVO> String toJson(final List<T> vos, boolean prettyPrint) {
		StringWriter sw = new StringWriter();

		try {
			JsonGenerator jg = jsonFactory.createGenerator(sw);
			if (prettyPrint) {
				jg.useDefaultPrettyPrinter();
			}
			objectMapper.writeValue(jg, vos);
		} catch ( IOException  e) {
			e.printStackTrace();
		}
		return sw.toString();

	}

	public static final <T extends BasicVO> String toJson(final T vo,
			final boolean prettyPrint) {
		StringWriter sw = new StringWriter();

		try {
			JsonGenerator jg = jsonFactory.createGenerator(sw);
			if (prettyPrint) {
				jg.useDefaultPrettyPrinter();
			}
			objectMapper.writeValue(jg, vo);
		} catch ( IOException  e) {
			e.printStackTrace();
		}
		return sw.toString();
	}

	public static final JSONObject createStatusMessage(final boolean isSuccess,
			final String message) {
		final JSONObject statusObject = new JSONObject();
		statusObject.put(STATUS, isSuccess ? STATUS_SUCCESS : STATUS_ERROR);
		statusObject.put(RESULT, new JSONObject().put(MESSAGE, message));

		return statusObject;
	}

	public static final JSONObject createErrorMessage(
			final ApplicationException exception) {
		return createErrorMessage("Exception while processing request..",
				exception);
	}

	public static final JSONObject createErrorMessage(final String message,
			final ApplicationException exception) {
		final JSONObject exceptionObject = new JSONObject();
		exceptionObject.put(STATUS, STATUS_ERROR);
		exceptionObject.put(RESULT, exception.getJsonObject());
		((JSONObject) exceptionObject.get(RESULT)).put(message, message);
		return exceptionObject;
	}

	public static final JSONObject createEmptyMessage() {
		final JSONObject emptyResult = new JSONObject();
		emptyResult.put(STATUS, STATUS_EMPTY);
		emptyResult.put(RESULT, new JSONObject().put(RECORD_COUNT, "0"));
		return emptyResult;
	}

}
