package ds;

public class ResourceRequirement {
	public Resource r;
	public long reqTime;
	public long reqDuration;
	
	public ResourceRequirement() {
		this.r = new Resource();
		this.reqTime = 0;
		this.reqDuration = 0;
	}
	
	public ResourceRequirement(Resource r, long reqTime, long reqDuration) {
		this.r = r;
		this.reqTime = reqTime;
		this.reqDuration = reqDuration;
	}
	
	public String toString () {
		return ("( Resource Name: " + r.getName() + "\n Access Time: " + reqTime + "\n Access Duration: " + reqDuration + "\n)");
	}

}
