package com.example.vitalik.todolist;

import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.Map;

public class Request {
    public static void sendRequest(long oldId, long newId, String title, Integer quantity, String description) {
        String request = "https://polar-garden-6129.herokuapp.com/add";
        try {
            URL url = new URL(request);
            Map<String,Object> params = new LinkedHashMap<>();
            params.put("oldid", oldId);
            params.put("newid", newId);
            params.put("title", title);
            params.put("quantity", quantity);
            params.put("description", description);

            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            conn.setDoOutput(true);
            conn.getOutputStream().write(postDataBytes);
        } catch (Exception ex) {
            Log.w("MyLog", ex.toString());
        }
    }
}
