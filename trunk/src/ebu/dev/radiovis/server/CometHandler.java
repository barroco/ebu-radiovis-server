package ebu.dev.radiovis.server;

import java.io.*;
import java.util.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
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

/**
 * 
 * MULTIPLE TOPIC NOT IMPLEMENTED FOR COMET
 * multiple threads to handle topics in progress
 *
 */
public class CometHandler implements HttpHandler {
	public TopicManager topicmanager;
	public LinkedList<String> topics;
	public ArrayList<Thread> threads;
	public String lastmessageid;
	
	
	public CometHandler(TopicManager topicmanager) {
		this.topicmanager = topicmanager;
		this.topics = new LinkedList<String>();
		this.threads = new ArrayList<Thread>();
		this.lastmessageid = "";
	}


	private void parseURL(HttpExchange t){
        String[] querysplit = t.getRequestURI().getQuery().split("&");

        for(int i=0;i< querysplit.length; i++){
        	String[] tuple = querysplit[i].split("=");

        	System.out.println(i+" "+ tuple[0] + " = "+tuple[1]);
        	if(tuple[0].equalsIgnoreCase("topic")){
        		this.topics.add(tuple[1]);
        	}
        	else if(tuple[0].equalsIgnoreCase("last_id")){
            	System.out.println("lastid");
        		lastmessageid = tuple[1];
        	}
        }
	}
    public void handle(HttpExchange t) throws IOException {

    	parseURL(t);
    	String topic = topics.get(0);
        
    	System.out.println("NEW COMET CONNECTION for topic: " + topic);
    	
    	
		TopicHistoryItem lastmsg = topicmanager.history.getLastMessage(topic);
        if(lastmsg!=null && lastmessageid!="" && !lastmsg.getId().equals(lastmessageid)){
			System.out.println("COMET SEND (late):"+lastmsg.getBody() + " id:"+lastmsg.getId());
			sendResponse(t, lastmsg.getBody(), 200);
        }
        else{
	        try {
				topicmanager.signals.wait(topic);
			} catch (InterruptedException e) {
				System.out.println("ERROR: wait interrupted, close connection");
				sendResponse(t, "TIMEOUT: no new message", 408);
				return;
			}

			TopicHistoryItem newmsg = topicmanager.history.getLastMessage(topic);
		
		
			if(newmsg!=null && newmsg.getId()!=lastmessageid){	
				System.out.println("COMET SEND:"+newmsg.getBody() + " id:"+newmsg.getId());
				sendResponse(t, newmsg.getBody(), 200);
			}else{
				System.out.println("TIMEOUT: no new message");
				sendResponse(t, "TimeOut", 408);
			}
        }
        
    }
        
    public void sendResponse(HttpExchange t, String msg) throws IOException{
        sendResponse(t,msg,200);    	
    }
    
    public void sendResponse(HttpExchange t, String msg, int responsecode) throws IOException{
        t.sendResponseHeaders(responsecode, msg.length());
        OutputStream os = t.getResponseBody();
        os.write(msg.getBytes());
        os.close();
    	
    }
}
