package wa.was.playerairevents.events;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/*************************
 * 
 *	Copyright (c) 2017 Jordan Thompson (WASasquatch)
 *	
 *	Permission is hereby granted, free of charge, to any person obtaining a copy
 *	of this software and associated documentation files (the "Software"), to deal
 *	in the Software without restriction, including without limitation the rights
 *	to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *	copies of the Software, and to permit persons to whom the Software is
 *	furnished to do so, subject to the following conditions:
 *	
 *	The above copyright notice and this permission notice shall be included in all
 *	copies or substantial portions of the Software.
 *	
 *	THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *	IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *	FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *	AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *	LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *	OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 *	SOFTWARE.
 *	
 *************************/

public class PlayerAirborneEvent extends Event {

	private static final HandlerList HANDLERS = new HandlerList();
	public static HandlerList getHandlerList() {
        return HANDLERS;
    }
	private Location from;
	private Boolean isCancelled;
	private Integer jumped;
	
	private Player player;
	
    private Location to;

    public PlayerAirborneEvent(Player player, Integer jumped, Location from, Location to) {
		this.player = player;
		this.jumped = jumped;
		this.from = from;
		this.to = to;
        this.isCancelled = false;
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
	
	public boolean isCancelled() {
        return this.isCancelled;
    }

    public void setCancelled(boolean isCancelled) {
        this.isCancelled = isCancelled;
    }
    
}
