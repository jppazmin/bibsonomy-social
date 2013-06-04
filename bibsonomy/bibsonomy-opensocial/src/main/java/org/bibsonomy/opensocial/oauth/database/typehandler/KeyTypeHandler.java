package org.bibsonomy.opensocial.oauth.database.typehandler;

import java.sql.SQLException;

import org.apache.shindig.gadgets.oauth.BasicOAuthStoreConsumerKeyAndSecret;

import com.ibatis.sqlmap.client.extensions.ParameterSetter;
import com.ibatis.sqlmap.client.extensions.ResultGetter;
import com.ibatis.sqlmap.client.extensions.TypeHandlerCallback;

public class KeyTypeHandler implements TypeHandlerCallback {
	public Object getResult(ResultGetter arg) throws SQLException {
		return valueOf(arg.getString());
	}

	public void setParameter(ParameterSetter param, Object value)
			throws SQLException {
		if ((value != null)
				&& ((value instanceof BasicOAuthStoreConsumerKeyAndSecret.KeyType)))
			param.setInt(((BasicOAuthStoreConsumerKeyAndSecret.KeyType) value)
					.ordinal());
	}

	public Object valueOf(String arg) {
		if ("0".equals(arg))
			return BasicOAuthStoreConsumerKeyAndSecret.KeyType.values()[0];
		if ("1".equals(arg)) {
			return BasicOAuthStoreConsumerKeyAndSecret.KeyType.values()[1];
		}
		throw new RuntimeException("Given key type ('" + arg
				+ "') not supported.");
	}
}