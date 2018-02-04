package algo.sched.hard;

import java.util.ArrayList;
import java.math.RoundingMode;
import java.math.MathContext;
import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

import ds.*;
import algo.sched.*;
import exceptions.*;

public class RMS {
	private static MathContext mc = new MathContext(3, RoundingMode.HALF_UP);	
	
	public static BigDecimal llBound (int numTasks) {
		/*
		 * This function computes the Liu Layland bound for the given task set
		 */
		
		if (numTasks == 0) return BigDecimal.ZERO;
		
		double exponent = (double) 1/numTasks;
		
		return new BigDecimal(numTasks * (Math.pow(2, exponent) - 1), mc);
	}
	
	public static boolean isLiuLaylandSchedulable(PeriodicTaskSet pts) {
		/*
		 * This function returns true if the given periodic task set fulfills the Liu-Layland bound
		 * Careful: if the result of the function is FALSE, the task set is not necessarily not schedulable!
		 */
		return (pts.utilizaton().compareTo(llBound(pts.getpTaskSet().size())) < 0) ? true : false;
	}
	
	public static BigDecimal hyperbolicBound (PeriodicTaskSet pts) {
		/*
		 * This function returns the value of the hyperbolic bound for a task set
		 */
		BigDecimal product = BigDecimal.ONE;
		for (PeriodicTask t : pts.getpTaskSet().values())
			product = product.multiply(t.utilization());
		
		return product;
	}
	
	public static boolean isHyperbolicallySchedulable(PeriodicTaskSet pts) {
		/*
		 * This function returns true if the given periodic task set fulfills the hyperbolic bound
		 * Careful: if the result of the function is FALSE, the task set is not necessarily not schedulable!
		 */
		BigDecimal product = BigDecimal.ONE;
		for (PeriodicTask t : pts.getpTaskSet().values())
			product = product.multiply(t.utilization().add(BigDecimal.ONE), mc);
		
//		System.out.println(product);
		
		return (product.compareTo(new BigDecimal("2.0")) <= 0);
	}
	
	public static boolean isHarmonicTaskSet(PeriodicTaskSet pts) {
		/*
		 * This function returns true if the given task set is harmonic (simply periodic)
		 */
		
		for (PeriodicTask p1 : pts.getpTaskSet().values())
			for (PeriodicTask p2 : pts.getpTaskSet().values()) {
				if (p1.getPeriod() % p2.getPeriod() != 0 && 
					p2.getPeriod() % p1.getPeriod() != 0)
						return false;
			}

		return true;
	}
	
	public static boolean isSchedulable(PeriodicTaskSet pts) {
		/*
		 * This function checks schedulability of a given periodic task set in 3 steps:
		 * 1. If hyperbolically, then it's fine
		 * 2. Harmonic task set with utilization <= 1 is always schedulable
		 * 3. TDA analysis
		 */
		
		//if (isLiuLaylandSchedulable(pts)) return true;
		
		if (isHyperbolicallySchedulable(pts)) return true;
		
		if (isHarmonicTaskSet(pts) && pts.utilizaton().compareTo(BigDecimal.ONE) <= 0)	return true;				
		
		if (TDA.isTDASchedulable(pts, false)) return true;
		
		return false;
	}
	
