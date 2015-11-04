package InsightDataScience_coding_challenge;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONObject;

public class average_degree {
	
	CharsetEncoder asciiEncoder = Charset.forName("US-ASCII").newEncoder(); 
	
	public static void main(String[] args){
		
		if(args.length < 1){
			System.out.println("Usage: provide filename containing tweets");
			System.exit(0);
		}
		
		average_degree ad = new average_degree();
		Charset.forName("US-ASCII").newEncoder(); 
		
		SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
		inputFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));		
		List<String> listOfHashtagsForOneMinuteWithTime = new ArrayList<String>();
		
		Calendar minWindowTime = Calendar.getInstance();
		Calendar maxWindowTime = Calendar.getInstance();
		maxWindowTime.setTimeInMillis(Long.MIN_VALUE);
		Calendar currentTimeFromLine = Calendar.getInstance();						
		Calendar currentTimeInLoop = Calendar.getInstance();
		
		StringBuffer sb;
		String[] cleanedTweetLine;
		String tweetLine;
		
		try {
			//BufferedReader br = new BufferedReader(new FileReader("./tweet_input/tweet.txt"));
			BufferedReader br = new BufferedReader(new FileReader(args[0]));
			PrintWriter pw = new PrintWriter(new FileWriter("./tweet_output/ft2.txt"));
			
			/**
			 * Read output file ft1.txt line by line
			 */
			while((tweetLine = br.readLine()) != null){
				
				cleanedTweetLine = ad.cleanTweets(tweetLine);
				
				if(cleanedTweetLine[0] == null && cleanedTweetLine[1] == null){
					continue;
				}
				
				//System.out.println(cleanedTweetLine[0]+" "+cleanedTweetLine[1]);
								
				sb = new StringBuffer();
				
				/**
				 * Skip a tweet line if it does not contains any hashtags
				 */
				if(!cleanedTweetLine[0].contains("#"))
					continue;
				else{
					
					try {
						
						/**
						 * Get all the hashtags from a tweetline using Java Regular Expression
						 */
						Matcher matcher = Pattern.compile("#(\\w+)").matcher(cleanedTweetLine[0]);	
						while (matcher.find()) {
							sb.append("#"+matcher.group(1)+",");
						}
						
						sb.append(","+cleanedTweetLine[1]);
												
						/**
						 * Adding proceed hashtags to the list of one minute window
						 */
						listOfHashtagsForOneMinuteWithTime.add(sb.toString());									
						
						/**
						 * Get the formatted Time from tweetline
						 */
						currentTimeFromLine.setTime(inputFormat.parse(cleanedTweetLine[1]));
						
						/**
						 * Update minWindowTime for one minute Window
						 */
						if(minWindowTime.getTimeInMillis() > currentTimeFromLine.getTimeInMillis()){
							minWindowTime.setTime(currentTimeFromLine.getTime());
						}
						
						/**
						 * Update maxWindowTime for one minute Window						  
						 */
						if(maxWindowTime.getTimeInMillis() < currentTimeFromLine.getTimeInMillis()){
							maxWindowTime.setTime(currentTimeFromLine.getTime());
						}
						
						/**
						 * Check if window of tweetlines has exceeded one minute time
						 */
						if(maxWindowTime.getTimeInMillis() - minWindowTime.getTimeInMillis() > 60000){
							
							listOfHashtagsForOneMinuteWithTime.remove(0);
							minWindowTime.setTime(inputFormat.parse(listOfHashtagsForOneMinuteWithTime.get(0).split(",,")[1]));													
														
							long timeDiffInMillis = -1L;	
							
							/**
							 * Iterate through all the hashtags line to retain only one minute window hashtags
							 */
							for(int i =0; i<listOfHashtagsForOneMinuteWithTime.size(); i++){
								
								currentTimeInLoop.setTime(inputFormat.parse(listOfHashtagsForOneMinuteWithTime.get(i).split(",,")[1]));
								
								/**
								 * Delete the hashtags which are older than one minute from list
								 */
								if(maxWindowTime.getTimeInMillis() - currentTimeInLoop.getTimeInMillis() > 60000){
									listOfHashtagsForOneMinuteWithTime.remove(i);
									i -= 1; 
								}else{
									
									/**
									 * This logic is to update minWindowTime even in case hashtags arrive out of order
									 */
									if(timeDiffInMillis < maxWindowTime.getTimeInMillis() - currentTimeInLoop.getTimeInMillis()){
										
										timeDiffInMillis = maxWindowTime.getTimeInMillis() - currentTimeInLoop.getTimeInMillis();
										minWindowTime.setTime(currentTimeInLoop.getTime());
										
									}
								}
							}
						}	
						
						/**
						 * Pass the list of all hashtags for one minute window
						 */
						ad.calculateAverageDegree(listOfHashtagsForOneMinuteWithTime, pw);
						
					} catch (ParseException e) {
						System.out.println("Error in Date parsing: "+ e.getMessage());
					}
				}
			}
			
			System.out.println("Output written to tweet_output/ft2.txt\n");
			
			/**
			 * Close bufferedreader and printwriter
			 */
			br.close();
			pw.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Exception: file not found: "+ e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException: "+ e.getMessage());
		}
	}

	/**
	 * This method is used to check if a String is Pure ASCII
	 * 
	 * @param 	str 	Input string provided to check if it contains unicode characters
	 * @return  boolean Returns true in case string is pure ASCII else false
	 */
	public boolean isPureAscii(String str) {
		
		return asciiEncoder.canEncode(str);
	}
	
	/**
	 * This methods cleans tweets after removing escape characters and unicode characters and then separating text and timestamp.
	 * 
	 * @param tweetLine One Single Line of tweets
	 * @return List of cleaned Text and timestamp after separating them from tweetline
	 */
	
	private String[] cleanTweets(String tweetLine) {
		
		average_degree ad = new average_degree();
		
		JSONObject obj = new JSONObject(tweetLine);
		String text = null;
		String timestamp = null;
		String[] textAndTimestamp = new String[2];
		
		/**
		 * Get the text field from json tweet only if it is present
		 */
		if(obj.has("text")){
			
			/**
			 * Remove all Escape characters from String line
			 */
			text = obj.getString("text").replaceAll("[\\n\\r\\t]", "").replaceAll("(?<=/)[^/]+?(?=/)", "");
			
			/**
			 * Count and remove unicode characters
			 */
			if(!ad.isPureAscii(text)){				
				
				text = text.replaceAll("[^\\p{ASCII}]", "");
			}
		}
		
		/**
		 * Get the created_at field from json tweet only if it is present
		 */
		if(obj.has("created_at")){
			timestamp = obj.getString("created_at").replaceAll("(?<=/)[^/]+?(?=/)", "");
		}

		textAndTimestamp[0] = text;
		textAndTimestamp[1] = timestamp;
		
		return textAndTimestamp;
	}


	/**
	 * This method calculates the average degree of twitter hashtag graph for one minute
	 * window. After calculating the average degree it wites the output to tweet_output/ft2.txt file.
	 *
	 * @param  listOfHashTagsForWindow  A list of hashtags for window of one minute
	 * @param  pw 						PrintWriter object
	 * @return void
	 */
	private void calculateAverageDegree(List<String> listOfHashTagsForWindow, PrintWriter pw) {
		
		Map<String, ArrayList<String>> hashmapOfTwitterGraph = new HashMap<String, ArrayList<String>>();
		ArrayList<String> listOfHashTagsForEachTweetLine;
		String[] hashTagsForEachTweetLine;
		
		/**
		 * Iterating for each tweetline from 1-minute window
		 */
		for(int i =0; i<listOfHashTagsForWindow.size(); i++){
			
			/**
			 * Get all the hashtags from a 1-minute window of hashtags
			 */
			hashTagsForEachTweetLine = listOfHashTagsForWindow.get(i).split(",,")[0].split(",");
			
			/**
			 * If there is only hashtag node then skip it
			 */
			if(hashTagsForEachTweetLine.length == 1){
				continue;
			}
			
			/**
			 * Iterating for all the hashtags present in a tweetline
			 */
			for (int j = 0; j < hashTagsForEachTweetLine.length; j++) {

				/**
				 * Create an empty list of connected hashtag nodes for a hashtag node
				 */
				listOfHashTagsForEachTweetLine = new ArrayList<String>();

				/**
				 * If a hashtag node is already present in a twitter hashtag graph
				 */
				if (hashmapOfTwitterGraph.containsKey(hashTagsForEachTweetLine[j])) {

					/**
					 * Get the list of all connected hashtag nodes for a hashtag node
					 */
					listOfHashTagsForEachTweetLine = hashmapOfTwitterGraph.get(hashTagsForEachTweetLine[j]);
					
					/**
					 * Iterate for all the hashtag nodes in a tweetline
					 */
					for (int k = 0; k < hashTagsForEachTweetLine.length; k++) {

						/**
						 * If the hashtag node is same for which we are iterating
						 */
						if (k == j){
							
							// Then skip it
							continue;
						}
						else {							
							
							/**
							 * If a connected node is already present in its list of connected hashtag nodes
							 */
							if (listOfHashTagsForEachTweetLine.contains(hashTagsForEachTweetLine[k])){
									
								/**
								 * Then skip it
								 */
								continue;
							}
							else{
									
								/**
								 * Otherwise add its connected node to the hashtag list
								 */
								listOfHashTagsForEachTweetLine.add(hashTagsForEachTweetLine[k]);
							}

						}
					}
					
				/**
				 * If a hashtag node is not already present in a twitter hashtag graph
				 */
				} else {
					
					/**
					 * Get all its connected hashtag nodes excluding itself
					 */
					for (int k = 0; k < hashTagsForEachTweetLine.length; k++) {

						if (j == k) {
							
							continue;							
						} else {
							
							listOfHashTagsForEachTweetLine.add(hashTagsForEachTweetLine[k]);
						}

					}
					
					/**
					 * Add current node to existing twitter hashtag graph along with all its connected hashtag nodes.
					 */
					hashmapOfTwitterGraph.put(hashTagsForEachTweetLine[j], listOfHashTagsForEachTweetLine);
				}
			}
		}
		
		double sumOfDegrees = 0;
		
		/**
		 * Iterate twitter hashtag graph to calculate the sum of degree of nodes
		 */
		for(Map.Entry<String, ArrayList<String>> entry: hashmapOfTwitterGraph.entrySet()) {
			
		    sumOfDegrees += entry.getValue().size();
		}
		
		/**
		 * Write the result to output file: tweet_output/ft2.txt
		 */
		pw.format("%.2f", sumOfDegrees / hashmapOfTwitterGraph.size());
		pw.println();
	}

}
