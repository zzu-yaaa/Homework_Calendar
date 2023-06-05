package com.example.mp_termproject;

public class CourseCheck {
    String name = null;
    boolean selected = false;

    public CourseCheck(String name, boolean selected){
        super();
        this.name = name;
        this.selected = selected;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public boolean isSelected(){
        return selected;
    }
    public void setSelected(boolean select){
        this.selected = select;
    }
}
