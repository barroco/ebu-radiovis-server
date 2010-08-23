package ebu.dev.radiovis.testcase;

import ebu.dev.radiovis.server.RadioVisServer;
import ebu.dev.radiovis.server.TopicId;
import ebu.dev.radiovis.server.exceptions.BadFormatException;
import ebu.dev.radiovis.server.exceptions.NotImplementedException;
import junit.framework.TestCase;
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
public class RegexpTestCase extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testRadioVisTopicID() {
		String param = "/topic/fm/us/2704/09810/text";
		String topics[] = new String[5];
    	topics[0] = "/topic/fm/3e2/3403/10230/text";
		topics[1] = "/topic/fm/ce1/c4b4/10580/text";
		topics[2] = "/topic/fm/gb/c5b1/10310/image";
		topics[2] = "/topic/fm/gb/c5b1/10310/text";
		topics[3] = "/topic/fm/ce1/c6b1/09720/text";
		topics[4] = "/topic/fm/us/2704/09810/text";
		TopicId topic = null;
		try {
			for(int i=0; i < topics.length; i++){
				topic = new TopicId(topics[i]);
				
				assertNotNull(topic);
			}
			
		} catch (BadFormatException e) {
			//e.printStackTrace();
			fail("bad format");
		} catch (NotImplementedException e) {
			//e.printStackTrace();
			fail("not implemented");
		}
	}

}
