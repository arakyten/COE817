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
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Deon
 */
public class CentralLegitimizationAgency {

    Cipher enc_cip;
    Cipher dec_cip;
    private Socket socket = null;
    private ServerSocket server = null;
    private DataInputStream in = null;
    private DataOutputStream outStream = null;

    private static final File file = new File(System.getProperty("user.dir") + "\\src\\CLAApplication\\CLA.txt");
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

    public String encrypt(String encryptTxt) throws Exception {
        byte[] utf8 = encryptTxt.getBytes("UTF8");

        byte[] enc = enc_cip.doFinal(utf8);

        return new sun.misc.BASE64Encoder().encode(enc);
    }

    public String decrypt(String decryptTxt) throws Exception {
        byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(decryptTxt);

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
            lines.add(firstName + " " + lastName + " " + sinCard);
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

    private static String generateVerfication() {
        String x = UUID.randomUUID().toString();
        // System.out.println(x);
        return x;
    }

    public static void addToFile(String info) throws IOException {
        //String col[] = info.split(" ");        
        File file = new File(System.getProperty("user.dir") + "\\src\\CLAApplication\\CLA.txt");
        Scanner sc = new Scanner(file);
        ArrayList<String> lines = new ArrayList<>();
        while (sc.hasNext()) {
            lines.add(sc.nextLine());
        }
        sc.close();
        FileWriter writer = new FileWriter(file);
        for (String s : lines) {

            writer.write(s + "\n");
        }
        writer.write(info + "\n");
        writer.close();
    }
  

    public static void main(String[] args) {
        int portNumber = 4500;
        try {
            //SecretKey key = KeyGenerator.getInstance("DES").generateKey();
            //System.out.println(key);            
            //String encodedKey = Base64.getEncoder().encodeToString(key.getEncoded());            
            String encodedKey = "uQ2XE0ZKUcE=";
            System.out.println("{"+encodedKey+"}");            
            byte[] decodedKey = Base64.getDecoder().decode(encodedKey);            
            SecretKey originalKey = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES"); 
            System.out.println(originalKey);
            
            CentralLegitimizationAgency x = new CentralLegitimizationAgency(originalKey);

            while (true) {
                ServerSocket serverSocket = null;
                Socket clientSocket = null;
                PrintWriter out = null;
                BufferedReader in = null;
                try {
                    serverSocket = new ServerSocket(portNumber);
                    System.out.println("Waiting for connection ... ");
                    clientSocket = serverSocket.accept();
                    System.out.println("Connection established ...");
                    out = new PrintWriter(clientSocket.getOutputStream(), true);
                    in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

                    String inputLine;
                    System.out.println("Waiting for msg");
                    //inputLine = in.readLine();
                    //while ((inputLine = in.readLine()) != null) {                        
                    while (true) {
                        inputLine = in.readLine();
                        if (inputLine != null) {
                            //inputLine = x.decrypt(inputLine);
                            System.out.println("Msg recieved :" + inputLine);
                            String vc = generateVerfication();
                            CentralLegitimizationAgency.addToFile(inputLine + " " + vc);                            
                            //out.println(x.encrypt(vc));
                            out.println(vc);
                            System.out.println("vc sent:" + vc);
                            break;
                        } else {
                            System.out.println("null");
                        }
                    }

                    //while (!clientSocket.isClosed());
                    System.out.println("Jobs Done.\n");
                    out.close();
                    in.close();
                    clientSocket.close();
                    serverSocket.close();
                    System.out.println("Jobs Done.\n");

                } catch (Exception e) {
                    out.close();
                    in.close();
                    clientSocket.close();
                    serverSocket.close();
                    System.out.println("Connection lost ... closing connection");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
