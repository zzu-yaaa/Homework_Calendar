package com.example.mp_termproject;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class SubjectFragment extends Fragment {

    Button save_btn;
    CheckBox swEngineering;
    CheckBox DataScience;
    CheckBox mobilePw;
    CheckBox seminar;
    Context ct;
    SendEventListener sendEventListener;

    //listener달아줌
    @Override
    public void onAttach(@NonNull Context context){
        super.onAttach(context);
        try{
            sendEventListener = (SendEventListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implements SendEventListener");

        }
    }

    @Override
    public void onDetach(){
        super.onDetach();
        if(sendEventListener !=null){
            sendEventListener = null;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_subject, container, false);
        ct = container.getContext();

        save_btn = v.findViewById(R.id.save_subject_button);
        swEngineering = v.findViewById(R.id.swEngineering);
        DataScience = v.findViewById(R.id.DataScience);
        mobilePw = v.findViewById(R.id.mobilePw);
        seminar = v.findViewById(R.id.seminar);
        //violence = v.findViewById(R.id.violence);


        //checkedTextView 체크 구현
//        violence.setChecked(false);
//        violence.setChecked(true);
//        View.OnClickListener listener = new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((CheckedTextView)v).toggle();
//            }
//        };
//
//        violence.setOnClickListener(listener);

        //save버튼 눌렀을때 체크박스 체크 된거/안된거 각각 해당list에 담아서 MainActivity로 보내주고 화면전환
        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //선택된 과목 id담을 arraylist
                ArrayList<Integer> selectedSubject = new ArrayList<>();
                //선택되지 않은 과목 id담을 arraylist
                ArrayList<Integer> unselectedSubject = new ArrayList<>();

                //Home Fragment로 어떤변수를 보내는게 좋을지 정해야함
                //id만 보내도 home fragment에서 안보여지게 가능한지
                //과목명text도 있어야 한다면 1.checkedTextView 2.ArrayList hashmap
                if(swEngineering.isChecked()){
                    selectedSubject.add(swEngineering.getId());
                }else {
                    unselectedSubject.add(swEngineering.getId());

                }

                if(DataScience.isChecked()){
                    selectedSubject.add(DataScience.getId());
                }else{
                    unselectedSubject.add(DataScience.getId());

                }

                if(mobilePw.isChecked()){
                    selectedSubject.add(mobilePw.getId());
                }else{
                    unselectedSubject.add(mobilePw.getId());

                }

                if(seminar.isChecked()){
                    selectedSubject.add(seminar.getId());
                }else{
                    unselectedSubject.add(seminar.getId());

                }
                String msg = "보내고 ";
                for(int i=0;i<selectedSubject.size();i++){
                    msg += selectedSubject.get(i) +",";
                }
                Toast.makeText(ct,msg,Toast.LENGTH_LONG).show();
//                Intent intent = new Intent(getActivity(), MainActivity.class);
//                //127이랑 한번씩 실행해보고 순서영향있나 체크
//                //sendEventListener.sendSelectedSubject(selectedSubject);
//                startActivity(intent);
//
//                sendEventListener.sendSelectedSubject(selectedSubject);
                Bundle bundle = new Bundle();
                bundle.putIntegerArrayList("SelectedSubject",selectedSubject);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container_view,homeFragment);
                transaction.commit();
            }
        });

        return v;
    }


}