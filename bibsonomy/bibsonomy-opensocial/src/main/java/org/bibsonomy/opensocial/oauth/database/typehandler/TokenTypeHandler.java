package org.bibsonomy.opensocial.oauth.database.typehandler;

import java.sql.SQLException;

import org.apache.shindig.social.opensocial.oauth.OAuthEntry;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

public class TokenTypeHandler implements TypeHandlerCallback {
	public Object getResult(ResultGetter arg) throws SQLException {
		return valueOf(arg.getString());
	}

	public void setParameter(ParameterSetter param, Object value)
			throws SQLException {
		OAuthEntry.Type tokenType = (OAuthEntry.Type) value;
		if (value != null)
			param.setInt(tokenType.ordinal());
	}

	public Object valueOf(String arg) {
		if ("0".equals(arg))
			return OAuthEntry.Type.REQUEST;
		if ("1".equals(arg))
			return OAuthEntry.Type.ACCESS;
		if ("2".equals(arg)) {
			return OAuthEntry.Type.DISABLED;
		}
		throw new RuntimeException("Given token type ('" + arg
				+ "') not supported.");
	}
}