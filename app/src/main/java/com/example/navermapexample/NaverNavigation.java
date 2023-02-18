package com.example.navermapexample;

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

public class NaverNavigation {
    String clientId = "8ml72v7bgj"; //애플리케이션 클라이언트 아이디
    String clientSecret = "LyzurN1acJ7MR0BoPR6LWpM3fg20hq3MMBZ71dTM"; //애플리케이션 클라이언트 시크릿

    public void search(String start, String goal) {
        new Thread(() -> {
            try {
                String startString = URLEncoder.encode(start, "UTF-8");
                String goalString = URLEncoder.encode(goal, "UTF-8");
                String apiURL = "https://naveropenapi.apigw.ntruss.com/map-direction-15/v1/driving?"
                        + "start=" + startString + "&goal=" + goalString
                        + "&option=trafast";

                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");
                con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
                con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

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
                Log.d("MyTag", response.toString());

                JSONObject object = new JSONObject(token);
                JSONObject route = object.getJSONObject("route").getJSONObject("trafast");


                Log.d("MyTag", route.get("distance") + ", " + route.get("duration"));

                //JSONArray arr = object.getJSONArray("route");



                /*
                //TODO: item 결과값 받아와 클래스 배열 만들기
                int len = arr.length();
                ArrayList<LocationData> locationDataList = new ArrayList<LocationData>();


                for (int i = 0; i < len; i++) {
                    JSONObject temp = (JSONObject) arr.get(i);

                    String name = temp.get("title").toString();
                    String jibunAddress = temp.get("address").toString();
                    String roadAddress = temp.get("roadAddress").toString();
                    String category = temp.get("category").toString();
                    String description = temp.get("description").toString();

                    LocationData locationData = new LocationData(
                            name, roadAddress, jibunAddress, category, description
                    );

                    locationDataList.add(locationData);
                } */

            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();

    }
}
