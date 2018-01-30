package demos.sched;

import algo.sched.Essence;
import java.math.BigDecimal;

public class generateRandomTask {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("Best utilization was " + Essence.generateRandomTask(1, new BigDecimal("1.000"), 5, 6).utilization());
		
		System.out.println("Best utilization was " + Essence.generateRandomTask(1, new BigDecimal("0.860"), 5, 15).utilization());
		
		System.out.println("Best utilization was " + Essence.generateRandomTask(1, new BigDecimal("0.869"), 5, 25).utilization());
	}

}
