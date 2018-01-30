package ds;

import java.util.Optional;

public class Resource {
	private String name;
	private R_STATE state;
	private Optional<Long> priorityCeiling;


	Resource() {
		this.name = "Default Name";
		this.state = R_STATE.FREE;
	}
	
	Resource (String name, long id) {
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
	
	public Optional<Long> getPriorityCeiling() {
		return priorityCeiling;
	}

	public void setPriorityCeiling(Long priorityCeiling) {
		this.priorityCeiling = Optional.ofNullable(priorityCeiling);
	}
	
	
	@Override
	public String toString() {
		return this.name + " : " + state + ((priorityCeiling.isPresent()) ? priorityCeiling.get() : "");
	}

}
