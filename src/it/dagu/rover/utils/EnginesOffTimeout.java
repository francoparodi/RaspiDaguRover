package it.dagu.rover.utils;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.wiringpi.SoftPwm;
import it.dagu.rover.model.Configuration;
import java.util.TimerTask;

public class EnginesOffTimeout extends TimerTask implements java.io.Serializable {
	
	private static final long serialVersionUID = 1L;
	private Configuration configuration;
	private GpioController gpio;
	private GpioPinDigitalOutput dirLeftFront;
	private GpioPinDigitalOutput dirLeftBack;
	private GpioPinDigitalOutput dirRightFront;
	private GpioPinDigitalOutput dirRightBack;
	private boolean stopEngines = false;
	private boolean engine = false;
	private int pwmLeft;
	private int pwmRight;

	public void run() {
		if (!isStopEngines()) {
			setStopEngines(true);
		} else {
			stopEngines();
			cancel();
		}
	}

	public void stopEngines() {
		if ((engine) && (configuration != null) && (configuration.isPiExists())) {
			SoftPwm.softPwmWrite(pwmLeft, 0);
			SoftPwm.softPwmWrite(pwmRight, 0);
			gpio.shutdown();
			gpio.unprovisionPin(new GpioPin[] { dirLeftFront });
			gpio.unprovisionPin(new GpioPin[] { dirLeftBack });
			gpio.unprovisionPin(new GpioPin[] { dirRightFront });
			gpio.unprovisionPin(new GpioPin[] { dirRightBack });
		}
		System.out.println("session expired, engines off.");
	}

	private boolean isStopEngines() {
		return stopEngines;
	}

	public void setStopEngines(boolean stopEngines) {
		this.stopEngines = stopEngines;
	}

	public Configuration getConfiguration() {
		return configuration;
	}

	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}

	public GpioController getGpio() {
		return gpio;
	}

	public void setGpio(GpioController gpio) {
		this.gpio = gpio;
	}

	public GpioPinDigitalOutput getDirLeftFront() {
		return dirLeftFront;
	}

	public void setDirLeftFront(GpioPinDigitalOutput dirLeftFront) {
		this.dirLeftFront = dirLeftFront;
	}

	public GpioPinDigitalOutput getDirLeftBack() {
		return dirLeftBack;
	}

	public void setDirLeftBack(GpioPinDigitalOutput dirLeftBack) {
		this.dirLeftBack = dirLeftBack;
	}

	public GpioPinDigitalOutput getDirRightFront() {
		return dirRightFront;
	}

	public void setDirRightFront(GpioPinDigitalOutput dirRightFront) {
		this.dirRightFront = dirRightFront;
	}

	public GpioPinDigitalOutput getDirRightBack() {
		return dirRightBack;
	}

	public void setDirRightBack(GpioPinDigitalOutput dirRightBack) {
		this.dirRightBack = dirRightBack;
	}

	public int getPwmLeft() {
		return pwmLeft;
	}

	public void setPwmLeft(int pwmLeft) {
		this.pwmLeft = pwmLeft;
	}

	public int getPwmRight() {
		return pwmRight;
	}

	public void setPwmRight(int pwmRight) {
		this.pwmRight = pwmRight;
	}

	public boolean isEngine() {
		return engine;
	}

	public void setEngine(boolean engine) {
		this.engine = engine;
	}
}
