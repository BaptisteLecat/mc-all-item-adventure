package fr.dreamin.desCodeurs.utils;

import fr.dreamin.desCodeurs.Main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ApiRequest {

  public static String get(String apiUrl, String id) {

    try{
      URL url = new URL(apiUrl + "/" + id);

      HttpURLConnection conection = (HttpURLConnection) url.openConnection();

      conection.setRequestMethod("GET");
      conection.setRequestProperty("Accept", "application/json");

      int responseCode = conection.getResponseCode();

      if (responseCode != HttpURLConnection.HTTP_OK) return "{}";

      BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
      String inputLine;
      StringBuilder response = new StringBuilder();

      while ((inputLine = in.readLine())!= null) {
        response.append(inputLine);
      }
      conection.disconnect();
      in.close();

      return response.toString();

    }
    catch(Exception e) {
      e.printStackTrace();
    }
    return "{}";
  }

  public static String post(String apiUrl, String json) {
    try{
      URL url = new URL(apiUrl);

      HttpURLConnection conection = (HttpURLConnection) url.openConnection();

      conection.setRequestMethod("POST");
      conection.setRequestProperty("Content-Type", "application/json");
      conection.setRequestProperty("Accept", "application/json");

      conection.setDoOutput(true);

      try(java.io.OutputStream os = conection.getOutputStream()) {
        byte[] input = json.getBytes("utf-8");
        os.write(input, 0, input.length);
      }

      int responseCode = conection.getResponseCode();

      System.out.println(responseCode);

      if (responseCode != HttpURLConnection.HTTP_CREATED) return "{}";

      BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
      String inputLine;
      StringBuilder response = new StringBuilder();

      while ((inputLine = in.readLine())!= null) {
        response.append(inputLine);
      }
      conection.disconnect();
      in.close();

      return response.toString();

    }
    catch(Exception e) {
      e.printStackTrace();
    }
    return "{}";
  }

}
