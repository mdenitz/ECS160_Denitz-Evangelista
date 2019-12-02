package brown.corpus.examples;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * See also: 
 * http://www.baeldung.com/java-8-collectors
 * http://www.baeldung.com/java-groupingby-collector
 *
 * https://www.javabrahman.com/java-8/java-8-how-to-use-collectors-collectingandthen-method-with-examples/
 */
public class Main {
	
	public static void main(String[] args)
	{
		List<String> lines = null;
		long startTime;
		long endTime;
		
		try {
			lines = Files.lines(Paths.get("example.txt"))
					.collect(Collectors.toList());
			//lines = Files.lines(Paths.get("BrownCorpusSample.txt"))
			//					.collect(Collectors.toList());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("------------------------------------\n");
		
		System.out.println("Lines in the file:");
		//Count the number of lines in the file
		System.out.println(lines.stream().collect(Collectors.counting()).toString());
		
		System.out.println("------------------------------------\n");
		
		//Join the first ten lines together in a string
		System.out.println("First 10 lines:");
		System.out.print(lines.stream()
			.limit(10)
			.collect(Collectors.joining("\n ---- \n")));
		
		System.out.println("\n------------------------------------\n");
		
		//Get the length of the top 5 distinct longest sentences in characters.
		System.out.println("Length of longest 5 distinct sentences:");
		startTime = System.currentTimeMillis();
		lines.stream()
		    .distinct()
			.map(x -> x.length())
			.sorted(Comparator.reverseOrder()) 
			.limit(5)
			.forEach(System.out::println);
		endTime = System.currentTimeMillis();
		System.out.println("Non-parallel:" + (endTime-startTime)+ " milliseconds");
		
		//Note: You can pass a Comparator to sorted to specify how the sorting will happen:
		//https://docs.oracle.com/javase/8/docs/api/java/util/Comparator.html
		//Comparators are a type of functional interface that must implement a function
		//int compare(T o1, T o2) - Returns a negative integer, 0, or positive integer depending on 
		//if the first object is less than, equal to, or greater than the second.

		
		//Sorting and distinct can make it harder to get gains from parallelism
		startTime = System.currentTimeMillis();
		lines.stream()
		.parallel()
	    .distinct()
		.map(x -> x.length())
		.sorted(Comparator.reverseOrder())
		.limit(5)
		.forEachOrdered(System.out::println);
		endTime = System.currentTimeMillis();
		System.out.println("Parallel:" + (endTime-startTime)+ " milliseconds");

		
		System.out.println("------------------------------------\n");
		//Get the actual lines for the top 5 longest sentences
		System.out.println("Actual top 5 longest sentences:");
		Map<String, Integer> labelledLines = lines.stream()
			.distinct()
			.collect(Collectors.toMap(Function.identity(), String::length, (a,b) ->a)); //Function for keys, function for values, function to resolve equivalent keys
		

		
		labelledLines.entrySet().stream()
			.sorted(Map.Entry.<String,Integer>comparingByValue().reversed())
			.map(x -> x.getKey())
			.limit(5)
			.forEach(System.out::println);
		
		
		System.out.println("------------------------------------\n");
		System.out.println("Map and FlatMap");
		//Map vs FlatMap
		//http://www.baeldung.com/java-difference-map-and-flatmap
		//http://www.baeldung.com/java-flatten-nested-collections
		//https://www.mkyong.com/java8/java-8-flatmap-example/
		String[][] data = new String[][]{{"a", "b"}, {"c", "d"}, {"e", "f"}};
		
		Arrays.stream(data)
		.map(tok -> Arrays.stream(tok))
		.forEach(System.out::println);
		
		Arrays.stream(data)
			.flatMap(tok -> Arrays.stream(tok)) //equivalent
			//.flatMap(Arrays::stream) //equivalent
			.forEach(System.out::println);
		
		System.out.println();
		
		List<List<String>> list = Arrays.asList(
				  Arrays.asList("a"),
				  Arrays.asList("b"));
		System.out.println(list);
				
	     System.out.println(list
						  .stream()
						  //.flatMap(x -> x.stream()) //equivalent
						  .flatMap(Collection::stream) //equivalent
						  .collect(Collectors.toList()));
	     
	    
	     System.out.println("------------------------------------\n");
	     
	     //Grouping By -> provides similar functionality to the SQL Group by
	     //Has one required argument and two optional ones:
	     //Required: Classifier - Map stream output to your map keys with some function - What are you 'grouping' by?
	     //Optional: Supplier - "MapFactory" which allows you to specify what type of map to store the data in.
	     //Optional: Another Collector -> specifies the form of that the values can take.  This could be another groupingBy! (see last example)
		
		//Get a map of the file's vocabulary
		System.out.println("Vocabulary of the file in descending order");
		Map<Stream<String>, Long> vocabNotFlat = lines.stream()
				.map(x -> Arrays.stream(x.split("\\s+")))
				.collect(Collectors.groupingBy(Function.identity(), Collectors.counting())); 
		
		//Enable only on shorter example
		//System.out.println("Without flattening the map: ");
		//System.out.println(vocabNotFlat.keySet());
			
		//FlatMap
		startTime = System.currentTimeMillis();
		Map<String, Long> vocab = lines.stream()
			.flatMap(x -> Arrays.stream(x.split("\\s+")))
			.collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));
		endTime = System.currentTimeMillis();
		System.out.println("Non-parallel:" + (endTime-startTime)+ " milliseconds");
		
