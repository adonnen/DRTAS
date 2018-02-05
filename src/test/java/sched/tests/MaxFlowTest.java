package sched.tests;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import algo.sched.offline.MaxFlow;
import algo.sched.offline.TimeLine;
import ds.*;

class MaxFlowTest {

	@Test
	void test() {
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
		
		
		System.out.println("Frame sizes are: " + TimeLine.computeFeasibleFrameSizes(pts));
		FlowNetwork fn = TimeLine.constructFlowNetwork(pts.generateJobs(0, pts.hyperPeriod()-1), pts.hyperPeriod(), TimeLine.computeFeasibleFrameSizes(pts).get(0));
		Flow f;
		try {
			f = MaxFlow.fordFulkerson(fn);
			System.out.println("Flow : ");
			for (int i = 0; i < fn.numFrames + fn.numJobs + 2; ++i) {
				for (int j = 0; j < fn.numFrames + fn.numJobs + 2; ++j)
					System.out.print(f.residualGraph[i][j] + "\t");
				System.out.println();	
			}
		} catch (Exception e) {
			System.out.println(e);
		}
		
		
		pts = TimeLine.jobSlicing(pts);
		pts = TimeLine.jobSlicing(pts); 
		System.out.println("Frame sizes are: " + TimeLine.computeFeasibleFrameSizes(pts));
		fn = TimeLine.constructFlowNetwork(pts.generateJobs(0, pts.hyperPeriod()-1), pts.hyperPeriod(), TimeLine.computeFeasibleFrameSizes(pts).get(0));
		try {
			f = MaxFlow.fordFulkerson(fn);
			System.out.println("Flow : ");
			for (int i = 0; i < fn.numFrames + fn.numJobs + 2; ++i) {
				for (int j = 0; j < fn.numFrames + fn.numJobs + 2; ++j)
					System.out.print(f.residualGraph[i][j] + "\t");
				System.out.println();	
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
