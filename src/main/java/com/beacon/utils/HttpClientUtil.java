package com.beacon.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.List;

/**
 * Created with Simple_love
 * Date: 2016/7/5.
 * Time: 16:15
 */
public class HttpClientUtil {
        public static final String JSON_TYPE = "json";
        private static final int MAX_TOTAL_CONNECTIONS = 800;
        private static final int WAIT_TIMEOUT = 30*1000;
        private static final int MAX_ROUT_CONNECTIONS = 400;
        private static final int CONNECT_TIMEOUT = 30*1000;
        private static final int READ_TIMEOUT = 300000;
        private static final String JSON_UTF8 = "application/json;charset=UTF-8";

        private static BasicHttpParams httpParams = null;
        private static DefaultHttpClient httpClient = null;
        private static ThreadSafeClientConnManager clientConnectionManager = null;
        private static final Logger logger = Logger.getLogger(HttpClientUtil.class);

        static {
                try {
                        httpParams = new BasicHttpParams();
                        httpParams.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECT_TIMEOUT);
                        HttpConnectionParams.setConnectionTimeout(httpParams, CONNECT_TIMEOUT);
                        HttpConnectionParams.setSoTimeout(httpParams, WAIT_TIMEOUT);
                        SchemeRegistry registry = new SchemeRegistry();
                        registry.register(new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
                        SSLContext sslContext = SSLContext.getInstance("SSL");
                        sslContext.init(null, new TrustManager[]{}, null);
                        SSLSocketFactory sslSocketFactory = new SSLSocketFactory(sslContext,SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
                        registry.register(new Scheme("https",443,sslSocketFactory));
                        clientConnectionManager = new ThreadSafeClientConnManager(registry);
                        clientConnectionManager.setDefaultMaxPerRoute(MAX_ROUT_CONNECTIONS);
                        clientConnectionManager.setMaxTotal(MAX_TOTAL_CONNECTIONS);
                        httpClient = new DefaultHttpClient(clientConnectionManager,httpParams);
                } catch (Exception e) {
                        e.printStackTrace();
                }
        }

        public static HttpClient getHttpClient() {
                return httpClient;
        }

        public static void release() {
                if(clientConnectionManager != null) {
                        clientConnectionManager.shutdown();
                }

        }

        public static String httpclientRequest(String requestType, String requestUrl, String requestStr) throws Exception {
                StringEntity strEntity;
                //requestType = requestType == null ? JSON_UTF8 : requestType;
                try {
                        strEntity = new StringEntity(requestStr, "UTF-8");
                        BasicHeader e = new BasicHeader("Content-Type", JSON_UTF8);
                        strEntity.setContentType(e);
                } catch (Exception var5) {
                        var5.printStackTrace();
                        throw new Exception("response unsupportedEncodingException");
                }

                return httpclientRequest((HttpEntity)strEntity, requestUrl);
        }

        public static String httpclientRequest(List<NameValuePair> nameValuePairList, String requestUrl) throws Exception {
                UrlEncodedFormEntity uefEntity;
                try {
                        uefEntity = new UrlEncodedFormEntity(nameValuePairList, "UTF-8");
                } catch (Exception var4) {
                        var4.printStackTrace();
                        throw new Exception("response unsupportedEncodingException");
                }

                return httpclientRequest((HttpEntity)uefEntity, requestUrl);
        }

        private static String httpclientRequest(HttpEntity httpEntity, String requestUrl) throws Exception {
                HttpClient httpClient = getHttpClient();
                HttpPost httpPost = new HttpPost(requestUrl);

                String var7;
                try {
                        httpPost.setEntity(httpEntity);
                        HttpResponse e = httpClient.execute(httpPost);
                        int code = e.getStatusLine().getStatusCode();
                        logger.debug("response status: " + e.getStatusLine());
                        if(code != 200) {
                                return null;
                        }

                        var7 = EntityUtils.toString(e.getEntity(), "UTF-8");
                } catch (Exception var10) {
                        var10.printStackTrace();
                        throw new Exception("response exception");
                } finally {
                        if(!httpPost.isAborted()) {
                                httpPost.abort();
                        }

                }

                return var7;
        }

        private static class TrustAnyTrustManager implements X509TrustManager {
                private TrustAnyTrustManager() {
                }

                public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
                }

                public X509Certificate[] getAcceptedIssuers() {
                        return new X509Certificate[0];
                }
        }
}
