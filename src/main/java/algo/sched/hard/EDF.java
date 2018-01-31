package algo.sched.hard;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;

import ds.*;
import algo.sched.*;
import exceptions.*;

public class EDF {
	private static MathContext mc = new MathContext(3, RoundingMode.HALF_UP);
	
	public static boolean isSchedulable(PeriodicTaskSet pts) {
		/*
		 * This function returns true if the given periodic task set is schedulable under EDF
		 * i.e. its utilization is <= 1
		 */
		BigDecimal sum = BigDecimal.ZERO;
		
		for (PeriodicTask t : pts.getpTaskSet().values())
			sum = sum.add(t.utilization(), mc);
		
		return (sum.compareTo(BigDecimal.ONE) <= 0);
	}
	
	public static Job hasHighestPriority(ArrayList<Job> jobList) {
		/*
		 * This function returns the job with highest priority at the current time point
		 */	
		if (jobList.isEmpty()) return new Job();
		
		Job jHighest = jobList.get(0);
		
		for (Job j : jobList)
			if (j.getAbsoluteDeadline() < jHighest.getAbsoluteDeadline()) // j.getReleaseTime() <= timePoint && 
				jHighest = j;
			else if (j.getAbsoluteDeadline() == jHighest.getAbsoluteDeadline()) {
				if (j.getReleaseTime() < jHighest.getReleaseTime())
					jHighest = j;
				else if (j.getReleaseTime() == jHighest.getReleaseTime()) {
					if (j.getTaskId() < jHighest.getTaskId())
						jHighest = j;
				}
					
			}
					
		return jHighest;
		
	}
	
//	public static Job jobToExecute(JobSet js, BigDecimal timePoint) {	
//		/*
//		 * This function finds an active job which must be executed at the given time point
//		 * If the job is not executed at the moment, context switch occurs
//		 */
//		Optional<Job> firstElement = js.getJobList().stream().findFirst();
//		Job latestReleasedBefore = null;
//		try {
//			latestReleasedBefore = firstElement.get();
//		} catch (Exception e) {
//			
//		}
//		
//		for (Job j : js.getJobList()) {
//			if (j.getReleaseTime() <= timePoint && j.getPrio() > latestReleasedBefore.getPrio())
//				latestReleasedBefore = j;				
//		}
//		
//		return latestReleasedBefore;
//	}
	
	
	
	public static PeriodicTaskSet generateSameDeadline (long numTasks, BigDecimal fromTime, BigDecimal toTime) {
		/*
		 * This function generates a task set which , when scheduled, will have at least two tasks with the same deadline
		 */
		
		return new PeriodicTaskSet();
	}
	
	public static PeriodicTaskSet generateSameDeadlineSameRT (int numTasks, long minPeriod, long maxPeriod) {
		/*
		 * This function generates a task set which , when scheduled, will have at least two tasks with the same deadline and the same release time
		 */
		
		return new PeriodicTaskSet();
	}
	
	public static boolean isValid(Schedule edfSchedule) {
		/*
		 * Check if the schedule is:
		 * 1. generally valid
		 * 2. also satisfies EDF constraint:
		 * 		if job has earlier deadline, it has to be scheduled before
		 * 		the rest conditions (when deadlines are the same) are depending on the implementation of the tie breaker
		 */
		
		boolean isValid = true;
		
		for (ScheduleUnit su : edfSchedule.getSched())
			for (ScheduleUnit suOther : edfSchedule.getSched())
				if (su.job.getAbsoluteDeadline() < suOther.job.getAbsoluteDeadline() && su.startTime > suOther.startTime) isValid = false;
		
		return (isValid && edfSchedule.isValid());
	}
	
//	private static boolean isValidDynPrioSchedule(Schedule dynSchedule) {
//		/*
//		 * Check if the schedule is:
//		 * 1. generally valid
//		 * 2. also satisfies dyn prio constraints
//		 */
//		
//		return dynSchedule.isValid();
//	}
	
	public static Schedule transformDPtoEDFSchedule(Schedule dynSchedule) {
		/*
		 * Transforms a valid dyn-priority schedule to a valid EDF schedule
		 */
		Schedule validEDFSchedule = new Schedule();
		
		return validEDFSchedule;
	}

}
