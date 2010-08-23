package ebu.dev.radiovis.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import com.sun.net.httpserver.*;

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
public class RadioVisServer {

	private static int stompport;
	private static int cometport;
		
	public static void main(String[] args) {
		
		checkArgs(args);
		
		
		//Stomp server
		StompServer server = new StompServer(stompport);
		Thread tserver = new Thread(server);
		tserver.start();
		
		HttpServer cometserver;
		try {
			String cometpath = "/comet";
			int cometport = 8100;
			cometserver = HttpServer.create(new InetSocketAddress(cometport),0);
			
			cometserver.createContext("/comet", new CometHandler(server.topicmanager));
			cometserver.setExecutor(Executors.newCachedThreadPool()); // creates a default executor
			cometserver.start();		
			System.out.println("Comet Server started on port "+cometport+" and path:"+cometpath);
		   
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

	}
	public static void checkArgs(String[] args){
		if(args.length==0){
			stompport=61613;
			cometport=80;
			System.out.println("Default ports: \n Stomp:"+stompport+" \n Comet:"+cometport);
		}
		else if(args.length==1){
			stompport=Integer.parseInt(args[0]);
			cometport=80;
		}
		else if(args.length==2){
			stompport=Integer.parseInt(args[0]);
			cometport=Integer.parseInt(args[1]);
		}
		else{
			System.out.println("Syntax: java RadioVisServer [Stomp port] [Comet port]");
			System.exit(1);
		}
	}
}
