package com.ihuxu.hestia.server.library.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class ClientThread extends Thread {

	private Socket socket;
	private String clientKey;
	private boolean listening = false;
	private BufferedReader br;
	private BufferedWriter bw;
	
	public ClientThread(Socket socket) throws Exception {
		super();
		this.setClientKey(Long.toString(System.currentTimeMillis()));
		this.setSocket(socket);
		this.setListening(true);
	}

	public void run() {
		System.out.println("client server thread running...");
		while(this.isListening()) {
			try {
				String line = this.readLine();
				System.out.println("new line:" + line);
				if (line == null) {
					throw new Exception("Read new line error.");
				}
			} catch (Exception e) {
				e.printStackTrace();
				this.close();
			}
		}
	}
	
	private BufferedReader getReader() throws IOException {
		if (this.br != null) {
			return this.br;
		}
		this.br = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
		return this.br;
	}
	
	private BufferedWriter getWriter() throws IOException {
		if (this.bw != null) {
			return this.bw;
		}
		this.bw = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));
		return this.bw;
	}

	private Socket getSocket() {
		return socket;
	}

	private void setSocket(Socket socket) {
		this.socket = socket;
	}

	public boolean isListening() {
		return listening;
	}

	private void setListening(boolean listening) {
		this.listening = listening;
	}

	private String readLine() throws IOException {
		return this.getReader().readLine();
	}
	
	private void writeLine(String line) throws IOException {
		this.getWriter().write(line);
		this.getWriter().newLine();
	}

	private void setClientKey(String clientKey) {
		this.clientKey = clientKey;
	}
	
	public String getClientKey() throws Exception {
		return this.clientKey;
	}
	
	public void close() {
		try {
			if (this.isListening()) {
				this.setListening(false);
			}
			if (this.br != null) {
				this.br = null;
			}
			if (this.bw != null) {
				this.bw = null;
			}
			if (this.socket != null) {
				this.socket.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}