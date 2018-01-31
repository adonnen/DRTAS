package demos.sched.rms;

import algo.sched.TDA;
import algo.sched.hard.RMS;
import ds.*;

public class generateGreyZone {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		long time = System.nanoTime();
		PeriodicTaskSet pts = RMS.generateGreyZoneTaskSet(4, 5, 15);
		time = System.nanoTime() - time;
		
		System.out.println("Elapsed time is " + (double) time / 1000000000.0);
		
		System.out.println(pts);
//		System.out.println("Num tasks is " + pts.getpTaskSet().size());
//		System.out.println("Liu Layland Bound is : " + RMS.llBound(pts.getpTaskSet().size()));
		System.out.println("Total utilization is " + pts.utilizaton());
		
		time = System.nanoTime();
		TDA.isTDASchedulable(pts, false);
		time = System.nanoTime() - time;
		
		System.out.println("Elapsed time is " + (double) time / 1000000000.0);

	}

}
