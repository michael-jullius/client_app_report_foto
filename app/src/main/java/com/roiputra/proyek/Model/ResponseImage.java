package com.roiputra.proyek.Model;

import java.util.List;

public class ResponseImage {
    private boolean status;
    private String error;
    List<ImageDate> data;


    public List<ImageDate> getData() {
        return data;
    }

    public void setData(List<ImageDate> data) {
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
