package dev.loapu.vestriadice.commands;

import dev.jorel.commandapi.annotations.Alias;
import dev.jorel.commandapi.annotations.Command;
import dev.jorel.commandapi.annotations.Default;
import dev.jorel.commandapi.annotations.Permission;
import dev.jorel.commandapi.annotations.arguments.AIntegerArgument;
import dev.jorel.commandapi.annotations.arguments.AStringArgument;
import dev.loapu.vestriadice.VestriaDice;
import dev.loapu.vestriadice.utils.LangManager;
import dev.loapu.vestriadice.utils.Message;
import dev.loapu.vestriadice.utils.Setting;
import mineverse.Aust1n46.chat.api.MineverseChatAPI;
import mineverse.Aust1n46.chat.api.MineverseChatPlayer;
import mineverse.Aust1n46.chat.channel.ChatChannel;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.sound.Sound.Emitter;
import net.kyori.adventure.sound.Sound.Source;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Command("roll")
@Alias({"vestriadice:roll"})
@Permission("vestriadice.command.roll")
public class RollCommand
{
	private static final VestriaDice plugin = VestriaDice.getInstance();
	private static final LangManager lm = LangManager.getInstance();
	private static final Map<String, String> commandMap = Map.of(
		"command.roll", "<click:run_command:/roll>/roll</click>"
	);
	@Default
	public static void roll(CommandSender sender)
	{
		sender.sendMessage(lm.getComponent(Message.COMMAND_ROLL_TEXT, sender, commandMap));
	}
	@Default
	public static void roll(Player player, @AStringArgument String dice)
	{
		rollDice(player, dice);
	}
	@Default
	public static void roll(Player player, @AStringArgument String dice, @AIntegerArgument int checkValue)
	{
		rollDice(player, dice, checkValue);
	}
	private static void rollDice(Player player, String dice)
	{
		rollDice(player, dice, 0);
	}
	private static void rollDice(Player sender, String input, int checkValue)
	{
		if (!input.matches("(0?[1-9]|[1-9][0-9])[wWdD](0?[1-9]|[1-9][0-9]{1,2})(\\.(0?[1-9]|[1-9][0-9])[wWdD](0?[1-9]|[1-9][0-9]{1,2}))*([+-]\\d{1,3})?"))
		{
			sender.sendMessage(lm.getComponent(Message.COMMAND_ROLL_WRONG_SYNTAX, sender, commandMap));
			return;
		}
		
		String diceString = input.toLowerCase();
		String diceStringWithoutModifier =  diceString.split("\\+")[0].split("-")[0];
		String[] diceArray;
		int modifier = 0;
		if (diceStringWithoutModifier.contains(".")) diceArray = diceStringWithoutModifier.split("\\."); // Split all different dice into one array
		else diceArray = new String[] { diceStringWithoutModifier };
		if (diceString.contains("+")) modifier += Integer.parseInt(diceString.split("\\+")[1]);
		if (diceString.contains("-")) modifier -= Integer.parseInt(diceString.split("-")[1]);
		int result = 0;
		int successCounter = 0;
		int missCounter = 0;
		// Now we have the following parts:
		// diceArray containing all dice
		// modifier being the modifier of the end result
		List<String> diceRolls = new ArrayList<>();
		for (String dice : diceArray)
		{
			StringBuilder stringBuilder = new StringBuilder();
			String[] tempArray = dice.contains("w") ? dice.split("w") : dice.split("d");
			int numberOfRolls = Integer.parseInt(tempArray[0]);
			int sides = Integer.parseInt(tempArray[1]);
			List<String> rolls = new ArrayList<>();
			boolean useLowestNumberForCriticalSuccess = Setting.USE_LOWEST_NUMBER_FOR_CRITICAL_SUCCESS.asBoolean();
			for (int i = 0; i < numberOfRolls; i++)
			{
				int roll = ThreadLocalRandom.current().nextInt(1, sides + 1);
				result += roll;
				int green = 255;
				int red = 255;
				float successPercentage = 0;
				if (roll == 1) successPercentage = (useLowestNumberForCriticalSuccess ? 1 : 0);
				else if (roll == sides) successPercentage = (useLowestNumberForCriticalSuccess ? 0 : 1);
				else successPercentage = (float) roll / (sides + 1);
				float finalColorFloat = successPercentage * 510;
				int finalColor = Math.round(finalColorFloat);
				if (finalColor > 255) red = 510 - finalColor;
				else green = finalColor;
				String finalColorString = String.format("#%02X%02X%02X", red, green, 0);
				String rollString = roll + "";
				if (sides <= 6)
				{
					rollString = switch(roll) {
						case 6 -> "⚅";
						case 5 -> "⚄";
						case 4 -> "⚃";
						case 3 -> "⚂";
						case 2 -> "⚁";
						default -> "⚀";
					};
				}
				if (finalColorString.equalsIgnoreCase("#FF0000") || finalColorString.equalsIgnoreCase("#00FF00")) rollString += "!";
				if (successPercentage == 1) successCounter++;
				if (successPercentage == 0) missCounter++;
				rolls.add("<color:" + finalColorString + ">" + rollString + "</color>");
			}
			stringBuilder.append("<gold><hover:show_text:'").append(String.join("<gray>, ", rolls)).append("'>").append(dice).append("</hover>");
			diceRolls.add(stringBuilder.toString());
		}
		diceString = String.join("<gray>, ", diceRolls);
		String modifierString = ((modifier > 0) ? "<color:#00ff00>+" : "<color:#ff0000>") + modifier + "</color>";
		String resultString = result + modifier + "";
		String deserializedDisplayname = MiniMessage.builder().build().serialize(sender.displayName());
		
		sendComponent(lm.getComponent(Message.COMMAND_ROLL_RESULT, sender, Map.of(
			"player", deserializedDisplayname,
			"dice", diceString,
			"modifier", modifierString,
			"result", resultString
		)), sender);
		
		if (checkValue == 0) return;
		String checkValueString = checkValue + "";
		String checkResultString = (result >= checkValue) ? "<color:#00ff00><lang:gui.yes>" : "<color:#ff0000><lang:gui.no>";
		String criticalSuccessesString = "<color:#00ff00>" + successCounter + "</color>";
		String criticalMissesString = "<color:#ff0000>" + missCounter + "</color>";
		sendComponent(lm.getComponent(Message.COMMAND_ROLL_CHECK, sender, Map.of(
			"checkValue", checkValueString,
			"checkResult", checkResultString,
			"criticalSuccesses", criticalSuccessesString,
			"criticalMisses", criticalMissesString
		)), sender);
	}
	
