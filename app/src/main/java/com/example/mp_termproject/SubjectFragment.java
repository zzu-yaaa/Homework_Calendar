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
                int id;
                Bundle bundle = new Bundle();

                if(swEngineering.isChecked()){

                }else{
                    id=swEngineering.getId();
                    bundle.putInt("unSelected",id);
                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                    HomeFragment homeFragment = new HomeFragment();
                    homeFragment.setArguments(bundle);
                    transaction.replace(R.id.fragment_container,homeFragment);
                    transaction.commit();
                }

                if(DataScience.isChecked()){
                    Toast.makeText(ct,"checked:",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ct,"Unchecked:",Toast.LENGTH_SHORT).show();
                }

                if(mobilePw.isChecked()){
                    Toast.makeText(ct,"checked:",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ct,"Unchecked:",Toast.LENGTH_SHORT).show();
                }

                if(seminar.isChecked()){
                    Toast.makeText(ct,"checked:",Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ct,"Unchecked:",Toast.LENGTH_SHORT).show();
                }

            }
        });

        return v;
    }
}