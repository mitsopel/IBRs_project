/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.phashservice;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ServiceConsole implements Runnable {

    @Override
    public void run() {
        try {
            System.out.print("Enter command : ");
            int cmd = System.in.read();
            if ( cmd == 0 || cmd == '0' ) {
                PHashService.setAlive(false);
            }
        } catch (IOException ex) {
            Logger.getLogger(ServiceConsole.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
