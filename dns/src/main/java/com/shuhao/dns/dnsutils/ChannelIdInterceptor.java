package com.shuhao.dns.dnsutils;

import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;

/**
 * Created by ming.o on 2017/9/8.
 */

public class ChannelIdInterceptor implements Interceptor {
    private Context context; //This is here because I needed it for some other cause

    //private static final String TOKEN_IDENTIFIER = "token_id";
    private String id = "";

    public ChannelIdInterceptor(Context context, String id) {
        this.context = context;
        this.id = id;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        RequestBody requestBody = request.body();
        String channelId = id;//whatever or however you get it.
        String subtype = requestBody.contentType().subtype();
        Log.d("subtype", subtype);
        if ("".equals(channelId)) {
            if (subtype.contains("json")) {
                requestBody = processApplicationJsonRequestBody(requestBody, channelId);
            } else if (subtype.contains("form")) {
                requestBody = processFormDataRequestBody(requestBody, channelId);
            }
            if (requestBody != null) {
                Request.Builder requestBuilder = request.newBuilder();
                request = requestBuilder
                        .post(requestBody)
                        .build();
            }
        }
        Log.d("subtype", request.body().contentType().toString());
        return chain.proceed(request);
    }

    private String bodyToString(final RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final IOException e) {
            return "did not work";
        }
    }

    private RequestBody processApplicationJsonRequestBody(RequestBody requestBody, String token) {
        String customReq = bodyToString(requestBody);
        try {
            JSONObject obj = new JSONObject(customReq);
            obj.put("channelId ", token);
            return RequestBody.create(requestBody.contentType(), obj.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private RequestBody processFormDataRequestBody(RequestBody requestBody, String token) {
        RequestBody formBody = new FormBody.Builder()
                .add("channelId ", token)
                .build();
        String postBodyString = bodyToString(requestBody);
        postBodyString += ((postBodyString.length() > 0) ? "&" : "") + bodyToString(formBody);
        Log.d("subtype", postBodyString);
        return RequestBody.create(requestBody.contentType(), postBodyString);
    }

}