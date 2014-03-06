package com.gwu.architecture;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class SwitchPanel extends JPanel {

	class switchThread extends Thread {		//thread class used for exchange information between computer and gui
		public void run() {
			synchronized (SwitchPanel.class) {
				while(threadFlag==1);		//lock, serialize the thread
				threadFlag=1;
				gwuComputer.step();
				gwuGui.refresh();
				threadFlag=0;
			}
		}
	}
	
	private static final long serialVersionUID = 1L;

	Computer gwuComputer;
	GUI gwuGui;
	GridBagConstraints constraints;
	GridBagLayout layout;
	JButton[] button;
	ImageIcon[] icon;
	JRadioButton[] radioButton;
	// claim a flag used to indicate if the computer is powerup or powerdown
	int flag;
	switchThread thread;
	int threadFlag;

	// SwitchPanel generator function
	public SwitchPanel(GUI gui) {
		gwuGui = gui;
		layout = new GridBagLayout();
		constraints = new GridBagConstraints();
		// set the icon for start button
		icon = new ImageIcon[2];
		icon[0] = new ImageIcon(getClass().getResource("start0.jpg"));
		icon[1] = new ImageIcon(getClass().getResource("start1.jpg"));
		//icon[0] = new ImageIcon("start0.jpg");
		//icon[1] = new ImageIcon("start1.jpg");

		// button object claim
		button = new JButton[6];
		button[0] = new JButton("step");
		button[1] = new JButton("run");
		button[2] = new JButton("stop");

		radioButton = new JRadioButton[6];
		radioButton[4] = new JRadioButton(icon[0]);

		flag = 0;

	}

	// init function of SwitchPanel
	public void init(Computer computer) {
		
		gwuComputer = computer;
		
		/***********************init swithc panel's ui******************************/
		setBorder(BorderFactory.createEtchedBorder());
		setLayout(layout);
		constraints.ipadx = 25;
		constraints.ipady = 25;
		constraints.insets = new Insets(20, 20, 20, 20);
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.anchor = GridBagConstraints.CENTER;
		add(button[0], constraints);
		add(button[1], constraints);
		add(button[2], constraints);
		add(radioButton[4], constraints);
		/***********************init swithc panel's ui******************************/
		
		//when step was pressed
		button[0].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				thread = new switchThread();
				thread.start();
			}
		});
		
		button[1].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				gwuGui.gwuConsolePanel.append("(Button)run: codes are waited to be added!\n");
			}
		});
		
		button[2].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				gwuGui.gwuConsolePanel.append("(Button)stop: codes are waited to be added!\n");
			}
		});
		
		
		// ActionListener of radioButton, which implements the action of
		// computer powerup and powerdowm
		radioButton[4].addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if (flag == 0) {
					gwuComputer.powerup();
					gwuGui.powerup();
				} else {
					gwuComputer.powerdown();
					gwuGui.powerdown();
				}
				radioButton[4].setIcon(icon[flag]);
			}
		});

	}

	// powerup function
	public void powerup() {
		flag = 1;
		threadFlag=0;
	}

	// powerdown function
	public void powerdown() {
		flag = 0;
	}

	// degenerator of SwithcPanel
	public void destroy() {

	}

	// refresh function
	public void refresh() {
		if (flag == 0) {

		} else {

		}
	}

}
