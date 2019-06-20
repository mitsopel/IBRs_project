/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.ibrservice.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import gr.aueb.msc.cs.tims.ibrservice.core.IBR;
import gr.aueb.msc.cs.tims.ibrservice.core.IBR.IBRStringRepresentation;
import gr.aueb.msc.cs.tims.ibrservice.index.IBREntry;
import gr.aueb.msc.cs.tims.ibrservice.index.IBRIndex;
import gr.aueb.msc.cs.tims.ibrservice.index.ImageDescriptor;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import shaded.org.codehaus.plexus.util.StringUtils;

public class ImageResolver implements Runnable {
    private final String url;
    private final Response response;
    private final Request request;
    private final IBRIndex index;
    
    public static final int PHASH_SERVER_PORT = 50000;
    
    public ImageResolver(IBRIndex index, Response response, Request request, String url) {
        this.url = url;
        this.response = response;
        this.request = request;
        this.index = index;
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
            
            Socket s = new Socket("127.0.0.1", PHASH_SERVER_PORT);
            SocketChannel sc = s.getChannel();
            //sc.configureBlocking(true);
            ByteBuffer bb = ByteBuffer.allocate(1024);
            String sendUrl = url.replace("\\", "");
            bb.put(sendUrl.getBytes());
            s.getOutputStream().write(bb.array(), 0, sendUrl.length());
            System.out.println("Wrote "+sendUrl);
            bb.rewind();
            byte[] arr = new byte[1024];
            int read = s.getInputStream().read(arr);
            System.out.println("Actyual read "+read);
            bb = ByteBuffer.wrap(arr, 0, read);
            bb.rewind();
            byte[] readBytes = new byte[read];
            bb.get(readBytes, 0, read);
            String url1 = new String(readBytes);
            System.out.println("Ret value "+url1);
            
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);
            IBREntry maxEntry = index.get(new IBR(url1, IBRStringRepresentation.HEX));
            System.out.println("\n\nEntry Size : "+maxEntry.getImgs().size());
            System.out.println("Entry : "+maxEntry.getImgs());
            System.out.println(mapper.writeValueAsString(maxEntry));
            String ret = mapper.writeValueAsString(maxEntry);
            System.out.println("Counting newlines");
            System.out.println(StringUtils.countMatches(ret, "\n"));
            System.out.println(StringUtils.countMatches(ret, "\\n"));
            System.out.println(StringUtils.countMatches(ret, "\r\n"));
            System.out.println(StringUtils.replace(ret, "\n", "\r\n"));
            ret = StringUtils.replace(ret, "\n", "\r\n");
            body.print(ret);
            body.flush();
            s.close(); 
        } catch (IOException ex) {
            Logger.getLogger(ImageResolver.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
                  
}
