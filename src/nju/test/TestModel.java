package nju.test;

import java.util.ArrayList;

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
		double u = 0.3;// 破产阈值，u >0 , 
		double e = 2;// 周期回复最小值
		double k = 10;// 周期回复速率指标。k越大，回复越慢(周期回复的值越小）。
		double aerfa = 0.5;//破产传染逆向影响系数，  0<=aerfa<=1
		
		int[] c = {20,30,30,30,2,15,5,20,15,30,
					30,5,8,13,2,13,5,30,8,
					15,5,30,20,15,15,9,5,3,7,
					3,15,33,18,8,7,12,30,10,8,
					8,7,6,30,15,10,28,8,10,8,7};
		
		for(int i = 0; i < 50 ; i++){
			agents[i] = new StatusAgent("status "+i, c[i]*u, c[i], e, k);
			System.out.println("初始化StatusAgent "+i);
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
		System.out.println("初始化第1"+"层中的数据：");
		//BnkSimulationLayer bnk =new BnkSimulationLayer(layer,relation1,indexAction1,aerfa);
		//AgentManager.setLayer(layer, bnk);
		
		layer = LayerEnum.valueOf("Layer2");
		System.out.println("初始化第2"+"层中的数据：");
		//bnk =new BnkSimulationLayer(layer,relation2,indexAction2,aerfa);
		//AgentManager.setLayer(layer, bnk);
		
		layer = LayerEnum.valueOf("Layer3");
		System.out.println("初始化第3"+"层中的数据：");
		//bnk =new BnkSimulationLayer(layer,relation3,indexAction3,aerfa);
		//AgentManager.setLayer(layer, bnk);
		
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
		
		
		//第四步，设置初始破产的agent数量
		int bnkAgentNumber = 5;
		AgentManager.initBankruptcySource(bnkAgentNumber);
		System.out.println(bnkAgentNumber+"个初始破产结点设置完成");
		
		
		//第五步，每一层都开始模拟
		for(int i = 0;i<layerNumber;i++){
			layer = LayerEnum.valueOf("Layer"+(i+1));
			BnkSimulationLayer bnkLayer = AgentManager.getlayer(layer);
			
			Thread thread = new Thread(bnkLayer);
			System.out.println("开始第"+(i+1)+"层网络的模拟");
			thread.start();
		}
		
		//System.out.println("整个实验模拟结束");
	}

}
