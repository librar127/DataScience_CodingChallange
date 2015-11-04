package InsightDataScience_coding_challenge;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
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

public class average_degree {
	
	public static void main(String[] args){
		
		SimpleDateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy", Locale.ENGLISH);
		inputFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));		
		List<String> listOfHashtagsForOneMinuteWithTime = new ArrayList<String>();
		
		Calendar minWindowTime = Calendar.getInstance();
		Calendar maxWindowTime = Calendar.getInstance();
		maxWindowTime.setTimeInMillis(Long.MIN_VALUE);
		Calendar currentTimeFromLine = Calendar.getInstance();
		
		StringBuffer sb;
		String tweetLine;
		String[] tweetLineParts;
		String tweetLineTime;
		int line = 0;
		
		average_degree ad = new average_degree();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader("./tweet_output/ft1.txt"));
			PrintWriter pw = new PrintWriter(new FileWriter("./tweet_output/ft2.txt"));
			
			// Read output file ft1.txt line by line
			while((tweetLine = br.readLine()) != null){
				
				sb = new StringBuffer();
				
				if(!tweetLine.contains("#"))
					continue;
				else{
					
					try {
				
						// Split the tweetline by "(" to separate hashtags and time fields
						tweetLineParts = tweetLine.split("\\(timestamp: ");
						
						// Split the time field by ": " to get the time
						tweetLineTime = tweetLineParts[1].substring(0, tweetLineParts[1].length()-1);
						
						// Get all the hashtags from a tweetline using Java Regular Expression
						Matcher matcher = Pattern.compile("#(\\w+)").matcher(tweetLineParts[0]);		
						int noOfHashtags = 0;
						while (matcher.find()) {
							noOfHashtags += 1;
							sb.append("#"+matcher.group(1)+",");
						}
						
						sb.append(","+tweetLineTime);
						
						// Continue if no of hashtags present in line are less than 2
						if(noOfHashtags < 2){
							continue;	
						}
						
						// Adding proceed hashtags to the list of one minute window
						listOfHashtagsForOneMinuteWithTime.add(sb.toString());									
						
						// Get the formatted Time from tweetline
						currentTimeFromLine.setTime(inputFormat.parse(tweetLineTime));
						
						// Update minWindowTime for one minute Window
						if(minWindowTime.getTimeInMillis() > currentTimeFromLine.getTimeInMillis()){
							minWindowTime.setTime(currentTimeFromLine.getTime());
						}
						
						// Update maxWindowTime for one minute Window
						if(maxWindowTime.getTimeInMillis() < currentTimeFromLine.getTimeInMillis()){
							maxWindowTime.setTime(currentTimeFromLine.getTime());
						}
						
						// Check if window of tweetlines has exceeded one minute time
						if(maxWindowTime.getTimeInMillis() - minWindowTime.getTimeInMillis() > 60000){
							
							// This logic assumes that tweets are coming in increasing order of time and one line at a time
							listOfHashtagsForOneMinuteWithTime.remove(0);
							minWindowTime.setTime(inputFormat.parse(listOfHashtagsForOneMinuteWithTime.get(0).split(",,")[1]));
							
							/*// Iterate through all the hashtags line to retain only one minute window hashtags
							for(int i =0; i<listOfHashtagsForOneMinuteWithTime.size(); i++){
								
								currentTimeInLoop.setTime(inputFormat.parse(listOfHashtagsForOneMinuteWithTime.get(i).split(",,")[1]));
								
								//System.out.println(outputFormat.format(minWindowTime.getTime())+"   "+ outputFormat.format(maxWindowTime.getTime())+"   "+outputFormat.format(timeInLoop.getTime()));
								
								// Delete the hashtags which are older than one minute
								if(maxWindowTime.getTimeInMillis() - currentTimeInLoop.getTimeInMillis() > 60000){
									listOfHashtagsForOneMinuteWithTime.remove(i);
									i -= 1; 
								}else{
									
									// This logic is to update minWindowTime even in case hashtags arrive out of order
									if(timeDiffInMillis < maxWindowTime.getTimeInMillis() - currentTimeInLoop.getTimeInMillis()){
										
										timeDiffInMillis = maxWindowTime.getTimeInMillis() - currentTimeInLoop.getTimeInMillis();
										minWindowTime.setTime(currentTimeInLoop.getTime());
										
									}
								}
							}*/
						}	
						
						System.out.print(++line+"  "+sb.toString()+ "\t"+listOfHashtagsForOneMinuteWithTime.size()+"\t");
						//Pass the list of all hashtags for one minute window
						ad.calculateAverageDegree(listOfHashtagsForOneMinuteWithTime, pw);
						
					} catch (ParseException e) {
						System.out.println("Error in Date parsing: "+ e.getMessage());
					}
				}
			}
			
			// close bufferedreader and printwriter
			br.close();
			pw.close();
			
		} catch (FileNotFoundException e) {
			System.out.println("Exception: file not found: "+ e.getMessage());
		} catch (IOException e) {
			System.out.println("IOException: "+ e.getMessage());
		}
	}

	private void calculateAverageDegree(List<String> listOfHashTagsForWindow, PrintWriter pw) {
		
		Map<String, ArrayList<String>> hashmapOfTwitterGraph = new HashMap<String, ArrayList<String>>();
		ArrayList<String> listOfHashTagsForEachTweetLine;
		String[] hashTagsForEachTweetLine;
		
		// Iterating for each tweetline from 1-minute window
		for(int i =0; i<listOfHashTagsForWindow.size(); i++){
			
			// Get all the hashtags from a tweetline
			hashTagsForEachTweetLine = listOfHashTagsForWindow.get(i).split(",,")[0].split(",");
			
			// Iterating for all the hashtags present in a tweetline
			for (int j = 0; j < hashTagsForEachTweetLine.length; j++) {

				// Create an empty list of connected hashtag nodes for a hashtag node
				listOfHashTagsForEachTweetLine = new ArrayList<String>();

				// If a hashtag node is already present in a twitter hashtag graph
				if (hashmapOfTwitterGraph.containsKey(hashTagsForEachTweetLine[j])) {

					// Get its list of all connected hashtag nodes
					listOfHashTagsForEachTweetLine = hashmapOfTwitterGraph.get(hashTagsForEachTweetLine[j]);
					
					// Iterate for all the hashtag nodes in a tweetline
					for (int k = 0; k < hashTagsForEachTweetLine.length; k++) {

						// If the hashtag node is same for which we are iterating
						if (k == j){
							
							// Then skip it
							continue;
						}
						else {							
							
							// If a connected node is already present in its list of connected hashtag nodes
							if (listOfHashTagsForEachTweetLine.contains(hashTagsForEachTweetLine[k])){
									
								// Then skip it
								continue;
							}
							else{
									
								// Otherwise add its connected node to the hashtag list
								listOfHashTagsForEachTweetLine.add(hashTagsForEachTweetLine[k]);
							}

						}
					}
					
				// If a hashtag node is not already present in a twitter hashtag graph
				} else {
					
					// Get all its connected hashtag nodes excluding itself
					for (int k = 0; k < hashTagsForEachTweetLine.length; k++) {

						if (j == k) {
							
							continue;							
						} else {
							
							listOfHashTagsForEachTweetLine.add(hashTagsForEachTweetLine[k]);
						}

					}
					
					// Add current node to existing twitter hashtag graph along with all its connected hashtag nodes.
					hashmapOfTwitterGraph.put(hashTagsForEachTweetLine[j], listOfHashTagsForEachTweetLine);
				}
			}
		}
		
		double sumOfDegrees = 0;
		
		// Iterate twitter hashtag graph to calculate the sum of degree of nodes
		for(Map.Entry<String, ArrayList<String>> entry: hashmapOfTwitterGraph.entrySet()) {
			
		    sumOfDegrees += entry.getValue().size();
		}
		
		// Write the result to output file: ft2.txt
		pw.format("%.2f", sumOfDegrees / hashmapOfTwitterGraph.size());
		pw.println();
		
		System.out.format(" == Degree: %.2f\n", sumOfDegrees/hashmapOfTwitterGraph.size());
	}

}
