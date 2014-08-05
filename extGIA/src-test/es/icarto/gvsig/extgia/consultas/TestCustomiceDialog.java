package es.icarto.gvsig.extgia.consultas;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class TestCustomiceDialog {

    public static void main(String args[]) {
	JFrame f = new JFrame("Dual List Box Tester");
	f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	CustomiceDialog dialog = new CustomiceDialog();
	dialog.addSourceElements(new String[] { "One", "Two", "Three" });
	dialog.addSourceElements(new String[] { "Four", "Five", "Six" });
	dialog.addSourceElements(new String[] { "Seven", "Eight", "Nine" });
	dialog.addSourceElements(new String[] { "Ten", "Eleven", "Twelve" });
	dialog.addSourceElements(new String[] { "Thirteen", "Fourteen",
		"Fifteen" });
	dialog.addSourceElements(new String[] { "Sixteen", "Seventeen",
		"Eighteen" });
	dialog.addSourceElements(new String[] { "Nineteen", "Twenty", "Thirty" });

	f.getContentPane().add(dialog, BorderLayout.CENTER);
	f.pack();
	f.setVisible(true);
    }

}
