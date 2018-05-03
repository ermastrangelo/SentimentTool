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

				lineaFinal += line+"\n";
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
		String line="";
		String tweetLimpio=""; 
		
		tweetLimpio=extractorT.preProcesarTweet(status.getText());
		
		
		
		line+=tweetLimpio+"	";//text
		
		if (tweetLimpio.length()<2){
			line="-	-	-	-	-	-	-	-	- \n";
			return line;			
		}

		line+=status.getRetweetCount()+"	";//retweets		
		
		line+=clasificador.clasificarTweets(tweetLimpio)+"	";//sentiment
		
		
		line+=status.getCreatedAt().getDay()+"	";//date day		
		line+=status.getCreatedAt().getMonth()+"	";//date month
		line+=(status.getCreatedAt().getYear()+1900)+"	";//date year

		//line+=status.getCreatedAt()+"	";//date time
		
		line+=status.getUser().getScreenName().replace("	", " ")+"	";//name

//		if (status.getPlace()!=null) {
//			line+=status.getPlace().getCountry()+"	";//place donde twiteo
//		}else { line+="-	";}
		
		if (status.getUser()!=null) {
			
			line+=status.getUser().getLocation().replace("	", " ")+"	";//user location
			
		}else { line+="-	";}
		
		
//		if (status.getGeoLocation()!=null) {
//			line+= status.getGeoLocation()+"	";//latitud longitud
//		}else { line+="-	";}
		

		line+=status.getUser().getTimeZone().replace("	", " ")+ " \n";//user timezone

		System.out.println(line.replace("	", "$"));
		
		return line;
	}
	
	
	public void writeDb(String tweet) {
		//guarda en la base de datos el tweet dado

		try {
			if((tweet!=null)&(tweet.length() > 1)){
				//bw.write(tweet+ "\n");
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
