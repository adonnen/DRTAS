package sched.tests;

import static org.junit.jupiter.api.Assertions.*;
//import java.math.BigDecimal;
//import static org.junit.Assert.*;
import org.junit.jupiter.api.Test;
import ds.*;
import algo.sched.RMS;

class RMSTest {
	
	@Test
	void LLValueTest() {
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

}
