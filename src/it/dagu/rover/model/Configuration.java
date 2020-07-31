package it.dagu.rover.model;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;

import it.dagu.rover.utils.Network;

@SessionScoped
public class Configuration implements Serializable {

	private static final long serialVersionUID = 1L;
	private static final String FILENAME_PROPS = "/WEB-INF/pidagurover.properties";
	private static final String UPPER_LIMIT_ENGINES = "upperLimitEngines";
	private static final String PERCENTAGE_STEPS = "percentageSteps";
	private static final String PERCENTAGE_DELTA_STEERING = "percentageDeltaSteering";
	private static final String POLLING_TIME = "pollingTime";
	private static final String ENGINES_TIMEOUT = "enginesTimeout";
	private static final String IP_SERVICE_DETECTOR = "publicIpServiceDetector";
	private static final String NET_INTERFACE = "netInterface";
	private static final String CAMERA_PORT = "cameraPort";
	private static final String CAMERA_URL = "cameraUrl";
	private static final String CAMERA_PROTOCOL = "http://";
	private static final String USERS = "users";
	private int upperLimitEngines;
	private int percentageSteps;
	private int percentageDeltaSteering;
	private int pollingTime;
	private int enginesTimeout;
	private String publicIpServiceDetector;
	private String netInterface;
	private String cameraUrl;
	private int cameraPort;
	private Map<String, UserLogged> users;

	public Configuration() {
		readConfiguration();
	}