	public static Job hasHighestPriority(ArrayList<Job> jobList) {
		/*
		 * This function returns the job with highest priority at the current time point
		 */	
		Job jHighest = new Job();
		
		for (Job j : jobList) 
			if (j.getPrio() > jHighest.getPrio()) // j.getAbsoluteDeadline() >= timePoint  &&  j.getReleaseTime() <= timePoint && 
				jHighest = j;
		
		return jHighest;
		
	}
	
		
//	public static Job jobToExecute(JobSet js, int timePoint) {	
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
			s = Essence.schedule(pts, 0, 10*maxPeriod, true, jobList -> RMS.hasHighestPriority(jobList));
		} catch (Exception e) {
			
		}
		
		
		return s;
	}
	
	public static PeriodicTaskSet generateFullUtilTaskSet (int numTasks, int minPeriod, int maxPeriod) {
		/*
		 * This function generates a task set which totally utilizes the processor but still has idle times in the schedule
		 */
		
		
		
		return new PeriodicTaskSet();
	}
	
	public static PeriodicTaskSet generateHarmonicTaskSet (int numTasks, int minPeriod, int maxPeriod) {
		/*
		 * This function generates a harmonic task set
		 */
		PeriodicTaskSet pts;
		ArrayList<Integer> harmonicPeriods = new ArrayList<Integer>();
		ArrayList<BigDecimal> utilizationsList;
		
		Random r = new Random();
				
		int newPeriod = Essence.generateRandomInteger(minPeriod, maxPeriod, r);
		harmonicPeriods.add(newPeriod);
		for (int i = 1; i < numTasks; ++i) {
			
			int lcmOfExistingPeriods = newPeriod;
			for (int num : harmonicPeriods)
				lcmOfExistingPeriods = Essence.lcm(lcmOfExistingPeriods, num);
			
			newPeriod = 	lcmOfExistingPeriods * Essence.generateRandomInteger(1, (maxPeriod/lcmOfExistingPeriods > 1) ? maxPeriod/lcmOfExistingPeriods : 1, r);	
			harmonicPeriods.add(newPeriod);
		}
		
		boolean wrongUtil;
		boolean zeroExTimeTask;
		int numAttempts = 100;
				
		do {
			pts = new PeriodicTaskSet();
			wrongUtil = false;
			zeroExTimeTask = false;
			
			
			utilizationsList = Essence.generateUtilizations(numTasks, new BigDecimal("50.000"), new BigDecimal(100.00));							
			System.out.println(utilizationsList);
			
			try {
				for (int i = 1; i <= utilizationsList.size(); i++) 
					pts.addPTask(Essence.generateRandomTask(i, utilizationsList.get(i-1).divide(Essence.HUNDRED), harmonicPeriods.get(i-1)));
			} catch (Exception e) {
				System.out.println(e);
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
		} while ((wrongUtil || zeroExTimeTask) && numAttempts > 0 && !RMS.isSchedulable(pts));

		return pts;
		
	}
	
	public static PeriodicTaskSet generateGreyZoneTaskSet (int numTasks, int minPeriod, int maxPeriod) {
		/*
		 * This function generates a task set which violates LL bound, but is still schedulable
		 */
		
		int maxAttempts = 1000;
		PeriodicTaskSet pts;
		boolean zeroWCET;
		BigDecimal bound = llBound(numTasks).setScale(2, RoundingMode.UP).multiply(Essence.HUNDRED);
		
		do {
			zeroWCET = false;
			
			pts = Essence.generateRandomTaskSet(numTasks, 
					bound, Essence.HUNDRED, minPeriod, maxPeriod, 1);			
			maxAttempts--;
			
			for (PeriodicTask p : pts.getpTaskSet().values())
				if (p.getWcet() == 0) { zeroWCET = true; break;}
			
		}
		while ((!TDA.isTDASchedulable(pts, false) || zeroWCET) && maxAttempts > 0);
		
		if (maxAttempts == 0) return new PeriodicTaskSet();
		
		return pts;
	}
	
	public static boolean isValid(Schedule rmsSchedule) {
		/*
		 * Check if the schedule is:
		 * 1. generally valid
		 * 2. also satisfies RMS constraints
		 */		
		
		return rmsSchedule.isValid() && isValidFixedPrioSchedule(rmsSchedule);
	}
	
	private static boolean isValidFixedPrioSchedule(Schedule fpSchedule) {
		/*
		 * Check if the schedule is:
		 * 1. generally valid
		 * 2. also satisfies fixed prio constraints
		 */
		
		boolean isValid = true;
		
		for (ScheduleUnit su : fpSchedule.getSched())
			for (ScheduleUnit suOther : fpSchedule.getSched())
				if (su.startTime < suOther.startTime && su.job.getPrio() > suOther.job.getPrio()) isValid = false;
		
		return (isValid && fpSchedule.isValid());
	}
	
	public static Schedule transformFPtoRMSSchedule(Schedule fpSchedule) {
		/*
		 * Transforms a valid fixed-priority schedule to a valid RMS schedule
		 */
		Schedule validRMSSchedule = new Schedule();
		
		return validRMSSchedule;
	}

}
