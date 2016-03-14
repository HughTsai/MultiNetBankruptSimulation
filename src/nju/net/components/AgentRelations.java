package nju.net.components;

import java.util.ArrayList;
import java.util.HashMap;

public class AgentRelations {
	private double[][] graphs;//这里的下标对应agent在agents列表里的index.
	private ActionAgent[] agents;//agents列表
	
	//记录agent id与index之间的对应关系
	private HashMap<String, Integer> id_table = new HashMap<String, Integer>();
	
	private AgentRelations(){
		id_table.clear();
	}
	
	public static AgentRelations getInstance(){
		return new AgentRelations();
	}
	
	/**
	 * 记录agent列表及其之间的关系，类功能做准备。
	 * @param agentsRelations
	 * @param agents
	 */
	public void initRelations(double[][] agentsRelations, ActionAgent[] agents){
		this.graphs = agentsRelations;
		
		for(int i = 0 ; i < agents.length ; i++){
			ActionAgent agent = agents[i];
			id_table.put(agent.getID(), i);
		}
		
		this.agents = agents;
	}
	
	/**
	 * 获取指定agent的上层结点，并将结果以数组返回
	 * @param index
	 * @return
	 */
	public ActionAgent[] getUpstreamAgents(int index){
		int len = agents.length;
		ArrayList<ActionAgent> list = new ArrayList<ActionAgent>();
		for(int i = 0 ; i < len ; i++){
			if(graphs[i][index] > 0){
				list.add(agents[i]);
			}
		}
		
		ActionAgent[] results = new ActionAgent[list.size()];
		list.toArray(results);
		
		return results;
	}
	
	public ActionAgent[] getUpstreamAgents(String agent_id){
		int agent_index = id_table.get(agent_id);
		return getUpstreamAgents(agent_index);
	}
	/**
	 * 获取指定agent的下层结点，并将结果以数组返回
	 * @param index
	 * @return
	 */
	public ActionAgent[] getDownstreamAgents(int index){
		int len = agents.length;
		ArrayList<ActionAgent> list = new ArrayList<ActionAgent>();
		
		for(int i = 0 ; i < len ; i++){
			if(graphs[index][i] > 0){
				list.add(agents[i]);
			}
		}
		
		ActionAgent[] results = new ActionAgent[list.size()];
		list.toArray(results);
		
		return results;
	}
	
	public ActionAgent[] getDownstreamAgents(String agent_id){
		int agent_index = id_table.get(agent_id);
		return getDownstreamAgents(agent_index);
	}
	
	/**
	 * 获取边上的权值
	 * @param i 边的入点
	 * @param j 边的出点
	 * @return
	 */
	public double getValue(int i , int j){
		return graphs[i][j];
	}
	
	public double getValue(String i_id, String j_id){
		int index_i = id_table.get(i_id);
		int index_j = id_table.get(j_id);
		
		return graphs[index_i][index_j];
	}

}
