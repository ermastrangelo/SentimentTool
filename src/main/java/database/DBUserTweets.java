package database;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class DBUserTweets extends DataBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(DBUserTweets.class);

	public DBUserTweets() {
		super();
	}

	@Override
	public void getTweets(String userName) {

		// ANDA DE 10! Baje 1400 y quiza se puede mas
		// FALTA: COMO SABER CUANTOS BAJAR

		int cantBajadaAnt = 0;
		boolean finish = false;

		Paging pg = new Paging();
		Twitter twitter = new TwitterFactory().getInstance();
		int numberOfTweets = 1000;// VER COMO HACER PARAMETRIZABLE
		long lastID = Long.MAX_VALUE;
		ArrayList<Status> tweets = new ArrayList<Status>();

		while ((tweets.size() < numberOfTweets) && (!finish)) {
			try {
				tweets.addAll(twitter.getUserTimeline(userName, pg));
				LOGGER.info("Gathered " + tweets.size() + " tweets");
				for (Status t : tweets)
					if (t.getId() < lastID)
						lastID = t.getId();
			} catch (TwitterException te) {
				System.out.println("Couldn't connect: " + te);
			}

			pg.setMaxId(lastID - 1);

			if (cantBajadaAnt == tweets.size())
				finish = true;
			cantBajadaAnt = tweets.size();
		}

		for (Status t : tweets) {
			writeDb(t.getText());
		}

		LOGGER.info("Downloaded tweets: " + tweets.size() + ".");

	}
}
