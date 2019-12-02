package edu.ucdavis.ecs160.hw3;
import java.util.function.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.AbstractMap.SimpleEntry;

import ConsumerDemo.Seg3;

import java.util.*;
import java.io.*;
public class TweetProc {
	private String word = "";
	private static final int tweetpos = 11;
	private static Function<String,String> getContent = 
			(line)->{
				if (line == null || line.trim().equals(""))
						return ("Empty Tweet");
				String[] items = line.split(",");
				//System.out.println(items[tweetpos]);
				return(items[tweetpos]);
			};
			private static Function<String,String> Doubleem = 
					(line)->{
						String yes = "";
						line = line.trim();
						
						Integer firstspace = line.indexOf(' ');
						if (firstspace == -1) {
							return "";
						}
						yes = line.substring(0,firstspace);
						line = line.replaceAll(yes, "");
						line = line.replaceAll(" @"," " + yes + "@" );
						//System.out.println(items[tweetpos]);
						return line.trim();
					};
			
			private static Function<String,String> getflatmap = 
					(line)->{
						if (line == null || line.trim().equals(""))
								return ("Empty Tweet");
						String[] items = line.split("\\s+");
						//System.out.println(items[tweetpos]);
						String author = items[0];
						String newstring = line.replaceAll("^(?!.*(^ @[A-Za-z0-9-_]+)).*$", "");
						return(author + " " +newstring);
					};
			private static Function<String,String> geturlandauthor = 
					(line)->{
						if (line == null || line.trim().equals(""))
								return ("Empty Tweet");
						String[] items = line.split(",");
						//System.out.println(items[tweetpos]);
						return( items[8] + " " + items[tweetpos]);
					};
					private static Function<String,String> tester = 
							(line)->{
								if (line == null || line.trim().equals(""))
										return ("Empty Tweet");
								String[] items = line.split(" , ");
								String tags = items[1].replaceAll(" @[^a-zA-Z0-9\\\\s]", "").trim();
								//System.out.println(items[tweetpos]);
								return( items[0] + ":" + tags);
							};
					private static Function<String,String> taggeetweeter = 
							(line)->{
								if (line == null || line.trim().equals(""))
										return ("Empty Tweet");
								String[] items = line.split(",");
								//System.out.println(items[tweetpos]);
								return( items[8] + " , " + items[tweetpos]);
							};
							private static Function<String,String> recombine = 
									(line)->{
										if (line == null || line.trim().equals(""))
												return ("Empty Tweet");
										String[] items = line.split(",");
										//System.out.println(items[tweetpos]);
										return( items[8] + " " );
									};
					
