package net.schoener.peter.hmr;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class OptionPanelElement extends JPanel
{
	// each frame contains a pair of coordinates and a "remove" button
	private JTextField xBox, yBox;
	private JButton rmBtn;
	
	// for click handler
	Component thisPanel = this;

	public OptionPanelElement()
	{
		// initialize elements
		setLayout(new BorderLayout());
		xBox = new JTextField();
		yBox = new JTextField();
		rmBtn = new JButton("remove");
		
		// do text field sizing things
		this.setMaximumSize(new Dimension(Integer.MAX_VALUE, rmBtn.getPreferredSize().height + xBox.getPreferredSize().height + 3));
		
		// make the text fields appear next to each other
		JPanel coordPanel = new JPanel();
		coordPanel.setLayout(new GridLayout(1, 2));
		coordPanel.add(xBox);
		coordPanel.add(yBox);
		
		// add the components to the element
		add(coordPanel, BorderLayout.CENTER);
		add(rmBtn, BorderLayout.SOUTH);
		
		// when the "remove" button is clicked, remove the element from its parent
		rmBtn.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e)
			{
				Container parent = thisPanel.getParent();
				parent.remove(thisPanel);
				parent.revalidate();
				parent.repaint();
			}
		});
	}
}
