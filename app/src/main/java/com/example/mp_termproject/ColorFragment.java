package com.example.mp_termproject;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ColorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ColorFragment extends Fragment {

    Button btn_save;
    Context ct;
    RadioButton green;
    RadioButton red;
    RadioButton pink;
    RadioButton blue;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ColorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ColorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ColorFragment newInstance(String param1, String param2) {
        ColorFragment fragment = new ColorFragment();
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
        View view =  inflater.inflate(R.layout.fragment_color, container, false);
        btn_save = view.findViewById(R.id.save_color_button);
        green = view.findViewById(R.id.color_green);
        blue = view.findViewById(R.id.color_blue);
        pink = view.findViewById(R.id.color_pink);
        red = view.findViewById(R.id.color_red);

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                ArrayList<Integer> checkedId = new ArrayList<>();
                ArrayList<String> checkedColor = new ArrayList<>();

                if(red.isChecked()){

                    checkedId.add(red.getId());
                    checkedColor.add((String) red.getText());

                }else if(green.isChecked()){

                    checkedId.add(green.getId());
                    checkedColor.add((String) green.getText());

                }
                if(blue.isChecked()){
                    checkedId.add(blue.getId());
                    checkedColor.add((String) blue.getText());

                }else if(pink.isChecked()){
                    checkedId.add(pink.getId());
                    checkedColor.add((String) pink.getText());
                }

                //bundle.putIntegerArrayList("checked_color",checkedId);
                bundle.putStringArrayList("checked_color",checkedColor);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                homeFragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container_view,homeFragment);
                transaction.commit();

            }
        });
        return view;
    }
}