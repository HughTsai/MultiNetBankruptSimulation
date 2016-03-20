package experiment2;

public class Main {
	
	private final static int SIMULATION_TIMES = 1000;
	private final static int INIT_BANKRUPT_NUMBER = 1;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		AgentWorldSimple2 agentWorld = new AgentWorldSimple2(SIMULATION_TIMES, 50);
		agentWorld.simulate(INIT_BANKRUPT_NUMBER);
	}

}
