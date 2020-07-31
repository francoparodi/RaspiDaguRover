package it.dagu.rover.controller;

import it.dagu.rover.model.Configuration;
import it.dagu.rover.model.Engines;
import it.dagu.rover.model.RaspiPi2Gpio;
import it.dagu.rover.utils.Cockpit;
import it.dagu.rover.utils.EnginesOffTimeout;
import it.dagu.rover.utils.Messages;
import java.io.IOException;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.SocketException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Timer;
import javax.annotation.PostConstruct;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

@Named
@ViewScoped
public class DaguController implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final int LOWER_LIMIT = 0;
	private String log;
	private EnginesOffTimeout enginesOffTimeout;
	private Timer timer;
	private boolean cameraLog = true;
	private final String LOGOUT_PAGE = "login.jsf?faces-redirect=true";

	@Inject
	private Configuration configuration;
	@Inject
	private Cockpit cockpit;
	@Inject
	private RaspiPi2Gpio raspiPi2Gpio;
	@Inject
	private Engines engines;
	@Inject
	private Messages messages;

	@PostConstruct
	public void printConfig() {
		System.out.println(configuration.toString());
	}

	private void startEngines() {
		if (configuration.isPiExists()) {
			raspiPi2Gpio.initialize();
			runEngineTimeout();
		}
		engines.setLeftForward(true);
		engines.setRightForward(true);
	}

	public void rightForward() {
		if (configuration.isPiExists()) {
			raspiPi2Gpio.getDirRightFront().high();
			raspiPi2Gpio.getDirRightBack().low();
		}
		engines.setRightForward(true);
		logging("right forward.");
	}

	public void leftForward() {
		if (configuration.isPiExists()) {
			raspiPi2Gpio.getDirLeftFront().high();
			raspiPi2Gpio.getDirLeftBack().low();
		}
		engines.setLeftForward(true);
		logging("left forward.");
	}

	public void rightBackward() {
		if (configuration.isPiExists()) {
			raspiPi2Gpio.getDirRightFront().low();
			raspiPi2Gpio.getDirRightBack().high();
		}
		engines.setRightForward(false);
		logging("right backward.");
	}

	public void leftBackward() {
		if (configuration.isPiExists()) {
			raspiPi2Gpio.getDirLeftFront().low();
			raspiPi2Gpio.getDirLeftBack().high();
		}
		engines.setLeftForward(false);
		logging("left backward.");
	}

	public void leftReverse() {
		if (engines.isLeftForward()) {
			leftBackward();
		} else {
			leftForward();
		}
	}

	public void rightReverse() {
		if (engines.isRightForward()) {
			rightBackward();
		} else {
			rightForward();
		}
	}

	public void reverseAll() {
		leftReverse();
		rightReverse();
	}

	public void setLeftSpeed(int speed) {
		if (configuration.isPiExists()) {
			raspiPi2Gpio.setLeftSpeed(speed);
		}
		logging("set left speed to " + speed + "%.");
	}

	public void setRightSpeed(int speed) {
		if (configuration.isPiExists()) {
			raspiPi2Gpio.setRightSpeed(speed);
		}
		logging("set right speed to " + speed + "%.");
	}

	public void setAllSpeed(int speed) {
		if ((speed > LOWER_LIMIT) && (getLeft() + speed >= configuration.getUpperLimitEngines())) {
			setLeft(100);
		} else if ((speed < LOWER_LIMIT) && (getLeft() + speed < LOWER_LIMIT)) {
			setLeft(LOWER_LIMIT);
		} else {
			setLeft(getLeft() + speed);
		}

		if ((speed > LOWER_LIMIT) && (getRight() + speed >= configuration.getUpperLimitEngines())) {
			setRight(100);
		} else if ((speed < LOWER_LIMIT) && (getRight() + speed < LOWER_LIMIT)) {
			setRight(LOWER_LIMIT);
		} else {
			setRight(getRight() + speed);
		}

		setLeftSpeed(getLeft());
		setRightSpeed(getRight());
	}

	public void stopLeft() {
		setLeft(LOWER_LIMIT);
		setLeftSpeed(getLeft());
		if (configuration.isPiExists()) {
			raspiPi2Gpio.stopLeft();
		}
		logging("stop left motor.");
	}

	public void stopRight() {
		setRight(LOWER_LIMIT);
		setRightSpeed(getRight());
		if (configuration.isPiExists()) {
			raspiPi2Gpio.stopRight();
		}
		logging("stop right motor.");
	}

	public void stopAll() {
		stopLeft();
		stopRight();
	}

	public void stopEngines() {
		stopAll();
		if (configuration.isPiExists()) {
			raspiPi2Gpio.shutdown();
		}
	}

	private void logging(String msg) {
		System.out.println(getTime() + " " + msg);
		setLog(msg + "\n" + getLog());
	}

	private String getTime() {
		Date date = new Date();
		return new Timestamp(date.getTime()).toString();
	}

	public int getLeft() {
		return engines.getLeft();
	}

	public void setLeft(int left) {
		engines.setLeft(left);
	}

	public int getRight() {
		return engines.getRight();
	}

	public void setRight(int right) {
		engines.setRight(right);
	}

	public boolean isEngine() {
		return engines.isEngine();
	}

	public void setEngine(boolean engine) {
		engines.setEngine(engine);
	}

	public void engineSwitch() {
		if (isEngine()) {
			reinitialize();
		} else {
			startEngines();
			setEngine(true);
		}
	}

	public boolean isLeftForward() {
		return engines.isLeftForward();
	}

	public void setLeftForward(boolean leftForward) {
		engines.setLeftForward(leftForward);
	}

	public boolean isRightForward() {
		return engines.isRightForward();
	}

	public void setRightForward(boolean rightForward) {
		engines.setRightForward(rightForward);
	}

	private void reinitialize() {
		stopEngines();
		setEngine(false);
		setCameraLog(true);
	}

	public void onLeftIncrease() {
		if (getLeft() < configuration.getUpperLimitEngines()) {
			setLeft(getLeft() + configuration.getPercentageSteps());
			setLeftSpeed(getLeft());
			logging("left speed increased to " + getLeft() + "%.");
		}
	}

	public void onRightIncrease() {
		if (getRight() < configuration.getUpperLimitEngines()) {
			setRight(getRight() + configuration.getPercentageSteps());
			setRightSpeed(getRight());
			logging("right speed increased to " + getRight() + "%.");
		}
	}

	public void onLeftDecrease() {
		if (getLeft() > 0) {
			setLeft(getLeft() - configuration.getPercentageSteps());
			setLeftSpeed(getLeft());
			logging("left speed decreased to " + getLeft() + "%.");
		}
	}

	public void onRightDecrease() {
		if (getRight() > 0) {
			setRight(getRight() - configuration.getPercentageSteps());
			setRightSpeed(getRight());
			logging("right speed decreased to " + getRight() + "%.");
		}
	}

	public void onLeftSteer() {
		if (getLeft() - configuration.getPercentageDeltaSteering() <= LOWER_LIMIT) {
			setLeft(LOWER_LIMIT);
			setLeftSpeed(getLeft());
		} else if (getRight() > getLeft()) {
			setLeft(getRight() - configuration.getPercentageDeltaSteering());
			setLeftSpeed(getLeft());
		} else {
			setLeft(getLeft() - configuration.getPercentageSteps());
			setLeftSpeed(getLeft());
		}
		logging("left speed decreased to " + getLeft() + "%.");
	}

	public void onRightSteer() {
		if (getRight() - configuration.getPercentageDeltaSteering() <= LOWER_LIMIT) {
			setRight(LOWER_LIMIT);
			setRightSpeed(getRight());
		} else if (getLeft() > getRight()) {
			setRight(getLeft() - configuration.getPercentageDeltaSteering());
			setRightSpeed(getRight());
		} else {
			setRight(getRight() - configuration.getPercentageSteps());
			setRightSpeed(getRight());
		}
		logging("right speed decreased to " + getRight() + "%.");
	}

	public void onAllIncrease() {
		setAllSpeed(configuration.getPercentageSteps());
	}

	public void onAllDecrease() {
		setAllSpeed(configuration.getPercentageSteps() * -1);
	}

	public void onStraight() {
		if (getLeft() > getRight()) {
			setRight(getLeft());
			setRightSpeed(getRight());
		}
		if (getLeft() < getRight()) {
			setLeft(getRight());
			setLeftSpeed(getLeft());
		}
	}

	public String getCameraUrl() {
		return configuration.getCameraUrl();
	}

	public int getUpperLimitEngines() {
		return configuration.getUpperLimitEngines();
	}

	public int getPercentageSteps() {
		return configuration.getPercentageSteps();
	}

	public int getPollingTime() {
		return configuration.getPollingTime();
	}

	public int getEnginesTimeout() {
		return configuration.getEnginesTimeout();
	}

	public int getPercentageDeltaSteering() {
		return configuration.getPercentageDeltaSteering();
	}

	public String getLog() {
		return log != null ? log : "";
	}

	public void setLog(String log) {
		this.log = log;
	}

	public void onPollingTimeout() {
		if (!cockpit.isLockActiveForLoggedUser()) {
			reinitialize();
			messages.adminForcedUnlock();
			redirectToLogout();
			return;
		}

		if (isEngine()) {
			runEngineTimeout();
		} else if (timer != null) {
			timer.cancel();
		}
	}

	private void runEngineTimeout() {
		if (isEngine()) {
			String r = "started";
			if (timer != null) {
				timer.cancel();
				r = "restarted";
			}
			timer = new Timer();
			enginesOffTimeout = new EnginesOffTimeout();
			enginesOffTimeout.setConfiguration(configuration);
			enginesOffTimeout.setGpio(raspiPi2Gpio.getGpioController());
			enginesOffTimeout.setDirLeftBack(raspiPi2Gpio.getDirLeftBack());
			enginesOffTimeout.setDirLeftFront(raspiPi2Gpio.getDirLeftFront());
			enginesOffTimeout.setDirRightBack(raspiPi2Gpio.getDirRightBack());
			enginesOffTimeout.setDirRightFront(raspiPi2Gpio.getDirRightFront());
			enginesOffTimeout.setPwmLeft(raspiPi2Gpio.getPWM_LEFT());
			enginesOffTimeout.setPwmRight(raspiPi2Gpio.getPWM_RIGHT());
			enginesOffTimeout.setEngine(engines.isEngine());
			timer.schedule(enginesOffTimeout, 0L, configuration.getEnginesTimeout() * 1000);
			System.out.println(r + " countdown to expiring session.");
		}
	}
	
	public String getOs() {
		return System.getProperty("os.name");
	}
	
	public String getArch() {
		return System.getProperty("os.arch");
	}

	public String getRaspberryPrivateIP() throws SocketException {
		return configuration.getInterfaceIP();
	}

	public String getRaspberryPublicIP() throws MalformedURLException, IOException {
		return configuration.getPublicIP();
	}

	public boolean isAllForward() {
		return engines.isAllForward();
	}

	public void setAllForward(boolean allForward) {
		engines.setAllForward(allForward);
	}

	public void setAll(int speed) {
		setLeft(getLeft() + speed);
		setRight(getRight() + speed);
	}

	public void clearLog() {
		log = "";
	}

	public boolean isCameraLog() {
		return cameraLog;
	}

	public void setCameraLog(boolean cameraLog) {
		this.cameraLog = cameraLog;
	}

	public void cameraLog(String cameraLog) {
		if ("true".equals(cameraLog)) {
			setCameraLog(true);
		} else {
			setCameraLog(false);
		}
	}

	public void unlockCockpit() {
		cockpit.anyLockRemove();
		cockpit.activateLock();
	}

	private void redirectToLogout() {
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		try {
			context.redirect(context.getRequestContextPath() + "/" + LOGOUT_PAGE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void onOffLight() throws InterruptedException{
		raspiPi2Gpio.getLightRelay().high();
		Thread.sleep(500);
		raspiPi2Gpio.getLightRelay().low();
		System.out.println("on-off light");
	}
}