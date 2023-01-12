package ai.hawkflow;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main(String[] args) {
        String url = "https://api.example.com/endpoint";

        for (int i = 0; i < 3; i++) {
            try {
                // create the HttpURLConnection
                HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();

                // set the request method and other properties
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(3000);
                connection.setReadTimeout(3000);

                // make the request and check the response code
                int responseCode = connection.getResponseCode();
                if (responseCode == 200) {
                    // request was successful, do something with the response
                    // ...
                    break;
                } else {
                    // request was unsuccessful, handle the error
                    // ...
                }
            } catch (IOException e) {
                // handle the exception
                // ...
            }
        }
    }
}
