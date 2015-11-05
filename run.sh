# example of the run script for running the java code from jar

# I'll execute my programs, with the input directory tweet_input and output the files in the directory tweet_output
echo -e "\n--------Executing tweets_cleaned.java --------"
java -cp IDS_Coding_Challange.jar InsightDataScience_coding_challenge.tweets_cleaned ./tweet_input/tweets.txt

echo -e "\n--------Executing average_degree.java --------"
java -cp IDS_Coding_Challange.jar InsightDataScience_coding_challenge.average_degree ./tweet_input/tweets.txt
