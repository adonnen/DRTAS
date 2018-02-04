package ds;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.*;

import algo.sched.Essence;

public class PeriodicTask extends Task {
	protected static MathContext mc = new MathContext(3, RoundingMode.HALF_UP);
	protected int period;
	protected int phase;
	protected int relativeDeadline;
	private Optional<ArrayList<ResourceRequirement>> requiredResources;
	private Optional<Integer> skipFactor;

	public PeriodicTask(){
		super();
		this.period = 0;
		this.phase = 0;
		this.relativeDeadline = 0;
		this.requiredResources = Optional.empty();
		this.skipFactor = Optional.of(0);
	}

	public PeriodicTask(String name, int exTime, int startTime, int period, int phase, int relDeadline, int id) {
		super(name, exTime, startTime, id);
		this.period = period;
		this.phase = phase;
		this.relativeDeadline = relDeadline;
		this.requiredResources = Optional.empty();
		this.skipFactor = Optional.of(0);
	}

	public PeriodicTask(String name, int exTime, int startTime, int period, int phase, int relDeadline, int id, int priority) {
		super(name, exTime, startTime, id, priority);
		this.period = period;
		this.phase = phase;
		this.relativeDeadline = relDeadline;
		this.requiredResources = Optional.empty();
		this.skipFactor = Optional.of(0);
	}
	
	public PeriodicTask(String name, int exTime, int startTime, int period, int phase, int relDeadline, int id, int priority, ArrayList<ResourceRequirement> rr) {
		super(name, exTime, startTime, id, priority);
		this.period = period;
		this.phase = phase;
		this.relativeDeadline = relDeadline;
		this.requiredResources = Optional.ofNullable(rr);
		this.skipFactor = Optional.of(0);
	}
	
	public PeriodicTask(String name, int exTime, int startTime, int period, int phase, int relDeadline, int id, int priority, int sp) {
		super(name, exTime, startTime, id, priority);
		this.period = period;
		this.phase = phase;
		this.relativeDeadline = relDeadline;
		this.requiredResources = Optional.empty();
		this.skipFactor = Optional.ofNullable(sp);
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Period: " + this.period + "\n");
		sb.append("Phase: " + this.phase + "\n");
		sb.append("Relative Deadline: " + this.relativeDeadline + "\n");
		
		return (super.toString() + sb.toString());	
	}
	
	public int getPeriod() {
		return period;
	}

	public void setPeriod(int period) {
		this.period = period;
	}

	public int getPhase() {
		return phase;
	}

	public void setPhase(int phase) {
		this.phase = phase;
	}

	public int getRelativeDeadline() {
		return relativeDeadline;
	}

	public void setRelativeDeadline(int relativeDeadline) {
		this.relativeDeadline = relativeDeadline;
	}
	
	public Optional<ArrayList<ResourceRequirement>> getRequiredResources() {
		return requiredResources;
	}

	public Optional<Integer> getSkipFactor() {
		return skipFactor;
	}

	public void setSkipFactor(int skipFactor) {
		this.skipFactor = Optional.of(skipFactor);
	}
	
	public void addResourceRequirement(ResourceRequirement newRequirement) {
		if (this.requiredResources.isPresent()) {
			this.requiredResources.get().add(newRequirement);
		}
		else {
			this.requiredResources = Optional.of(new ArrayList<ResourceRequirement>());
			this.requiredResources.get().add(newRequirement);
		}
	}
	
	public void removeResourceRequirement(String name) {
		if (this.requiredResources.isPresent())
			for (int i = 0; i < this.requiredResources.get().size(); ++i)
				if (this.requiredResources.get().get(i).r.getName().equals(name)) this.requiredResources.get().remove(i);
	}
	
	
	public BigDecimal utilization() throws ArithmeticException {
		if (this.period == 0) throw new ArithmeticException("denominator == 0");
		return (new BigDecimal(this.getWcet())).divide(new BigDecimal(this.getPeriod()), mc);
	}
	
	public Job generateNextJob(int timePoint, int nextFreeId) { // shouldn't the task be a parameter as well?
		/*
		 * This function is needed to generate new jobs as int as scheduling is running (with no time limits)
		 * Step size of the simulation should be considered here, so that enough jobs of the same task are generated until the next step occurrs
		 */
		return new Job(timePoint, timePoint + this.relativeDeadline, this.getWcet(), this.getId(), nextFreeId);
	}
	
	public ArrayList<Job> generateJobs(int fromTime, int toTime) {
		/*
		 * This function generates all of the jobs for a given task from fromTime to toTime
		 * Priorities are inherited from the tasks. The scheduling algorithm has to take care of the priorities itself!
		 */
		ArrayList<Job> taskInstances = new ArrayList<Job>();
		int jobId = 1;
		int time;
		
		for (time = this.startingTime; time + this.period < fromTime; time += this.period);

		while (time <= toTime) {
			Job taskInstance = new Job(time, time + relativeDeadline, this.wcet, this.id, jobId);
			taskInstance.setPrio(this.prio);
			taskInstances.add(taskInstance);
			
			time += period;			
			jobId++;
		}
		
		return taskInstances;
	}
	
}
