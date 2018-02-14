package database;

import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.KeywordController;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class DBHashTag extends DataBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(DBHashTag.class);

	public DBHashTag() {
		super();
	}

	@Override
	public void getTweets(String hashTag,int cantBajar) {
		//cantBajar tiene como default 500
		
		// Sive para Keywords tambien, "eric" @ericBana hola.

		// Puede haber lineas repetidas si es RT ...ELIMINO?
		// Esta bien dejarla es el mismo senti pero diferente persona
		// el asunto es si no tiene sentimiento como el link

		// https://twitter.com/BT21_/status/956125656034586624 retwiteado 66K
		// veces
		// ó porq una persona publica lo mismo identico
		
		if (cantBajar==0){cantBajar=500;}
		
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
				System.out.println("Couldn't connect: " + te);
			}

			query.setMaxId(lastID - 1);
		}

		for (Status t : tweets) {
			writeDb(t.getText());
		}
		
		LOGGER.info("Downloaded tweets: " + tweets.size() + ".");

	}

}
