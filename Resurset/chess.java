package Resurset;

import java.awt.*;
import java.awt.event.*;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import ServerSide.ChessImpl;
import ServerSide.ChessInterface;

//Chess piece images from http://world.std.com/~wij/fixation/chess-sets.html

public class chess extends JFrame
{
	
	private static final long serialVersionUID = 1L;
	public static String ClientServer = "";
	public static String IP = "192.168.43.24";
	public static String PORT = "1099";
		
	public static void main(String[] args) //With applications, you have to specify a main method (not with applets)
	{
		
		JFrame.setDefaultLookAndFeelDecorated(true); //Make it look nice
        JFrame frame = new JFrame("Chess Game"); //Title
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        chessGUI chessWindow = new chessGUI();
        frame.setContentPane(chessWindow.createGUI(frame));
        frame.addWindowFocusListener(chessWindow);
        
        frame.setSize(550,650);
        frame.setResizable(false);
        frame.setVisible(true);  
        frame.pack();     
    }
	
}