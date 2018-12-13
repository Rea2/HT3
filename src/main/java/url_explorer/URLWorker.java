package url_explorer;

import url_explorer.instruction.ElapsedTime;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Created by Raik Yauheni on 11.12.2018.
 */
public class URLWorker  {
    private URL url = null;
    private String htmlPage;
    private HttpURLConnection connection;



    public URLConnection getConnection(String textUrl, String timeout) {
        try {
            int time = Integer.parseInt(timeout);
            // get URL content
            url = new URL(textUrl);
            connection =  (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(time * 1000);
            connection.setReadTimeout(time * 1000);
            connection.setRequestMethod("GET");
            connection.connect();

        } catch ( IOException  e) {
            connection = null;
        }
        finally {
            return connection;
       }
    }

//    @Override
//    public void measureTime() throws IOException {
//            connection.connect();
//    }

    public String  getHtmlPage() {
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
