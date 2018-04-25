package ClientApplication;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import javax.swing.JFrame;

/**
 *
 * @author Deon
 */
public final class ClientController {

    //Test
    public static void main(String[] args) {
        // ClientController x = new ClientController();        
        String hostName = "127.0.0.1.";
        int portNumber = 500;
        try {
            Socket clientSocket = new Socket(hostName, portNumber);
            PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            
            out.write("hello");
            out.flush();
            
            out.close();
            in.close();
            clientSocket.close();
            
        } catch (Exception e) 
        {
            e.printStackTrace();
        }
    }

    private JFrame frame;
    private ClientGUI c_gui;
    private VotingGUI v_gui;

    public ClientController() {

        initRGUI();

    }

    private void initRGUI() {
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

    public void initVGUI() {
        ActionListener buttonEvent = (ActionEvent e) -> {
            vote();
        };
        String options[] = {"Donald Trump", "Barack Obama", "George W. Bush"};
        v_gui = new VotingGUI(buttonEvent, options);
        c_gui.setVisible(false);
        frame.add(v_gui);
        frame.setSize(new Dimension(350, 350));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        v_gui.resetGUI();
    }

    public void register() {
        System.out.println(c_gui.getFirstName());
        System.out.println(c_gui.getLastName());
        System.out.println(c_gui.getSIN());
        initVGUI();

    }

    public void vote() {
        System.out.println("vote");
        frame.dispose();
        initRGUI();
    }

}
