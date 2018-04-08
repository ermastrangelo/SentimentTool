package controllers;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import algorithms.AlgorithmLingPipe;
import algorithms.AlgorithmStanfordCoreNLP;
import algorithms.AlgoritmosClasificacion;
import analizer.ClasificadorDeSentimientos;
import database.DBHashTag;
import database.DataBase;


//http://localhost:8080/websocket?action=Keyword&user=LeoDiCaprio&tweetId=958406889288716290&cantBajar=500

@RestController
public class WebSocketController {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(WebSocketController.class);
	
	@RequestMapping("/websocket")
	
public String getKeywords(@RequestParam Map<String, String> requestParams) {
		
		String newMessage="";
		LOGGER.info(" -------------BEGIN WEB SOCKET CONTROLLER--------------");
		
		//an action can be keywords - usertweets - replies
		String action = requestParams.get("action");
		String user = requestParams.get("user");
		String tweetId = requestParams.get("tweetId");
		String cantBajar = requestParams.get("cantBajar");

		try {
            // open websocket
            final WebsocketClientEndpoint clientEndPoint = new WebsocketClientEndpoint(new URI("ws://localhost:4848/app/test.qvf"));

            // add listener
            clientEndPoint.addMessageHandler(new WebsocketClientEndpoint.MessageHandler() {
                public void handleMessage(String message) {
                    //System.out.println(message);
                    LOGGER.info(message);
                }
            });

            // send message to websocket
            //clientEndPoint.sendMessage("{'event':'addChannel','channel':'ok_btccny_ticker'}");
            
         // wait 5 seconds for messages from websocket
            Thread.sleep(5000);
            
            // Open this test.qvf app
            newMessage="{\"jsonrpc\": \"2.0\",\"id\": 2,\"method\": \"OpenDoc\",\"handle\": -1,\"params\": [ \"C:\\\\Users\\\\Eric\\\\Documents\\\\Qlik\\\\Sense\\\\Apps\\\\test.qvf\" ]}";
            clientEndPoint.sendMessage(newMessage);
            
         // wait 5 seconds for messages from websocket
            Thread.sleep(5000);
            
            //modify Rest connection with parameters
            newMessage="{\"jsonrpc\": \"2.0\",\"id\": 10, \"method\": \"ModifyConnection\", \"handle\": 1, \"params\": [\"783c6ea1-94f2-4443-8abf-bb6eeae15fcc\", {\"qName\": \"restConnection\", \"qConnectionString\":\"CUSTOM CONNECT TO \\\"provider=QvRestConnector.exe;url=http://localhost:8080/"+action+";timeout=30;method=GET;autoDetectResponseType=true;keyGenerationStrategy=0;authSchema=anonymous;skipServerCertificateValidation=false;useCertificate=No;certificateStoreLocation=CurrentUser;certificateStoreName=My;queryParameters=id%2"+tweetId+"%1user%2"+user+"%1cantBajar%2"+cantBajar+";addMissingQueryParametersToFinalRequest=false;PaginationType=None;allowResponseHeaders=false;allowHttpsOnly=false;\\\"\", \"qType\": \"QvRestConnector.exe\"} ] }";
            clientEndPoint.sendMessage(newMessage);

            // wait 5 seconds for messages from websocket
            Thread.sleep(5000);

        } catch (InterruptedException ex) {
            System.err.println("InterruptedException exception: " + ex.getMessage());
        } catch (URISyntaxException ex) {
            System.err.println("URISyntaxException exception: " + ex.getMessage());}  

		
		
		
		

		LOGGER.info(" -------------END WEB SOCKET CONTROLLER--------------");


		return "FINALIZO WEB SOCKET V3";
		
	}
	

}
