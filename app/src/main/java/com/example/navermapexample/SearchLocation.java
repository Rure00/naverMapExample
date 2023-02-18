package com.example.navermapexample;

import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.navermapexample.data.LocationData;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class SearchLocation {
    String clientId = "eoAFK4Y9R_UmyYErLstp"; //애플리케이션 클라이언트 아이디
    String clientSecret = "JFiCa3Hdxs"; //애플리케이션 클라이언트 시크릿

    public ArrayList<LocationData> search(String searchWord) {
        ArrayList<LocationData> locationDataList = new ArrayList<LocationData>();

        new Thread(() -> {
            try {
                String text = URLEncoder.encode(searchWord, "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/search/local?query=" + text + "&display=" + 10;

                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);

                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) { // 정상 호출
                    br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                } else {  // 에러 발생
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }

                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                    response.append("\n");
                }
                br.close();

                JSONTokener token = new JSONTokener(response.toString());
                JSONObject object = new JSONObject(token);
                JSONArray arr = object.getJSONArray("items");

                //TODO: item 결과값 받아와 클래스 배열 만들기
                int len = arr.length();

                for (int i = 0; i < len; i++) {
                    JSONObject temp = (JSONObject) arr.get(i);

                    String name = temp.get("title").toString();
                    String jibunAddress = temp.get("address").toString();
                    String roadAddress = temp.get("roadAddress").toString();
                    String category = temp.get("category").toString();
                    String description = temp.get("description").toString();
                    String mapX = temp.get("mapx").toString();
                    String mapY = temp.get("mapy").toString();


                    LocationData locationData = new LocationData(
                            name, roadAddress, jibunAddress, category, description, mapX, mapY
                    );

                    locationDataList.add(locationData);
                }

                Log.d("MyTag", locationDataList.get(0).getName() + ", " + locationDataList.get(0).getDescription());

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

        if(locationDataList.size() == 0) {
            return null;
        } else {
            return locationDataList;
        }
    }
}
