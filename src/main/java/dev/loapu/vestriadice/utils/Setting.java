package dev.loapu.vestriadice.utils;

import dev.loapu.vestriadice.VestriaDice;

public enum Setting
{
	LANGUAGE("language"),
	USE_PLAYER_LOCALE("usePlayerLocale"),
	USE_LOWEST_NUMBER_FOR_CRITICAL_SUCCESS("useLowestNumberForCriticalSuccess"),
	RANGE_FOR_RESULT_ANNOUNCEMENT("rangeForResultAnnouncement"),
	USE_VENTURE_CHAT_CHANNEL_RANGE("useVentureChatChannelRange"),
	
	
	ENABLE_DEBUG_MESSAGES("enableDebugMessages");
	
	private final String path;
	private final VestriaDice plugin = VestriaDice.getInstance();
	Setting(String path)
	{
		this.path = path;
	}
	
	public boolean asBoolean()
	{
		return plugin.getConfig().getBoolean(path, false);
	}
	
	public String asString()
	{
		return plugin.getConfig().getString(path, "");
	}
	
	public int asInt()
	{
		return plugin.getConfig().getInt(path, 0);
	}
}
