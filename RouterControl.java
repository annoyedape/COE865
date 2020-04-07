/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package routercontrol;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author ape
 */
public class RouterControl {

    /**
     * @param args the command line arguments
     */
    private RController[] routers = new RController[4];
    int sourceArray[] = new int[4];
    int destArray[] = new int[4];
    int pathArray[] = new int[10];
    int costArray[] = new int[10];
    int bwArray[] = new int [10];
    int source;
    int dest;
    
    public static void main(String[] args) {
        // TODO code application logic here
        RouterControl r = new RouterControl();
        System.out.println("Create Routers:");
        r.createRouters();
        System.out.println("Display Routers:");
        r.displayRouters();
        System.out.println("Please enter your source:");
        Scanner mySource = new Scanner(System.in);
        r.source = mySource.nextInt();
        System.out.println("Please enter your destination:");
        Scanner myDest = new Scanner(System.in);
        r.dest = myDest.nextInt();
        System.out.println("The source client is: ASN" + r.source);
        System.out.println("The destination client is: ASN" + r.dest);
        
        while(true){
            System.out.println("Seeking source and destination client:");
            r.findFromTo();
            System.out.println("Finding Paths...");
            r.findPath();
            //System.out.println("Showing Paths and Costs:");
            //r.printPath();
            System.out.println("Choosing Best Path...");
            r.findBestPath();
            try {
                TimeUnit.MINUTES.sleep(3);
            } catch (InterruptedException ex) {
                Logger.getLogger(RouterControl.class.getName()).log(Level.SEVERE, null, ex);
            }
            System.out.println("Recomputing Path...");
        }
        
        
    }
    
    public void createRouters(){
        routers[0] = new RController(1, "10.1.1.1", 10, 20, 1, 2, 2, 3, 0, 1, 1, 0, 3, 3, 3);
        routers[1] = new RController(2, "10.1.1.2", 10, 40, 1, 4, 1, 3, 4, 1, 1, 1, 3, 3, 3);
        routers[2] = new RController(3, "10.1.1.3", 10, 30, 1, 3, 1, 2, 4, 1, 1, 1, 3, 3, 3);
        routers[3] = new RController(4, "10.1.1.4", 20, 30, 2, 3, 2, 3, 0, 1, 1, 0, 3, 3, 3);
    }
    public void displayRouters() {
        for (RController r:routers){
            System.out.println("Ip: " + r.getIp()+" RCID: "+r.getRcid() + " ASN1: " + r.getAsn1() + " ASN1 Cost: " + r.getCost1() +" ASN2: " + r.getAsn2() + " ASN2 Cost: " + r.getCost2() + " Interface 1: RC" + r.getPort1() +  " Interface 2: RC" + r.getPort2());
        }
    }
    
    public void findFromTo(){
        for (int i = 0; i < 4; i++){
            if( (routers[i].asn1 == source)|| (routers[i].asn2 == source) ){
                sourceArray[i] = 1;
            }
            else {
                sourceArray[i] = 0;
            }
        }
        for (int i = 0; i < 4; i++){
            if( (routers[i].asn1 == dest)|| (routers[i].asn2 == dest) ){
                destArray[i] = 1;
            }
            else {
                destArray[i] = 0;
            }
        }
        //System.out.println("Sources: " + Arrays.toString(sourceArray));
        //System.out.println("Destinations: " + Arrays.toString(destArray));
    }
    
