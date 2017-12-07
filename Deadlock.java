/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deadlock;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
class Resource {

    boolean check = false;

    synchronized public void f1() {
        while (!check) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Resource.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

        check = true;
        notify();
    }

    synchronized public void f2() {
        while (!check) {
            try {
                wait();
            } catch (InterruptedException ex) {
                Logger.getLogger(Resource.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
        check = true;
        notify();
    }
}

class mThread implements Runnable {
    Resource R;

    public mThread(Resource R) {
        this.R = R;
    }
    
    @Override
    public void run() {
        R.f1();
    }
    
}

class nThread implements Runnable {
    Resource R;

    public nThread(Resource R) {
        this.R = R;
    }

    @Override
    public void run() {
        R.f2();
    }
    
    
}



public class Deadlock {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Resource r  = new Resource();
        Thread t1 = new Thread(new mThread(r));
        Thread t2 = new Thread(new nThread(r));
        t1.start();
        t2.start();
    }

}
