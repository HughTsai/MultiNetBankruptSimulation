package experiment1;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import nju.AgentManager.AgentManager;
import nju.AgentManager.LayerEnum;
import nju.AgentManager.StatusAgent;
import nju.net.components.ActionAgent;
import nju.net.components.BnkSimulationLayer;
import nju.util.AgentsWorld;
import nju.util.UtilLock;

public class AgentsWorldSimple1{
	private double u = 0.3;// 破产阈值，u >0 , 
	private double e = 2;// 周期回复最小值
	private double k = 10;// 周期回复速率指标。k越大，回复越慢(周期回复的值越小）。
	private double aerfa = 0.5;//破产传染逆向影响系数，  0<=aerfa<=1
	
	private ExecutorService pool = Executors.newFixedThreadPool(3);
	//private CyclicBarrier barrier = new CyclicBarrier(3) ;
	private CyclicBarrier barrier = null ;

	public AgentsWorldSimple1(double u, double e, double k, double aerfa) {
		super();
		this.u = u;
		this.e = e;
		this.k = k;
		this.aerfa = aerfa;
	}

	private void init() {
		//AgentManager.clearAll();
		//第一步先初始化AgentManager中的所有statusAgent
		int statusAgentLength = 50;
		StatusAgent[] agents = new StatusAgent[statusAgentLength]; 
		
		int[] c = {20,30,30,30,2,15,5,25,15,30,
					30,5,8,20,2,25,5,30,20,
					15,20,30,20,15,15,11,5,3,7,
					20,15,33,20,8,7,12,30,10,8,
					8,7,6,30,15,10,28,8,10,8,7};
		
		for(int i = 0; i < 50 ; i++){
			agents[i] = new StatusAgent("status "+i, c[i]*u, c[i], e, k);
			//System.out.println("初始化StatusAgent "+i);
			AgentManager.setStatusAgent("status "+i, agents[i]);
		}
		
		//第二步，初始化每一个层次，并将层次存入AgentManager，归其管理
		int layerNumber = 3;
		//初始化各层的边
		double[][] relation1 = BnkSimulationLayer.getEmptyRelations(50);
		relation1[0][1] = 30;
		relation1[1][2] = 40;
		relation1[2][3] = 40;
		relation1[3][4] = 5;
		relation1[4][5] = 3;
		relation1[5][6] = 10;
		relation1[6][7] = 8;
		relation1[7][8] = 30;
		relation1[8][9] = 25;
		relation1[10][1] = 20;
		relation1[10][11] = 10;
		relation1[10][12] = 15;
		relation1[11][13] = 8;
		relation1[12][13] = 10;
		relation1[13][2] = 10;
		relation1[13][14] = 5;
		relation1[14][3] = 3;
		relation1[3][15] = 30;
		relation1[15][5] = 25;
		relation1[5][16] = 10;
		relation1[16][7] = 8;
		relation1[17][7] = 20;
		relation1[17][18] = 20;
		relation1[18][9] = 13;
		
		
		double[][] relation2 = BnkSimulationLayer.getEmptyRelations(50);
		relation2[10][20] = 50;
		relation2[19][13] = 20;
		relation2[20][13] = 33;
		relation2[13][21] = 40;
		relation2[21][15] = 35;
		relation2[22][21] = 17;
		relation2[23][15] = 22;
		relation2[15][24] = 38;
		relation2[24][16] = 10;
		relation2[24][25] = 10;
		relation2[24][26] = 10;
		relation2[16][27] = 8;
		relation2[27][25] = 5;
		relation2[25][28] = 12;
		relation2[26][29] = 7;
		relation2[28][18] = 9;
		relation2[29][18] = 5;
		relation2[18][30] = 12;
		
		double[][] relation3 = BnkSimulationLayer.getEmptyRelations(50);
		relation3[31][18] = 38;
		relation3[32][18] = 15;
		relation3[18][29] = 40;
		relation3[29][33] = 15;
		relation3[29][34] = 15;
		relation3[34][35] = 10;
		relation3[33][35] = 10;
		relation3[35][15] = 17;
		relation3[15][23] = 15;
		relation3[23][37] = 13;
		relation3[23][38] = 12;
		relation3[23][39] = 15;
		relation3[42][44] = 8;
		relation3[43][44] = 20;
		relation3[44][36] = 15;
		relation3[45][36] = 33;
		relation3[36][23] = 35;
		relation3[36][46] = 8;
		relation3[46][39] = 10;
		relation3[38][20] = 10;
		relation3[39][40] = 13;
		relation3[20][41] = 8;
		relation3[40][47] = 5;
		relation3[40][48] = 5;
		relation3[49][48] = 10;
		
		
		//初始化各层的action序号
		ArrayList<Integer> indexAction1 = new ArrayList<>();
		for(int i = 0;i<=18;i++)
			indexAction1.add(i);
		
		ArrayList<Integer> indexAction2 = new ArrayList<>();
		for(int i = 19;i<=30;i++)
			indexAction2.add(i);
		indexAction2.add(10);
		indexAction2.add(13);
		indexAction2.add(15);
		indexAction2.add(16);
		indexAction2.add(18);
		
		ArrayList<Integer> indexAction3 = new ArrayList<>();
		for(int i = 31;i<=49;i++)
			indexAction3.add(i);
		indexAction3.add(20);
		indexAction3.add(23);
		indexAction3.add(15);
		indexAction3.add(29);
		indexAction3.add(18);
		
		AgentManager.setLayerNumber(layerNumber);
		//实例化各个层
		LayerEnum layer = LayerEnum.valueOf("Layer1");
		//System.out.println("初始化第1"+"层中的数据：");
		BnkSimulationLayer bnk =new BnkSimulationLayer(layer,relation1,indexAction1,aerfa,barrier);
		AgentManager.setLayer(layer, bnk);
		
		layer = LayerEnum.valueOf("Layer2");
		//System.out.println("初始化第2"+"层中的数据：");
		bnk =new BnkSimulationLayer(layer,relation2,indexAction2,aerfa,barrier);
		AgentManager.setLayer(layer, bnk);
		
		layer = LayerEnum.valueOf("Layer3");
		//System.out.println("初始化第3"+"层中的数据：");
		bnk =new BnkSimulationLayer(layer,relation3,indexAction3,aerfa,barrier);
		AgentManager.setLayer(layer, bnk);
		
		//第三步，将StatusAgent与每一层对应的分身绑定好
		
		BnkSimulationLayer bnkLayer1 = AgentManager.getlayer(LayerEnum.Layer1);
		BnkSimulationLayer bnkLayer2 = AgentManager.getlayer(LayerEnum.Layer2);
		BnkSimulationLayer bnkLayer3 = AgentManager.getlayer(LayerEnum.Layer3);
		ActionAgent[] actionAgents1 = bnkLayer1.getActionAgents();
		ActionAgent[] actionAgents2 = bnkLayer2.getActionAgents();
		ActionAgent[] actionAgents3 = bnkLayer3.getActionAgents();
		
		StatusAgent statusAgent;
		for(int i = 0;i<=18;i++){
			statusAgent = AgentManager.getStatusAgent("status "+i);
			statusAgent.setActionAgent(LayerEnum.Layer1, actionAgents1[i]);
		}
		for(int i = 19;i<=30;i++){
			statusAgent = AgentManager.getStatusAgent("status "+i);
			statusAgent.setActionAgent(LayerEnum.Layer2, actionAgents2[i]);
		}
		for(int i = 31;i<=49;i++){
			statusAgent = AgentManager.getStatusAgent("status "+i);
			statusAgent.setActionAgent(LayerEnum.Layer3, actionAgents3[i]);
		}
		statusAgent = AgentManager.getStatusAgent("status 10");
		statusAgent.setActionAgent(LayerEnum.Layer2, actionAgents2[10]);
		
		statusAgent = AgentManager.getStatusAgent("status 13");
		statusAgent.setActionAgent(LayerEnum.Layer2, actionAgents2[13]);
		
		statusAgent = AgentManager.getStatusAgent("status 16");
		statusAgent.setActionAgent(LayerEnum.Layer2, actionAgents2[16]);
		
		statusAgent = AgentManager.getStatusAgent("status 20");
		statusAgent.setActionAgent(LayerEnum.Layer3, actionAgents3[20]);
		
		statusAgent = AgentManager.getStatusAgent("status 23");
		statusAgent.setActionAgent(LayerEnum.Layer3, actionAgents3[23]);
		
		statusAgent = AgentManager.getStatusAgent("status 29");
		statusAgent.setActionAgent(LayerEnum.Layer3, actionAgents3[29]);
		
		statusAgent = AgentManager.getStatusAgent("status 15");
		statusAgent.setActionAgent(LayerEnum.Layer2, actionAgents2[15]);
		statusAgent.setActionAgent(LayerEnum.Layer3, actionAgents3[15]);
		
		statusAgent = AgentManager.getStatusAgent("status 18");
		statusAgent.setActionAgent(LayerEnum.Layer2, actionAgents2[18]);
		statusAgent.setActionAgent(LayerEnum.Layer3, actionAgents3[18]);
		
		
//		//第四步，设置初始破产的agent数量
//		int bnkAgentNumber = 5;
//		AgentManager.initBankruptcySource(bnkAgentNumber);
//		System.out.println(bnkAgentNumber+"个初始破产结点设置完成");
//		
//		
//		//第五步，每一层都开始模拟
//		for(int i = 0;i<layerNumber;i++){
//			layer = LayerEnum.valueOf("Layer"+(i+1));
//			BnkSimulationLayer bnkLayer = AgentManager.getlayer(layer);
//			
//			Thread thread = new Thread(bnkLayer);
//			System.out.println("开始第"+(i+1)+"层网络的模拟");
//			thread.start();
//		}
		
		//System.out.println("整个实验模拟结束");
	}
	
