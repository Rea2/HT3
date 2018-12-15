package url_explorer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by Raik Yauheni on 11.12.2018.
 */
public class URLWorker  {
    private URL url = null;
    private String htmlPage;
    private HttpURLConnection connection;

    public URLConnection getConnection(String textUrl, String timeout) throws IOException {
            int time = Integer.parseInt(timeout);

            // Get URL with content for testing
            url = new URL(textUrl);
            connection =  (HttpURLConnection) url.openConnection();

            // Set connection's properties
            connection.setConnectTimeout(time * 1000);
            connection.setReadTimeout(time * 1000);
            connection.setRequestMethod("GET");
            connection.connect();
            return connection;
    }

    public String getHtmlPage() {
        if (connection == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        try {
            // OPEN the stream and put it into BufferedReader
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine ;
            while ((inputLine = br.readLine()) != null) {
                sb.append(inputLine);
            }
            br.close();
            return sb.toString();

        } catch ( IOException  e) {
            e.printStackTrace();
            return null;
        }
    }


}