    public void findPath() {
        int k = 0;
        int sLocalCost = 0;
        int dLocalCost = 0;
        for (int i = 0; i < 4; i++){
            if (sourceArray[i] == 1){
                if (routers[i].asn1 == source){
                    sLocalCost = routers[i].cost1;
                }
                else{
                    sLocalCost = routers[i].cost2;
                }
                for (int j = 0; j < 4; j++){
                    if (destArray[j] == 1){
                        if (routers[j].asn1 == dest){
                            dLocalCost = routers[j].cost1;
                        }
                        else if (routers[j].asn2 == dest){
                            dLocalCost = routers[j].cost2;
                        }
                        else{}
                        if (routers[i].port1 == j+1){
                            pathArray[k] = (routers[i].rcid*10)+(routers[j].rcid);
                            costArray[k] = routers[i].intCost1 + sLocalCost + dLocalCost;
                            bwArray[k] = routers[i].bw1;
                            k++;
                        }
                        else if (routers[i].port2 == j+1){
                            pathArray[k] = (routers[i].rcid*10)+(routers[j].rcid);
                            costArray[k] = routers[i].intCost2 + sLocalCost + dLocalCost;
                            bwArray[k] = routers[i].bw2;
                            k++;
                        }
                        else if (routers[i].port3 == j+1){
                            pathArray[k] = (routers[i].rcid*10)+(routers[j].rcid);
                            costArray[k] = routers[i].intCost3 + sLocalCost + dLocalCost;
                            bwArray[k] = routers[i].bw3;
                            k++;
                        }
                        else if (routers[i] == routers[j]){
                            pathArray[k] = (routers[j].rcid);
                            costArray[k] = sLocalCost + dLocalCost;
                            bwArray[k] = 25;
                            k++;
                        }
                        else {
                            if (routers[i].port1 != 0){
                                if (routers[(routers[i].port1)-1].port1 == j+1){
                                    pathArray[k] = (routers[i].rcid*100)+(routers[(routers[i].port1)-1].rcid*10)+(routers[j].rcid);
                                    costArray[k] = routers[i].intCost1 + routers[(routers[i].port1)-1].intCost1 + sLocalCost + dLocalCost;
                                    if (routers[(routers[i].port1)-1].bw1 < bwArray[k]){
                                        bwArray[k] = routers[(routers[i].port1)-1].bw1;
                                    }
                                    else {
                                        bwArray[k] = routers[i].bw1;
                                    }
                                    k++;
                                }
                                else if (routers[(routers[i].port1)-1].port2 == j+1){
                                    pathArray[k] = (routers[i].rcid*100)+(routers[(routers[i].port1)-1].rcid*10)+(routers[j].rcid);
                                    costArray[k] = routers[i].intCost1 + routers[(routers[i].port1)-1].intCost2 + sLocalCost + dLocalCost;
                                    if (routers[(routers[i].port1)-1].bw1 < bwArray[k]){
                                        bwArray[k] = routers[(routers[i].port1)-1].bw2;
                                    }
                                    else {
                                        bwArray[k] = routers[i].bw2;
                                    }
                                    k++;
                                }
                                else if (routers[(routers[i].port1)-1].port3 == j+1){
                                    pathArray[k] = (routers[i].rcid*100)+(routers[(routers[i].port1)-1].rcid*10)+(routers[j].rcid);
                                    costArray[k] = routers[i].intCost1 + routers[(routers[i].port1)-1].intCost3 + sLocalCost + dLocalCost;
                                    if (routers[(routers[i].port1)-1].bw1 < bwArray[k]){
                                        bwArray[k] = routers[(routers[i].port1)-1].bw3;
                                    }
                                    else {
                                        bwArray[k] = routers[i].bw3;
                                    }
                                    k++;
                                }
                                else {
                                    if (routers[routers[(routers[i].port1)-1].port1-1].port1 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port1)-1].rcid*100)+(routers[routers[(routers[i].port1)-1].port1-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port1)-1].intCost1 + routers[routers[(routers[i].port1)-1].port1-1].intCost1 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port1)-1].port1-1].port2 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port1)-1].rcid*100)+(routers[routers[(routers[i].port1)-1].port1-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port1)-1].intCost1 + routers[routers[(routers[i].port1)-1].port1-1].intCost2 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port1)-1].port1-1].port3 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port1)-1].rcid*100)+(routers[routers[(routers[i].port1)-1].port1-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port1)-1].intCost1 + routers[routers[(routers[i].port1)-1].port1-1].intCost3 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port1)-1].port2-1].port1 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port1)-1].rcid*100)+(routers[routers[(routers[i].port1)-1].port2-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port1)-1].intCost1 + routers[routers[(routers[i].port1)-1].port2-1].intCost1 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port1)-1].port2-1].port2 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port1)-1].rcid*100)+(routers[routers[(routers[i].port1)-1].port2-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port1)-1].intCost1 + routers[routers[(routers[i].port1)-1].port2-1].intCost2 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port1)-1].port2-1].port3 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port1)-1].rcid*100)+(routers[routers[(routers[i].port1)-1].port2-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port1)-1].intCost1 + routers[routers[(routers[i].port1)-1].port2-1].intCost3 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port1)-1].port3-1].port1 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port1)-1].rcid*100)+(routers[routers[(routers[i].port1)-1].port3-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port1)-1].intCost1 + routers[routers[(routers[i].port1)-1].port3-1].intCost1 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port1)-1].port3-1].port2 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port1)-1].rcid*100)+(routers[routers[(routers[i].port1)-1].port3-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port1)-1].intCost1 + routers[routers[(routers[i].port1)-1].port3-1].intCost2 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port1)-1].port3-1].port3 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port1)-1].rcid*100)+(routers[routers[(routers[i].port1)-1].port3-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port1)-1].intCost1 + routers[routers[(routers[i].port1)-1].port3-1].intCost3 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                }
                            }
                            if (routers[i].port2 != 0){
                                if (routers[(routers[i].port2)-1].port1 == j+1){
                                    pathArray[k] = (routers[i].rcid*100)+(routers[(routers[i].port2)-1].rcid*10)+(routers[j].rcid);
                                    costArray[k] = routers[i].intCost1 + routers[(routers[i].port2)-1].intCost1 + sLocalCost + dLocalCost;
                                    if (routers[(routers[i].port2)-1].bw1 < bwArray[k]){
                                        bwArray[k] = routers[(routers[i].port2)-1].bw1;
                                    }
                                    else {
                                        bwArray[k] = routers[i].bw2;
                                    }
                                    k++;
                                }
                                else if (routers[(routers[i].port2)-1].port2 == j+1){
                                    pathArray[k] = (routers[i].rcid*100)+(routers[(routers[i].port2)-1].rcid*10)+(routers[j].rcid);
                                    costArray[k] = routers[i].intCost1 + routers[(routers[i].port2)-1].intCost2 + sLocalCost + dLocalCost;
                                    if (routers[(routers[i].port2)-1].bw2 < bwArray[k]){
                                        bwArray[k] = routers[(routers[i].port2)-1].bw2;
                                    }
                                    else {
                                        bwArray[k] = routers[i].bw2;
                                    }
                                    k++;
                                }
                                else if (routers[(routers[i].port2)-1].port3 == j+1){
                                    pathArray[k] = (routers[i].rcid*100)+(routers[(routers[i].port2)-1].rcid*10)+(routers[j].rcid);
                                    costArray[k] = routers[i].intCost1 + routers[(routers[i].port2)-1].intCost3 + sLocalCost + dLocalCost;
                                    if (routers[(routers[i].port2)-1].bw3 < bwArray[k]){
                                        bwArray[k] = routers[(routers[i].port2)-1].bw3;
                                    }
                                    else {
                                        bwArray[k] = routers[i].bw2;
                                    }
                                    k++;
                                }
                                else {
                                    if (routers[routers[(routers[i].port2)-1].port1-1].port1 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port2)-1].rcid*100)+(routers[routers[(routers[i].port2)-1].port1-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port2)-1].intCost1 + routers[routers[(routers[i].port2)-1].port1-1].intCost1 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port2)-1].port1-1].port2 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port2)-1].rcid*100)+(routers[routers[(routers[i].port2)-1].port1-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port2)-1].intCost1 + routers[routers[(routers[i].port2)-1].port1-1].intCost2 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port2)-1].port1-1].port3 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port2)-1].rcid*100)+(routers[routers[(routers[i].port2)-1].port1-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port2)-1].intCost1 + routers[routers[(routers[i].port2)-1].port1-1].intCost3 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port2)-1].port2-1].port1 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port2)-1].rcid*100)+(routers[routers[(routers[i].port2)-1].port2-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port2)-1].intCost1 + routers[routers[(routers[i].port2)-1].port2-1].intCost1 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port2)-1].port2-1].port2 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port2)-1].rcid*100)+(routers[routers[(routers[i].port2)-1].port2-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port2)-1].intCost1 + routers[routers[(routers[i].port2)-1].port2-1].intCost2 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port2)-1].port2-1].port3 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port2)-1].rcid*100)+(routers[routers[(routers[i].port2)-1].port2-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port2)-1].intCost1 + routers[routers[(routers[i].port2)-1].port2-1].intCost3 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port2)-1].port3-1].port1 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port2)-1].rcid*100)+(routers[routers[(routers[i].port2)-1].port3-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port2)-1].intCost1 + routers[routers[(routers[i].port2)-1].port3-1].intCost1 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port2)-1].port3-1].port2 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port2)-1].rcid*100)+(routers[routers[(routers[i].port2)-1].port3-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port2)-1].intCost1 + routers[routers[(routers[i].port2)-1].port3-1].intCost2 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port2)-1].port3-1].port3 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port2)-1].rcid*100)+(routers[routers[(routers[i].port2)-1].port3-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port2)-1].intCost1 + routers[routers[(routers[i].port2)-1].port3-1].intCost3 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                }
                            }
                            if (routers[i].port3 != 0){
                                if (routers[(routers[i].port3)-1].port1 == j+1){
                                    pathArray[k] = (routers[i].rcid*100)+(routers[(routers[i].port3)-1].rcid*10)+(routers[j].rcid);
                                    costArray[k] = routers[i].intCost1 + routers[(routers[i].port3)-1].intCost1 + sLocalCost + dLocalCost;
                                    if (routers[(routers[i].port3)-1].bw1 < bwArray[k]){
                                        bwArray[k] = routers[(routers[i].port3)-1].bw1;
                                    }
                                    else {
                                        bwArray[k] = routers[i].bw3;
                                    }
                                    k++;
                                }
                                else if (routers[(routers[i].port3)-1].port2 == j+1){
                                    pathArray[k] = (routers[i].rcid*100)+(routers[(routers[i].port3)-1].rcid*10)+(routers[j].rcid);
                                    costArray[k] = routers[i].intCost1 + routers[(routers[i].port3)-1].intCost2 + sLocalCost + dLocalCost;
                                    if (routers[(routers[i].port3)-1].bw2 < bwArray[k]){
                                        bwArray[k] = routers[(routers[i].port3)-1].bw2;
                                    }
                                    else {
                                        bwArray[k] = routers[i].bw3;
                                    }
                                    k++;
                                }
                                else if (routers[(routers[i].port3)-1].port3 == j+1){
                                    pathArray[k] = (routers[i].rcid*100)+(routers[(routers[i].port3)-1].rcid*10)+(routers[j].rcid);
                                    costArray[k] = routers[i].intCost1 + routers[(routers[i].port3)-1].intCost3 + sLocalCost + dLocalCost;
                                    if (routers[(routers[i].port3)-1].bw3 < bwArray[k]){
                                        bwArray[k] = routers[(routers[i].port3)-1].bw3;
                                    }
                                    else {
                                        bwArray[k] = routers[i].bw3;
                                    }
                                    k++;
                                }
                                else {
                                    if (routers[routers[(routers[i].port3)-1].port1-1].port1 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port3)-1].rcid*100)+(routers[routers[(routers[i].port3)-1].port1-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port3)-1].intCost1 + routers[routers[(routers[i].port3)-1].port1-1].intCost1 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port3)-1].port1-1].port2 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port3)-1].rcid*100)+(routers[routers[(routers[i].port3)-1].port1-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port3)-1].intCost1 + routers[routers[(routers[i].port3)-1].port1-1].intCost2 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port3)-1].port1-1].port3 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port3)-1].rcid*100)+(routers[routers[(routers[i].port3)-1].port1-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port3)-1].intCost1 + routers[routers[(routers[i].port3)-1].port1-1].intCost3 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port3)-1].port2-1].port1 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port3)-1].rcid*100)+(routers[routers[(routers[i].port3)-1].port2-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port3)-1].intCost1 + routers[routers[(routers[i].port3)-1].port2-1].intCost1 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port3)-1].port2-1].port2 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port3)-1].rcid*100)+(routers[routers[(routers[i].port3)-1].port2-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port3)-1].intCost1 + routers[routers[(routers[i].port3)-1].port2-1].intCost2 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port3)-1].port2-1].port3 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port3)-1].rcid*100)+(routers[routers[(routers[i].port3)-1].port2-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port3)-1].intCost1 + routers[routers[(routers[i].port3)-1].port2-1].intCost3 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port3)-1].port3-1].port1 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port3)-1].rcid*100)+(routers[routers[(routers[i].port3)-1].port3-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port3)-1].intCost1 + routers[routers[(routers[i].port3)-1].port3-1].intCost1 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port3)-1].port3-1].port2 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port3)-1].rcid*100)+(routers[routers[(routers[i].port3)-1].port3-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port3)-1].intCost1 + routers[routers[(routers[i].port3)-1].port3-1].intCost2 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                    else if (routers[routers[(routers[i].port3)-1].port3-1].port3 == j+1){
                                       pathArray[k] = (routers[i].rcid*1000)+(routers[(routers[i].port3)-1].rcid*100)+(routers[routers[(routers[i].port3)-1].port3-1].rcid*10)+(routers[j].rcid);
                                       costArray[k] = routers[i].intCost1 + routers[(routers[i].port3)-1].intCost1 + routers[routers[(routers[i].port3)-1].port3-1].intCost3 + sLocalCost + dLocalCost;
                                       k++; 
                                    }
                                }
                            }
                        }
                    }
                    else {}
                }
            }
            else {}
        }
    }
    public void printPath() {
        System.out.println("Path: " + Arrays.toString(pathArray));
        System.out.println("Cost: " + Arrays.toString(costArray));
        System.out.println("Bandwidth: " + Arrays.toString(bwArray));
    }
    public void findBestPath() {
        int min = costArray[0];
	int index=0;
        for(int i = 0; i < costArray.length; i++)
	       {
	            if(min > costArray[i] &&(costArray[i] != 0))
	            {
	                min = costArray[i];
	                index=i;
	            }
	        }

        //System.out.println("Best path is: " + pathArray[index]);
        //System.out.println("Cost of best path: " + costArray[index]);
        //System.out.println("Bandwidth of path is: " + bwArray[index]);
        String pathString = String.valueOf(pathArray[index]);
        char[] fixedString = new char[pathString.length()];
        for(int i = 0; i < pathString.length(); i++){
            fixedString[i] = pathString.charAt(i);    
        }
        System.out.println("Destination: " + dest + ", Path = " + Arrays.toString(fixedString) + ", Bandwidth = " + bwArray[index] + ", Cost = " + costArray[index]);
    }
    
}