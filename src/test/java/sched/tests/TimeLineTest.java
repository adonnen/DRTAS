package sched.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

import algo.sched.Essence;
import algo.sched.offline.MaxFlow;
import algo.sched.offline.TimeLine;
import ds.Flow;
import ds.FlowNetwork;
import ds.PeriodicTask;
import ds.PeriodicTaskSet;

class TimeLineTest {

	@Test
	void frameSizesWithoutSlicingTest() {
		System.out.println("====================================================================");
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
		
		ArrayList<Integer> test = new ArrayList<Integer>(Arrays.asList(Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6)));
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
		
//		ArrayList<Integer> test = new ArrayList<Integer>(Arrays.asList(Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6)));
		System.out.println("Frame sizes are: " + TimeLine.computeFeasibleFrameSizes(pts));
//		assertTrue(TimeLine.computeFeasibleFrameSizes(pts).equals(test) );

	}
	
	@Test
	void BeforeAndAfterSlicingTest2() {
		System.out.println("====================================================================");
		System.out.println("Before and After Slicing Second Test:");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		PeriodicTask p1 = new PeriodicTask("1", 30, 0, 40, 0, 40, 1, 1);
		PeriodicTask p2 = new PeriodicTask("2", 15, 0, 60, 0, 60, 2, 2);

		
		try {		
			pts.addPTask(p1);
			pts.addPTask(p2);
			
		} catch (Exception e) {		
		}
		
//		ArrayList<Integer> test = new ArrayList<Integer>(Arrays.asList(Integer.valueOf(3), Integer.valueOf(4), Integer.valueOf(5), Integer.valueOf(6)));
		System.out.println("Frame sizes before slicing are: " + TimeLine.computeFeasibleFrameSizes(pts));
//		assertTrue(TimeLine.computeFeasibleFrameSizes(pts).equals(test) );
		pts = TimeLine.jobSlicing(pts);
		System.out.println("Frame sizes after slicing are: " + TimeLine.computeFeasibleFrameSizes(pts));
		pts = TimeLine.jobSlicing(pts);
		System.out.println("Frame sizes after slicing are: " + TimeLine.computeFeasibleFrameSizes(pts));
	}
	
	@Test
	void constructingNetworkFlowTest1() {
		System.out.println("====================================================================");
		System.out.println("Constructing Network Flow 1st Test:");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		PeriodicTask p1 = new PeriodicTask("1", 30, 0, 40, 0, 40, 1, 1);
		PeriodicTask p2 = new PeriodicTask("2", 15, 0, 60, 0, 60, 2, 2);
		try {		
			pts.addPTask(p1);
			pts.addPTask(p2);
			
		} catch (Exception e) {		
		}
		
		System.out.println("Frame sizes are: " + TimeLine.computeFeasibleFrameSizes(pts));
		TimeLine.constructFlowNetwork(pts.generateJobs(0, pts.hyperPeriod()-1), pts.hyperPeriod(), TimeLine.computeFeasibleFrameSizes(pts).get(0));
		pts = TimeLine.jobSlicing(pts);
		System.out.println("Frame sizes are: " + TimeLine.computeFeasibleFrameSizes(pts));
		TimeLine.constructFlowNetwork(pts.generateJobs(0, pts.hyperPeriod()-1), pts.hyperPeriod(), TimeLine.computeFeasibleFrameSizes(pts).get(0));
		pts = TimeLine.jobSlicing(pts);
		System.out.println("Frame sizes are: " + TimeLine.computeFeasibleFrameSizes(pts));
		TimeLine.constructFlowNetwork(pts.generateJobs(0, pts.hyperPeriod()-1), pts.hyperPeriod(), TimeLine.computeFeasibleFrameSizes(pts).get(1));
	}
	
	@Test
	void isFullyMappedTest() {
		System.out.println("====================================================================");
		System.out.println("Ford Fulkerson Test:");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		PeriodicTask p1 = new PeriodicTask("1", 30, 0, 40, 0, 40, 1, 1);
		PeriodicTask p2 = new PeriodicTask("2", 15, 0, 60, 0, 60, 2, 2);
		try {		
			pts.addPTask(p1);
			pts.addPTask(p2);
			
		} catch (Exception e) {		
		}
		
		Flow f;
	
		pts = TimeLine.jobSlicing(pts);
		pts = TimeLine.jobSlicing(pts); 
		System.out.println("Frame sizes are: " + TimeLine.computeFeasibleFrameSizes(pts));
		FlowNetwork fn = TimeLine.constructFlowNetwork(pts.generateJobs(0, pts.hyperPeriod()-1), pts.hyperPeriod(), TimeLine.computeFeasibleFrameSizes(pts).get(0));
		for (int i = 0; i < fn.numFrames + fn.numJobs + 2; ++i) {
			for (int j = 0; j < fn.numFrames + fn.numJobs + 2; ++j)
				System.out.print(fn.residualGraph[i][j] + "\t");
			System.out.println();	
		}
		
		try {
			f = MaxFlow.fordFulkerson(fn);
			System.out.println("Flow : ");
			for (int i = 0; i < fn.numFrames + fn.numJobs + 2; ++i) {
				for (int j = 0; j < fn.numFrames + fn.numJobs + 2; ++j)
					System.out.print(f.residualGraph[i][j] + "\t");
				System.out.println();	
			}
			
			assertTrue(TimeLine.isFullyMapped(Essence.generateSortedJobList(pts, 0, pts.hyperPeriod()-1, (o1, o2) -> Integer.compare(o1.getInstanceId(), o2.getInstanceId())), f, fn.numFrames));
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
	}
	
	@Test
	void timeLineSchedulingTest1() {
		System.out.println("====================================================================");
		System.out.println("Timeline Scheduling 1st Test:");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		PeriodicTask p1 = new PeriodicTask("1", 30, 0, 40, 0, 40, 1, 1);
		PeriodicTask p2 = new PeriodicTask("2", 15, 0, 60, 0, 60, 2, 2);
		try {		
			pts.addPTask(p1);
			pts.addPTask(p2);
			
		} catch (Exception e) {		
		}
		
		try {
			System.out.println(TimeLine.schedule(pts));
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
	}
	

}
