/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.ibrservice.service;

import java.io.IOException;
import java.io.PrintStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;


public class IBRResolver implements Runnable{
    private final String[] ibrs;
    private final Response response;
    private final Request request;
    
    public IBRResolver(Response response, Request request, String[] ibrs) {
        this.ibrs = ibrs;
        this.response = response;
        this.request = request;
    }

    @Override
    public void run() {
        try (PrintStream body = response.getPrintStream()) {
            long time = System.currentTimeMillis()+10800000;

            response.setValue("Content-Type", "text/plain");
            response.setValue("Server", "HelloWorld/1.0 (Simple 4.0)");
            response.setValue("Access-Control-Allow-Origin", "*");
            response.setDate("Date", time);
            response.setDate("Last-Modified", time);
            
            body.print("{\"result\":true,\"count\":1}");
            body.flush();
        } catch (IOException ex) {
            Logger.getLogger(ImageResolver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
                  
}
