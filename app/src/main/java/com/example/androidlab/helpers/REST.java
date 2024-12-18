package com.example.androidlab.helpers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class REST {

    private static BufferedWriter bufferedWriter;
    private static OutputStream outputStream;
    private static BufferedReader bufferedReader;
    private static StringBuffer response;

    public static void setHttpConnectionProperties(String requestMethod, HttpURLConnection httpURLConnection) throws ProtocolException {
        httpURLConnection.setRequestMethod(requestMethod);
        httpURLConnection.setConnectTimeout(20000);
        httpURLConnection.setReadTimeout(20000);
        httpURLConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        httpURLConnection.setRequestProperty("Accept", "*/*");
        httpURLConnection.setDoInput(true);
        if (requestMethod.equals("GET")) {
            httpURLConnection.setDoOutput(false);
        } else {
            httpURLConnection.setDoOutput(true);
        }
        System.out.println("Request method: " + httpURLConnection.getRequestMethod());
    }

    public static int outputHttpResponseCode(HttpURLConnection httpURLConnection) throws IOException {
        int code = httpURLConnection.getResponseCode();
        System.out.println("Response code: " + code);
        return code;
    }

    public static String sendPost(String postUrl, String info) throws IOException {
        URL url = new URL(postUrl);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        setHttpConnectionProperties("POST", httpURLConnection);
        outputStream = httpURLConnection.getOutputStream();
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
        bufferedWriter.write(info);

        bufferedWriter.close();
        outputStream.close();

        if (outputHttpResponseCode(httpURLConnection) == HttpURLConnection.HTTP_OK) {
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            response = new StringBuffer();

            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
            }

            bufferedReader.close();
            return response.toString();
        } else {
            return "Error";
        }
    }

    public static String sendGet(String urlGet) throws IOException {
        URL url = new URL(urlGet);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        setHttpConnectionProperties("GET", httpURLConnection);
        if (outputHttpResponseCode(httpURLConnection) == HttpURLConnection.HTTP_OK) {
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            response = new StringBuffer();

            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
                break;
            }

            bufferedReader.close();
            return response.toString();
        }
        return "Error";
    }

    public static String sendPut(String urlPost, String info) {
        return "";
    }

    public static String sendPut(String urlPut) throws IOException {
        URL url = new URL(urlPut);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        setHttpConnectionProperties("PUT", httpURLConnection);
        if (outputHttpResponseCode(httpURLConnection) == HttpURLConnection.HTTP_OK) {
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            response = new StringBuffer();

            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
                break;
            }

            bufferedReader.close();
            return response.toString();
        }
        return "Error";
    }

    public static String sendDelete(String urlDelete) throws IOException {
        URL url = new URL(urlDelete);
        HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
        setHttpConnectionProperties("DELETE", httpURLConnection);
        if (outputHttpResponseCode(httpURLConnection) == HttpURLConnection.HTTP_OK) {
            bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));
            String line;
            response = new StringBuffer();

            while ((line = bufferedReader.readLine()) != null) {
                response.append(line);
                break;
            }

            bufferedReader.close();
            return response.toString();
        }
        return "Error";
    }
}
