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
public class RouterControl {

    /**
     * @param args the command line arguments
     */
    private RController[] routers = new RController[4];
    
    public static void main(String[] args) {
        // TODO code application logic here
        RouterControl r = new RouterControl();
        System.out.println("Create Routers:");
        r.createRouters();
        System.out.println("Display Routers:");
        r.displayRouters();
        
    }
    
    public void createRouters(){
        routers[0] = new RController(1, "10.1.1.1", 10, 20, 1, 2, 2, 3, 0, 1, 1, 0);
        routers[1] = new RController(2, "10.1.1.2", 10, 40, 1, 4, 1, 3, 4, 1, 1, 1);
        routers[2] = new RController(3, "10.1.1.3", 10, 30, 1, 3, 1, 2, 4, 1, 1, 1);
        routers[3] = new RController(4, "10.1.1.4", 20, 30, 2, 3, 2, 3, 0, 1, 1, 0);
    }
    public void displayRouters() {
        for (RController r:routers){
            System.out.println("Ip:" + r.getIp()+" ID: "+r.getRcid() + " ASN1: " + r.getAsn1() + " ASN1 Cost: " + r.getCost1() +" ASN2: " + r.getAsn2() + " ASN2 Cost: " + r.getCost2() + " Interface 1: RC" + r.getPort1() +  " Interface 2: RC" + r.getPort2());
        }
    }
    
}
