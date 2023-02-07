package ai.hawkflow;


class HawkFlowNoApiKeyException extends Exception {
    private static final String docsUrl = "Please see docs at https://docs.hawkflow.ai/integration/index.html";
    private static final String message = "No HawkFlow API Key set. " + docsUrl;

    /**
     * HawkFlowNoApiKeyException
     */
    public HawkFlowNoApiKeyException() {
        super(message);
    }
}

