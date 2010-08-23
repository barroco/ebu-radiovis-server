package ebu.dev.radiovis.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;

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
public class TopicManager {

	

	private TopicSubscriptions subscriptions;
	public TopicHistory history;
	public TopicSignals signals;
	
	public TopicManager() {
		subscriptions = new TopicSubscriptions();
		history = new TopicHistory();
		//this.locks = new HashMap<String, ReentrantLock>();
		signals = new TopicSignals();
	}
	
	public void subscribe(String topic, StompSession session){
		this.subscriptions.subscribe(topic, session);
	}
	
	public void unsubscribe(String topic, StompSession session){
		this.subscriptions.unsubscribe(topic, session);
	}
	
	public String toString(){
		return subscriptions.toString();
	}
	
	public void broadcast(String topic, String message){
		String messageid = history.add(topic, message);
		broadcastToStompListeners(topic, message, messageid);
		broadcastToCometListeners(topic);
	}
	
	
	private void broadcastToCometListeners(String destination) {
		this.signals.notifyAll(destination);
	}

	private void broadcastToStompListeners(String destination, String message, String messageid) {
		//STOMP MESSAGE
		//System.out.println("STOMP SEND:\n"+message);
		LinkedList<StompSession> listeners = this.subscriptions.getListeners(destination);

		System.out.println("BROADCAST TO: "+listeners.size()+" listeners:\n");
		if(listeners.isEmpty()){
			System.out.println("Nobody registred to "+destination);
		}
		else{
			for(int i=0; i<listeners.size();i++){
				System.out.print(" "+listeners.get(i).getSessionId());
				System.out.println();
				
				listeners.get(i).protocol.sendMessage(destination, message, messageid);
			}
		}
	}

	private String newMessageId(){
		return "NEWMESSAGEID";
	}
	
}
