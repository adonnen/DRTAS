package algo.sched.offline;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;

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
		
		PeriodicTask longestExTimeTask = Essence.findLongestExTimeTask(pts);
//		System.out.println("intest task is: " + intestExTimeTask);
		try {
			pts.removeTask(longestExTimeTask);
		} catch (Exception e) {
			System.out.println(e);
		}
		 
		// find the best value for the new ex time
		int newWcet = longestExTimeTask.getWcet();
		
		for (PeriodicTask p : pts.getpTaskSet().values())
			newWcet += p.getWcet();
		
		newWcet /= (pts.getpTaskSet().size() + 1);
		// if all the tasks have the same ex time, reduce it
		if (newWcet == longestExTimeTask.getWcet()) newWcet /= 2;
		
		
		int freeId = nextFreeHighestId(pts);
		int index = 1;
		
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
		
		
		//(String name, int exTime, int startTime, int period, int phase, int relDeadline, int id)
		// find the highest id (next free id)
		// divide the wcet of the intest while it's length is >= newWcet
		// by each division create a new task with the same period but reduced wcet
		// if there are several tasks with the same max ex time, it would be eventually overwhelmed by number of attempts in the calling function 
		
		
		return ptsNew;
	} 
	
	public static FlowNetwork constructFlowNetwork (ArrayList<Job> totalJobList, int hyperPeriod, int frameSize) {
		/*
		 * Flow network graph will have the following structure:
		 * first line : source to other nodes
		 * lines one - numOfJobs : valid mappings from jobs to frames
		 * lines numOfJobs+1 - numOfJobs+numOfFrames : mappings from frames to the sink
		 * last line : sink
		 */
		
		FlowNetwork fn;
		
		int totalSize = totalJobList.size() + hyperPeriod/frameSize + 2;
		System.out.println(totalJobList.size());
		System.out.println(hyperPeriod/frameSize);
		System.out.println(totalJobList);
		
		
		fn = new FlowNetwork(totalSize);	
		fn.numFrames = hyperPeriod/frameSize;
		fn.numJobs = totalJobList.size();
		fn.sink = fn.numFrames + fn.numJobs + 1;
		fn.source = 0;

		for (int i = 1; i <= totalJobList.size(); ++i) {
			fn.residualGraph[0][i] = totalJobList.get(i-1).getExecutionTime();
		}
		
		ArrayList<Frame> frameList = new ArrayList<Frame>();
		for (int i = 0; i < hyperPeriod/frameSize; ++i)
			frameList.add(new Frame(i * frameSize, (i+1) * frameSize));
		
		for (Job j : totalJobList)
			for (Frame f : frameList)
				if (j.getReleaseTime() <= f.start &&  f.end <= j.getAbsoluteDeadline())
					fn.residualGraph[totalJobList.indexOf(j)+1][totalJobList.size()+1+frameList.indexOf(f)] = frameSize;

		for (int i = 1; i <= frameList.size(); ++i)
			fn.residualGraph[totalJobList.size()+i][totalSize-1] = frameSize;
		
		for (int i = 0; i < totalSize; ++i) {
			for (int j = 0; j < totalSize; ++j)
				System.out.print(fn.residualGraph[i][j] + " ");
			System.out.println();
		}
		
		return fn;
	}
	
	public static boolean isValidFlowNetwork (FlowNetwork fn) {
		
		return true;
	}
	
	public static Schedule schedule (PeriodicTaskSet pts) throws NotSchedulableException {
		
		if (pts.utilizaton().compareTo(BigDecimal.ONE) > 0) throw new NotSchedulableException("The given task set is not schedulable!");
		
		int hyperPeriod = pts.hyperPeriod();
		ArrayList<Job> totalJobList = Essence.generateSortedJobList(pts, 0, hyperPeriod-1, (o1, o2) -> Integer.compare(o1.getInstanceId(), o2.getInstanceId()));
		Collections.sort(totalJobList, (o1, o2) -> Integer.compare(o1.getTaskId(), o2.getTaskId()));
		System.out.println(totalJobList);
		ArrayList<Integer> frameSizes = computeFeasibleFrameSizes(pts);
		FlowNetwork fn;
		Flow f;
		
		int maxAttempts = 10;
		
		do {
			// construct flow network
			for (int size : frameSizes) {
				fn = constructFlowNetwork(totalJobList, hyperPeriod, size);
				
				try {
					f = MaxFlow.fordFulkerson(fn, fn.numFrames + fn.numJobs + 2);
				} catch (Exception e) {
					continue;
				}
				
				if (isFullyMapped(totalJobList, f)) break;
				
			}
			
			
			// compute flow
			
			// if not schedulable, use the next frame size
			
			// if no other frame sizes, do job slicing and retry
			
			// construct schedule from the flow network and return
		} while (maxAttempts > 0);
		
		return new Schedule();
		
	}
	
	
	
	private static int nextFreeHighestId (PeriodicTaskSet pts) {
		int highestId = 0;
		
		for (PeriodicTask p : pts.getpTaskSet().values())
			if (p.getId() > highestId) highestId = p.getId();
			
		return highestId + 1;
	}
	
	private static boolean isFullyMapped (ArrayList<Job> jobList, Flow f ) {
		return true;
	}
	

}
