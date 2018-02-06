package sched.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

//import java.math.BigDecimal;
//import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;

import algo.sched.Essence;
import algo.sched.TDA;
import algo.sched.hard.RMS;
import ds.*;
import exceptions.InvalidTimeInterval;
import exceptions.NotSchedulableException;
import exceptions.ViolatedDeadlineException;

class RMSTest {
	
	@Test
	void LLValueTest() {
		System.out.println("====================================================================");
		System.out.println("Testing liu layland computation: ");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 3, 0, 6, 0, 6, 1));
		} catch (Exception e) {
		}
		//System.out.println(RMS.llBound(pts));
		assertEquals(Double.valueOf(RMS.llBound(pts.getpTaskSet().size()).toString()), 1.0, .001);
		
		try {
			pts.addPTask(new PeriodicTask("2", 5, 0, 10, 0, 10, 2));
		} catch (Exception e) {
		}
		//System.out.println(RMS.llBound(pts));
		assertEquals(Double.valueOf(RMS.llBound(pts.getpTaskSet().size()).toString()), 0.828, .001);
		
		try {
			pts.addPTask(new PeriodicTask("3", 1, 0, 10000, 0, 10000, 3));
			pts.addPTask(new PeriodicTask("4", 1, 0, 1000, 0, 1000, 4));
			pts.addPTask(new PeriodicTask("5", 1, 0, 100, 0, 100, 5));
		} catch (Exception e) {
		}
		//System.out.println(RMS.llBound(pts));
		assertEquals(Double.valueOf(RMS.llBound(pts.getpTaskSet().size()).toString()), 0.743, .001);
	}

	@Test
	void SchedulabilityNegativeLLTests() {
		System.out.println("====================================================================");
		System.out.println("Testing negative ll schedulability: ");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 3, 0, 6, 0, 6, 1));
		} catch (Exception e) {
			
		}
		assertTrue(RMS.isLiuLaylandSchedulable(pts));
		
		try {
			pts.addPTask(new PeriodicTask("2", 5, 0, 10, 0, 10, 2));
		} catch (Exception e) {
			
		}	
		assertFalse(RMS.isLiuLaylandSchedulable(pts));
		
		try {
			pts.addPTask(new PeriodicTask("3", 1, 0, 10000, 0, 10000, 3));
		} catch (Exception e) {
			
		}	
		assertFalse(RMS.isLiuLaylandSchedulable(pts));
	}
	
	@Test
	void SchedulabilityPositiveLLTests() {
		System.out.println("====================================================================");
		System.out.println("Testing positive ll schedulability: ");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 1, 0, 6, 0, 6, 1));
		} catch (Exception e) {			
		}
		assertTrue(RMS.isLiuLaylandSchedulable(pts));
		
		try {
			pts.addPTask(new PeriodicTask("2", 2, 0, 10, 0, 10, 2));
		} catch (Exception e) {
			
		}		
		assertTrue(RMS.isLiuLaylandSchedulable(pts));
	}
	
	@Test
	void HyperbolicSchedulabilityTests() {
		System.out.println("====================================================================");
		System.out.println("Testing hyperbolic schedulability: ");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 1, 0, 6, 0, 6, 1));
		} catch (Exception e) {
			
		}
		assertTrue(RMS.isHyperbolicallySchedulable(pts));
		
		try {
			pts.addPTask(new PeriodicTask("2", 4, 0, 10, 0, 10, 2));
		} catch (Exception e) {
			
		}
		assertTrue(RMS.isHyperbolicallySchedulable(pts));
	}
	
	@Test
	void harmonicityTests() {
		System.out.println("====================================================================");
		System.out.println("Testing harmonicity of a task set: ");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 3, 0, 6, 0, 6, 1));
		} catch (Exception e) {
			
		}
		assertTrue(RMS.isHarmonicTaskSet(pts));
		
		try {
			pts.addPTask(new PeriodicTask("2", 5, 0, 18, 0, 18, 2));
		} catch (Exception e) {
			
		}	
		assertTrue(RMS.isHarmonicTaskSet(pts));
		
		try {
			pts.addPTask(new PeriodicTask("3", 1, 0, 6, 0, 6, 3));
		} catch (Exception e) {
			
		}	
		assertTrue(RMS.isHarmonicTaskSet(pts));
		
		try {
			pts.addPTask(new PeriodicTask("4", 1, 0, 12, 0, 12, 4));
		} catch (Exception e) {
			
		}	
		assertFalse(RMS.isHarmonicTaskSet(pts));
	}
	
	@Test
	void rmsSchedule1Tests() {
		System.out.println("====================================================================");
		System.out.println("Testing rms 1: ");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 3, 0, 6, 0, 6, 1));
			pts.addPTask(new PeriodicTask("2", 4, 0, 10, 0, 10, 2));
		} catch (Exception e) {		
		}
		
		Schedule rmsSchedule = new Schedule();
		try {
			rmsSchedule = Essence.schedule(pts, 0, 60, true, jobList -> RMS.hasHighestPriority(jobList), a -> RMS.isSchedulable(a));
		} catch (Exception e) {
			System.out.println(e);
		}
		
		System.out.println(rmsSchedule);
		System.out.println(Essence.normalizeSchedule(rmsSchedule));
		assertTrue(pts.utilizaton().compareTo(BigDecimal.ONE) <= 0) ;
		
	}
	
	@Test
	void rmsSchedule2Tests() {
		System.out.println("====================================================================");
		System.out.println("Testing rms 2: ");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 1, 0, 4, 0, 6, 1));
			pts.addPTask(new PeriodicTask("2", 6, 0, 12, 0, 10, 2));
			pts.addPTask(new PeriodicTask("3", 10, 0, 48, 0, 10, 3));
		} catch (Exception e) {		
		}
		
		Schedule rmsSchedule = new Schedule();
		try {
			rmsSchedule = Essence.schedule(pts, 0, 60, true, jobList -> RMS.hasHighestPriority(jobList), a -> RMS.isSchedulable(a));
		} catch (Exception e) {
		}
		
		System.out.println(rmsSchedule);
		System.out.println(Essence.normalizeSchedule(rmsSchedule));
		
	}
	
	@Test
	void rmsSchedule3Tests() {
		System.out.println("====================================================================");
		System.out.println("Testing rms 3: ");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 18, 0, 48, 0, 48, 1));
			pts.addPTask(new PeriodicTask("2", 2, 0, 6, 0, 6, 2));
			pts.addPTask(new PeriodicTask("3", 1, 0, 4, 0, 4, 3));
		} catch (Exception e) {		
		}
		
		Schedule rmsSchedule = new Schedule();
		try {
			rmsSchedule = Essence.schedule(pts, 0, 50, true, jobList -> RMS.hasHighestPriority(jobList), a -> RMS.isSchedulable(a));
		} catch (Exception e) {
		}
		
		System.out.println(rmsSchedule);
		System.out.println(Essence.normalizeSchedule(rmsSchedule));
		
	}
	
	@Test
	void rmsScheduleAsFarAsTests() {
		System.out.println("====================================================================");
		System.out.println("Testing rms as far as possible: ");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 3, 0, 6, 0, 6, 1));
			pts.addPTask(new PeriodicTask("2", 5, 0, 10, 0, 10, 2));
			// pts.addPTask(new PeriodicTask("3", BigDecimal.ONE, 0, new BigDecimal("4.0"), 0, new BigDecimal("4.0"), 3));
		} catch (Exception e) {		
		}
		
