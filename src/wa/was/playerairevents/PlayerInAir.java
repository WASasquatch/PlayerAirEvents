package wa.was.playerairevents;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.BlockIterator;
import org.bukkit.util.Vector;

import wa.was.playerairevents.events.PlayerFallEvent;
import wa.was.playerairevents.events.PlayerFlyingEvent;
import wa.was.playerairevents.events.PlayerJumpEvent;
import wa.was.playerairevents.events.PlayerLandedEvent;
import wa.was.playerairevents.events.PlayerRisingEvent;
import wa.was.playerairevents.spigot.events.PlayerExitsEvent;

public final class PlayerInAir extends JavaPlugin implements Listener {

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

	@EventHandler(priority = EventPriority.LOWEST)
	public void playerInAir(PlayerMoveEvent e) {
		Player player = e.getPlayer();
		if (player.getEyeLocation().getBlock().getType().equals(Material.WATER)
				|| player.getEyeLocation().getBlock().getType().equals(Material.STATIONARY_WATER)
				|| player.getEyeLocation().getBlock().getType().equals(Material.LADDER)) {
			resetPlayer(player);
			return;
		}
		if (player.isOnGround()) {
			resetPlayer(player);
			return;
		}
		Location f = e.getFrom(), t = e.getTo();
		// Jump
		if (f.getBlock().getY() < t.getBlock().getY()
				&& !(f.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR))
				&& t.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR)) {
			if (!(wasAirborn.containsKey(player.getUniqueId()))) {
				wasAirborn.put(player.getUniqueId(), true);
			}
			int jumped = 1;
			if (hasJumped.containsKey(player.getUniqueId())) {
				jumped = hasJumped.get(player.getUniqueId());
			}
			Bukkit.getServer().getPluginManager()
					.callEvent(new PlayerJumpEvent(player, jumped, e.getFrom(), e.getTo()));
			// Airborne
		} else if (f.getBlock().getY() < t.getBlock().getY()
				&& t.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR)) {
			if (!(wasAirborn.containsKey(player.getUniqueId()))) {
				wasAirborn.put(player.getUniqueId(), true);
			}
			if (hasJumped.containsKey(player.getUniqueId())) {
				int jumped = hasJumped.get(player.getUniqueId());
				hasJumped.put(player.getUniqueId(), jumped + 1);
			} else {
				hasJumped.put(player.getUniqueId(), 1);
			}
			int jumped = hasJumped.get(player.getUniqueId());
			Bukkit.getServer().getPluginManager()
					.callEvent(new PlayerRisingEvent(player, jumped, e.getFrom(), e.getTo()));
			// Falling
		} else if (f.getBlock().getY() > t.getBlock().getY()
				&& t.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR)) {
			if (!(wasAirborn.containsKey(player.getUniqueId()))) {
				wasAirborn.put(player.getUniqueId(), true);
			}
			if (!(fallLocation.containsKey(player.getUniqueId()))) {
				fallLocation.put(player.getUniqueId(), f.getBlock().getLocation());
			}
			int fallen = (int) Math
					.abs((fallLocation.get(player.getUniqueId()).getY() - t.getBlock().getLocation().getY()));
			hasFallen.put(player.getUniqueId(), fallen);
			int jumped = 1;
			if (hasJumped.containsKey(player.getUniqueId())) {
				jumped = hasJumped.get(player.getUniqueId());
			}
			Bukkit.getServer().getPluginManager().callEvent(new PlayerFallEvent(player, fallen, jumped, e.getFrom(),
					e.getTo(), fallLocation.get(player.getUniqueId())));
			// Landed (?)
		} else if (wasAirborn.containsKey(player.getUniqueId()) && wasAirborn.get(player.getUniqueId())
				|| (t.getBlock().getRelative(BlockFace.DOWN, 1).getType().isSolid()
						&& f.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR))) {
			int jumped = 1;
			int fallen = 1;
			if (hasJumped.containsKey(player.getUniqueId())) {
				jumped = hasJumped.get(player.getUniqueId());
			}
			if (hasFallen.containsKey(player.getUniqueId())) {
				fallen = hasFallen.get(player.getUniqueId());
			}
			Bukkit.getServer().getPluginManager()
					.callEvent(new PlayerLandedEvent(player, fallen, jumped, e.getFrom(), e.getTo()));
			resetPlayer(player);
		}

		// Fly Event
		if (t.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR)
				&& f.getBlock().getRelative(BlockFace.DOWN, 1).getType().equals(Material.AIR)
				&& !(f.getBlock().getLocation().equals(t.getBlock().getLocation()))) {

			BlockIterator trace = new BlockIterator(f.getWorld(), f.toVector(),
					new Vector(BlockFace.DOWN.getModX(), BlockFace.DOWN.getModY(), BlockFace.DOWN.getModZ()), 0, 256);

			Block groundBlock = null;

			int toGround = 0;
			while (trace.hasNext()) {
				Block block = trace.next();
				if (!(block.getType().equals(Material.AIR))) {
					groundBlock = block;
					break;
				}
				toGround++;
			}

			Bukkit.getServer().getPluginManager()
					.callEvent(new PlayerFlyingEvent(player, e.getFrom(), e.getTo(), toGround, groundBlock));

		}

	}

	public void resetPlayer(Player player) {
		hasJumped.remove(player.getUniqueId());
		hasFallen.remove(player.getUniqueId());
		wasAirborn.put(player.getUniqueId(), false);
		fallLocation.remove(player.getUniqueId());
	}

}
