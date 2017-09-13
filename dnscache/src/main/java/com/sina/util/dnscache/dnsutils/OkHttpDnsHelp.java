package com.sina.util.dnscache.dnsutils;

import android.content.Context;
import android.util.Log;

import com.sina.util.dnscache.DNSCache;
import com.sina.util.dnscache.DomainInfo;
import com.zhy.http.okhttp.log.LoggerInterceptor;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Dns;
import okhttp3.OkHttpClient;

/**
 * Created by luke on 2017/9/12.
 */

public class OkHttpDnsHelp {

    public static OkHttpClient.Builder initOkHttp(Context context, String channeId, long connectTimeout, long readTimeout) {
        return new OkHttpClient.Builder()
                .dns(new Dns() {
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
                })
                .addInterceptor(new ChannelIdInterceptor(context, channeId))
                .addInterceptor(new LoggerInterceptor("TAG", true))
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS);
    }

    public static OkHttpClient.Builder initOkHttp(Context context, String channeId, long connectTimeout, long readTimeout, Dns dns) {
        return new OkHttpClient.Builder()
                .dns(dns)
                .addInterceptor(new ChannelIdInterceptor(context, channeId))
                .addInterceptor(new LoggerInterceptor("TAG", true))
                .connectTimeout(connectTimeout, TimeUnit.MILLISECONDS)
                .readTimeout(readTimeout, TimeUnit.MILLISECONDS);
    }

}
