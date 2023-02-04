package ai.hawkflow;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public final class HawkFlowApi {
    private static final String hawkFlowApiUrl = "https://api.hawkflow.ai/v1";
    private static final int MAX_RETRIES = 3;
    private static final int WAIT_TIME_SECONDS = 2;

    public static void metrics(String process, String meta, ArrayList<HashMap<String, Float>> items, String apiKey) {
        if(items == null) {
            items = new ArrayList<HashMap<String, Float>>();
        }

        try {
            String url = hawkFlowApiUrl + "/metrics";
            JSONObject data = Endpoints.metricData(process, meta, items);
            hawkFlowPost(url, data, apiKey);
        } catch(HawkFlowDataTypesException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void metrics(String process, String meta, ArrayList<HashMap<String, Float>> items) {
        metrics(process, meta, items, "");
    }

    public static void exception(String process, String meta, String exceptionText, String apiKey) {
        try {
            String url = hawkFlowApiUrl + "/exception";
            JSONObject data = Endpoints.exceptionData(process, meta, exceptionText);
            hawkFlowPost(url, data, apiKey);
        } catch(HawkFlowDataTypesException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void exception(String process, String meta, String exceptionText) {
        exception(process, meta, exceptionText, "");
    }

    public static void start(String process, String meta, String uid, String apiKey) {
        try {
            String url = hawkFlowApiUrl + "/timed/start";
            JSONObject data = Endpoints.exceptionData(process, meta, uid);
            hawkFlowPost(url, data, apiKey);
        } catch(HawkFlowDataTypesException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void start(String process, String meta, String uid) {
        start(process, meta, uid, "");
    }

    public static void start(String process, String meta) {
        start(process, meta, "", "");
    }

    public static void end(String process, String meta, String uid, String apiKey) {
        try {
            String url = hawkFlowApiUrl + "/timed/end";
            JSONObject data = Endpoints.exceptionData(process, meta, uid);
            hawkFlowPost(url, data, apiKey);
        } catch(HawkFlowDataTypesException ex) {
            System.out.println(ex.getMessage());
        }
    }

    public static void end(String process, String meta, String uid) {
        end(process, meta, uid, "");
    }

    public static void end(String process, String meta) {
        end(process, meta, "", "");
    }

    private static void hawkFlowPost(String url, JSONObject data, String apiKey) {
        try {
            Validation.validateApiKey(apiKey);
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        int retries = 0;
        boolean success = false;

        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        StringEntity entity = null;

        try {
            entity = new StringEntity(data.toString());
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        httpPost.setEntity(entity);
        httpPost.setHeader("Content-type", "application/json");
        httpPost.setHeader("hawkflow-api-key", apiKey);

        while (retries < MAX_RETRIES && !success) {
            try {
                CloseableHttpResponse response = httpClient.execute(httpPost);
                if (response.getStatusLine().getStatusCode() == 200) {
                    success = true;
                    String responseBody = EntityUtils.toString(response.getEntity());
                    System.out.println("Successful response: " + responseBody);
                } else {
                    System.out.println("Failed with status code: " + response.getStatusLine().getStatusCode());
                }
            } catch (ClientProtocolException e) {
                System.out.println("Client protocol exception: " + e.getMessage());
                e.printStackTrace();
            } catch (IOException e) {
                System.out.println("IOException: " + e.getMessage());
                e.printStackTrace();
            }
            if (!success) {
                retries++;
                if (retries < MAX_RETRIES) {
                    System.out.println("Retrying... (attempt " + retries + " of " + MAX_RETRIES + ")");
                    try {
                        TimeUnit.SECONDS.sleep(WAIT_TIME_SECONDS);
                    } catch (InterruptedException e) {
                        System.out.println("InterruptedException: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
        if (!success) {
            System.out.println("Failed after maximum number of retries");
        }
    }
}
