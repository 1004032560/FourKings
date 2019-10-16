package com.server;

import com.server.socketForwarding;
import com.enums.enumMETHOD.METHOD;
import com.enums.enumADDRESS_TYPE.ADDRESS_TYPE;
import com.enums.enumCOMMAND.COMMAND;
import com.enums.enumCOMMAND_STATUS.COMMAND_STATUS;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class socksFiveProxyServer {
	private static final Integer SERVICE_LISTENER_PORT = 8080;
	private static final Integer MAX_CLIENTS = 10;
	private static final byte VERSION = 0X05;
	private static final byte RSV = 0x00;
	private static String SERVER_IP_ADDRESS;
	static AtomicInteger clientNum = new AtomicInteger();

	static {
		try {
			SERVER_IP_ADDRESS = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public class ClientHandler implements Runnable {

		private Socket clientSocket;
		private String clientIP;
		private int clientPort;

		public ClientHandler(Socket clientSocket) {
			this.clientSocket = clientSocket;
			this.clientIP = clientSocket.getInetAddress().getHostAddress();
			this.clientPort = clientSocket.getPort();
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			try {
				// 协商认证方法。
				authenticationMethod();
				// 处理客户端命令。
				handleClientCommand();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				handleLog("exception:" + e.getMessage());
				e.printStackTrace();
			} finally {
				close(clientSocket);
				handleLog("client dead, current client count=%s" + clientNum.decrementAndGet());
			}

		}

		private void close(Socket clientSocket2) {
			// TODO Auto-generated method stub

		}

		// 协商客户端的认证方法。
		private void authenticationMethod() throws IOException {
			// TODO Auto-generated method stub
			InputStream in = clientSocket.getInputStream();
			OutputStream out = clientSocket.getOutputStream();
			byte buffer[] = new byte[255];

			in.read(buffer, 0, 2);
			int version = buffer[0];
			int methodNum = buffer[1];

			if (version != VERSION) {
				throw new RuntimeException("版本必须是0x05!");
			}
			if (methodNum < 1) {
				throw new RuntimeException("方法数不能为0!");
			}

			in.read(buffer, 0, methodNum);
			List<METHOD> clientSupportMethodList = METHOD.converToMethod(Arrays.copyOfRange(buffer, 0, methodNum));
			handleLog("version=%s, methodNum=%s, clientSupportMethodList=%s", version, methodNum,
					clientSupportMethodList);

			buffer[0] = VERSION;
			buffer[1] = METHOD.NO_AUTHENTICATION_REQUIRED.getRangeStart();

			out.write(buffer, 0, 2);
			out.flush();
		}

		private void handleClientCommand() throws IOException {
			// TODO Auto-generated method stub
			InputStream in = clientSocket.getInputStream();
			OutputStream out = clientSocket.getOutputStream();
			byte[] buffer = new byte[255];

			in.read(buffer, 0, 4);
			int version = buffer[0];
			COMMAND cmd = COMMAND.converToCMD(buffer[1]);
			int rsv = buffer[2];
			ADDRESS_TYPE type = ADDRESS_TYPE.converToType(buffer[3]);
			if (rsv != RSV) {
				throw new RuntimeException("RSV版本必须是0x05!");
			} else if (version != VERSION) {
				throw new RuntimeException("VERSION版本必须是0x05!");
			} else if (cmd == null) {
				sendCommandResponse(COMMAND_STATUS.COMMAND_NOT_SUPPORTED);
				handleLog("该地址类型不支持。");
				return;
			}

			// 判断地址种类。
			String targetAddress = null;
			switch (type) {
			case DOMAIN:
				in.read(buffer, 0, 1);
				int domainLength = buffer[0];
				in.read(buffer, 0, domainLength);
				targetAddress = new String(Arrays.copyOfRange(buffer, 0, domainLength));
				break;
			case IPV4:
				in.read(buffer, 0, 1);
				targetAddress = ipAddressBytesToString(buffer);
				break;
			case IPV6:
				throw new RuntimeException("目前不支持 ipv6.");
			}

			in.read(buffer, 0, 2);
			int targetPort = ((buffer[0] & 0XFF) << 8) | (buffer[1] & 0XFF);
			StringBuilder msg = new StringBuilder();
			msg.append("version=").append(version).append(", cmd=").append(cmd.name()).append(", addressType=")
					.append(type.name()).append(", domain=").append(targetAddress).append(", port=").append(targetPort);
			handleLog(msg.toString());

			switch (cmd) {
			case CONNECT:
				handleConnectCommant(targetAddress, targetPort);
				break;
			case BIND:
				throw new RuntimeException("暂不支持BIND命令");
			case UDP_ASSOCIATE:
				throw new RuntimeException("暂不支持UDP_ASSOCIATE命令");
			}
		}

		// 转换ipv4的格式。
		private String ipAddressBytesToString(byte[] ipAddressBytes) {
			// 先转化为int避免出问题。
			return (ipAddressBytes[0] & 0XFF) + "." + (ipAddressBytes[1] & 0XFF) + "." + (ipAddressBytes[2] & 0XFF)
					+ "." + (ipAddressBytes[3] & 0XFF);
		}

		// 处理connect指令。
		private void handleConnectCommant(String targetAddress, int targetPort) throws IOException {
			Socket targetSocket = null;
			try {
				targetSocket = new Socket(targetAddress, targetPort);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				sendCommandResponse(COMMAND_STATUS.GENERAL_SOCKS_SERVER_FAILURE);
				return;
			}
			sendCommandResponse(COMMAND_STATUS.SUCCEEDED);
			new socketForwarding(clientSocket, targetSocket).start();
		}

		private void sendCommandResponse(COMMAND_STATUS cmdStatus) throws IOException {
			OutputStream out = clientSocket.getOutputStream();
			out.write(buildCommandResponse(cmdStatus.getRangeStart()));
			out.flush();
		}

		private byte[] buildCommandResponse(byte cmdStatusCode) {
			ByteBuffer payload = ByteBuffer.allocate(100);
			payload.put(VERSION);
			payload.put(cmdStatusCode);
			payload.put(RSV);
			payload.put(ADDRESS_TYPE.DOMAIN.value);
			byte[] addressBytes = SERVER_IP_ADDRESS.getBytes();
			payload.put((byte) addressBytes.length);
			payload.put(addressBytes);
			payload.put((byte) (((SERVICE_LISTENER_PORT & 0XFF00) >> 8)));
			payload.put((byte) (SERVICE_LISTENER_PORT & 0XFF));
			byte[] payloadBytes = new byte[payload.position()];
			payload.flip();
			payload.get(payloadBytes);
			return payloadBytes;
		}

		private void handleLog(String format, Object... args) {
			log("handle, clientIP=" + clientIP + ", port=" + clientPort + ", " + format, args);
		}

	}
	
	//写一个静态内部类
//	public static class SocketForwaring{
//		
//	}

	synchronized static void log(String format, Object... args) {
		System.out.println(String.format(format, args));
	}

	static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static String getSERVER_IP_ADDRESS() {
		return SERVER_IP_ADDRESS;
	}

	public static void setSERVER_IP_ADDRESS(String sERVER_IP_ADDRESS) {
		SERVER_IP_ADDRESS = sERVER_IP_ADDRESS;
	}

	public static AtomicInteger getClientNum() {
		return clientNum;
	}

	public static void setClientNum(AtomicInteger clientNum) {
		socksFiveProxyServer.clientNum = clientNum;
	}

	public static Integer getServiceListenerPort() {
		return SERVICE_LISTENER_PORT;
	}

	public static Integer getMaxClients() {
		return MAX_CLIENTS;
	}

	public static byte getVersion() {
		return VERSION;
	}

	public static byte getRsv() {
		return RSV;
	}
}
