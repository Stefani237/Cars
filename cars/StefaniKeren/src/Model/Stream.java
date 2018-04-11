package Model;

import java.io.ObjectOutputStream;
import java.net.Socket;

/** 
 * This Stream class keeps the race connection information details. 
 * @author Keren Laor
 * @author Stefani Moron
 * @since 15-03-2017
 */

public class Stream {
	
	
	private Socket socket;
	private ObjectOutputStream st;
	
	public Stream (Socket socket,ObjectOutputStream st){
		
		this.socket = socket;
		this.st =  st;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ObjectOutputStream getSt() {
		return st;
	}

	public void setSt(ObjectOutputStream st) {
		this.st = st;
	}
	
	

}
