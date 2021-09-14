package dev.loapu.vestriadice.utils;

public enum Message
{
	COMMAND_BASE_TEXT,
	COMMAND_BASE_HELP_TEXT,
	COMMAND_BASE_LANG_TEXT,
	COMMAND_BASE_RELOAD_SUCCESS,
	
	COMMAND_ROLL_TEXT,
	COMMAND_ROLL_WRONG_SYNTAX,
	COMMAND_ROLL_RESULT,
	COMMAND_ROLL_CHECK;
	
	
	@Override
	public String toString()
	{
		return super.toString().replace('_', '.').toLowerCase();
	}
}
