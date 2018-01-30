package sched.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import algo.sched.TimeLine;
import ds.PeriodicTask;
import ds.PeriodicTaskSet;

class TimeLineTest {

	@Test
	void frameSizesWithoutSlicingTest() {
		System.out.println("Frame Size Computation Test:");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		PeriodicTask p1 = new PeriodicTask("1", 1, 0, 15, 0, 14, 1, 1);
		PeriodicTask p2 = new PeriodicTask("2", 2, 0, 20, 0, 26, 2, 2);
		PeriodicTask p3 = new PeriodicTask("3", 3, 0, 22, 0, 22, 3, 3);
		
		try {		
			pts.addPTask(p1);
			pts.addPTask(p2);
			pts.addPTask(p3);
			
		} catch (Exception e) {		
		}
		
		ArrayList<Long> test = new ArrayList<Long>(Arrays.asList(Long.valueOf(3), Long.valueOf(4), Long.valueOf(5), Long.valueOf(6)));
		System.out.println("Frame sizes are: " + TimeLine.computeFeasibleFrameSizes(pts));
		assertTrue(TimeLine.computeFeasibleFrameSizes(pts).equals(test) );
	}
	
	@Test
	void SlicingTest() {
		System.out.println("====================================================================");
		System.out.println("Slicing Test:");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		PeriodicTask p1 = new PeriodicTask("1", 1, 0, 4, 0, 4, 1, 1);
		PeriodicTask p2 = new PeriodicTask("2", 2, 0, 5, 0, 5, 2, 2);
		PeriodicTask p3 = new PeriodicTask("3", 5, 0, 20, 0, 20, 3, 3);
		
		try {		
			pts.addPTask(p1);
			pts.addPTask(p2);
			pts.addPTask(p3);
			
		} catch (Exception e) {		
		}
		
		System.out.println(TimeLine.jobSlicing(pts));

	}
	
	@Test
	void BeforeAndAfterSlicingTest() {
		System.out.println("====================================================================");
		System.out.println("Before and After Slicing Test:");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		PeriodicTask p1 = new PeriodicTask("1", 1, 0, 4, 0, 4, 1, 1);
		PeriodicTask p2 = new PeriodicTask("2", 2, 0, 5, 0, 5, 2, 2);
		PeriodicTask p3 = new PeriodicTask("3", 5, 0, 20, 0, 20, 3, 3);
		
		try {		
			pts.addPTask(p1);
			pts.addPTask(p2);
			pts.addPTask(p3);
			
		} catch (Exception e) {		
		}
		
//		ArrayList<Long> test = new ArrayList<Long>(Arrays.asList(Long.valueOf(3), Long.valueOf(4), Long.valueOf(5), Long.valueOf(6)));
//		System.out.println("Frame sizes are: " + TimeLine.computeFeasibleFrameSizes(pts));
//		assertTrue(TimeLine.computeFeasibleFrameSizes(pts).equals(test) );

	}
	
	@Test
	void BeforeAndAfterSlicingTest2() {
		System.out.println("====================================================================");
		System.out.println("Before and After Slicing Test:");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		PeriodicTask p1 = new PeriodicTask("1", 30, 0, 40, 0, 40, 1, 1);
		PeriodicTask p2 = new PeriodicTask("2", 15, 0, 60, 0, 60, 2, 2);

		
		try {		
			pts.addPTask(p1);
			pts.addPTask(p2);
			
		} catch (Exception e) {		
		}
		
//		ArrayList<Long> test = new ArrayList<Long>(Arrays.asList(Long.valueOf(3), Long.valueOf(4), Long.valueOf(5), Long.valueOf(6)));
//		System.out.println("Frame sizes are: " + TimeLine.computeFeasibleFrameSizes(pts));
//		assertTrue(TimeLine.computeFeasibleFrameSizes(pts).equals(test) );

	}
	

}
