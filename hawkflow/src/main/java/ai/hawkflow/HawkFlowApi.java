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

/**
 * HawkFlowApi
 */
public class HawkFlowApi {
    /**
     * hawkflow.ai api endpoint
     */
    private static final String hawkFlowApiUrl = "https://api.hawkflow.ai/v1";
    /**
     * your apikey
     */
    public String apiKey = "";
    /**
     * number of retries to send data
     */
    public int maxRetries = 3;
    /**
     * time to wait in milliseconds
     */
    public int waitTime = 100;

    /**
     * @param apiKey your apikey
     * @param maxRetries number of retries to send data
     * @param waitTime time to wait in milliseconds
     */
    public HawkFlowApi(String apiKey, int maxRetries, int waitTime) {
        this.apiKey = apiKey;
        this.maxRetries = maxRetries;
        this.waitTime = waitTime;
    }

    /**
     * @param apiKey your apikey
     */
    public HawkFlowApi(String apiKey) {
        this.apiKey = apiKey;
    }

    /**
     * HawkFlowApi
     */
    public HawkFlowApi() {
        this.apiKey = "";
    }

    /**
     * @param process the process name
     * @param meta the meta name
     * @param items the metric items
     */
    public void metrics(String process, String meta, ArrayList<HashMap<String, Float>> items) {
        if(items == null) {
            items = new ArrayList<HashMap<String, Float>>();
        }

        try {
            String url = hawkFlowApiUrl + "/metrics";
            JSONObject data = Endpoints.metricData(process, meta, items);
            this.hawkFlowPost(url, data);
        } catch(HawkFlowDataTypesException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * @param process the process name
     * @param meta the meta name
     * @param exceptionText
     */
    public void exception(String process, String meta, String exceptionText) {
        try {
            String url = hawkFlowApiUrl + "/exception";
            JSONObject data = Endpoints.exceptionData(process, meta, exceptionText);
            this.hawkFlowPost(url, data);
        } catch(HawkFlowDataTypesException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * @param process the process name
     * @param meta the meta name
     * @param uid the uid if you are sending duplicate process meta at the same time
     */
    public void start(String process, String meta, String uid) {
        try {
            String url = hawkFlowApiUrl + "/timed/start";
            JSONObject data = Endpoints.exceptionData(process, meta, uid);
            this.hawkFlowPost(url, data);
        } catch(HawkFlowDataTypesException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * @param process the process name
     * @param meta the meta name
     */
    public void start(String process, String meta)
    {
        this.start(process, meta, "");
    }

    /**
     * @param process the process name
     */
    public void start(String process)
    {
        this.start(process, "", "");
    }

    /**
     * @param process the process name
     * @param meta the meta name
     * @param uid the uid if you are sending duplicate process meta at the same time
     */
    public void end(String process, String meta, String uid) {
        try {
            String url = hawkFlowApiUrl + "/timed/end";
            JSONObject data = Endpoints.exceptionData(process, meta, uid);
            this.hawkFlowPost(url, data);
        } catch(HawkFlowDataTypesException ex) {
            System.out.println(ex.getMessage());
        }
    }

    /**
     * @param process the process name
     * @param meta the meta name
     */
    public void end(String process, String meta)
    {
        this.end(process, meta, "");
    }

    /**
     * @param process the process name
     */
    public void end(String process)
    {
        this.end(process, "", "");
    }

    /**
     * @param url the url of the endpoint
     * @param data the json data
     */
    private void hawkFlowPost(String url, JSONObject data) {
        try {
            this.apiKey = Validation.validateApiKey(this.apiKey);

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
            httpPost.setHeader("hawkflow-api-key", this.apiKey);

            while (retries < this.maxRetries && !success) {
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
                    if (retries < this.maxRetries) {
                        System.out.println("Retrying... (attempt " + retries + " of " + this.maxRetries + ")");
                        try {
                            TimeUnit.MILLISECONDS.sleep(this.waitTime);
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
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }
}
