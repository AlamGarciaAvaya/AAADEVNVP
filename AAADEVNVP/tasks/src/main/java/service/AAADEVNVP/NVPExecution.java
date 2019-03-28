package service.AAADEVNVP;

import java.beans.Transient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.json.JSONArray;
import org.json.JSONObject;

import com.avaya.app.entity.Instance;
import com.avaya.app.entity.NodeInstance;
import com.avaya.collaboration.call.Call;
import com.avaya.collaboration.call.CallFactory;
import com.avaya.collaboration.call.CallListenerAbstract;
import com.avaya.collaboration.call.IdentityFactory;
import com.avaya.collaboration.call.Participant;
import com.avaya.collaboration.call.media.MediaFactory;
import com.avaya.collaboration.call.media.MediaServerInclusion;
import com.avaya.collaboration.call.media.MediaService;
import com.avaya.collaboration.call.media.PlayItem;
import com.avaya.collaboration.email.EmailFactory;
import com.avaya.collaboration.email.EmailRequest;
import com.avaya.collaboration.eventing.EventingFactory;
import com.avaya.collaboration.sms.SmsFactory;
import com.avaya.collaboration.sms.SmsRequest;
import com.avaya.workflow.eventing.Event;
import com.avaya.workflow.impl.edp.util.EdpUtil;
import com.roobroo.bpm.model.BpmNode;

public class NVPExecution extends NodeInstance {

	private static final long serialVersionUID = 1L;
	// Creamos la variable del modelo.
	private NVPModel modelNVP;
	Boolean result = Boolean.valueOf(false);
	private static final String TRUE = "true";
	private static final String CALL_ANSWERED = "CALL_ANSWERED";
	private String callId;
	private String appId;
	private volatile boolean isInstanceResumed;

	public NVPExecution() {
	}

