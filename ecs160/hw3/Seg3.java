package ConsumerDemo;
import java.util.function.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.*;
import java.io.*;

public class Seg3 {

	private static final int tweetpos = 11;
	private static Function<String,String> getContent = 
			(line)->{
				if (line == null || line.trim().equals(""))
						return ("Empty Tweet");
				String[] items = line.split(",");
				return(items[tweetpos]);
			};
			private static Predicate <String> testifcontains  = 
					(tweet)-> {if (tweet == null || !tweet.contains("Aggie"))
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
			
	public static void main(String[] args) {
		try{
		      File inputF = new File("/Users/devanbu/Downloads/cl-tweets-short.csv");
		      // This tweets file is not inlcuded in this demo
		      // It has  language that may bother  some people
		      // WE shall make it available later, or you can find it on the web,
		      // Google for "Airline tweets dataset". 
		      // It's probably the least offensive one you can find. 
		      
		      InputStream inputFS = new FileInputStream(inputF);
		      BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
 
		               Stream<String> xx = br.lines().limit(20).filter((x)->x.contains("crapx`"));
             
	//	   Stream<String>xx= br
		//    		  	.lines()
	//	    		  	.filter(testifcontains);
		      
		      
              xx.forEach(System.out::println);
				 long startTime = System.currentTimeMillis();
		      // skip the header of the csv
	//	  Map<String, Set<String>>  res = 
		//	 long sum1 =
		    		  br.lines().
		//    		  		skip(1).   //headerline
	 // 		  		parallel().
		    	//	  		map(getContent).
//		   		  		reduce(0,(x,y)->x+y);
	//  		  		collect(Collectors.groupingBy(Seg3::tagged,Collectors.toSet())); 

		    		  		//
		      br.close();
				 long endTime = System.currentTimeMillis();
		//     res.get("@jetblue").forEach(System.out::println);
		      System.out.println("Total Time = "+ (endTime-startTime));
		    } 
		catch (IOException e) {
			System.out.println("no  file");
		};

	}
}

//1 First search in messages using contains, and then print with foreach
//2 use tagged to find tags. (use Seg3::tagged) and then print with foreach. 
// 3 show collection   Map<String, Set<String>>  res = br.lines().
// 4 collect(Collectors.groupingBy(Seg3::tagged,Collectors.toSet())); and show using 		      res.get("@united").forEach(System.out::println);
// 5 collect(Collectors.summarizingInt(s  -> s.length()))  IntSummaryStatistics tweetSummary = and then System.out.println