	private static void sendComponent(Component component, Player originalReceiver)
	{
		int range = Setting.RANGE_FOR_RESULT_ANNOUNCEMENT.asInt();
		String channelPermission = null;
		if (Setting.USE_VENTURE_CHAT_CHANNEL_RANGE.asBoolean() && plugin.getEnabledSoftdepends().contains("VentureChat"))
		{
			MineverseChatPlayer mcp = MineverseChatAPI.getMineverseChatPlayer(originalReceiver);
			ChatChannel chatChannel = mcp.getCurrentChannel();
			range = chatChannel.getDistance().intValue();
			if (chatChannel.hasPermission())
			{
				channelPermission = chatChannel.getPermission();
			}
		}
		plugin.debug("Range: " + range);
		Audience audience;
		if (range <= 0 && channelPermission == null) audience = Audience.audience(Bukkit.getOnlinePlayers());
		else
		{
			List<Player> receiverList = new ArrayList<>();
			for (Player potentialReceiver : Bukkit.getOnlinePlayers())
			{
				boolean permissionBoolean = channelPermission == null || potentialReceiver.hasPermission(channelPermission);
				boolean rangeBoolean = true;
				if (range >= 1) rangeBoolean = originalReceiver.getWorld() == potentialReceiver.getWorld() && originalReceiver.getLocation().distance(potentialReceiver.getLocation()) <= range;
				if (permissionBoolean && rangeBoolean)
				{
					receiverList.add(potentialReceiver);
				}
			}
			audience = Audience.audience(receiverList);
		}
		audience.sendMessage(component);
		Sound sound = Sound.sound(Key.key("block.stem.step"), Source.MASTER, 1F, .1F);
		audience.playSound(sound, Emitter.self());
	}
}
