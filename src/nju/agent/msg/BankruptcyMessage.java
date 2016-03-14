package nju.agent.msg;

import nju.net.components.ActionAgent;

public class BankruptcyMessage implements AbstractMessage{
	private ActionAgent src;
	private double value;
	private boolean isReverse;
	
	public BankruptcyMessage(ActionAgent src, double value , boolean isReverse){
		this.src = src;
		this.value = value;
		this.isReverse = isReverse;
	}
	//信息的传递发起方
	public ActionAgent getSrc(){
		return src;
	}
	//表示有向边的值
	public double getValue(){
		return value;
	}
	//表示是否是逆着有向边传递影响；
	public boolean isReverse(){
		return isReverse;
	}
}
