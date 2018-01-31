package algo.sched;

import java.util.ArrayList;
import java.util.Random;
import java.util.function.Function;

import algo.sched.hard.EDF;
import algo.sched.hard.RMS;

import java.math.BigDecimal;
import java.math.RoundingMode;

import ds.*;
import exceptions.InvalidTimeInterval;
import exceptions.NotSchedulableException;
import exceptions.ViolatedDeadlineException;



public final class Essence {
	
	public static BigDecimal HUNDRED = BigDecimal.TEN.multiply(BigDecimal.TEN);
	public static Schedule schedule;
	
	public static long hyperPeriod(PeriodicTaskSet pts) {
		if (pts.getpTaskSet().size() == 0) return 0;
		
//		System.out.println("hello");
		ArrayList<PeriodicTask> ptArrayList = new ArrayList<PeriodicTask>();
		ptArrayList.addAll(pts.getpTaskSet().values());
//		System.out.println(ptArrayList);
		long result = ptArrayList.get(0).getPeriod();
		
		for (PeriodicTask p : ptArrayList) 
			result = lcm(result, p.getPeriod());
		
		return result;
		
	}
	
	public static long nextEarliestRelease(ArrayList<Job> jobList, long timePoint) {
		/*
		 * This function returns the relative offset of the next earliest job release
		 */
		if (jobList.isEmpty()) return 0;				
		return jobList.get(0).getReleaseTime() - timePoint;
		
	}
	
	public static boolean twoInstancesActive (ArrayList<Job> jobList) {
		/*
		 * This function returns true if there are two different instances of the same task
		 * If task set was not checked to be schedulable, then there is a possibility of deadline violation during scheduling
		 * Every time a new task instance arrives, the active job set is checked if there are two different instances of the same task
		 * If so, then the earlier of them has not yet been executed to the end and remains ready/active
		 * In such a case, an exception is raised and the process of scheduling terminates
		 */
		
		for (Job j1 : jobList)
			for (Job j2 : jobList)
				if (j1.getTaskId() == j2.getTaskId() && j1.getInstanceId() != j2.getInstanceId())
					return true;
		
		return false;
	}
	
	public static Schedule schedule(PeriodicTaskSet pts, long fromTime, long toTime, boolean onlyIfSchedulable, Function<ArrayList<Job>, Job> hasHighestPriority) 
			throws NotSchedulableException, InvalidTimeInterval, ViolatedDeadlineException {
		/*
		 * This function returns an rms schedule from fromTime to toTime, 
		 * where all of the jobs are generated from the periodic task set pts.
		 * This is no simulation, it just provides a complete schedule in one step.
		 */
		if (onlyIfSchedulable)
				if (!RMS.isSchedulable(pts)) 
					throw new NotSchedulableException("This task set is not schedulable under RMS!");
		if (fromTime < 0 || toTime < fromTime || fromTime >= toTime) 
			throw new InvalidTimeInterval("Scheduling is not possible in such a time interval!");
		
		schedule = new Schedule();	
		ArrayList<Job> totalJobList = pts.generateJobsWithRMSPriorities(fromTime, toTime);		
		totalJobList.sort((o1, o2) -> Long.compare(o1.getReleaseTime(), o2.getReleaseTime()));
//		System.out.println(totalJobList);
		
		// Initialize an active job list
		JobSet activeJobSet = new JobSet();
		long time = fromTime;
		
		while (time < toTime) {

			// new arrivals so far?
			for (Job j : totalJobList)
				if (j.getReleaseTime() <= time) {
					j.setState(STATE.READY);
					activeJobSet.add(j);
				}
			// remove them from the total list, they will not be scheduled
			for (Job j : activeJobSet.getJobList())
				totalJobList.remove(j);	
			
			// If task set was not checked to be schedulable, then there is a possibility of deadline violation during scheduling
			// Every time a new task instance arrives, the active job set is checked if there are two different instances of the same task
			if (!onlyIfSchedulable) {
				if (Essence.twoInstancesActive(activeJobSet.getJobList()))
					throw new ViolatedDeadlineException("A deadline is violated at time point " + time);
			}
			
			long nextRelease = (Essence.nextEarliestRelease(totalJobList, time) == 0) ? toTime : Essence.nextEarliestRelease(totalJobList, time);
			
			// select the active job with the highest priority
			Job currentJob = hasHighestPriority.apply(activeJobSet.getJobList());
			
			// if the current job is a dummy, the active job list is empty
			// jump to the next release
			if (currentJob.getInstanceId() == 0) {
				time += nextRelease;
				continue;
			}
			
			// if the current job can be completely scheduled before the next release, 
			// it is safe to schedule it completely and jump to the time point when its execution ends
			if (currentJob.getExecutionTime() <= nextRelease) {
				long upperLimit = time + currentJob.getExecutionTime();
				schedule.add(currentJob, time, (upperLimit > toTime) ? toTime : upperLimit, null);
				time = time + currentJob.getExecutionTime();
				activeJobSet.remove(currentJob);
			}
			else { // else schedule until the next release, reduce the remaining execution time, and then decide with newly arrived jobs
				schedule.add(currentJob, time, time + nextRelease, null);
				time += nextRelease;
				currentJob.setExecutionTime(currentJob.getExecutionTime() - nextRelease);
			}
		}
		
		return schedule;
	}
	
