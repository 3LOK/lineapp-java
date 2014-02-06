package org.battlehack.lineapp.json;

import java.io.IOException;
import java.io.InputStream;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {
	public static final ObjectMapper mapper = new ObjectMapper();
	
	private Json() {}
	
	public static <T> T parse(InputStream is, TypeReference<T> typeReference) {
		try {
			return mapper.readValue(is, typeReference);
		} catch (IOException e) {
			throw new RuntimeException("could not parse json stream", e);
		}
	}
	
	public static <T> T parse(String json, TypeReference<T> typeReference) {
		try {
			return mapper.readValue(json, typeReference);
		} catch (IOException e) {
			throw new RuntimeException("could not parse json string", e);
		}
	}
	
	public static <T> T parse(byte[] json, TypeReference<T> typeReference) {
		try {
			return mapper.readValue(json, typeReference);
		} catch (IOException e) {
			throw new RuntimeException("could not parse json bytes", e);
		}
	}
	
	public static String stringify(Object obj) {
		try {
			return mapper.writeValueAsString(obj);
		} catch (IOException e) {
			throw new RuntimeException("could not stringify object", e);
		}
	}
	
	public static byte[] bytify(Object obj) {
		try {
			return mapper.writeValueAsBytes(obj);
		} catch (IOException e) {
			throw new RuntimeException("could not bytify object", e);
		}
	}
}
