package com.example.dankerbell.bloodManagement;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;



import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import com.example.dankerbell.Firebase.BloodSugarCrud;
import com.example.dankerbell.ProfileActivity;
import com.example.dankerbell.R;
import com.example.dankerbell.SamsungStepCountReporter;
import com.example.dankerbell.SamsungweightReporter;
import com.example.dankerbell.bloodManagement.BloodReporter;
import com.example.dankerbell.bloodManagement.glucoseReporter;
import com.example.dankerbell.homeActivity;
import com.example.dankerbell.mealManagement.mealActivity;
import com.example.dankerbell.pillManagement.pillActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.samsung.android.sdk.healthdata.HealthConnectionErrorResult;
import com.samsung.android.sdk.healthdata.HealthConstants;
import com.samsung.android.sdk.healthdata.HealthDataStore;
import com.samsung.android.sdk.healthdata.HealthPermissionManager;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionKey;
import com.samsung.android.sdk.healthdata.HealthPermissionManager.PermissionType;



import java.text.SimpleDateFormat;

import java.util.Calendar;

import java.util.HashSet;

import java.util.Locale;

import java.util.Map;

import java.util.Set;



import butterknife.BindView;

import butterknife.ButterKnife;

public class bloodActivity extends AppCompatActivity{ // 혈당관리클래스
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    final String User = user.getEmail();

    BloodSugarCrud mBloodSugar = BloodSugarCrud.getInstance(); //firebase 참조 singletone
    cholesterolCrud mchol=cholesterolCrud.getInstance();
    TextView toolbar;
    TextView close;
    DrawerLayout drawerLayout;
    View drawerView;
    Button logout,drawer_pill,drawer_meal,drawer_blood;
    Button mypage;
    TextView home; //
    TextView meal_txt; // 상단에 식단관리 TextView
    TextView pill_txt; // 상단에 복약관리 TextView
        TextView wakeup,wakeUp,wakeupbloodedit,wakesugartext, wakepressuretext,wakepressuretext2,wakeupbloodfinish;  // 기상 후 !!!
    EditText inputdang,inputcol;
    // wakeUp,wakeup=기상후 , wakeupbloodedit : 기상 후 연필 아이콘 , wakesugartext : 기상 후 혈당 text wakepressuretext : 기상 후 혈압text
    // wakepressuretext : 기상 후 혈압 text wakeupbloodfinish : 체크 아이콘

    EditText wakesugaredit, wakepressureedit,wakepressureedit2; // 기상 후 혈당입력칸 / 혈압 입력칸
    TextView Morning,morning,morningbloodedit,morningsugartext, morningpressuretext,morningpressuretext2,morningbloodfinish;  // 아침 !!!
    EditText morningsugaredit, morningpressureedit,morningpressureedit2; // 아침 혈당입력칸 / 혈압 입력칸
    TextView Lunch,lunch,lunchbloodedit,lunchsugartext, lunchpressuretext,lunchpressuretext2,lunchbloodfinish;  // 점심 !!!

    // Lunch,lunch : 점심 , lunchbloodedit : 점심 연필 아이콘 , lunchsugartext : 점심 혈당 text, lunchbloodfinish : 점심 혈압 text

    // lunchpressuretext : 점심 혈압 text lunchbloodfinish : 체크 아이콘

    EditText lunchsugaredit, lunchpressureedit,lunchpressureedit2; // 점심 혈당입력칸 / 혈압 입력칸


    TextView Dinner,dinner,dinnerbloodedit,dinnersugartext, dinnerpressuretext,dinnerpressuretext2,dinnerbloodfinish;  // 저녁 !!!
    EditText dinnersugaredit, dinnerpressureedit,dinnerpressureedit2; // 저녁 혈당입력칸 / 혈압 입력칸
    TextView Sleep,sleep,sleepbloodedit,sleepsugartext, sleeppressuretext,sleeppressuretext2,sleepbloodfinish;  // 취침전 !!!
    EditText sleepsugaredit, sleeppressureedit,sleeppressureedit2; // 취침 전 혈당입력칸 / 혈압 입력칸

    TableLayout wakeuptext,wakeupedit,morningtext,morningedit,lunchtext,lunchedit,dinnertext,dinneredit,sleeptext,sleepedit;

    TextView userid;

    TextView currentdate;
    TextView prev,next;
    private static Handler mHandler ;

    String time; // 기상후, 식전 식후 등
    public static final String APP_TAG = "DANGKERBELL";

    TextView textdangwha,textcol,editcol,finishcol;


    //@BindView(R.id.editHealthDateValue1) TextView mStepCountTv;



    private HealthDataStore mStore;

    //private StepCountReporter mReporter;

