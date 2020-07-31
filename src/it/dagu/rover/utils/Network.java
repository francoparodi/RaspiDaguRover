package it.dagu.rover.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLConnection;
import java.util.Enumeration;

public class Network {
	private static final String CHARSET = "UTF-8";
	private static final String REQUEST_BROWSERS = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11";
	
	public static String retriveExternalIP(String urlService) {
		String ip = "";
		try {
			URLConnection connection = new java.net.URL(urlService).openConnection();
			connection.setRequestProperty("User-Agent", REQUEST_BROWSERS);
			connection.connect();
			BufferedReader r = new BufferedReader(new java.io.InputStreamReader(connection.getInputStream(),
					java.nio.charset.Charset.forName(CHARSET)));

			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = r.readLine()) != null) {
				sb.append(line);
			}
			ip = sb.toString();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return ip;
	}

	public static String retriveInterfaceIP(String interfaceName) {
		String ip = "";

		try {
			Enumeration<NetworkInterface> eni = NetworkInterface.getNetworkInterfaces();
			Enumeration<InetAddress> inetAddresses;
			while (eni.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) eni.nextElement();
				if(interfaceName.equals(ni.getName())){
					inetAddresses = ni.getInetAddresses();
					while(inetAddresses.hasMoreElements()){
						InetAddress ia = (InetAddress) inetAddresses.nextElement();
						if (!(ia instanceof Inet6Address)) {
							ip = ia.getHostAddress();
							return ip;
						}
					}
					
				}

			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		return ip;
	}
}
