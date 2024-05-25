package com.example.final_term_project_rdmd.ui.map;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import com.example.final_term_project_rdmd.MainActivity;
import com.example.final_term_project_rdmd.R;

public class MapFragment extends Fragment {
    ArrayList<Double> lat_list;
    ArrayList<Double> lng_list;
    ArrayList<String> name_list;
    ArrayList<String> vicinity_list;
    // 지도의 표시한 마커(주변장소표시)를 관리하는 객체를 담을 리스트

    public int selectedCategoryIndex = -1;
    Toast t;

    ArrayList<Marker> markers_list;
    // 다이얼로그를 구성하기 위한 배열
    String[] category_name_array={
            "모두","병원","동물병원"
    };
    // types 값 배열
    String[] category_value_array={
            "all","hospital","veterinary_care"
    };

    String[] permission_list = {
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    // 현재 사용자 위치
    Location myLocation;
    TextView text;
    // 위치 정보를 관리하는 매니저
    LocationManager manager;
    // 지도를 관리하는 객체
    GoogleMap map;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        lat_list = new ArrayList<>();
        lng_list = new ArrayList<>();
        name_list = new ArrayList<>();
        vicinity_list = new ArrayList<>();
        markers_list = new ArrayList<>();

        checkPermission();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fragment);
        if (mapFragment != null) {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {
                    map = googleMap;
                }
            });
        }

        // 옵션 메뉴를 사용한다고 시스템에 알림
        setHasOptionsMenu(true);

        return view;
    }

    public void checkPermission() {
        boolean isGrant = false;
        for (String str : permission_list) {
            if (ContextCompat.checkSelfPermission(getContext(), str) == PackageManager.PERMISSION_GRANTED) {
            } else {
                isGrant = false;
                break;
            }
        }
        if (!isGrant) {
            requestPermissions(permission_list, 0);
        } else {
            getMyLocation();
        }
    }

    // 사용자가 권한 허용/거부 버튼을 눌렀을 때 호출되는 메서드
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        boolean isGrant = true;
        for (int result : grantResults) {
            if (result == PackageManager.PERMISSION_DENIED) {
                isGrant = false;
                break;
            }
        }
        // 모든 권한을 허용했다면 사용자 위치를 측정한다.
        if (isGrant == true) {
            getMyLocation();
        }
    }

    // 현재 위치를 가져온다.
    public void getMyLocation() {
        manager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        // 권한이 모두 허용되어 있을 때만 동작하도록 한다.
        int chk1 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        int chk2 = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
        if (chk1 == PackageManager.PERMISSION_GRANTED && chk2 == PackageManager.PERMISSION_GRANTED) {
            myLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            showMyLocation();
        }
        // 새롭게 위치를 측정한다.
        GpsListener listener = new GpsListener();
        if (manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            manager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10, listener);
        }
        if (manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10, listener);
        }
    }

    // GPS Listener
    class GpsListener implements LocationListener {

        public void onLocationChanged(Location location) {
            // 현재 위치 값 저장
            myLocation = location;

            // 위치 측정 중단
            manager.removeUpdates(this);

            // 지도 현재 위치로 이동
            showMyLocation();
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }
    }

    // 현재 위치를 기반으로 지도를 표시한다.
    public void showMyLocation() {
        // Defensive Coding
        if (myLocation == null) {
            return;
        }
        // 현재 위치값 추출
        double lat = myLocation.getLatitude();
        double lng = myLocation.getLongitude();

        // 위도 경도 변화 객체 생성
        LatLng position = new LatLng(lat, lng);

        // 현재 위치 설정.
        if (map != null) {
            CameraUpdate update1 = CameraUpdateFactory.newLatLng(position);
            map.moveCamera(update1);


            // 확대
            CameraUpdate update2 = CameraUpdateFactory.zoomTo(15);
            map.animateCamera(update2);

            // 현재 위치 표시
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.setMyLocationEnabled(true);

            // 지도 모드 변경
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.map_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    // 메뉴 항목을 터치하면 호출되는 메서드
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // id 추출
        // id 추출
        int id = item.getItemId();
        if (id == R.id.item1) {
            // 현재 위치 측정
            getMyLocation();
        } else if (id == R.id.item2) {
            // 주변 정보 가져오기
            showCategoryList();
        }
        return super.onOptionsItemSelected(item);
    }
    // 주변 카테고리 리스트
    private void showCategoryList() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("장소 타입 선택");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, category_name_array);
        DialogListener listener = new DialogListener();
        builder.setAdapter(adapter, listener);
        builder.setNegativeButton("취소", null);
        builder.show();
    }

    // 다이얼로그의 리스너
    class DialogListener implements DialogInterface.OnClickListener{
        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            // 사용자가 선택한 항목 인덱스번째의 type 값을 가져온다.
            String type=category_value_array[i];

            selectedCategoryIndex = i; // 사용자가 선택한 항목의 인덱스를 저장

            // 주변 정보를 가져온다
            getNearbyPlace(type);
        }
    }

    //주변 정보 가져오기
    public void getNearbyPlace(String type_keyword){
        NetworkThread thread=new NetworkThread(type_keyword);
        thread.start();
    }
    public void showMarker(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 지도에 마커 표시

                // 기존 마커를 모두 제거
                for (Marker marker : markers_list) {
                    marker.remove();
                }
                markers_list.clear();

                // 리스트만큼 마커 객체 만들기
                for (int i = 0; i < lat_list.size(); i++) {
                    // 값 추출
                    double lat = lat_list.get(i);
                    double lng = lng_list.get(i);
                    String name = name_list.get(i);
                    String vicinity = vicinity_list.get(i);
                    // 생성 예정 마커의 정보 보유 객체 생성
                    MarkerOptions options = new MarkerOptions();
                    // 위치설정
                    LatLng pos = new LatLng(lat, lng);
                    options.position(pos);
                    // 말풍선이 표시될 값 설정
                    options.title(name);
                    options.snippet(vicinity);
                    // 마커 지도 표시
                    Marker marker = map.addMarker(options);
                    markers_list.add(marker);
                }
            }
        });
    }
    class NetworkThread extends Thread {
        String type_keyword;

        public NetworkThread(String type_keyword) {
            this.type_keyword = type_keyword;
        }

        @Override
        public void run() {
            try {
                // 데이터를 담아놓을 리스트를 초기화한다.
                lat_list.clear();
                lng_list.clear();
                name_list.clear();
                vicinity_list.clear();

                // 검색 페이지 주소 설정
                String site = "https://maps.googleapis.com/maps/api/place/nearbysearch/json";
                site += "?location=" + myLocation.getLatitude() + ","
                        + myLocation.getLongitude()
                        // 여기서 범위 설정, 지금은 1km
                        + "&radius=1000&sensor=false&language=ko"
                        + "&key=AIzaSyC2kNOA7cqmGt9fxWfJioxewwrwqi7tgiA";
                if (type_keyword != null && type_keyword.equals("all") == false) {
                    site += "&type=" + type_keyword;
                }
                Log.d("URL", site);
                // 접속
                URL url = new URL(site);
                URLConnection conn = url.openConnection();
                // 스트림 추출
                InputStream is = conn.getInputStream();
                InputStreamReader isr = new InputStreamReader(is, "utf-8");
                BufferedReader br = new BufferedReader(isr);
                String str = null;
                StringBuffer buf = new StringBuffer();
                // 읽어온다
                do {
                    str = br.readLine();
                    if (str != null) {
                        buf.append(str);
                    }
                } while (str != null);
                String rec_data = buf.toString();
                // JSON 데이터 분석
                JSONObject root = new JSONObject(rec_data);
                // status 값을 추출한다.
                String status = root.getString("status");
                // 가져온 값이 있을 경우에 지도에 표시한다.
                if (status.equals("OK")) {
                    // results 배열을 가져온다
                    JSONArray results = root.getJSONArray("results");
                    // 개수만큼 반복한다.
                    for (int i = 0; i < results.length(); i++) {
                        // 객체를 추출한다.(장소하나의 정보)
                        JSONObject obj1 = results.getJSONObject(i);
                        // 위도 경도 추출
                        JSONObject geometry = obj1.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        double lat = location.getDouble("lat");
                        double lng = location.getDouble("lng");
                        // 장소 이름 추출
                        String name = obj1.getString("name");
                        // 대략적인 주소 추출
                        String vicinity = obj1.getString("vicinity");
                        // 데이터를 담는다.
                        lat_list.add(lat);
                        lng_list.add(lng);
                        name_list.add(name);
                        vicinity_list.add(vicinity);
                    }
                    showMarker();
                } else {
                    String selectedOption = category_name_array[selectedCategoryIndex]; // 사용자가 선택한 옵션 이름
                    String message = "주변에 " + selectedOption + "이(가) 없습니다.";
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast toast = Toast.makeText(getContext(), message, Toast.LENGTH_SHORT);
                            Gravity Gravity = null;
                            toast.setGravity(Gravity.TOP | Gravity.CENTER_HORIZONTAL, 0, 0);
                            toast.show();
                        }
                    });
                    Log.d("Toast", message);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
