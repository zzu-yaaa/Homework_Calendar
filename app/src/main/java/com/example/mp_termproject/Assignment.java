package com.example.mp_termproject;

public class Assignment {
    private String subjectName;
    private String asgName;
    private String link;

    public Assignment(String subjectName,String asgName, String link) {
        this.subjectName = subjectName;
        this.asgName = asgName;
        this.link = link;
    }

    public String getSubjectName() {
        return subjectName;
    }

    public String getAsgName() {
        return asgName;
    }

    public String getLink() {
        return link;
    }
}
