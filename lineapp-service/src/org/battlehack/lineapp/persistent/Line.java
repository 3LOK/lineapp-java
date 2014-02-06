package org.battlehack.lineapp.persistent;

import javax.jdo.JDOObjectNotFoundException;
import javax.jdo.PersistenceManager;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import org.battlehack.lineapp.api.Error;
import org.battlehack.lineapp.api.LineappException;
import org.battlehack.lineapp.json.JsonUtils;

import com.fasterxml.jackson.core.type.TypeReference;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Line {
	public Line(org.battlehack.lineapp.api.Line line) {
		line.id = null;
		json = JsonUtils.toJson(line);
	}
	
	public org.battlehack.lineapp.api.Line get() {
		final org.battlehack.lineapp.api.Line line =
				JsonUtils.fromJson(json, new TypeReference<org.battlehack.lineapp.api.Line>() {});
		line.id = id.toString();
		return line;
	}
	
	public static org.battlehack.lineapp.api.Line get(PersistenceManager pm, String id) throws LineappException {
		try {
			final Line persistLine = pm.getObjectById(Line.class, Long.valueOf(id));
			return persistLine.get();
		} catch (JDOObjectNotFoundException e) {
			throw new LineappException(new Error(Error.ERROR_NOT_FOUND, "unknown lineId: " + id));
		}
	}
	
	@PrimaryKey
    @Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
    private Long id;
	
    @Persistent(serialized = "true")
    private byte[] json;
}
