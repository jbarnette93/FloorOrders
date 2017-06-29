/*********************************************************************** 
Program Name: FloorOrders.java 
Programmer's Name: Jonathan Barnette
Program Description: Allows the user to select flooring type and dimensions, and displays and saves total cost
***********************************************************************/

import java.awt.*; //allows use of JFrame
import javax.swing.*; //allows use of JTextFields, JRadioButtons, etc. 
import java.util.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.*;
import javax.swing.*;
import java.util.*;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.sql.*;

public class FloorOrders extends JFrame {

	//name variables so they can be used in the constructors and private classes
	private final JTextField txtName = new JTextField(20); 
	private final JTextField txtAddress = new JTextField(20);
	private final JTextField txtLength = new JTextField(20);
	private final JTextField txtWidth = new JTextField(20);
	private final JRadioButton woodButton = new JRadioButton("Wood ($20/sq ft)", true);
	private final JRadioButton carpetButton = new JRadioButton("Carpet ($10/sq ft)", false);
	private final ButtonGroup radioGroup = new ButtonGroup();
	private final JTextArea txtArea = new JTextArea();
	private final JTextArea txtAreaDisplay = new JTextArea();
	private String name;
	private String address;
	private double area;
	private double cost;
	private final JTabbedPane tab = new JTabbedPane();
	private static Statement st = null;
	private static Connection cnn = null;
	
	FloorOrders() //constructor
	{
		super("Floor Orders");
		
		
		// constructing the first panel
		JLabel l1 = new JLabel("Customer Name:");
		JLabel l2 = new JLabel("Customer Address:");
		JPanel p2 = new JPanel();
		p2.setLayout(new GridLayout(2,2));
		p2.add(l1);p2.add(txtName);p2.add(l2);p2.add(txtAddress);
		tab.addTab("Customer",null, p2,"Panel #1");
		
		
		// constructing the second panel	
		JLabel l3 =new JLabel("Select Floor Type:");
		radioGroup.add(woodButton);
		radioGroup.add(carpetButton);
		JPanel p4 =new JPanel();
		p4.setLayout(new GridLayout(3, 1));
		p4.add(l3); p4.add(woodButton); p4.add(carpetButton);
		tab.addTab("Flooring Type",null, p4," Panel #2");
		
		// constructing the third panel
		JLabel l4 = new JLabel("Length:");
		JLabel l5 = new JLabel("Width:");
		JPanel p6 = new JPanel();
		p6.setLayout(new GridLayout(2,2));
		p6.add(l4);p6.add(txtLength);p6.add(l5);p6.add(txtWidth);
		tab.addTab("Dimensions",null, p6,"Panel #3");
		
		//constructing the fourth panel
		JButton btnCalculateArea = new JButton("1) Calculate Area");
		JButton btnCalculateCost = new JButton("2) Calculate Cost");
		JButton btnDisplaySummary = new JButton("3) Display Summary");
		JButton btnSubmitOrder = new JButton("4) Submit Order");
		JButton btnDisplayOrderList = new JButton("Display Order List");
		JPanel p8 = new JPanel();
		p8.setLayout(new GridLayout(5,1));
		p8.add(btnCalculateArea);p8.add(btnCalculateCost);p8.add(btnDisplaySummary);p8.add(btnSubmitOrder);p8.add(btnDisplayOrderList);
		tab.addTab("Submit",null, p8," Panel #4");
		
		//button handler
		ButtonHandlerCalculateArea handlerCalculateArea = new ButtonHandlerCalculateArea();
		btnCalculateArea.addActionListener(handlerCalculateArea);
		
		ButtonHandlerCalculateCost handlerCalculateCost = new ButtonHandlerCalculateCost();
		btnCalculateCost.addActionListener(handlerCalculateCost);
		
		ButtonHandlerDisplaySummary handlerDisplaySummary = new ButtonHandlerDisplaySummary();
		btnDisplaySummary.addActionListener(handlerDisplaySummary);
		
		ButtonHandlerSubmitOrder handlerSubmitOrder = new ButtonHandlerSubmitOrder();
		btnSubmitOrder.addActionListener(handlerSubmitOrder);
		
		ButtonHandlerDisplayOrderList handlerDisplayOrderList = new ButtonHandlerDisplayOrderList();
		btnDisplayOrderList.addActionListener(handlerDisplayOrderList);
		
		
		// add JTabbedPane to container
		getContentPane().add( tab );
		setSize(450,250);
		setVisible(true);
		
	}
	
