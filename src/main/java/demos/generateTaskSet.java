package demos;

import java.math.BigDecimal;
import algo.sched.Essence;
import ds.PeriodicTaskSet;

public class generateTaskSet {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		long time = System.nanoTime();
		PeriodicTaskSet p = Essence.generateRandomTaskSet(5, new BigDecimal("50.000"), new BigDecimal("100.000"), 10, 20, 0);
		time = System.nanoTime() - time;
		
		System.out.println("Elapsed time is " + (double) time / 1000000000.0 + "s");
		
		
		System.out.println(p);
		System.out.println(p.utilizaton());

	}

}
