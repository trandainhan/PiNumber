package com.tma.gbst.piexcercise.formula;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Responsible for receive the calculation order. Manage meta data, divide task
 * to {@code Worker} and get result from it.
 * 
 * Using {@code ScheduledThreadPoolExecutor} to manage thread.
 * 
 * @author tdainhan
 *
 */
public class Master {

	private ThreadPoolExecutor executor;

	// The result stores here.
	private LinkedList<Future<Result>> futures;

	// The final value of Pi stored here.
	private Result finalResult;

	// worker creator help to create a worker.
	private WorkerCreator workerCreator;

	/**
	 * Construct a {@code Master} with given initial parameters. 
	 * 
	 * @param workerCreator  the {@code WorkerCretor} help to create {@code Worker}
	 * @param finalResult  the result instances responsible for get result after calculating 
	 * that was passed into {@code Master} class.
	 */
	public Master(WorkerCreator workerCreator, Result finalResult) {
		this.workerCreator = workerCreator;
		this.finalResult = finalResult;
	}

	/**
	 * Get the the result of pi value after calculating.
	 * 
	 * Iterating {@code ArrayList} futures, check each task whether done or not
	 * then get the result.
	 * 
	 * @return Result the result of calculating.
	 */
	public Result getResult() {
		for (Future<Result> fu : futures) {
			if (fu.isDone() && !fu.isCancelled()) {
				try {
					finalResult.add(fu.get());
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (ExecutionException e) {
					e.printStackTrace();
				}
			} else {
				break;
			}
		}
		return finalResult;
	}

	/**
	 * {@code Master} use this to divide task to all worker. Using
	 * {@link ThreadPoolExecutor} to manage all task that running and
	 * get result via a {@link List} of {@link Future}.
	 * 
	 * <p>
	 * Finding the number of available processor to set the number of
	 * thread that run simultaneously. Aiming to get the best performance.
	 * 
	 */
	public void process() {

		futures = new LinkedList<Future<Result>>();
		int nThreads = Runtime.getRuntime().availableProcessors();
		executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(nThreads);
		Worker worker;
		while ((worker = workerCreator.createNextWorker()) != null) {
			synchronized (this) {
				if (!executor.isShutdown()) {
					Future<Result> future = executor.submit(worker);
					futures.add(future);
				} else {
					break;
				}
			}
		}
		executor.shutdown();
		try {
			executor.awaitTermination(120, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Attempt to shut down all task, after doing this, we wait for actively
	 * tasks execute, all task on waiting list will be canceled and no task can
	 * submit anymore.
	 */
	public void shutdown() {
		synchronized (this) {
			executor.shutdownNow();
		}
		try {
			executor.awaitTermination(120, TimeUnit.MINUTES);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
