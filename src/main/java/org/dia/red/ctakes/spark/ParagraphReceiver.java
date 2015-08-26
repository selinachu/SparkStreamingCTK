/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dia.red.ctakes.spark;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.spark.storage.StorageLevel;
import org.apache.spark.streaming.receiver.Receiver;

/**
 * @author Selina Chu, Michael Starch, and Giuseppe Totaro
 *
 */
public class ParagraphReceiver extends Receiver<String> {
	private static final long serialVersionUID = 1L;
	private int port = 0;
	public ParagraphReceiver(StorageLevel storageLevel, int port) {
		super(storageLevel);
		// TODO Auto-generated constructor stub
		this.port = port;
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		try {
			new Thread(new ReadSocketThread(this.port)).start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	public void onStop() {
		// TODO Auto-generated method stub
		
	}

	class ReadSocketThread implements Runnable {
		ServerSocket socket = null;
		public ReadSocketThread(int port) throws IOException {
			this.socket = new ServerSocket(port);
		}
		@Override
		public void run() {
			Socket client = null;
			BufferedReader buffer = null;
			while (!ParagraphReceiver.this.isStopped()) {
				try {
					if (client == null || !client.isConnected()) {
						client = this.socket.accept();
						buffer = new BufferedReader(new InputStreamReader(client.getInputStream()));
					}
					String s = buffer.readLine();
					if (s != null)
						ParagraphReceiver.this.store(s);
					// this is where paragraph are split.  Currently, Each newline is a paragraph
					// buffer.XXX to read a paragraph
					else {
						try {
							buffer.close();
							client.close();
						}
						catch(IOException ee) {
							ee.printStackTrace();
						}
						client = null;
					}
						
				}
				catch (IOException e) {
					e.printStackTrace();
					try {
						buffer.close();
						client.close();
					}
					catch(IOException ee) {
						ee.printStackTrace();
					}
					client = null;
				}
				
			}
			// TODO Auto-generated method stub
			
		}
		
	}
	
}
