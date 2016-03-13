package com.example.vitalik.todolist;

import android.util.Log;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

public class BackEndSender {
    private static String BACKEND_API_URL;
    public BackEndSender() {
        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream("config.properties");
            prop.load(input);
            BACKEND_API_URL = prop.getProperty("BACKEND_API_URL");
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static void sendRequest(long id, Item item) {

        String backendApiEndpointURL = BACKEND_API_URL + "/add";

        try {
            URL url = new URL(backendApiEndpointURL);
            Map<String,Object> params = new LinkedHashMap<>();
            params.put("id", id);
            params.put("title", item.getTitle());
            params.put("description", item.getDescription());


            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postData.length()));
            conn.setDoOutput(true);
            OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());

            writer.write(postData.toString());
            writer.flush();
            writer.close();
            conn.disconnect();
        } catch (Exception ex) {
            Log.w("MyLog", ex.toString());
        }
    }
}
