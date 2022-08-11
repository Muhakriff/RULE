package com.example.rule.ui.home;

public class ptModel { //9 data
    String task;
    String studentID;
    String DateOfTaskGiven;
    String Place;

    String docID;
    String PICuid;
    String taskDetail;
    String type;
    String result;

    public ptModel() {
    }

    public ptModel(String dateOfTaskGiven, String PICuid, String place, String docID, String studentID, String task, String taskDetail, String type, String result) {
        DateOfTaskGiven = dateOfTaskGiven;
        this.PICuid = PICuid;
        Place = place;
        this.docID = docID;
        this.studentID = studentID;
        this.task = task;
        this.taskDetail = taskDetail;
        this.type = type;
        this.result = result;
    }

    public String getDateOfTaskGiven() {
        return DateOfTaskGiven;
    }

    public void setDateOfTaskGiven(String dateOfTaskGiven) {
        DateOfTaskGiven = dateOfTaskGiven;
    }

    public String getPICuid() {
        return PICuid;
    }

    public void setPICuid(String PICuid) {
        this.PICuid = PICuid;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getTaskDetail() {
        return taskDetail;
    }

    public void setTaskDetail(String taskDetail) {
        this.taskDetail = taskDetail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
