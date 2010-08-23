package ebu.dev.radiovis.server;

import java.util.Date;

import ebu.dev.radiovis.interfaces.ITopicHistoryItem;

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
public class TopicHistoryItem implements ITopicHistoryItem{

	private String body;
	private String id;
	private String topic;
	private Date timestamp;
	
	public TopicHistoryItem(String body, String id, String topic, Date timestamp) {
		this.body = body;
		this.id = id;
		this.topic = topic;
		this.timestamp = timestamp;
	}

	
	public String getBody() {
		return this.body;
	}

	public String getId() {
		return this.id;
	}

	public String getTopic() {
		return this.topic;
	}

	public Date getTimestamp() {
		return this.timestamp;
	}

}
