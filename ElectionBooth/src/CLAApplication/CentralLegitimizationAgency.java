/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CLAApplication;
import java.security.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.*;
import javax.crypto.*;
import java.net.*;
/**
 *
 * @author Deon
 */
public class CentralLegitimizationAgency 
{
	Cipher enc_cip;

	  Cipher dec_cip;
	  private Socket          socket   = null;
	    private ServerSocket    server   = null;
	    private DataInputStream in       =  null;
	    private DataOutputStream outStream       =  null;
	    
	  private static final File file = new File("CLA.txt");
	  private static final File vc = new File("vc.txt");
	    private static FileWriter writer;
	    private static Scanner s;
	    
	    private static final ArrayList<String> voters = new ArrayList<>();
	    private static final ArrayList<String> vcLog = new ArrayList<>();
	    private static final ArrayList<String> names = new ArrayList<>();
	    private static final ArrayList<Integer> sin = new ArrayList<>();
	    
	    private static String firstName, lastName, sinID;

		private static Integer sinCard;
	    
	    CentralLegitimizationAgency(SecretKey key) throws Exception {
	    enc_cip = Cipher.getInstance("DES");
	    dec_cip = Cipher.getInstance("DES");
	    enc_cip.init(Cipher.ENCRYPT_MODE, key);
	    dec_cip.init(Cipher.DECRYPT_MODE, key);
	  }
	    
	    public String encrypt (String encryptTxt)throws Exception{
	        byte[] utf8 = encryptTxt.getBytes("UTF8");
	        
	        byte[] enc = enc_cip.doFinal(utf8);
	      
	        return new sun.misc.BASE64Encoder().encode(enc);
	    }
	    
	    public String decrypt (String decryptTxt) throws Exception{
	        byte [] dec = new sun.misc.BASE64Decoder().decodeBuffer(decryptTxt);
	        
	        byte[] utf8 = dec_cip.doFinal(dec);
	        
	        return new String(utf8, "UTF8");
	    }
	    
	    public static void returnLine() throws FileNotFoundException {

	        s = new Scanner(file);
	        ArrayList<String> lines = new ArrayList<>();

	        while (s.hasNext()) {
	            try {
	                firstName = s.next();
	                lastName = s.next();
	                sinID = s.next();
	            } catch (Exception e) {
	                break;
	            }
	            if (isInt(sinID)) {
	                sinCard = Integer.valueOf(sinID);
	            }
	           

	            names.add(firstName);
	            sin.add(sinCard);
	            lines.add(firstName+" "+lastName+" "+sinCard);
	       }
	        voters.addAll(lines);
	    
	    }
	    
	    private static boolean isInt(String s) {
	        try {
	            int d = Integer.valueOf(s);
	            return true;
	        } catch (NumberFormatException e) {
	            return false;
	        }

	    }
	    
	    private static String generateVerfication()
	    {
	        String x= UUID.randomUUID().toString();
	       // System.out.println(x);
	        return x;
	    }
	    
	    public static void addToFile(String verNum) throws IOException {
	        vcLog.add(verNum);
	        
	        writer = new FileWriter(vc);

	       for (String s : vcLog) {

	            writer.write(s + "\r\n");
	        }
	        writer.close();
	    }
	    
	    public CentralLegitimizationAgency(int port)
	    {
	        
	        // starts server and waits for a connection
	        try
	        {
	            server = new ServerSocket(port);
	            socket = server.accept();
	             
	            // takes input from the client socket
	            in = new DataInputStream(socket.getInputStream());
	            
	            outStream = new DataOutputStream(socket.getOutputStream());
	         
	            
	                try
	                {
	                     
	                	System.out.println("Sent Text of Message 1: " + verNum);
	                	outStream.writeUTF(verNum);
	 
	                }
	                catch(IOException i)
	                {
	                    System.out.println(i);
	                }
	            
	           // System.out.println("Closing connection");
	 
	            // close connection
	            socket.close();
	            in.close();
	            outStream.close();
	            server.close();
	        }
	        catch(IOException i)
	        {
	            System.out.println(i);
	        }
	    }
	 
	    
	    public static void main(String[] args) throws Exception {
	        returnLine();
	        SecretKey key = KeyGenerator.getInstance("DES").generateKey();
	        CentralLegitimizationAgency encrypter = new CentralLegitimizationAgency(key);
	        
	        for(String str:voters)
	        {
	            System.out.println(str.toString());
	            
	            String vc = generateVerfication();
	           // String encVC = encrypter.encrypt(vc);
	            addToFile(vc); 
	            System.out.println(vc);
	           // System.out.println(encVC);
			       
	        }

	    }
    
    
}
