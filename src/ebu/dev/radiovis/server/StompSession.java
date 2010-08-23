package ebu.dev.radiovis.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.LinkedList;

import ebu.dev.radiovis.interfaces.IStompSession;

/*
Copyright (C) 2010 European Broadcasting Union
http://tech.ebu.ch
*/
/*
This file is part of EBU-radiovis-server.

EBU-radiovis-server is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as
published by the Free Software Foundation, either version 3 of the
License, or (at your option) any later version.

EBU-radiovis-server is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with EBU-radiovis-server.  If not, see <http://www.gnu.org/licenses/>.
*/
public class StompSession implements Runnable, IStompSession {
	private boolean running=true;
	
	public StompProtocol protocol;
	private StompServer server;
	
	private Socket socket;
	private PrintWriter out;
	private BufferedReader in;
	private DataInputStream bin;
	
	private long sessionid;
		
	
	public StompSession(Socket socket) {
		this(socket, 0, null);
	}
	
	public StompSession(Socket socket, long sessionid, StompServer server) {
		this.socket = socket;
		this.sessionid = sessionid;
		this.server = server;
		
		try {
			out = new PrintWriter(socket.getOutputStream(),true);
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			bin = new DataInputStream(socket.getInputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	public void run() {
		try{
		 InetAddress reqAddr = socket.getInetAddress();
		 System.out.println("New connection from " + reqAddr);
		 
		 protocol = new StompProtocol(socket, this, server.topicmanager);
		 
		 while(running){
			 StompFrame message = this.acceptData(); //CONNECT COMMAND
			 //System.out.println("NEW MESSAGE: "+message.command.toString());
			 switch(message.command){
				 case CONNECT:
					 	protocol.handleConnect(message);
				     break;
				 case SUBSCRIBE:
						protocol.handleSubscribe(message);
					 break;
				 case SEND:
					    protocol.handleSend(message);
					 break;
				 case ERROR:
					 System.out.println("!!!! ERROR !!!! error: undefined");
				 default: 
			 }
		 }
		 closeConnection();
		}
		catch (Exception e) {
			System.out.println("Client "+sessionid+" disconnected");
			 closeConnection();
		}
		server.connections.remove((int)sessionid);
	}
	
	public void error(){
		System.out.println("!!!! ERROR !!!! error: not connected");
	}
	
	public void closeConnection(){
		//System.out.println("Closing connection");
		protocol.closeSession();
		try {
			this.sendData("DISCONNECT\n\n"+(char)0);
			out.close();
			in.close();
			socket.close();
		} catch (IOException e) {
			System.out.println("Error when closing transaction");
		}

		killThread();
	}
	
	public void killThread(){
		running =false;
	}

	public StompFrame acceptData() {
		try{
			int i=0;
			String commandLine="";
			int b = 0;
			boolean quit =false;
			i=0;
			while((b=bin.read())!=-1){

				if(b == 0){
					return new StompFrame(commandLine);
				}
				commandLine += new Character((char)b);
							
			}
			
		} catch(Exception e){
			System.out.println("Connection lost with client "+sessionid);
			this.closeConnection();
		}
		return null;
	}

	
	public void sendData(String data) {
		out.print(data);
		out.flush();
	}

	@Override
	public long getSessionId() {
		return this.sessionid;
	}
	
}
