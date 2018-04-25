/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package readfromfile;

import java.io.*;
import java.text.DecimalFormat;
import java.util.*;

/**
 *
 * @author Deon
 */
public class EditFile {

    /**
     * @param args the command line arguments
     */
    private static final File file = new File("test.txt");
    private static FileWriter writer;

    private static boolean firstInstance = false;
    private static DecimalFormat dec = new DecimalFormat("0.00");

    private static Scanner s;

    private static final ArrayList<String> customers = new ArrayList<>();
    private static final ArrayList<String> names = new ArrayList<>();
    private static final ArrayList<String> pws = new ArrayList<>();
    private static final ArrayList<Double> checks = new ArrayList<>();
    private static final ArrayList<Double> saves = new ArrayList<>();

    private static String name, pw, checking, savings;
    private static double check = -1, save = -1;

    public static void addAdmin()
    {
        names.add(0, "Admin");
        pws.add(0,"Admin");
        checks.add(0,0.0);
        saves.add(0,0.0);
        
        customers.add(names.get(0)+" "+pws.get(0)+" "+ checks.get(0)+" "+saves.get(0));
       // System.out.println(customers.get(0));
        
    }
    //reads from file to add to customer lists
    public static void returnLine() throws FileNotFoundException {

        s = new Scanner(file);
        ArrayList<String> lines = new ArrayList<>();

        while (s.hasNext()) {
            try {
                name = s.next();
                pw = s.next();
                checking = s.next();
                savings = s.next();
            } catch (Exception e) {
                break;
            }
            if (isDouble(checking)) {
                check = Double.valueOf(checking);
            }
            if (isDouble(savings)) {
                save = Double.valueOf(savings);
            }

            names.add(name);
            pws.add(pw);
            checks.add(check);
            saves.add(save);
            lines.add(name+" "+pw+" "+check+" "+save);
       }
        customers.addAll(lines);
        
    }

    private static boolean isDouble(String s) {
        try {
            double d = Double.valueOf(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }

    }

    private static void openFile() throws IOException {
        file.delete();
        if (!file.exists()) {
            file.createNewFile();
        }
        writer = new FileWriter(file);
       
    }

    public static void addToFile(String user) throws IOException {
        customers.add(user);
        writer.close();
        writer = new FileWriter(file);

        for (String s : customers) {

            writer.write(s + "\n");
        }

    }

    public static void main(String[] args) throws IOException {

        addAdmin();
        returnLine();
        for(String str:customers)
        {
            System.out.println(str.toString());
        }

    }

}
