package com.roiputra.proyek.Model;

import java.util.ArrayList;
import java.util.List;

public class ResponseDetailImage {
    private List<DetailImage> data;
    private boolean status;

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }


    public List<DetailImage> getData() {
        return data;
    }

    public void setData(List<DetailImage> data) {
        this.data = data;
    }
}
