package com.example.rule.ui.report;

import androidx.annotation.NonNull;

public class historyModel {
    String studentID, DateOfTaskGiven,
            DateOfTaskUploaded, docID,
            task, taskDetail, Place, PICuid,
            type, result, jawatan, kehadiran,
            khidmatSumbangan, komitmen, eventID,
            pencapaian, penglibatan, totalPoint, component;

    public historyModel() {
    }

    public historyModel(String studentID, String dateOfTaskGiven, String dateOfTaskUploaded, String docID, String task, String taskDetail, String place, String PICuid, String type, String result, String jawatan, String kehadiran, String khidmatSumbangan, String komitmen, String eventID, String pencapaian, String penglibatan, String totalPoint, String component) {
        this.studentID = studentID;
        DateOfTaskGiven = dateOfTaskGiven;
        DateOfTaskUploaded = dateOfTaskUploaded;
        this.docID = docID;
        this.task = task;
        this.taskDetail = taskDetail;
        Place = place;
        this.PICuid = PICuid;
        this.type = type;
        this.result = result;
        this.jawatan = jawatan;
        this.kehadiran = kehadiran;
        this.khidmatSumbangan = khidmatSumbangan;
        this.komitmen = komitmen;
        this.eventID = eventID;
        this.pencapaian = pencapaian;
        this.penglibatan = penglibatan;
        this.totalPoint = totalPoint;
        this.component = component;
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

    public String getPlace() {
        return Place;
    }

    public void setPlace(String place) {
        Place = place;
    }

    public String getPICuid() {
        return PICuid;
    }

    public void setPICuid(String PICuid) {
        this.PICuid = PICuid;
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

    public String getJawatan() {
        return jawatan;
    }

    public void setJawatan(String jawatan) {
        this.jawatan = jawatan;
    }

    public String getKehadiran() {
        return kehadiran;
    }

    public void setKehadiran(String kehadiran) {
        this.kehadiran = kehadiran;
    }

    public String getKhidmatSumbangan() {
        return khidmatSumbangan;
    }

    public void setKhidmatSumbangan(String khidmatSumbangan) {
        this.khidmatSumbangan = khidmatSumbangan;
    }

    public String getKomitmen() {
        return komitmen;
    }

    public void setKomitmen(String komitmen) {
        this.komitmen = komitmen;
    }

    public String getEventID() {
        return eventID;
    }

    public void setEventID(String eventID) {
        this.eventID = eventID;
    }

    public String getPencapaian() {
        return pencapaian;
    }

    public void setPencapaian(String pencapaian) {
        this.pencapaian = pencapaian;
    }

    public String getPenglibatan() {
        return penglibatan;
    }

    public void setPenglibatan(String penglibatan) {
        this.penglibatan = penglibatan;
    }

    public String getTotalPoint() {
        return totalPoint;
    }

    public void setTotalPoint(String totalPoint) {
        this.totalPoint = totalPoint;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    @NonNull
    @Override
    public String toString() {
        return this.docID;
    }
}
