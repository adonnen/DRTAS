package ds.tests;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import ds.*;

class ScheduleTest {

	@Test
	void test() {
		Schedule s = new Schedule();
		
		assertTrue(s.isValid());
		
		s.add(new Job(0, 10, 0, 0, 0), 0, 1, null);
		s.add(new Job(0, 10, 0, 0, 0), 1, 2, null);
		s.add(new Job(0, 10, 0, 0, 0), 3, 10, null);
		s.add(new Job(0, 10, 0, 0, 0), 10, 10, null);
		
		assertTrue(s.isValid());
		
		s.add(new Job(0, 10, 0, 0, 0), 0, 1000, null);
		
		assertFalse(s.isValid());
	}

}
