package algo.sched;

import java.util.ArrayList;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import ds.*;
import exceptions.*;

public class TDA {
	private static MathContext mc = new MathContext(3, RoundingMode.HALF_UP);
	
	public static long timeDemandFunction(PeriodicTaskSet pts, PeriodicTask pt, long timePoint) throws IllegalArgumentException, TaskSetTaskNonexistent {
		/*
		 * This function computes the time demand value for the task id from task set pts
		 * from time point 0 (assuming that all tasks arrive at their critical instances) until time point timePoint.
		 * Time demand is only computed up until the job's absolute deadline at the latest (even if timePoint exceeds that value)
		 */
		if (pts == null) throw new IllegalArgumentException("Task list is not existing!");
		
		long tdValue = 0;
				
		for (PeriodicTask p : pts.getpTaskSet().values()) 
			if (p.getPrio() > pt.getPrio()) {
				BigDecimal numPreemptions = BigDecimal.ONE;
				if (timePoint != 0) {
					numPreemptions = new BigDecimal(timePoint).divide(new BigDecimal(p.getPeriod()), mc);
					numPreemptions = numPreemptions.setScale(0, RoundingMode.UP); // take a ceiling
				}
					
//				System.out.println("Number of preemptions at time point " + timePoint + " for task " + pt.getId() + " by task " + p.getId() + " is " + numPreemptions);
				
				tdValue += numPreemptions.longValue() * p.getWcet();	
//				System.out.println("Time demand due to preemptions is " + tdValue);
			}
		
		return tdValue + pt.getWcet();	
	}
	
	public static boolean isTDASchedulable (PeriodicTaskSet pts, boolean showArithmeticSteps) {
		/*
		 * This function determines whether a task set is schedulable with the arithmetic TDA
		 * It simultaneously demonstrates the arithmetic steps of the algorithm (in the console mode)
		 */
		
		// Sort the tasks according to their periods
		ArrayList<PeriodicTask> sortedTasks = new ArrayList<PeriodicTask>(pts.getpTaskSet().values());
		sortedTasks.sort((o1, o2) -> Long.compare(o1.getPeriod(), o2.getPeriod()));
		
		// Assign priorities to tasks according to their periods
		int prio = sortedTasks.size();
		for (PeriodicTask p : sortedTasks) 
			p.setPrio(prio--);
		
		if (showArithmeticSteps) System.out.println("Time Demand Analysis for Task Set with " + sortedTasks.size() + " Tasks:");
				
		// For every task in the sorted list
		for (PeriodicTask p : sortedTasks) {
			if (showArithmeticSteps) System.out.println(p);
			long timePoint = 0;
			long demand = 0;
			ArrayList<Job> jobList = null;
			
			// create the job list of all the jobs of all the tasks up until the task p's relative deadline
			try {					
				jobList = new ArrayList<Job>();
				for (PeriodicTask pLower : sortedTasks) {
					if (pLower.getPeriod() <= p.getPeriod())
						jobList.addAll(pLower.generateJobs(0, p.getRelativeDeadline()));
				}
			} catch (Exception e) {
					System.out.println(e);
			}
					
			// for every time point, until the relative deadline of the task p
			while (timePoint <= p.getRelativeDeadline()) {
				try {	
					// compute the new demand of the task p 
					demand = TDA.timeDemandFunction(pts, p, timePoint);
					if (showArithmeticSteps) System.out.println("Time point: " + timePoint + " Demand: " + demand);
					// }
				} catch (Exception e) {
					System.out.println(e);
				}
				
				// if demand meets supply, we're done with the task and can exit the cycle
				if (demand <= timePoint) {
					if (showArithmeticSteps) System.out.println("Task " + p.getId() + " with priority " + p.getPrio() + " is schedulable with WCRT = " + demand);
					break;
				}
				else timePoint = demand;	// else we have to press on			
			}
			if (showArithmeticSteps) System.out.println();
			// if the cycle was left with demand not meeting supply, the task is not schedulable
			if (timePoint > p.getRelativeDeadline()) {
				if (showArithmeticSteps) System.out.println("Task " + p.getId() + " violates its deadline " + p.getRelativeDeadline() + " with demand " + demand);
				return false;
			}
		}
		
		if (showArithmeticSteps) { 
			System.out.println("Task set is schedulable!");
			System.out.println();
		}		
		return true;
	}
	
	public static boolean isTDASchedulableFX (PeriodicTaskSet pts, boolean showArithmeticSteps) {
		/*
		 * This function determines whether a task set is schedulable with the arithmetic TDA
		 * It simultaneously demonstrates the arithmetic steps of the algorithm in the JavaFX GUI
		 */
		return true;
	}
	
	public static void graphicTDAScheduleFX (PeriodicTaskSet pts, boolean showArithmeticSteps) {
		/*
		 * This function generates graphical objects for the JavaFX GUI 
		 * It can simultaneously show the arithmetic steps of the TDA algorithm
		 */
	}

}