    BloodReporter mReporter;
    glucoseReporter gluecoseReporter;
    SamsungStepCountReporter hReporter;
    SamsungweightReporter wReporter;
    private Set<HealthPermissionManager.PermissionKey> mKeySet;



    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood);
        home=findViewById(R.id.home_txt);

        meal_txt=findViewById(R.id.meal_txt);
        pill_txt=findViewById(R.id.pill_txt);
        currentdate=findViewById(R.id.date);
        userid=findViewById(R.id.userid);
        inputdang=findViewById(R.id.inputdangwha);
        textdangwha=findViewById(R.id.textdangwha);
        textcol=findViewById(R.id.textcol);
        editcol=findViewById(R.id.editcol);
        finishcol=findViewById(R.id.finishcol);
        inputcol=findViewById(R.id.inputcol);
        mKeySet = new HashSet<HealthPermissionManager.PermissionKey>();
        mKeySet.add(new PermissionKey(HealthConstants.BloodPressure.HEALTH_DATA_TYPE, PermissionType.READ));
        mKeySet.add(new PermissionKey(HealthConstants.BloodGlucose.HEALTH_DATA_TYPE, PermissionType.READ));
//        mKeySet.add(new PermissionKey(HealthConstants.Height.HEALTH_DATA_TYPE, PermissionType.READ));
//        mKeySet.add(new PermissionKey(HealthConstants.Weight.HEALTH_DATA_TYPE, PermissionType.READ));



        final SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
        final SimpleDateFormat timeinclude = new SimpleDateFormat("yy.MM.dd.HH:MM", Locale.getDefault());

        final Calendar calendar = Calendar.getInstance(); // 오늘날짜

        final String date = sdf.format(calendar.getTime());
        final String timeminutedate=timeinclude.format(calendar.getTime());
        wakesugartext=findViewById(R.id.wakesugartext);
        wakepressuretext=findViewById(R.id.wakepressureetext);
        wakepressuretext2=findViewById(R.id.wakepressureetext2);
        wakepressureedit=findViewById(R.id.wakepressureedit);
        wakepressureedit2=findViewById(R.id.wakepressureedit2);

        wakeuptext=findViewById(R.id.wakeuptext);
        wakeupedit=findViewById(R.id.wakeupedit);
        mBloodSugar.wakeupread(date);
        mBloodSugar.morningread(date);
        mBloodSugar.lunchread(date);
        mBloodSugar.dinnerread(date);
        mBloodSugar.sleepread(date);

        toolbar=findViewById(R.id.toolbar_menu);
        drawerLayout=findViewById(R.id.drawer_layout) ;
        mypage = findViewById(R.id.mypage);
        logout = findViewById(R.id.logout);
        drawer_blood=findViewById(R.id.drawer_blood);
        drawer_meal=findViewById(R.id.drawer_meal);
        drawer_pill=findViewById(R.id.drawer_pill);

        drawerView=findViewById(R.id.drawer);
        close=findViewById(R.id.toolbar_close);
        userid=findViewById(R.id.userid);
        userid.setText(User);

        finishcol.setOnClickListener(new View.OnClickListener() { //당화혈 체크 클릭
            @Override
            public void onClick(View view) {
                mchol.create(inputcol.getText().toString(),inputdang.getText().toString(),date);
                textcol.setText(inputcol.getText());
                textdangwha.setText(inputdang.getText());
                inputcol.setVisibility(View.GONE);
                inputdang.setVisibility(View.GONE);
                textcol.setVisibility(View.VISIBLE);
                textdangwha.setVisibility(View.VISIBLE);
                editcol.setVisibility(View.VISIBLE);
                finishcol.setVisibility(View.GONE);
            }
        });
        mchol.read(date);
        mchol.cholHandler = new Handler(){

            @Override public void handleMessage(Message msg){
                if (msg.what==1001){
                 textcol.setText(mchol.getCholesterol());
                 textdangwha.setText(mchol.getDangwha());
                }
            }

        };

        editcol.setOnClickListener(new View.OnClickListener() { //당화혈 수정 클릭
            @Override
            public void onClick(View view) {
                textcol.setVisibility(View.GONE);
                textdangwha.setVisibility(View.GONE);
                inputcol.setVisibility(View.VISIBLE);
                inputdang.setVisibility(View.VISIBLE);
                editcol.setVisibility(View.GONE);
                finishcol.setVisibility(View.VISIBLE);
            }
        });

        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                Intent drawer = new Intent(getApplicationContext(), DrawerActivity.class);

