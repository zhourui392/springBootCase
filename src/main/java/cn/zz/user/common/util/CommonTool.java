package cn.zz.user.common.util;

import java.util.UUID;

public class CommonTool {
	public static String uuid(){
		return UUID.randomUUID().toString();//UUID含义是通用唯一识别码 (Universally Unique Identifier)
	}
}