package com.example.mp_termproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.time.LocalDate;
import java.util.ArrayList;

public class SpeechFragment extends Fragment {
    ImageButton sttStart;
    TextView txtInput, txtSystem, sttState;
    Context context;
    String logTag = "[STT]";

    //음성인식용
    Intent sttIntent;
    SpeechRecognizer speechRecognizer;
    final int PERMISSION = 1;	//permission 변수
    boolean recording = false;  // 음성인식중인지 확인용 변수

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_speech, container, false);
        context = getActivity();

 //       CheckPermission();  //권한요청 확인

        // 음성인식 객체 생성
        sttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        sttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getApplicationContext().getPackageName());
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");    //한국어 사용

        // 음성인식 부분
        txtInput = (TextView) rootView.findViewById(R.id.userInput); //음성인식된 문장 출력
        txtSystem = (TextView) rootView.findViewById(R.id.answer);   //답변 출력
        sttState = (TextView) rootView.findViewById(R.id.state); //음성인식 상태 출력

        sttStart = (ImageButton) rootView.findViewById(R.id.mike);
        sttStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(logTag, "음성인식 버튼 눌림");

                if (!recording) {   //녹음 시작
                    Log.d("record", "녹음시작");
                    CheckPermission();  //권한요청 확인
                    StartRecord();
                    //Toast.makeText(getApplicationContext(), "지금부터 음성으로 기록합니다.", Toast.LENGTH_SHORT).show();
                }
                else {  //이미 녹음 중이면 녹음 중지
                    StopRecord();
                    CheckPermission();  //권한요청 확인
                    StartRecord();
                }

            }
        });
        return rootView;
    }   // onCreate end

    //권한 체크 및 음성 인식 시작
    public void CheckPermission(){
        //안드로이드 버전이 6.0 이상
        if ( Build.VERSION.SDK_INT >= 23 ){
            //인터넷이나 녹음 권한이 없으면 권한 요청
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED ) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO},PERMISSION);
                Log.d("permossion", "권한 요청됨");
            }
        }
    }

    //음성 녹음 시작
    public void StartRecord(){
        recording = true;
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context.getApplicationContext());
        speechRecognizer.setRecognitionListener(listener);
        speechRecognizer.startListening(sttIntent);

    }

    //음성 녹음 중지
    public void StopRecord() {
        recording = false;
        sttState.setText(" ");
        speechRecognizer.stopListening();   //녹음 중지
        //Toast.makeText(getApplicationContext(), "음성 기록을 중지합니다.", Toast.LENGTH_SHORT).show();
    }

    //음성 녹음
    private RecognitionListener listener = new RecognitionListener() {
        @Override // 말 시작할 준비가 되면 호출
        public void onReadyForSpeech(Bundle bundle) {
            txtSystem.setText(" ");
            sttState.setText("듣고있어요");
        }

        @Override   // 말하기 시작하면 호출
        public void onBeginningOfSpeech() {
        }

        @Override   // 입력받는 소리의 크기 알려줌
        public void onRmsChanged(float dB) {
        }

        @Override   // 인식된 단어를 buffer에 담음
        public void onBufferReceived(byte[] bytes) {
            Log.d(logTag, "버퍼에 입력 단어 저장");
        }

        @Override   // 사용자가 말을 멈추면 호출
        public void onEndOfSpeech() {
            sttState.setText(" ");
            Log.d(logTag, "말하기 입력받기 끝");
        }

        @Override   // 에러 발생
        public void onError(int error) {
            String message;
            switch (error) {
                case SpeechRecognizer.ERROR_AUDIO:
                    message = "오디오 에러";
                    break;
                case SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS:
                    message = "퍼미션 없음";
                    break;
                case SpeechRecognizer.ERROR_NETWORK:
                    message = "네트워크 에러";
                    break;
                case SpeechRecognizer.ERROR_NETWORK_TIMEOUT:
                    message = "네트워크 타임아웃";
                    break;
                case SpeechRecognizer.ERROR_NO_MATCH:
                    //녹음을 오래하거나 speechRecognizer.stopListening()을 호출하면 발생하는 에러
                    //speechRecognizer를 다시 생성하여 녹음 재개
                    if (recording) {
                        CheckPermission();
                    }
                    return; //토스트 메세지 출력 X
                case SpeechRecognizer.ERROR_RECOGNIZER_BUSY:
                    message = "RECOGNIZER is busy";
                    break;
                case SpeechRecognizer.ERROR_SERVER:
                    message = "서버 에러";
                    break;
                case SpeechRecognizer.ERROR_SPEECH_TIMEOUT:
                    message = "말하는 시간 초과";
                    break;
                default:
                    message = "알 수 없는 오류";
                    break;
            }
            //Toast.makeText(getApplicationContext(), "에러가 발생하였습니다. : " + message, Toast.LENGTH_SHORT).show();
            sttState.setText("다시 말해주세요");
        }

        // 인식 결과가 준비되면 호출
        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);	//인식 결과를 담은 ArrayList
            String resultText = " ";

            if (matches != null && !matches.isEmpty()) {
                //인식 결과 (사용자 발언 문장)
                for (int i = 0; i < matches.size() ; i++) {
                    resultText += matches.get(i);
                }
                txtInput.setText(resultText);
            } else {
                txtInput.setText("No results found");
            }

            // 결과 처리
            handleRecognitionResult(resultText);
            recording = false;
        }
        @Override   // 부분 인식 결과를 사용할 수 있을 때 호출
        public void onPartialResults(Bundle bundle) {
        }

        @Override   // 이벤트 추가를 위한 예약
        public void onEvent(int i, Bundle bundle) {
        }
    };

    // 음성인식 결과 처리
    private void handleRecognitionResult(String result) {
        AsgDBHelper asgHelper = new AsgDBHelper(context);
        SQLiteDatabase db = asgHelper.getReadableDatabase();

        String[] text = {"오늘", "내일", "제출 안 한"};
        ArrayList<String> assignmentList;

        LocalDate today = LocalDate.now();  // 현재 날짜
        LocalDate tomorrow = today.plusDays(1);  // 내일 날짜
        String date;


        for(int i=0; i < text.length; i++) {
            boolean containsKeyword = result.contains(text[i]);
            Log.d("과제 확인", "있나? " + containsKeyword);
            if (containsKeyword) {
                switch (i){
                    case 0 ://오늘까지인 과제 출력
                        txtSystem.setText("[ 오늘까지인 과제들입니다 ]\n\n");
                        date = today.toString();
                        assignmentList = asgHelper.getAssignmentByDate(date);

                        for(String data : assignmentList){
                            txtSystem.append(data + "\n");
                        }
                        break;

                    case 1: //내일까지인 과제 출력
                        txtSystem.setText("[ 내일까지인 과제들입니다 ]\n\n");
                        date = tomorrow.toString();
                        assignmentList = asgHelper.getAssignmentByDate(date);

                        for(String data : assignmentList){
                            txtSystem.append(data + "\n");
                        }
                        break;

                    case 2: // 제출 안 한 과제 출력
                        txtSystem.setText("[ 제출하지 않은 과제들입니다 ]\n\n");
                        assignmentList = asgHelper.getAssignmentsBySubmission();
                        for(String data : assignmentList){
                            txtSystem.append(data + "\n");
                        }
                        break;

                    default:
                        txtSystem.setText("답변을 찾지 못 했어요. 다시 질문해주세요.");
                        break;
                }
                break;
            } else {
               txtSystem.setText("답변을 찾지 못 했어요. 제안을 확인해주세요");
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (speechRecognizer != null) {
            speechRecognizer.destroy();
            speechRecognizer.cancel();
            speechRecognizer = null;
        }
    }
}
