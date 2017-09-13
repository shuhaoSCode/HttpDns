package com.sina.util.dnscache.dnsutils;

import android.util.Log;

import com.sina.util.dnscache.DNSCache;
import com.sina.util.dnscache.DomainInfo;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import okhttp3.Dns;

/**
 * Created by ming.o on 2017/9/12.
 */

public class HttpDNS implements Dns {
    private final Dns SYSTEM = Dns.SYSTEM;

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        Log.d("check", hostname);
        DNSCache dnsCache = DNSCache.getInstance();
        String ip = InetAddress.getByName(hostname).getHostAddress();
        Log.d("check", ip);
        DomainInfo[] domainInfos = dnsCache.getDomainServerIp(hostname);

        List<InetAddress> inetAddresses = new ArrayList<InetAddress>();
        for (DomainInfo info : domainInfos) {
            inetAddresses.addAll(Arrays.asList(InetAddress.getAllByName(info.url)));
        }
        return inetAddresses.size() > 0 ? inetAddresses : SYSTEM.lookup(hostname);
    }

}
