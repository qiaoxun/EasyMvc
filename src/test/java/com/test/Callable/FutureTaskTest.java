package com.test.Callable;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class FutureTaskTest {
	private static int cpuNum = 4;

	static {

	}

	private static ThreadPoolExecutor tpe = new ThreadPoolExecutor(cpuNum, cpuNum, 0, TimeUnit.SECONDS,
			new LinkedBlockingQueue<Runnable>());

	public static void main(String[] args) {
		CountDownLatch cdl = new CountDownLatch(50);
		for (int i = 0; i < 50; i++) {
			Task t = new Task(i, cdl);
			Future<Integer> f = tpe.submit(t);
			try {
				System.out.println(i + " = " + f.get());
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		}

		System.out.println("main thread");
		try {
			cdl.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}

class Task implements Callable<Integer> {

	private int num;
	private CountDownLatch cdl;

	public Task(int num, CountDownLatch cdl) {
		this.num = num;
		this.cdl = cdl;
	}

	@Override
	public Integer call() throws Exception {
		System.out.println(Thread.currentThread().getName());

		cdl.countDown();
		return num;
	}
}