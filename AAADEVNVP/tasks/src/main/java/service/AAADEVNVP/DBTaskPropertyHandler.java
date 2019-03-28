package service.AAADEVNVP;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.avaya.workflow.base.db.dataaccess.DataAccessAgent;
import com.avaya.workflow.base.db.util.SqlStatementValidator;
import com.avaya.workflow.db.DBServerProfile;
import com.avaya.workflow.db.DBServerProfileSupplier;
import com.avaya.workflow.db.DBServerProfileSupplierFactory;
import com.avaya.workflow.logger.Logger;
import com.avaya.workflow.logger.LoggerFactory;
import com.google.common.base.CharMatcher;

public class DBTaskPropertyHandler {
	private Logger logger;

	public DBTaskPropertyHandler() {
		this.logger = LoggerFactory.getLogger(DBTaskPropertyHandler.class);
	}

	public String getInputDBSchema(String allInOne) {
		String[] parameters = allInOne.split(";");
		return getInputSchema(parameters[0].trim(), parameters[1].trim(), true);
	}

	public String getInputDBSchemaBody(String allInOne) {
		String[] parameters = allInOne.split(";");
		return getInputSchemaBody(parameters[0].trim(), parameters[1].trim(), parameters[2].trim(), parameters[3].trim());
	}

	public String getInputSchemaForUpdateDb(String allInOne) {
		String[] parameters = allInOne.split(";");
		return getInputSchema(parameters[0].trim(), parameters[1].trim(), false);
	}

	public String getOutputDBSchema(String allInOne) {

		try {
			String[] parameters = allInOne.split(";");
			String dbProfileName = parameters[0].trim();
			String sqlStatement = parameters[1].trim();

			DataAccessAgent dataAccessAgent = new DataAccessAgent(
					getDbServerProfileByName(dbProfileName));
			SqlStatementValidator.validate(sqlStatement, true);
			JSONObject outputSchema = dataAccessAgent.getMetaData(sqlStatement);
			if (this.logger.isFineEnabled()) {
				this.logger.fine("Output Schema is : "
						+ outputSchema.toString());
			}
			return outputSchema.toString();
		} catch (Exception exception) {
			String errorString = "Error getting Output Schema for Read DB Task";
			this.logger.error("Error getting Output Schema for Read DB Task",
					exception);
			if ((exception instanceof NullPointerException)) {
				return "Error getting Output Schema for Read DB Task: Please check the configurations and contact administrator if the problem persists.";
			}
			return "Error getting Output Schema for Read DB Task: "
					+ exception.toString();
		}
	}

	public List<String> getDBProfileIDs() {
		List<String> profileNames = new ArrayList();
		DBServerProfileSupplier dbServerProfileSupplier = DBServerProfileSupplierFactory
				.getSupplier();
		List<DBServerProfile> dbProfiles = dbServerProfileSupplier
				.getJDBCConfiguration();
		for (DBServerProfile dbProfile : dbProfiles) {
			profileNames.add(dbProfile.getDbProfileName());
		}
		return profileNames;
	}

	private String getInputSchema(String dbProfileName, String sqlStatement,
			boolean isReadDbTask) {
		try {
			DataAccessAgent dataAccessAgent = new DataAccessAgent(
					getDbServerProfileByName(dbProfileName));
			SqlStatementValidator.validate(sqlStatement, isReadDbTask);
			JSONObject inputSchema = dataAccessAgent.getParameterMetaData(
					sqlStatement, isReadDbTask);
			if (this.logger.isFineEnabled()) {
				this.logger.fine("Input Schema is : " + inputSchema.toString());
			}
			return inputSchema.toString();
		} catch (Exception exception) {
			String taskString = isReadDbTask ? " Read " : " Update ";
			String errorString = "Error getting Input Schema for" + taskString
					+ "DB Task";
			this.logger.error(errorString, exception);
			if ((exception instanceof NullPointerException)) {
				return errorString
						+ ": "
						+ "Please check the configurations and contact administrator if the problem persists.";
			}
			return errorString + ": " + exception.toString();
		}
	}

	private DBServerProfile getDbServerProfileByName(String dbProfileName) {
		DBServerProfileSupplier dbServerProfileSupplier = DBServerProfileSupplierFactory
				.getSupplier();

		return dbServerProfileSupplier.getValidDBProfileByName(dbProfileName);
	}

	private String getInputSchemaBody(String sqlStatement, String checkEmail, String checkSMS, String checkCall) {
		try {
			String sqlBody = sqlStatement;
			String titleString = "ReadBodyInputSchema";
			JSONObject inputSchema = new JSONObject();
			inputSchema.put("title", titleString);
			inputSchema.put("type", "object");

			JSONObject schemaProperties = new JSONObject();
			int countString = CharMatcher.is('?').countIn(sqlBody);
			int countNumber = CharMatcher.is('°').countIn(sqlBody);
			int email = 0;
			int sms = 0;
			int call = 0;
			int cout = 0;
			
			if (checkEmail.equals("Yes")) {
				email = 1;
			}
			if(checkSMS.equals("Yes")){
				sms = 1;
			}
			if(checkCall.equals("Yes")){
				call = 1;
			}

			int countTotal = countString + countNumber + email + sms + call;
			for (int i = 1; i < countTotal + 1; i++) {
				String parameterType = "array";
				JSONObject typeData = new JSONObject().put("type", parameterType);
				if ("array".equals(parameterType)) {
					if (email > 0) {
						typeData.put("items",
								new JSONObject().put("type", "string"));
						schemaProperties.put("inputEmail", typeData);
						inputSchema.put("properties", schemaProperties);
						email--;
						cout ++;
						continue;
					}
					if (sms > 0) {
						typeData.put("items",
								new JSONObject().put("type", "string"));
						schemaProperties.put("inputSMS", typeData);
						inputSchema.put("properties", schemaProperties);
						sms--;	
						cout ++;
						continue;
					}
					if (call > 0) {
						typeData.put("items",
								new JSONObject().put("type", "string"));
						schemaProperties.put("inputCall", typeData);
						inputSchema.put("properties", schemaProperties);
						call--;	
						cout ++;
						continue;
					}
					if (countString > 0) {
						typeData.put("items",
								new JSONObject().put("type", "string"));
						schemaProperties.put("inputParam" + (i - cout), typeData);
						inputSchema.put("properties", schemaProperties);
						countString--;
						continue;
					}
					if (countNumber > 0) {
						typeData.put("items",
								new JSONObject().put("type", "number"));
						
						schemaProperties.put("inputParam" + (i - cout), typeData);
						inputSchema.put("properties", schemaProperties);
						countNumber--;
						continue;
					}
				}
				schemaProperties.put("inputParam" + i, typeData);
				inputSchema.put("properties", schemaProperties);
			}

			return inputSchema.toString();

		} catch (Exception exception) {
			String errorString = "Error getting Input Schema for";
			if ((exception instanceof NullPointerException)) {
				return errorString
						+ ": "
						+ "Please check the configurations and contact administrator if the problem persists.";
			}
			return errorString + ": " + exception.toString();
		}
	}
}