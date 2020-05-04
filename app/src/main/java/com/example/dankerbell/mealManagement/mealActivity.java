package com.example.dankerbell.mealManagement;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.dankerbell.R;
import com.example.dankerbell.bloodManagement.bloodActivity;
import com.example.dankerbell.pillManagement.pillActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class mealActivity extends AppCompatActivity { // 식단관리 클래스
    TextView home;
    TextView blood_txt,pill_txt;
    SearchmealActivity sm;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);

        //날짜 설정
        final TextView currentdate=findViewById(R.id.date);
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
        final Calendar calendar = Calendar.getInstance(); // 오늘날짜
        final String date = sdf.format(calendar.getTime());
        currentdate.setText(date);
        TextView prev=findViewById(R.id.prev);
        TextView next=findViewById(R.id.next);
        prev.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) { // 상단에 날짜 중 < 버튼 클릭
                SimpleDateFormat sdf2 = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
                calendar.add(Calendar.DATE, -1);  // 오늘 날짜에서 하루를 뺌.
                String yesterday = sdf2.format(calendar.getTime());
                currentdate.setText(yesterday);

            }
        });
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 상단에 날짜 중 > 버튼 클릭
                SimpleDateFormat sdf3 = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
                calendar.add(Calendar.DATE, +1);  // 오늘 날짜에서 하루를 더함
                String tomorrow = sdf3.format(calendar.getTime());
                currentdate.setText(tomorrow);
            }
        });

        final TextView morningmealmore=findViewById(R.id.morningmealmore); // 아침 식단 펼치기
        final TextView morningmealless=findViewById(R.id.morningmealless); // 아침 식단 접기
        final LinearLayout morningmeal=findViewById(R.id.morningmeal); // 아침 식단 총 layout
        final LinearLayout morningtext1=findViewById(R.id.morningtext1); // 아침식단입력창1
        final LinearLayout morningtext2=findViewById(R.id.morningtext2); // 아침식단입력창2
        final LinearLayout morningtext3=findViewById(R.id.morningtext3); // 아침식단입력창3
        final ImageView addmorningtext1=findViewById(R.id.addmorningtext1); // 아침식단입력창1에 + 버튼1
        final ImageView addmorningtext2=findViewById(R.id.addmorningtext2); // 아침식단입력창2에 + 버튼2
        final ImageView addmorningtext3=findViewById(R.id.addmorningtext3); // 아침식단입력창3에 + 버튼3
        final TextView morningfinish=findViewById(R.id.morningfinish); // 아침식단 체크 버튼
        final Button morningsearch1=findViewById(R.id.morningsearch1);
        final EditText morningfood1=findViewById(R.id.morningfood1);

        morningfinish.setOnClickListener(new View.OnClickListener() { // 아침식단 체크버튼 클릭 시 실행
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"아침 체크버튼 클릭");
                Log.d(this.getClass().getName(),"ㅇㅂㅇ");

            }
        });
  //      final TextView morningfinish=findViewById(R.id.morningfinish);
        morningsearch1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 아직 미완성
                Intent searchintent = new Intent(getApplicationContext(), SearchmealActivity.class);
                startActivity(searchintent);// 음식검색화면으로 이동

            }
        });


        addmorningtext1.setOnClickListener(new View.OnClickListener() { // 아침식단입력창1에 + 버튼1 클릭 시 실행
            @Override
            public void onClick(View view) {
                morningtext2.setVisibility(View.VISIBLE); // 아침식단입력창2 추가
                addmorningtext1.setVisibility(View.INVISIBLE); // 아침식단 추가버튼1 가리기
            }
        });
        addmorningtext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addmorningtext2.setVisibility(View.INVISIBLE);

                morningtext3.setVisibility(View.VISIBLE);
            }
        });

        morningmealmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 아침 펼치기 클릭 시 실행
                Log.d(this.getClass().getName(),"펼치기 클릭");
                morningmealmore.setVisibility(View.GONE); // 펼치기 버튼 사라짐
                morningfinish.setVisibility(View.VISIBLE);
                morningmealless.setVisibility(View.VISIBLE); // 접기 버튼 생성
                morningmeal.setVisibility(View.VISIBLE); // 아침식단 작성 layout 생성
            }
        });
        morningmealless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 아침 접기 클릭 시 실행
                Log.d(this.getClass().getName(),"접기 클릭");
                morningmealmore.setVisibility(View.VISIBLE); //펼치기 버튼 생성
                morningfinish.setVisibility(View.GONE);
                morningmealless.setVisibility(View.GONE); // 접기 버튼 사라짐
                morningmeal.setVisibility(View.GONE); // 아침식단 작성 layout 사라짐
            }
        });

        final TextView lunchmealmore=findViewById(R.id.lunchmealmore); // 점식식단 펼치기
        final TextView lunchmealless=findViewById(R.id.lunchmealless); // 점심식단 접기
        final LinearLayout lunchmeal=findViewById(R.id.lunchmeal); // 점심식단 펼치기 클릭하면 나타나는 화면
        final LinearLayout lunchtext1=findViewById(R.id.lunchtext1); // 점심식단입력창1
        final LinearLayout lunchtext2=findViewById(R.id.lunchtext2); // 점심식단입력창2
        final LinearLayout lunchtext3=findViewById(R.id.lunchtext3); // 점심식단입력창3
        final ImageView addlunchtext1=findViewById(R.id.addlunchtext1); // 점심식단입력창1에 + 버튼1
        final ImageView addlunchtext2=findViewById(R.id.addlunchtext2); // 점심식단입력창2에 + 버튼2
        final ImageView addlunchtext3=findViewById(R.id.addlunchtext3); // 점심식단입력창3에 + 버튼3
        final TextView lunchfinish=findViewById(R.id.lunchfinish); // 점심식단 체크버튼
        lunchfinish.setOnClickListener(new View.OnClickListener() { // 점심식단 체크버튼 클릭 시 실행
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"점심 체크버튼 클릭");

            }
        });
        lunchmealmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 점심 펼치기 클릭 시 실행
                Log.d(this.getClass().getName(),"펼치기 클릭");
                lunchmealmore.setVisibility(View.GONE); // 펼치기 버튼 사라짐
                lunchfinish.setVisibility(View.VISIBLE);
                lunchmealless.setVisibility(View.VISIBLE); // 접기 버튼 생성
                lunchmeal.setVisibility(View.VISIBLE); //점심식단 작성 layout 사라짐
            }
        });
        lunchmealless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 점심 접기 클릭 시 실행
                Log.d(this.getClass().getName(),"접기 클릭");
                lunchmealmore.setVisibility(View.VISIBLE); // 펼치기 버튼 생성
                lunchfinish.setVisibility(View.GONE);
                lunchmealless.setVisibility(View.GONE); // 접기 버튼 사라짐
                lunchmeal.setVisibility(View.GONE); // 점식식단 작성 layot 사라짐
            }
        });
        addlunchtext1.setOnClickListener(new View.OnClickListener() { // 점심식단입력창1의 + 버튼1 클릭 시 실행
            @Override
            public void onClick(View view) {
                lunchtext2.setVisibility(View.VISIBLE); // 점심식단입력창2 추가
                addlunchtext1.setVisibility(View.INVISIBLE); // 아침식단 추가버튼1 가리기
            }
        });
        addlunchtext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// 점심식단입력창2의 + 버튼2 클릭 시 실행
                addlunchtext2.setVisibility(View.INVISIBLE); // 버튼2 가리기
                lunchtext3.setVisibility(View.VISIBLE); // 점심식단입력창3추가
            }
        });

        final TextView dinnermealmore=findViewById(R.id.dinnermealmore);
        final TextView dinnermealless=findViewById(R.id.dinnermealless);
        final LinearLayout dinnermeal=findViewById(R.id.dinnermeal);
        final LinearLayout dinnertext1=findViewById(R.id.dinnertext1); // 저녁식단입력창1
        final LinearLayout dinnertext2=findViewById(R.id.dinnertext2); // 저녁식단입력창2
        final LinearLayout dinnertext3=findViewById(R.id.dinnertext3); // 저녁식단입력창3
        final ImageView adddinnertext1=findViewById(R.id.adddinnertext1); // 저녁식단입력창1에 + 버튼1
        final ImageView adddinnertext2=findViewById(R.id.adddinnertext2); // 저녁식단입력창2에 + 버튼2
        final ImageView adddinnertext3=findViewById(R.id.adddinnertext3); // 저녁식단입력창3에 + 버튼3
        final TextView dinnerfinish=findViewById(R.id.dinnerfinish);

        dinnerfinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"저녁 체크버튼 클릭");

            }
        });
        dinnermealmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 저녁 펼치기 클릭 시 실행
                Log.d(this.getClass().getName(),"펼치기 클릭");
                dinnermealmore.setVisibility(View.GONE); // 펼치기 버튼 사라짐
                dinnerfinish.setVisibility(View.VISIBLE);
                dinnermealless.setVisibility(View.VISIBLE); // 접기 버튼 생성
                dinnermeal.setVisibility(View.VISIBLE); // 저녁식단 작성 layout 생성
            }
        });

        dinnermealless.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 저녁 접기 클릭 시 실행
                Log.d(this.getClass().getName(),"접기 클릭");
                dinnermealmore.setVisibility(View.VISIBLE); // 펼치기 버튼 생성
                dinnerfinish.setVisibility(View.GONE);
                dinnermealless.setVisibility(View.GONE); // 접기 버튼 사라짐
                dinnermeal.setVisibility(View.GONE); // 저녁식단 작성 layout 사라짐
            }
        });

        adddinnertext1.setOnClickListener(new View.OnClickListener() { // 저녁식단입력창1의 + 버튼1 클릭 시 실행
            @Override
            public void onClick(View view) {
                dinnertext2.setVisibility(View.VISIBLE); // 저녁식단입력창2 추가
                adddinnertext1.setVisibility(View.INVISIBLE); // 저녁식단 추가버튼1 가리기
            }
        });
        adddinnertext2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {// 저녁식단입력창2의 + 버튼2 클릭 시 실행
                adddinnertext2.setVisibility(View.INVISIBLE); // 버튼2 가리기
                dinnertext3.setVisibility(View.VISIBLE); // 저녁식단입력창3추가
            }
        });

        home=findViewById(R.id.home_txt);

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        blood_txt=findViewById(R.id.blood_txt); // 상단에 있는 혈당관리 TextView
        pill_txt=findViewById(R.id.pill_txt); // 상단에 있는 복약관리 TextView
        blood_txt.setOnClickListener(new View.OnClickListener() { // 상단에 있는 혈당관리 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent bloodintent = new Intent(getApplicationContext(), bloodActivity.class);
                startActivity(bloodintent);// 혈당관리 화면으로 이동하도록 bloodActivity로 전환
            }
        });
        pill_txt.setOnClickListener(new View.OnClickListener() { // 상단에 있는 복약관리 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent pillintent = new Intent(getApplicationContext(), pillActivity.class);
                startActivity(pillintent);// 복약관리 화면으로 이동하도록 pillActivity로 전환
            }
        });
    }

}
