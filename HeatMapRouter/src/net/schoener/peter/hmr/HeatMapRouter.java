package net.schoener.peter.hmr;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;

public class HeatMapRouter
{
	public static final int HEIGHT = 700,
			WIDTH = 1000;
	
	public static void main(String[] args)
	{
		// define main window
		JFrame hmr = new JFrame("Heat Map Router");
		try
		{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		}
		catch (Exception e)
		{
			JOptionPane.showMessageDialog(hmr, "Error setting look and feel.", "shh bby is ok", JOptionPane.ERROR_MESSAGE);
		}
		hmr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// set window dimensions and display
		hmr.setSize(WIDTH, HEIGHT);
		hmr.setLocationRelativeTo(null);
		
		// make internal panels
		hmr.setLayout(new GridLayout(1, 2));
		JPanel mapPanel = new JPanel();
		JPanel optnPanel = new JPanel();
		JScrollPane optnScrollPane = new JScrollPane(optnPanel);
		JPanel leftContainer = new JPanel();
		JPanel rightContainer = new JPanel();
		
		// make left
		JButton nOptnBtn = new JButton("new coordinate pair");
		leftContainer.setLayout(new BorderLayout());
		leftContainer.add(nOptnBtn, BorderLayout.SOUTH);
		nOptnBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				optnPanel.add(new OptionPanelElement());
				optnPanel.revalidate();
				optnPanel.repaint();
			}
		});
		
		// make right
		JButton fPickerBtn = new JButton("select map file");
		rightContainer.setLayout(new BorderLayout());
		rightContainer.add(fPickerBtn, BorderLayout.SOUTH);
		
		// make option pane
		optnPanel.setLayout(new BoxLayout(optnPanel, BoxLayout.Y_AXIS));
		optnPanel.add(new OptionPanelElement()); // initialize with one element
		leftContainer.add(optnScrollPane, BorderLayout.CENTER);
		
		// make map pane
		mapPanel.setLayout(new BorderLayout());
		rightContainer.add(mapPanel, BorderLayout.CENTER);
		
		// add panes to main window
		hmr.add(leftContainer);
		hmr.add(rightContainer);
	}
}
