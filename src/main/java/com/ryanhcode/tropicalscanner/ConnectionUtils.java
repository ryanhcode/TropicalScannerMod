package com.ryanhcode.tropicalscanner;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ConnectionUtils {

    public static String read(String url) {
        HttpURLConnection con = null;
        String result;
        try {
            final URL httpUrl = new URL(url);
            con = (HttpURLConnection)httpUrl.openConnection();
            result = contents(con);
        }
        catch (MalformedURLException e) {
            result = "url-error";
        }
        catch (IOException e2) {
            result = "network-error";
        }
        finally {
            if (con != null) {
                con.disconnect();
            }
        }
        return result;
    }

    private static String contents(HttpURLConnection connection) {
        if (connection != null) {
            try {
                final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                final StringBuilder builder = new StringBuilder();
                String result;
                String input;
                while ((input = reader.readLine()) != null) {
                    builder.append(input);
                }
                result = builder.toString();
                if (connection != null) {
                    connection.disconnect();
                }
                return result;
            }
            catch (Exception e) {
                e.printStackTrace();
                return "content-error";
            }
        }else {
            return "network-error";
        }
    }
}
