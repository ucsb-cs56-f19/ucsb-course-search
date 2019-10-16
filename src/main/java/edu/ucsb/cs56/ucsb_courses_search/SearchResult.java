package edu.ucsb.cs56.ucsb_courses_search;

import java.io.Serializable;

public class SearchResult implements Serializable {

    private String subjectArea;
    private String quarter;
    private String courseLevel;

    public String getSubjectArea() {
        return this.subjectArea;
    }
    public void setSubjectArea(String subjectArea) {
        this.subjectArea = subjectArea;
    }

    public String getQuarter() {
        return this.quarter;
    }
    public void setQuarter(String quarter) {
        this.quarter = quarter;
    }

    public String getCourseLevel() {
        return this.courseLevel;
    }
    public void setCourseLevel(String courseLevel) {
        this.courseLevel = courseLevel;
    }

    public SearchResult() {}

}