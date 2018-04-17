package database;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import analizer.ClasificadorDeSentimientos;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class DBUserTweets extends DataBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(DBUserTweets.class);

	public DBUserTweets(ClasificadorDeSentimientos cl) {
		super(cl);
	}

	@Override
	public void getTweets(String userName, int cantBajar) {

		// ANDA DE 10! Baje 1400 y quiza se puede mas
		// FALTA: COMO SABER CUANTOS BAJAR

		if (cantBajar == 0) {
			cantBajar = 100;
		}

		int cantBajadaAnt = 0;
		boolean finish = false;

		Paging pg = new Paging();
		Twitter twitter = new TwitterFactory().getInstance();

		long lastID = Long.MAX_VALUE;
		ArrayList<Status> tweets = new ArrayList<Status>();

		while ((tweets.size() < cantBajar) && (!finish)) {
			try {
				tweets.addAll(twitter.getUserTimeline(userName, pg));
				LOGGER.info("Gathered " + tweets.size() + " tweets");
				for (Status t : tweets)
					if (t.getId() < lastID)
						lastID = t.getId();
			} catch (TwitterException te) {
				LOGGER.error("Couldn't connect Twitter API: " + te.getMessage());
			}

			pg.setMaxId(lastID - 1);

			if (cantBajadaAnt == tweets.size())
				finish = true;
			cantBajadaAnt = tweets.size();
		}

		int bajados = cantBajar;

		String line="";
		for (Status t : tweets) {
			if (cantBajar > 0) {
				line=armarLineaCSV(t);
				if(line.length()>2) {
					writeDb(line);
				}
				cantBajar--;
			} else {
				LOGGER.info("Downloaded tweets: " + bajados + ".");
				return;
			}
		}

		LOGGER.info("Downloaded tweets: " + tweets.size() + ".");

	}
}
