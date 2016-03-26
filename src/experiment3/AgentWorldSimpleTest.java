package experiment3;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import experiment2.PointResult;
import nju.AgentManager.AgentManager;
import nju.AgentManager.LayerEnum;
import nju.AgentManager.StatusAgent;
import nju.net.components.ActionAgent;
import nju.net.components.BnkSimulationLayer;
import nju.util.AgentsWorld;
import nju.util.OutboundException;
import nju.util.UtilLock;

public class AgentWorldSimpleTest {
	public static long INTERVAL = 0;
	//该次实验中，asset上限，下限，增量
	private double assetUpper;
	private double assetLower;
	private double assetIncrement;
	//存放该次实验的所有结果
	private ExperimentData results =null;
	AssetsRstData assetsRstData = null;
	//ArrayList<AssetsRstData> temp = new ArrayList<>();
	//指定的agent序号
	int appointedAgent = -1;
	
	private ExecutorService pool = Executors.newFixedThreadPool(3);
	//private CyclicBarrier barrier =  new CyclicBarrier(3) ;
	private CyclicBarrier barrier = null;
	
	
	double u ;// 破产阈值，u >0 , 
	double e ;// 周期回复最小值
	double k ;// 周期回复速率指标。k越大，回复越慢(周期回复的值越小）。
	double aerfa ;//破产传染逆向影响系数，  0<=aerfa<=1
	
