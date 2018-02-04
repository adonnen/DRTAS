package ds.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;

import ds.Job;
import ds.PeriodicTask;
import ds.PeriodicTaskSet;

class PeriodicTaskSetTest {

	@Test
	void utilizationTest() {
		PeriodicTaskSet pts = new PeriodicTaskSet();
		assertEquals(new BigDecimal("0.0"), pts.utilizaton().setScale(1), "Utilization is execution time by period");
		
		PeriodicTask pt1 = new PeriodicTask("test", 5, 0, 20, 0, 20, 1);		
		
		try {
			pts.addPTask(pt1);
		} catch (Exception e) {
			System.out.println(e);
		}
		assertEquals(new BigDecimal("0.25"), pts.utilizaton(), "Utilization is execution time by period");
		
		PeriodicTask pt2 = new PeriodicTask("test", 5, 0, 20, 0, 20, 2);
		try {
			pts.addPTask(pt2);
		} catch (Exception e) {
			System.out.println(e);
		}
		assertEquals(new BigDecimal("0.5"), pts.utilizaton().setScale(1), "Utilization is execution time by period");
	}
	
	@Test
	void allReleasesnTest() {
		PeriodicTaskSet pts = new PeriodicTaskSet();
				
		assertEquals(new ArrayList<Integer>(), pts.allReleases(0, 19), pts.allReleases(0, 19).toString());
		
		PeriodicTask pt1 = new PeriodicTask("test", 5, 0, 20, 0, 20, 1);		
		
		try {
			pts.addPTask(pt1);
		} catch (Exception e) {
			System.out.println(e);
		}
		ArrayList<Integer> testTimes = new ArrayList();
		testTimes.add(0);
		assertEquals(testTimes, pts.allReleases(0, 19), pts.allReleases(0, 19).toString());
		
		PeriodicTask pt2 = new PeriodicTask("test", 1, 0, 5, 0, 5, 2);
		try {
			pts.addPTask(pt2);
		} catch (Exception e) {
			System.out.println(e);
		}
		testTimes.add(5);
		testTimes.add(10);
		testTimes.add(15);
		testTimes.add(20);
		assertEquals(testTimes, pts.allReleases(0, 20), pts.allReleases(0, 20).toString());
	}
	
	@Test
	void generateJobsWithRMSPrioritiesTest() {
		PeriodicTaskSet pts = new PeriodicTaskSet();
				
		assertEquals(new ArrayList<Integer>(), pts.generateJobsWithRMSPriorities(0, 0), pts.generateJobsWithRMSPriorities(0, 0).toString());
		
		PeriodicTask pt1 = new PeriodicTask("test", 5, 0, 20, 0, 20, 1);		
		
		try {
			pts.addPTask(pt1);
		} catch (Exception e) {
			System.out.println(e);
		}

		assertEquals(1, pts.generateJobsWithRMSPriorities(0, 0).get(0).getPrio());
		
		PeriodicTask pt2 = new PeriodicTask("test", 1, 0, 5, 0, 5, 2);
		try {
			pts.addPTask(pt2);
		} catch (Exception e) {
			System.out.println(e);
		}
		assertEquals(2, pts.generateJobsWithRMSPriorities(0, 0).get(0).getPrio());
		assertEquals(1, pts.generateJobsWithRMSPriorities(0, 0).get(1).getPrio());
		
		for (Job j : pts.generateJobsWithRMSPriorities(0, 1000))
			if (j.getTaskId() == 1)
				assertEquals(1, j.getPrio());
			else if (j.getTaskId() == 2) 
				assertEquals(2, j.getPrio());
			else fail("some unknown job here");
	}

}
