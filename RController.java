/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routercontrol;

/**
 *
 * @author ape
 */
public class RController {
    private int rcid;
    private String ip;
    private int asn1;
    private int asn2;
    private double cost1;
    private double cost2;
    private int port1;
    private int port2;
    private int port3;
    private int intCost1;
    private int intCost2;
    private int intCost3;

    public RController(int rcid, String ip, int asn1, int asn2, double cost1, double cost2, int port1, int port2, int port3, int intCost1, int intCost2, int intCost3) {
        this.rcid = rcid;
        this.ip = ip;
        this.asn1 = asn1;
        this.asn2 = asn2;
        this.cost1 = cost1;
        this.cost2 = cost2;
        this.port1 = port1;
        this.port2 = port2;
        this.port3 = port3;
        this.intCost1 = intCost1;
        this.intCost2 = intCost2;
        this.intCost3 = intCost3;
    }

    public int getIntCost1() {
        return intCost1;
    }

    public int getIntCost2() {
        return intCost2;
    }

    public int getIntCost3() {
        return intCost3;
    }

    public void setIntCost1(int intCost1) {
        this.intCost1 = intCost1;
    }

    public void setIntCost2(int intCost2) {
        this.intCost2 = intCost2;
    }

    public void setIntCost3(int intCost3) {
        this.intCost3 = intCost3;
    }

    

    public void setPort1(int port1) {
        this.port1 = port1;
    }

    public void setPort2(int port2) {
        this.port2 = port2;
    }

    public void setPort3(int port3) {
        this.port3 = port3;
    }

    public int getPort1() {
        return port1;
    }

    public int getPort2() {
        return port2;
    }

    public int getPort3() {
        return port3;
    }

    public int getRcid() {
        return rcid;
    }

    public String getIp() {
        return ip;
    }

    public int getAsn1() {
        return asn1;
    }

    public int getAsn2() {
        return asn2;
    }

    public double getCost1() {
        return cost1;
    }

    public double getCost2() {
        return cost2;
    }

    public void setRcid(int rcid) {
        this.rcid = rcid;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public void setAsn1(int asn1) {
        this.asn1 = asn1;
    }

    public void setAsn2(int asn2) {
        this.asn2 = asn2;
    }

    public void setCost1(double cost1) {
        this.cost1 = cost1;
    }

    public void setCost2(double cost2) {
        this.cost2 = cost2;
    }
    
    
}
