package wa.was.playerairevents.events;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class PlayerFlyingEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();

	public static HandlerList getHandlerList() {
		return HANDLERS;
	}

	private Block blockBelow;
	private int blocksToGround;

	private Location from;

	private Boolean isCancelled;
	private Player player;
	private Location to;

	public PlayerFlyingEvent(Player player, Location from, Location to, int blocksToGround, Block blockBelow) {
		this.player = player;
		this.from = from;
		this.to = to;
		this.blocksToGround = blocksToGround;
		this.blockBelow = blockBelow;
		this.isCancelled = false;
	}

	public int blocksToGround() {
		return this.blocksToGround;
	}

	public Block getBlockBelow() {
		return this.blockBelow;
	}

	public Location getFrom() {
		return this.from;
	}

	@Override
	public HandlerList getHandlers() {
		return HANDLERS;
	}

	public Player getPlayer() {
		return this.player;
	}

	public Location getTo() {
		return this.to;
	}

	public boolean isCancelled() {
		return this.isCancelled;
	}

	public void setCancelled(boolean isCancelled) {
		this.isCancelled = isCancelled;
	}

}
