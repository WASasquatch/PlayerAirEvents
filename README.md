# PlayerAirEvents
## A Spigot API Extension


### Example Usage

```java
	
@EventHandler
public void playerFall(PlayerFallEvent e) {
	Player player = e.getPlayer();
	if ( player.isOnline() ) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You jumped &f"+e.getJumpedBlocks()+" &6and fallen &f"+e.getFallenBlocks()+" &6blocks."));
	}
}
	
@EventHandler
public void playerAirborn(PlayerAirbornEvent e) {
	Player player = e.getPlayer();
	if ( player.isOnline() ) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You are airborn and rising &f"+e.getJumpedBlocks()+" &6blocks."));
	}
}
	
@EventHandler
public void playerJump(PlayerJumpEvent e) {
	Player player = e.getPlayer();
	if ( player.isOnline() ) {
		player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You have jumped."));
	}
}
	
@EventHandler
public void playerLanded(PlayerLandedEvent e) {
	Player player = e.getPlayer();
	if ( player.isOnline() ) {
			player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You have landed. You jumped &f"+e.getJumpedBlocks()+" &6and fallen &f"+e.getFallenBlocks()+" &6blocks."));
	}
}
  
```
