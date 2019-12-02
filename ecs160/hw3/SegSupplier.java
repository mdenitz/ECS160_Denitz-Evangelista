package ConsumerDemo;
import java.util.*;

import java.util.stream.*;
import java.util.function.*;

public class SegSupplier {
	

			
	public static void main (String args[]) {


	UnaryOperator <Integer> countup =  (x)->x+1;
		
	Supplier <String> ts =  ()-> {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			};
			return String.valueOf(System.currentTimeMillis());};
	
	Supplier<Integer> ts2 = ()-> 1;
	
//	Stream.
//	generate(ts2).
// limit(20).
//	forEach(System.out::println);
	
	Stream.iterate(2,countup).limit(20).forEach(System.out::println);;
	 
//	Stream
//	.generate(new Random()::nextInt).parallel()
//	.limit(20)
//	.forEach(System.out::println);
	
	
	
	}
}

// Start with this. Look up the generate API, then the supplier API. 
// Explain that supplier is a functional API, and only needs one iterate. 
// Then look up the iterate API
// 0. Look up STreams API for iterate. 
//0.5 Also show what happens when we drop a parallel. 
// 1. Stream <String> nos = Stream.iterate ("hey, ", x ->x + " no!" );
// 2. nos.limit(10).forEach(System.out::println);