	public AgentWorldSimpleTest(double assetUpper, double assetLower,
			double assetIncrement, int appointedAgent) {
		super();
		this.assetUpper = assetUpper;
		this.assetLower = assetLower;
		this.assetIncrement = assetIncrement;
		this.appointedAgent = appointedAgent;
	}
	public void init(){
		//this.barrier = new CyclicBarrier(3,new DealWithResult(temp, appointedAgent));
		AgentManager.clearAll();
		//第一步先初始化AgentManager中的所有statusAgent
		int statusAgentLength = 50;
		StatusAgent[] agents = new StatusAgent[statusAgentLength]; 
		u = 0.3;// 破产阈值，u >0 , 
		e = 2;// 周期回复最小值
		k = 10;// 周期回复速率指标。k越大，回复越慢(周期回复的值越小）。
		aerfa = 0.5;//破产传染逆向影响系数，  0<=aerfa<=1
		
		int[] c = {20,30,30,30,2,15,5,20,15,30,
					30,5,8,13,2,13,5,30,8,
					15,5,30,20,15,15,9,5,3,7,
					3,15,33,18,8,7,12,30,10,8,
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
		relation2[22][23] = 30;
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
		relation3[32][18] = 20;
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
		relation3[42][43] = 25;
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
		BnkSimulationLayer bnk =new BnkSimulationLayer(layer,relation1,indexAction1,aerfa,
				this.barrier);
		AgentManager.setLayer(layer, bnk);
		
		layer = LayerEnum.valueOf("Layer2");
		//System.out.println("初始化第2"+"层中的数据：");
		bnk =new BnkSimulationLayer(layer,relation2,indexAction2,aerfa,
				this.barrier);
		AgentManager.setLayer(layer, bnk);
		
		layer = LayerEnum.valueOf("Layer3");
		//System.out.println("初始化第3"+"层中的数据：");
		bnk =new BnkSimulationLayer(layer,relation3,indexAction3,aerfa,
				this.barrier);
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
		
	}
	
	/**
	 * 破产传递的“一次”模拟（只适用于实验四）
	 * @param num 该次模拟的序号
	 * @param currentAlpha 该次模拟的α值
	 */
	private void startSimulation(int num ,double currentAsset) {
		// TODO 自动生成的方法存根
		int turnbefore_bankruptNums = 0;
		//int timestep = 0;
		
		for(int i = 0;i<3;i++){
			LayerEnum layer = LayerEnum.valueOf("Layer"+(i+1));
			BnkSimulationLayer bnkLayer = AgentManager.getlayer(layer);
			pool.execute(bnkLayer);
		}
		while(true){
			if(UtilLock.getLayerDoneCounter()==3){
				//UtilLock.lock();
				System.out.println("数据统计开始");
				for(int i = 0;i<3;i++){
					LayerEnum layer = LayerEnum.valueOf("Layer"+(i+1));
					ActionAgent agent= AgentManager.getStatusAgent("status "+this.appointedAgent).getActionAgent(layer);
					if(agent!=null){
						if(agent.isBankruptcy()){
							this.assetsRstData.addBnkNum(1);
							break;
						}
					}
				}
				System.out.println("数据统计结束");
				//UtilLock.unlock();
				UtilLock.resetLayerDoneCounter();
				break;
			}
		}
		System.out.println("当前c值为："+currentAsset+":第"+(num+1)+"次模拟结束");
	}
	/**
	 * 实验模拟
	 * @param simulateNum 每个α值需要模拟的次数
	 * @param initBankruptNumP 设置初始破产结点数量
	 */
	public void simulate(int initBankruptNumP ,int simulateNum){
		int initBankruptNum = initBankruptNumP;
		this.results = new ExperimentData(simulateNum);
		results.setAppointedAgent(this.appointedAgent);
		
		results.clean();//实验前结果集清空

		
		//让初始资产从最小值一直增加到最大值
		for(double c = this.assetLower;c <= this.assetUpper;c += this.assetIncrement){
			//c = this.round(c, 2);
			//this.temp.clear();
			this.assetsRstData = new AssetsRstData(c);
			//this.temp.add(assetsRstData);
			//一个c值，进行多次模拟
			for(int i = 0;i < simulateNum;i++){
				this.init();
				AgentManager.getStatusAgent("status "+this.appointedAgent).setAsset(c);
				AgentManager.getStatusAgent("status "+this.appointedAgent).setU(this.u*c);
				if(initBankruptNum > 50)
					break;
				
				initBankruptcySource(initBankruptNum,this.appointedAgent);//不会挑选到指定的结点
				
				startSimulation(i, c);
				
				
			}
			results.addData(assetsRstData);
		}
		
		//用图显示结果。
		results.showData();
	}
	
	/**
	 * 设置初始破产结点的数量
	 * @param n 初始破产结点的数量
	 * @param appointedAgent 需要初始破产的agent序列
	 */
	public void initBankruptcySource(final int n,int appointedAgent){
		int len = 50;
		if(n > len){
			throw new OutboundException();
		}
		
		if(n < len/2){
			ArrayList<Integer> indexes = null;
			while(true){
				indexes = AgentManager.selectDistinctNums(n, len);//indexes存放要破产的初始agent序列
				if(!indexes.contains(appointedAgent)){
					break;
				}
			}
			if(indexes == null)
				return;
			for(int i = 0; i < indexes.size() ;  i++){
				int index = indexes.get(i);
				AgentManager.getStatusAgent("status "+index).setBankruptcy();
			}
		}else{
			int exclude_nums = len - n;
			ArrayList<Integer> list = null;
			while(true){
				list = AgentManager.selectDistinctNums(exclude_nums, len);//存放不要破产的初始agent序列
				if(list.contains(this.appointedAgent)){
					break;
				}
			}
			for(int i = 0 ; i < len ; i++){
				if(!list.contains(i)){
					AgentManager.getStatusAgent("status "+i).setBankruptcy();
				}
			}
		}
		
		AgentsWorld.bankruptNum = n;//与设置多少个破产传染源有关
	}
	/**
	 * 用于将小数平滑至最接近它本身的数，控制double类型的精度
	 * @param source 传入α的值
	 * @param newValue 保留的精度
	 * @return 平滑后的结果
	 */
    private double round(double source,int newValue){
    	DecimalFormat formater = new DecimalFormat();
    	formater.setMaximumFractionDigits(newValue);
    	formater.setRoundingMode(RoundingMode.HALF_DOWN);
    	String result = formater.format(source);
    	
    	return Double.parseDouble(result);
    }
}

class DealWithResult implements Runnable{
	ArrayList<AssetsRstData> temp = null;
	AssetsRstData assetsRstData = null;
	int appointedAgent = -1;
	public DealWithResult(ArrayList<AssetsRstData> temp,int appoint){
		this.temp = temp;
		this.appointedAgent = appoint;
	}
	public void run() {
		//如果模拟结束指定结点破产，则对assetsRstData+1；
		for(int i1 = 0;i1<3;i1++){
			LayerEnum layer = LayerEnum.valueOf("Layer"+(i1+1));
			ActionAgent agent= AgentManager.getStatusAgent("status "+this.appointedAgent).getActionAgent(layer);
			if(agent.isBankruptcy()){
				temp.get(0).addBnkNum(1);
				break;
			}
		}
//		UtilLock2.lock();
//		UtilLock2.ConSignal();
//		UtilLock2.unlock();
	}
}
