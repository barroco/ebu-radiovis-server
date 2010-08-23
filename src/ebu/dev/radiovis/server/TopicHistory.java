package ebu.dev.radiovis.server;

import java.util.*;

import ebu.dev.radiovis.interfaces.ITopicHistory;

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
public class TopicHistory implements ITopicHistory{

	HashMap<String, LinkedList<TopicHistoryItem>> messages;
	
	public TopicHistory(){
		
		messages = new HashMap<String, LinkedList<TopicHistoryItem>>();
		
	}
	
	public String add(String topic, String message) {
		Date timestamp = new Date();
		String newmessageid = topic+""+(timestamp).getTime(); 
		TopicHistoryItem item = new TopicHistoryItem(message, newmessageid, topic, timestamp);
		if(!messages.containsKey(topic)){
			LinkedList<TopicHistoryItem> list = new LinkedList<TopicHistoryItem>();
			list.addLast(item);
			messages.put(topic, list);
		}
		else
			messages.get(topic).addLast(item);
		
		return newmessageid;
	}

	public TopicHistoryItem getLastMessage(String topic) { //Return 
		TopicHistoryItem lastmessage =null;
		if(messages.containsKey(topic))
			lastmessage = messages.get(topic).getLast();
		return lastmessage;
	}

}

