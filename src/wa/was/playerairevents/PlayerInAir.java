package wa.was.playerairevents;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import wa.was.playerairevents.events.PlayerAirbornEvent;
import wa.was.playerairevents.events.PlayerFallEvent;
import wa.was.playerairevents.events.PlayerJumpEvent;
import wa.was.playerairevents.events.PlayerLandedEvent;

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

public class PlayerInAir extends JavaPlugin implements Listener {
	
	private Map<UUID, Integer> hasFallen;
	private Map<UUID, Integer> hasJumped;
	private Map<UUID, Boolean> wasAirborn;
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		wasAirborn = new HashMap<UUID, Boolean>();
		hasJumped = new HashMap<UUID, Integer>();
		hasFallen = new HashMap<UUID, Integer>();
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerInAir(PlayerMoveEvent e) {
		if ( e.isCancelled() ) return;
		Player player = e.getPlayer();
		if ( player.getEyeLocation().getBlock().getType().equals(Material.WATER)
				|| player.getEyeLocation().getBlock().getType().equals(Material.STATIONARY_WATER)
				|| player.getEyeLocation().getBlock().getType().equals(Material.LADDER) ) return;
		if ( player.isOnGround() ) return;
		Location f = e.getFrom(), t = e.getTo();
		if ( f.getY() < t.getY()
				&& ! ( f.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR) )
				&& t.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR) ) {
			if ( ! ( wasAirborn.containsKey(player.getUniqueId()) ) ) {
				wasAirborn.put(player.getUniqueId(), true);
			}
			int jumped = 0;
			if ( hasJumped.containsKey(player.getUniqueId()) ) {
				jumped = hasJumped.get(player.getUniqueId());
			}
			Bukkit.getServer().getPluginManager().callEvent(new PlayerJumpEvent(player, ( jumped / 2 ), e.getFrom(), e.getTo()));
		} else if ( f.getY() < t.getY()
				&& t.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR) ) {
			if ( ! ( wasAirborn.containsKey(player.getUniqueId()) ) ) {
				wasAirborn.put(player.getUniqueId(), true);
			}
			if ( hasJumped.containsKey(player.getUniqueId()) ) {
				int jumped = hasJumped.get(player.getUniqueId());
				hasJumped.put(player.getUniqueId(), jumped+1);
			} else {
				hasJumped.put(player.getUniqueId(), 1);
			}
			int jumped = hasJumped.get(player.getUniqueId());
			Bukkit.getServer().getPluginManager().callEvent(new PlayerAirbornEvent(player, ( jumped / 2 ), e.getFrom(), e.getTo()));
		} else if ( f.getY() > t.getY() 
				&& t.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR) ) {
			if ( ! ( wasAirborn.containsKey(player.getUniqueId()) ) ) {
				wasAirborn.put(player.getUniqueId(), true);
			}
			if ( hasFallen.containsKey(player.getUniqueId()) ) {
				int fallen = hasFallen.get(player.getUniqueId());
				hasFallen.put(player.getUniqueId(), fallen+1);
			} else {
				hasFallen.put(player.getUniqueId(), 1);
			}
			int jumped = 0;
			int fallen = 0;
			if ( hasJumped.containsKey(player.getUniqueId()) ) {
				jumped = hasJumped.get(player.getUniqueId());
			}
			if ( hasFallen.containsKey(player.getUniqueId()) ) {
				fallen = hasFallen.get(player.getUniqueId());
			}
			Bukkit.getServer().getPluginManager().callEvent(new PlayerFallEvent(player,  ( fallen / 2 ), ( jumped / 2 ), e.getFrom(), e.getTo()));
		} else if ( wasAirborn.containsKey(player.getUniqueId()) 
				&& wasAirborn.get(player.getUniqueId()) ) {
			int jumped = 0;
			int fallen = 0;
			if ( hasJumped.containsKey(player.getUniqueId()) ) {
				jumped = hasJumped.get(player.getUniqueId());
			}
			if ( hasFallen.containsKey(player.getUniqueId()) ) {
				fallen = hasFallen.get(player.getUniqueId());
			}
			Bukkit.getServer().getPluginManager().callEvent(new PlayerLandedEvent(player, ( fallen / 2 ), ( jumped / 2 ), e.getFrom(), e.getTo()));
			hasJumped.remove(player.getUniqueId());
			hasFallen.remove(player.getUniqueId());
			wasAirborn.put(player.getUniqueId(), false);
		} else if ( wasAirborn.containsKey(player.getUniqueId()) 
				&& ! ( wasAirborn.get(player.getUniqueId()) )
				&& ! ( t.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR) ) 
				&& f.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR) ) {
			int jumped = 0;
			int fallen = 0;
			if ( hasJumped.containsKey(player.getUniqueId()) ) {
				jumped = hasJumped.get(player.getUniqueId());
			}
			if ( hasFallen.containsKey(player.getUniqueId()) ) {
				fallen = hasFallen.get(player.getUniqueId());
			}
			Bukkit.getServer().getPluginManager().callEvent(new PlayerLandedEvent(player, ( fallen / 2 ), ( jumped / 2 ), e.getFrom(), e.getTo()));
			hasJumped.remove(player.getUniqueId());
			hasFallen.remove(player.getUniqueId());
			wasAirborn.put(player.getUniqueId(), false);
		}
	}
	
}
