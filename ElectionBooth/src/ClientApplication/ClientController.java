package ClientApplication;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.swing.JFrame;

/**
 *
 * @author Deon
 */
public final class ClientController {

    //Test
    public static void main(String[] args) {

        String hostName = "127.0.0.1";
        int CTFportNumber = 500;
        int CLAportNumber = 4500;

        try {
            ClientController x = new ClientController(hostName, CTFportNumber, CLAportNumber);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private JFrame frame;
    private ClientGUI c_gui;
    private VotingGUI v_gui;
    private PrintWriter out;
    private BufferedReader in;
    private Socket clientSocket;
    private String hostName, options[] = {"Donald_Trump", "Barack_Obama", "George_W._Bush"};
    private int CTFportNumber, CLAportNumber;
    private Cipher enc_cip;
    private Cipher dec_cip;

    public ClientController(String hostName, int CTFportNumber, int CLAportNumber) throws Exception {
        this.hostName = hostName;
        this.CTFportNumber = CTFportNumber;
        this.CLAportNumber = CLAportNumber;

        String encodedKey = "uQ2XE0ZKUcE=";
        //System.out.println("{" + encodedKey + "}");
        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
        SecretKey key = new SecretKeySpec(decodedKey, 0, decodedKey.length, "DES");
        //System.out.println(key);

        enc_cip = Cipher.getInstance("DES");
        dec_cip = Cipher.getInstance("DES");
        enc_cip.init(Cipher.ENCRYPT_MODE, key);
        dec_cip.init(Cipher.DECRYPT_MODE, key);
        initRGUI();
    }

    private void initRGUI() throws IOException {
        clientSocket = new Socket(hostName, CLAportNumber);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

        frame = new JFrame();
        ActionListener buttonEvent = (ActionEvent e) -> {
            register();
        };
        c_gui = new ClientGUI(buttonEvent);
        frame.add(c_gui);
        frame.setSize(new Dimension(350, 350));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
    }

    public void initVGUI() throws IOException {
        ActionListener buttonEvent = (ActionEvent e) -> {
            try {
                vote();
            } catch (IOException ex) {

            }
        };

        v_gui = new VotingGUI(buttonEvent, options);
        c_gui.setVisible(false);
        frame.add(v_gui);
        frame.setSize(new Dimension(350, 350));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        v_gui.resetGUI();
        v_gui.setVCfield(c_gui.getVerficationCode());
        System.out.println("GUI WORKING");
        try {

            clientSocket.close();
            out.close();
            in.close();
        } catch (Exception e) {
        }
        clientSocket = new Socket(hostName, CTFportNumber);
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        System.out.println("SOCKET WORKING");

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

    public void register() {
        System.out.println(c_gui.getFirstName());
        System.out.println(c_gui.getLastName());
        System.out.println(c_gui.getSIN());

        String msg = c_gui.getFirstName() + " "
                + c_gui.getLastName() + " "
                + c_gui.getSIN();
        //out.write(msg);
        //out.print(msg);
        
        try {
            //out.println(this.encrypt(msg));
            out.println(msg);
            System.out.println("Msg sent:" + msg);
            String vc;// = in.readLine();
            System.out.println("Waiting for vc...");
            //vc = in.readLine();
            while ((vc = in.readLine()) != null) {
                //vc = this.decrypt(vc);
                System.out.println(vc);
                break;
            }

            System.out.println("VC recieved:" + vc);
            c_gui.setVerficationCode(vc);

            out.close();
            in.close();
            clientSocket.close();
            System.out.println("Proceed to Voting");
            c_gui.setButtonText("Proceed to Vote");

            ActionListener buttonEvent = (ActionEvent e) -> {
                try {
                    proceedToVote();
                } catch (IOException ex) {

                }
            };
            c_gui.getButton().removeActionListener(c_gui.getButton().getActionListeners()[0]);
            c_gui.getButton().addActionListener(buttonEvent);
        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

    }

    public void resetGUI() throws Exception {
        frame.dispose();
        initRGUI();
    }

    public void proceedToVote() throws IOException {
        initVGUI();
    }

    public void vote() throws IOException {
        System.out.println("vote");
        System.out.println(v_gui.getVerification());
        System.out.println(options[v_gui.getVote()]);

        String msg = v_gui.getVerification() + " "
                + options[v_gui.getVote()];
        //out.write(msg);
        //out.print(msg);
        out.println(msg);

        System.out.println("Msg sent:" + msg);
        try {
            String vc = "";
            System.out.println("Waiting for vc...");
            while ((vc = in.readLine()) != null) {
                System.out.println(vc);
                break;
            }

            System.out.println("VC recieved:" + vc);
            v_gui.setVerficationCode(v_gui.getVerification());
            if (clientSocket.isClosed()) {
                out.close();
                in.close();
                clientSocket.close();
            }
            ActionListener buttonEvent = (ActionEvent e) -> {
                try {
                    resetGUI();
                } catch (Exception ex) {

                }
            };
            v_gui.getButton().removeActionListener(v_gui.getButton().getActionListeners()[0]);
            v_gui.getButton().addActionListener(buttonEvent);
            v_gui.setButtonText("Return to Registration");

        } catch (Exception e) {
            System.out.println(e);
            e.printStackTrace();
        }

        // frame.dispose();
        // initRGUI();
    }

}
