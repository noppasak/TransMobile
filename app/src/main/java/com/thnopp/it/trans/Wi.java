package com.thnopp.it.trans;

public class Wi {
    private String id;
    private String wicd;
    private String wi_desc;
    private String path;
    private String lastupdate;

    public Wi(){}

    public Wi(String id, String wicd, String wi_desc, String path, String lastupdate ){
        this.id=id;
        this.wicd =wicd;
        this.wi_desc = wi_desc;
        this.path=path;
        this.lastupdate=lastupdate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWicd() {
        return wicd;
    }

    public void setWicd(String wicd) {
        this.wicd = wicd;
    }

    public String getWi_desc() {
        return wi_desc;
    }

    public void setWi_desc(String wi_desc) {
        this.wi_desc = wi_desc;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getLastupdate() {
        return lastupdate;
    }

    public void setLastupdate(String lastupdate) {
        this.lastupdate = lastupdate;
    }
}
