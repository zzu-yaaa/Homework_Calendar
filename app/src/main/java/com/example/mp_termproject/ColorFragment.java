package com.example.mp_termproject;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ColorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ColorFragment extends Fragment {

    Button btn_save;
    Button btn_cancel;
    Context ct;
    ArrayList<String> color_list = new ArrayList<>();
    Spinner spinner_done;
    Spinner spinner_undone;

    ColorDBHelper colorHelper;

    private ColorAdapter colorAdapter;

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
        btn_cancel = view.findViewById(R.id.cancel_color_button);
        ct = container.getContext();
        colorHelper = new ColorDBHelper(ct);

        spinner_done = view.findViewById(R.id.spinner_done);
        spinner_undone = view.findViewById(R.id.spinner_undone);
        Resources res = getResources();

        List<Colors> colorList = new ArrayList<>();

        Colors red = new Colors();
        red.setColor("RED");
        red.setImage(R.drawable.red);
        colorList.add(red);

        Colors pink = new Colors();
        pink.setColor("PINK");
        pink.setImage(R.drawable.pink);
        colorList.add(pink);

        Colors orange = new Colors();
        orange.setColor("ORANGE");
        orange.setImage(R.drawable.orange);
        colorList.add(orange);

        Colors yellow = new Colors();
        yellow.setColor("YELLOW");
        yellow.setImage(R.drawable.yellow);
        colorList.add(yellow);

        Colors green = new Colors();
        green.setColor("GREEN");
        green.setImage(R.drawable.green);
        colorList.add(green);

        Colors skyblue = new Colors();
        skyblue.setColor("SKYBLUE");
        skyblue.setImage(R.drawable.skyblue);
        colorList.add(skyblue);

        Colors blue = new Colors();
        blue.setColor("BLUE");
        blue.setImage(R.drawable.blue);
        colorList.add(blue);

        Colors grey = new Colors();
        grey.setColor("GREY");
        grey.setImage(R.drawable.grey);
        colorList.add(grey);

        Colors purple = new Colors();
        purple.setColor("PURPLE");
        purple.setImage(R.drawable.purple);
        colorList.add(purple);

        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,colors);
        //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorAdapter = new ColorAdapter(getActivity(),colorList);
        spinner_done.setAdapter(colorAdapter);

        //ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item,colors);
        //adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_undone.setAdapter(colorAdapter);
        spinner_done.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("spinner_done",colorList.get(position).getColor());
                String doneColor = colorList.get(position).getColor();
                String colorName = doneColor.toLowerCase(); // 비교할 문자열
                Resources resources = ct.getResources();
                String colorCode = null;
                int colorResId = resources.getIdentifier(colorName, "color", ct.getPackageName());
                if (colorResId != 0) {
                    int colorValue = resources.getColor(colorResId);
                    colorCode = String.format("#%06X", (0xFFFFFF & colorValue));
                    // 색상 코드를 사용하여 필요한 작업 수행
                    Log.d("Color", "Color Code: " + colorCode);
                } else {
                    // 색상 리소스를 찾을 수 없는 경우에 대한 처리
                    Log.d("Color", "Color Resource Not Found");
                }

                color_list.add(colorCode);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_undone.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("spinner_undone",colorList.get(position).getColor());
                String undoneColor = colorList.get(position).getColor();
                String colorName = undoneColor.toLowerCase(); // 비교할 문자열
                Resources resources = ct.getResources();
                String colorCode = null;
                int colorResId = resources.getIdentifier(colorName, "color", ct.getPackageName());
                if (colorResId != 0) {
                    int colorValue = resources.getColor(colorResId);
                    colorCode = String.format("#%06X", (0xFFFFFF & colorValue));
                    // 색상 코드를 사용하여 필요한 작업 수행
                    Log.d("Color", "Color Code: " + colorCode);
                } else {
                    // 색상 리소스를 찾을 수 없는 경우에 대한 처리
                    Log.d("Color", "Color Resource Not Found");
                }

                    color_list.add(colorCode);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle = new Bundle();
                //color_list의 0,1번 인덱스 값은 RED가 들어감 2번이 done, 3번이 undone 색
                Log.d("color_list", String.valueOf(color_list.size()));
                colorHelper.updateColor(color_list.get(2), color_list.get(3));
                //bundle.putStringArrayList("colors", color_list);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                //homeFragment.setArguments(bundle);
                transaction.replace(R.id.fragment_container_view,homeFragment);
                transaction.commit();

            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container_view, new HomeFragment()).commit();
            }
        });
        return view;
    }
}