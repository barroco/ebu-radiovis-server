package ebu.dev.radiovis.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;

import ebu.dev.radiovis.interfaces.ITopicSubscriptions;

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
public class TopicSubscriptions implements ITopicSubscriptions {

	private HashMap<String, LinkedList<StompSession>> subscriptions;
	
	public TopicSubscriptions() {
		this.subscriptions = new HashMap<String, LinkedList<StompSession>>();
	}
	
	public void subscribe(String topic, StompSession session) {
		if(!this.subscriptions.containsKey(topic)){
			//Create new list
			LinkedList<StompSession> list = new LinkedList<StompSession>();
			list.add(session);
			subscriptions.put(topic, list);
		}
		else{
			//Just add session
			if(!subscriptions.get(topic).contains(session))
				subscriptions.get(topic).add(session);
		}
		
		/*if(!locks.containsKey(topic))
			locks.put(topic, new ReentrantLock());
		System.out.println("a: " + locks.get(topic));
		System.out.println(this.toString());
		*/
	}

	public void unsubscribe(String topic, StompSession session) {
		LinkedList<StompSession> list = subscriptions.get(topic);	
		if(list !=null && list.contains(session))
			list.remove(session);
		if(list !=null && list.isEmpty()){
			subscriptions.remove(topic);
		}
		//locks.remove("topic");

		
	}	

	
	public String toString(){
		String s="";
		Set<String> keys = subscriptions.keySet();
		s+=("TOPICMANAGER\ntopic\t\tclients\n");
		for(int i=0; i<keys.size();i++){
			String key = (String) keys.toArray()[i];
			s+=(key+": ");
			LinkedList<StompSession> list = subscriptions.get(key);
			s+="("+list.size()+") ";
			for(int j=0; j<list.size();j++){
				s+=""+list.get(j).getSessionId()+" ";
			}
			s+="\n";
		}
		return s;
	}

	public LinkedList<StompSession> getListeners(String topic) {
		LinkedList<StompSession> listeners = this.subscriptions.get(topic);
		if(listeners == null) listeners = new LinkedList<StompSession>();
		return listeners;
	}
}