	public String getAppId() {
		return this.appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public boolean isInstanceResumed() {
		return this.isInstanceResumed;
	}

	public void setInstanceResumed(boolean isInstanceResumed) {
		this.isInstanceResumed = isInstanceResumed;
	}

	public String getCallId() {
		return this.callId;
	}

	public void setCallId(String callId) {
		this.callId = callId;
	}

	public NVPExecution(Instance instance, BpmNode node) {
		super(instance, node);
		if ((node instanceof NVPModel)) {
			this.modelNVP = ((NVPModel) node);
		} else {
			throw new IllegalStateException(
					"Error: node is not a ReadDBModel node.");
		}
	}

	public Object execute() throws Exception {
		String [] returnText = null;
		String [] returnTextbyEmail = null;
		String [] returnTextbySMS = null;
		String [] returnCall = null;
		boolean makeCallBoolean = false;
		
		/*
		 * Inicio primer try GLOBAL
		 */
		try {
			
			
			
			

			/*
			 * Obteniendo la información de los inputParams creados y asignando
			 * a un array para su correcta manipulación (inputParams).
			 */
			String[][] inputParams = null;
			int totalParamPorInput = 0;
			for (int i = 1; i < getInputData().length() + 1; i++) {

				try {
					JSONArray array = getInputData().getJSONArray(
							"inputParam" + i);
					String stringArrayTo = array.toString();
					String replaceArrayTo = stringArrayTo.replaceAll(
							"[\\[\\]\"]", "");
					String[] splitArrayTo1 = replaceArrayTo.split(",", 100);
					if (i == 1) {
						inputParams = new String[getInputData().length() + 1][splitArrayTo1.length + 1];
					}
					for (int j = 0; j < splitArrayTo1.length; j++) {

						inputParams[i][j] = splitArrayTo1[j];

					}

					totalParamPorInput = splitArrayTo1.length;
				} catch (Exception e) {
					continue;
				}

			}

			/*
			 * En caso que se afirme que se desea enviar email
			 */
			if (getReadDBModel().getCheckEmal().equals("Yes")) {
				
				
				/* Extrayendo la información de Email */
				
				
				String[] splitArrayTo1Email = null;
				
				
				for (int i = 1; i < getInputData().length() + 1; i++) {
					try {
						JSONArray arrayEmail = getInputData().getJSONArray(
								"inputEmail");
						String stringArrayToEmail = arrayEmail.toString();
						String replaceArrayToEmail = stringArrayToEmail
								.replaceAll("[\\[\\]\"]", "");
						splitArrayTo1Email = replaceArrayToEmail
								.split(",", 100);

					} catch (Exception e) {
						continue;
					}

				}

				/*
				 * Buscando los caracteres ? y ° para sustituir por los valores
				 * de entrada (inputParams)
				 */
				String BuscandoInterrogación = "?";
				String BuscandoDegree = "°";
				char[] vector = getReadDBModel().getSqlQuery().toCharArray();

				/*
				 * for, de acuerdo a la cantidad de correos existentes en
				 * inputEmail
				 */
				returnText = splitArrayTo1Email;
				returnTextbyEmail = splitArrayTo1Email;
				for (int k = 0; k < splitArrayTo1Email.length; k++) {
					/*
					 * Enviando Email de acuerdo a la cantidad de email
					 * registrados
					 */
					EmailRequest emailRequest = EmailFactory
							.createEmailRequest();

					List<String> toList = new ArrayList<String>();
					if (!EmailValidator.getInstance().isValid(
							splitArrayTo1Email[k])) {
						throw new IllegalArgumentException(
								"Invalid sender email " + splitArrayTo1Email[k]);
					} else {

						toList.add(splitArrayTo1Email[k]);
						emailRequest.getTo().addAll(toList);
					}

					emailRequest.setFrom("lab194@collaboratory.avaya.com");
					emailRequest
							.setSubject("Notificación Vencimiento de Pago Número: "
									+ k);
					int countArray = 1;
					StringBuilder sb = new StringBuilder();

					try {
						for (int a = 0; a < getReadDBModel().getSqlQuery()
								.length(); a++) {
							String params = String.valueOf(vector[a]);
							if (BuscandoInterrogación.equalsIgnoreCase(params)) {

								if (inputParams[countArray][k] == null) {
									break;
								} else {
									sb.append(inputParams[countArray][k]);
									countArray++;
									continue;
								}

							}
							if (BuscandoDegree.equalsIgnoreCase(params)) {
								if (inputParams[countArray][k] == null) {
									break;
								} else {
									sb.append(inputParams[countArray][k]);
									countArray++;
									continue;
								}
							}

							sb.append(params);
							
						}

						emailRequest.setTextBody(sb.toString());
						returnTextbyEmail[k] = sb.toString();

					} catch (Exception e) {
						continue;
					}
					emailRequest.setListener(new SampleEmailListener());
					emailRequest.send();
				}

				this.result = Boolean.valueOf(true);

			}/* Fin if validando Email */

			/*
			 * En caso que se afirme que se desea enviar sms
			 */
			if (getReadDBModel().getCheckSMS().equals("Yes")) {

				String[] splitArrayTo1SMS = null;
				/*
				 * for, para obtener los registros de inputSMS y almacenarlos en
				 * splitArrayTo1SMS
				 */
				for (int i = 1; i < getInputData().length() + 1; i++) {
					try {
						JSONArray arraySMS = getInputData().getJSONArray(
								"inputSMS");
						String stringArrayToSMS = arraySMS.toString();
						String replaceArrayToSMS = stringArrayToSMS.replaceAll(
								"[\\[\\]\"]", "");
						splitArrayTo1SMS = replaceArrayToSMS.split(",", 100);

					} catch (Exception e) {
						continue;
					}

				}

				String BuscandoInterrogación = "?";
				String BuscandoDegree = "°";
				char[] vector = getReadDBModel().getSqlQuery().toCharArray();
				
				returnText = splitArrayTo1SMS;
				returnTextbySMS = splitArrayTo1SMS;
				
				for (int k = 0; k < splitArrayTo1SMS.length; k++) {

					int countArray = 1;
					StringBuilder sb = new StringBuilder();
					SmsRequest smsRequest;

					try {
						for (int a = 0; a < getReadDBModel().getSqlQuery()
								.length(); a++) {
							String params = String.valueOf(vector[a]);
							if (BuscandoInterrogación.equalsIgnoreCase(params)) {

								if (inputParams[countArray][k] == null) {
									break;
								} else {
									sb.append(inputParams[countArray][k]);
									countArray++;
									continue;
								}

							}
							if (BuscandoDegree.equalsIgnoreCase(params)) {
								if (inputParams[countArray][k] == null) {
									break;
								} else {
									sb.append(inputParams[countArray][k]);
									countArray++;
									continue;
								}
							}

							sb.append(params);
						}

						// Creación Mensaje!!
						smsRequest = SmsFactory.createSmsRequest(
								splitArrayTo1SMS[k], sb.toString());
						smsRequest.setSender("13212046780");
						smsRequest.setListener(new MySmsListener(smsRequest));
						returnTextbySMS[k] = sb.toString();

					} catch (Exception e) {
						continue;
					}
					// Ejecutar
					smsRequest.send();
				}


				this.result = Boolean.valueOf(true);
				
				
				
			}/* Fin if validando SMS */

			/*
			 * En caso que se afirme que se desea realizar llamada
			 */
			
			
			if (getReadDBModel().getMakeCall().equals("Yes")) {
				String[] splitArrayToMekeCall = null;
				/*
				 * for, para obtener los registros de inputCall y almacenarlos
				 * en splitArrayToMekeCall
				 */
				for (int i = 1; i < getInputData().length() + 1; i++) {
					try {
						JSONArray arrayCall = getInputData().getJSONArray(
								"inputCall");
						String stringArrayToCall = arrayCall.toString();
						String replaceArrayToCall = stringArrayToCall
								.replaceAll("[\\[\\]\"]", "");
						splitArrayToMekeCall = replaceArrayToCall.split(",",
								100);

					} catch (Exception e) {
						continue;
					}

				}
				
				makeCallBoolean = true;

			}// FINAL del IF para hacer llamada

			/*
			 * Final primer try GLOBAL
			 */
		} catch (Exception exception) {
			throw new IllegalStateException(exception.getMessage(), exception);
		}
		
		if (getReadDBModel().getCheckSMS().equals("Yes")){
			returnText = returnTextbySMS;
		}
		if(getReadDBModel().getCheckEmal().equals("Yes")){
			returnText = returnTextbyEmail;
		}
		if(getReadDBModel().getCheckSMS().equals("Yes") && getReadDBModel().getCheckEmal().equals("Yes")){
			returnText = returnTextbyEmail;
		}
		
		

		JSONObject output = new JSONObject();
		JSONObject itemCall = new JSONObject();
		if (this.result.booleanValue()) {
			output.put("status", NodeInstance.Status.SUCCESS.toString());
			output.put("text", returnText);
			if(makeCallBoolean == true){
				output.put("call", "true");
			}else{
				output.put("call", "false");
			}
			
		} else {
			output.put("status", NodeInstance.Status.FAILED.toString());
			output.put("text", returnText);
			if(makeCallBoolean == true){
				output.put("call", "true");
			}else{
				output.put("call", "false");
			}

		}
		return output;

	}

	@Transient
	public NVPModel getReadDBModel() {
		if (this.modelNVP == null) {
			this.modelNVP = ((NVPModel) getNode());
		}
		return this.modelNVP;
	}

}
