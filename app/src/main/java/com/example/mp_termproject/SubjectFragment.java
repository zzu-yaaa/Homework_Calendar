package com.example.mp_termproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class SubjectFragment extends Fragment {

    Button save_btn;
    CourseDBHelper courseHelper;
    SQLiteDatabase courseDb;
    MyCustomAdapter adapter;
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

        //Generate list View from ArrayList
        displayListView(v,ct);


        save_btn = v.findViewById(R.id.save_subject_button);

        save_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                transaction.replace(R.id.fragment_container_view,homeFragment);
                transaction.commit();
            }
        });

        return v;
    }

    private void displayListView(View v, Context ct) {
        ListView listView = v.findViewById(R.id.listViewC);
        courseHelper = new CourseDBHelper(ct);
        //Get check status
        courseDb = courseHelper.getReadableDatabase();
        String[] check = courseHelper.getCheck();

        //Add lecture name to course list
        ArrayList<CourseCheck> courseList = new ArrayList<CourseCheck>();
        String[][] courseArray = courseHelper.selectCourse();

        int cNum = courseHelper.courseNum();
        for (int i = 0; i < cNum; i++) {

            if(check[i].equals("check")) {
                CourseCheck course = new CourseCheck(courseArray[i][0], true);
                courseList.add(course);
                Log.d("course list", "강의 list에 추가 : " + course.getName() + " " + " 상태 : " + check[i]);
            }else{
                CourseCheck course = new CourseCheck(courseArray[i][0], false);
                courseList.add(course);
                Log.d("course list", "강의 list에 추가 : " + course.getName() + " " + " 상태 : " + check[i]);
            }
        }

        //Create adapter and assign it to ListView
        adapter = new MyCustomAdapter(ct, R.layout.listview_info, courseList);
        listView = (ListView) v.findViewById(R.id.listViewC);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            }
        });
    }

    private class MyCustomAdapter extends ArrayAdapter<CourseCheck> {
        private ArrayList<CourseCheck> courseList;
        ViewHolder holder;

        public MyCustomAdapter(@NonNull Context context, int resource, ArrayList<CourseCheck> courseList) {
            super(context, resource, courseList);
            this.courseList = new ArrayList<CourseCheck>();
            this.courseList.addAll(courseList);
        }

        private class ViewHolder {
            TextView name;
            CheckBox check;
        }

        //Display listView entries
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            holder = null;
            Log.v("Convert View 실행", String.valueOf(position));

            courseHelper = new CourseDBHelper(ct);
            //Get check status
            courseDb = courseHelper.getWritableDatabase();

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) ct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listview_info, null);

                holder = new ViewHolder();
                holder.name = (TextView) convertView.findViewById(R.id.courseName);
                holder.check = (CheckBox) convertView.findViewById(R.id.checkB);
                convertView.setTag(holder);

                //Set check box status change event
                holder.check.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        CourseCheck course = (CourseCheck) cb.getTag();

                        holder.check.setSelected(cb.isChecked());   //Check
                        courseList.get(position).setSelected(cb.isChecked()); // Update selection status

                        //Reflect check changes to db
                        if(cb.isChecked() == true){
                            courseHelper.updateChecked(position, "check");
                        }
                        if (cb.isChecked() == false){
                            courseHelper.updateChecked(position, null);
                        }

                        for (CourseCheck c : courseList) {
                            Log.d("checkbox", "Name: " + c.getName() + ", Selected: " + c.isSelected()); //Purpose of verification
                        }
                    }
                });

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            CourseCheck course = courseList.get(position);
            holder.name.setText(course.getName());
            holder.check.setChecked(course.isSelected());
            holder.name.setTag(course);

            Log.d("checkbox", "check box " + course.getName() + " 추가");

            return convertView;
        }
    }
    @Override
    public void onDestroy() {
        courseHelper.close();
        courseDb.close();
        adapter.clear();
        super.onDestroy();
    }
}