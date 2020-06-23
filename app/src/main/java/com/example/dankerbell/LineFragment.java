package com.example.dankerbell;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.Fragment;


import com.google.firebase.ml.common.FirebaseMLException;

import java.util.ArrayList;

import im.dacer.androidcharts.LineView;

import static com.example.dankerbell.homeActivity.blood;

public class LineFragment extends Fragment {
    static LineFragment instance;
    static ArrayList<Float> realBG = new ArrayList<>();
    static ArrayList<Float> predBG = new ArrayList<>();
    ArrayList<String> dateTime;
    LineView lineViewFloat;
    float prediction = 0.0f;
    ForecastingBG fbg = null;

    public static LineFragment getInstance() {
        if (instance == null) {
            instance = new LineFragment();
        }
        return instance;
    }
    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {

        ArrayList<String> tmp = new ArrayList<>();
        Log.i("create","view");
        View rootView = inflater.inflate(R.layout.fragment_line, container, false);
        lineViewFloat = (LineView) rootView.findViewById(R.id.line_view_float);
        if(lineViewFloat ==null){ Log.i(String.valueOf(getActivity()),"lineView가 존재하지 않음");}



        try {
            fbg = new ForecastingBG();
        } catch (FirebaseMLException e) {
            Log.e(getClass().getName(), "new Forecasting 되지않음");
        }
        fbg.runModel();
        //예측 실행
        fbg.fbgHandler = new Handler(){
            @Override public void handleMessage(Message msg) {
                if (msg.what == 1000) {

                    prediction = fbg.getPrediction();
                    Log.e(getClass().getName(), "그래프에 적용될 예측값 >>" + prediction);
                    initLineView(lineViewFloat);
                }
                else{
                    prediction = 0.0f;
                    Log.e(getClass().getName(), "cant do prediction");
                    initLineView(lineViewFloat);
                }
            }
        };

        return rootView;
    }
    //데이터 로드
    //로드 후 1주일치 이상이 아니면 예측 보여주지 않음
    //데이터가 1주일 이상이다. ForecastingBG

    private synchronized void initLineView(LineView lineView) {

        realBG.clear();
        predBG.clear();
        //label
        dateTime = new ArrayList<String>();
        dateTime.add("기상 후");
        dateTime.add("아침");
        dateTime.add("점심");
        dateTime.add("저녁");
        dateTime.add("취침 전");

        Log.i("Linefragment","레이블 추가 완");

        //homeactivity에서 당일의 혈당만을 읽어옴
        homeActivity h = new homeActivity();
        ArrayList<String> dlist = h.blood; //home activity의 blood static arraylist에 있음
        Log.i("Lingfragment", ""+ String.valueOf(blood.isEmpty()));

        for(String a: blood){
            Log.e("blood가 가진 값","|"+ a+"|");
        }

        for(int i = 0; i< blood.size(); i++){
             Log.i(String.valueOf(getActivity()), "받아온 값 :"+i+">>"+blood.get(i));
            if (i == 0 && blood.get(i).isEmpty() ){ //하루의 첫번째 값(기상후)이 null이라면
                realBG.add((float) 0.0); //0.0으로 나타낸다. 이전날의 마지막 값으로 하면 더 좋을 듯
            }
            else if(blood.get(i).isEmpty()){ //값이 null이면
                if(!blood.get(i-1).isEmpty()){
                    realBG.add(Float.parseFloat(blood.get(i-1))); //이전 time의 혈당 값으로 저장
                }
            }
            else{
                realBG.add(Float.parseFloat(blood.get(i))); //값이 존재한다면 그대로 출력
            }
        }

        predBG.addAll(realBG);
        predBG.add((float) (Math.round(prediction*100)/100.0)); //예측값 소수점 아래 세번째 자리에서 반올림

        Log.i("draw","line");
        // 그림그리는 라인 getData로 넣을 값 homeact에서 전달해주고 이메소드 불러주기
        ArrayList<ArrayList<Float>> datalists = new ArrayList<>();
        datalists.add(predBG);
        datalists.add(realBG);
        //get data method
        //draw graph
        if(lineViewFloat == null){
            Log.i("NULL","lineview is null");
        }
        lineViewFloat.setDrawDotLine(true);
        lineViewFloat.setShowPopup(LineView.SHOW_POPUPS_MAXMIN_ONLY); //최대최소값 마커 없앨거면 show_popups_none
        lineViewFloat.setColorArray(new int[]{Color.parseColor("#82BEE6"),Color.parseColor("#FBDD65")});
        Log.i(getActivity().getLocalClassName(),dateTime.size()+"/"+datalists.get(0).size()+" , "+datalists.get(1).size());
        lineViewFloat.setBottomTextList(dateTime);
        lineViewFloat.setFloatDataList(datalists);
        Log.i(getActivity().getLocalClassName(), "onCreate: lineview set완료");
    }

    private ArrayList<Float> loadRealBG() {
        ArrayList<Float> result = new ArrayList<>();
        return result;
    }
}
