package com.example.mp_termproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

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
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public SubjectFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SubjectFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SubjectFragment newInstance(String param1, String param2) {
        SubjectFragment fragment = new SubjectFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
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