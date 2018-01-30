package algo.sched;

import java.util.ArrayList;

import ds.*;
import algo.sched.Essence;

public class TimeLine {
	
	public static ArrayList<Long> computeFeasibleFrameSizes(PeriodicTaskSet pts) {
		long maximumExecTime; 
		long hyperPeriod = Essence.hyperPeriod(pts);
		ArrayList<Long> feasibleFrameSizes;
		
		int maxAttempts = 10;
		boolean jobSlicingTaken;
		
		do {
//			System.out.println("Attempt number " + (3-maxAttempts));
			jobSlicingTaken = false;
			feasibleFrameSizes = new ArrayList<Long>();
			maximumExecTime = 0;
			
			for (PeriodicTask p: pts.getpTaskSet().values())
				if (p.getWcet() > maximumExecTime) maximumExecTime = p.getWcet();
			
			for (long f = maximumExecTime; f <= hyperPeriod; ++f) {
				if (hyperPeriod % f == 0) {
					boolean smallEnough = true;
					
					for (PeriodicTask p: pts.getpTaskSet().values()) 
						if (2*f - Essence.gcd(f, p.getPeriod()) > p.getRelativeDeadline()) smallEnough = false;
					
					if (smallEnough) feasibleFrameSizes.add(f);
				}
			}
			
//			System.out.println("Feasible frame sizes : " + feasibleFrameSizes);
			
			if (feasibleFrameSizes.isEmpty()) {
				jobSlicingTaken = true;
				pts = jobSlicing(pts);
//				System.out.println("New pts is " + pts);
				maxAttempts--;
			}

		} while (jobSlicingTaken && maxAttempts > 0);
		
		return feasibleFrameSizes;
	}
	
	
	
	public static PeriodicTaskSet jobSlicing (PeriodicTaskSet pts) {
		if (pts.getpTaskSet().isEmpty()) return new PeriodicTaskSet();
		
		PeriodicTaskSet ptsNew = new PeriodicTaskSet();
		
		PeriodicTask longestExTimeTask = Essence.findLongesExTimeTask(pts);
//		System.out.println("Longest task is: " + longestExTimeTask);
		try {
			pts.removeTask(longestExTimeTask);
		} catch (Exception e) {
			System.out.println(e);
		}
		 
		// find the best value for the new ex time
		long newWcet = longestExTimeTask.getWcet();
		
		for (PeriodicTask p : pts.getpTaskSet().values())
			newWcet += p.getWcet();
		
		newWcet /= (pts.getpTaskSet().size() + 1);
		// if all the tasks have the same ex time, reduce it
		if (newWcet == longestExTimeTask.getWcet()) newWcet /= 2;
		
		
		long freeId = nextFreeHighestId(pts);
		long index = 1;
		
		while (longestExTimeTask.getWcet() >= newWcet) {
			PeriodicTask pNew = new PeriodicTask(longestExTimeTask.getName() + index, 
												 newWcet, 
												 longestExTimeTask.getStartingTime(),
												 longestExTimeTask.getPeriod(),
												 longestExTimeTask.getPhase(),
												 longestExTimeTask.getRelativeDeadline(),
												 freeId);
			
			try {
				ptsNew.addPTask(pNew);
			} catch (Exception e) {
				System.out.println(e);
			}
			
			longestExTimeTask.setWcet(longestExTimeTask.getWcet()-newWcet);			
			freeId++;
			index++;
		}
		
		if (longestExTimeTask.getWcet() > 0) {
			PeriodicTask pNew = new PeriodicTask(longestExTimeTask.getName() + index, 
												 longestExTimeTask.getWcet(), 
												 longestExTimeTask.getStartingTime(),
												 longestExTimeTask.getPeriod(),
												 longestExTimeTask.getPhase(),
												 longestExTimeTask.getRelativeDeadline(),
												 freeId);

			try {
				ptsNew.addPTask(pNew);
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		
		for (PeriodicTask p : pts.getpTaskSet().values())
			try {
				ptsNew.addPTask(p);
			} catch (Exception e) {
				System.out.println(e);
			}
		
		
		//(String name, long exTime, long startTime, long period, long phase, long relDeadline, long id)
		// find the highest id (next free id)
		// divide the wcet of the longest while it's length is >= newWcet
		// by each division create a new task with the same period but reduced wcet
		// if there are several tasks with the same max ex time, it would be eventually overwhelmed by number of attempts in the calling function 
		
		
		return ptsNew;
	} 
	
//	public static long[][] constructFlowNetwork (PeriodicTaskSet pts) {
//		
//		
//	}
	
	
	
	private static long nextFreeHighestId (PeriodicTaskSet pts) {
		long highestId = 0;
		
		for (PeriodicTask p : pts.getpTaskSet().values())
			if (p.getId() > highestId) highestId = p.getId();
			
		return highestId + 1;
	}
	

}
