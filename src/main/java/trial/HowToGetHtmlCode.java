package trial;

import java.io.*;
import java.net.URL;
import java.net.URLConnection;

public class HowToGetHtmlCode {
    private static String url = "https://sport.netbet.ro/tenis/";

    public static void main(String[] args) throws Exception {
        String s = getURLSource(url);
        PrintWriter writer = new PrintWriter("site.html", "UTF-8");
        writer.print(s);
        writer.close();
    }

    public static String getURLSource(String url) throws IOException {
        URL urlObject = new URL(url);
        URLConnection urlConnection = urlObject.openConnection();
        urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

        return toString(urlConnection.getInputStream());
    }

    private static String toString(InputStream inputStream) throws IOException {
        try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"))) {
            String inputLine;
            StringBuilder stringBuilder = new StringBuilder();
            while ((inputLine = bufferedReader.readLine()) != null) {
                stringBuilder.append(inputLine);
            }

            return stringBuilder.toString();
        }
    }
}
