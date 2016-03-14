package nju.net.components;

import java.util.ArrayList;

import nju.AgentManager.AgentManager;
import nju.AgentManager.LayerEnum;
import nju.util.AgentsWorld;


public class BnkSimulationLayer extends AgentsWorld implements Runnable{
	public static long INTERVAL = 0;
	//记录自己所处的层次
	private LayerEnum layer = null;
	
	public BnkSimulationLayer(LayerEnum layer){
		this.layer = layer;
		this.init();
	}
	
	ArrayList<ActionAgent> bnkAgents = new ArrayList<>();
	ActionAgent[] agents = null;
	AgentRelations relations = AgentRelations.getInstance();
	
	public ActionAgent[] getActionAgents(){
		return this.agents;
	}
	public LayerEnum getLayer(){
		return this.layer;
	}
	
	private double[][] getEmptyRelations(int len){
		double[][] relations = new double[len][len];
		for(int i = 0 ; i < len ; i++){
			for( int j = 0 ; j < len ; j++){
				relations[i][j] = 0;
			}
		}
		return  relations;
	}

	@Override
	public void initRelations() {
		// TODO Auto-generated method stub
		int len = agents.length;
		double[][] relations = getEmptyRelations(len);
		
		relations[0][7] = 50;
		relations[0][8] = 20;
		relations[0][9] = 30;
		relations[1][0] = 20;
		relations[1][5] = 15;
		relations[2][0] = 40;
		relations[3][0] = 50;
		relations[4][0] = 30;
		relations[4][8] = 10;
		relations[5][10] = 25;
		relations[7][12] = 30;
		relations[7][14] = 5;
		relations[8][14] = 5;
		relations[8][16] = 7;
		relations[8][17] = 6;
		relations[9][10] = 5;
		relations[9][12] = 5;
		relations[9][13] = 10;
		relations[10][30] = 15;
		relations[11][5] = 30;
		relations[12][15] = 18;
		relations[12][18] = 4;
		relations[13][27] = 4;
		relations[13][31] = 4;
		relations[14][12] = 3;
		relations[14][33] = 2;
		relations[15][35] = 10;
		relations[16][33] = 15;
		relations[18][35] = 20;
		relations[19][3] = 25;
		relations[19][4] = 25;
		relations[20][4] = 25;
		relations[20][16] = 13;
		relations[21][3] = 10;
		relations[22][3] = 30;
		relations[23][1] = 5;
		relations[23][2] = 50;
		relations[24][1] = 30;
		relations[24][11] = 10;
		relations[24][43] = 20;
		relations[25][6] = 5;
		relations[25][23] = 25;
		relations[26][10] = 10;
		relations[26][11] = 22;
		relations[27][31] = 30;
		relations[28][11] = 5;
		relations[28][26] = 35;
		relations[29][26] = 10;
		relations[32][27] = 20;
		relations[33][36] = 10;
		relations[33][37] = 5;
		relations[34][18] = 20;
		relations[35][36] = 30;
		relations[37][38] = 2;
		relations[39][20] = 10;
		relations[39][38] = 10;
		relations[40][19] = 30;
		relations[40][20] = 10;
		relations[41][19] = 30;
		relations[41][20] = 20;
		relations[41][22] = 20;
		relations[42][22] = 20;
		relations[44][29] = 10;
		relations[44][32] = 25;
		relations[45][6] = 30;
		relations[45][21] = 20;
		relations[45][42] = 40;
		relations[46][23] = 35;
		relations[46][24] = 25;
		relations[47][24] = 30;
		relations[48][24] = 20;
		relations[49][28] = 50;
		relations[49][29] = 50;
		relations[49][43] = 5;
		
		this.relations.initRelations(relations, agents);
		
		System.out.println("  初始化第"+this.layer.getName()+"层关系网络完成");
	}

