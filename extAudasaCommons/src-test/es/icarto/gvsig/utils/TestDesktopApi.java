package es.icarto.gvsig.utils;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URI;
import java.net.URISyntaxException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class TestDesktopApi {

    public static void main(String[] args) {
	final JFrame mainFrame = new JFrame();
	JButton bt = new JButton("open google");
	bt.addActionListener(new ActionListener() {

	    @Override
	    public void actionPerformed(ActionEvent e) {
		try {
		    DesktopApi.browse(new URI("http://google.com"));
		} catch (URISyntaxException use) {
		    use.printStackTrace();
		}
	    }
	});
	mainFrame.add(bt);
	mainFrame.setSize(100, 100);
	mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	SwingUtilities.invokeLater(new Runnable() {
	    @Override
	    public void run() {
		mainFrame.setVisible(true);

	    }
	});
    }

}
