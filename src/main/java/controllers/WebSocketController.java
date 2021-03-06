package controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import algorithms.AlgorithmLingPipe;
import algorithms.AlgorithmStanfordCoreNLP;
import algorithms.AlgoritmosClasificacion;
import analizer.ClasificadorDeSentimientos;
import database.DBHashTag;
import database.DataBase;

//http://localhost:8080/websocket?action=keywords&user=LeoDiCaprio&tweetId=958406889288716290&cantBajar=500

@RestController
public class WebSocketController {

	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketController.class);
	private static int idCount=1;
	private static int estado=0;//segun la respuesta de qlik cambio de estado, para saber cuando DoSave
	
	@CrossOrigin(origins = "http://localhost:4200")
	@RequestMapping("/websocket")
	
	public String getKeywords(@RequestParam Map<String, String> requestParams) {

		String newMessage = "";
		LOGGER.info(" -------------BEGIN WEB SOCKET CONTROLLER--------------");

		// an action can be keywords - usertweets - replies
		String action = requestParams.get("action");
		String user = requestParams.get("user");
		String tweetId = requestParams.get("tweetId");
		String cantBajar = requestParams.get("cantBajar");

		try {
			// open websocket
			final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(
					new URI("ws://localhost:4848/app/TesisApp.qvf"));
			
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}


			// add listener
			clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
				public void handleMessage(String message) {
					
					LOGGER.info("Response "+estado+" Qlik Engine: "+message);
					estado++;

					if (estado==3) {
						
						// do Save
						LOGGER.info("Request: "+estado+"- Starting to save Qlik app");
						String newMessage="{\"jsonrpc\": \"2.0\", \"id\": "+idCount+", \"method\": \"DoSave\", \"handle\": 1, \"params\": [ ] }";
						idCount++;
						clientEndPoint.sendMessage(newMessage);
						
						try {
							Thread.sleep(3000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						

					}
//					if (estado==4){
//						clientEndPoint.onClose(clientEndPoint.userSession, null);
//					}
				}
			});
			
			idCount=0;

			// wait 1 seconds = 1000 miliseconds for messages from websocket (unidad en milisegundos)
			Thread.sleep(3000);
			estado=0;
			// Open TesisApp.qvf
			LOGGER.info("Request: "+idCount+"- Starting Open Qlik engine api session");
			newMessage = "{\"jsonrpc\": \"2.0\",\"id\": "+idCount+",\"method\": \"OpenDoc\",\"handle\": -1,\"params\": [ \"C:\\\\Users\\\\Eric\\\\Documents\\\\Qlik\\\\Sense\\\\Apps\\\\TesisApp.qvf\" ]}";
			idCount++;
			clientEndPoint.sendMessage(newMessage);

			Thread.sleep(3000);

			// modify Rest connection with parameters
			LOGGER.info("Request: "+idCount+"- Starting to modify the Qlik app rest connection");
			newMessage = "{\"jsonrpc\": \"2.0\",\"id\": "+idCount+", \"method\": \"ModifyConnection\", \"handle\": 1, \"params\": [\"7b5d4372-3431-47b2-a60d-48d9fa719223\", {\"qName\": \"RestBackend\", \"qConnectionString\":\"CUSTOM CONNECT TO \\\"provider=QvRestConnector.exe;url=http://localhost:8080/"
					+ action
					+ ";timeout= "+(Integer.valueOf(cantBajar)+10)+";method=GET;autoDetectResponseType=true;keyGenerationStrategy=0;authSchema=anonymous;skipServerCertificateValidation=false;useCertificate=No;certificateStoreLocation=CurrentUser;certificateStoreName=My;queryParameters=id%2"
					+ tweetId + "%1user%2" + user + "%1cantBajar%2" + cantBajar
					+ ";addMissingQueryParametersToFinalRequest=false;PaginationType=None;allowResponseHeaders=false;allowHttpsOnly=false;\\\"\", \"qType\": \"QvRestConnector.exe\"} ] }";
			idCount++;

			clientEndPoint.sendMessage(newMessage);

			Thread.sleep(6000);//OJOOOOOOOOOOO, a veces no llega a guardar la nueva query! por eso no aparece
			
			// do Reload
			LOGGER.info("Request: "+idCount+"- Starting to reload Qlik app");
			newMessage="{\"jsonrpc\": \"2.0\",\"id\": "+idCount+",\"handle\": 1,\"method\": \"DoReload\",\"params\": {\"qMode\": 0,\"qPartial\": false,\"qDebug\": false}}";
			idCount++;
			clientEndPoint.sendMessage(newMessage);
			
			
			} catch (InterruptedException ex) {
			LOGGER.error("InterruptedException exception: " + ex.getMessage());
			System.err.println("InterruptedException exception: " + ex.getMessage());
		} catch (URISyntaxException ex) {
			LOGGER.error("URISyntaxException exception: " + ex.getMessage());
			System.err.println("URISyntaxException exception: " + ex.getMessage());
		}

		LOGGER.info(" -------------END WEB SOCKET CONTROLLER--------------");

		return "{ result: 1.3 }";

	}

}
