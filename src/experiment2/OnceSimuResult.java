package experiment2;

import java.util.ArrayList;

import nju.util.OutboundException;


/**
 * 用于存放一次模拟的所有周期的BN/SN值
 * 使用ArrayList集合来存放
 * @author HUGH
 *
 */
public class OnceSimuResult {
	private int num;//用于存放模拟序号 例如：num==1，表示该对象存放第一次模拟的结果
	private ArrayList<PointResult> list = new ArrayList<PointResult>();//存放该次模拟每一个周期的数据
	
	
	/**
	 * 构造函数，需要存入该次模拟序号
	 * @param num
	 */
	public OnceSimuResult(int num) {
		this.num = num;
	}
	/**
	 * 添加模拟结果的周期数据
	 * @param point 周期数据
	 */
	public void add(PointResult point){
		list.add(point);
	}
	/**
	 * 获得该次模拟的结果数据周期的长度
	 * @return
	 */
	public int length(){
		return list.size();
	}
	/**
	 * 获得该次模拟的结果数据的数组表达形式。
	 * 其中下标的值指的是模拟周期，数组的长度是传入参数的长度
	 * @param size 传入生成数组的长度
	 * @return
	 */
	public int[] getData(int size){
		if(size<list.size())
			throw new OutboundException("模拟的结果数据，数组化失败，传入size小于最大长度");
		else{
			int[] BNs = new int[size];
			int i=0;
			for(i=0;i<list.size();i++){
				BNs[i] = list.get(i).getBN();
			}
			int finalyRst = list.get(i-1).getBN();
			//数据稳定了之后，后面的空白全部用稳定的BN值来填入
			for(;i<size;i++){
				BNs[i] = finalyRst;
			}
			return BNs;
		}
	}
	/**
	 * 获取arraylist形式的模拟的数据结果
	 * @return
	 */
	public ArrayList<PointResult> getData(){
		return list;
	}
	/**
	 * 为了重复利用对象，设置清空函数，用于下一次模拟的结果数据的存放
	 */
	public void clear(){
		list.clear();
	}
	
	//下面是get和set方法
	public int getNum() {
		return num;
	}
	public void setNum(int num) {
		this.num = num;
		this.clear();//设置了模拟的序号，该对象用来存放新的模拟的结果数据，那么list集合自动清空
	}
	
}
