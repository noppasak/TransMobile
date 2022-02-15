package com.thnopp.it.trans;

/**
 * Created by THLT88 on 2/11/2018.
 */

public class Scanvin {
    String vin;
    String path;
    Long id;
    int desc;


    public Scanvin(){}

    public Scanvin(String vin, String path, Long id, int desc){
        this.vin=vin;
        this.path=path;
        this.id=id;
        this.desc=desc;
    }

    public int getDesc() {
        return desc;
    }

    public void setDesc(int desc) {
        this.desc = desc;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
