package org.bibsonomy.database;

import org.apache.shindig.auth.SecurityToken;
import org.bibsonomy.model.logic.LogicInterface;
import org.bibsonomy.util.ValidationUtils;

public class ShindigDBLogicUserInterfaceFactory extends
		DBLogicNoAuthInterfaceFactory implements ShindigLogicInterfaceFactory {

	public LogicInterface getLogicAccess(SecurityToken st) {
		if ((ValidationUtils.present(st)) && (!st.isAnonymous())) {
			return getLogicAccess(st.getViewerId(), null);
		}

		return getLogicAccess(null, null);
	}
}