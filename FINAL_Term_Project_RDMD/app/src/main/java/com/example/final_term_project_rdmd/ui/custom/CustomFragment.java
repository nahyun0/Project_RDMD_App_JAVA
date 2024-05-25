package com.example.final_term_project_rdmd.ui.custom;

import static java.net.URLEncoder.*;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/* 기존 import */

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.final_term_project_rdmd.R;


public class CustomFragment extends Fragment {

    EditText editText1;
    ListView listView;
    ArrayAdapter adapter;
    ArrayList<String> items = new ArrayList<String>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Fragment의 레이아웃을 인플레이트하고 해당 View 객체를 반환
        View rootView = inflater.inflate(R.layout.fragment_custom, container, false);

        // View 객체에서 UI 요소에 접근하여 초기화
        editText1 = rootView.findViewById(R.id.EditText1);
        listView = rootView.findViewById(R.id.listview);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        return rootView;

        /*editText1 = findViewById(R.id.EditText1);
        listView = findViewById(R.id.listview);
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);*/

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.Ueser1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick(v);
            }
        });
        view.findViewById(R.id.Ueser2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick2(v);
            }
        });
        view.findViewById(R.id.Ueser3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick3(v);
            }
        });
        view.findViewById(R.id.Ueser4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick4(v);
            }
        });
    }

    //근로자 버튼 layout에 android:onClick="mOnClick"
    public void mOnClick(View v) {
        if (v.getId() == R.id.Ueser1) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // open API을 통해 정보를 가진 xml문서를 읽어와서 listView에 보여주기
                    items.clear();

                    String location = editText1.getText().toString(); //입력 한거 문자열 location 으로 저장

                    //api 호출을 위한 uri 생성
                    String queryUrl = "https://apis.data.go.kr/B490001/sjbJhgigwanGwanriInfoService/getSjbWkGigwanInfoList?"
                            + "&ServiceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D"
                            + "&numOfRows=1000&pageNo=1&gigwanFg=12"; //gigwanFg=12가 재활스포츠기관 의미


                    try {
                        /*과정
                         * 1. URL 객체 만들어 stream 열고 inputStream에게 준다.
                         * 2. InputStream은 byte씩 데이터 전송으로 불편 --> InputStreamReader(바이트->문자 입력 스트림으로) 변경
                         * 3. InputSteamReader를 XmlPullParser에게 준다
                         * 4. ui로 띄울 데이터의 내용들은 stringBuffer로 저장.*/

                        // URL 객체 생성
                        URL url = new URL(queryUrl);
                        //Stream 열기
                        InputStream is = url.openStream();

                        //읽어들인 XML 문서를 분석(parse)해주는 객체 생성 , xpp(파서, parase)를 만들려면 팩토리를 먼저 생성
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser xpp = factory.newPullParser();
                        xpp.setInput(new InputStreamReader(is, "UTF-8"));  // InputStream으로부터 XML 데이터를 읽어오기

                        String tag;

                        int eventType = xpp.getEventType(); //이벤트 타입 얻어오기
                        StringBuffer buffer = new StringBuffer();
                        String currentAddress = "";

                        while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                            switch (eventType) {
                                case XmlPullParser.START_DOCUMENT:
                                    break;

                                case XmlPullParser.START_TAG: // 태그의 시작일 때
                                    tag = xpp.getName();

                                    if (tag.equals("addr")) {
                                        currentAddress = xpp.nextText(); //addr tag 안 text 부분 가져오기
                                        if (currentAddress.contains(location)) { //주소 text 부분에 문자열 location 포함시 나타나게 하기
                                            buffer.append("\n");
                                            buffer.append("주소 : ");
                                            xpp.next();
                                            buffer.append(currentAddress);
                                            buffer.append("\n");
                                        }

                                    } else if (tag.equals("gigwanNm")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("기관명 : ");
                                            xpp.next(); //tag 다음 text 로 이동
                                            buffer.append(xpp.getText()); //xml tag 안 text을 얻어서 버퍼에 추가
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("gigwanFgNm")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("기관구분명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("telNo")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("전화번호 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    }
                                    break;

                                case XmlPullParser.TEXT: //xml tag안의 text 내용
                                    break;

                                case XmlPullParser.END_TAG: // 태그의 끝일 때
                                    tag = xpp.getName();
                                    if (tag.equals("items")) { //하나의 정보가 끝나면
                                        //파싱 결과를 items 리스트에 추가
                                        items.add(buffer.toString());
                                    }
                                    break;
                            }
                            eventType = xpp.next();
                        }
                        //xml 파싱 시 발생 가능한 예외 처리
                    } catch (MalformedURLException e) { //네트워크 연결 관련
                        e.printStackTrace();
                    } catch (IOException e) { //입출력 관련
                        e.printStackTrace();
                    } catch (XmlPullParserException e) { //xml 파싱 관련
                        e.printStackTrace();
                    }

                    // 데이터 로드 후 UI 업데이트 요청
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
        }
    }

    //노약자
    public void mOnClick2(View v) {
        if (v.getId() == R.id.Ueser2) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // open API을 통해 정보를 가진 xml문서를 읽어와서 listView에 보여주기
                    items.clear();

                    String location = editText1.getText().toString(); //입력 한거 문자열 location 으로 저장

                    //api 호출을 위한 uri 생성
                    String queryUrl = "https://apis.data.go.kr/B551182/spclMdlrtHospInfoService1/getRcperHospList1?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                            "&pageNo=1&numOfRows=500";

                    try {
                        // URL 객체 생성
                        URL url = new URL(queryUrl);
                        //Stream 열기
                        InputStream is = url.openStream();

                        //읽어들인 XML 문서를 분석(parse)해주는 객체 생성 , xpp(파서, parase)를 만들려면 팩토리를 먼저 생성
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser xpp = factory.newPullParser();
                        xpp.setInput(new InputStreamReader(is, "UTF-8"));  // InputStream으로부터 XML 데이터를 읽어오기

                        String tag;

                        int eventType = xpp.getEventType(); //이벤트 타입 얻어오기
                        StringBuffer buffer = new StringBuffer();

                        String currentAddress = "";

                        while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                            switch (eventType) {
                                case XmlPullParser.START_DOCUMENT:
                                    break;

                                case XmlPullParser.START_TAG: // 태그의 시작일 때
                                    tag = xpp.getName();

                                    if (tag.equals("addr")) { //<addr> 태그라면
                                        currentAddress = xpp.nextText(); //주소에서 editText에 친 location과 비교위해 현재 위치 알아내기
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("\n");
                                            buffer.append("주소 : ");
                                            xpp.next();
                                            buffer.append(currentAddress);
                                            buffer.append("\n");
                                        }

                                    } else if (tag.equals("yadmNm")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("병원명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("clCdNm")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("기관구분명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("hospUrl")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("홈페이지 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("telno")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("전화번호 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    }
                                    break;

                                case XmlPullParser.TEXT: //xml tag안의 text 내용
                                    break;

                                case XmlPullParser.END_TAG: // 태그의 끝일 때

                                    tag = xpp.getName();
                                    if (tag.equals("items")) { //하나의 정보가 끝나면

                                        //파싱 결과를 items 리스트에 추가
                                        items.add(buffer.toString());
                                    }

                                    break;
                            }
                            eventType = xpp.next();
                        }
                        //xml 파싱 시 발생 가능한 예외 처리
                    } catch (MalformedURLException e) { //네트워크 연결 관련
                        e.printStackTrace();
                    } catch (IOException e) { //입출력 관련
                        e.printStackTrace();
                    } catch (XmlPullParserException e) { //xml 파싱 관련
                        e.printStackTrace();
                    }
                    // 데이터 로드 후 UI 업데이트 요청
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
        }
    }

    //어린이 (소아) 야간 진료 병원
    public void mOnClick3(View v) {
        if (v.getId() == R.id.Ueser3) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // open API을 통해 정보를 가진 xml문서를 읽어와서 listView에 보여주기
                    items.clear();

                    String location = editText1.getText().toString(); //입력 한거 문자열 location 으로 저장

                    //api 호출을 위한 uri 생성
                    String queryUrl = "https://apis.data.go.kr/B551182/spclMdlrtHospInfoService1/getChildNightMdlrtList1?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D&pageNo=1" +
                            "&numOfRows=500";

                    try {
                        // URL 객체 생성
                        URL url = new URL(queryUrl);
                        //Stream 열기
                        InputStream is = url.openStream();

                        //읽어들인 XML 문서를 분석(parse)해주는 객체 생성 , xpp(파서, parase)를 만들려면 팩토리를 먼저 생성
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser xpp = factory.newPullParser();
                        xpp.setInput(new InputStreamReader(is, "UTF-8"));  // InputStream으로부터 XML 데이터를 읽어오기

                        String tag;

                        int eventType = xpp.getEventType(); //이벤트 타입 얻어오기
                        StringBuffer buffer = new StringBuffer();

                        String currentAddress = "";

                        while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                            switch (eventType) {
                                case XmlPullParser.START_DOCUMENT:
                                    break;

                                case XmlPullParser.START_TAG: // 태그의 시작일 때
                                    tag = xpp.getName();

                                    if (tag.equals("addr")) { //<addr> 태그라면
                                        currentAddress = xpp.nextText(); //주소에서 editText에 친 location과 비교위해 현재 위치 알아내기
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("\n");
                                            buffer.append("주소 : ");
                                            xpp.next();
                                            buffer.append(currentAddress);
                                            buffer.append("\n");
                                        }

                                    } else if (tag.equals("yadmNm")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("병원명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("clCdNm")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("기관구분명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("hospUrl")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("홈페이지 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("telno")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("전화번호 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    }
                                    break;

                                case XmlPullParser.TEXT: //xml tag안의 text 내용
                                    break;

                                case XmlPullParser.END_TAG: // 태그의 끝일 때

                                    tag = xpp.getName();
                                    if (tag.equals("items")) { //하나의 정보가 끝나면

                                        //파싱 결과를 items 리스트에 추가
                                        items.add(buffer.toString());
                                    }

                                    break;
                            }
                            eventType = xpp.next();
                        }
                        //xml 파싱 시 발생 가능한 예외 처리
                    } catch (MalformedURLException e) { //네트워크 연결 관련
                        e.printStackTrace();
                    } catch (IOException e) { //입출력 관련
                        e.printStackTrace();
                    } catch (XmlPullParserException e) { //xml 파싱 관련
                        e.printStackTrace();
                    }
                    // 데이터 로드 후 UI 업데이트 요청
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
        }
    }
    //응급환자 (응급병원)
    public void mOnClick4(View v) {
        if (v.getId() == R.id.Ueser4) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    // open API을 통해 정보를 가진 xml문서를 읽어와서 listView에 보여주기
                    items.clear();

                    String location = editText1.getText().toString(); //입력 한거 문자열 location 으로 저장

                    //api 호출을 위한 uri 생성
                    String queryUrl = "https://apis.data.go.kr/B552657/ErmctInfoInqireService/getEgytListInfoInqire?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D&QT=5" +
                            "&pageNo=1&numOfRows=500";

                    try {
                        // URL 객체 생성
                        URL url = new URL(queryUrl);
                        //Stream 열기
                        InputStream is = url.openStream();

                        //읽어들인 XML 문서를 분석(parse)해주는 객체 생성 , xpp(파서, parase)를 만들려면 팩토리를 먼저 생성
                        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                        XmlPullParser xpp = factory.newPullParser();
                        xpp.setInput(new InputStreamReader(is, "UTF-8"));  // InputStream으로부터 XML 데이터를 읽어오기

                        String tag;

                        int eventType = xpp.getEventType(); //이벤트 타입 얻어오기
                        StringBuffer buffer = new StringBuffer();

                        String currentAddress = "";

                        while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                            switch (eventType) {
                                case XmlPullParser.START_DOCUMENT:
                                    break;

                                case XmlPullParser.START_TAG: // 태그의 시작일 때
                                    tag = xpp.getName();

                                    if (tag.equals("dutyAddr")) { //<addr> 태그라면
                                        currentAddress = xpp.nextText(); //주소에서 editText에 친 location과 비교위해 현재 위치 알아내기
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("주소 : ");
                                            xpp.next();
                                            buffer.append(currentAddress);
                                            buffer.append("\n");}

                                    }
                                    else if (tag.equals("dutyName")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("병원명 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    } else if (tag.equals("dutyEmclsName")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("응급의료기관구분 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n");
                                        }
                                    }  else if (tag.equals("dutyTel1")) {
                                        if (currentAddress.contains(location)) { //location 포함시 나타나게 하기
                                            buffer.append("전화번호 : ");
                                            xpp.next();
                                            buffer.append(xpp.getText());
                                            buffer.append("\n\n\n");
                                        }
                                    }
                                    break;

                                case XmlPullParser.TEXT: //xml tag안의 text 내용
                                    break;

                                case XmlPullParser.END_TAG: // 태그의 끝일 때

                                    tag = xpp.getName();
                                    if (tag.equals("items")) { //하나의 정보가 끝나면

                                        //파싱 결과를 items 리스트에 추가
                                        items.add(buffer.toString());
                                    }

                                    break;
                            }
                            eventType = xpp.next();
                        }
                        //xml 파싱 시 발생 가능한 예외 처리
                    } catch (MalformedURLException e) { //네트워크 연결 관련
                        e.printStackTrace();
                    } catch (IOException e) { //입출력 관련
                        e.printStackTrace();
                    } catch (XmlPullParserException e) { //xml 파싱 관련
                        e.printStackTrace();
                    }
                    // 데이터 로드 후 UI 업데이트 요청
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.notifyDataSetChanged();
                        }
                    });
                }
            }).start();
        }
    }
}
