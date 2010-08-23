package ebu.dev.radiovis.server;

import ebu.dev.radiovis.server.exceptions.BadFormatException;
import ebu.dev.radiovis.server.exceptions.NotImplementedException;
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
enum BroadcastType {fm, dab, drm, amss, hd};
enum ServiceContent {text, image};
public class TopicId {
	
	BroadcastType broadcasttype;
	ServiceContent servicecontent;
	String topicID;
	
	public TopicId(String topicID) throws BadFormatException, NotImplementedException{
		this.topicID=topicID.trim();
		//System.out.println("CHECK TopicID: "+topicID);
		
		if(topicID == null) 
			throw new BadFormatException();
		
		String[] parts = topicID.split("/topic/");

		for(int i=0; i < parts.length; i++)
			System.out.println(i+":"+parts[i]);
		if(parts.length == 2 && this.topicID.indexOf("/topic/")==0){
			//SERVICE CONTENT
			if(parts[1].matches(".*/text")){
				this.servicecontent = ServiceContent.text;
			}else if(parts[1].matches(".*/image")){
				this.servicecontent = ServiceContent.image;
			}
			else{
				throw new BadFormatException("ServiceContent not supported");				
			}
			//BroadCastType
			//FM
			String eccReg = "(([0-9[A-Fa-f]]){3})";
			String countryReg = "(([A-Za-z]){2})";
			String piReg = "(([0-9[A-Fa-f]]){4})";
			String freqReg = "(([0-9]){5})";
			String eidReg = "(([0-9[A-Fa-f]]){4})";
			String sidReg = "(([0-9[A-Fa-f]]){4})";
			String sidbigReg = "(([0-9[A-Fa-f]]){8})";
			String scidReg = "(([0-9[A-Fa-f]]){1})";
			String scidbigReg = "(([0-9[A-Fa-f]]){3})";
			if(parts[1].matches("fm/("+eccReg+"|"+countryReg+")/"+piReg+"/"+freqReg+"(/text|/image)")){
				broadcasttype = BroadcastType.fm;
			}
			else if(parts[1].matches("dab/("+eccReg+"|"+countryReg+")/"+eidReg+"/("+sidReg+"|"+sidbigReg+")/("+scidReg+"|"+scidbigReg+")(/text|/image)")){
				broadcasttype = BroadcastType.dab;
			
			}else if(parts[1].matches("(drm|amss|hd)/.*")){
				throw new NotImplementedException();
			}
			else {
				throw new BadFormatException("NOT COMPATIBLE FORMAT");
			}
		}else{
			throw new BadFormatException("???");
		}
	}
	
	 public boolean equals( Object aThat ) {
		    return this.topicID.equals(((TopicId)aThat).topicID);
	 }
	 
		  
	public String toString(){
		return this.topicID;
	}
}
