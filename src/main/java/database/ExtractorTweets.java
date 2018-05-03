package database;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class ExtractorTweets
{

	enum CaseSensitive {
		INSENSITIVE, EQUAL
	};
	
	public ExtractorTweets(){}
	
	
	private String extraer(String tweet, String regEx, CaseSensitive cs) {

		Pattern p = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);


		// if (cs == CaseSensitive.INSENSITIVE)
		// p = Pattern.compile(regEx,Pattern.CASE_INSENSITIVE);
		// else
		// p = Pattern.compile(regEx,Pattern.CANON_EQ);

		Matcher m = p.matcher(tweet);

		while (m.find()) {

			tweet = tweet.replace(m.group(0), " ").trim();
			m = p.matcher(tweet);// VER SI SUBE MUCHO LA COMPLEJIDAD
		}

		return tweet;
	}
	
	
	private String splitHashtag(String tweet) {

		String newHashtag = "";

		String hashtagPattern = "([#][\\w_-Ã¡Ã©Ã­Ã³ÃºÃ�Ã‰Ã�Ã“Ãš]+)";
		Pattern p = Pattern.compile(hashtagPattern, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(tweet);

		while (m.find()) {
			newHashtag = "";
			String hashtag = m.group().substring(1);

			// ahora splittear el hastag encontrado

			String wordPattern = "([A-ZÃ�Ã‰Ã�Ã“Ãš]([A-ZÃ�Ã‰Ã�Ã“Ãš]+)([a-zÃ¡Ã©Ã­Ã³Ãº]+))|([A-Za-zÃ¡Ã©Ã­Ã³ÃºÃ�Ã‰Ã�Ã“Ãš]([a-zÃ¡Ã©Ã­Ã³Ãº]+))|([A-ZÃ�Ã‰Ã�Ã“Ãš]([A-ZÃ�Ã‰Ã�Ã“Ãš]+))";

			Pattern p2 = Pattern.compile(wordPattern, Pattern.CANON_EQ);
			Matcher m2 = p2.matcher(hashtag);

			while (m2.find()) {
				if (Character.isUpperCase(m2.group(0).charAt(0)) && Character.isUpperCase(m2.group(0).charAt(1))
						&& (Character.isLowerCase(m2.group().charAt(m2.group().length() - 1)))) {
					int i = 2;
					while ((i < m2.group(0).length()) && (Character.isUpperCase(m2.group(0).charAt(i)))) {

						i++;

					}
					newHashtag = newHashtag + " " + m2.group().substring(0, i - 1);
					hashtag = hashtag.replace(m2.group().substring(0, i - 1), " ").trim();
					m2 = p2.matcher(hashtag);
				} else {

					newHashtag = newHashtag + " " + m2.group();
					hashtag = hashtag.replace(m2.group(0), " ").trim();
					m2 = p2.matcher(hashtag);

				}

			}

			tweet = tweet.replace(m.group(0), newHashtag + " ").trim();
		}
		return tweet;
	}

	
	public String preProcesarTweet(String tweet) {
		// si en un tweet tengo al final "th" el remove ordinal no lo saca
		// ME SIRVE PARA ARMAR LOS TrainingDirectories,le saco los hash tag
		// tweetPreProcesado= extractor(tweetPreProcesado,"([#][\\w_-]+)" ,
		// CaseSensitive.INSENSITIVE);

		String tweetPreProcesado = tweet.replace("\n", " ").trim();
		tweetPreProcesado=tweetPreProcesado.replace("\t", " ");
		tweetPreProcesado=tweetPreProcesado.replace("	", " ");
		tweetPreProcesado=tweetPreProcesado.replace("  ", " ");
		
		// remove "RT_"
		if ((tweetPreProcesado.length()>=4)&&(tweetPreProcesado.substring(0, 3).equals("RT "))){
			tweetPreProcesado=tweetPreProcesado.substring(3);
		}

		// Remove emojins
		tweetPreProcesado = extraer(tweetPreProcesado,
				"([\\u20a0-\\u32ff\\ud83c\\udc00-\\ud83d\\udeff\\udbb9\\udce5-\\udbb9\\udcee])",
				CaseSensitive.INSENSITIVE);
		// remove Email
		tweetPreProcesado = extraer(tweetPreProcesado, "[a-zA-Z0-9._%+-]+(@)[a-zA-Z0-9.-]+[.][a-zA-Z]{2,6}",
				CaseSensitive.INSENSITIVE);
		// remove Url
		tweetPreProcesado = extraer(tweetPreProcesado,
				"((https?|ftp|gopher|telnet|file|Unsure|http):((//)|(\\\\))+[\\w\\d:#@%/;$()~_?\\+-=\\\\\\.&]*)",
				CaseSensitive.INSENSITIVE);
		// remove Mention
		tweetPreProcesado = extraer(tweetPreProcesado, "([@][\\w_:-]+)", CaseSensitive.INSENSITIVE);
		// split Hashtag
		tweetPreProcesado = splitHashtag(tweetPreProcesado);
		// remove Numbers despues de split hashtag x si el hash tenia numeros
		tweetPreProcesado = extraer(tweetPreProcesado, "([\\d]+)", CaseSensitive.INSENSITIVE);
		// remove Symbols and single letters
		tweetPreProcesado = extraer(tweetPreProcesado, "[^a-zA-ZÃ¡Ã©Ã­Ã³ÃºÃ�Ã‰Ã�Ã“Ãš'â€™Ã±Ã‘](.)[^a-zA-ZÃ¡Ã©Ã­Ã³ÃºÃ�Ã‰Ã�Ã“Ãš'â€™Ã±Ã‘]",
				CaseSensitive.INSENSITIVE);
		// remove Ordinal
		tweetPreProcesado = extraer(tweetPreProcesado, "[^a-zA-Z]+(nd|st|th|rd)[^a-zA-Z]+",
				CaseSensitive.INSENSITIVE); // y si esta pegado a Ã± o letra con
											// acento?

		return tweetPreProcesado;
	}
}
