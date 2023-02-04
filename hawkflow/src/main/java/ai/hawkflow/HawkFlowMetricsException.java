package ai.hawkflow;


class HawkFlowMetricsException extends Exception {
    private static final String docsUrl = "Please see docs at https://docs.hawkflow.ai/integration/index.html";
    private static final String message = "@HawkflowMetrics missing items parameter. " + docsUrl;
    public HawkFlowMetricsException() {
        super(message);
    }
}

