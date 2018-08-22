package com.hengyi.japp.fund.client;

import com.github.ixtf.japp.codec.Jcodec;
import com.github.ixtf.japp.core.J;
import okhttp3.*;

import javax.ws.rs.core.StreamingOutput;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.hengyi.japp.fund.client.Constant.TMP_PATH;

public class Api {
    public static final OkHttpClient OK_CLIENT = new OkHttpClient.Builder()
            .connectTimeout(1, TimeUnit.HOURS)
            .writeTimeout(1, TimeUnit.HOURS)
            .readTimeout(1, TimeUnit.HOURS)
            .build();
    public static final MediaType OK_JSON = MediaType.parse("application/json; charset=utf-8");

    public static Request buildGet(Principal principal, String url, Map<String, Object> paramMap) {
        String token = principal.getName();
        return new Request.Builder()
                .addHeader("authorization", "Bearer " + token)
                .url(queryUrl(url, paramMap))
                .build();
    }

    public static String reqToString(Request request) throws Exception {
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body()) {
                return body.string();
            }
        }
    }

    public static StreamingOutput download(Request request) throws Exception {
        try (Response response = OK_CLIENT.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new RuntimeException();
            }
            try (ResponseBody body = response.body();
                 InputStream is = body.byteStream()) {
                Path path = Paths.get(TMP_PATH, Jcodec.uuid58() + ".xlsx");
                Files.copy(is, path, StandardCopyOption.REPLACE_EXISTING);
                return (out) -> Files.copy(path, out);
            }
        }
    }

    public static String post(Principal principal, String url, String body) throws Exception {
        return post(principal, url, null, body);
    }

    public static String post(Principal principal, String url, Map<String, Object> paramMap, String body) throws Exception {
        String token = principal.getName();
        Request request = new Request.Builder()
                .addHeader("authorization", "Bearer " + token)
                .url(queryUrl(url, paramMap))
                .post(RequestBody.create(OK_JSON, body))
                .build();
        return reqToString(request);
    }

    public static String put(Principal principal, String url, String body) throws Exception {
        return put(principal, url, null, body);
    }

    public static String put(Principal principal, String url, Map<String, Object> paramMap, String body) throws Exception {
        String token = principal.getName();
        Request request = new Request.Builder()
                .addHeader("authorization", "Bearer " + token)
                .url(queryUrl(url, paramMap))
                .put(RequestBody.create(OK_JSON, body))
                .build();
        return reqToString(request);
    }

    public static String get(Principal principal, final String url) throws Exception {
        return get(principal, url, null);
    }

    public static String get(Principal principal, final String url, Map<String, Object> paramMap) throws Exception {
        String token = principal.getName();
        Request request = new Request.Builder()
                .addHeader("authorization", "Bearer " + token)
                .url(queryUrl(url, paramMap))
                .build();
        return reqToString(request);
    }

    public static void delete(Principal principal, String url) throws Exception {
        delete(principal, url, null);
    }

    public static void delete(Principal principal, final String url, Map<String, Object> paramMap) throws Exception {
        String token = principal.getName();
        Request request = new Request.Builder()
                .addHeader("authorization", "Bearer " + token)
                .url(queryUrl(url, paramMap))
                .delete()
                .build();
        reqToString(request);
    }

    public static String queryUrl(final String url, final Map<String, Object> paramMap) {
        if (J.isEmpty(paramMap)) {
            return url;
        }
        String queryParams = paramMap.entrySet()
                .parallelStream()
                .map(entry -> {
                    String key = entry.getKey();
                    Object o = entry.getValue();
                    return queryParam(key, o);
                })
                .filter(J::nonBlank)
                .collect(Collectors.joining("&"));
        return url + "?" + queryParams;
    }

    public static String queryParam(String key, Object o) {
        if (o == null) {
            return null;
        }
        if (o instanceof Collection) {
            Collection<?> collection = (Collection) o;
            if (J.isEmpty(collection)) {
                return null;
            }
            return collection.parallelStream()
                    .map(v -> queryParam(key, v))
                    .filter(J::nonBlank)
                    .collect(Collectors.joining("&"));
        }
        return key + "=" + o.toString();
    }

    public static class Urls {
        public static final String BASE_API_URL = "http://task.hengyi.com:8080/fund-server/api";
        public static final String AUTH = BASE_API_URL + "/auth";
        public static final String CORPORATIONS = BASE_API_URL + "/corporations";
        public static final String MONTHFUNDPLANS = BASE_API_URL + "/monthFundPlans";
        public static final String REPORTS_SUM_BALANCES = BASE_API_URL + "/reports/sum/balances";
        public static final String REPORTS_SUM_BALANCES_CONFIG = REPORTS_SUM_BALANCES + "/config";
        public static final String BALANCES = BASE_API_URL + "/balances";
        public static final String FUNDLIKES = BASE_API_URL + "/fundlikes";
        public static final String BATCH_FUNDLIKES = BASE_API_URL + "/batchFundlikes";
        public static final String EXPORTS = BASE_API_URL + "/exports";
    }
}
