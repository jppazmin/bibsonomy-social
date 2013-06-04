package org.bibsonomy.database;

import org.apache.shindig.auth.SecurityToken;
import org.bibsonomy.model.logic.LogicInterface;

public abstract interface ShindigLogicInterfaceFactory {
	public abstract LogicInterface getLogicAccess(
			SecurityToken paramSecurityToken);
}