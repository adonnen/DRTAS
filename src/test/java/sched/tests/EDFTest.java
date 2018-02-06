package sched.tests;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

import algo.sched.Essence;
import algo.sched.hard.EDF;
import algo.sched.hard.RMS;
import ds.PeriodicTask;
import ds.PeriodicTaskSet;
import ds.Schedule;

class EDFTest {

	@Test
	void edfSchedule1Tests() {
		System.out.println("====================================================================");
		System.out.println("Testing edf 1: ");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 3, 0, 6, 0, 6, 1));
			pts.addPTask(new PeriodicTask("2", 4, 0, 10, 0, 10, 2));
		} catch (Exception e) {		
		}
		
		Schedule edfSchedule = new Schedule();
		try {
			edfSchedule = Essence.schedule(pts, 0, 60, true, jobList -> EDF.hasHighestPriority(jobList), a -> EDF.isSchedulable(a));
		} catch (Exception e) {
			System.out.println(e);
		}
		
		System.out.println(edfSchedule);
		System.out.println(Essence.normalizeSchedule(edfSchedule));
		
	}
	
	@Test
	void edfSchedule3Tests() {
		System.out.println("====================================================================");
		System.out.println("Testing edf 2: ");
		PeriodicTaskSet pts = new PeriodicTaskSet();
		try {
			pts.addPTask(new PeriodicTask("1", 18, 0, 48, 0, 48, 1));
			pts.addPTask(new PeriodicTask("2", 2, 0, 6, 0, 6, 2));
			pts.addPTask(new PeriodicTask("3", 1, 0, 4, 0, 4, 3));
		} catch (Exception e) {		
		}
		
		Schedule edfSchedule = new Schedule();
		try {
			edfSchedule = Essence.schedule(pts, 0, 50, true, jobList -> EDF.hasHighestPriority(jobList), a -> EDF.isSchedulable(a));
		} catch (Exception e) {
		}
		
		System.out.println(edfSchedule);
		System.out.println(Essence.normalizeSchedule(edfSchedule));
		
	}


}
