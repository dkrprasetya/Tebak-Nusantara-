package com.deadlinestudio.tebaknusantara.tebakbudaya;

import java.util.Timer;
import java.util.TimerTask;

public class Stopwatch {
	private int duration;
	
	private int interval;
	private Timer timer;
	
	private int centi=100;
	private Timer timerCenti;
	
	private int delay = 1000;
	private int period = 1000;
	
	private boolean includeCenti;
	private boolean running = false;
	//private boolean timeUp = false;
	
	
	public Stopwatch(int interval,boolean includeCenti) {
		this.interval = interval;
		duration = interval;
		this.includeCenti = includeCenti;
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				decInterval();
			}
		}, delay, period);
		if (includeCenti){
			timerCenti = new Timer();
			timerCenti.scheduleAtFixedRate(new TimerTask() {
				
				@Override
				public void run() {
					decCenti();
				}
			}, delay, 10);
		}
	}
	
	public void start(){
		running = true;
	}
	
	public void pause(){
		//berhentikan stopwatch
		running = false;
	}
	
	public boolean isRunning(){
		//keberjalanan stopwatch
		return running;
	}
	
	public boolean isTimeUp(){
		//waktu tersisa habis
		return (interval == 0 && centi ==0);
	}
	
	private void decInterval(){
		if (running){
			//if (interval == 1) running = false;//timer.cancel();
			--interval;
		}

	}
	
	private void decCenti(){
		if (running){
			if (centi == 1){
				if (interval == 0){
					running = false;
					//timerCenti.cancel();
					//timeUp = true;
				}
				else centi = 100;
			}
			--centi;
		}
	}
	
	@Override
	public String toString(){
		//ubah nilai waktu yang tersisa ke format hh:mm:ss:cc
		String count="";
		int hour = interval/3600;
		int minute = (interval%3600)/60;
		int sec = interval%60;
		if (hour >= 0){
			if (hour <= 9) count += "0";
			count += String.valueOf(hour);
			count += ":";
		}
		if (minute >= 0){
			if (minute <= 9) count += "0";
			count += String.valueOf(minute);
			count += ":";
		}
		if (sec <= 9) count += "0";
		count += String.valueOf(sec);
		if (includeCenti) {
			count += ":";
			if (centi%100 <= 9) count += "0";
			count += String.valueOf(centi%100);
		}
		return count;
	}
	
	
	public int getInterval(){
		return interval;
	}
	
	public void reset(){
		centi = 100;
		interval = duration;
	}

}
