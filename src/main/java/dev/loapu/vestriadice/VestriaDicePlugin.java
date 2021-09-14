package dev.loapu.vestriadice;

import dev.loapu.vestriadice.utils.Setting;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public class VestriaDicePlugin extends JavaPlugin
{
	private TextComponent logPrefixColored;
	private String logPrefixPlain;
	private final TextColor primary = NamedTextColor.GOLD;
	private final TextColor dark = NamedTextColor.DARK_GRAY;
	private long enableTime;
	
	@Override
	public void onLoad()
	{
		onLoadPre();
		onLoadInner();
		onLoadPost();
	}
	
	private void onLoadPre()
	{
		logPrefixColored = (Component.text("[").color(dark).append(Component.text("VD").color(primary)).append(Component.text("]").color(dark)));
		logPrefixPlain = "[" + getDescription().getPrefix() + "]";
	}
	
	private void onLoadInner()
	{
	}
	
	private void onLoadPost()
	{
	}
	
	public long getEnableTime()
	{
		return enableTime;
	}
	
	@Override
	public void onEnable()
	{
		if (!onEnablePre()) return;
		onEnableInner();
		onEnablePost();
	}
	
	private boolean onEnablePre()
	{
		enableTime = System.currentTimeMillis();
		
		log("// ========== STARTING PLUGIN ========== //");
		
		return true;
	}
	
	public void onEnableInner()
	{
	}
	
	private void onEnablePost()
	{
		long ms = System.currentTimeMillis() - enableTime;
		log("// ========== DONE (Time: " + ms + "ms) ========== //");
	}
	
	@Override
	public void onDisable()
	{
	}
	
	public void suicide()
	{
		log(Level.WARNING, "Plugin will be killed!");
		Bukkit.getPluginManager().disablePlugin(this);
	}
	
	public void debug(String debugString)
	{
		if (Setting.ENABLE_DEBUG_MESSAGES.asBoolean())
		{
			log("DEBUG", debugString);
		}
	}
	
	public void error(Exception e)
	{
		log(Level.SEVERE, "Looks like we have a " + e.toString() + " here.");
		log(Level.SEVERE, "Here is a bit more detail:");
		e.printStackTrace();
	}
	
	public void log(String prefixString, String messageString)
	{
		log(Level.INFO, "[" + prefixString + "] " + messageString);
	}
	
	public void log(String messageString)
	{
		log(Level.INFO, messageString);
	}
	
	public void log(Level level, String messageString)
	{
		ConsoleCommandSender console = Bukkit.getConsoleSender();
		if (level == Level.INFO)
		{
			console.sendMessage(logPrefixColored.append(Component.text(" ")).append(Component.text(messageString).color(primary)));
		}
		else
		{
			Bukkit.getLogger().log(level, logPrefixPlain + " " + messageString);
		}
	}
}
