package ai.hawkflow;


class HawkFlowDataTypesException extends Exception {
    private static final String docsUrl = "Please see docs at https://docs.hawkflow.ai/integration/index.html";
    private static final String message = "HawkFlow data types not set correctly. " + docsUrl;
    public HawkFlowDataTypesException(String error) {
        super(error + " " + message);
    }
}

