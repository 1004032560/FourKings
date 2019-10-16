package com.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.UUID;  

public class test {
	@SuppressWarnings({ "static-access", "resource" })
	public static void main(String[] args) throws IOException {
		ServerSocket serverSocket = new ServerSocket(8080);
		socksFiveProxyServer socks = new socksFiveProxyServer();
		while (true) {
			Socket socket = serverSocket.accept();
			if (socks.getClientNum().get() >= socks.getMaxClients()) {
				socks.log("当前开启的客户端过多。");
				continue;
			}
			socks.log("new client, ip=%s:%d, current client count=%s", socket.getInetAddress(), socket.getPort(),
					socks.getClientNum().get());
			socks.clientNum.incrementAndGet();
			new Thread(socks.new ClientHandler(socket), "client-handler-" + UUID.randomUUID().toString()).start();
		}
	}
}
