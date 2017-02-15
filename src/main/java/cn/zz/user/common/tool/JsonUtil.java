package cn.zz.user.common.tool;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;

public class JsonUtil {
	public static <T> Map<String, T> changeGsonToMaps(String gsonString) {
		Map<String, T> map  = (Map<String, T>) JSONObject.parse(gsonString);
		return map;
	}

	public static List<Map<String,Object>> changeGsonToListMaps(String strResult) {
		return JSONObject.parseObject(strResult, new TypeReference<List<Map<String,Object>>>(){});
	}
}
