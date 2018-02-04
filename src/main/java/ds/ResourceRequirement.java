package ds;

public class ResourceRequirement {
	public Resource r;
	public int reqTime;
	public int reqDuration;
	
	public ResourceRequirement() {
		this.r = new Resource();
		this.reqTime = 0;
		this.reqDuration = 0;
	}
	
	public ResourceRequirement(Resource r, int reqTime, int reqDuration) {
		this.r = r;
		this.reqTime = reqTime;
		this.reqDuration = reqDuration;
	}
	
	public String toString () {
		return ("( Resource Name: " + r.getName() + "\n Access Time: " + reqTime + "\n Access Duration: " + reqDuration + "\n)");
	}

}
