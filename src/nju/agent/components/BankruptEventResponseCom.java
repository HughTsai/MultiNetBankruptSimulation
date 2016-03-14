package nju.agent.components;

import nju.agent.msg.BankruptcyMessage;
import nju.net.components.ActionAgent;

public class BankruptEventResponseCom implements Component{
	private final double aerfa;//逆向影响的衰减系数α
	
	/**
	 * 
	 * @param agent
	 * @param aerfa   逆向影响的衰减系数α
	 */
	public BankruptEventResponseCom(double aerfa){
		this.aerfa = aerfa;
	}
	
	public double handle(BankruptcyMessage msg){
		double diff_c;
		if(msg.isReverse()){
			//是逆向传来的信息，即信息时从下游agent传来
			diff_c =  msg.getValue() * this.aerfa;
		}else{
			//是顺向传来的信息，即从上游传来
			diff_c = msg.getValue();
		}
		return diff_c;
	}

}
