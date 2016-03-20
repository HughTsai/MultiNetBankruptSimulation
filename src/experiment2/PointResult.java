package experiment2;

/**
 * 用于存放一次实验的一个周期的BN/SN值
 * @author HUGH
 *
 */
public class PointResult {
	private int round;//存放周期
	private int BN;//存放BN的值
	
	public PointResult(int round, int BN) {
		this.round = round;
		this.BN = BN;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public int getBN() {
		return BN;
	}

	public void setBN_SN(int BN) {
		this.BN = BN;
	}
	
	
}
