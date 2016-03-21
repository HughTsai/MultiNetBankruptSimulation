package nju.AgentManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import nju.net.components.ActionAgent;
import nju.net.components.BnkSimulationLayer;
import nju.util.OutboundException;


public class AgentManager {
	//agent管理者所管理的所有agent，使用agent的id作为key。
	private static HashMap<String,StatusAgent> agents = new HashMap<>();
	//agent需要分成多少个层次
	private static int layerNumber = 0;
	//记录所有层次的引用
	private static HashMap<LayerEnum,BnkSimulationLayer> layers = new HashMap<>();
	
	
	
	public static int getLayerNumber() {
		return layerNumber;
	}

	public static void setLayerNumber(int layerNumber) {
		AgentManager.layerNumber = layerNumber;
	}

	public static HashMap<LayerEnum, BnkSimulationLayer> getLayers() {
		return layers;
	}

	public static void setLayers(HashMap<LayerEnum, BnkSimulationLayer> layers) {
		AgentManager.layers = layers;
	}

	public static void setLayer(LayerEnum layer,BnkSimulationLayer LObject){
		AgentManager.layers.put(layer, LObject);
	}
	
	public static BnkSimulationLayer getlayer(LayerEnum layer){
		return AgentManager.layers.get(layer);
	}
	
	//id的形式是“status xx”
	public static void setStatusAgent(String ID,StatusAgent statusAgent){
		agents.put(ID, statusAgent);
	}
	
	public static StatusAgent getStatusAgent(String statusAgentID){
		Set<String> IDs = agents.keySet();
		if(!IDs.contains(statusAgentID)){
			System.out.println("输入的StatusAgentID非法");
			return null;
		}
		return agents.get(statusAgentID);
	}
	
	/**
	 * 随机选择初始破产结点
	 * @param n 初始破产结点的数量
	 * @param len agent的总数量
	 * @return 返回被选择破产的结点list
	 */
	public static ArrayList<Integer> selectDistinctNums(final int n, final int len){
		if(n == 0)
			return null;
		if(n == len){
			ArrayList<Integer> temp = new ArrayList<Integer>();
			for(int i = 0 ; i < len; i ++){
				temp.add(i);
			}
			
			return temp;
		}
		
		int count = n;
		ArrayList<Integer> list = new ArrayList<Integer>();
		while(count > 0){
			//初始化破产节点
			int d = (int) Math.floor( Math.random() * len );
			if(d == len)
				d = len-1;
			
			if(!list.contains(d)){
				list.add(d);
				count--;
			}
		}
		
		
		return list;
	}
	
	/**
	 * 设置初始破产结点的数量
	 * @param n 初始破产结点的数量
	 */
	public static void initBankruptcySource(final int n){
		int len = agents.keySet().size();
		if(n > len){
			throw new OutboundException();
		}
		
		if(n < len/2){
			ArrayList<Integer> indexes = AgentManager.selectDistinctNums(n, len);
			if(indexes == null)
				return;
			for(int i = 0; i < indexes.size() ;  i++){
				int index = indexes.get(i);
				agents.get("status "+index).setBankruptcy();
			}
		}else if(n < len){
			int exclude_nums = len - n;
			ArrayList<Integer> list = AgentManager.selectDistinctNums(exclude_nums, len);
			for(int i = 0 ; i < len ; i++){
				if(!list.contains(i)){
					agents.get("status "+i).setBankruptcy();
				}
			}
		}else{
			for(int i = 0 ; i < len ; i++){
				agents.get("status "+i).setBankruptcy();
			}
		}
	}
	public static void clearAll(){
		Iterator<String> itr = agents.keySet().iterator();
		while(itr.hasNext()){
			StatusAgent agent = agents.get(itr.next());
			agent.clearAgents();
		}
		agents.clear();
		
		Iterator<LayerEnum> itr2 = layers.keySet().iterator();
		while(itr.hasNext()){
			LayerEnum layer = itr2.next();
			BnkSimulationLayer bnk = layers.get(layer);
			ActionAgent[] agents = bnk.getActionAgents();
			for(int i = 0;i<agents.length;i++){
				agents[i].clearStatusAgent();
			}
			bnk.clearAll();
		}
		layers.clear();
	}
	
}
