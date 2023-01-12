package com.sm.netty.bio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.lang.management.RuntimeMXBean;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;

/**
 * @description:
 * @author: cyx
 * @date: 2023-01-12
 **/
public class BioDemo {

    public static void main(String[] args)throws Exception  {
        int port = 8080;
        ServerSocket serverSocket = null;
        try{
            while (true){
                serverSocket = new ServerSocket(port);
                System.out.println("start server in"+port);
                Socket socket = serverSocket.accept();
                new Thread(new SocketHandle(socket)).start();
            }
        }finally {
            if (serverSocket != null){
                serverSocket.close();
            }
        }
    }


    public static class SocketHandle implements Runnable {

        private Socket socket;

        public SocketHandle(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            if (socket == null){
                return;
            }
            BufferedReader reader = null;
            PrintWriter write = null;
            try {
                InputStream in = socket.getInputStream();
                 reader = new BufferedReader(new InputStreamReader(in));
                OutputStream out = socket.getOutputStream();
                 write = new PrintWriter(socket.getOutputStream(), true);
                while (true){
                    //读取数据
                    String str = reader.readLine();
                    if ("exit".equals(str)){
                        break;
                    }
                    System.out.println("recive the message"+ str);
                    Date date = new Date();
                    System.out.println("this the time is"+ date);
                    write.println("server :"+str);
                }

            } catch (IOException e) {
                if (reader != null){
                    try {
                        reader.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
                if (write != null){
                    write.close();
                }
                if (socket != null){
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }

        }
    }

}