//                startActivity(drawer);//액티비티 띄우기

                drawerLayout.openDrawer(drawerView);
            }
        });

        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                drawerLayout.closeDrawer(drawerView);
            }
        });

        mypage.setOnClickListener(new View.OnClickListener() { // 내 정보 버튼 클릭 시 실행
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                startActivity(intent);//액티비티 띄우기
            }
        });
        drawer_pill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pill = new Intent(getApplicationContext(), pillActivity.class);
                startActivity(pill);//혈당관리 클래스 전환
            }
        });
        drawer_blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent blood = new Intent(getApplicationContext(), bloodActivity.class);
                startActivity(blood);//혈당관리 클래스 전환
            }
        });
        drawer_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent meal = new Intent(getApplicationContext(), mealActivity.class);
                startActivity(meal);//식단관리 클래스 전환
            }
        });


        logout.setOnClickListener(new View.OnClickListener() { // 로그아웃 버튼 클릭
            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"로그아웃 클릭");
                //signOut();
            }
        });



//        mBloodSugar.wakeupbloodPressure(date);

//        mBloodSugar.readwakeup(date);

        //  mBloodSugar.morning(date);

        mBloodSugar.mHandler1 = new Handler(){

            @Override public void handleMessage(Message msg){
                if (msg.what==1000){
                    wakesugartext.setText(mBloodSugar.getBloodsugar());
                    wakepressuretext.setText(mBloodSugar.getBloodpressure());
                    wakepressuretext2.setText(mBloodSugar.getBloodpressure2());
                    morningsugartext.setText(mBloodSugar.getmBloodsugar());
                    morningpressuretext.setText(mBloodSugar.getmBloodpressure());
                    morningpressuretext2.setText(mBloodSugar.getMbloodpressure2());
                    lunchsugartext.setText(mBloodSugar.getLbloodsugar());
                    lunchpressuretext.setText(mBloodSugar.getLbloodpressure());
                    lunchpressuretext2.setText(mBloodSugar.getLbloodpressure2());
                    dinnersugartext.setText(mBloodSugar.getDbloodsugar());
                    dinnerpressuretext.setText(mBloodSugar.getDbloodpressure());
                    dinnerpressuretext2.setText(mBloodSugar.getDbloodpressure2());
                    sleepsugartext.setText(mBloodSugar.getSbloodsugar());
                    sleeppressuretext.setText(mBloodSugar.getSbloodpressure());
                    sleeppressuretext2.setText(mBloodSugar.getSbloodpressure2());
                }
            }

        };



        currentdate.setText(date);
        prev=findViewById(R.id.prev);
        next=findViewById(R.id.next);



        //mBloodSugar.wakeupbloodSugar(mBloodSugar.getBloodpressure());

        // mBloodSugar.wakeupbloodPressure(mBloodSugar.getBloodsugar());





        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { // 상단에 날짜 중 < 버튼 클릭
                SimpleDateFormat sdf2 = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
                calendar.add(Calendar.DATE, -1);  // 오늘 날짜에서 하루를 뺌.
                String yesterday = sdf.format(calendar.getTime());

                mBloodSugar.wakeupread(yesterday);
                mBloodSugar.morningread(yesterday);
                mBloodSugar.lunchread(yesterday);
                mBloodSugar.dinnerread(yesterday);
                mBloodSugar.sleepread(yesterday);
                mchol.read(yesterday);

                //mBloodSugar.wakeupbloodPressure(yesterday);

                // mBloodSugar.readwakeup(yesterday);

                //mBloodSugar.morning(yesterday);

                currentdate.setText(yesterday);
                mchol.cholHandler = new Handler(){

                    @Override public void handleMessage(Message msg){
                        if (msg.what==1001){
                            textcol.setText(mchol.getCholesterol());
                            textdangwha.setText(mchol.getDangwha());
                        }
                    }

                };
                mBloodSugar.mHandler1 = new Handler(){

                    @Override public void handleMessage(Message msg){
                        if (msg.what==1000){
                            Log.d("메세지 받음",mBloodSugar.getBloodsugar());
                            wakesugartext.setText(mBloodSugar.getBloodsugar());
                            wakepressuretext.setText(mBloodSugar.getBloodpressure());
                            wakepressuretext2.setText(mBloodSugar.getBloodpressure2());
                            morningsugartext.setText(mBloodSugar.getmBloodsugar());
                            morningpressuretext.setText(mBloodSugar.getmBloodpressure());
                            morningpressuretext2.setText(mBloodSugar.getMbloodpressure2());
                            lunchsugartext.setText(mBloodSugar.getLbloodsugar());
                            lunchpressuretext.setText(mBloodSugar.getLbloodpressure());
                            lunchpressuretext2.setText(mBloodSugar.getLbloodpressure2());
                            dinnersugartext.setText(mBloodSugar.getDbloodsugar());
                            dinnerpressuretext.setText(mBloodSugar.getDbloodpressure());
                            dinnerpressuretext2.setText(mBloodSugar.getDbloodpressure2());
                            sleepsugartext.setText(mBloodSugar.getSbloodsugar());
                            sleeppressuretext.setText(mBloodSugar.getSbloodpressure());
                            sleeppressuretext2.setText(mBloodSugar.getSbloodpressure2());
                        }
                    }

                };
            }
        });



        next.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) { // 상단에 날짜 중 > 버튼 클릭

                calendar.add(Calendar.DATE, +1);  // 오늘 날짜에서 하루를 더함
                String tomorrow = sdf.format(calendar.getTime());
                currentdate.setText(tomorrow);

                //  mBloodSugar.readwakeup(tomorrow);
                mchol.read(tomorrow);
                mBloodSugar.wakeupread(tomorrow);
                mBloodSugar.morningread(tomorrow);
                mBloodSugar.lunchread(tomorrow);
                mBloodSugar.dinnerread(tomorrow);
                mBloodSugar.sleepread(tomorrow);

//                mBloodSugar.wakeupbloodPressure(tomorrow);

                //    mBloodSugar.morning(tomorrow);
                mchol.cholHandler = new Handler(){

                    @Override public void handleMessage(Message msg){
                        if (msg.what==1001){
                            textcol.setText(mchol.getCholesterol());
                            textdangwha.setText(mchol.getDangwha());
                        }
                    }

                };
                mBloodSugar.mHandler1 = new Handler(){

                    @Override public void handleMessage(Message msg){

                        if (msg.what==1000){

                            Log.d("메세지 받음",mBloodSugar.getBloodsugar());
                            wakesugartext.setText(mBloodSugar.getBloodsugar());
                            wakepressuretext.setText(mBloodSugar.getBloodpressure());
                            wakepressuretext2.setText(mBloodSugar.getBloodpressure2());
                            morningsugartext.setText(mBloodSugar.getmBloodsugar());
                            morningpressuretext.setText(mBloodSugar.getmBloodpressure());
                            morningpressuretext2.setText(mBloodSugar.getMbloodpressure2());
                            lunchsugartext.setText(mBloodSugar.getLbloodsugar());
                            lunchpressuretext.setText(mBloodSugar.getLbloodpressure());
                            lunchpressuretext2.setText(mBloodSugar.getLbloodpressure2());
                            dinnersugartext.setText(mBloodSugar.getDbloodsugar());
                            dinnerpressuretext.setText(mBloodSugar.getDbloodpressure());
                            dinnerpressuretext2.setText(mBloodSugar.getDbloodpressure2());
                            sleepsugartext.setText(mBloodSugar.getSbloodsugar());
                            sleeppressuretext.setText(mBloodSugar.getSbloodpressure());
                            sleeppressuretext2.setText(mBloodSugar.getSbloodpressure2());
                        }

                    }

                };



            }

        });