//		System.out.println(pts.utilizaton());
//		System.out.println(RMS.isLiuLaylandSchedulable(pts));
//		System.out.println(RMS.isHyperbolicallySchedulable(pts));
//		System.out.println(TDA.isTDASchedulable(pts, true));
		
		Schedule rmsSchedule = new Schedule();
		try {
			rmsSchedule = Essence.schedule(pts, 0, 50, false, jobList -> RMS.hasHighestPriority(jobList), a -> RMS.isSchedulable(a));
			System.out.println(rmsSchedule);
		} catch (ViolatedDeadlineException e) {
			System.out.println(e);
			assertEquals(e.getMessage(), "A deadline is violated at time point 10");
			System.out.println("Schedule so far:");
			System.out.println(Essence.schedule);
		} catch (NotSchedulableException e) {
			System.out.println(e);
			System.out.println("Failed schedulability tests. If you want to schedule as far as possible, set the onlyIfSchedulable-variable to false.");
		} catch (InvalidTimeInterval e) {
			System.out.println(e);
		}
		
	}
	
	@Test
	void greyZoneGenerationTests() {
		System.out.println("====================================================================");
		System.out.println("Testing rms grey zone task set generation: ");
		long time = System.nanoTime();
		PeriodicTaskSet pts = RMS.generateSchedulableGreyZoneTaskSet(4, 5, 15);
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
		assertTrue(pts.utilizaton().compareTo(RMS.llBound(pts.getpTaskSet().size())) > 0) ;
		assertTrue(pts.utilizaton().compareTo(BigDecimal.ONE) <= 0) ;
		
	}

}
