package ebu.dev.radiovis.server;

import java.util.HashMap;
import java.util.LinkedList;

import ebu.dev.radiovis.server.exceptions.BadFormatException;
import ebu.dev.radiovis.server.exceptions.NotFoundKeyException;

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


enum Command {CONNECT, SUBSCRIBE, UNSUBSCRIBE, MESSAGE, ERROR, ACK, SEND};
public class StompFrame{
	Command command;
	String brutInput;
	public String body;
	HashMap<String, String> fields;
	
	public StompFrame(){}
	/*public RequestHeader(Command command, String destination, String ack,
			String message, String brutInput) {
		this.command = command;
		this.destination = destination;
		this.ack = ack;
		this.body = body;
		this.brutInput = brutInput;
	}*/
	public StompFrame(String input){
		fields=new HashMap<String, String>();
		parseInput(input);
	}
	
	public String toString(){
		String s = "*** REQUEST HEADER ***\n"; 
		s+= "COMMAND: "+command+"\n";
		for(int i=0; i<fields.size();i++)
			s+= fields.keySet().toArray()[i]+": "+fields.values().toArray()[i]+"\n";
		s+= "body:\n"+this.body;
		return s;
		
	}

	public void parseInput(String input){
		this.brutInput = new String(input);
		LinkedList<String> lines = new LinkedList<String>();
		String actualLine = "";
		char actualChar;
		for(int i=0; i < input.length(); i++){
			actualChar = input.charAt(i);
			if(actualChar == '\n'){
				lines.add(actualLine);
				actualLine = "";
			}
			else{
				actualLine += actualChar;
			}
			
		}
		
		Command[] cmd = Command.values();
		for(int i=0; i<cmd.length; i++){
			int pos = lines.get(0).indexOf(cmd[i].toString());
			if(pos == 0){
				this.command = cmd[i];
				break;
			}
		}
		this.body="";
		boolean bodyline=false;
		for(int i=1; i< lines.size()-1;i++){
			String line = lines.get(i);
			//System.out.print(i+":"+line);
			if(bodyline){
				this.body += line+'\n';
			}else{
				if(line.matches("([a-zA-Z])*:.*")){
				//	System.out.print("!");
					int pos = line.indexOf(":");
					fields.put(line.substring(0,pos), line.substring(pos+1));
				}
				else{
					//System.out.print("BODY!!");
					bodyline = true;				
				}
			}
			//System.out.println();
		}
		if(this.command == null){
			this.command=Command.ERROR;	
			return;
		}
				
	}
	
	public String getField(String key) throws NotFoundKeyException{
		if(fields.containsKey(key)){
			return fields.get(key);
		}
		else 
			throw new NotFoundKeyException();
	}

}