			private static Function<String,String> getauthor = 
					(line)->{
						if (line == null || line.trim().equals(""))
								return ("Empty Tweet");
						String[] items = line.split(",");
						return(items[8]);
					};
					private static Function<String,String> getwordcount = 
							(line)->{
								if (line == null || line.trim().equals(""))
										return "";
								String[] items = line.trim().split("\\s+");
								return(items[0] + " " + (items.length - 1));
								
							};
							private static Function<String,String> nametaggee = 
									(line)->{
										if (line == null || line.trim().equals(""))
												return "";
										String[] items = line.trim().split("\\s+");
										return(items[0] + " " + (items[8]));
										
									};
			private static Predicate <String> testifcontains  = 
					(tweet)-> {if (tweet == null || !tweet.contains("Aggie"))
					 return false;
					else return true;};
					private static Predicate <String> empty  = 
							(tweet)-> {
								
								tweet = tweet.trim();
										if (tweet == null || tweet == "")
							 return false;
							else return true;};
					private static Predicate <String> containsurl  = 
							(tweet)-> {if (tweet == null || !tweet.contains(" http://"))
							 return false;
							else return true;};
							private static Predicate <String> containstag  = 
									(tweet)-> {if (tweet == null || !tweet.contains("@[A-Za-z0-9-_]+"))
									 return false;
									else return true;};
							
					
			private static String tagged(String tw)		{
				 String patternStr = "@[A-Za-z0-9-_]+";
			      Pattern pattern = Pattern.compile(patternStr);
			      Matcher m = pattern.matcher(tw);
			      if (m.find())
			      return m.group(0);
			      else return "@NOTAG";
			}
			private static String http(String tw)		{
				 String patternStr = "http\\\\://[A-Za-z0-9-_]+";
			      Pattern pattern = Pattern.compile(patternStr);
			      Matcher m = pattern.matcher(tw);
			      if (m.find())
			      return m.group(0);
			      else return "heh";
			}
			private static String author(String tw)		{
				 return tw;
			}
			private static String name(String tw)		{
				try {
				String[] items = tw.split(" ");
				
				
				 return items[0];}
				catch ( Exception e) {
					System.out.println(tw);
					return "@NOTAG";
				}
			}
			private static String names(String tw)		{
				try {
				String[] items = tw.split(" ");
				
				
				 return items[0].replaceAll("::", "");}
				catch ( Exception e) {
					System.out.println(tw);
					return "@NOTAG";
				}
			}
			private static String tag(String tw)		{
				try {
				String[] items = tw.split(" ");
				
				 return items[1];
				}
				catch ( Exception e) {
					System.out.println(tw);
					return "@NOTAG";
				}
				
			}
			private static Integer getcounter(String tw)		{
				String[] items = tw.split(" ");
				 return Integer.parseInt(items[1]);
			}
			

public static Map <String,Long> getPerTaggeeName(String FullPathName){
	try{
	      File inputF = new File(FullPathName);
	      InputStream inputFS = new FileInputStream(inputF);
	      BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
	      Map< String,Long> hm =  
                new HashMap< String,Long>();
			 long startTime = System.currentTimeMillis();

			 hm = br.lines().
	    		  		skip(1)
	    		  		.parallel()	    		  		
	    		  		.map(getContent)
	    		  		.flatMap(line -> Arrays.stream(line.replaceAll("@\\b(\\w+)\\b\\s*(?=.*\\b\\1\\b)", "").trim().split(" ")))
	    		  		.map(word -> word.replaceAll("^(?!.*(^@[A-Za-z0-9-_]+)).*$", "").trim())
	                    .collect(Collectors.groupingBy(TweetProc::tagged, Collectors.counting()));
	   		  	
	      br.close();

	      return hm;
	    } 
	catch (IOException e) {
		System.out.println("no  file");
	};
	return null;
	
	

}
public static Map <String,Long> getPerTweeterCount(String FullPathName){
	try{
	      File inputF = new File(FullPathName);
	      // This tweets file is not inlcuded in this demo
	      // It has  language that may bother  some people
	      // WE shall make it available later, or you can find it on the web,
	      // Google for "Airline tweets dataset". 
	      // It's probably the least offensive one you can find. 
	      
	      InputStream inputFS = new FileInputStream(inputF);
	      BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
	      Map< String,Long> hm =  
                new HashMap< String,Long>();
	        
			 long startTime = System.currentTimeMillis();
			 hm = br.lines().
	    		  		skip(1).  //headerline
	    		  		parallel()
	    		  		.map(getauthor)
		  		.collect(Collectors.groupingBy(TweetProc::author,Collectors.counting())); 

	    		  		//
	      br.close();
	      
			 long endTime = System.currentTimeMillis();
	      System.out.println("Total Time = "+ (endTime-startTime));
	      return hm;
	    } 
	catch (IOException e) {
		System.out.println("no  file");
	};
	return null;
	
	

}

public static Map <String,Long> getTweeterURLTweetCount(String FullPathName){
	try{
	      File inputF = new File(FullPathName);
	      
	      InputStream inputFS = new FileInputStream(inputF);
	      BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
	      Map< String,Long> hm =  
                new HashMap< String,Long>();
			 long startTime = System.currentTimeMillis();

			 Pattern pattern = Pattern.compile("...");
			 hm = br.lines().
	    		  		skip(1)
	    		  		
	    		  		.parallel()
	    		  		.map(geturlandauthor)
	    		  		.filter(containsurl)

	    		  		.map(ip -> ip.substring(0, ip.indexOf(" ")))
	                    .collect(Collectors.groupingBy(x -> x, Collectors.counting()));
	      br.close();

	      return hm;
	    } 
	catch (IOException e) {
		System.out.println("no  file");
	};
	return null;
	
	

}


public static Map <String,Long> getTweeterWordCount(String FullPathName,String word){
	
	try{
	      File inputF = new File(FullPathName);
	     
	      
	      InputStream inputFS = new FileInputStream(inputF);
	      BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
	      Map< String,Long> hm =  
                new HashMap< String,Long>();
	               
			 long startTime = System.currentTimeMillis();
	    
			 Pattern pattern = Pattern.compile("...");
			 hm = br.lines().
	    		  		skip(1)
	    		  		.parallel()
	    		  		.map(geturlandauthor)
	    		  		.filter(s -> s.contains(word))
	    		  		.map(ip -> ip.substring(0, ip.indexOf(" ")))
	                    .collect(Collectors.groupingBy(x -> x, Collectors.counting()));
	   		  		
	      br.close();

	      return hm;
	    } 
	catch (IOException e) {
		System.out.println("no  file");
	};
	return null;
	
	

}

public static Map <String, Map <String, Long>>  getTweeterTaggeeCount(String FullPathName){
	
	try{
	      File inputF = new File(FullPathName);
	     
	      
	      InputStream inputFS = new FileInputStream(inputF);
	      BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
	      Map<String, Map <String, Long>>   hm =  
                new HashMap <String, Map <String, Long>>();
	               
			 long startTime = System.currentTimeMillis();
	    
			 Pattern pattern = Pattern.compile("...");
			 hm = br.lines().
	    		  		skip(1)
	    		  		.map(taggeetweeter)
	    		  		.flatMap(x -> Arrays.stream(getflatmap(x)))
	    		  		.map(x -> x.replaceAll("::", " ").replaceAll(" +", " ").replaceAll(" @ ", "").trim())
	    		  		.filter(empty)
	    		  		.map(Doubleem)
	    		  		.flatMap(x -> Arrays.stream(x.split("\\s+")))
	    		  		.map(x -> x.replaceAll("@", " @"))
	    		  		.collect(Collectors.groupingBy(TweetProc::name,
								Collectors.groupingBy(TweetProc::tag, Collectors.counting())));
	    		  		
   		  	

	    		  		//
	      br.close();
	      return hm;
	    } 
	catch (IOException e) {
		System.out.println("no  file");
	};
	return null;
	
	

}
private static String[] getflatmap(String line) {
	if (line == null || line.trim().equals(""))
		return ("Empty Tweet").split("\\s+");
String[] items = line.split("\\s+");
//System.out.println(items[tweetpos]);
String author = items[0];
String newstring = line.replaceAll("\\b(?<!@)[^@]+", "::");
newstring = author + "::" + newstring.replaceAll("@\\b(\\w+)\\b\\s*(?=.*\\b\\1\\b)", "").replaceAll("::@ ", "");
newstring = newstring.trim();
return newstring.split("\\s+");
}
public static Map <String,Double> getTweeterAverageVerbosity(String FullPathName){
	
	try{
	      File inputF = new File(FullPathName);
	     
	      
	      InputStream inputFS = new FileInputStream(inputF);
	      BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
	      Map< String,Double> hm =  
                new HashMap<String,Double>();
	               
			 long startTime = System.currentTimeMillis();
	    
			 Pattern pattern = Pattern.compile("...");
			 hm = br.lines().
	    		  		skip(1)
	    		  		
	    		  		
	    		  		
	    		  		.map(geturlandauthor)
	    		  		//.flatMap(line -> Arrays.stream(line.replaceAll("^(?!.*(^http\\://[A-Za-z0-9-_]+)).*$", "").trim().split(" ")))
	    		  		.map(getwordcount)
	    		  		//.peek(System.out::println)
	    		  		//.peek(System.out::println)
	    		  		
	    		  		//.map(ip -> ip.substring(0, ip.indexOf(" ")))
	    		  		//.peek(System.out::println)
	    		  		
	    		  		
	                    //.collect(Collectors.toMap(s -> s, s -> 1, Integer::sum));
	                    .collect(Collectors.groupingBy(TweetProc::name, Collectors.averagingInt( 
                                TweetProc::getcounter)));
	   		  		//.reduce(0,(x,y)->x+y)
		  		//.collect(Collectors.groupingBy(TweetProc::tagged,Collectors.counting())); 

	    		  		//
	      br.close();
//	      for (Map.Entry<String,Long> entry : hm.entrySet()) {  
//	            System.out.println("Key = " + entry.getKey() + 
//	                             ", Value = " + entry.getValue()); 
//	    } 
//			 long endTime = System.currentTimeMillis();
//	
//	      System.out.println("Total Time = "+ (endTime-startTime));
	      return hm;
	    } 
	catch (IOException e) {
		System.out.println("no  file");
	};
	return null;
	
	

}
//public static void main(String[] args) {
//	Map< String,Long> hm =  
//            new HashMap< String,Long>();
//	Map< String,Double> dm =  
//            new HashMap< String,Double>();
//	Map< String,Integer> test =  
//            new HashMap< String,Integer>();
//	
//	Map < String,String> sm =
//			new HashMap < String,String> ();
//	Map <String, Map <String, Long>> bm =
//			new HashMap<String, Map <String, Long>> ();
//	
//////	part 1
////	hm =getPerTweeterCount("/Users/mdenitz/Downloads/cl-tweets-short-clean.csv");
////	for (Map.Entry<String,Long> entry : hm.entrySet()) {  
////        System.out.println("Key = " + entry.getKey() + 
////                         ", Value = " + entry.getValue()); 
////        
////} 
////	
////	// part 2
////	hm =getPerTaggeeName("/Users/mdenitz/Downloads/cl-tweets-short-clean.csv");
////	for (Map.Entry<String,Long> entry : hm.entrySet()) {  
////        System.out.println("Key = " + entry.getKey() + 
////                         ", Value = " + entry.getValue()); 
////        
////} 
//////	 System.out.println(hm.get("@Tinder"));
////	// part 2
////		hm =getTweeterURLTweetCount("/Users/mdenitz/Downloads/cl-tweets-short-clean.csv");
////		for (Map.Entry<String,Long> entry : hm.entrySet()) {  
////	        System.out.println("Key = " + entry.getKey() + 
////	                         ", Value = " + entry.getValue()); 
////	        
////	}
////		hm =getTweeterWordCount("/Users/mdenitz/Downloads/cl-tweets-short-clean.csv","@united");
////		for (Map.Entry<String,Long> entry : hm.entrySet()) {  
////	        System.out.println("Key = " + entry.getKey() + 
////	                         ", Value = " + entry.getValue()); 
////}
////		
////		dm =getTweeterAverageVerbosity("/Users/mdenitz/Downloads/cl-tweets-short-clean.csv");
////		for (Map.Entry<String,Double> entry : dm.entrySet()) {  
////	        System.out.println("Key = " + entry.getKey() + 
////	                         ", Value = " + entry.getValue()); 
////}
//	 
////	 bm =getTweeterTaggeeCount("/Users/mdenitz/Downloads/cl-tweets-short-clean.csv");
////		for (Map.Entry <String, Map <String, Long>> entry : bm.entrySet()) {  
////	        System.out.println("Key = " + entry.getKey() + "::");
////	        hm = entry.getValue();
////	        for (Map.Entry<String,Long> en : hm.entrySet()) {  
////	          System.out.println("Key = " + en.getKey() + 
////	                           ", Value = " + en.getValue()); 
////	          
////	  } 
////	                        
////	        
////	} 
//}
}