package it.dagu.rover.model;

import java.sql.Timestamp;
import java.util.Date;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.pi4j.io.gpio.GpioController;
import com.pi4j.io.gpio.GpioPin;
import com.pi4j.io.gpio.GpioPinDigitalOutput;
import com.pi4j.io.gpio.PinState;
import com.pi4j.io.gpio.RaspiPin;
import com.pi4j.wiringpi.SoftPwm;

@Named
@SessionScoped
public class RaspiPi2Gpio implements java.io.Serializable {

	private static final long serialVersionUID = 1L;
	private GpioController gpioController;
	private GpioPinDigitalOutput dirLeftFront;
	private GpioPinDigitalOutput dirLeftBack;
	private GpioPinDigitalOutput dirRightFront;
	private GpioPinDigitalOutput dirRightBack;
	private GpioPinDigitalOutput lightRelay;
	private final int PWM_LEFT = 3;
	private final int PWM_RIGHT = 1;
	private final int LOWER_LIMIT = 0;

	@Inject
	private Configuration configuration;

	public void initialize() {
		logging("instance gpioController.");
		gpioController = com.pi4j.io.gpio.GpioFactory.getInstance();

		logging("wiring Pi library.");
		com.pi4j.wiringpi.Gpio.wiringPiSetup();

		logging("set GPIO_GEN4 on pin #16");

		dirLeftFront = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_04, PinState.HIGH);

		logging("set GPIO_GEN5 on pin #18");

		dirLeftBack = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_05, PinState.LOW);

		logging("set GPIO_GEN0 on pin #11");

		dirRightFront = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_00, PinState.HIGH);

		logging("set GPIO_GEN2 on pin #13");

		dirRightBack = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_02, PinState.LOW);

		logging("set GPIO_GEN6 on pin #22");

		lightRelay = gpioController.provisionDigitalOutputPin(RaspiPin.GPIO_06, PinState.LOW);
		
		logging("set range speed of left motors, GPIO_GEN3 on pin #15");
		SoftPwm.softPwmCreate(PWM_LEFT, 0, configuration.getUpperLimitEngines());
		logging("set range speed of right motors, GPIO_GEN1 on pin #12");
		SoftPwm.softPwmCreate(PWM_RIGHT, 0, configuration.getUpperLimitEngines());

		logging("engines on.");
	}

	public void shutdown() {
		logging("shutdown gpioController.");
		gpioController.shutdown();
		gpioController.unprovisionPin(new GpioPin[] { dirLeftFront });
		gpioController.unprovisionPin(new GpioPin[] { dirLeftBack });
		gpioController.unprovisionPin(new GpioPin[] { dirRightFront });
		gpioController.unprovisionPin(new GpioPin[] { dirRightBack });
		gpioController.unprovisionPin(new GpioPin[] { lightRelay });
		logging("engines off.");
	}

	private void logging(String msg) {
		System.out.println(getTime() + " " + msg);
	}

	private String getTime() {
		Date date = new Date();
		return new Timestamp(date.getTime()).toString();
	}

	public GpioController getGpioController() {
		return gpioController;
	}

	public void setGpioController(GpioController gpioController) {
		this.gpioController = gpioController;
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
	
	public GpioPinDigitalOutput getLightRelay() {
		return lightRelay;
	}

	public void setLightRelay(GpioPinDigitalOutput lightRelay) {
		this.lightRelay = lightRelay;
	}

	public void setLeftSpeed(int speed) {
		SoftPwm.softPwmWrite(PWM_LEFT, speed);
	}

	public void setRightSpeed(int speed) {
		SoftPwm.softPwmWrite(PWM_RIGHT, speed);
	}

	public void stopLeft() {
		SoftPwm.softPwmWrite(PWM_LEFT, 0);
	}

	public void stopRight() {
		SoftPwm.softPwmWrite(PWM_RIGHT, 0);
	}

	public int getPWM_LEFT() {
		return PWM_LEFT;
	}

	public int getPWM_RIGHT() {
		return PWM_RIGHT;
	}

	public int getLOWER_LIMIT() {
		return LOWER_LIMIT;
	}
}
