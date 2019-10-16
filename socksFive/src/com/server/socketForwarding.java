package com.server;

import com.server.socksFiveProxyServer;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

public class socketForwarding {
	// �ͻ��˵�socket
	private Socket clientSocket;
	private String clientIP;
	// Ŀ���ַ��socket
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
					transientLog("�ͻ��˶�Զ�̵�bytes��: ", n);
					needSleep = false;
				}

				while (targetIn.available() != 0) {
					int n = targetIn.read(buffer);
					clientOut.write(buffer, 0, n);
					transientLog("Զ�̶Կͻ��˵�bytes��: ", n);
					needSleep = false;
				}

				if (clientSocket.isClosed()) {
					transientLog("�ͻ��˹رա�");
				}

				// ��ֹһֱռ���̲߳��š�
				if (System.currentTimeMillis() - start >= 15000) {
					transientLog("ռ��ʱ���������ʱ�رա�");
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
		transientLog("���������");
	}

	private void transientLog(String format, Object... args) {
		socksFiveProxyServer.log("forwarding, clientIp=" + clientIP + ", targetAddress=" + targetAddress + ", port=" + targetPort + ", "
				+ format, args);
	}

//	private synchronized static void log(String format, Object... args) {
//		System.out.println(String.format(format, args));
//	}

}
