package com.example.navermapexample.activities;

import static kotlinx.coroutines.CoroutineScopeKt.CoroutineScope;

import androidx.annotation.NonNull;
import androidx.annotation.UiThread;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.navermapexample.NaverNavigation;
import com.example.navermapexample.R;
import com.example.navermapexample.SearchLocation;
import com.example.navermapexample.data.LocationData;
import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraAnimation;
import com.naver.maps.map.CameraPosition;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapFragment;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.NaverMapOptions;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.UiSettings;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import kotlinx.coroutines.Dispatchers;

public class MapViewActivity extends FragmentActivity implements OnMapReadyCallback {

    //GPS ㅣ요이
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;

    //UI setting
    UiSettings uiSettings = null;

    private NaverMap nMap;
    NaverMapOptions options = null;

    //class 선언
    SearchLocation searchLocation = null;
    NaverNavigation naverNavigation = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_view);

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);

        //searchLocation 할당
        searchLocation = new SearchLocation();
        naverNavigation = new NaverNavigation();

        //Option 설정
        options = new NaverMapOptions()
                .camera(new CameraPosition(new LatLng(35.1798159, 129.0750222), 8))
                .mapType(NaverMap.MapType.Basic);

        //mapFragment 할당
        FragmentManager fm = getSupportFragmentManager();
        MapFragment mapFragment = (MapFragment)fm.findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = MapFragment.newInstance(options);
            fm.beginTransaction().add(R.id.map, mapFragment).commit();
        }

        mapFragment.getMapAsync(this);

        //Button 로직
        findViewById(R.id.modeChangeBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraUpdate cameraUpdate = CameraUpdate.scrollTo(new LatLng(37.476750, 126.981631))
                        .animate(CameraAnimation.Easing, 2000)
                        .finishCallback(() -> {
                            Toast.makeText(MapViewActivity.this, "카메라 이동 완료", Toast.LENGTH_SHORT).show();
                        })
                        .cancelCallback(() -> {
                            Toast.makeText(MapViewActivity.this, "카메라 이동 취소", Toast.LENGTH_SHORT).show();
                        });
                nMap.moveCamera(cameraUpdate);
            }
        });
        findViewById(R.id.searchBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = ((EditText)findViewById(R.id.searchText)).getText().toString();
                Log.d("MyTag", searchText);
                searchLocation.search(searchText);
            }
        });
        findViewById(R.id.navigationBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String start = "126.9816814,37.4770008";   //사당
                String goal = "126.9821837,37.4866644";     //이수

                naverNavigation.search(start, goal);
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,  @NonNull int[] grantResults) {
        if (locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated()) { // 권한 거부됨
                nMap.setLocationTrackingMode(LocationTrackingMode.None);
            }
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults);
    }

    @UiThread
    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        nMap = naverMap;

        //UiSetting 연결
        uiSettings = nMap.getUiSettings();
        uiSettings.setLocationButtonEnabled(true);
        nMap.setLocationTrackingMode(LocationTrackingMode.Follow);

        //locationSource - naverMap 연결
        nMap.setLocationSource(locationSource);

        //option 변경 콜백 함수
        nMap.addOnOptionChangeListener(() -> {
            //...
        });


    }
}