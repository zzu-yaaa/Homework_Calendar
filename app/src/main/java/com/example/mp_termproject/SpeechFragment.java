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

    //For voice recognition
    Intent sttIntent;
    SpeechRecognizer speechRecognizer;
    final int PERMISSION = 1;	//permission value
    boolean recording = false;  // Variables to determine if speech is being recognized

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_speech, container, false);
        context = getActivity();

        // Creating a Speech Recognition Object
        sttIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        sttIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.getApplicationContext().getPackageName());
        sttIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "ko-KR");    //Speaking Korean

        // Voice recognition part
        txtInput = (TextView) rootView.findViewById(R.id.userInput); //Voice-recognized Sentence Output
        txtSystem = (TextView) rootView.findViewById(R.id.answer);  //answer output
        sttState = (TextView) rootView.findViewById(R.id.state); //voice recognition status output

        sttStart = (ImageButton) rootView.findViewById(R.id.mike);
        sttStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(logTag, "음성인식 버튼 눌림");

                if (!recording) {   //Start recording
                    Log.d("record", "녹음시작");
                    CheckPermission();  //Confirm permission request
                    StartRecord();
                }
                else {  //Stop recording if you are already recording
                    StopRecord();
                    CheckPermission();  //Confirm permission request
                    StartRecord();
                }

            }
        });
        return rootView;
    }   // onCreate end

    //Permission Check and Start Speech Recognition
    public void CheckPermission(){
        //Android version 6.0 or later
        if ( Build.VERSION.SDK_INT >= 23 ){
            //Request permission if you do not have permission to record or the Internet
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_DENIED
                    || ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED ) {
                ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.INTERNET, Manifest.permission.RECORD_AUDIO},PERMISSION);
                Log.d("permossion", "권한 요청됨");
            }
        }
    }

    //Start recording voice
    public void StartRecord(){
        recording = true;
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context.getApplicationContext());
        speechRecognizer.setRecognitionListener(listener);
        speechRecognizer.startListening(sttIntent);

    }

    //Stop recording your voice
    public void StopRecord() {
        recording = false;
        sttState.setText(" ");
        speechRecognizer.stopListening();   //Stop recording
    }

    //voice recording
    private RecognitionListener listener = new RecognitionListener() {
        @Override // Call when ready to start talking
        public void onReadyForSpeech(Bundle bundle) {
            txtSystem.setText(" ");
            sttState.setText("듣고있어요");
        }

        @Override   // Call when ready to start talking
        public void onBeginningOfSpeech() {
        }

        @Override   // Tell how loud the incoming sound is
        public void onRmsChanged(float dB) {
        }

        @Override   // Include recognized words in buffer
        public void onBufferReceived(byte[] bytes) {
            Log.d(logTag, "버퍼에 입력 단어 저장");
        }

        @Override  // Call when user stops talking
        public void onEndOfSpeech() {
            sttState.setText(" ");
            Log.d(logTag, "말하기 입력받기 끝");
        }

        @Override   // Error
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
                    //Error when recording too long or calling speechRecognizer.stopListening()
                    //Re-create the speechRecognizer to resume recording
                    if (recording) {
                        CheckPermission();
                    }
                    return; //Toast message output X
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
            sttState.setText("다시 말해주세요");
        }

        // Call when recognition results are ready
        @Override
        public void onResults(Bundle results) {
            ArrayList<String> matches = results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);	//Array List with recognition results
            String resultText = " ";

            if (matches != null && !matches.isEmpty()) {
                //Recognition Results (User Speech Sentences)
                for (int i = 0; i < matches.size() ; i++) {
                    resultText += matches.get(i);
                }
                txtInput.setText(resultText);
            } else {
                txtInput.setText("No results found");
            }

            // Processing results
            handleRecognitionResult(resultText);
            recording = false;
        }
        @Override   // Call when partial recognition results are available
        public void onPartialResults(Bundle bundle) {
        }

        @Override   // Schedule for adding events
        public void onEvent(int i, Bundle bundle) {
        }
    };

    // Processing Speech Recognition Results
    private void handleRecognitionResult(String result) {
        AsgDBHelper asgHelper = new AsgDBHelper(context);
        SQLiteDatabase db = asgHelper.getReadableDatabase();

        String[] text = {"오늘까진 과제", "오늘까지인 과제", "내일까진 과제", "내일까지인 과제", "제출 안 한"};
        ArrayList<String> assignmentList;

        LocalDate today = LocalDate.now();  // 현재 날짜
        LocalDate tomorrow = today.plusDays(1);  // 내일 날짜
        String date;


        for(int i=0; i < text.length; i++) {
            boolean containsKeyword = result.contains(text[i]);
            if (containsKeyword) {
                switch (i){
                    case 0: case 1:// assignment due today
                        txtSystem.setText("[ 오늘까지인 과제들입니다 ]\n\n");
                        date = today.toString();
                        assignmentList = asgHelper.getAssignmentByDate(date);

                        for(String data : assignmentList){
                            txtSystem.append(data + "\n");
                        }
                        break;

                    case 2: case 3:// assignment due tomorrow
                        txtSystem.setText("[ 내일까지인 과제들입니다 ]\n\n");
                        date = tomorrow.toString();
                        assignmentList = asgHelper.getAssignmentByDate(date);

                        for(String data : assignmentList){
                            txtSystem.append(data + "\n");
                        }
                        break;

                    case 4: // not submitted assignment
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
