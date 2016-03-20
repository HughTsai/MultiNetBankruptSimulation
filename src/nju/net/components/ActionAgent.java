package nju.net.components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

import nju.AgentManager.LayerEnum;
import nju.AgentManager.StatusAgent;
import nju.agent.components.BankruptDetectCom;
import nju.agent.components.BankruptEventResponseCom;
import nju.agent.components.Component;
import nju.agent.msg.*;
import nju.util.AgentsWorld;

public class ActionAgent {
	private String id;
	//变量表示该agent所处的层次
	private LayerEnum layer = null;
	//变量指向该agent的本体对象
	private StatusAgent statusAgent = null;
	//该agent所处层次的关系图
	private AgentRelations relations = null;
	//用来记录当前周期破产的agent
	ArrayList<ActionAgent> bnkAgents = null;
	//agentsWorld
	AgentsWorld agentsWorld = null;
	private BankruptEventResponseCom beRespCom;
	
	private boolean isBankruptcy = false;//是否破产
	private Stack<AbstractMessage> msg_queue = new Stack<AbstractMessage>();
	private HashMap<String,Component> register_map = new HashMap<String, Component>();
	/**
	 * 
	 * @param id，	agent的id作为识别依据 
	 * @param aerfa	逆向影响的衰减系数α
	 * 
	 *  U = 0.3
	 *  Aerfa = 0.5 
	 *  e=2
	 *  K=10
	 */
	public ActionAgent(String id, double aerfa,LayerEnum layer,StatusAgent statusAgent,
			AgentRelations relations,ArrayList<ActionAgent> bnkAgents){
		this.id = id;
		this.layer = layer;
		this.statusAgent = statusAgent;
		this.relations = relations;
		this.bnkAgents = bnkAgents;
		
		beRespCom = new BankruptEventResponseCom(aerfa);
		register_map.put(beRespCom.getClass().getName(), beRespCom);
		
	}
	//仅在模拟初始时调用，设置破产传染源；
	public void setBankruptcy(){
		//记录
		if(this.isBankruptcy==false){
			this.isBankruptcy = true;
			//this.agentsWorld.bankruptNum++;
			//bankruptAction();
			this.bnkAgents.add(this);
		}
	}
	
	public String getID(){
		return this.id;
	}
	
	//给agent传递信息的接口
	public void informAgent(AbstractMessage message){
		msg_queue.push(message);
	}
	
	//自身破产后的行为——对周围agents 采取的动作。!!说白了就是消息传递
	public void bankruptAction(){
		//TODO  向周围的agent 发送破产信息。
		AgentRelations agentRelations = this.relations;
		
		//往下游传递影响; 顺向
		ActionAgent[] downstreamAgents = agentRelations.getDownstreamAgents(this.id);
		int d_len = downstreamAgents.length;
		for(int i = 0 ; i < d_len ; i++){
			ActionAgent agent = downstreamAgents[i];
			if(!agent.isBankruptcy()){
				double value = agentRelations.getValue(this.id, agent.getID());
				BankruptcyMessage msg = new BankruptcyMessage(this, value, false);
				agent.informAgent(msg);
			}
		}
		
		//往上游传递影响； 逆向
		ActionAgent[] upstreamAgents = agentRelations.getUpstreamAgents(this.id);
		int u_len = upstreamAgents.length;
		for(int i = 0 ; i < u_len ; i++){
			ActionAgent agent = upstreamAgents[i];
			if(!agent.isBankruptcy()){
				double value = agentRelations.getValue(agent.getID(), this.id);
				BankruptcyMessage msg = new BankruptcyMessage(this, value, true);
				agent.informAgent(msg);
			}
		}
		
	}
	
	
	//思考， 每个时间周期调用的agent 处理/思考的行为。
	public void thinking(){
		//“破产事件响应”组件 发挥作用；
		double diff_c = 0;
		while(!msg_queue.empty()){
			AbstractMessage message = msg_queue.remove(0);
			if(message instanceof BankruptcyMessage){
				diff_c += this.beRespCom.handle((BankruptcyMessage)message);
			}
		}
		this.statusAgent.acceptDiff_c(diff_c);
	}
	
	//获得组件
	public Component getComponent(String key){
		return register_map.get(key);
	}
	
	public boolean isBankruptcy(){
		return isBankruptcy;
	}

}