	public static Schedule normalizeSchedule (Schedule s) {
		/*
		 * This function returns the schedule s where two consecutive execution of the same job are merged into a single execution
		 */
		if (s.getSched().isEmpty() || s == null) return new Schedule();
		
		ScheduleUnit[] sArray = s.getSched().toArray(new ScheduleUnit[s.getSched().size()]);
		Schedule sNew = new Schedule();
		
		for (int i = 0; i < sArray.length-1; ){
//			System.out.println("i = " + i);
			if (sArray[i+1].job.equals(sArray[i].job)) {
				int j = i+1;
//				System.out.println("j = " + j);
				while (j < sArray.length && sArray[j].job.equals(sArray[j-1].job)) j++;
				sArray[i].endTime = sArray[j-1].endTime;
				sNew.add(sArray[i]);
				i = j;
			}
			else {
				sNew.add(sArray[i]);
				i++;
			}
			
		}
		
		try {
			if (!sArray[sArray.length-1].job.equals(sNew.getSched().get(sNew.getSched().size() - 1).job))
				sNew.add(sArray[sArray.length-1]);
		} catch (Exception e) {
		}		
		
		return sNew;
	}
	
	public static PeriodicTaskSet generateRandomTaskSet (int numTasks, BigDecimal minUtilization, BigDecimal maxUtilization, long minPeriod, long maxPeriod, int scale) {
		/*
		 * This function generates a random schedule with the predefined number of Tasks
		 */
		PeriodicTaskSet pts;
		BigDecimal utilization; 		
		ArrayList<BigDecimal> utilizationsList;
		
		boolean wrongUtil;
		boolean zeroExTimeTask;
		int numAttempts = 100;
		
		do {
			pts = new PeriodicTaskSet();
			utilizationsList = new ArrayList<BigDecimal>();
			utilization = randomBigDecimalFromRange(minUtilization, maxUtilization).setScale(3, RoundingMode.DOWN);
			
			// for correction checking
			wrongUtil = false;
			zeroExTimeTask = false;
			
//			System.out.println("Aimed at utilization: " + utilization);
			
			Random r = new Random();
			int sum = 0;
			
			for (int i = 1; i <= numTasks; i++) {
		        if (i != numTasks) {
		        		int temp = r.nextInt((utilization.intValue() - sum) / (numTasks - i)) + 1;
		        		utilizationsList.add(new BigDecimal(temp));
		            sum += temp;
		        } else
		            utilizationsList.add(new BigDecimal(utilization.intValue() - sum));
		    }
			
			System.out.println(utilizationsList);
			
			try {
				for (int i = 1; i <= utilizationsList.size(); i++) 
					pts.addPTask(generateRandomTask(i, utilizationsList.get(i-1).divide(HUNDRED), minPeriod, maxPeriod));
			} catch (Exception e) {
				System.out.printf("wtf!!!");
			}
			
			// if the resulting util lies outside of the acceptable scope, regenerate the task set
			BigDecimal resultingUtil = pts.utilizaton();
			if (resultingUtil.compareTo(BigDecimal.ONE) > 0 || resultingUtil.compareTo(minUtilization.divide(HUNDRED)) < 0) wrongUtil = true;
			
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
	
	public static PeriodicTaskSet generateTaskSetWithUtilONE (int numTasks, long minPeriod, long maxPeriod) {
		/*
		 * This function generates a task set which totally utilizes the processor but still has idle times in the schedule
		 */
		PeriodicTaskSet pts;
		int maxAttempts = 1000;
		
		do {
			pts = generateRandomTaskSet(numTasks, HUNDRED, HUNDRED, minPeriod, maxPeriod, 3);
			maxAttempts--;
		}
		while (BigDecimal.ONE.subtract(pts.utilizaton()).compareTo(new BigDecimal ("0.005")) > 0 && maxAttempts > 0);
		
		return pts;
	}
	
	public static PeriodicTask generateRandomTask(int id, BigDecimal utilization, long minPeriod, long maxPeriod) {
		/*
		 * This function generates a random task with the given utilization
		 * For that, first, the period is computed, which lies between two time points
		 * Then, the right execution time is computed and the task is generated
		 * The task has no explicit starting time, phase, and the relative deadline is equal to the period
		 */
		
		long bestNominator = 0;
		long bestDenominator = minPeriod;
		BigDecimal maxUtil = BigDecimal.ZERO;
		
		for (long nom = 1; nom <= maxPeriod; nom++)
			for (long denom = max(nom, minPeriod); denom <= maxPeriod; denom++) {
				BigDecimal currentUtil = new BigDecimal((double) nom / denom).setScale(3, RoundingMode.HALF_UP);
				if (currentUtil.compareTo(maxUtil) > 0 && currentUtil.compareTo(utilization) <= 0) {
//					System.out.println(currentUtil);
//					System.out.println("current " + nom + " " + denom);
//					System.out.println("best " + bestNominator + " " + bestDenominator);
					maxUtil = currentUtil;
					bestNominator = nom;
					bestDenominator = denom;
				}
					
			}
				
			
//		System.out.println("For u = " + utilization + " the period is " + bestDenominator + " and ex time is " + bestNominator);
		
		return new PeriodicTask("Task" + id, bestNominator, 0, bestDenominator, 0, bestDenominator, id);
	}
	
	
	public static long totalIdleTime(Schedule s) {
		/*
		 * This function returns the total idle time for the generated schedule
		 */
		long execTime = 0;
		
		for (ScheduleUnit su : s.getSched())
			execTime += su.endTime - su.startTime;
		
		long totalTime = 0;
		try {
			totalTime = s.getSched().get(s.getSched().size()-1).endTime - (s.getSched().get(0).startTime);
		} catch (Exception e) {
			
		}
		
		return totalTime - execTime;
	}
	
	
	
	public static PeriodicTaskSet generateDifferentEDFtoRMS (int numTasks, long minPeriod, long maxPeriod) {
		
		PeriodicTaskSet pts = new PeriodicTaskSet();
		
		boolean isSame;
		int maxAttempts = 100;

		do {
			isSame = false;
			
			pts = RMS.generateGreyZoneTaskSet(numTasks, minPeriod, maxPeriod);
			try {
				Schedule sRMS = schedule(pts, 0, maxPeriod*2, true, jobList -> RMS.hasHighestPriority(jobList));
				Schedule sEDF = schedule(pts, 0, maxPeriod*2, true, jobList -> EDF.hasHighestPriority(jobList));
				if (normalizeSchedule(sRMS).equals(normalizeSchedule(sEDF))) isSame = true;
			} catch (Exception e) {
				System.out.println(e);
			}
			
			maxAttempts--;
						
		} while (isSame && maxAttempts > 0);
		
		return pts;
	}
	
	public static PeriodicTask findLongesExTimeTask (PeriodicTaskSet pts) {
		PeriodicTask maxSoFar = new PeriodicTask();
		
		for (PeriodicTask p : pts.getpTaskSet().values()) 
			if (p.getWcet() > maxSoFar.getWcet()) maxSoFar = p;
		
		return maxSoFar;
	}
	
	
	/*
	 * Math
	 */
	
	
	public static long generateRandomInteger(long aStart, long aEnd, Random aRandom){
	    if (aStart > aEnd) {
	      long temp = aStart;
	      aStart = aEnd;
	      aEnd = temp;
	    }

	    long range = aEnd - aStart + 1;
	    // compute a fraction of the range, 0 <= frac < range
	    long fraction = (long)(range * aRandom.nextDouble());
	    return (fraction + aStart);    
	  }
	
	
	public static BigDecimal randomBigDecimalFromRange(BigDecimal min, BigDecimal max) {
		BigDecimal range = new BigDecimal(Math.abs(max.subtract(min).doubleValue()));
	    if (min.compareTo(max) <= 0)
	    {
	    		return range.multiply(new BigDecimal(Math.random())).add(min).setScale(0, RoundingMode.HALF_UP);
	    }
	    else
	    		return range.multiply(new BigDecimal(Math.random())).add(max).setScale(0, RoundingMode.HALF_UP);
	}
	
	
	public static long gcd(long a, long b) {
	    while (b > 0) {
	        long temp = b;
	        b = a % b; // % is remainder
	        a = temp;
	    }
	    return a;
	}
	
	
	private static long lcm(long a, long b) {
	    return a * (b / gcd(a, b));
	}
	
	public static long max(long a, long b) {
		return (a > b) ? a : b;
	}
	
	public static long min(long a, long b) {
		return (a < b) ? a : b;
	}
	

	

}
