package nju.AgentManager;

import java.util.HashMap;
import java.util.Set;

import nju.agent.components.*;
import nju.net.components.ActionAgent;
import nju.util.AgentsWorld;


public class StatusAgent {
	private String id;
	//存放该agent在分层中的各个分身，如果值为null表示在对应层次没有该agent的分身。
	//层次的id简单的表示为1,2,3...等数字
	private HashMap<String,ActionAgent> agentsInNet = new HashMap<>();
	
	public void clearAgents(){
		this.agentsInNet.clear();
	}
	private AssetsCom assetsCom;
	private BankruptDetectCom brdCom;
	
	private boolean isBankruptcy = false;//是否破产
	private HashMap<String,Component> register_map = new HashMap<String, Component>();
	/**
	 * 
	 * @param id，	agent的id作为识别依据
	 * @param u，	agent的破产阈值U	 
	 * @param c，	agent的初始资产C
	 * @param e，	agent周期回复的最小值ε
	 * @param k，	agent回复速率指标k
	 * 
	 *  U = 0.3
	 *  Aerfa = 0.5 
	 *  e=2
	 *  K=10
	 */
	public StatusAgent(){}
	public StatusAgent(String id, double u, double c, double e, double k){
		this.id = id;
		
		brdCom = new BankruptDetectCom(this, u);
		assetsCom = new AssetsCom(this, c, e, k);
		register_map.put(assetsCom.getClass().getName(), assetsCom);
		register_map.put(brdCom.getClass().getName(), brdCom);
		this.agentsInNet.put("1", null);
		this.agentsInNet.put("2", null);
		this.agentsInNet.put("3", null);
	}
	public void setAsset(double c){
		this.assetsCom.setC(c);
	}
	public void setU(double u){
		this.brdCom.setU(u);
	}
	//仅在模拟初始时调用，设置破产传染源；
	public void setBankruptcy(){
		isBankruptcy = true;
		AgentsWorld.bankruptNum++;
		Set<String> layerIDs = this.agentsInNet.keySet();
		for(String layerID: layerIDs){
			ActionAgent actionAgent = this.agentsInNet.get(layerID);
			if(actionAgent != null){
				actionAgent.setBankruptcy();
			}
		}
	}
	public String getID(){
		return this.id;
	}
	//获得组件
	public Component getComponent(String key){
		return register_map.get(key);
	}
	//判断是否破产
	public boolean isBankruptcy(){
		this.brdCom.execute();
		return isBankruptcy;
	}
	//根据层次的ID，获取本agent在三个层次中所对应的Agent
	public ActionAgent getActionAgent(String layerID){
		ActionAgent agent= this.agentsInNet.get(layerID);
		return agent;
	}
	public ActionAgent getActionAgent(LayerEnum layerID){
		return this.getActionAgent(layerID.getName());
	}
	//设置本agent在其他层次的对象引用
	public void setActionAgent(String layerID,ActionAgent agent){
		this.agentsInNet.put(layerID, agent);
	}
	public void setActionAgent(LayerEnum layerID,ActionAgent agent){
		this.setActionAgent(layerID.getName(),agent );
	}
	//接收传递过来需要减少的资产值，并在自己的资产中扣除这部分资产，扣除完毕，表示
	//状态发生改变，马上进行破产确认。
    public  void acceptDiff_c(double diff_c){
    	this.assetsCom.minusCash(diff_c);
    	if(!this.isBankruptcy()){
    		this.assetsCom.autoRecovery();
    	}
    }
}
