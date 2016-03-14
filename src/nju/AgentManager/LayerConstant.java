package nju.AgentManager;

public enum LayerConstant {
	Layer1("第一层"),Layer2("第二层"),Layer3("第三层");
	
	String layerName;
	LayerConstant (String name){
		this.layerName = name;
	}
	public String getName(){
		return this.layerName;
	}
}
