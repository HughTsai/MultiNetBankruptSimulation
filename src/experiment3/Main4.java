package experiment3;


public class Main4 {

	private static final double ASSETUPPER = 15;//C值的上限
	private static final double ASSETLOWER = 10;//C值的下限
	private static final double ASSETINCREMENT = 0.01;//C值的增量
	private static final int APPOINTEDAGENT = 25;//指定的agent
	
	private final static int SIMULATION_TIMES = 1000;//每个α值模拟次数
	private final static int INIT_BANKRUPT_NUMBER = 1;//初始破产结点数量
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO 自动生成的方法存根
		AgentWorldSimpleTest agentWorld = new AgentWorldSimpleTest(ASSETUPPER, ASSETLOWER, ASSETINCREMENT, APPOINTEDAGENT);
		agentWorld.simulate(INIT_BANKRUPT_NUMBER, SIMULATION_TIMES);
	}

}
