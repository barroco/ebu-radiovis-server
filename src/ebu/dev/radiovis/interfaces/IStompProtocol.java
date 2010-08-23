package ebu.dev.radiovis.interfaces;

import ebu.dev.radiovis.server.StompFrame;
import ebu.dev.radiovis.server.StompServer;
import ebu.dev.radiovis.server.exceptions.AlreadyConnectedException;
import ebu.dev.radiovis.server.exceptions.BadAnswerException;
import ebu.dev.radiovis.server.exceptions.NotConnectedException;
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
enum StompCommand {CONNECT, SUBSCRIBE, UNSUBSCRIBE, MESSAGE, ERROR, ACK, SEND};

public interface IStompProtocol {
	void handleConnect(StompFrame frame) throws BadAnswerException, AlreadyConnectedException;
	void handleSubscribe(StompFrame frame) throws NotConnectedException;
	void handleUnsubscribe(StompFrame frame);
	void handleSend(StompFrame frame) throws NotConnectedException;
	void sendMessage(String topic, String message, String messageId);
	void closeSession();
}
