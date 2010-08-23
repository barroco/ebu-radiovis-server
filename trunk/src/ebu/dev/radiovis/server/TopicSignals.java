package ebu.dev.radiovis.server;

import java.util.HashMap;

import ebu.dev.radiovis.interfaces.ITopicSignals;

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
public class TopicSignals implements ITopicSignals {

	private HashMap<String, Object> signals;
	private final int TIMEOUT = 30000;
	
	public TopicSignals() {
		this.signals = new HashMap<String, Object>();
	}

	
	private Object getSignal(String topic) {
		Object signal = signals.get(topic);
		if(signal == null){
			signal = new Object();
			signals.put(topic, signal);
		}
		return signal;
	}
	
	public void wait(String topic) throws InterruptedException{
		Object signal = getSignal(topic);
		synchronized(signal){
			if(TIMEOUT >0){
				signal.wait(TIMEOUT);
			}else
				signal.wait();
		}
	}


	@Override
	public synchronized void notifyAll(String topic) {
		Object signal = getSignal(topic);
		synchronized(signal){
			signal.notifyAll();
		}
	}

}
