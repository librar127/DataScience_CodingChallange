# InsightDataScience Coding Challange

## Dependencies:

1. 	I have used org.json library for parsing input tweets provided in json format

## Directory Hierarchy

	Directory hierarchy is same as mentioned in instuctions provided in README.md file except one additional jar file added into my GITHUB repository. 
	This jar file includes all the library dependencies for running java code. For example org.json: json parser library in java. My run.sh script runs using this jar file only.

## Assumption for implementing feature 2

1. 	First Assumption made is that tweetline can come out of order of time. Means a tweetline which has lower timestamp comes after a tweetline which has higher timestamp. 
	However tweetlines are comming in order of time only. This situation is handled just in case any scenario occurs.
	
2. 	Second assumption is that while running average_degree.java, output is written to ft2.txt file for each line. For very first line it give some output (average degree) writes 
	it to ft2.txt. When second line comes, it checks for window duration calculates average degree for two lines hashtags (in case they fall in 1-minute window) and writes the output. 
	Third time when a new line comes, it check the window duration calculates average degree for three lines (in case they fall in one minute  window) and writes to ft2.txt and so...
