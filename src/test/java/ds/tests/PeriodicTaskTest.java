package ds.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import ds.PeriodicTask;
import java.math.BigDecimal;

class PeriodicTaskTest {

	@Test
	void utilizationTest() {
		PeriodicTask pt = new PeriodicTask("test", 5, 0, 20, 0, 20, 1);
		assertEquals(new BigDecimal("0.25"), pt.utilization(), "Utilization is execution time by period");
		pt.setWcet(10);
		assertEquals(new BigDecimal("0.5"), pt.utilization(), "Utilization is execution time by period");
	}
	
	@Test
	void zeroPeriodUtilizationTest() {		
		try {
			PeriodicTask pt = new PeriodicTask("test", 5, 0, 20, 0, 20, 1);
			pt.setPeriod(0);
			BigDecimal u = pt.utilization();
	        fail("Expected an ArithmeticException to be thrown");
	    } catch (ArithmeticException e) {
	        assertEquals(e.getMessage(), "denominator == 0");
	    }
	}
	

}
