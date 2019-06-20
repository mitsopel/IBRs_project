/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.aueb.msc.cs.tims.phashservice;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PHashServer implements Runnable {
    public static final int PHASH_SERVER_PORT = 50000;
    Selector selector;
    private final Map<SocketChannel, String> socketMap = new ConcurrentHashMap<>();
    
    public PHashServer(Selector sel) {
        try {
            selector = sel;
            
            ServerSocketChannel pHashServerChannel = ServerSocketChannel.open();
            pHashServerChannel.configureBlocking(false);
            int options = SelectionKey.OP_ACCEPT;
            pHashServerChannel.register(selector, options );
            pHashServerChannel.socket().bind(new InetSocketAddress(PHASH_SERVER_PORT));
            
            
        } catch (IOException ex) {
            Logger.getLogger(PHashServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    
    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                System.out.println("Is connected ");
                selector.select();
                Iterator it = selector.selectedKeys().iterator();
                while (it.hasNext()) {
                System.out.println("Is connected ");
                    SelectionKey selKey = (SelectionKey) it.next();
                    it.remove();
                    
                    if (selKey.isAcceptable()) {
                System.out.println("Is connected Accept");
                        ServerSocketChannel ssChannel = (ServerSocketChannel) selKey.channel();
                        SocketChannel sc = ssChannel.accept();
                        sc.configureBlocking(false);
                        sc.register(selector, SelectionKey.OP_READ | SelectionKey.OP_WRITE);
                        ByteBuffer bb = ByteBuffer.allocate(1024);
                        int read = sc.read(bb);
                        while ( read == 0 )
                            read = sc.read(bb);
                        
                        bb.rewind();
                        byte[] readBytes = new byte[read];
                        bb.get(readBytes, 0, readBytes.length);
                        String url = new String(readBytes);
                        System.out.println("Read "+url);
                        socketMap.put(sc, url);
                        
                    } else {
                        if (selKey.isReadable()) {
                            SocketChannel ssChannel = (SocketChannel) selKey.channel();
                            
                            ByteBuffer bb = ByteBuffer.allocate(1024);
                            if ( ssChannel.isOpen() ) {
                                ssChannel.read(bb);
                                System.out.println("read");
                                System.out.println(bb.toString());
                            }
                        }

                        // Check if the key is still valid, 
                        // since it might have been invalidated 
                        // in the read handler (for instance, 
                        // the socket might have been closed)
                        if (selKey.isValid() && selKey.isWritable()) {
                            System.out.println("write");
                            SocketChannel ssChannel = (SocketChannel) selKey.channel();
                            ssChannel.register(selector, SelectionKey.OP_READ);
                            ByteBuffer bb = ByteBuffer.allocate(1024);
                            String url = socketMap.get(ssChannel);
                            socketMap.remove(ssChannel);
                            
                            // Fire Thread that hashes and returns IBR
                            (new Thread(new ImageHasher(ssChannel, url))).start();
                        }
                    }
                }
            } catch (IOException ex) {
                Logger.getLogger(PHashServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        System.out.println("end");
    }

    public static void main(String args[]) {
        try {
            Selector sel = Selector.open();
            (new Thread(new PHashServer(sel))).start();          
        } catch (IOException ex) {
            Logger.getLogger(PHashServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
