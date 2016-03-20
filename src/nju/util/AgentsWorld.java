package nju.util;

import java.util.ArrayList;

import nju.net.components.ActionAgent;

public abstract class AgentsWorld {
	public int timestep = 0;
	public static int bankruptNum = 0;

	
	public void init(){
		bankruptNum = 0;
		initAgents();
		initRelations();
	}
	
	
	protected abstract void initRelations();
	protected abstract void initAgents();
	
	public abstract void startSimulation();

}