		//Why might this one slower on the shorter text?
		startTime = System.currentTimeMillis(); 
		Map<String, Long> vocab2 = lines.stream()
			.parallel()
			.flatMap(x -> Arrays.stream(x.split("\\s+")))
			.collect(Collectors.groupingByConcurrent(Function.identity(), 
					Collectors.counting()));
		endTime = System.currentTimeMillis();
		System.out.println("Parallel: " + (endTime-startTime)+ " milliseconds");
		System.out.println("Result equivalent? " + vocab.equals(vocab2));
		
		//Show the top 50 vocab words
		vocab.entrySet().stream()
		.sorted(Map.Entry.<String,Long>comparingByValue().reversed())
		.filter(x -> !x.getKey().equals(""))
		.limit(50)
		.forEach(x -> {
			System.out.println(x.getKey() + ":" + x.getValue());
		});		
		
		System.out.println("------------------------------------\n");
		
		System.out.println("Counts of tokens partitioned by the first character, then the second: ");
		BiFunction<String, Integer, String> nChar = (s, n) -> 
			{
				if(s.length() > n)
				{
					return String.valueOf(s.charAt(n));
				}
				else
				{
					return "";
				}
			};
		
		Function<String, String> firstChar = s -> nChar.apply(s,0);
		Function<String, String> secondChar = s -> nChar.apply(s, 1);
		
		//Curried Version
		Function<String, Function<Integer, String>> firstCurrier = s -> n -> nChar.apply(s,n);
		
		
		//Double collect example - Show the top 5 starting characters alphabetic characters
		//and show the frequencies of every possible 2nd character in the word.
		Map<String, Map<String, Long>> words = lines.stream()
									//.parallel()
									.map(x -> x.toLowerCase())
									.flatMap(x -> Arrays.stream(x.split("\\s+")))
									.collect(Collectors.groupingBy(firstChar, 
											Collectors.groupingBy(secondChar, Collectors.counting())));
		
		
		words.entrySet().stream()
		.filter(x -> Pattern.matches("[a-z]", x.getKey()))
		.limit(5)
		.forEach(x -> {
			System.out.println("----" + x.getKey() + "----");
			x.getValue().entrySet().stream()
				.filter(y -> !y.getKey().equals(""))
				.forEach(y -> {System.out.println(y.getKey() + ":" + y.getValue());});
		});
	}
}
