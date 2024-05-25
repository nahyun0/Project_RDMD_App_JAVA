package com.example.final_term_project_rdmd.ui.rank;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/*기존 import*/

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import com.example.final_term_project_rdmd.MainActivity;
import com.example.final_term_project_rdmd.R;

public class RankFragment extends Fragment {
    Button button1;
    Button button2;
    Button button3;
    ListView listView;

    ArrayAdapter adapter;
    ArrayList<String> items = new ArrayList<String>();
    RadioGroup radioGroup;
    RadioButton rg_btn1, rg_btn2, rg_btn3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rank, container, false);

        button1 = view.findViewById(R.id.button1);
        button2 = view.findViewById(R.id.button2);
        button3 = view.findViewById(R.id.button3);

        radioGroup = view.findViewById(R.id.radioGroup);
        rg_btn1 = view.findViewById(R.id.rg_btn1);
        rg_btn2 = view.findViewById(R.id.rg_btn2);
        rg_btn3 = view.findViewById(R.id.rg_btn3);

        listView = view.findViewById(R.id.listView);
        adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, items);
        listView.setAdapter(adapter);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rg_btn1.setText("대장암");
                rg_btn2.setText("위암");
                rg_btn3.setText("폐암");
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rg_btn1.setText("고혈압");
                rg_btn2.setText("당뇨병");
                rg_btn3.setText("천식");
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rg_btn1.setText("폐렴");
                rg_btn2.setText("급성기 뇌졸중");
                rg_btn3.setText("관상동맥우회술");
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        view.findViewById(R.id.rg_btn1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick_rank1(v);
            }
        });
        view.findViewById(R.id.rg_btn2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick_rank2(v);
            }
        });
        view.findViewById(R.id.rg_btn3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mOnClick_rank3(v);
            }
        });
    }
    public void mOnClick_rank1(View v) {
        if (v.getId() == R.id.rg_btn1) {
            if(rg_btn1.getText().equals("대장암")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // open API을 통해 정보를 가진 xml문서를 읽어와서 listView에 보여주기
                        items.clear();

                        //api 호출을 위한 uri 생성
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
                                "&numOfRows=500";

                        try {
                            /*과정
                             * 1. URL 객체 만들어 stream 열고 inputStream에게 준다.
                             * 2. InputStream은 byte씩 데이터 전송으로 불편 --> InputStreamReader(바이트->문자 입력 스트림으로) 변경
                             * 3. InputSteamReader를 XmlPullParser에게 준다.
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

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "대장암"; //질병명 작성
                            String searchAsmGrd = "3"; //조회는 3회이상만

                            while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG: // 태그의 시작일 때
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText(); //현재 질병명 가져오기
                                            // 현재 질병명 과 찾고자 하는 질병명이 일치할때 and 우수기간 선정 3회 이상일 때 조회
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();

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
                            // 데이터 로드 후 UI 업데이트를 위해 메인 스레드에 메시지 보내기
                            handler.sendEmptyMessage(0);
                            //xml 파싱 시 발생 가능한 예외 처리
                        } catch (MalformedURLException e) { //네트워크 연결 관련
                            e.printStackTrace();
                        } catch (IOException e) { //입출력 관련
                            e.printStackTrace();
                        } catch (XmlPullParserException e) { //xml 파싱 관련
                            e.printStackTrace();
                        }
                    }
                }).start();
            } else if (rg_btn1.getText().equals("고혈압")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // open API을 통해 정보를 가진 xml문서를 읽어와서 listView에 보여주기
                        items.clear();

                        //api 호출을 위한 uri 생성
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
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

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "고혈압"; //질병명 작성
                            String searchAsmGrd = "3"; //조회는 3회이상만

                            while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG: // 태그의 시작일 때
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText(); //현재 질병명 가져오기
                                            // 현재 질병명 과 찾고자 하는 질병명이 일치할때 and 우수기간 선정 3회 이상일 때 조회
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();

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
                        // 데이터 로드 후 UI 업데이트를 위해 메인 스레드에 메시지 보내기
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }else if (rg_btn1.getText().equals("폐렴")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // open API을 통해 정보를 가진 xml문서를 읽어와서 listView에 보여주기
                        items.clear();

                        //api 호출을 위한 uri 생성
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
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

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "폐렴"; //질병명 작성
                            String searchAsmGrd = "3"; //조회는 3회이상만

                            while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG: // 태그의 시작일 때
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText(); //현재 질병명 가져오기
                                            // 현재 질병명 과 찾고자 하는 질병명이 일치할때 and 우수기간 선정 3회 이상일 때 조회
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();
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
                        // 데이터 로드 후 UI 업데이트를 위해 메인 스레드에 메시지 보내기
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }


        }

    }

    //두번째 라디오 버튼
    public void mOnClick_rank3(View v) {
        if (v.getId() == R.id.rg_btn3) {
            if(rg_btn3.getText().equals("폐암")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // open API을 통해 정보를 가진 xml문서를 읽어와서 listView에 보여주기
                        items.clear();

                        //api 호출을 위한 uri 생성
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
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

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "폐암"; //질병명 작성
                            String searchAsmGrd = "3"; //조회는 3회이상만

                            while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG: // 태그의 시작일 때
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText(); //현재 질병명 가져오기
                                            // 현재 질병명 과 찾고자 하는 질병명이 일치할때 and 우수기간 선정 3회 이상일 때 조회
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();
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
                        // 데이터 로드 후 UI 업데이트를 위해 메인 스레드에 메시지 보내기
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            } else if (rg_btn3.getText().equals("천식")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // open API을 통해 정보를 가진 xml문서를 읽어와서 listView에 보여주기
                        items.clear();

                        //api 호출을 위한 uri 생성
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
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

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "천식"; //질병명 작성
                            String searchAsmGrd = "3"; //조회는 3회이상만

                            while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG: // 태그의 시작일 때
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText(); //현재 질병명 가져오기
                                            // 현재 질병명 과 찾고자 하는 질병명이 일치할때 and 우수기간 선정 3회 이상일 때 조회
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();
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
                        // 데이터 로드 후 UI 업데이트를 위해 메인 스레드에 메시지 보내기
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }else if (rg_btn3.getText().equals("관상동맥우회술")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // open API을 통해 정보를 가진 xml문서를 읽어와서 listView에 보여주기
                        items.clear();

                        //api 호출을 위한 uri 생성
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
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

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "관상동맥우회술"; //질병명 작성
                            String searchAsmGrd = "3"; //조회는 3회이상만

                            while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG: // 태그의 시작일 때
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText(); //현재 질병명 가져오기
                                            // 현재 질병명 과 찾고자 하는 질병명이 일치할때 and 우수기간 선정 3회 이상일 때 조회
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();
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
                        // 데이터 로드 후 UI 업데이트를 위해 메인 스레드에 메시지 보내기
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }


        }

    }


    //세번째 라디오 버튼
    public void mOnClick_rank2(View v) {
        if (v.getId() == R.id.rg_btn2) {
            if(rg_btn2.getText().equals("위암")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // open API을 통해 정보를 가진 xml문서를 읽어와서 listView에 보여주기
                        items.clear();

                        //api 호출을 위한 uri 생성
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
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

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "위암"; //질병명 작성
                            String searchAsmGrd = "3"; //조회는 3회이상만

                            while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG: // 태그의 시작일 때
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText(); //현재 질병명 가져오기
                                            // 현재 질병명 과 찾고자 하는 질병명이 일치할때 and 우수기간 선정 3회 이상일 때 조회
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();

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
                        // 데이터 로드 후 UI 업데이트를 위해 메인 스레드에 메시지 보내기
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            } else if (rg_btn2.getText().equals("당뇨병")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // open API을 통해 정보를 가진 xml문서를 읽어와서 listView에 보여주기
                        items.clear();

                        //api 호출을 위한 uri 생성
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
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

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "당뇨병"; //질병명 작성
                            String searchAsmGrd = "3"; //조회는 3회이상만

                            while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG: // 태그의 시작일 때
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText(); //현재 질병명 가져오기
                                            // 현재 질병명 과 찾고자 하는 질병명이 일치할때 and 우수기간 선정 3회 이상일 때 조회
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();

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
                        // 데이터 로드 후 UI 업데이트를 위해 메인 스레드에 메시지 보내기
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }else if (rg_btn2.getText().equals("급성기 뇌졸중")) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // open API을 통해 정보를 가진 xml문서를 읽어와서 listView에 보여주기
                        items.clear();

                        //api 호출을 위한 uri 생성
                        String queryUrl = "https://apis.data.go.kr/B551182/exclInstHospAsmInfoService/getExclInstHospAsmInfo?serviceKey=RTtmgcCboZ5HFOb1%2FbZ8ad3E9JzY5H73vHsBzNNGtG4H3DoOQmrCi9SSNHGnm8%2F7P7rQTumoqPE1QyRbuVpeVA%3D%3D" +
                                "&pageNo=1" +
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

                            String currentAsmNm = "";
                            String countAsmGrd = "";
                            String searchAsmNm = "급성기 뇌졸중"; //질병명 작성
                            String searchAsmGrd = "3"; //조회는 3회이상만

                            while (eventType != XmlPullParser.END_DOCUMENT) { //XML 문서 끝날때까지 반복

                                switch (eventType) {
                                    case XmlPullParser.START_DOCUMENT:
                                        break;

                                    case XmlPullParser.START_TAG: // 태그의 시작일 때
                                        tag = xpp.getName();

                                        if (tag.equals("item")) ;
                                        else if (tag.equals("asmNm")) {
                                            xpp.next();
                                            currentAsmNm = xpp.getText(); //현재 질병명 가져오기
                                            // 현재 질병명 과 찾고자 하는 질병명이 일치할때 and 우수기간 선정 3회 이상일 때 조회
                                            if (currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("\n");
                                                buffer.append("질병명 : ");
                                                buffer.append(currentAsmNm);
                                                buffer.append("\n");
                                            }

                                        } else if (tag.equals("yadmNm")) {
                                            if (currentAsmNm != null && currentAsmNm.equals(searchAsmNm) && countAsmGrd.equals(searchAsmGrd)) {
                                                buffer.append("병원명 : ");
                                                xpp.next();
                                                buffer.append(xpp.getText());
                                                buffer.append("\n");
                                                buffer.append("우수기관 선정: 3회 연속 지정 병원");
                                                buffer.append("\n");
                                            }
                                        } else if (tag.equals("asmGrd")) {
                                            xpp.next();
                                            countAsmGrd = xpp.getText();

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
                        // 데이터 로드 후 UI 업데이트를 위해 메인 스레드에 메시지 보내기
                        handler.sendEmptyMessage(0);
                    }
                }).start();
            }


        }

    }
    //Handler 선언
    Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            // UI 업데이트 코드
            adapter.notifyDataSetChanged();
        }
    };
}

