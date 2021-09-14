package dev.loapu.vestriadice;

import dev.jorel.commandapi.CommandAPI;
import dev.loapu.vestriadice.commands.BaseCommand;
import dev.loapu.vestriadice.commands.RollCommand;
import dev.loapu.vestriadice.utils.LangManager;
import org.bukkit.Bukkit;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class VestriaDice extends VestriaDicePlugin
{
	private final List<String> enabledSoftdepends = new ArrayList<>();
	private final int CONFIG_VERSION = 3;
	
	private static VestriaDice instance;
	public VestriaDice()
	{
		instance = this;
	}
	public static VestriaDice getInstance()
	{
		return instance;
	}
	
	@Override
	public void onEnableInner()
	{
		
		log("Loading config file v" + CONFIG_VERSION + "...");
		try
		{
			loadConfig();
		}
		catch (Exception e)
		{
			error(e);
		}
		log("Success!");
		log("Loading language files...");
		new LangManager();
		log("Success!");
		log("Loading commands...");
		CommandAPI.registerCommand(BaseCommand.class);
		CommandAPI.registerCommand(RollCommand.class);
		log("Success!");
		log("Loading available hooks...");
		for (String softDepend : getDescription().getSoftDepend())
		{
			if (Bukkit.getPluginManager().isPluginEnabled(softDepend))
			{
				log("- " + softDepend);
				enabledSoftdepends.add(softDepend);
			}
		}
		log("Success!");
	}
	
	private void loadConfig() throws Exception
	{
		File cFile = new File(getDataFolder(), "config.yml");
		Path cFileAbsolutePath = Paths.get(getDataFolder().getAbsolutePath(), "config.yml");
		String attrName = "dev.loapu.version";
		String attrValue = CONFIG_VERSION + "";
		byte[] bytes = attrValue.getBytes(StandardCharsets.UTF_8);
		int configVersionOld = CONFIG_VERSION;
		UserDefinedFileAttributeView view = Files.getFileAttributeView(cFileAbsolutePath, UserDefinedFileAttributeView.class);
		if (cFile.exists())
		{
			ByteBuffer readBuffer = ByteBuffer.allocate(view.size(attrName));
			view.read(attrName, readBuffer);
			readBuffer.flip();
			String valueFromAttributes = new String(readBuffer.array(), StandardCharsets.UTF_8);
			configVersionOld = Integer.parseInt(valueFromAttributes);
		}
		if (configVersionOld < CONFIG_VERSION)
		{
			File bFile = new File(getDataFolder(), "old_config.yml");
			Files.move(cFile.toPath(), bFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
			log(Level.WARNING, "New config version detected. Please update your settings!");
		}
		saveDefaultConfig();
		ByteBuffer writeBuffer = ByteBuffer.allocate(bytes.length);
		writeBuffer.put(bytes);
		writeBuffer.flip();
		view.write(attrName, writeBuffer);
	}
	
	public List<String> getEnabledSoftdepends()
	{
		return enabledSoftdepends;
	}
}
