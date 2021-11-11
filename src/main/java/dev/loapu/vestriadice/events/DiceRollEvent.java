package dev.loapu.vestriadice.events;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class DiceRollEvent extends Event implements Cancellable
{
	private static final HandlerList HANDLERS = new HandlerList();
	
	private final Player player;
	private final int result;
	private final short modifier;
	private boolean isCancelled;
	
	public DiceRollEvent(Player player, int result, short modifier)
	{
		this.player = player;
		this.result = result;
		this.modifier = modifier;
	}
	
	@Override
	public HandlerList getHandlers()
	{
		return HANDLERS;
	}
	
	public static HandlerList getHandlerList()
	{
		return HANDLERS;
	}
	
	@Override
	public boolean isCancelled()
	{
		return this.isCancelled;
	}
	
	@Override
	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}
	
	public Player getPlayer()
	{
		return player;
	}
	
	public int getResult()
	{
		return result;
	}
	
	public short getModifier()
	{
		return modifier;
	}
	
	
}