	private class ButtonHandlerCalculateArea implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			if(Double.parseDouble(txtLength.getText()) > 0 && Double.parseDouble(txtLength.getText()) < 10000000 && Double.parseDouble(txtWidth.getText()) > 0 && Double.parseDouble(txtWidth.getText()) < 1000000000)
			{
				area = Double.parseDouble(txtLength.getText()) * Double.parseDouble(txtWidth.getText());
				JOptionPane.showMessageDialog(FloorOrders.this, "Area Calculated: " +area);
			}
			else
				JOptionPane.showMessageDialog(FloorOrders.this, "Entered invalid numbers! Go back to the Flooring Dimensions tab and enter numberical values greater than 1 for width and length.");	
		}
	}
	
	private class ButtonHandlerCalculateCost implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			if(area >= 1)
			{
				double flooringCost = 0;
				if(woodButton.isSelected())
					{
						flooringCost = 20;
					}
				else
				{
					flooringCost = 10;
				}
				cost = area * flooringCost;
				JOptionPane.showMessageDialog(FloorOrders.this, "Cost Calculated: " +cost);
			}
			else
				JOptionPane.showMessageDialog(FloorOrders.this, "Error! Area must be calcuated first. Click button 1 to calculate area.");
		}
	}
	
	private class ButtonHandlerDisplaySummary implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			if(area>=1 && cost >=1)
			{
				String output = "";
				output += "Customer Name: " +txtName.getText();
				output += "\nCustomer Addres: " +txtAddress.getText();
				output +="\nFlooring Type: ";
				if (woodButton.isSelected())
				{
					output += "Wood";
				}
				if (carpetButton.isSelected())
				{
					output += "Carpet";
				}
				output += "\nFloor Area: " +area;
				output += "\nTotal Cost: " +cost;
				txtArea.setText(output);
				JOptionPane.showMessageDialog(FloorOrders.this, output);
			}
			else
				JOptionPane.showMessageDialog(FloorOrders.this, "Error! Area and total cost must be calculated first.");	
		}
	}
	
	private class ButtonHandlerSubmitOrder implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{
			if(txtName.getText() != "" && txtAddress.getText() != "")
			{
				String flooringType;
				if(woodButton.isSelected())
				{
					flooringType = "Wood";
				}
				else
				{
					flooringType = "Carpet";
				}
				
				try
				{
					st = cnn.createStatement();
					int x = st.executeUpdate("Insert into flooring values " + "('" +txtName.getText()+ "','"+txtAddress.getText()+"','" +flooringType+"','"+area+"','"+cost+"')");
					if (x==1)
						JOptionPane.showMessageDialog(null, "Order Submitted!");
				}
				catch (SQLException e)
				{
					System.out.println("Connection to database failed!");
					System.out.println(e.getMessage());
					//return;
				}
			}
			else
				JOptionPane.showMessageDialog(null, "Error! Enter name and address on customer tab.");
			
		}
	}
	
	private class ButtonHandlerDisplayOrderList implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event)
		{

				try
				{
					String resultOutput = "";
					ResultSet rs = null;
					rs = st.executeQuery("Select * from flooring");
					resultOutput += "Order List:";
					resultOutput += "\nCustomer Name | Customer Address | Flooring Type | Floor Area | Floor Cost";
					while(rs.next())
					{
						resultOutput += "\n"+rs.getString(1)+" | "+rs.getString(2)+" | "+rs.getString(3)+" | "+rs.getString(4)+" | "+rs.getString(5);
						txtAreaDisplay.setText(resultOutput);
					}
					JOptionPane.showMessageDialog(FloorOrders.this, txtAreaDisplay);
				}
				
				catch (SQLException e)
				{
					System.out.println("Connection to database failed!");
					System.out.println(e.getMessage());
					//return;
				}
			
		}
	}


public static void main(String args[])
{
	
//	try 
//	{ 
//		Class.forName("com.mysql.jdbc.Driver");
//		System.out.println("JDBC Driver registered!");
//	} 
//	catch(ClassNotFoundException cnfe) 
//	{ 
//	     System.out.println("Cannot locate the driver!"); 
//	     return;
//	}
//	try
//	{
//		System.out.println("Connecting to database...");
//		cnn = DriverManager.getConnection("jdbc:mysql://devry.edupe.net:4300/cis355_3229", "3229", "DeVry_Student");
//		System.out.println("Connection to database established!");
//
//	}
//	catch (SQLException e)
//	{
//		System.out.println("Connection to database failed!");
//		System.out.println(e.getMessage());
//		//return;
//	}
	FloorOrders demo =new FloorOrders();
	
	
	demo.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );}
}

