package dev.loapu.vestriadice.commands;

import dev.jorel.commandapi.annotations.Alias;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.Subcommand;
import dev.loapu.vestriadice.VestriaDice;
import dev.loapu.vestriadice.utils.LangManager;
import dev.loapu.vestriadice.utils.Language;
import dev.loapu.vestriadice.utils.Message;
import org.bukkit.command.CommandSender;

import java.util.Map;

@Command("vestriadice")
@Alias({"vd", "vestriadice:vd", "dice", "vestriadice:dice", "vdice", "vestriadice:vdice"})
@Permission("vestriadice.command.base")
public class BaseCommand
{
	private static final VestriaDice plugin = VestriaDice.getInstance();
	private static final LangManager lm = LangManager.getInstance();
	private static final Map<String, String> commandMap = Map.of(
		"command.base", "<click:run_command:/vestriadice>/vestriadice</click>",
		"command.base.help", "<click:run_command:/vestriadice help>/vestriadice help</click>",
		"command.base.lang", "<click:run_command:/vestriadice lang>/vestriadice lang</click>",
		"command.base.reload", "<click:run_command:/vestriadice reload>/vestriadice reload</click>",
		"command.roll", "<click:run_command:/roll>/roll</click>"
	);
	
	@Default
	public static void vestriadice(CommandSender sender)
	{
		sender.sendMessage(lm.getComponent(Message.COMMAND_BASE_TEXT, sender, commandMap));
	}
	@Subcommand("help")
	public static void vestriadiceHelp(CommandSender sender)
	{
		sender.sendMessage(lm.getComponent(Message.COMMAND_BASE_HELP_TEXT, sender, commandMap));
	}
	@Subcommand("lang")
	public static void vestriadiceLang(CommandSender sender)
	{
		StringBuilder stringBuilder = new StringBuilder();
		for (Language lang : Language.values())
		{
			stringBuilder.append("\n- <hover:show_text:'").append(lang.toString()).append("'>").append(lang.getLanguage()).append("</hover>");
		}
		Map<String, String> languageMap = Map.of("languages", stringBuilder.toString());
		sender.sendMessage(lm.getComponent(Message.COMMAND_BASE_LANG_TEXT, sender, languageMap));
	}
	@Subcommand("reload")
	public static void vestriadiceReload(CommandSender sender)
	{
		plugin.reloadConfig();
		lm.reload();
		sender.sendMessage(lm.getComponent(Message.COMMAND_BASE_RELOAD_SUCCESS, sender));
	}
}
