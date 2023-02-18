package com.example.navermapexample;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GeoCoding {
        BufferedReader io = new BufferedReader(new InputStreamReader(System.in));
        String clientId = "8ml72v7bgj";
        String clientSecret = "LyzurN1acJ7MR0BoPR6LWpM3fg20hq3MMBZ71dTM";


        void search(String address) {
            try {
                String addr = URLEncoder.encode(address, "UTF-8");
                // Geocoding 개요에 나와있는 API URL 입력.
                String apiURL = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + addr;	// JSON

                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.setRequestMethod("GET");

                // Geocoding 개요에 나와있는 요청 헤더 입력.
                con.setRequestProperty("X-NCP-APIGW-API-KEY-ID", clientId);
                con.setRequestProperty("X-NCP-APIGW-API-KEY", clientSecret);

                // 요청 결과 확인. 정상 호출인 경우 200
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if (responseCode == 200) {
                    br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"));
                } else {
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }

                String inputLine = null;
                StringBuffer response = new StringBuffer();

                while((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }

                br.close();

                JSONTokener token = new JSONTokener(response.toString());
                JSONObject object = new JSONObject(token);
                JSONArray arr = object.getJSONArray("addresses");

                //TODO: 위치를 나타낼 Class 생성하여 정보 저장하기
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject temp = (JSONObject) arr.get(i);
                    System.out.println("address : " + temp.get("roadAddress"));
                    System.out.println("jibunAddress : " + temp.get("jibunAddress"));
                    System.out.println("위도 : " + temp.get("y"));
                    System.out.println("경도 : " + temp.get("x"));
                }

            } catch (Exception e) {
                Log.d("MyTag", e.toString());
            }
        }
}

