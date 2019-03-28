package service.AAADEVNVP;

import com.roobroo.bpm.model.BpmNode;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class NVPModel extends BpmNode {

	private static final long serialVersionUID = 1L;
	private String dbProfileId;
	private String sqlQuery;
	private String inputSchema;
	private String outputSchema;
	private String checkEmal;
	private String checkSMS;
	private String makeCall;

	public NVPModel(String name, String id) {
		super(name, id);

	}

	public String getDbProfileId() {
		return this.dbProfileId;
	}

	public void setDbProfileId(String dbProfileId) {
		this.dbProfileId = dbProfileId;
	}

	public String getSqlQuery() {
		return this.sqlQuery;
	}

	public void setSqlQuery(String sqlQuery) {
		this.sqlQuery = sqlQuery;
	}

	public String getInputSchema() {
		return this.inputSchema;
	}

	public void setInputSchema(String inputSchema) {
		this.inputSchema = inputSchema;
	}

	public String getOutputSchema() {
		return this.outputSchema;
	}

	public void setOutputSchema(String outputSchema) {
		this.outputSchema = outputSchema;
	}

	public String getCheckEmal() {
		return checkEmal;
	}

	public void setCheckEmal(String checkEmal) {
		this.checkEmal = checkEmal;
	}

	public String getCheckSMS() {
		return checkSMS;
	}

	public void setCheckSMS(String checkSMS) {
		this.checkSMS = checkSMS;
	}

	public String getMakeCall() {
		return makeCall;
	}

	public void setMakeCall(String makeCall) {
		this.makeCall = makeCall;
	}

	public boolean validateProperties(List<String> w, List<String> e) {
		boolean isValid = true;
		if (!validatethisProperty(this.sqlQuery, "Body", e)) {
			isValid = false;
		}
		
		if (!validatethisProperty(this.inputSchema, "Input schema", e)) {
			isValid = false;
		}
		
		if (!validatethisProperty(this.checkEmal, "Email", e)) {
			isValid = false;
		}
		
		if (!validatethisProperty(this.checkSMS, "SMS", e)) {
			isValid = false;
		}
		if (!validatethisProperty(this.makeCall, "Make Call", e)) {
			isValid = false;
		}
		
		return (super.validateProperties(w, e)) && (isValid);
	}

	private boolean validatethisProperty(String propertyVar,
			String propertyName, List<String> errorMessages) {
		boolean returnValue = true;
		if (StringUtils.isBlank(propertyVar)) {
			errorMessages.add("AAADEVNVP: " + propertyName
					+ " cannot be null, empty or whitespaces.");
			returnValue = false;
		}
		return returnValue;
	}



}
