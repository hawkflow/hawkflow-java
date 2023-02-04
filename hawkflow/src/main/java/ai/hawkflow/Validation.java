package ai.hawkflow;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

final class Validation {
    private static final String pattern = "[a-zA-Z0-9_-]+";
    public static void validateApiKey(String apiKey) throws HawkFlowNoApiKeyException, HawkFlowApiKeyFormatException {
        if(apiKey == null || apiKey.equals("")) {
            apiKey = System.getenv("HAWKFLOW_API_KEY");
        }

        if(apiKey == null || apiKey.equals("")) {
            throw new HawkFlowNoApiKeyException();
        }

        if (!apiKey.matches(pattern)) {
            throw new HawkFlowApiKeyFormatException();
        }

        if (apiKey.length() > 50) {
            throw new HawkFlowApiKeyFormatException();
        }
    }

    public static void validateTimedData(String process, String meta, String uid) throws HawkFlowDataTypesException {
        validateCore(process, meta);
        validateUid(uid);
    }

    public static void validateMetricData(String process, String meta, ArrayList<HashMap<String, Float>> items) throws HawkFlowDataTypesException {
        validateCore(process, meta);
        validateMetricItems(items);
    }

    public static void validateExceptionData(String process, String meta, String exceptionText) throws HawkFlowDataTypesException {
        validateCore(process, meta);
        validateExceptionText(exceptionText);
    }

    public static void validateCore(String process, String meta) throws HawkFlowDataTypesException {
        validateProcess(process);
        validateMeta(meta);
    }

    public static void validateProcess(String process) throws HawkFlowDataTypesException {
        if(process.length() > 249) {
            throw new HawkFlowDataTypesException("HawkFlow API process parameter exceeded max length of 300.");
        }

        if (!process.matches(pattern)) {
            throw new HawkFlowDataTypesException("HawkFlow API process parameter incorrect format.");
        }
    }

    public static void validateMeta(String meta) throws HawkFlowDataTypesException {
        if(meta.length() > 499) {
            throw new HawkFlowDataTypesException("HawkFlow API meta parameter exceeded max length of 300.");
        }

        if (!pattern.matches(meta)) {
            throw new HawkFlowDataTypesException("HawkFlow API meta parameter incorrect format.");
        }
    }

    public static void validateUid(String uid) throws HawkFlowDataTypesException {
        if(uid.length() > 50) {
            throw new HawkFlowDataTypesException("HawkFlow API uid parameter exceeded max length of 50.");
        }

        if (!uid.matches(pattern)) {
            throw new HawkFlowDataTypesException("HawkFlow API uid parameter incorrect format.");
        }
    }

    public static void validateExceptionText(String exceptionText) throws HawkFlowDataTypesException {
        if(exceptionText.length() > 15000) {
            throw new HawkFlowDataTypesException("HawkFlow API exceptionText parameter exceeded max length of 15000.");
        }
    }

    public static void validateMetricItems(ArrayList<HashMap<String, Float>> items) throws HawkFlowDataTypesException {
        for (HashMap<String, Float> map : items) {
            for (Map.Entry<String, Float> entry : map.entrySet()) {
                String name = entry.getKey();

                if(name != "name") {
                    throw new HawkFlowDataTypesException("HawkFlow API metric items parameter HashMap key must be called 'name'.");
                }

                if(name.length() > 50) {
                    throw new HawkFlowDataTypesException("HawkFlow API metric items parameter name exceeded max length of 50.");
                }

                if (!name.matches(pattern)) {
                    throw new HawkFlowDataTypesException("HawkFlow API metric items parameter name is in incorrect format.");
                }
            }
        }
    }
}