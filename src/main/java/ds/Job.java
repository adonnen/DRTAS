package ds;

import java.util.ArrayList;
import java.util.Optional;

public class Job {
	private int releaseTime;
	private int absoluteDeadline;
	private int executionTime;
	private int prio;
	private int taskId;
	private int instanceId;
	private STATE state;
	private Optional<ArrayList<ResourceRequirement>> requirementInstances;


	public Job(){
		this.releaseTime = 0;
		this.absoluteDeadline = 0;
		this.executionTime = 0;
		this.prio = 0;
		this.taskId = 0;
		this.instanceId = 0;
		this.state = STATE.INACTIVE;
		this.requirementInstances = Optional.empty();
	}
	
	public Job(int rTime, int aDeadline, int eTime, int id, int iid){
		this.releaseTime = rTime;
		this.absoluteDeadline = aDeadline;
		this.executionTime = eTime;
		this.taskId = id;
		this.instanceId = iid;
		this.state = STATE.INACTIVE;
		this.requirementInstances = Optional.empty();
	}
	
	public Job(int rTime, int aDeadline, int eTime, int id, int iid, ArrayList<ResourceRequirement> rr){
		this.releaseTime = rTime;
		this.absoluteDeadline = aDeadline;
		this.executionTime = eTime;
		this.taskId = id;
		this.instanceId = iid;
		this.state = STATE.INACTIVE;
		this.requirementInstances = Optional.ofNullable(rr);
	}

	public int getPrio() {
		return this.prio;
	}

	public void setPrio(int prio) {
		this.prio = prio;
	}
	
	public int getReleaseTime() {
		return releaseTime;
	}

	public void setReleaseTime(int releaseTime) {
		this.releaseTime = releaseTime;
	}

	public int getAbsoluteDeadline() {
		return absoluteDeadline;
	}

	public void setAbsoluteDeadline(int absoluteDeadline) {
		this.absoluteDeadline = absoluteDeadline;
	}

	public int getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(int executionTime) {
		this.executionTime = executionTime;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getInstanceId() {
		return instanceId;
	}

	public void setInstanceId(int instanceId) {
		this.instanceId = instanceId;
	}
	
	public STATE getState() {
		return state;
	}

	public void setState(STATE state) {
		this.state = state;
	}
	
	public void addResourceRequirement(ResourceRequirement newReq) {
		if (this.requirementInstances.isPresent()) {
			this.requirementInstances.get().add(newReq);
		} else {
			this.requirementInstances = Optional.of(new ArrayList<ResourceRequirement>());
			this.requirementInstances.get().add(newReq);
		}
	}
	
	public void removeResourceRequirement(String name) {
		if (this.requirementInstances.isPresent())
			for (int i = 0; i < this.requirementInstances.get().size(); ++i)
				if (this.requirementInstances.get().get(i).r.getName().equals(name)) this.requirementInstances.get().remove(i);
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (absoluteDeadline ^ (absoluteDeadline >>> 32));
		result = prime * result + (int) (executionTime ^ (executionTime >>> 32));
		result = prime * result + (int) (instanceId ^ (instanceId >>> 32));
		result = prime * result + (int) (prio ^ (prio >>> 32));
		result = prime * result + (int) (releaseTime ^ (releaseTime >>> 32));
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + (int) (taskId ^ (taskId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Job other = (Job) obj;
		if (absoluteDeadline != other.absoluteDeadline)
			return false;
		if (executionTime != other.executionTime)
			return false;
		if (instanceId != other.instanceId)
			return false;
		if (prio != other.prio)
			return false;
		if (releaseTime != other.releaseTime)
			return false;
		if (state != other.state)
			return false;
		if (taskId != other.taskId)
			return false;
		return true;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Job " + instanceId + " of task " + taskId + "\n");
		sb.append("Release Time: " + this.releaseTime + "\n");
		sb.append("Execution Time: " + this.executionTime + "\n");
		sb.append("Absolute Deadline: " + this.absoluteDeadline + "\n");
		sb.append("Priority: " + this.prio + "\n");
		sb.append("Current State: " + state + "\n");
		
		if (this.requirementInstances.isPresent())
			for (ResourceRequirement rr : this.requirementInstances.get())
				sb.append(rr.toString());
		
		return sb.toString();
	}
	
}
