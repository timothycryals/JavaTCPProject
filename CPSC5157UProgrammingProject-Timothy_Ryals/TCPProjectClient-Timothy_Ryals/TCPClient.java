//Author: Timothy Ryals
//Name: TCPClient

import java.io.*;
import java.net.*;

public class TCPClient
{
   public static void main(String argv[]) throws Exception{
       File myFile = new File("Test1 - large.txt");
       
       System.out.println("I am connecting to server side: 192.168.0.7");
       
       long tStart = System.currentTimeMillis();;
       long tEnd;
       long tDelta;
       
       long tTotal = 0;
       
       Socket clientSocket = new Socket("192.168.0.7", 5555);
       //Socket clientSocket = new Socket("168.27.165.176", 5555);
       //InputStream is = clientSocket.getInputStream();
       
       OutputStream outServer = clientSocket.getOutputStream();
       //byte[] byteArray = new byte[(int) myFile.length()];
       FileInputStream fis = new FileInputStream(myFile);
       BufferedInputStream bis = new BufferedInputStream(fis);
       
       byte[] contents;
       long fileLength;
       long current;
       
       
       
       int count = 1;
       
       while (count < 101){
           contents = null;
           fileLength = myFile.length();
           current = 0;
           System.out.println("I am sending file testFile for the " + Integer.toString(count) + "th time.");
           
           while (current != fileLength){
               int size = 10000;
               if (fileLength - current >= size)
                    current += size;
               else{
                   size = (int) (fileLength - current);
                   current = fileLength;
                }
                contents = new byte[size];
                bis.read(contents, 0, size);
                outServer.write(contents);
                //System.out.println("Sending file ... "+(current*100)/fileLength+"% complete!");
            }
            
            System.out.println("I am finishing sending file testFile for the " + Integer.toString(count) + "th time.");
            
            outServer.flush(); //Force any remaining bytes in the stream to their destination
            
                                
            clientSocket.close(); //Close the socket after each iteration
            
            tEnd = System.currentTimeMillis();
            tDelta = tEnd - tStart;
            tTotal += tDelta;
            
            System.out.println("The time used in milliseconds to send file testFile for " + Integer.toString(count) 
                                + "th time is: " + Long.toString(tDelta) + "\n");
                                
            tStart = System.currentTimeMillis();
            
            clientSocket = new Socket("192.168.0.7", 5555); //Create new socket connection to the server
            
            fis = new FileInputStream(myFile);
            bis = new BufferedInputStream(fis);
           
            outServer = clientSocket.getOutputStream();
            
            count++;
       }
        
       float avgTime = (float)tTotal / 100f;
       
       System.out.println("The average time to send file testFile in milliseconds is: " + Float.toString(avgTime));
       
       bis.close();
       fis.close();
       outServer.close();
       clientSocket.close();
       
       System.out.println("I am done.");
   
    }
}
