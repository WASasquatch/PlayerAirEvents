package wa.was.playerairevents.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;


@Deprecated
public class PlayerAirborneEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	public static HandlerList getHandlerList() {
        return HANDLERS;
    }
	private Location from;
	private Integer jumped;
	
	private Player player;
	
    private Location to;

    public PlayerAirborneEvent(Player player, Integer jumped, Location from, Location to) {
		this.player = player;
		this.jumped = jumped;
		this.from = from;
		this.to = to;
    }

    public Location getFrom() {
    	return this.from;
    }
    
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }
	
    public Integer getJumpedBlocks() {
		return this.jumped;
	}
    
	public Player getPlayer() {
		return this.player;
	}
	
	public Location getTo() {
    	return this.to;
    }
    
}
