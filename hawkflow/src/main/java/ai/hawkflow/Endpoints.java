package ai.hawkflow;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

final class Endpoints {
    /**
     * @param process the process name
     * @param meta the meta name
     * @param uid the uid if you are sending duplicate process meta at the same time
     * @return
     * @throws HawkFlowDataTypesException
     */
    public static JSONObject timedData(String process, String meta, String uid) throws HawkFlowDataTypesException {
        Validation.validateTimedData(process, meta, uid);

        JSONObject map = new JSONObject();
        map.put("process", process);
        map.put("meta", meta);
        map.put("uid", uid);
        return map;
    }

    /**
     * @param process the process name
     * @param meta the meta name
     * @param items the items for metrics api
     * @return
     * @throws HawkFlowDataTypesException
     */
    public static JSONObject metricData(String process, String meta, ArrayList<HashMap<String, Float>> items) throws HawkFlowDataTypesException {
        Validation.validateMetricData(process, meta, items);

        JSONArray jsonArray = new JSONArray();
        for (HashMap<String, Float> map : items) {
            JSONObject jsonObject = new JSONObject();
            for (Map.Entry<String, Float> entry : map.entrySet()) {
                try {
                    jsonObject.put(entry.getKey(), entry.getValue());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            jsonArray.put(jsonObject);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("process", process);
        jsonObject.put("meta", meta);
        jsonObject.put("items", jsonArray);

        return jsonObject;
    }

    /**
     * @param process the process name
     * @param meta the meta name
     * @param exceptionText the exception text
     * @return
     * @throws HawkFlowDataTypesException
     */
    public static JSONObject exceptionData(String process, String meta, String exceptionText) throws HawkFlowDataTypesException {
        Validation.validateExceptionData(process, meta, exceptionText);

        JSONObject map = new JSONObject();
        map.put("process", process);
        map.put("meta", meta);
        map.put("exception", exceptionText);
        return map;
    }
}