	@SuppressWarnings("unused")
	private void writeConfiguration() {
		Properties prop = new Properties();
		OutputStream output = null;
		try {
			output = new FileOutputStream("config.properties");
			prop.setProperty("database", "localhost");
			prop.setProperty("dbuser", "mkyong");
			prop.setProperty("dbpassword", "password");
			prop.store(output, null);
		} catch (IOException io) {
			io.printStackTrace();

			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} finally {
			if (output != null) {
				try {
					output.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private void readConfiguration() {
		Properties prop = new Properties();
		InputStream input = null;
		try {
			input = FacesContext.getCurrentInstance().getExternalContext().getResourceAsStream(FILENAME_PROPS);
			prop.load(input);
			setUpperLimitEngines(getIntegerPropByName(prop, UPPER_LIMIT_ENGINES));
			setPercentageSteps(getIntegerPropByName(prop, PERCENTAGE_STEPS));
			setPercentageDeltaSteering(getIntegerPropByName(prop, PERCENTAGE_DELTA_STEERING));
			setPollingTime(getIntegerPropByName(prop, POLLING_TIME));
			setEnginesTimeout(getIntegerPropByName(prop, ENGINES_TIMEOUT));
			setPublicIpServiceDetector(getStringPropByName(prop, IP_SERVICE_DETECTOR));
			setNetInterface(getStringPropByName(prop, NET_INTERFACE));
			setCameraPort(getIntegerPropByName(prop, CAMERA_PORT));
			setCameraUrl(
					CAMERA_PROTOCOL + getInterfaceIP(getStringPropByName(prop, CAMERA_URL)) + ":" + getCameraPort());
			setUsers(getMapPropByName(prop, USERS));
		} catch (IOException ex) {
			ex.printStackTrace();

			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

//	private boolean getBooleanPropByName(Properties propFile, String propName) {
//		boolean value = false;
//		if ((!"".equals(propName)) && (propName != null) && (propFile.getProperty(propName) != null)
//				&& (!"".equals(propFile.getProperty(propName)))) {
//			value = propFile.getProperty(propName).equalsIgnoreCase("true");
//		}
//		return value;
//	}

	private int getIntegerPropByName(Properties propFile, String propName) {
		int value = 0;
		if ((!"".equals(propName)) && (propName != null) && (propFile.getProperty(propName) != null)
				&& (!"".equals(propFile.getProperty(propName)))) {
			value = Integer.valueOf(propFile.getProperty(propName)).intValue();
		}
		return value;
	}

	private String getStringPropByName(Properties propFile, String propName) {
		String value = "";
		if ((!"".equals(propName)) && (propName != null) && (propFile.getProperty(propName) != null)
				&& (!"".equals(propFile.getProperty(propName)))) {
			value = propFile.getProperty(propName);
			if ("netInterface".equals(value)) {
				value = getNetInterface();
			}
		}
		return value;
	}

	private Map<String, UserLogged> getMapPropByName(Properties propFile, String propName) {
		users = new HashMap<String, UserLogged>();
		if ((!"".equals(propName)) && (propName != null) && (propFile.getProperty(propName) != null)
				&& (!"".equals(propFile.getProperty(propName)))) {
			String[] usrPwd = propFile.getProperty(propName).split(",");
			for (int i = 0; i < usrPwd.length; i++) {
				String name = usrPwd[i].substring(0, usrPwd[i].indexOf(":"));
				String password = usrPwd[i].substring(usrPwd[i].indexOf(":") + 1);
				UserLogged user = new UserLogged();
				user.setName(name);
				user.setPassword(password);
				users.put(name, user);
			}
		}

		return users;
	}

	public int getUpperLimitEngines() {
		return upperLimitEngines;
	}

	public void setUpperLimitEngines(int upperLimitEngines) {
		this.upperLimitEngines = upperLimitEngines;
	}

	public int getPercentageSteps() {
		return percentageSteps;
	}

	public void setPercentageSteps(int percentageSteps) {
		this.percentageSteps = percentageSteps;
	}

	public int getPollingTime() {
		return pollingTime;
	}

	public void setPollingTime(int pollingTime) {
		this.pollingTime = pollingTime;
	}

	public String getCameraUrl() {
		return cameraUrl;
	}

	public void setCameraUrl(String cameraUrl) {
		this.cameraUrl = cameraUrl;
	}

	public boolean isPiExists() {
		return ("Linux".equalsIgnoreCase(System.getProperty("os.name")) &&
				"arm".equalsIgnoreCase(System.getProperty("os.arch")) );
	}

	public int getEnginesTimeout() {
		return enginesTimeout;
	}

	public void setEnginesTimeout(int enginesTimeout) {
		this.enginesTimeout = enginesTimeout;
	}

	public int getPercentageDeltaSteering() {
		return percentageDeltaSteering;
	}

	public void setPercentageDeltaSteering(int percentageDeltaSteering) {
		this.percentageDeltaSteering = percentageDeltaSteering;
	}

	public String toString() {
		String s = "piExists:" + isPiExists() + " " + "upperLimitEngines:" + getUpperLimitEngines() + " "
				+ "percentageSteps:" + getPercentageSteps() + " " + "percentageDeltaSteering:"
				+ getPercentageDeltaSteering() + " " + "pollingTime:" + getPollingTime() + " " + "enginesTimeout:"
				+ getEnginesTimeout() + " " + "netInterface:" + getNetInterface() + " " + "cameraUrl:" + getCameraUrl()
				+ " " + "cameraPort:" + getCameraPort() + " ";
		return s;
	}

	public Map<String, UserLogged> getUsers() {
		return users;
	}

	public void setUsers(Map<String, UserLogged> users) {
		this.users = users;
	}

	public String getInterfaceIP(String interfaceName) {
		return Network.retriveInterfaceIP(interfaceName);
	}

	public String getPublicIP() {
		return Network.retriveExternalIP(getPublicIpServiceDetector());
	}

	public String getInterfaceIP() throws java.net.SocketException {
		return getInterfaceIP(getNetInterface());
	}

	public int getCameraPort() {
		return cameraPort;
	}

	public void setCameraPort(int cameraPort) {
		this.cameraPort = cameraPort;
	}

	public String getNetInterface() {
		return netInterface;
	}

	public void setNetInterface(String netInterface) {
		this.netInterface = netInterface;
	}

	public String getPublicIpServiceDetector() {
		return publicIpServiceDetector;
	}

	public void setPublicIpServiceDetector(String publicIpServiceDetector) {
		this.publicIpServiceDetector = publicIpServiceDetector;
	}
}
