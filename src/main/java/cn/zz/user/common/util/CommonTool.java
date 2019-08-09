package cn.zz.user.common.util;

import java.util.UUID;

public class CommonTool {
	public static String uuid(){
		//UUID含义是通用唯一识别码 (Universally Unique Identifier)
		return UUID.randomUUID().toString();
	}
}