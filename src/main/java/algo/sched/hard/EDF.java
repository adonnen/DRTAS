package algo.sched.hard;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Random;

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
	
	public static Schedule generateRandomSchedule (int numTasks, int minPeriod, int maxPeriod) {
		/*
		 * This function generates a random schedule with the predefined number of Tasks
		 */
		PeriodicTaskSet pts = Essence.generateRandomTaskSet(numTasks, Essence.randomBigDecimalFromRange(new BigDecimal("30.000"), new BigDecimal("90.000")), 
				Essence.randomBigDecimalFromRange(new BigDecimal("90.000"), new BigDecimal("100.000")), 
				minPeriod, maxPeriod, 1);
		
		Schedule s = new Schedule();
		try {
			s = Essence.schedule(pts, 0, 10*maxPeriod, true, jobList -> EDF.hasHighestPriority(jobList), a -> EDF.isSchedulable(a));
		} catch (Exception e) {
			
		}
		
		
		return s;
	}
	
	public static PeriodicTaskSet generateSameDeadline (int numTasks, int minPeriod, int maxPeriod, boolean idTieBreaker) {
		/*
		 * This function generates a task set which , when scheduled, will have at least two tasks with the same deadline
		 */
		
		return EDF.generateTieBreaker(numTasks, minPeriod, maxPeriod, false);
	}
	
	public static PeriodicTaskSet generateSameDeadlineSameRT (int numTasks, int minPeriod, int maxPeriod) {
		/*
		 * This function generates a task set which , when scheduled, will have at least two tasks with the same deadline and the same release time
		 */
		
		return EDF.generateTieBreaker(numTasks, minPeriod, maxPeriod, true);
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
	
	
	private static PeriodicTaskSet generateTieBreaker (int numTasks, int minPeriod, int maxPeriod, boolean idTieBreaker) {
		/*
		 * This function generates a task set which , when scheduled, will have at least two tasks with the same deadline
		 */
		
		// numtasks must be >= 2
		// determine, how many equals I may have: at least two, at most all of the tasks
		// randomize utilization/extime for them
		// 
		// 
		
		PeriodicTaskSet pts;
		BigDecimal utilization; 		
		ArrayList<BigDecimal> utilizationsList;
		
		boolean wrongUtil;
		boolean zeroExTimeTask;
		int numAttempts = 100;
		
		Random r = new Random();
		
		
		do {
			pts = new PeriodicTaskSet();
									
			// for correction checking
			wrongUtil = false;
			zeroExTimeTask = false;
			
			utilizationsList = Essence.generateUtilizations(numTasks, new BigDecimal("50.000"), new BigDecimal("100.000"));
			System.out.println(utilizationsList);
			
			int numTasksWithSamePeriod = Essence.generateRandomInteger(2, numTasks, r);
			int equalPeriod = Essence.generateRandomInteger(minPeriod, maxPeriod, r);
			
			try {
				for (int i = 1; i <= utilizationsList.size(); ++i)
					if (i <= numTasksWithSamePeriod) {
						if (idTieBreaker)
							pts.addPTask(Essence.generateRandomTask(i, utilizationsList.get(i-1).divide(Essence.HUNDRED), equalPeriod));
						else 
							pts.addPTask(Essence.generateRandomTask(i, utilizationsList.get(i-1).divide(Essence.HUNDRED), 
									equalPeriod* r.nextInt((int) ((100L < maxPeriod/equalPeriod) ?  100 : maxPeriod/equalPeriod))));
					} 
					else
						pts.addPTask(Essence.generateRandomTask(i, utilizationsList.get(i-1).divide(Essence.HUNDRED), minPeriod, maxPeriod));
			} catch (Exception e) {
				System.out.printf("wtf!!!");
			}
			
			// if the resulting util lies outside of the acceptable scope, regenerate the task set
			BigDecimal resultingUtil = pts.utilizaton();
			if (resultingUtil.compareTo(BigDecimal.ONE) > 0) wrongUtil = true;
			
			// if the resulting task set has a task with utilization == 0, redo
			for (PeriodicTask p : pts.getpTaskSet().values())
				if (p.getWcet() == 0) zeroExTimeTask = true;
			
			
//			System.out.println("Resulting utilization: " + resultingUtil);
			
//			for (int i = 0; i < utilizationsList.size(); i++)
//				if (utilizationsList.get(i).compareTo(BigDecimal.ZERO) == 0) containsZero = true;
			
			numAttempts--;
		} while ((wrongUtil || zeroExTimeTask) && numAttempts > 0);

		return pts;
	}

}
