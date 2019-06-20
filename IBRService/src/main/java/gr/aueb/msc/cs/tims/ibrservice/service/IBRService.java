/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.ibrservice.service;

import gr.aueb.msc.cs.tims.ibrservice.core.IBR;
import gr.aueb.msc.cs.tims.ibrservice.index.IBRIndex;
import gr.aueb.msc.cs.tims.ibrservice.index.ByteableImageDescriptor;
import gr.aueb.msc.cs.tims.ibrservice.index.IBREntry;
import gr.aueb.msc.cs.tims.ibrservice.util.Distances;
import gr.aueb.msc.cs.tims.ibrservice.util.JSONUtils;
import java.io.File;
import java.io.PrintStream;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.SocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;
import org.simpleframework.http.core.ContainerSocketProcessor;
import org.simpleframework.transport.SocketProcessor;
import org.simpleframework.transport.connect.Connection;
import org.simpleframework.transport.connect.SocketConnection;


public class IBRService implements Container {
    private final IBRIndex index;

    public IBRService(IBRIndex index) {
        this.index = index;
    }
    
    // Trivial async handler using thread per request
    @Override
    public void handle(Request request, Response response) {
        try {
            // Sample IBR request
            String[] ibrs = request.getParameter("ibrs").split(",");
            System.out.println("Recvd request");
            if ( ibrs.length == 1 ) {
                if ( ibrs[0].contains(".") || ibrs[0].contains(File.separator) ) {
                    System.out.println(ibrs[0]);
                    (new ImageResolver(index, response, request, ibrs[0])).run();
                } else {
                    // Else it' s an IBR String
                    System.out.println("Recvd request and it' s an IBR");
                    (new IBRResolver(response, request, ibrs)).run();
                }
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    } 

    public static void main(String[] list) throws Exception {
        //runTests();
              
        IBRIndex ibrIdx = new IBRIndex();
        ibrIdx.initialize(false);
        
        Container container = new IBRService(ibrIdx);
        SocketProcessor server = new ContainerSocketProcessor(container);
        Connection connection = new SocketConnection(server);
        SocketAddress address = new InetSocketAddress(8080);

        connection.connect(address);
        System.out.println("Server running");
        System.out.print("Enter command : ");
        int cmd = System.in.read();
        if ( cmd == 0 || cmd == '0' ) {
            connection.close();
        }
    }
    
    public static void runTests() {
        /*
        System.out.println("---------------");
        System.out.println("    TESTING    ");
        System.out.println("---------------");
        
        //Test IBR printing
        System.out.println();
        System.out.println("Test IBR generation and printing");
        System.out.println(new IBR());
        System.out.println(new IBR());
        System.out.println(new IBR());
        System.out.println(new IBR());
        System.out.println(new IBR());
        
        System.out.println();
        System.out.println("Test LSH hash function generation and printing");
        IBRIndex ibrIdx = new IBRIndex();
        ibrIdx.initialize();
        IBR ref = new IBR(1212121);
        ibrIdx.add(ref, "nick");
        System.out.println(ref);
        byte b1 = (byte) 129;
String s1 = String.format("%8s", Integer.toBinaryString(b1 & 0xFF)).replace(' ', '0');
System.out.println(s1); // 10000001
System.out.println("Hasher 0 : "+ibrIdx.getHashers()[0].hash(ref.getIbr())); // 10000001
        ibrIdx.printHashes(ref);
        System.out.println();
        // Test Hamming Distances
        System.out.println("Test Distances");
        System.out.println("Hamming Distance between 1 and 3: "+Distances.HammingDistance(
                ByteBuffer.allocate(4).putInt(1).array(),
                ByteBuffer.allocate(4).putInt(3).array()));
        System.out.println("Hamming Distance between 1 and 2: "+Distances.HammingDistance(
                ByteBuffer.allocate(4).putInt(1).array(),
                ByteBuffer.allocate(4).putInt(3).array()));
        
        System.out.println();
        // Test Hamming Distances
        System.out.println("Test IBR I/O");
        System.out.println(ibrIdx.get(ref));
        ibrIdx.add(ref, "test");
        System.out.println(ibrIdx.get(ref));
                */
    }
}