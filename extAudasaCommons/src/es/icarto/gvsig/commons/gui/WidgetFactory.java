package es.icarto.gvsig.commons.gui;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

public class WidgetFactory {

    private WidgetFactory() {
	throw new AssertionError("Only static methods");
    }

    public static Border borderTitled(String title) {
	TitledBorder border = BorderFactory.createTitledBorder(title);
	border.setTitleColor(new Color(0, 60, 140));
	// default for title border
	// javax.swing.plaf.FontUIResource[family=Dialog,name=Dialog,style=bold,size=12]
	Font font = new Font("Arial", Font.BOLD, 12);
	border.setTitleFont(font);

	return border;
    }

    public static JLabel labelTitled(String text) {
	JLabel label = new JLabel(text);
	Font font = new Font("Arial", Font.BOLD, 11);
	label.setFont(font);
	label.setForeground(new Color(0, 60, 140));
	return label;
    }
}
