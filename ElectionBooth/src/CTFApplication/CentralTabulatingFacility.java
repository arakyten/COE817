/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CTFApplication;

import java.awt.Dimension;

import javax.swing.JFrame;

/**
 *
 * @author Deon
 */
public class CentralTabulatingFacility {

    public static void main(String[] args) {
        new CentralTabulatingFacility().initGUI();
    }

    public void initGUI() {
        CTFGUI gui = new CTFGUI();
        gui.setSize(new Dimension(350, 350));
        gui.setResizable(false);
        gui.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gui.setLocation(350, 0);

        gui.setVisible(true);

    }
}
