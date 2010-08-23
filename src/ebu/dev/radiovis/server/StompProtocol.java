package ebu.dev.radiovis.server;

import java.net.*;
import java.sql.Time;
import java.util.*;

import com.sun.jmx.snmp.Timestamp;

import ebu.dev.radiovis.interfaces.*;
import ebu.dev.radiovis.server.exceptions.*;

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


class StompStatus{
	private boolean connected;
	private boolean registred;
	
	public StompStatus(){
		connected = false;
		registred = false;
	}

	public boolean isConnected() {
		return connected;
	}

	public void setConnected(boolean connected) {
		this.connected = connected;
	}

	public boolean isRegistred() {
		return registred;
	}

	public void setRegistred(boolean registred) {
		this.registred = registred;
	}
}

public class StompProtocol implements IStompProtocol{

	private LinkedList<TopicId> subscriptions;
	private StompStatus status;	
	private StompSession session;
	private TopicManager topicmanager;
	
	public StompProtocol(Socket socket, StompSession session, TopicManager topicmanager){
		this.status = new StompStatus();
		this.session = session;
		this.subscriptions = new LinkedList<TopicId>();
		this.topicmanager = topicmanager;

	}

	
	public void handleConnect(StompFrame message) throws BadAnswerException, AlreadyConnectedException {		
		if(this.status.isConnected()){
			throw new AlreadyConnectedException();
		}
		if(message.command.equals(Command.CONNECT)){
			System.out.println("Reception:"+message.brutInput);
			String msg = ("CONNECTED\nsession:"+session.getSessionId()+"\n\n"+ (char)0);
			System.out.print("Answer:"+msg);
			session.sendData(msg);
		}
		/*StompFrame answer = session.acceptData(); //ACK
		if(answer.command.equals(Command.ACK)){*/
			this.status.setConnected(true);
			/*return;
		}
		else {
			throw new BadAnswerException();
		}	*/
	}
	
	public void handleSubscribe(StompFrame message) throws NotConnectedException {
		if(!this.status.isConnected()){
			throw new NotConnectedException();
		}
		if(message.command.equals(Command.SUBSCRIBE)){
			try {
				System.out.println("Reception:"+message.brutInput);
				TopicId topic = new TopicId(message.fields.get("destination"));
				System.out.println("New subscription: "+this.session.getSessionId()+" for "+topic);
				this.status.setRegistred(true);//useless
				topicmanager.subscribe(topic.toString(), session);
				if(!this.subscriptions.contains(topic))
					this.subscriptions.add(topic);
				
				/*String msg="SUBSCRIBE\n";
				msg+="destination: "+topic.toString()+"\n";
				msg+="ack: auto\n";
				msg+="\n\n"+(char)0;*/
				//session.sendData(msg);
				//System.out.println("Answer:"+msg);
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
	}
	
	public void handleSend(StompFrame message) throws NotConnectedException {
		if(!this.status.isConnected()){
			throw new NotConnectedException();
		}
		if(message.command.equals(Command.SEND)){
			System.out.println("SEND message:"+ message.body.trim() +" to "+ message.fields.get("destination"));
			topicmanager.broadcast(message.fields.get("destination"), message.body.trim());
		}
	}
	
	public void sendMessage(String destination, String body, String messageId){
		String msg="MESSAGE\n";
		/*msg+="destination:"+destination+"\n";
		msg+="message-id:"+messageId+"\n";
		msg+="content-length:"+body.getBytes().length+"\n";
		msg+="\n";
		msg+=body+"\n"+(char)0;
		*/
		long timestamp = (new Date()).getTime();
		msg+="destination:"+destination+"\n";
		msg+="message-id:"+messageId+"\n";
		msg+="link:http://www.ebu.ch/\n";
		msg+="timestamp:"+timestamp+"\n";
		msg+="expires:0\n";
		msg+="priority:0\n";
		msg+="trigger-time:NOW\n\n";
		msg+= body+""+(char)0;
		sendMessageToSocket(msg);
		System.out.println(msg);
	}
	
	private void sendMessageToSocket(String msg){
		session.sendData(msg);
	}
	
	public void closeSession() {
		for(int i=0; i< subscriptions.size(); i++){
			topicmanager.unsubscribe(subscriptions.get(i).toString(),session);
		}
	}

	public void handleUnsubscribe(StompFrame frame) {
		try {
			throw new NotImplementedException();
		} catch (NotImplementedException e) {
			e.printStackTrace();
		}
		
	}
}
