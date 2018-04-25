/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CTFApplication;

import java.awt.Dimension;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.UUID;

import javax.swing.JFrame;

/**
 *
 * @author Deon
 */
public class CentralTabulatingFacility {

    private static String c[] = {"a", "b", "c"};
    private final String CANDIADATES[];
    private final ArrayList<Integer> votes;
    private CTFGUI gui;

    CentralTabulatingFacility(String candiadates[]) {
        this.CANDIADATES = candiadates;
        this.votes = new ArrayList<>();
        //Intialize votes too 0
        for (String c : candiadates) {
            votes.add(0);
        }
        this.initGUI();
    }

    public void vote(String verificationCode, String selection) throws Exception {
        boolean valid = validateCode(verificationCode);
        boolean hasVoted = checkVoteStatus(verificationCode);
        // System.out.println(hasVoted);
        if (valid && !hasVoted) {
            int index = getCandiadateIndex(selection);
            if (index < votes.size()) {
                int voteCount = votes.get(index) + 1;
                votes.remove(index); // ?
                votes.add(index, voteCount);
                /*for(int x :votes)    //test            
                    System.out.print(x);                
                System.out.println();*/
                addToList(verificationCode);
                System.out.println(verificationCode + " has succesfully voted");
            } else {
                throw new Exception("No such candiadate");
            }
        } else {
            if (hasVoted) {
                System.out.println(verificationCode + " has already voted");
            } else {
                throw new Exception("Invalid verfication code");
            }
        }
    }

    private boolean validateCode(String verificationCode) throws Exception {
        //Recover valid codes from text file           
        File file = new File(System.getProperty("user.dir") + "\\src\\CLAApplication\\CLA.txt");
        Scanner s = new Scanner(file);
        ArrayList<String[]> lines = new ArrayList<>();
        while (s.hasNext()) {
            String fname = s.next();
            String lname = s.next();
            String sin_N = s.next();
            String vCode = s.next();
            //System.out.println(fname + " " + lname + " " + sin_N + " " + vCode);
            String tmp[] = {fname, lname, sin_N, vCode};
            lines.add(tmp);
        }
        s.close();
        //Cross reference
        boolean valid = false;
        for (String l[] : lines) {
            //System.out.println(l[3] + " =? " + verificationCode+" " +l[3].equals(verificationCode));            
            if (l[3].equals(verificationCode)) {
                valid = true;
                break;
            }
        }
        return valid;
    }

    private boolean checkVoteStatus(String verificationCode) throws Exception {
        File file = new File(System.getProperty("user.dir") + "\\src\\CTFApplication\\CTF.txt");
        Scanner s = new Scanner(file);
        ArrayList<String> vcs = new ArrayList<>();
        //GetContents of file
        // System.out.println(verificationCode + ">>");
        while (s.hasNext()) {
            String v = s.nextLine();
            vcs.add(v);
            // System.out.println(v);
        }
        //check contents
        return vcs.contains(verificationCode);
    }

    private void addToList(String verificationCode) throws Exception {
        File file = new File(System.getProperty("user.dir") + "\\src\\CTFApplication\\CTF.txt");
        Scanner sc = new Scanner(file);
        ArrayList<String> x = new ArrayList<>();
        while (sc.hasNext()) {
            x.add(sc.nextLine());
        }
        sc.close();
        FileWriter writer = new FileWriter(file);

        for (String s : x) {
            writer.write(s + "\n");
        }
        writer.write(verificationCode + "\n");

        writer.close();
    }

    private int getCandiadateIndex(String selection) {
        int index = -1;
        for (int i = 0; i < CANDIADATES.length; i++) {
            if (CANDIADATES[i].equals(selection)) {
                index = i;
                return index;
            }
        }
        return index;
    }

    private void printVotes() {
        System.out.println("Vote count for " + CANDIADATES[0] + ": " + votes.get(0));
        System.out.println("Vote count for " + CANDIADATES[1] + ": " + votes.get(1));
        System.out.println("Vote count for " + CANDIADATES[2] + ": " + votes.get(2));

        gui.setMsg1("Vote count for " + CANDIADATES[0] + ": " + votes.get(0));
        gui.setMsg2("Vote count for " + CANDIADATES[1] + ": " + votes.get(1));
        gui.setMsg3("Vote count for " + CANDIADATES[2] + ": " + votes.get(2));

    }

    public static void main(String[] args) {
        CentralTabulatingFacility x = new CentralTabulatingFacility(c);
        try {
            x.vote("v1", "a");
            x.vote("v2", "b");
            x.vote("v3", "b");
            x.vote("v4", "c");
            x.vote("v5", "b");

            x.printVotes();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void maain(String[] args) {
        int portNumber = 500;
        try {
            ServerSocket serverSocket = new ServerSocket(portNumber);
            Socket clientSocket = serverSocket.accept();
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            //Read from Socket get number of candiadates
            //---
            //CentralTabulatingFacility x = new CentralTabulatingFacility(c);
            //
            
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                System.out.println(inputLine);                
                break;
            }

            out.close();
            in.close();
            clientSocket.close();
            serverSocket.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initGUI() {
        gui = new CTFGUI();
        gui.setSize(new Dimension(350, 350));
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setLocation(350, 0);
        gui.setVisible(true);
    }
}
