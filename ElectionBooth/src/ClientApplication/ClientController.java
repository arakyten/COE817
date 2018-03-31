package ClientApplication;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;

/**
 *
 * @author Deon
 */
public final class ClientController {

    //Test
    public static void main(String[] args) {
        ClientController x = new ClientController();
    }

    
    private JFrame frame;
    private ClientGUI c_gui;
    private VotingGUI v_gui;
    public ClientController() {
        
        initRGUI();
        c_gui.resetGUI();
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
        String options[] = {"Option 1","Option 2","Option 3"};
        v_gui = new VotingGUI(buttonEvent,options);        
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
