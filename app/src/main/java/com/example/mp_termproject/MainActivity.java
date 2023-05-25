package com.example.mp_termproject;

import static java.sql.Types.NULL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.Color;
import android.hardware.usb.UsbEndpoint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;
import com.jakewharton.threetenabp.AndroidThreeTen;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import javax.security.auth.Subject;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, SendEventListener {

    private DrawerLayout drawer;
    NavigationView navigationView;
    private MaterialCalendarView materialCalendarView;
    TextView dateTextView;
    HomeFragment homeFragment;
    SubjectFragment subjectFragment;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = findViewById(R.id.textView);
        dateTextView = findViewById(R.id.dateTextView);
        homeFragment = (HomeFragment) getSupportFragmentManager().findFragmentById(R.id.home_fragment);
        subjectFragment = (SubjectFragment) getSupportFragmentManager().findFragmentById(R.id.subject_fragment);

        setLayout();

        Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar); //toolbar를 MainActivity의 AppBar로 지정
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.navigation_view);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawer,toolbar,
                R.string.navigation_drawer_open
                ,R.string.navigation_drawer_close);

        drawer.addDrawerListener(toggle);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);


    }

    private void setLayout() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new HomeFragment()).commit();

    }

    //Subject Fragment로부터 넘겨받은 과목id list로 toast메세지 띄워줌
    @Override
    public void sendSelectedSubject(ArrayList<Integer> integerArrayList) {

        String msg = "받고 ";
        for(int i=0;i<integerArrayList.size();i++){
            msg += integerArrayList.get(i) + ",";
        }

        if(integerArrayList != null){
            Toast.makeText(getApplicationContext(),msg,Toast.LENGTH_LONG).show();
            //textView.setText("선택된 과목 : "+integerArrayList.size()); //다른 프래그먼트 갔다오면 반영되는지 확인해보기
            //HomeFragment로 ArrayList보내줌
//            homeFragment.SelectedSubject(integerArrayList);
        }
    }

    //홈화면 상단 메뉴클릭했을때
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        switch(menuItem.getItemId()){
            case R.id.home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new HomeFragment()).commit();
                break;
            case R.id.myInfo:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new InfoFragment()).commit();
                break;
            case R.id.color:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new ColorFragment()).commit();
                break;
            case R.id.subject:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new SubjectFragment()).commit();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Subject Fragment로부터 선택되지 않은 과목id list 넘겨받음
    @Override
    public void sendUnSelectedSubject(ArrayList<Integer> integerArrayList) {

    }
}