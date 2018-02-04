package ds;

public abstract class Task {
	protected int wcet;
	protected int startingTime;
	protected int id;
	protected int prio;
	protected String name;

	public Task() {
		this.wcet = 0;
		this.startingTime = 0;
		this.id = 0;
		this.prio = 0;
		this.name = "Default Name";
	}

	public Task(String taskName, int uWcet, int sTime, int id) {
		this.name = taskName;
		this.wcet = uWcet;
		this.startingTime = sTime;
		this.id = id;
	}
	
	public Task(String taskName, int uWcet, int sTime, int id, int priority) {
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
	
	public int getWcet() {
		return wcet;
	}

	public void setWcet(int wcet) {
		this.wcet = wcet;
	}
	
	public int getStartingTime() {
		return startingTime;
	}

	public void setStartingTime(int sTime) {
		this.startingTime = sTime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getPrio() {
		return prio;
	}

	public void setPrio(int prio) {
		this.prio = prio;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
