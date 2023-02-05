package ai.hawkflow;


class HawkFlowApiKeyFormatException extends Exception {
    private static final String docsUrl = "Please see docs at https://docs.hawkflow.ai/integration/index.html";
    private static final String message = "HawkFlow Invalid API Key format. " + docsUrl;
    public HawkFlowApiKeyFormatException() {

        super(message);
    }
}

