/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.ibrservicetester;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;


public class IBRServiceTester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        try {
            URL myURL = new URL("http://localhost:8080/");
            //URL myURL = new URL("http://httpbin.org/post");
            HttpURLConnection con = (HttpURLConnection) myURL.openConnection();
            //con.connect();

            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Dummy Client");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            String urlParameters = "ibrs=asasa,sasasa,asasa,asas,asaewe,ewedfgft,bjuy,hh,67676";
            //String urlParameters = "ibr=aaaaaa";

            // Send post request
            con.setDoOutput(true);
            con.setDoInput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(urlParameters);
            //wr.flush();
            wr.close();
            if (con.getResponseCode() == HttpURLConnection.HTTP_OK) {
                DataInputStream rd = new DataInputStream(con.getInputStream());
                System.out.println(con.getResponseMessage() + " : " + con.getResponseCode());
                System.out.println(con.getResponseMessage());
                System.out.println(rd.());
                System.out.println(rd.readLine());
            } else {
                System.out.println(con.getResponseMessage() + " : " + con.getResponseCode());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // new URL() failed
            // ...
        } catch (IOException e) {
            e.printStackTrace();
            // openConnection() failed
            // ...
        }
    }

}
