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

import wa.was.playerairevents.events.PlayerAirborneEvent;
import wa.was.playerairevents.events.PlayerFallEvent;
import wa.was.playerairevents.events.PlayerJumpEvent;
import wa.was.playerairevents.events.PlayerLandedEvent;
import wa.was.playerairevents.events.PlayerRisingEvent;
import wa.was.playerairevents.spigot.events.PlayerExitsEvent;

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

@SuppressWarnings("deprecation")
public class PlayerInAir extends JavaPlugin implements Listener {
	
	private static PlayerInAir instance;
	
	public static PlayerInAir getInstance() {
		return instance;
	}
	private Map<UUID, Integer> hasFallen;
	private Map<UUID, Integer> hasJumped;
	
	private Map<UUID, Boolean> wasAirborn;
	private Map<UUID, Location> fallLocation;
	
	public PlayerInAir() {
		instance = this;
	}
	
	@Override
	public void onEnable() {
		getServer().getPluginManager().registerEvents(this, this);
		getServer().getPluginManager().registerEvents(new PlayerExitsEvent(), this);
		wasAirborn = new HashMap<UUID, Boolean>();
		hasJumped = new HashMap<UUID, Integer>();
		hasFallen = new HashMap<UUID, Integer>();
		fallLocation = new HashMap<UUID, Location>();
	}
	
	@EventHandler(priority=EventPriority.LOWEST)
	public void playerInAir(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		if ( player.getEyeLocation().getBlock().getType().equals(Material.WATER)
				|| player.getEyeLocation().getBlock().getType().equals(Material.STATIONARY_WATER)
				|| player.getEyeLocation().getBlock().getType().equals(Material.LADDER) )  {
			hasJumped.remove(player.getUniqueId());
			hasFallen.remove(player.getUniqueId());
			wasAirborn.put(player.getUniqueId(), false);
			return;
		}
		if ( player.isOnGround() )  {
			resetPlayer(player);
			return;
		}
		Location f = e.getFrom(), t = e.getTo();
		// Jump
		if ( f.getBlock().getY() < t.getBlock().getY()
				&& ! ( f.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR) )
				&& t.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR) ) {
			if ( ! ( wasAirborn.containsKey(player.getUniqueId()) ) ) {
				wasAirborn.put(player.getUniqueId(), true);
			}
			int jumped = 0;
			if ( hasJumped.containsKey(player.getUniqueId()) ) {
				jumped = hasJumped.get(player.getUniqueId());
			}
			Bukkit.getServer().getPluginManager().callEvent(new PlayerJumpEvent(player, jumped, e.getFrom(), e.getTo()));
		// Airborne
		} else if ( f.getBlock().getY() < t.getBlock().getY()
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
			Bukkit.getServer().getPluginManager().callEvent(new PlayerAirborneEvent(player, jumped, e.getFrom(), e.getTo()));
			Bukkit.getServer().getPluginManager().callEvent(new PlayerRisingEvent(player, jumped, e.getFrom(), e.getTo()));
		// Falling
		} else if ( f.getBlock().getY() > t.getBlock().getY() 
				&& t.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR) ) {
			if ( ! ( wasAirborn.containsKey(player.getUniqueId()) ) ) {
				wasAirborn.put(player.getUniqueId(), true);
			}
			if ( ! ( fallLocation.containsKey(player.getUniqueId()) ) ) {
				fallLocation.put(player.getUniqueId(), f.getBlock().getLocation());
			}
			int fallen = (int) Math.abs(( fallLocation.get(player.getUniqueId()).getY() - t.getBlock().getLocation().getY() ));
			hasFallen.put(player.getUniqueId(), fallen);
			int jumped = 0;
			if ( hasJumped.containsKey(player.getUniqueId()) ) {
				jumped = hasJumped.get(player.getUniqueId());
			}
			Bukkit.getServer().getPluginManager().callEvent(new PlayerFallEvent(player, fallen, jumped, e.getFrom(), e.getTo()));
		// Landed (?)
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
			Bukkit.getServer().getPluginManager().callEvent(new PlayerLandedEvent(player, fallen, jumped, e.getFrom(), e.getTo()));
			resetPlayer(player);
		// Surely must be landed? (Don't call me Sherly)
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
			Bukkit.getServer().getPluginManager().callEvent(new PlayerLandedEvent(player, fallen, jumped, e.getFrom(), e.getTo()));
			resetPlayer(player);
		}
	}
	
	public void resetPlayer(Player player) {
		hasJumped.remove(player.getUniqueId());
		hasFallen.remove(player.getUniqueId());
		wasAirborn.put(player.getUniqueId(), false);
		fallLocation.remove(player.getUniqueId());
	}
	
}
