package dev.loapu.vestriadice.utils;

import dev.loapu.vestriadice.VestriaDice;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class LangManager
{
	private final VestriaDice plugin = VestriaDice.getInstance();
	private final Locale DEFAULT_LOCALE = Locale.ENGLISH;
	private ClassLoader languageLoader;
	private final Map<String, String> globalPlaceholders = new HashMap<>();
	private final MiniMessage serializer = MiniMessage.get();
	
	private static LangManager instance;
	
	public LangManager()
	{
		instance = this;
		initialize();
	}
	
	private void initialize()
	{
		File langFolder = new File(plugin.getDataFolder(), "lang");
		if (!langFolder.exists())
		{
			plugin.debug("+ language folder");
			langFolder.mkdirs();
		}
		for (Language language : Language.values())
		{
			plugin.debug(language.getLanguage());
			String fileName = "lang" + (language == Language.DEFAULT ? "" : "_" + language) + ".properties";
			File langFile = new File(langFolder, fileName);
			InputStream langResource = plugin.getResource("lang" + File.separator + fileName);
			if (langResource == null) continue;
			try
			{
				Files.copy(langResource, langFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			}
			catch (IOException e)
			{
				plugin.error(e);
			}
		}
		try
		{
			URL[] urls = { langFolder.getAbsoluteFile().toURI().toURL() };
			languageLoader = new URLClassLoader(urls);
		}
		catch (MalformedURLException e)
		{
			plugin.error(e);
		}
		globalPlaceholders.clear();
		globalPlaceholders.put("author", plugin.getDescription().getAuthors().get(0));
		globalPlaceholders.put("prefix", "<dark_gray>[<gold>⚅</gold><dark_gray>]</dark_gray>");
		globalPlaceholders.put("plugin", plugin.getName());
		globalPlaceholders.put("version", plugin.getDescription().getVersion());
		globalPlaceholders.put("docs", "<click:open_url:'" + plugin.getDescription().getWebsite() + "'>" + plugin.getDescription().getWebsite() + "</click>");
		globalPlaceholders.put("colorPrimary", "<gold>");
		globalPlaceholders.put("colorDark", "<dark_gray>");
		globalPlaceholders.put("colorLight", "<gray>");
	}
	
	public static LangManager getInstance()
	{
		return instance;
	}
	
	public void reload()
	{
		initialize();
	}
	
	public String getString(Message message)
	{
		return getString(message, Bukkit.getConsoleSender());
	}
	public String getString(Message message, CommandSender sender)
	{
		String key = message.toString();
		Locale locale = DEFAULT_LOCALE;
		if (Setting.USE_PLAYER_LOCALE.asBoolean() && sender instanceof Player player)
		{
			Locale playerLocale = player.locale();
			locale = new Locale(playerLocale.getLanguage(), playerLocale.getCountry(), "custom");
		}
		else
		{
			String languageString = Setting.LANGUAGE.asString();
			plugin.debug(languageString);
			if (!languageString.isEmpty())
			{
				String[] langArr = languageString.split("_");
				locale = switch (langArr.length)
							 {
								 case 3 -> new Locale(langArr[0], langArr[1], langArr[2]);
								 case 2 -> new Locale(langArr[0], langArr[1]);
								 default -> new Locale(langArr[0]);
							 };
			}
		}
		ResourceBundle rb = ResourceBundle.getBundle("lang", locale, languageLoader);
		return rb.containsKey(key) ? rb.getString(key) : key;
	}
	public Component getComponent(Message message)
	{
		return serializer.parse(getString(message), globalPlaceholders);
	}
	public Component getComponent(Message message, CommandSender sender)
	{
		return serializer.parse(getString(message, sender), globalPlaceholders);
	}
	public Component getComponent(Message message, Map<String, String> placeholders)
	{
		Map<String, String> placeholdersMutable = new HashMap<>(placeholders);
		placeholdersMutable.putAll(globalPlaceholders);
		return serializer.parse(getString(message), placeholdersMutable);
	}
	public Component getComponent(Message message, CommandSender sender, Map<String, String> placeholders)
	{
		Map<String, String> placeholdersMutable = new HashMap<>(placeholders);
		placeholdersMutable.putAll(globalPlaceholders);
		return serializer.parse(getString(message, sender), placeholdersMutable);
	}
}
