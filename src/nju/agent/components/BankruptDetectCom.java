package nju.agent.components;

import nju.AgentManager.StatusAgent;

public class BankruptDetectCom implements Component{
	private double U;//破产阈值U
	private StatusAgent agent;
	
	
	public void setU(double u){
		this.U = u;
	}
	public BankruptDetectCom(StatusAgent agent, double u){
		this.agent = agent;
		this.U = u;
	}
	
	public void execute(){
		AssetsCom assetsCom = (AssetsCom) agent.getComponent(AssetsCom.class.getName());
		double cur_c = assetsCom.getCurrent_C();
		if(cur_c < U){
			agent.setBankruptcy();
		}
		//agent未破产则不做任何动作。
	}

}
