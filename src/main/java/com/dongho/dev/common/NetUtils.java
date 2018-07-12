package com.dongho.dev.common;

import lombok.extern.slf4j.Slf4j;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

@Slf4j
public class NetUtils {

	private static InetAddress getInet4Address() {

		Inet4Address inet4Address = null;

		try {
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface iface = interfaces.nextElement();
				// filters out 127.0.0.1 and inactive interfaces
				if (iface.isLoopback() || !iface.isUp())
					continue;

				Enumeration<InetAddress> addresses = iface.getInetAddresses();
				while (addresses.hasMoreElements()) {
					InetAddress addr = addresses.nextElement();

					// *EDIT*
					if (addr instanceof Inet4Address) {
						inet4Address = Inet4Address.class.cast(addr);
					}
				}
			}
		} catch (SocketException e) {
			throw new RuntimeException(e);
		}

		return inet4Address;
	}

	public static String getIpv4Address() {

		InetAddress inetAddress = getInet4Address();

		if (inetAddress == null) {
			return "";
		}

		log.debug("Ip Address: {}", inetAddress.getHostAddress());
		return inetAddress.getHostAddress();
	}

	public static String getHostName() {

		InetAddress inetAddress = getInet4Address();

		if (inetAddress == null) {
			return "";
		}

		log.debug("Hostname: {}", inetAddress.getHostName());
		return inetAddress.getHostName();
	}
}
