package dev.loapu.vestriadice.utils;

public enum Language
{
	DEFAULT("en_UK", "English (United Kingdom)"),
	DE_DE("de_DE", "Deutsch (Deutschland)");
	
	private final String langCode;
	private final String language;
	Language(String langCode, String language)
	{
		this.langCode = langCode;
		this.language = language;
	}
	
	@Override
	public String toString()
	{
		return langCode;
	}
	
	public String getLanguage()
	{
		return language;
	}
}
