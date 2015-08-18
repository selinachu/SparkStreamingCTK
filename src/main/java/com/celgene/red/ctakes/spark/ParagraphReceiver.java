/**
 * 
 */
package com.celgene.red.ctakes.spark;

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
