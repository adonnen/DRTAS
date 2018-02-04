package algo.sched.offline;

import java.math.BigDecimal;
import java.util.ArrayList;

import ds.*;
import exceptions.*;
import algo.sched.Essence;

public class TimeLine {
	
	public static ArrayList<Integer> computeFeasibleFrameSizes(PeriodicTaskSet pts) {
		int maximumExecTime; 
		int hyperPeriod = pts.hyperPeriod();
		ArrayList<Integer> feasibleFrameSizes;
		
		int maxAttempts = 10;
		boolean jobSlicingTaken;
		
		do {
//			System.out.println("Attempt number " + (3-maxAttempts));
			jobSlicingTaken = false;
			feasibleFrameSizes = new ArrayList<Integer>();
			maximumExecTime = 0;
			
			for (PeriodicTask p: pts.getpTaskSet().values())
				if (p.getWcet() > maximumExecTime) maximumExecTime = p.getWcet();
			
			for (int f = maximumExecTime; f <= hyperPeriod; ++f) {
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
		
		PeriodicTask intestExTimeTask = Essence.findintesExTimeTask(pts);
//		System.out.println("intest task is: " + intestExTimeTask);
		try {
			pts.removeTask(intestExTimeTask);
		} catch (Exception e) {
			System.out.println(e);
		}
		 
		// find the best value for the new ex time
		int newWcet = intestExTimeTask.getWcet();
		
		for (PeriodicTask p : pts.getpTaskSet().values())
			newWcet += p.getWcet();
		
		newWcet /= (pts.getpTaskSet().size() + 1);
		// if all the tasks have the same ex time, reduce it
		if (newWcet == intestExTimeTask.getWcet()) newWcet /= 2;
		
		
		int freeId = nextFreeHighestId(pts);
		int index = 1;
		
		while (intestExTimeTask.getWcet() >= newWcet) {
			PeriodicTask pNew = new PeriodicTask(intestExTimeTask.getName() + index, 
												 newWcet, 
												 intestExTimeTask.getStartingTime(),
												 intestExTimeTask.getPeriod(),
												 intestExTimeTask.getPhase(),
												 intestExTimeTask.getRelativeDeadline(),
												 freeId);
			
			try {
				ptsNew.addPTask(pNew);
			} catch (Exception e) {
				System.out.println(e);
			}
			
			intestExTimeTask.setWcet(intestExTimeTask.getWcet()-newWcet);			
			freeId++;
			index++;
		}
		
		if (intestExTimeTask.getWcet() > 0) {
			PeriodicTask pNew = new PeriodicTask(intestExTimeTask.getName() + index, 
												 intestExTimeTask.getWcet(), 
												 intestExTimeTask.getStartingTime(),
												 intestExTimeTask.getPeriod(),
												 intestExTimeTask.getPhase(),
												 intestExTimeTask.getRelativeDeadline(),
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
		
		
		//(String name, int exTime, int startTime, int period, int phase, int relDeadline, int id)
		// find the highest id (next free id)
		// divide the wcet of the intest while it's length is >= newWcet
		// by each division create a new task with the same period but reduced wcet
		// if there are several tasks with the same max ex time, it would be eventually overwhelmed by number of attempts in the calling function 
		
		
		return ptsNew;
	} 
	
	public static FlowNetwork constructFlowNetwork (PeriodicTaskSet pts) throws NotSchedulableException {
		/*
		 * Flow network graph will have the following structure:
		 * first line : source to other nodes
		 * lines one - numOfJobs : valid mappings from jobs to frames
		 * lines numOfJobs+1 - numOfJobs+numOfFrames : mappings from frames to the sink
		 * last line : sink
		 */
		if (pts.utilizaton().compareTo(BigDecimal.ONE) > 0) throw new NotSchedulableException("The given task set is not schedulable!");
		
		FlowNetwork fn;
		
		int hyperPeriod = pts.hyperPeriod();
		
		ArrayList<Job> totalJobList = Essence.generateSortedJobList(pts, 0, hyperPeriod-1, (o1, o2) -> Integer.compare(o1.getTaskId(), o2.getTaskId()));
		
		ArrayList<Integer> frameSizes = computeFeasibleFrameSizes(pts);
		
		for (int fs : frameSizes ) {
			fn = new FlowNetwork(totalJobList.size() + fs + 2);
			
			
		}
		
		
		
		
		return fn;
	}
	
	public static boolean isValidFlowNetwork (FlowNetwork fn) {
		
		return true;
	}
	
	
	
	private static int nextFreeHighestId (PeriodicTaskSet pts) {
		int highestId = 0;
		
		for (PeriodicTask p : pts.getpTaskSet().values())
			if (p.getId() > highestId) highestId = p.getId();
			
		return highestId + 1;
	}
	

}
