package com.thnopp.it.trans;

public class Dealerinst {
    private String dealer;
    private String dealername;
    private String instruction1;
    private String instruction2;

    public Dealerinst(){}

    public Dealerinst(String dealer, String dealername, String instruction1, String instruction2){
        this.dealer=dealer;
        this.dealername = dealername;
        this.instruction1 = instruction1;
        this.instruction2 = instruction2;
    }

    public String getDealer() {
        return dealer;
    }

    public void setDealer(String dealer) {
        this.dealer = dealer;
    }

    public String getDealername() {
        return dealername;
    }

    public void setDealername(String dealername) {
        this.dealername = dealername;
    }

    public String getInstruction1() {
        return instruction1;
    }

    public void setInstruction1(String instruction1) {
        this.instruction1 = instruction1;
    }

    public String getInstruction2() {
        return instruction2;
    }

    public void setInstruction2(String instruction2) {
        this.instruction2 = instruction2;
    }
}
