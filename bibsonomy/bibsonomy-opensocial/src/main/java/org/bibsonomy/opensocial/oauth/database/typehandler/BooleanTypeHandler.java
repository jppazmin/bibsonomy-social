package org.bibsonomy.opensocial.oauth.database.typehandler;

import java.sql.SQLException;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

public class BooleanTypeHandler implements TypeHandlerCallback {
	public Object getResult(ResultGetter arg) throws SQLException {
		return valueOf(arg.getString());
	}

	public void setParameter(ParameterSetter param, Object value)
			throws SQLException {
		Boolean truth = (Boolean) value;
		if (value != null)
			param.setInt(truth.booleanValue() ? 1 : 0);
	}

	public Object valueOf(String arg) {
		if ("0".equals(arg))
			return Boolean.FALSE;
		if ("1".equals(arg)) {
			return Boolean.TRUE;
		}
		throw new RuntimeException("Given truth value '" + arg
				+ "' ist not supported.");
	}
}