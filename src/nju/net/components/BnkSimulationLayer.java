package nju.net.components;

import java.util.ArrayList;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.locks.ReentrantLock;

import nju.AgentManager.AgentManager;
import nju.AgentManager.LayerEnum;
import nju.util.AgentsWorld;
import nju.util.UtilLock;


public class BnkSimulationLayer extends AgentsWorld implements Runnable{
	public static long INTERVAL = 0;
	//记录自己所处的层次
	private LayerEnum layer = null;
	double aerfa = 0;
	
	CyclicBarrier barrier = null;
	//构造函数
	public BnkSimulationLayer(LayerEnum layer,double[][] relationsArray, ArrayList<Integer> index,
			double aerfa,CyclicBarrier barrier){
		this.relationsArray = relationsArray;
		this.actionAgentIndex = index;
		this.layer = layer;
		this.aerfa = aerfa;
		this.barrier = barrier;
		this.init();
	}
	
	ArrayList<ActionAgent> bnkAgents = new ArrayList<>();
	ActionAgent[] agents = null;
	AgentRelations relations = AgentRelations.getInstance();
	
	//由外部传入的该层的网络关系结构
	double[][] relationsArray ;
	//由外部传入的该层的ActionAgent序列号
	ArrayList<Integer> actionAgentIndex = new ArrayList<>();
	
	public void clearAll(){
		actionAgentIndex.clear();
		bnkAgents.clear();
	}
	
	public ActionAgent[] getActionAgents(){
		return this.agents;
	}
	public LayerEnum getLayer(){
		return this.layer;
	}
	
	public static double[][] getEmptyRelations(int len){
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
//		int len = agents.length;
		//this.relationsArray = getEmptyRelations(len);
		
		this.relations.initRelations(relationsArray, agents);
		
		//System.out.println("  初始化第"+this.layer.getName()+"层关系网络完成");
	}

	@Override
	public void initAgents() {
		// TODO Auto-generated method stub 可以从文件读取也可以硬编码
		//this.aerfa = 0.5;//破产传染逆向影响系数，  0<=aerfa<=1
	
		agents = new ActionAgent[50]; 
		for(int i = 0; i < 50 ; i++){
			if(this.actionAgentIndex.contains(i)){
				agents[i] = new ActionAgent("action "+i, this.aerfa, this.layer, 
						AgentManager.getStatusAgent("status "+i),
						this.relations, bnkAgents);
			}else{
				agents[i] = null;
			}
		}
		//System.out.println("  初始化第"+this.layer.getName()+"层ActionAgent完成");
	}

	/**
	 * 时间周期方面的模拟可能不太准. 因为是先后执行的顺序，前面的agent传出的信息会被后面的agent在当前周期处理，而反之不然。
	 */
	@Override 
	public void startSimulation() {
		// TODO Auto-generated method stub
		
		int turnbefore_bankruptNums = 0;
		timestep = 0;
		//System.out.println("第"+this.layer.getName()+"层模拟，"+"第"+timestep+"周期");
		//System.out.println("第"+this.layer.getName()+"层模拟，"+"当前破产总数量"+AgentsWorld.bankruptNum);
		while(turnbefore_bankruptNums != AgentsWorld.bankruptNum){
			turnbefore_bankruptNums = AgentsWorld.bankruptNum;
			
			UtilLock.lock();
			if(UtilLock.getPower()==4){
				UtilLock.layerConWait();
			}
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
			for(int i = 0 ; i < agents.length ; i++){
				ActionAgent agent = agents[i];
				if(agent!=null){
					if(!agent.isBankruptcy())
						agent.thinking();
				}
			}
			
			
			UtilLock.autoIncrease_1();
			if(UtilLock.getPower()==4){
				UtilLock.controlConSignal();
			}
			UtilLock.unlock();
		}
		//System.out.println("第"+this.layer.getName()+"层模拟，"+"在"+timestep+"周期停止");
		//System.out.println("第"+this.layer.getName()+"层模拟，"+"最终破产总数量"+AgentsWorld.bankruptNum);
		//System.out.println("第"+this.layer.getName()+"层"+"模拟结束");
		
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		this.startSimulation();
		try {
			barrier.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
