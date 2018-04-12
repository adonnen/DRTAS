package ds;

import java.util.Optional;

public class Resource {
	private String name;
	private R_STATE state;
	private Optional<Integer> priorityCeiling;


	Resource() {
		this.name = "Default Name";
		this.state = R_STATE.FREE;
	}
	
	Resource (String name, int id) {
		this.name = name;
		this.state = R_STATE.FREE;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public R_STATE getState() {
		return state;
	}

	public void setState(R_STATE state) {
		this.state = state;
	}
	
	public Optional<Integer> getPriorityCeiling() {
		return priorityCeiling;
	}

	public void setPriorityCeiling(int ceiling) {
		this.priorityCeiling = Optional.ofNullable(ceiling);
	}
	
	
	@Override
	public String toString() {
		return this.name + " : " + state + ((priorityCeiling.isPresent()) ? priorityCeiling.get() : "");
	}

}
