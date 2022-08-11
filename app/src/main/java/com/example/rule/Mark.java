package com.example.rule;

import androidx.annotation.NonNull;

public class Mark {
    String docID,perkara, elemen, kodPerkara;
    float skor;

    public Mark() {
    }

    public Mark(String docID, String perkara, String elemen, String kodPerkara, float skor) {
        this.docID = docID;
        this.perkara = perkara;
        this.elemen = elemen;
        this.kodPerkara = kodPerkara;
        this.skor = skor;
    }

    public String getDocID() {
        return docID;
    }

    public void setDocID(String docID) {
        this.docID = docID;
    }

    public String getPerkara() {
        return perkara;
    }

    public void setPerkara(String perkara) {
        this.perkara = perkara;
    }

    public String getElemen() {
        return elemen;
    }

    public void setElemen(String elemen) {
        this.elemen = elemen;
    }

    public String getKodPerkara() {
        return kodPerkara;
    }

    public void setKodPerkara(String kodPerkara) {
        this.kodPerkara = kodPerkara;
    }

    public float getSkor() {
        return skor;
    }

    public void setSkor(float skor) {
        this.skor = skor;
    }

    @NonNull
    @Override
    public String toString() {
        return perkara;
    }
}
