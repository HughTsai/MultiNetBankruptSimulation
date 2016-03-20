package experiment1;

public class Main {

	/**
	 * @param args
	 */
	private static double u = 0.3;// 破产阈值，u >0 , 
	private static double e = 2;// 周期回复最小值
	private static double k = 10;// 周期回复速率指标。k越大，回复越慢(周期回复的值越小）。
	private static double aerfa = 0.5;
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		AgentsWorldSimple1 simpleTest = new AgentsWorldSimple1(u, e, k, aerfa);
		simpleTest.simulate();
	}

}
