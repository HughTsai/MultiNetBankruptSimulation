package experiment3;

import java.util.ArrayList;

import expt4.DotGraph;

import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;


public class ExperimentData {
	//所指定的agent
	int appointedAgent = -1;
	//存放不同初始资产时以及对应不同破产次数的对象
	ArrayList<AssetsRstData> results = new ArrayList<AssetsRstData>();
	//存放不同的初始资产数组，与破产率数组一一对应
	double[] assets = null;
	double[] bnkRate = null;
	//每个初始资产值模拟的次数
	int simulationNum = -1;
	
	public ExperimentData( int simulationNum) {
		super();
		this.simulationNum = simulationNum;
	}
	
	public int getAppointedAgent() {
		return appointedAgent;
	}
	
	public void setAppointedAgent(int appointedAgent) {
		this.appointedAgent = appointedAgent;
	}

	/**
	 * 将一个assetsRstData的结果计入result
	 * @param assetsRstData 
	 */
	public void addData(AssetsRstData assetsRstData){
		results.add(assetsRstData);
	}
	/**
	 * 清空该次实验的结果
	 */
	public void clean(){
		results.clear();
	}
	//计算所有的破产率
	private void calResult(){
		int len = this.results.size();
		assets = new double[len];
		bnkRate = new double[len];
		for(int i=0;i<len;i++){
			AssetsRstData temp = this.results.get(i);
			assets[i] = temp.getAssets();
			bnkRate[i] = temp.getBankruptcyRate(simulationNum);
		}
	}
	
	
	
	//==============================================================
		/**
		 * 显示实验结果的diagram
		 */
		public void showData(){
			System.out.println("开始计算实验结果");
			calResult();
			//printArray();
			System.out.println("开始显示数据图表");
			plot();
		}
		
		private void plot(){
			//get x,y 
			int len = results.size();
			double[] ax = new double[len];
			double[] ay = new double[len];
			
			for(int i = 0 ; i < len ; i++){
				ax[i] = this.assets[i];//将初始资产值作为横坐标
				ay[i] = this.bnkRate[i];//将破产率作为纵坐标
			}
			
			MWNumericArray x = new MWNumericArray(ax, MWClassID.DOUBLE);
			MWNumericArray y = new MWNumericArray(ay, MWClassID.DOUBLE);
			
			try {
				DotGraph figure = new DotGraph();
				figure.Plot(x,y);
				figure.waitForFigures();
				
				figure.dispose();
			} catch (MWException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally{
				MWArray.disposeArray(x);
				MWArray.disposeArray(y);
				System.out.println("Over!!!!!");
			}
		}
}
