package ds;

public abstract class Task {
	private long wcet;
	private long startingTime;
	private long id;
	private long prio;
	private String name;

	public Task() {
		this.wcet = 0;
		this.startingTime = 0;
		this.id = 0;
		this.prio = 0;
		this.name = "Default Name";
	}

	public Task(String taskName, long uWcet, long sTime, long id) {
		this.name = taskName;
		this.wcet = uWcet;
		this.startingTime = sTime;
		this.id = id;
	}
	
	public Task(String taskName, long uWcet, long sTime, long id, long priority) {
		this.name = taskName;
		this.wcet = uWcet;
		this.startingTime = sTime;
		this.id = id;
		this.prio = priority;
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		
		sb.append("Task: " + this.getName() + "\n");
		sb.append("id: " + this.getId() + "\n");
		sb.append("Starting Time: " + this.getStartingTime() + "\n");
		sb.append("WCET: " + this.getWcet() + "\n");
		sb.append("Priority: " + this.getPrio() + "\n");
		
		return sb.toString();
		
	}
	
	public long getWcet() {
		return wcet;
	}

	public void setWcet(long wcet) {
		this.wcet = wcet;
	}
	
	public long getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(long sTime) {
		this.startingTime = sTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getPrio() {
		return prio;
	}

	public void setPrio(long prio) {
		this.prio = prio;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
