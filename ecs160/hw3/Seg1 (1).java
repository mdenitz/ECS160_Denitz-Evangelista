package ConsumerDemo;

import java.util.*;

import java.util.stream.*;
import java.util.function.*;

public class Seg1 {
	static long val= 0;
	public static void main(String[] arg) {
	
		 long startTime = System.currentTimeMillis();
		 
		 Integer i;

		 
		 for(i = 0; i< 1000000000; i++) {
			 if (i % 2 == 0)
			   val = val + i;
		 }
		 
		 long endTime = System.currentTimeMillis();
		 
		 System.out.println("(Vanilla Loop) +Sum of even numbers from 1 to 1000,000,000 is "+val +" in "+(endTime-startTime )+ " milliseconds");
	

		 
		 startTime = System.currentTimeMillis();
    
		 i = 2;
	val = 
				LongStream.
				range(1,1000000000).
//				parallel().
				peek(x -> {if (x % 10000000==0) System.out.println("Thread ID is "+Thread.currentThread().getId());}).
				filter((x) -> x %  2 ==0).
			   reduce(0,(x,y)->x+y);
		
		
		endTime = System.currentTimeMillis();
		 System.out.println("(Vanilla Stream) +Sum of even numbers from 1 to 1000,000,000 is "+val +" in "+(endTime-startTime )+ " milliseconds");

	}

}

//	peek(x -> {if (x % 10000000==0) 
//System.out.println("Thread ID is "+Thread.currentThread().getId());}).



// 1 Show plain
// 2 Show parallel version of streams
// 3 Show threads going. 
//peek(x -> {if (x % 10000000==0) 
//	System.out.println("Thread ID is "+Thread.currentThread().getId());}).
// For local variable capture, use a i instead of 2. Then make the variable
// a final, and each case show performance. 