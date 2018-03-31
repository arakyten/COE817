package ClientApplication;
import CTFApplication.CTFGUI;
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
        CTFGUI.main(args);
    }

    ClientGUI gui;

    public ClientController() {
        initGUI();
        gui.resetGUI();
    }

    public void initGUI() {
        JFrame frame = new JFrame();
        ActionListener buttonEvent = (ActionEvent e) -> {
            register();
        };        
        gui = new ClientGUI(buttonEvent);
        frame.add(gui);
        frame.setSize(new Dimension(350, 350));
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        gui.resetGUI();
        

    }

    public void register() {
        System.out.println(gui.getFirstName());
        System.out.println(gui.getLastName());
        System.out.println(gui.getSIN());
    }

}
