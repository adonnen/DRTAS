package demos.sched;


import algo.sched.Essence;
import ds.*;

public class generateFullUtilDemo {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		long time = System.nanoTime();
		PeriodicTaskSet result = Essence.generateTaskSetWithUtilONE(3, 3, 15);
		time = System.nanoTime() - time;
		
		System.out.println("Elapsed time is " + (double) time / 1000000000.0 + "s");
		
		System.out.println(result);
		System.out.println("Resulting utilization: " + result.utilizaton());

	}

}
