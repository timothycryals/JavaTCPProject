//Author: Timothy Ryals
//Name: TCPServer

import java.io.*;
import java.net.*;

public class TCPServer
{
    public static boolean CompareFiles(File f1, File f2) throws Exception{
        //This method will read through two files line by line to determine if they match.
        
        FileInputStream fstream1 = new FileInputStream(f1);
        FileInputStream fstream2 = new FileInputStream(f2);
        
        DataInputStream in1 = new DataInputStream(fstream1);
        DataInputStream in2 = new DataInputStream(fstream2);
        
        BufferedReader br1 = new BufferedReader(new InputStreamReader(in1));
        BufferedReader br2 = new BufferedReader(new InputStreamReader(in2));
        
        String strLine1, strLine2;
        
        while((strLine1 = br1.readLine()) != null && (strLine2 = br2.readLine()) != null){
            if(strLine1.equals(strLine2)){
                //If the lines match, continue to the next line
                continue;
            }
            else{
                //If files do not match, return false
                return false;
            }
            
        }
        //If files match, return true
        return true;
    }
    
    public static void main (String argv[]) throws Exception
    {
        
        
        ServerSocket sock = new ServerSocket(5555);
        
        File sampleFile = new File("SampleFile.txt");
        
        System.out.println("I am waiting for a client connection...");
        
        Socket clientSocket = sock.accept();
        
        System.out.println("Client Connected");
        System.out.println("I am ready for any client side request\n");
        
        int count = 1;
        long totalTime = 0;
        int incorrectTransfers = 0;
        
        byte[] contents = new byte[10000];
        
        //Initialize variables for FileOutputStream, BufferedOutputStream, and InputStream
        FileOutputStream fos;
        BufferedOutputStream bos;
        InputStream is;
        
        int bytesRead = 0;
        
        while (count < 101)
        {
            long tStart = System.currentTimeMillis(); //Start timer
            fos = new FileOutputStream("testFile" + Integer.toString(count) + ".txt");
            bos = new BufferedOutputStream(fos);
            is = clientSocket.getInputStream();
            
            System.out.println("I am starting receiving file testFile for the " + Integer.toString(count) + "th time.");
            
            
            
            while((bytesRead=is.read(contents)) != -1)
                bos.write(contents, 0, bytesRead);
                
            System.out.println("I am finishing receiving file testFile for the " + Integer.toString(count) + "th time.");    
            
            bos.flush(); //Force any remaining bytes in the BufferedOutputStream to their destination.
            
            long tEnd = System.currentTimeMillis(); //End the timer
            long tDelta = tEnd - tStart;
            totalTime += tDelta;
            
            System.out.println("The time used in milliseconds to receive file testFile for " + Integer.toString(count) 
                                + "th time is: " + Long.toString(tDelta) + "\n");
                                
            bytesRead = 0;
            
            File newFile = new File("testFile" + Integer.toString(count) + ".txt");
            
            boolean filesMatch = CompareFiles(sampleFile, newFile);
            if(filesMatch == false)
                incorrectTransfers += 1;
                
            clientSocket = sock.accept(); //Accept new client socket connection
            
            count++;
        }
        
        sock.close();
       
        float avgTime = (float)totalTime / 100f;
        
        System.out.println("The number of incorrect file transfers is " + incorrectTransfers + "\n");
        
        System.out.println("The average time to receive file testFile in milliseconds is: " + Float.toString(avgTime));
        
        System.out.println("I am done.");
    }
}
