/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deadlock;

/**
 *
 * @author User
 */

public class printer {

    public static void main(String[] args) {
        String tkd = "Tanmoy";
        String AKD = "Anwoy";

        Thread t1 = new Thread() {
            public void run() {
                synchronized (tkd) {
                    System.out.println("Thread 1: " + tkd);

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }

                    synchronized (AKD) {
                        System.out.println("Thread 1: " + AKD);
                    }
                }
            }
        };
        
        

        
        Thread t2 = new Thread() {
            public void run() {
                synchronized (AKD) {
                    System.out.println("Thread 2: "+ AKD);

                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }

                    synchronized (tkd) {
                        System.out.println("Thread 2: "+tkd);
                    }
                }
            }
        };

        t1.start();
        t2.start();
    }
}