//새로 추가

// 기상 후
        wakeup=findViewById(R.id.wakeup);
        wakeUp=findViewById(R.id.wakeUp);
        wakeupbloodedit=(TextView)findViewById(R.id.wakeupbloodedit);
        wakeupbloodfinish=findViewById(R.id.wakeupbloodfinish);
        wakesugaredit=findViewById(R.id.wakesugaredit);
        wakepressureedit=findViewById(R.id.wakepressureedit);
        wakepressureedit2=findViewById(R.id.wakepressureedit2);

        //아침
        morning=findViewById(R.id.morning);
        Morning=findViewById(R.id.Morning);
        morningbloodedit=findViewById(R.id.morningbloodedit);
        morningsugartext=findViewById(R.id.morningsugartext);
        morningpressuretext=findViewById(R.id.morningpressuretext);
        morningpressuretext2=findViewById(R.id.morningpressuretext2);
        morningtext=findViewById(R.id.morningtext);
        morningedit=findViewById(R.id.morningedit);
        morningbloodfinish=findViewById(R.id.morningbloodfinish);
        morningsugaredit=findViewById(R.id.morningsugaredit);
        morningpressureedit=findViewById(R.id.morningpressureedit);
        morningpressureedit2=findViewById(R.id.morningpressureedit2);
        //점심

        lunch=findViewById(R.id.lunch);
        Lunch=findViewById(R.id.Lunch);
        lunchbloodedit=findViewById(R.id.lunchbloodedit);
        lunchsugartext=findViewById(R.id.lunchsugartext);
        lunchtext=findViewById(R.id.lunchtext);
        lunchedit=findViewById(R.id.lunchedit);
        lunchpressuretext2=findViewById(R.id.lunchpressuretext2);
        lunchpressuretext=findViewById(R.id.lunchpressuretext);
        lunchbloodfinish=findViewById(R.id.lunchbloodfinish);
        lunchsugaredit=findViewById(R.id.lunchsugaredit);
        lunchpressureedit=findViewById(R.id.lunchpressureedit);
        lunchpressureedit2=findViewById(R.id.lunchpressureedit2);



        //저녁
        dinner=findViewById(R.id.dinner);
        Dinner=findViewById(R.id.Dinner);
        dinnertext=findViewById(R.id.dinnertext);
        dinneredit=findViewById(R.id.dinneredit);
        dinnerbloodedit=findViewById(R.id.dinnerbloodedit);
        dinnersugartext=findViewById(R.id.dinnersugartext);
        dinnerpressuretext=findViewById(R.id.dinnerpressuretext);
        dinnerpressuretext2=findViewById(R.id.dinnerpressuretext2);
        dinnerbloodfinish=findViewById(R.id.dinnerbloodfinish);
        dinnersugaredit=findViewById(R.id.dinnersugaredit);
        dinnerpressureedit=findViewById(R.id.dinnerpressureedit);
        dinnerpressureedit2=findViewById(R.id.dinnerpressureedit2);



        //취침 후

        sleep=findViewById(R.id.sleep);
        Sleep=findViewById(R.id.Sleep);
        sleeptext=findViewById(R.id.sleeptext);
        sleepedit=findViewById(R.id.sleepedit);
        sleepbloodedit=findViewById(R.id.sleepbloodedit);
        sleepsugartext=findViewById(R.id.sleepsugartext);
        sleeppressuretext=findViewById(R.id.sleeppressuretext);
        sleeppressuretext2=findViewById(R.id.sleeppressuretext2);
        sleepbloodfinish=findViewById(R.id.sleepbloodfinish);
        sleepsugaredit=findViewById(R.id.sleepsugaredit);
        sleeppressureedit=findViewById(R.id.sleeppressureedit);
        sleeppressureedit2=findViewById(R.id.sleeppressureedit2);



        wakeupbloodedit.setOnClickListener(new View.OnClickListener() { // 기상 후 행의 연필 아이콘 클릭 시 실행

            @Override
            public void onClick(View view) {
                Log.d(this.getClass().getName(),"연필클릭");
                wakeUp.setVisibility(View.GONE);
                wakeupbloodedit.setVisibility(View.GONE);
                wakesugartext.setVisibility(View.GONE);
                wakepressuretext.setVisibility(View.GONE);
                wakeupbloodfinish.setVisibility(View.VISIBLE);
                wakesugaredit.setVisibility(View.VISIBLE);
                wakepressureedit.setVisibility(View.VISIBLE);
                wakepressureedit2.setVisibility(View.VISIBLE);
                wakeuptext.setVisibility(View.GONE);
                wakeupedit.setVisibility(View.VISIBLE);
                wakeup.setVisibility(View.VISIBLE);
            }
        });



        wakeupbloodfinish.setOnClickListener(new View.OnClickListener() { // 새로 추가한 부분

            //입력을 다하고, 기상 후 체크모양 클릭 시 다시 TextView로 전환, 입력한 혈당,혈압 값 set 설정

            //데베 삽입 O

            @Override

            public void onClick(View view) {

                Log.d(this.getClass().getName(),"체크 클릭");

                wakeup.setVisibility(View.GONE);
                wakeUp.setVisibility(View.VISIBLE);
                wakeupbloodedit.setVisibility(View.VISIBLE);
                wakesugartext.setVisibility(View.VISIBLE);
                wakepressuretext.setVisibility(View.VISIBLE);
                wakepressuretext2.setVisibility(View.VISIBLE);
                wakeupbloodfinish.setVisibility(View.GONE);
                wakesugaredit.setVisibility(View.GONE);
                wakepressureedit2.setVisibility(View.GONE);
                wakeuptext.setVisibility(View.VISIBLE);
                wakeupedit.setVisibility(View.GONE);
                wakeupedit.setVisibility(View.GONE);
                wakepressureedit.setVisibility(View.GONE);
                String wakesugar=(String)wakesugaredit.getText().toString();
                wakesugartext.setText(wakesugar);



                String wakepressure1 = (String)wakepressureedit.getText().toString(); //수축기

                String wakepressure2 = (String)wakepressureedit2.getText().toString();



                if(wakepressure1.equals("")){

                    wakepressure1="";

                }

                // else if(wakepressureedit2.equals())

                wakepressuretext.setText(wakepressure1);

                if(wakepressure2.equals(""))

                    wakepressure2="";

                wakepressuretext2.setText(wakepressure2);



                /*

                bloodsugarDB : userid - { bs, bp, date, time }

                 */

                // DB 삽입부

                time = wakeup.toString();

                time = "기상 후";

                Log.d(this.getClass().getName(),wakesugar+"이거닷~!!!!!!1");

             //  mBloodSugar.create(Double.parseDouble(wakesugar), Double.parseDouble(wakepressure1),Double.parseDouble(wakepressure2),date, time);
                mBloodSugar.create(Double.parseDouble(wakesugar), Double.parseDouble(wakepressure1),Double.parseDouble(wakepressure2),timeminutedate, time);

            }



        });

        morningbloodedit.setOnClickListener(new View.OnClickListener() { // 아침 행의 연필 아이콘 클릭 시 실행

            @Override

            public void onClick(View view) {

                Log.d(this.getClass().getName(),"왜 앙대 바부야");

                Morning.setVisibility(View.GONE);

                morningbloodedit.setVisibility(View.GONE);

                morningsugartext.setVisibility(View.GONE);

                morningpressuretext.setVisibility(View.GONE);

                morningpressuretext2.setVisibility(View.GONE);

                morningbloodfinish.setVisibility(View.VISIBLE);

                morningsugaredit.setVisibility(View.VISIBLE);

                morningpressureedit.setVisibility(View.VISIBLE);

                morningpressureedit2.setVisibility(View.VISIBLE);

                morning.setVisibility(View.VISIBLE);

                morningedit.setVisibility(View.VISIBLE);

                morningtext.setVisibility(View.GONE);

            }

        });



        morningbloodfinish.setOnClickListener(new View.OnClickListener() { // 새로 추가한 부분

            //입력을 다하고, 기상 후 체크모양 클릭 시 다시 TextView로 전환, 입력한 혈당,혈압 값 set 설정

            //데베 삽입 X

            @Override

            public void onClick(View view) {

                Log.d(this.getClass().getName(),"체크 클릭");

                morning.setVisibility(View.GONE);

                Morning.setVisibility(View.VISIBLE);

                morningedit.setVisibility(View.GONE);

                morningtext.setVisibility(View.VISIBLE);

                morningbloodedit.setVisibility(View.VISIBLE);

                morningsugartext.setVisibility(View.VISIBLE);

                morningpressuretext.setVisibility(View.VISIBLE);

                morningpressuretext2.setVisibility(View.VISIBLE);

                morningbloodfinish.setVisibility(View.GONE);

                morningsugaredit.setVisibility(View.GONE);

                morningpressureedit.setVisibility(View.GONE);

                morningpressureedit2.setVisibility(View.GONE);







                String morningsugar = (String) morningsugaredit.getText().toString();

                morningsugartext.setText(morningsugar); // 입력한 아침 혈당을 morningsugartext에 입력



                String morningpressure = (String)morningpressureedit.getText().toString();

                morningpressuretext.setText(morningpressure); // 입력한 아침 혈압을 morningpressuretext에 입력



                String morningpressure2 = (String)morningpressureedit2.getText().toString();

                morningpressuretext2.setText(morningpressure2); // 입력한 아침 혈압을 morningpressuretext에 입력



                time = morning.toString();



                time = "아침";

                //mBloodSugar.create(Double.parseDouble(morningsugar), Double.parseDouble(morningpressure),Double.parseDouble(morningpressure2), date, time);

                mBloodSugar.create(Double.parseDouble(morningsugar), Double.parseDouble(morningpressure),Double.parseDouble(morningpressure2), timeminutedate, time);


                Log.d(this.getClass().getName(),morningsugar+"아침혈당이거닷~!!!!!!1");



            }



        });





        lunchbloodedit.setOnClickListener(new View.OnClickListener() { // 점심 행의 연필 아이콘 클릭 시 실행

            @Override

            public void onClick(View view) {

                Log.d(this.getClass().getName(),"왜 앙대 바부야");

                Lunch.setVisibility(View.GONE);

                lunchbloodedit.setVisibility(View.GONE);

                lunchsugartext.setVisibility(View.GONE);

                lunchpressuretext.setVisibility(View.GONE);

                lunchbloodfinish.setVisibility(View.VISIBLE);

                lunchsugaredit.setVisibility(View.VISIBLE);

                lunchpressureedit2.setVisibility(View.VISIBLE);

                lunchpressureedit.setVisibility(View.VISIBLE);

                lunch.setVisibility(View.VISIBLE);

                lunchtext.setVisibility(View.GONE);

                lunchedit.setVisibility(View.VISIBLE);



            }

        });



        lunchbloodfinish.setOnClickListener(new View.OnClickListener() { // 새로 추가한 부분

            //입력을 다하고, 기상 후 체크모양 클릭 시 다시 TextView로 전환, 입력한 혈당,혈압 값 set 설정

            //데베 삽입 X

            @Override

            public void onClick(View view) {

                Log.d(this.getClass().getName(),"체크 클릭");
                lunch.setVisibility(View.GONE);
                Lunch.setVisibility(View.VISIBLE);
                lunchbloodedit.setVisibility(View.VISIBLE);
                lunchsugartext.setVisibility(View.VISIBLE);
                lunchpressuretext.setVisibility(View.VISIBLE);
                lunchbloodfinish.setVisibility(View.GONE);
                lunchsugaredit.setVisibility(View.GONE);
                lunchpressureedit.setVisibility(View.GONE);
                lunchpressureedit2.setVisibility(View.GONE);
                lunchtext.setVisibility(View.VISIBLE);
                lunchedit.setVisibility(View.GONE);
                String lunchsugar = (String)lunchsugaredit.getText().toString(); // 입력한 점심 혈당을 lunchsugaredit에 입력
                lunchsugartext.setText(lunchsugar);
                String lunchpressure = (String)lunchpressureedit.getText().toString(); // 입력한 점심 혈압을 lunchpressureedit에 입력
                lunchpressuretext.setText(lunchpressure);
                String lunchpressure2 = (String)lunchpressureedit2.getText().toString(); // 입력한 점심 혈압을 lunchpressureedit에 입력
                lunchpressuretext2.setText(lunchpressure2);

                time = "점심";
                Log.d(this.getClass().getName(),lunchsugar+"점심혈당이거닷~!!!!!!1");

                //   mBloodSugar.create("userid", Double.parseDouble(lunchsugar), Double.parseDouble(lunchpressure), new Date(), time);

              //  mBloodSugar.create(Double.parseDouble(lunchsugar), Double.parseDouble(lunchpressure),Double.parseDouble(lunchpressure2), date, time);

                mBloodSugar.create(Double.parseDouble(lunchsugar), Double.parseDouble(lunchpressure),Double.parseDouble(lunchpressure2), timeminutedate, time);


            }



        });



        dinnerbloodedit.setOnClickListener(new View.OnClickListener() { // 저녁 행의 연필 아이콘 클릭 시 실행

            @Override

            public void onClick(View view) {
                Log.d(this.getClass().getName(),"왜 앙대 바부야");
                Dinner.setVisibility(View.GONE);
                dinnerbloodedit.setVisibility(View.GONE);
                dinnersugartext.setVisibility(View.GONE);
                dinnerpressuretext.setVisibility(View.GONE);

                dinnerpressuretext2.setVisibility(View.GONE);



                dinnerbloodfinish.setVisibility(View.VISIBLE);

                dinnersugaredit.setVisibility(View.VISIBLE);

                dinnerpressureedit.setVisibility(View.VISIBLE);

                dinnerpressureedit2.setVisibility(View.VISIBLE);

                dinnertext.setVisibility(View.GONE);

                dinneredit.setVisibility(View.VISIBLE);

                dinner.setVisibility(View.VISIBLE);

            }

        });



        dinnerbloodfinish.setOnClickListener(new View.OnClickListener() { // 새로 추가한 부분

            //입력을 다하고, 기상 후 체크모양 클릭 시 다시 TextView로 전환, 입력한 혈당,혈압 값 set 설정

            //데베 삽입 X

            @Override

            public void onClick(View view) {

                Log.d(this.getClass().getName(),"체크 클릭");

                dinner.setVisibility(View.GONE);

                Dinner.setVisibility(View.VISIBLE);

                dinnerbloodedit.setVisibility(View.VISIBLE);

                dinnersugartext.setVisibility(View.VISIBLE);

                dinnerpressuretext.setVisibility(View.VISIBLE);

                dinnerpressuretext2.setVisibility(View.VISIBLE);

                dinnertext.setVisibility(View.VISIBLE);

                dinneredit.setVisibility(View.GONE);

                dinnerbloodfinish.setVisibility(View.GONE);

                dinnersugaredit.setVisibility(View.GONE);

                dinnerpressureedit.setVisibility(View.GONE);

                dinnerpressureedit2.setVisibility(View.GONE);



                String dinnersugar = (String)dinnersugaredit.getText().toString(); // 입력한 저녁 혈당을 lunchsugaredit에 입력

                dinnersugartext.setText(dinnersugar);



                String dinnerpressure = (String)dinnerpressureedit.getText().toString(); // 입력한 저녁 혈압을 lunchpressureedit에 입력

                dinnerpressuretext.setText(dinnerpressure);



                String dinnerpressure2 = (String)dinnerpressureedit2.getText().toString(); // 입력한 저녁 혈압을 lunchpressureedit에 입력

                dinnerpressuretext2.setText(dinnerpressure2);





                time = "저녁";



                Log.d(this.getClass().getName(),dinnersugar+"저녁혈당이거닷~!!!!!!1");

                //   mBloodSugar.create("userid", Double.parseDouble(dinnersugar), Double.parseDouble(dinnerpressure), new Date(), time);

                //mBloodSugar.create(Double.parseDouble(dinnersugar), Double.parseDouble(dinnerpressure),Double.parseDouble(dinnerpressure2), date, time);

                mBloodSugar.create(Double.parseDouble(dinnersugar), Double.parseDouble(dinnerpressure),Double.parseDouble(dinnerpressure2), timeminutedate, time);



            }



        });



        sleepbloodedit.setOnClickListener(new View.OnClickListener() { // 취침 후  행의 연필 아이콘 클릭 시 실행

            @Override

            public void onClick(View view) {

                Log.d(this.getClass().getName(),"왜 앙대 바부야");

                Sleep.setVisibility(View.GONE);

                sleepbloodedit.setVisibility(View.GONE);

                sleepsugartext.setVisibility(View.GONE);

                sleeppressuretext.setVisibility(View.GONE);

                sleeppressuretext2.setVisibility(View.GONE);

                sleepbloodfinish.setVisibility(View.VISIBLE);

                sleepsugaredit.setVisibility(View.VISIBLE);

                sleeppressureedit.setVisibility(View.VISIBLE);

                sleeppressureedit2.setVisibility(View.VISIBLE);



                sleep.setVisibility(View.VISIBLE);

                sleepedit.setVisibility(View.VISIBLE);

                sleeptext.setVisibility(View.GONE);

            }

        });



        sleepbloodfinish.setOnClickListener(new View.OnClickListener() { // 새로 추가한 부분

            //입력을 다하고, 기상 후 체크모양 클릭 시 다시 TextView로 전환, 입력한 혈당,혈압 값 set 설정

            //데베 삽입 X

            @Override

            public void onClick(View view) {

                Log.d(this.getClass().getName(),"체크 클릭");

                sleep.setVisibility(View.GONE);

                Sleep.setVisibility(View.VISIBLE);

                sleepbloodedit.setVisibility(View.VISIBLE);

                sleepsugartext.setVisibility(View.VISIBLE);

                sleeppressuretext.setVisibility(View.VISIBLE);

                sleeppressuretext2.setVisibility(View.VISIBLE);



                sleepbloodfinish.setVisibility(View.GONE);

                sleepsugaredit.setVisibility(View.GONE);

                sleeppressureedit.setVisibility(View.GONE);

                sleeppressureedit2.setVisibility(View.GONE);

                sleeptext.setVisibility(View.VISIBLE);

                sleepedit.setVisibility(View.GONE);

                String sleepsugar = (String)sleepsugaredit.getText().toString(); // 입력한 취침 후  혈당을 dinnersugaredit에 입력

                sleepsugartext.setText(sleepsugar);



                String sleeppressure = (String)sleeppressureedit.getText().toString(); // 입력한 취침 후  혈압을 dinnerpressureedit에 입력

                sleeppressuretext.setText(sleeppressure);

                String sleeppressure2 = (String)sleeppressureedit2.getText().toString(); // 입력한 취침 후  혈압을 dinnerpressureedit에 입력

                sleeppressuretext2.setText(sleeppressure2);





                time = "취침 전";

                Log.d(this.getClass().getName(),sleepsugar+"취침혈당이거닷~!!!!!!1");

                //  mBloodSugar.create("userid", Double.parseDouble(sleepsugar), Double.parseDouble(sleeppressure), new Date(), time);

               // mBloodSugar.create(Double.parseDouble(sleepsugar), Double.parseDouble(sleeppressure),Double.parseDouble(sleeppressure2), date, time);

                mBloodSugar.create(Double.parseDouble(sleepsugar), Double.parseDouble(sleeppressure),Double.parseDouble(sleeppressure2), timeminutedate, time);



            }



        });





        home.setOnClickListener(new View.OnClickListener() { // 당커벨 클릭 시 홈화면으로 전환
            @Override

            public void onClick(View view) {
                Intent homeintent = new Intent(getApplicationContext(), homeActivity.class);
                startActivity(homeintent);// 홈화면 전환하도록 homeActivity로 전환
            }
        });

        meal_txt.setOnClickListener(new View.OnClickListener() { //상단에 식단관리 클릭 시 실행

            @Override

            public void onClick(View view) {

                Intent mealintent = new Intent(getApplicationContext(), mealActivity.class);

                startActivity(mealintent);// 식단관리 화면으로 이동하도록 mealActivity로 전환

            }

        });

        pill_txt.setOnClickListener(new View.OnClickListener() { // 상단에 복약관리 클릭 시 실행

            @Override
            public void onClick(View view) {
                Intent pillintent = new Intent(getApplicationContext(), pillActivity.class);
                startActivity(pillintent);// 복약관리 화면으로 이동하도록 pillActivity로 전환
            }
        });
    }





}