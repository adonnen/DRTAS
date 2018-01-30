package sched.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import algo.sched.TDA;
import ds.PeriodicTask;
import ds.PeriodicTaskSet;

class TDATest {

	@Test
	void timeDemandTestCases() {
		PeriodicTaskSet pts = new PeriodicTaskSet();
		PeriodicTask p1 = new PeriodicTask("1", 15, 0, 48, 0, 48, 1, 1);
		PeriodicTask p2 = new PeriodicTask("2", 2, 0, 6, 0, 6, 2, 2);
		PeriodicTask p3 = new PeriodicTask("3", 1, 0, 4, 0, 4, 3, 3);
		
		try {		
			pts.addPTask(p1);
			pts.addPTask(p2);
			pts.addPTask(p3);
			
		} catch (Exception e) {		
		}
		
		try {
			assertEquals(TDA.timeDemandFunction(pts, p1, 0), 18 );
			assertEquals(TDA.timeDemandFunction(pts, p1, 4), 18 );
		} catch (Exception e) {		
		}
	}
	
	@Test
	void TDApositiveSchedulabilityTestCases() {
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 16, 0, 48, 0, 48, 1));
			pts.addPTask(new PeriodicTask("2", 2, 0, 6, 0, 6, 2));
			pts.addPTask(new PeriodicTask("3", 1, 0, 3, 0, 3, 3));
		} catch (Exception e) {		
		}
		
		try {
			assertTrue(TDA.isTDASchedulable(pts, true));
		} catch (Exception e) {		
		}
	}
	
	@Test
	void TDAnegativeSchedulabilityTestCases() {
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 17, 0, 48, 0, 48, 1));
			pts.addPTask(new PeriodicTask("2", 2, 0, 6, 0, 6, 2));
			pts.addPTask(new PeriodicTask("3", 1, 0, 3, 0, 3, 3));
		} catch (Exception e) {		
		}
		
		try {
			assertFalse(TDA.isTDASchedulable(pts, true));
		} catch (Exception e) {		
		}
	}

}
