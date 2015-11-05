# InsightDataScience Coding Challange

## Dependencies:

1. 	org.json library has been used for parsing input tweets provided into json format. This library needs to be required for runninh my implementaion. I have included jar file in a directory called IDS_Coding_Challange_lib/json-20140107.jar.

## Directory Hierarchy

1.	Directory hierarchy is same as mentioned in instuctions provided in README.md file of challange except one additional jar file IDS_Coding_Challange.jar added into my GITHUB repository and one external library for json parsing. This jar file includes all the library dependencies required for running java code. For example org.json: json parser library in java. run.sh script is being run using this jar file only.

## Assumption for implementing feature 2

1. 	It is assumed that tweets will be comming in order of time.
	
2. 	Second assumption is that while running average_degree.java, output is written to tweet_output/ft2.txt file for each line which contains more than 1 hashtags. 
	For very first line it calculates average degree and writes it to ft2.txt. When second line comes, it checks for window duration calculates average degree for 
	two lines hashtags (in case they fall in 1-minute window) and writes the output. Third time when a new line comes, it check the window duration calculates average 
	degree for three lines (in case they fall in one minute  window) and writes to ft2.txt and so... I think this is what we needed as output. 

3.	In case of there is only one hashtag node present in window of one minute and that hashtag is not connected to any other hashtag node. In tis case average degree 0.00 is being returned as no of node connected(0)/total nodes present(1) = 0.
