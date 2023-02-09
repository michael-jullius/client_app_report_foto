package com.roiputra.proyek.Model;

import java.util.List;

public class ResponseImageData {
    private boolean status;
    private List<ImageData> pagi;
    private List<ImageData> sore;

    public List<ImageData> getPagi() {
        return pagi;
    }

    public void setPagi(List<ImageData> pagi) {
        this.pagi = pagi;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public List<ImageData> getSore() {
        return sore;
    }

    public void setSore(List<ImageData> sore) {
        this.sore = sore;
    }
}
