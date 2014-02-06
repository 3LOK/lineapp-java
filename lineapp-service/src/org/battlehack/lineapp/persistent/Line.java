package org.battlehack.lineapp.persistent;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.battlehack.lineapp.json.Json;

import com.fasterxml.jackson.core.type.TypeReference;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Line {
	public Line(org.battlehack.lineapp.api.Line line) {
		set(line);
	}
	
	public org.battlehack.lineapp.api.Line get() {
		final org.battlehack.lineapp.api.Line line =
				Json.parse(json, new TypeReference<org.battlehack.lineapp.api.Line>() {});
		line.id = id.toString();
		return line;
	}
	
	public void set(org.battlehack.lineapp.api.Line line) {
		line.id = null;
		json = Json.bytify(line);
	}
	
	public static Line byIdNoThrow(PersistenceManager pm, String id) {
		try {
			return pm.getObjectById(Line.class, Long.valueOf(id));
		} catch (JDOObjectNotFoundException e) {
			return null;
		}
	}
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
    @Persistent(serialized = "true")
    private byte[] json;
}
