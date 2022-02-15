package com.thnopp.it.trans;

import java.util.Date;

/**
 * Created by THLT88 on 3/24/2018.
 */

public class Vinmaster {
    String vin;
    String engine;
    Long id;
    String dealer;
    String dealer_name;
    String status;
    String ref;
    String dest;
    Date scandt;
    Date shipdt;
    Date arrivaldt;
    String sender;
    String receiver;
    String remark;
    String carrier;
    int q1;
    int q2;
    int q3;
    int q4;
    int q5;
    int q6;
    String comment;
    String receive_by;
    String source;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(String carrier) {
        this.carrier = carrier;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getQ1() {
        return q1;
    }

    public void setQ1(int q1) {
        this.q1 = q1;
    }

    public int getQ2() {
        return q2;
    }

    public void setQ2(int q2) {
        this.q2 = q2;
    }

    public int getQ3() {
        return q3;
    }

    public void setQ3(int q3) {
        this.q3 = q3;
    }

    public int getQ4() {
        return q4;
    }

    public void setQ4(int q4) {
        this.q4 = q4;
    }

    public int getQ5() {
        return q5;
    }

    public void setQ5(int q5) {
        this.q5 = q5;
    }

    public int getQ6() {
        return q6;
    }

    public void setQ6(int q6) {
        this.q6 = q6;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getReceive_by() {
        return receive_by;
    }

    public void setReceive_by(String receive_by) {
        this.receive_by = receive_by;
    }

    public Date getArrivaldt() {
        return arrivaldt;
    }

    public void setArrivaldt(Date arrivaldt) {
        this.arrivaldt = arrivaldt;
    }

    String trailer;
    String ltcode;

    public Vinmaster(){}
    public Vinmaster(String vin, String engine, Long id, String dealer, String dealer_name, String status, String trailer
            , Date scandt, String ltcode){
        this.vin=vin;
        this.engine = engine;
        this.id=id;
        this.dealer=dealer;
        this.dealer_name = dealer_name;
        this.status=status;
        this.trailer=trailer;
        this.scandt =scandt;
        this.ltcode=ltcode;
    }

    public Vinmaster(String vin, String engine, Long id, String dealer, String dealer_name, String status, String trailer
            , Date scandt, String ltcode, Date shipdt){
        this.vin=vin;
        this.engine = engine;
        this.id=id;
        this.dealer=dealer;
        this.dealer_name = dealer_name;
        this.status=status;
        this.trailer=trailer;
        this.scandt =scandt;
        this.ltcode=ltcode;
        this.shipdt = shipdt;
    }

    public String getRef() {
        return ref;
    }

    public void setRef(String ref) {
        this.ref = ref;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }

    public Date getShipdt() {
        return shipdt;
    }

    public void setShipdt(Date shipdt) {
        this.shipdt = shipdt;
    }

    public String getVin() {
        return vin;
    }

    public void setVin(String vin) {
        this.vin = vin;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }


    public String getDealer() {
        return dealer;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getStatus() {
        return status;
    }

    public String getLtcode() {
        return ltcode;
    }

    public void setLtcode(String ltcode) {
        this.ltcode = ltcode;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getScandt() {
        return scandt;
    }

    public void setScandt(Date scandt) {
        this.scandt = scandt;
    }

    public String getTrailer() {
        return trailer;
    }

    public void setTrailer(String trailer) {
        this.trailer = trailer;
    }

    public String getDealer_name() {
        return dealer_name;
    }

    public void setDealer_name(String dealer_name) {
        this.dealer_name = dealer_name;
    }
}
