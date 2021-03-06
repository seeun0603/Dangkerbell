package com.example.dankerbell.mealManagement;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dankerbell.Firebase.FoodlistCrud;
import com.example.dankerbell.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import static com.example.dankerbell.Firebase.FoodlistCrud.getFood;
import static com.example.dankerbell.Firebase.FoodlistCrud.mealHandler;

public class SearchmealActivity extends AppCompatActivity implements RecyclerViewAdapterCallback{
    public static Handler mHandler2=new Handler();

    FoodlistCrud foodlistCrud=FoodlistCrud.getInstance();
    SearchView searchView;
    mealActivity ma=new mealActivity();
    RecyclerView mRecyclerView = null ;
    com.example.dankerbell.mealManagement.RecyclerImageTextAdapter mAdapter = null ;
    static ArrayList<RecyclerItem> mList = new ArrayList<RecyclerItem>();

    static final ArrayList<String> mfoodarraylist=new ArrayList<>();
    static final ArrayList<String> mkcalarraylist=new ArrayList<>();
    static final ArrayList<String> lfoodarraylist=new ArrayList<>();
    static final ArrayList<String> lkcalarraylist=new ArrayList<>();
    static final ArrayList<String> dfoodarraylist=new ArrayList<>();
    static final ArrayList<String> dkcalarraylist=new ArrayList<>();

    String food,kcal;
    TextView foodfinish;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.AppTheme2);
        SimpleDateFormat sdf = new SimpleDateFormat("yy.MM.dd", Locale.getDefault());
        final Calendar calendar = Calendar.getInstance(); // 오늘날짜
        final String date = sdf.format(calendar.getTime());
        setContentView(R.layout.activity_searchmeal);
        RecyclerView mRecyclerView = findViewById(R.id.recycler1);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        FoodlistCrud foodlistCrud=FoodlistCrud.getInstance();
        final mealActivity ma=new mealActivity();
        foodfinish=findViewById(R.id.foodfinish);
      //  final TextView t=findViewById(R.id.morningfood1);
        foodfinish.setOnClickListener(new View.OnClickListener() { // 확인하기 버튼 클릭
            @Override
            public void onClick(View view) {
                Intent meal = new Intent(getApplicationContext(),mealActivity.class);
                for (int j=0; j<mList.size();j++){
                    if (mList.get(j).isSelected() == true){
                        if(ma.op=="아침"){
                            food=mList.get(j).getFood();
                            kcal=mList.get(j).getKcal();
                            foodlistCrud.create(ma.op,date,food,kcal);
//                            mfoodarraylist.add(mList.get(j).getFood()); // 선택된 음식
//                            mkcalarraylist.add(mList.get(j).getKcal());

                        }
                        else if(ma.op=="점심"){
                            food=mList.get(j).getFood();
                            kcal=mList.get(j).getKcal();
                            foodlistCrud.create(ma.op,date,food,kcal);
                        }
//                            lfoodarraylist.add(mList.get(j).getFood()); // 선택된 음식
//                            lkcalarraylist.add(mList.get(j).getKcal());
                        else{
                            food=mList.get(j).getFood();
                            kcal=mList.get(j).getKcal();
                            foodlistCrud.create(ma.op,date,food,kcal);
//                            dfoodarraylist.add(mList.get(j).getFood()); // 선택된 음식
//                            dkcalarraylist.add(mList.get(j).getKcal());                        }
                        }
                        }

                }
                mHandler2.sendEmptyMessage(100);
                Log.d(this.getClass().getName(),"메세지 보냄");
                startActivity(meal);// 음식검색화면으로 이동
            }
        });

        if(mList.isEmpty()) {
            Log.d(this.getClass().getName(),"mList비어져잇음");
            for(int i=0;i<ma.getfood.size();i++){ //파이어베이스에서 받아온 코드
                Log.d(this.getClass().getName(),ma.getfood.get(0));
                Log.d(this.getClass().getName(),ma.getfood.get(1));
            addItem(ma.getfood.get(i),ma.getkcal.get(i));
            }
            //final String food=null;
//            FoodlistCrud.mealHandler = new Handler(){
//               @Override public void handleMessage(Message msg){
//                   if (msg.what==1001){
//                           if(getfood.isEmpty()){
//                            for(int i=0;i<FoodlistCrud.getKcal().size();i++){
//                                Log.d("음식", String.valueOf(i)+FoodlistCrud.getFood());
//                                getfood.add(FoodlistCrud.getFood().get(i));
//                                getkcal.add(FoodlistCrud.getKcal().get(i));
//                                Log.d("칼로리", String.valueOf(i)+FoodlistCrud.getKcal());
//                                addItem(getfood.get(i),getkcal.get(i));
//                            }
//                           }
//                   }
//               }
//           };
//           addItem("감자튀김", "500");
//           addItem("새우볶음밥", "500");
//           addItem("전복죽", "500");
//           addItem("곰탕", "500");
//           addItem("돼지국밥", "500");
       }
        init();
        mAdapter = new RecyclerImageTextAdapter(SearchmealActivity.this,mList);
        mRecyclerView.setAdapter(mAdapter) ;

    }
public void init(){
        for(int i=0;i<mList.size();i++){
            mList.get(i).setSelected(false);
        }
}
    private void addItem(String food, String kcal) {
        RecyclerItem item=new RecyclerItem(food,kcal,false);
        item.setFood(food);
        item.setKcal(kcal);
        mList.add(item);
        Log.d(this.getClass().getName(), String.valueOf(mList.get(0)));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.option_menu, menu);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            TextView finish=findViewById(R.id.foodfinish);

            SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
            SearchView search = (SearchView) menu.findItem(R.id.search).getActionView();
            search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));

            search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String s) {
                    Log.d("검색어",s);
                    mAdapter.exampleFilter.filter(s);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String s) {
                    mAdapter.exampleFilter.filter(s);
                    return true;
                }
            });

        }

        return true;
    }

    @Override
    public void showToast(int position) {
        Toast.makeText(this, position + " clicked.", Toast.LENGTH_SHORT).show();

    }
}


