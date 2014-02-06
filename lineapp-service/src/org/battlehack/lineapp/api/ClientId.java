package org.battlehack.lineapp.api;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class ClientId implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;
    
    public static final String NS_LINEAPP = "org.battlehack.lineapp";
    public static final String NS_FACEBOOK = "com.facebook";
    
    public ClientId(String ns, String id) {
    	this.ns = ns;
        this.id = id;
    }
    
    /** Default constructor for JSON deserialization. */
    public ClientId() {}
    
    @Override
	public Object clone() {
    	return new ClientId(ns, id);
	}
    
    /** The unique id's namespace. */
    @JsonInclude(Include.NON_NULL)
    public String ns;
	
    /** The unique id. */
    @JsonInclude(Include.NON_NULL)
    public String id;
    
    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((ns == null) ? 0 : ns.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ClientId other = (ClientId) obj;
		if (ns == null) {
			if (other.ns != null)
				return false;
		} else if (!ns.equals(other.ns))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	public void validate() throws LineappException {
		if ((ns == null) || (ns.isEmpty())) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid ns"));
		}
		
		if ((id == null) || (id.isEmpty())) {
			throw new LineappException(new Error(Error.ERROR_INVALID_DATA, "invalid id"));
		}
	}
}