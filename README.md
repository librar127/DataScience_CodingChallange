# InsightDataScience Coding Challange

## Dependencies:

1. 	org.json library has been used for parsing input tweets provided into json format.

## Directory Hierarchy

1.	Directory hierarchy is same as mentioned in instuctions provided in README.md file of challange except one additional jar file added into my GITHUB repository. This jar file 
	includes all the library dependencies required for running java code. For example org.json: json parser library in java. run.sh script is being run using this jar file only.

## Assumption for implementing feature 2

1. 	Althought tweets are comming in order of time only. This situation of being out of order is also handled just in case any such scenario occurs.
	
2. 	Second assumption is that while running average_degree.java, output is written to tweet_output/ft2.txt file for each line which contains more than 1 hashtags. 
	For very first line it calculates average degree and writes it to ft2.txt. When second line comes, it checks for window duration calculates average degree for 
	two lines hashtags (in case they fall in 1-minute window) and writes the output. Third time when a new line comes, it check the window duration calculates average 
	degree for three lines (in case they fall in one minute  window) and writes to ft2.txt and so... I think this is what we needed as output. 
