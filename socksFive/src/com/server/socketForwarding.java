package com.server;

import com.server.socksFiveProxyServer;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class socketForwarding {
	// 客户端的socket
	private Socket clientSocket;
	private String clientIP;
	// 目标地址的socket
	private Socket targetSocket;
	private String targetAddress;
	private int targetPort;

	public socketForwarding(Socket clientSocket, Socket targetSocket) {
		this.clientSocket = clientSocket;
		this.targetSocket = targetSocket;
		this.clientIP = clientSocket.getInetAddress().getHostAddress();
		this.targetAddress = targetSocket.getInetAddress().getHostAddress();
		this.targetPort = targetSocket.getPort();
	}

	public void start() {
		long start = System.currentTimeMillis();
		OutputStream clientOut = null;
		OutputStream targetOut = null;
		InputStream clientIn = null;
		InputStream targetIn = null;
		try {
			clientOut = clientSocket.getOutputStream();
			targetOut = targetSocket.getOutputStream();
			clientIn = clientSocket.getInputStream();
			targetIn = targetSocket.getInputStream();

			byte[] buffer = new byte[1024 * 512];

			while (true) {
				boolean needSleep = true;
				while (clientIn.available() != 0) {
					int n = clientIn.read(buffer);
					targetOut.write(buffer, 0, n);
					transientLog("客户端对远程的bytes是: ", n);
					needSleep = false;
				}

				while (targetIn.available() != 0) {
					int n = targetIn.read(buffer);
					clientOut.write(buffer, 0, n);
					transientLog("远程对客户端的bytes是: ", n);
					needSleep = false;
				}

				if (clientSocket.isClosed()) {
					transientLog("客户端关闭。");
				}

				// 防止一直占用线程不放。
				if (System.currentTimeMillis() - start >= 15000) {
					transientLog("占用时间过长，超时关闭。");
				}

				if (needSleep) {
					try {
						TimeUnit.MILLISECONDS.sleep(10000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			transientLog("Connection exception." + e.getMessage());
		}finally {
			socksFiveProxyServer.close(clientSocket);
			socksFiveProxyServer.close(targetSocket);
			socksFiveProxyServer.close(clientIn);
			socksFiveProxyServer.close(clientOut);
			socksFiveProxyServer.close(targetIn);
			socksFiveProxyServer.close(targetOut);
		}
		transientLog("程序结束。");
	}

	private void transientLog(String format, Object... args) {
		socksFiveProxyServer.log("forwarding, clientIp=" + clientIP + ", targetAddress=" + targetAddress + ", port=" + targetPort + ", "
				+ format, args);
	}

//	private synchronized static void log(String format, Object... args) {
//		System.out.println(String.format(format, args));
//	}

}
