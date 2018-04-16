package database;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.KeywordController;
import analizer.ClasificadorDeSentimientos;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class DBHashTag extends DataBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(DBHashTag.class);

	public DBHashTag(ClasificadorDeSentimientos cl) {
		super(cl);
	}
	
	private String armarLineaCSV(Status status) {
		String line="";
		String tweetLimpio=""; 
		
		tweetLimpio=extractorT.preProcesarTweet(status.getText());
		
		line+=tweetLimpio+"	";//text

		line+=status.getRetweetCount()+"	";//retweets		
		line+=clasificador.clasificarTweets(tweetLimpio)+"	";//sentiment
		
		line+=status.getCreatedAt().getMonth()+"	";//date
		
//		line+=status.getGeoLocation()+"	";//latitud longitud
		
		line+=status.getUser().getScreenName()+"	";//name

//		if (status.getPlace()!=null) {
//			line+=status.getPlace().getCountry()+"	";//place donde twiteo
//		}else { line+="-	";}
		
		if (status.getUser()!=null) {
			line+=status.getUser().getLocation()+"	";//user location
		}else { line+="-	";}

		line+=status.getUser().getTimeZone()+ " \n";//user timezone

		
		return line;
	}

	@Override
	public void getTweets(String hashTag, int cantBajar) {
		// cantBajar tiene como default 500

		// Sive para Keywords tambien, "eric" @ericBana hola.

		// Puede haber lineas repetidas si es RT ...ELIMINO?
		// Esta bien dejarla es el mismo senti pero diferente persona
		// el asunto es si no tiene sentimiento como el link

		// https://twitter.com/BT21_/status/956125656034586624 retwiteado 66K
		// veces
		// ó porq una persona publica lo mismo identico

		if (cantBajar == 0) {
			cantBajar = 500;
		}

		Twitter twitter = new TwitterFactory().getInstance();
		Query query = new Query(hashTag);

		long lastID = Long.MAX_VALUE;
		ArrayList<Status> tweets = new ArrayList<Status>();
		boolean finish = false;

		while ((tweets.size() < cantBajar) && (!finish)) {

			if (cantBajar - tweets.size() > 100)
				query.setCount(100);
			else
				query.setCount(cantBajar - tweets.size());

			try {
				QueryResult result = twitter.search(query);

				if (result.getTweets().size() < query.getCount())
					finish = true;

				tweets.addAll(result.getTweets());

				LOGGER.info("Gathered " + tweets.size() + " tweets" + "\n");

				for (Status t : tweets)
					if (t.getId() < lastID)
						lastID = t.getId();
			}

			catch (TwitterException te) {
				LOGGER.error("Couldn't connect Twitter API: " + te);
			}

			query.setMaxId(lastID - 1);
		}
		
		//tratar de poner encabezado
		//writeDb("TEXT	RETWEETS	SENTIMENT	DATE	LOCATION	USER	PLACE	TIMEZONE\r\n");

		//por cada tweet recibido armo la linea que quiero almacenar
		for (Status t : tweets) {
			if (t.getLang().equals("en")) {//solo idioma ingles
				
				writeDb(armarLineaCSV(t));
				//writeDb(t.getText());
			}
			
		}

		LOGGER.info("Downloaded tweets: " + tweets.size() + ".");

	}

}
