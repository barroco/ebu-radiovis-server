package ebu.dev.radiovis.server;

import java.io.*;
import java.math.BigInteger;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
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
public class StompServer implements Runnable {
	private ServerSocket server;
	private int stompport;
	private long idNumber=0; 
	HashMap<Integer, StompSession> connections;
	public TopicManager topicmanager;
	
	public StompServer(int port){
		try {
			 server = new ServerSocket(port);
			 this.stompport = port;
			 topicmanager = new TopicManager();
			 connections = new HashMap<Integer, StompSession>();
			 System.out.println("Server started on port "+port);
		} catch (IOException e) {
			System.out.println("Unable to start ServerSocket on port "+port);
		}
		
	}

	@Override
	public void run() {
		//Request loop
		 while(true){
			Socket newRequest = null;
			try {
				newRequest = server.accept(); // wait for new connection

				long sessionid = newSessionId();
				StompSession session = new StompSession(newRequest, sessionid, this);
				//Start new thread for the new client
				Thread t = new Thread(session);
				t.start();			

			} catch (IOException e) {
				System.out.println("Unable to handle new request");
				e.printStackTrace();
			}
			
		 }
		
	}
	
	public long newSessionId(){
		this.idNumber = this.idNumber+1;
		System.out.println("NEW ID: "+this.idNumber);
		return this.idNumber;
	}

	
}



