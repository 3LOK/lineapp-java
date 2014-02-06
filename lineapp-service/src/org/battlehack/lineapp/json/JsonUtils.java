package org.battlehack.lineapp.json;

import java.io.IOException;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonUtils {
	public static final ObjectMapper mapper = new ObjectMapper();
	
	private JsonUtils() {}
	
	public static <T> T fromJson(byte[] json, TypeReference<T> typeReference) {
		try {
			final T obj = mapper.readValue(json, 0, json.length, typeReference);
			return obj;
		} catch (IOException e) {
			throw new RuntimeException("could not deserialize json", e);
		}
	}
	
	public static <T> T fromJsonString(String json, Class<T> clazz) {
		try {
			final T obj = mapper.readValue(json, clazz);
			return obj;
		} catch (IOException e) {
			throw new RuntimeException("could not deserialize json", e);
		}
	}
	
	public static <T> byte[] toJson(T obj) {
		try {
			return mapper.writeValueAsBytes(obj);
		} catch (IOException e) {
			throw new RuntimeException("could not serialize to json", e);
		}
	}
	
	public static String toJsonString(Object obj) {
		try {
			return JsonUtils.mapper.writeValueAsString(obj);
		} catch (IOException e) {
			throw new RuntimeException("could not serialize to json", e);
		}
	}
}
