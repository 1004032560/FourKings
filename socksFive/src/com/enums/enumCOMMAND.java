package com.enums;

public class enumCOMMAND {
	public static enum COMMAND{
		CONNECT((byte)0x01,"CONNECT"),
		BIND((byte) 0X02, "BIND"),
		UDP_ASSOCIATE((byte) 0X03, "UDP ASSOCIATE");
		
		public byte value;
		public String description;
		
		COMMAND(byte value, String description){
			this.value = value;
			this.description = description;
		}
		
		public static COMMAND converToCMD(byte value) {
			for(COMMAND cmd : COMMAND.values()) {
				if(cmd.value == value) {
					return cmd;
				}
			}
			return null;
		}
		
	}
}
