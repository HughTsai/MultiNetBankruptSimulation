package nju.util;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class UtilLock2 {
	private static ReentrantLock lock = new ReentrantLock();
	private static Condition condition = lock.newCondition();
	public static void lock(){
		lock.lock();
	}
	public static void unlock(){
		lock.unlock();
	}
	public static void ConSignal(){
		condition.signalAll();
	}
	public static void ConWait(){
		try {
			condition.await();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("layerCondition的wait出错");
		}
	}
}
