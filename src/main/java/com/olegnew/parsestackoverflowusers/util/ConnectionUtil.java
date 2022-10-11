package com.olegnew.parsestackoverflowusers.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class ConnectionUtil {
    public String getStringFromUrl(String urlString) {
        StringBuilder stringBuilder = new StringBuilder();
        URL url;
        {
            try {
                url = new URL(urlString);
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            HttpURLConnection urlConnection;
            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestProperty("Accept-Charset", "UTF-8");
                urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
                urlConnection.setRequestMethod("GET");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            try {
                if (urlConnection.getResponseCode() != 200) {
                    stringBuilder.append("");
                    return stringBuilder.toString();
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Map map = urlConnection.getHeaderFields();
            Iterator it = map.keySet().iterator();
            boolean gzip = false;
            while (it.hasNext()) {
                Object type = map.get(it.next());
                if (type.toString().indexOf("gzip") != -1) {
                    gzip = true;
                    break;
                }
            }
            InputStreamReader isr;
            try (InputStream inputStream = urlConnection.getInputStream()) {
                if (gzip) {
                    isr = new InputStreamReader(new GZIPInputStream(inputStream));
                } else {
                    isr = new InputStreamReader(inputStream);
                }
                int character;
                do {
                    character = isr.read();
                    if (character >= 0) {
                        stringBuilder.append((char) character);
                    }
                } while (character >= 0);

            } catch (Exception e) {
                throw new RuntimeException("Can't get json object ", e);
            }

        }
        return stringBuilder.toString();
    }
}
