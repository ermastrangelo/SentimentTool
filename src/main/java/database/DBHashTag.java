package database;
import java.util.ArrayList;

import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class DBHashTag extends DataBase {

	public DBHashTag() {
		super();
	}

	@Override
	public void getTweets(String hashTag) {
		//Sive para Keywords tambien, "eric" @ericBana hola.
		
		//Puede haber lineas repetidas si es RT ...ELIMINO?
		//Esta bien dejarla es el mismo senti pero diferente persona
		//el asunto es si no tiene sentimiento como el link
		
		//https://twitter.com/BT21_/status/956125656034586624 retwiteado 66K veces
		
		//O porq una persona publica lo mismo identico
		

		Twitter twitter = new TwitterFactory().getInstance();
		Query query = new Query(hashTag);
		int numberOfTweets = 1000;
		long lastID = Long.MAX_VALUE;
		ArrayList<Status> tweets = new ArrayList<Status>();
		boolean finish=false;

		while ((tweets.size() < numberOfTweets)&& (!finish)) {

			if (numberOfTweets - tweets.size() > 100)
				query.setCount(100);
			else
				query.setCount(numberOfTweets - tweets.size());

			try {
				QueryResult result = twitter.search(query);
				
				if (result.getTweets().size()<query.getCount())
					finish=true;
				
				tweets.addAll(result.getTweets());
				
				System.out.println("Gathered " + tweets.size() + " tweets" + "\n");
				
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
			System.out.println(t.getText());
			writeDb(t.getText());
		}

	}

}
