package nju.AgentManager;

public enum LayerEnum {
	Layer1("1"),Layer2("2"),Layer3("3");
	
	String layerName;
	LayerEnum (String name){
		this.layerName = name;
	}
	public String getName(){
		return this.layerName;
	}
}
