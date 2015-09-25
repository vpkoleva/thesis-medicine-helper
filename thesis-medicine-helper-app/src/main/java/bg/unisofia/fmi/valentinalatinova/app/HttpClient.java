package bg.unisofia.fmi.valentinalatinova.app;

import bg.unisofia.fmi.valentinalatinova.core.dto.AuthTokenDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Serializable;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public class HttpClient implements Serializable {

    private ObjectMapper jsonObjectMapper;
    private AuthTokenDto authToken;
    private boolean acceptAllCertificates = true;
    private String endpointUrl;
    private String username;
    private String password;

    public HttpClient() {
        jsonObjectMapper = new ObjectMapper();
    }

    public <T> T get(String path, Class<T> entityResponseClass) {
        try {
            final String url = endpointUrl + path;
            String resultJson = doRequest(url, null);
            return jsonObjectMapper.readValue(resultJson, entityResponseClass);
        } catch (KeyManagementException | NoSuchAlgorithmException | IOException ex) {
            Logger.error(ex);
            return null;
        }
    }

    public <E, T> T post(String path, E entityToPost, Class<T> entityResponseClass) {
        try {
            final String url = endpointUrl + path;
            String request = jsonObjectMapper.writeValueAsString(entityToPost);
            String resultJson = doRequest(url, request);
            return jsonObjectMapper.readValue(resultJson, entityResponseClass);
        } catch (KeyManagementException | NoSuchAlgorithmException | IOException ex) {
            Logger.error(ex);
            return null;
        }
    }

    public boolean authenticate() {
        try {
            final String url = endpointUrl + "/token";
            final String data = "grant_type=password&username=" + username + "&password=" + password;
            String resultJson = doRequest(url, data);
            authToken = jsonObjectMapper.readValue(resultJson, AuthTokenDto.class);
            return true;
        } catch (KeyManagementException | NoSuchAlgorithmException | IOException ex) {
            Logger.error(ex);
            return false;
        }
    }

    public boolean isAuthenticated() {
        return authToken != null && authToken.getExpiryDate().isAfterNow();
    }

    public void setEndpointUrl(String endpointUrl) {
        this.endpointUrl = endpointUrl;
    }

    public void setAcceptAllCertificates(boolean acceptAllCertificates) {
        this.acceptAllCertificates = acceptAllCertificates;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Private methods
    private String doRequest(String endpointUrl, String data)
            throws IOException, KeyManagementException, NoSuchAlgorithmException {
        URLConnection urlConnection = openUrlConnection(endpointUrl);
        urlConnection.setConnectTimeout(10000);
        if (authToken != null) {
            urlConnection.setRequestProperty("Authorization", "Bearer " + authToken.getAuthToken());
        }
        // This is POST
        if (data != null) {
            urlConnection.setDoOutput(true);
            // This is authentication request
            if (data.startsWith("grant_type")) {
                urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            } else {
                urlConnection.setRequestProperty("Content-Type", "application/json");
            }
            try (OutputStream output = urlConnection.getOutputStream()) {
                output.write(data.getBytes());
            }
        }
        InputStream response = urlConnection.getInputStream();
        StringBuilder result = new StringBuilder();
        try (InputStreamReader inputStreamReader = new InputStreamReader(response)) {
            try (BufferedReader bufferedReader = new BufferedReader(inputStreamReader)) {
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    result.append(line);
                }
            }
        }
        return result.toString();
    }

    private URLConnection openUrlConnection(String endpointUrl)
            throws IOException, NoSuchAlgorithmException, KeyManagementException {
        URL url = new URL(endpointUrl);
        if (endpointUrl.toLowerCase().startsWith("https")) {
            HttpsURLConnection result = (HttpsURLConnection) url.openConnection();
            // Accept of all certificates
            if (acceptAllCertificates) {
                // Trust all certificates
                TrustManager[] trustAllCerts = new TrustManager[] {
                        new X509TrustManager() {
                            public void checkClientTrusted(X509Certificate[] certs, String authType) {
                            }

                            public void checkServerTrusted(X509Certificate[] certs, String authType) {
                            }

                            public X509Certificate[] getAcceptedIssuers() {
                                return null;
                            }
                        }
                };
                SSLContext sslContext = SSLContext.getInstance("SSL");
                sslContext.init(null, trustAllCerts, new java.security.SecureRandom());
                HttpsURLConnection.setDefaultSSLSocketFactory(sslContext.getSocketFactory());
                // Trust all hosts
                result.setHostnameVerifier(new HostnameVerifier() {
                    public boolean verify(String hostname, SSLSession session) {
                        return true;
                    }
                });
            }
            return result;
        } else {
            return url.openConnection();
        }
    }
}
