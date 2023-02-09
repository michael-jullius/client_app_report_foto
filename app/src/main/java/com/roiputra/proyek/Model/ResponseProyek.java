package com.roiputra.proyek.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseProyek {
    List<Proyek> data;
    @SerializedName("status")
    @Expose
    private boolean status;
    private String error;

    public List<Proyek> getData() {
        return data;
    }

    public void setData(List<Proyek> data) {
        this.data = data;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
