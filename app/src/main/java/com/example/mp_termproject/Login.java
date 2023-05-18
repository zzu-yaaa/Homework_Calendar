package com.example.mp_termproject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {

    private String url = "https://cyber.gachon.ac.kr/login.php";

    TextView showResult;    //아이디, 비밀번호, 크롤링 결과값
    EditText name, password;
    Button loginBtn;
    String loginId, loginPw, content;
    Message msg;    //data 전달 class
    ValueHandler handler = new ValueHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        Bundle bundle = new Bundle();
        name = (EditText) findViewById(R.id.name);
        password = (EditText) findViewById(R.id.password);
        showResult = (TextView) findViewById(R.id.showText);
        loginBtn = (Button) findViewById(R.id.lgnBtn);

        loginBtn.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //입력한 아이디, 비번 가져오기
                loginId = name.getText().toString();
                loginPw = password.getText().toString();

                new Thread() {
                    @Override
                    public void run() {
                        try {
                            // 1. 전송할 폼 데이터
                            Map<String, String> data = new HashMap<>();
                            data.put("userid", loginId);
                            data.put("password", loginPw);
                            data.put("redirect", "/");

                            // 2. 로그인(POST)
                            Connection.Response response = Jsoup.connect(url)
                                    .data("username", loginId, "password", loginPw)
                                    .method(Connection.Method.POST)
                                    .execute();

                            // 쿠키값 가져오기
                            Map<String, String> loginCookie = response.cookies();

                            String cookie = response.cookies().toString();
                            Log.d("Tag", "쿠기정보 : " + cookie); //확인용 로그캣 출력 --> 최종에서는 빼도 된다

                            // 3. 쿠키 이용해 로그인 후 필요한 정보 빼오기
                            Document doc = (Document) Jsoup.connect("https://cyber.gachon.ac.kr/")
                                    .timeout(3000000)
                                    .cookies(loginCookie)
                                    .method(Connection.Method.POST)
                                    .get();

                            //강의명 가져오기
                            Elements course = doc.select(".course-title"); //끌어온 html에서 태그 class가 "course-title"인 값만 선택해서 빼오기
                            boolean isEmpty = course.isEmpty(); //빼온 값 null체크
                            Log.d("Tag", "isNull? : " + isEmpty); //빼온 값의 null 여부 로그캣 출력

                            //null값이 아니면 크롤링 실행
                            if(isEmpty == false) {
                                content = course.get(0).text(); //크롤링 값 가져오기

                               /*
                               * get() : 얘는 가져온 정보들 배열 형식으로 저장하는 것 같다
                               *
                               * get(0).text() --> 젤 위에 있는 강좌명
                               * get(1).text() --> 두번째에 위치한 강좌 명
                               * 이렇게 가져온다
                               *
                               * 강의명 하나씩 가져와서 Bundle에 답고 thread 밖으로 값 보내주기 번거로움
                               * 그리고 지금은 .text()로 가져와서 해당 클래스 내의 string을 다 가져오니 교수명도 딸려온다
                               * */

                                //결과값 Bundle 통해 전달
                                bundle.putString("course", content); //bundle에 뽑아낸 결과값 담기
                                msg = handler.obtainMessage(); //handler에 결과값 추가
                                msg.setData(bundle);
                                handler.sendMessage(msg);  //msg를 message queue에 전달
                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }.start();
            }
        });

    }

    class ValueHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            //key값을 통해 원하는 정보를 가져와 textView에 출력하는 부분
            String value = msg.getData().getString("course");

            showResult.append("\n[강의] \n" + value + "\n");    //화면에 출력
        }
    }
}
