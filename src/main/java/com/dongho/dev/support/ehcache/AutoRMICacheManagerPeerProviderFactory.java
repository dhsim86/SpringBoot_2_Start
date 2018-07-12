package com.dongho.dev.support.ehcache;

import com.dongho.dev.common.NetUtils;
import lombok.extern.slf4j.Slf4j;
import net.sf.ehcache.distribution.CacheManagerPeerProvider;
import net.sf.ehcache.distribution.RMICacheManagerPeerProviderFactory;
import net.sf.ehcache.util.PropertyUtil;
import org.apache.commons.lang3.StringUtils;

import java.util.Properties;
import java.util.StringTokenizer;

@Slf4j
public class AutoRMICacheManagerPeerProviderFactory extends RMICacheManagerPeerProviderFactory {

	// From net.sf.ehcache.distribution.RMICacheManagerPeerProviderFactory.RMI_URLS
	private static final String RMI_URLS = "rmiUrls";

	// From net.sf.ehcache.distribution.PayloadUtil.URL_DELIMITER
	public static final String URL_DELIMITER = "|";

	private boolean isValidRmiUrls(String rmiUrls) {
		if (rmiUrls == null || rmiUrls.length() == 0) {
			return false;
		}

		return true;
	}

	private String getPeerRmiUrls(String rmiUrls) {

		rmiUrls = rmiUrls.trim();

		StringTokenizer stringTokenizer = new StringTokenizer(rmiUrls, URL_DELIMITER);
		StringBuilder peerRmiUrlsBuilder = new StringBuilder();

		String hostName = NetUtils.getHostName();
		String ipv4Address = NetUtils.getIpv4Address();

		while(stringTokenizer.hasMoreTokens() == true) {
			String rmiUrl = stringTokenizer.nextToken();
			rmiUrl = rmiUrl.trim();

			if (StringUtils.contains(rmiUrl, hostName) == false && StringUtils.contains(rmiUrl, ipv4Address) == false) {
				peerRmiUrlsBuilder.append(rmiUrl);

				if (stringTokenizer.hasMoreTokens() == true) {
					peerRmiUrlsBuilder.append(URL_DELIMITER);
				}
			}
		}

		return peerRmiUrlsBuilder.toString();
	}

	/**
	 * peerDiscovery=manual, rmiUrls=//hostname:port/cacheName //hostname:port/cacheName //hostname:port/cacheName
	 */
	@Override
	protected CacheManagerPeerProvider createManuallyConfiguredCachePeerProvider(Properties properties) {

		String rmiUrls = PropertyUtil.extractAndLogProperty(RMI_URLS, properties);

		if (isValidRmiUrls(rmiUrls) == false) {
			log.info("Starting manual peer provider with empty list of peers. " +
					 "No replication will occur unless peers are added.");
			rmiUrls = "";
		}

		String otherRmiUrls = getPeerRmiUrls(rmiUrls);
		properties.setProperty(RMI_URLS, otherRmiUrls);
		return super.createManuallyConfiguredCachePeerProvider(properties);
	}
}
