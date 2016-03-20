package experiment2;

import java.util.ArrayList;



import com.mathworks.toolbox.javabuilder.MWArray;
import com.mathworks.toolbox.javabuilder.MWClassID;
import com.mathworks.toolbox.javabuilder.MWException;
import com.mathworks.toolbox.javabuilder.MWNumericArray;

import expt2.DotGraph;

/**
 * 用于存放每一次实验（例如100次模拟的，200次模拟的实验等...）的最终结果
 * 也就是各次模拟结果的对应周期的平均值
 * @author HUGH
 *
 */
public class ExperimentData {
	//存放每一次模拟的结果
	private ArrayList<OnceSimuResult> results = new ArrayList<OnceSimuResult>();
	//该次实验中，模拟次数
	private int simulateNum = 0;
	//该次实验每次模拟的平均结果
	private double[] avgResults =null;
	//该次实验的BN/SN的值
	private double[] bn_sn = null;
	//该次实验的agent数量
	private int agentNum = -1;
	
	
	public ExperimentData(int simulateNum, int agentNum) {
		super();
		this.simulateNum = simulateNum;
		this.agentNum = agentNum;
	}
	
	public int getSimulateNum() {
		return simulateNum;
	}

	public void setSimulateNum(int simulateNum) {
		this.simulateNum = simulateNum;
	}

	public int getAgentNum() {
		return agentNum;
	}

	public void setAgentNum(int agentNum) {
		this.agentNum = agentNum;
	}

	/**
	 * 将模拟结果计入result
	 * @param onceExptRst 当次模拟的结果
	 */
	public void addData(OnceSimuResult onceExptRst){
		results.add(onceExptRst);
	}
	/**
	 * 清空该次实验的结果
	 */
	public void clean(){
		results.clear();
	}
	
	/**
	 * 获取所有模拟结果中，周期最长的周期值。
	 * 该周期值将用于创建数组，数组用来统计该实验每一次模拟对应周期的
	 * @return
	 */
	private int getMaxLength(){
		int max=0;
		for(int i=0;i<results.size();i++){
			int simulationLength = results.get(i).length();//得到第i次模拟的结果周期
			if(max < simulationLength){
				max = simulationLength;
			}
		}
		return max;
	}
	/**
	 * 把每一次模拟结果进行统计，得到平均值
	 */
	private void statisticResult(){
		int max = getMaxLength();
		avgResults = new double[max];//自动初始化为0
		if(simulateNum != results.size())
			throw new RuntimeException("模拟成功次数与实验设置的次数不符合");
		for(int i=0;i < simulateNum;i++){
			int[] temp = results.get(i).getData(max);
			for(int j=0;j<max;j++){
				avgResults[j] += (double)temp[j];
			}
		}
		for(int i=0;i<max;i++){
			avgResults[i] = avgResults[i]/(double)simulateNum;
		}
	}
	/**
	 * 计算得到该次实验的BN/SN值（数组下标表示周期）
	 */
	private void calBN_SN(){
		statisticResult();
		bn_sn = new double[avgResults.length];
		for(int i=0;i<bn_sn.length;i++){
			bn_sn[i] = avgResults[i]/(double)agentNum;
		}
	}
	
	//==============================================================
	/**
	 * 显示实验结果的diagram
	 */
	public void showData(){
		
		calBN_SN();
		//printArray();
		
		plot();
	}
	
	private void plot(){
		//get x,y 
		int len = bn_sn.length;
		double[] ax = new double[len];
		double[] ay = new double[len];
		
		for(int i = 0 ; i < len ; i++){
			double temp = bn_sn[i];
			ax[i] = i;//将周期作为横坐标
			ay[i] = bn_sn[i];//将BN/SN作为纵坐标
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
	
	private static void printArray(){
		
	}
	
	
}
