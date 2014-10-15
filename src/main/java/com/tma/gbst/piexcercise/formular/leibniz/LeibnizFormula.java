package com.tma.gbst.piexcercise.formular.leibniz;

import java.util.Scanner;

import com.tma.gbst.piexcercise.formular.Formula;
import com.tma.gbst.piexcercise.formular.Master;
import com.tma.gbst.piexcercise.formular.Result;
import com.tma.gbst.piexcercise.formular.WorkerCreator;

/**
 * This  class define the leibeniz formular
 * 
 * @author tdainhan
 * @version 1.0
 * @since 10/9/2014
 */
public class LeibnizFormula  implements Formula {
	
	private static final String NAME = "Leibeniz Formular";
	
	private Master master;
	private WorkerCreator workerCreator = new LeibnizWorkerCreator();
	
	/**
	 * Construct a Leibeniz Formula
	 */
	public LeibnizFormula(){
		LeibnizResult leibnizResult = new LeibnizResult();
		master = new Master(workerCreator, leibnizResult);
	};

	/**
	 * Start to calculate pi number follow it's formular
	 */
	public void calculate() {
		master.processing();
	}
	
	@Override
	public void setParameters(String[] parameters) {
		
	}
	
	/**
	 * Stop to calculate pi number by this formular
	 */
	public void cancel() {
		master.shutdown();
	}
	
	/**
	 * Get Pi number at current time, make sure you have start to calculate, otherwise
	 * it will return 0.0
	 */
	public Result getResult(){
		return master.getResult();
	}

	/**
	 * Get formular name
	 */
	public String getFormularName(){
		return NAME;
	}

}