	@Override
	public void initAgents() {
		// TODO Auto-generated method stub 可以从文件读取也可以硬编码
		double aerfa = 0.5;//破产传染逆向影响系数，  0<=aerfa<=1
	
		agents = new ActionAgent[50]; 
		for(int i = 0; i < 50 ; i++){
			agents[i] = new ActionAgent("action "+i, aerfa, this.layer, 
					AgentManager.getStatusAgent("status "+i),
					this.relations, bnkAgents, this);
		}
		System.out.println("  初始化第"+this.layer.getName()+"层ActionAgent完成");
	}

	/**
	 * 时间周期方面的模拟可能不太准. 因为是先后执行的顺序，前面的agent传出的信息会被后面的agent在当前周期处理，而反之不然。
	 */
	@Override 
	public void startSimulation() {
		// TODO Auto-generated method stub
		
		int turnbefore_bankruptNums = 0;
		timestep = 0;
		System.out.println("第"+this.layer.getName()+"层模拟，"+"第"+timestep+"周期");
		System.out.println("第"+this.layer.getName()+"层模拟，"+"当前破产数量"+this.bankruptNum);
		while(turnbefore_bankruptNums != this.bankruptNum){
			turnbefore_bankruptNums = this.bankruptNum;
			
			try {
				Thread.sleep(INTERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//破产消息传递
			int length = this.bnkAgents.size();
			for(int i = 0;i < length;i++){
				ActionAgent agent = this.bnkAgents.get(i);
				agent.bankruptAction();
			}
			bnkAgents.clear();
			//周期从这里开始，thinking是第一步，上面的破产消息传递是第二步。
			//agent进行思考，处理破产消息，判断自己是否破产
			timestep++;
			System.out.println("第"+this.layer.getName()+"层模拟，"+"第"+timestep+"周期");
			System.out.println("第"+this.layer.getName()+"层模拟，"+"当前破产数量"+this.bankruptNum);
			for(int i = 0 ; i < agents.length ; i++){
				ActionAgent agent = agents[i];
				if(!agent.isBankruptcy())
					agent.thinking();
			}
			
		}
		System.out.println("第"+this.layer.getName()+"层模拟，"+"在"+timestep+"周期停止");
		System.out.println("第"+this.layer.getName()+"层模拟，"+"最终破产数量"+this.bankruptNum);
		System.out.println("第"+this.layer.getName()+"层"+"模拟结束");
		
	}
	
	
	
//	public void simulate(){
//		int init_B_num = 1;
//		ExperimentData.clean();
//		try {
//			Logger.start();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		for(int k = 0 ; k < 50 ; k++){
//			final int counts = 500;
//			double[] incres = new double[counts];
//			double init_R_ratio = 0;
//			
//			for(int i = 0 ; i < counts ; i  ++){
//				
//				
//				Logger.log_startSimulation();
//				
//				initBankruptcySource(init_B_num);
//				
//				init_R_ratio = this.calBankruptRatio();
//				
//				startSimulation();
//				
//				Logger.log_endSimulation();
//				
//				double final_R_ratio = this.calBankruptRatio();
//				double incre_R_ratio = final_R_ratio - init_R_ratio;
//				
//				incres[i] = incre_R_ratio;
//			}
//			//we have 1 init_R_ratio and 10 incre_R_ratio now
//			double incre_avg = this.calAverage(incres);
//			SimulationResult line = new SimulationResult(init_R_ratio, incre_avg);
//			ExperimentData.addData(line);
//			
//			//初始破产agent数增加；
//			System.out.println(init_B_num++);
//		}
//		
//		Logger.stop();
//		//用图显示结果。
//		
//		ExperimentData.showData();
//		//plot(data);
//	}
	
	
	
	private double calBankruptRatio(){
		int len = agents.length;
		double ratio = ((double) this.bankruptNum ) / len;
		return ratio;
	}
	
	private double calAverage(double[] data){
		double temp = 0 ;
		int len = data.length;
		for(int i = 0 ; i < len ; i++){
			temp += data[i];
		}
		
		return temp/len;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.startSimulation();
	}

}
