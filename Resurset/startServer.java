package Resurset;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import ServerSide.ChessImpl;

public class startServer {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		 startSrv();
	}
	
	public static void startSrv(){
		try {
    		int convertPort = new Integer(chess.PORT).intValue();
			LocateRegistry.createRegistry(convertPort);
			ChessImpl impl= new ChessImpl();
			Naming.rebind("rmi://"+chess.IP+":"+chess.PORT+"/Chess", impl);
		} catch (RemoteException e1) {
			e1.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		System.out.println("Serveri u startua.");
	}

}
