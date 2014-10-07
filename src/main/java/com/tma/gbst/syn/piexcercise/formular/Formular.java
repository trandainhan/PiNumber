package com.tma.gbst.syn.piexcercise.formular;

public interface Formular {
	
	/**
	 * get the name of formular
	 * 
	 * @return string  the name of formular
	 */
	public String getFormularName();
	
	/**
	 * Starting to calculate pi number by this formular
	 */
	public double startCalculate();
	
	/**
	 * Stop calculate pi number
	 */
	public double stopCalculate();
	
	/**
	 * Return the result of pi number after calculate
	 * 
	 * @return double  the result of pi 
	 */
	public double getResult();
	
}
