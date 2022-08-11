package com.example.rule;

import androidx.annotation.NonNull;

public class User {
    public String docID, email, name, position, status, teacherID;
    float maxPoint;

    public User() {
    }

    public User(String docID, String email, String name, String position, String status, String teacherID, float maxPoint) {
        this.docID = docID;
        this.email = email;
        this.name = name;
        this.position = position;
        this.status = status;
        this.teacherID = teacherID;
        this.maxPoint = maxPoint;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTeacherID() {
        return teacherID;
    }

    public void setTeacherID(String teacherID) {
        this.teacherID = teacherID;
    }

    public float getMaxPoint() {
        return maxPoint;
    }

    public void setMaxPoint(float maxPoint) {
        this.maxPoint = maxPoint;
    }

    @NonNull
    @Override
    public String toString() {
        return this.name;
    }
}
