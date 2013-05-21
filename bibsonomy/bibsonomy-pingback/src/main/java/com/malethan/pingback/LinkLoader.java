package com.malethan.pingback;

import java.util.List;

public abstract interface LinkLoader {
	public abstract Link loadLink(String paramString);

	public abstract List<String> findLinkAddresses(String paramString);

	public abstract boolean containsLink(String paramString1,
			String paramString2);

	public abstract String loadPageContents(String paramString);
}