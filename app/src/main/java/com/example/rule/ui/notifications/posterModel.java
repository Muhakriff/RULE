package com.example.rule.ui.notifications;

public class posterModel {
    String task, studentID, DateOfTaskGiven, Place, DateOfTaskUploaded, docID, PICuid, taskDetail, type, result;

    public posterModel() {
    }

    public posterModel(String task, String studentID, String dateOfTaskGiven, String place, String dateOfTaskUploaded, String docID, String PICuid, String taskDetail, String type, String result) {
        this.task = task;
        this.studentID = studentID;
        DateOfTaskGiven = dateOfTaskGiven;
        Place = place;
        DateOfTaskUploaded = dateOfTaskUploaded;
        this.docID = docID;
        this.PICuid = PICuid;
        this.taskDetail = taskDetail;
        this.type = type;
        this.result = result;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public String getDateOfTaskGiven() {
        return DateOfTaskGiven;
    }

    public void setDateOfTaskGiven(String dateOfTaskGiven) {
        DateOfTaskGiven = dateOfTaskGiven;
    }

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getDateOfTaskUploaded() {
        return DateOfTaskUploaded;
    }

    public void setDateOfTaskUploaded(String dateOfTaskUploaded) {
        DateOfTaskUploaded = dateOfTaskUploaded;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getPICuid() {
        return PICuid;
    }

    public void setPICuid(String PICuid) {
        this.PICuid = PICuid;
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
