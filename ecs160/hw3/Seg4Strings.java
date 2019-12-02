package ConsumerDemo;

import java.util.stream.LongStream;
import java.util.stream.Collectors;
import java.util.stream.*;

public class Seg4Strings {

	public static void main(String[] args) {
		long startTime,endTime;
		 startTime = System.currentTimeMillis();
	     
			

String val= LongStream.
				range(1,1000).
            parallel().
			filter((x) -> x %  6 ==0).
			mapToObj(String::valueOf).
			collect(Collectors.joining(",")).
            toString();
			
		
		 
		
		
		endTime = System.currentTimeMillis();
		 System.out.println("(Vanilla Stream) +String of numbers div by 6, calculated in  "+(endTime-startTime )+ " milliseconds is"+val);

	}

}
