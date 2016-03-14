package nju.test;

import nju.AgentManager.AgentManager;
import nju.AgentManager.LayerEnum;
import nju.AgentManager.StatusAgent;
import nju.net.components.ActionAgent;
import nju.net.components.BnkSimulationLayer;

public class TestModel {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		//第一步先初始化AgentManager中的所有statusAgent
		int statusAgentLength = 50;
		StatusAgent[] agents = new StatusAgent[statusAgentLength]; 
		double u = 0.7;// 破产阈值，u >0 , 
		double e = 2;// 周期回复最小值
		double k = 10;// 周期回复速率指标。k越大，回复越慢(周期回复的值越小）。
		
		int[] c = {100,15,30,45,35,20,30,28,25,20,
					25,28,20,5,5,10,15,5,20,45,
					35,5,30,50,60,25,40,25,35,5,
					10,25,20,10,19,25,35,2,10,15,
					35,50,15,20,25,100,50,25,18,50};
		
		for(int i = 0; i < 50 ; i++){
			agents[i] = new StatusAgent("status "+i, c[i]*u, c[i], e, k);
			System.out.println("初始化StatusAgent "+i);
			AgentManager.setStatusAgent("status "+i, agents[i]);
		}
		
		//第二步，初始化每一个层次，并将层次存入AgentManager，归其管理
		int layerNumber = 3;
		AgentManager.setLayerNumber(layerNumber);
		for(int i=0;i<layerNumber;i++){
			LayerEnum layer = LayerEnum.valueOf("Layer"+(i+1));
			System.out.println("初始化第"+(i+1)+"层中的数据：");
			BnkSimulationLayer bnk =new BnkSimulationLayer(layer);
			AgentManager.setLayer(layer, bnk);
		}
		//第三步，将StatusAgent与每一层对应的分身绑定好
		for(int i = 0;i<statusAgentLength;i++){
			StatusAgent statusAgent = AgentManager.getStatusAgent("status "+i);
			int layerNum = layerNumber;
			for(int j=0;j<layerNum;j++){
				LayerEnum layer = LayerEnum.valueOf("Layer"+(j+1));
				BnkSimulationLayer bnkLayer = AgentManager.getlayer(layer);
				ActionAgent[] actionAgents = bnkLayer.getActionAgents();
				statusAgent.setActionAgent(layer, actionAgents[i]);
				System.out.println("StatusAgent结点"+i+"与分身绑定完成");
			}
		}
		//第四步，设置初始破产的agent数量
		int bnkAgentNumber = 1;
		AgentManager.initBankruptcySource(bnkAgentNumber);
		System.out.println(bnkAgentNumber+"个初始破产结点设置完成");
		//第五步，每一层都开始模拟
		for(int i = 0;i<layerNumber;i++){
			LayerEnum layer = LayerEnum.valueOf("Layer"+(i+1));
			BnkSimulationLayer bnkLayer = AgentManager.getlayer(layer);
			
			Thread thread = new Thread(bnkLayer);
			System.out.println("开始第"+(i+1)+"层网络的模拟");
			thread.start();
		}
		
		//System.out.println("整个实验模拟结束");
	}

}
