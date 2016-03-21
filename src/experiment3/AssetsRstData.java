package experiment3;
/**
 * 用于保存每次初始资产以及对应的破产率
 * @author HUGH
 *
 */
public class AssetsRstData {
	private double Assets = 0;//初始资产的值
	private int bankruptNum = 0;//在多次模拟中，破产的次数
	private double bankruptcyRate = -1;//破产率
	
	/**
	 * 初始化时需要设置初始资产值
	 * @param assets 初始资产值
	 */
	public AssetsRstData(double assets) {
		super();
		Assets = assets;
	}
	//-----get函数-------
	public double getAssets() {
		return Assets;
	}
	public int getBankruptNum() {
		return bankruptNum;
	}

	
	/**
	 * 破产次数增加
	 * @param increment增加的量
	 */
	public void addBnkNum(int increment){
		this.bankruptNum += increment;
	}
	
	/**
	 * 获取破产率
	 * @param times 基数
	 * @return
	 */
	public double getBankruptcyRate(int times) {
		
		this.bankruptcyRate = (double)this.bankruptNum/(double)times;
		return bankruptcyRate;
	}
	
	
}
