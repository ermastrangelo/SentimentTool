package database;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import algorithms.AlgoritmosClasificacion;
import analizer.ClasificadorDeSentimientos;
import ch.qos.logback.core.net.SyslogOutputStream;
import database.ExtractorTweets.CaseSensitive;
import twitter4j.Status;

public abstract class DataBase {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DataBase.class);

	BufferedWriter bw = null;
	FileWriter fw = null;
	ExtractorTweets extractorT=new ExtractorTweets();
	ClasificadorDeSentimientos clasificador;

	public DataBase(ClasificadorDeSentimientos cl) {
		// constructor solo crea e inicializa el archivo
		try {
			fw = new FileWriter("TweetsDB.csv");
			bw = new BufferedWriter(fw);

		} catch (IOException e) {
			LOGGER.error("Creating TweetsDB : " + e.getMessage());
		}
		clasificador=cl;
	}

	public String returnForQlik() {

		String lineaFinal = "";
		

		try {
			BufferedReader nuevoBuffer = new BufferedReader(new FileReader("TweetsDB.csv"));

			String line = nuevoBuffer.readLine();

			while (line != null) {
				
				//chequeo que no vengan lineas en blanco, que al agregar salto de linea generan entrada erronea para qlik
				if (line.length() > 3) {
					lineaFinal += line+"\n";
				}
				
				line = nuevoBuffer.readLine();

			}

			nuevoBuffer.close();

		} catch (FileNotFoundException e) {
			LOGGER.error("Opening TweetsDB.csv for read 1: " + e.getMessage());
		} catch (IOException e) {
			LOGGER.error("Opening TweetsDB.csv for read 1: " + e.getMessage());
		}
		return lineaFinal;

	}
	
	
	public String armarLineaCSV(Status status) {
		String line = "";
		String tweetLimpio = "";
		
		String aux="";

		if (status != null) {

			tweetLimpio = extractorT.preProcesarTweet(status.getText());

			line += tweetLimpio + "	";// texto tweet

			if (tweetLimpio.length() < 2) {
				line = "NNNNNNNNNNUUUUUUUUUULLLLLLLLLLLLLLL	";
			}

			line += status.getRetweetCount() + "	";// cantidad retweets int

			
			if (tweetLimpio.length() < 2) {
				line += "Neutral	";// tweet sentiment;
			}else
			{	
				line += clasificador.clasificarTweets(tweetLimpio) + "	";// tweet sentiment
			}
			
			

			
			if(status.getCreatedAt()!=null){
				line += status.getCreatedAt().getDay() + "	";// date day
				line += status.getCreatedAt().getMonth() + "	";// date month
				line += (status.getCreatedAt().getYear() + 1900) + "	";// date year
				line+= status.getCreatedAt()+ "	";//date
			} else {line += "-	-	-	-	";}

			if(status.getUser()!=null){
				line += status.getUser().getScreenName()+ "	";// name 
			} else {line += "-	";}
			

			if (status.getUser().getLocation() != null) {
				
				aux="";
				
				if ((status.getUser().getLocation().equals(""))||(status.getUser().getLocation().contains("Undisclosed"))){
					line += "-";					
				}else {
								
					aux=extractorT.extraer(status.getUser().getLocation(), "[^a-zA-Z�������,\\s]+",CaseSensitive.INSENSITIVE);
	
					if (aux.length() < 2) {
						line += "-";

					}else {
						line += aux;
					}

				}				
				
			} else {
				line += "-";
			}

			line=line.replace("\n", "");//text.replace("\n", "").replace("\r", ""); TAMBIEN SE PUEDE PROBAR ESO
			return line+" \n";

		} // if status !=null

		return "";
	}
	
	
	public void writeDb(String tweet) {
		//guarda en la base de datos el tweet dado

		try {
			if((tweet!=null)&(tweet.length() > 15)){
				
				bw.write(tweet);

			}
		} catch (IOException e) {
			LOGGER.error("Writing line: " + e.getMessage());
		}
	}

	public void closeFile() {
		// cierra el archivo - DB
		try {
			bw.close();
		} catch (IOException e) {
			LOGGER.error("Closing TweetsDB file: " + e.getMessage());
		}
	}

	public abstract void getTweets(String userName,int cantBajar);
	
	

}
