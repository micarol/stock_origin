package com.micarol.stock.util;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

public class OkHttpUtil {

    static Logger log = LoggerFactory.getLogger(OkHttpUtil.class.getName());

    private final static OkHttpClient client = new OkHttpClient();
    
    private final static OkHttpClient shortClient = new OkHttpClient();

    static {
        client.setReadTimeout(5, TimeUnit.MINUTES);
        client.setWriteTimeout(5, TimeUnit.MINUTES);
        client.setConnectTimeout(20, TimeUnit.SECONDS);
        
        shortClient.setReadTimeout(5, TimeUnit.SECONDS);
        shortClient.setWriteTimeout(1, TimeUnit.MINUTES);
        shortClient.setConnectTimeout(5, TimeUnit.SECONDS);
        
    }

    private OkHttpUtil() {
    }

    /**
     * http get
     *
     * @param url
     * @return null if http request fail
     */
    public static String get(String url) {
        final long startTime = System.currentTimeMillis();
        Request request = new Request.Builder().url(url).build();
        Response response;
        try {
            response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            log.error("http get throw Exception, url: {}, exception: {}", new Object[]{url, e.getMessage()});
            log.info("api={}, params={}, cost {} ms", url, "", (System.currentTimeMillis() - startTime));
        }

        return null;
    }

    public static String get(String url, Map<String, String> params) {
        final long startTime = System.currentTimeMillis();
        if (params != null && !params.isEmpty()) {
            StringBuilder paramsString = new StringBuilder();
            for (String key : params.keySet()) {
                paramsString.append("&").append(key).append("=").append(params.get(key));
            }

            url = url + "?" + paramsString.substring(1);
        }
        Request request = new Request.Builder().url(url).build();

        try {
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (IOException e) {
            log.error("http get throw Exception, url: {}, exception: {}", new Object[]{url, e.getMessage()});
            log.info("api={}, params={}, cost {} ms", url, params, (System.currentTimeMillis() - startTime));
        }

        return null;
    }

    /**
     * http post
     *
     * @param url
     * @param params
     * @return null if http request fail
     * @throws IOException
     */
    public static String post(String url, Map<String, Object> params, boolean isJsonBody) throws IOException {
        final long startTime = System.currentTimeMillis();
        try {
            RequestBody body;

            if (isJsonBody) {
                body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JsonUtil.obj2JsonStr(params));
            } else {
                FormEncodingBuilder formBuilder = new FormEncodingBuilder();
                if (params != null && !params.isEmpty()) {
                    Iterator<Entry<String, Object>> iterator = params.entrySet().iterator();
                    Entry<String, Object> entry = null;
                    while (iterator.hasNext()) {
                        entry = iterator.next();
                        formBuilder.add(entry.getKey(), entry.getValue().toString());
                    }
                }

                body = formBuilder.build();
            }

            Request request = new Request.Builder().url(url).post(body).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }

            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            if (url.endsWith("media/upload")) {
                Map<String, Object> printParam = new HashMap<>(params);
                printParam.put("content", "太长了, 禁止打印");
                log.info("api={}, params={}, isJson={}, cost {} ms", url, printParam, isJsonBody, (System.currentTimeMillis() - startTime));
            } else {
                log.info("api={}, params={}, isJson={}, cost {} ms", url, params, isJsonBody, (System.currentTimeMillis() - startTime));
            }
        }
    }
    
    public static String shortPost(String url, Map<String, Object> params, boolean isJsonBody) throws IOException {
        final long startTime = System.currentTimeMillis();
        try {
            RequestBody body;

            if (isJsonBody) {
                body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), JsonUtil.obj2JsonStr(params));
            } else {
                FormEncodingBuilder formBuilder = new FormEncodingBuilder();
                if (params != null && !params.isEmpty()) {
                    Iterator<Entry<String, Object>> iterator = params.entrySet().iterator();
                    Entry<String, Object> entry = null;
                    while (iterator.hasNext()) {
                        entry = iterator.next();
                        formBuilder.add(entry.getKey(), entry.getValue().toString());
                    }
                }

                body = formBuilder.build();
            }

            Request request = new Request.Builder().url(url).post(body).build();
            Response response = shortClient.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }

            return null;
        } catch (IOException e) {
        	log.info("api={}, e={}", url, e.getMessage());
            throw e;
        } finally {
            log.info("api={}, params={}, isJson={}, cost {} ms", url, params, isJsonBody, (System.currentTimeMillis() - startTime));
        }
    }

    public static String upload(String url, Map<String, Object> params, File file) throws IOException {
        final long startTime = System.currentTimeMillis();
        try {
            MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM)
                    .addFormDataPart("package", "file", RequestBody.create(MediaType.parse("text/plain;"), file));

            if (params != null && !params.isEmpty()) {
                Iterator<Entry<String, Object>> iterator = params.entrySet().iterator();
                Entry<String, Object> entry = null;
                while (iterator.hasNext()) {
                    entry = iterator.next();
                    builder.addFormDataPart(entry.getKey(), entry.getValue().toString());
                }
            }

            Request request = new Request.Builder().url(url).post(builder.build()).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }

            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("api={}, params={}, file={}, cost {} ms", url, params, file, (System.currentTimeMillis() - startTime));
        }

    }

    public static String fileUpload(String url, Map<String, Object> params, File file) throws IOException {
        final long startTime = System.currentTimeMillis();
        try {
            MultipartBuilder builder = new MultipartBuilder().type(MultipartBuilder.FORM)
                    .addFormDataPart("file", file.getName(), RequestBody.create(MediaType.parse("text/plain;"), file));
            if (params != null && !params.isEmpty()) {
                Iterator<Entry<String, Object>> iterator = params.entrySet().iterator();
                Entry<String, Object> entry = null;
                while (iterator.hasNext()) {
                    entry = iterator.next();
                    builder.addFormDataPart(entry.getKey(), entry.getValue().toString());
                }
            }

            Request request = new Request.Builder().url(url).post(builder.build()).build();
            Response response = client.newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }

            return null;
        } catch (Exception e) {
            throw e;
        } finally {
            log.info("api={}, params={}, file={}, cost {} ms", url, params, file, (System.currentTimeMillis() - startTime));
        }

    }
}
