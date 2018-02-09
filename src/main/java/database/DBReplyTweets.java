package database;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.UserTweetsController;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;

public class DBReplyTweets extends DataBase {

	private static final Logger LOGGER = LoggerFactory.getLogger(DBReplyTweets.class);

	long tweetId;

	public DBReplyTweets(String tID) {
		super();
		tweetId = Long.parseLong(tID, 10);
	}

	@Override
	public void getTweets(String userName) {
		// ANDA DE 10
		// FALTA: RESOLVER COMO PASARLE EL ID del tweet
		// 956518245154148352L
		Twitter twitter = TwitterFactory.getSingleton();

		Query query = new Query("[to:" + userName + "]");
		query.count(100);
		QueryResult result = null;

		int coments = 1;
		int numBajados = 0;
		boolean finish = false;

		while (!(finish) && (coments != 0)) {
			result = null;

			try {
				result = (twitter.search(query));

			} catch (TwitterException e) {

				e.printStackTrace();
			}

			coments = 0;

			if (result != null) {

				for (Status status : result.getTweets()) {

					if (status.getInReplyToStatusId() == tweetId) {

						writeDb(status.getText());
						coments++;
						numBajados++;
					}

				}

				if (result.nextQuery() != null) {
					query = result.nextQuery();
				} else {
					finish = true;
				}
			}

		}
		LOGGER.info("Downloaded tweets: " + numBajados + ".");

	}

}