	public void simulate(){
		int init_B_num = 1;
		ExperimentData.clean();
		
		for(int k = 0 ; k < 50 ; k++){
			final int counts = 500;
			double[] incres = new double[counts];
			double init_R_ratio = 0;
			System.out.println("初始破产点数："+init_B_num+"=============");
			for(int i = 0 ; i < counts ; i  ++){
				//初始化层结构，包括各个agent与边
				System.out.println("初始破产点数："+init_B_num+"第"+(i+1)+"模拟");
				this.init();
				
				//初始化最初的破产agent数量
				AgentManager.initBankruptcySource(init_B_num);
				
				init_R_ratio = this.calBankruptRatio();
				
				startSimulation();
				
				while(true){
					if(UtilLock.getLayerDoneCounter()==3){
						double final_R_ratio = this.calBankruptRatio();
						double incre_R_ratio = final_R_ratio - init_R_ratio;
						
						incres[i] = incre_R_ratio;
						UtilLock.resetLayerDoneCounter();
						break;
					}
				}
				
			}
			//we have 1 init_R_ratio and 10 incre_R_ratio now
			double incre_avg = this.calAverage(incres);
			SimulationResult line = new SimulationResult(init_R_ratio, incre_avg);
			ExperimentData.addData(line);
			
			//初始破产agent数增加；
			System.out.println(init_B_num++);
			//System.gc();
		}
		
		ExperimentData.showData();
	}
	
	
	
	private void startSimulation() {
		for(int i = 0;i<3;i++){
			LayerEnum layer = LayerEnum.valueOf("Layer"+(i+1));
			BnkSimulationLayer bnkLayer = AgentManager.getlayer(layer);
			
			pool.execute(bnkLayer);
		}
	}



	private double calBankruptRatio(){
		int len = 50;
		double ratio = ((double) AgentsWorld.bankruptNum ) / len;
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

}
