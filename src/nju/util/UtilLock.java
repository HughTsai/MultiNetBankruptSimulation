package nju.util;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class UtilLock {
	private static ReentrantLock lock = new ReentrantLock(true);
	private static Condition layerCondition = lock.newCondition();
	private static Condition controlCondition = lock.newCondition();
	private static int power = 1;
	private static int layerDoneCounter = 0;
	
	
	public static synchronized int getLayerDoneCounter() {
		return layerDoneCounter;
	}
	public static synchronized void resetLayerDoneCounter() {
		UtilLock.layerDoneCounter = 0;
	}
	public static synchronized void autoIncrease_1Counter(){
		UtilLock.layerDoneCounter++;
	}
	public static int getPower() {
		return power;
	}
//	public static void setPower(int power) {
//		UtilLock.power = power;
//	}
	public static void lock(){
		lock.lock();
	}
	public static void unlock(){
		lock.unlock();
	}
	public static void layerConSignal(){
		layerCondition.signalAll();
	}
	public static void layerConWait(){
		try {
			layerCondition.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("layerCondition的wait出错");
		}
	}
	public static void controlConSignal(){
		controlCondition.signalAll();
	}
	public static void controlConWait(){
		try {
			controlCondition.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("controlCondition的wait出错");
		}
	}
	public static synchronized void autoIncrease_1(){
		UtilLock.power+=1;
	}
	public static synchronized void resetPower(){
		UtilLock.power = 1;
	}
	
}
