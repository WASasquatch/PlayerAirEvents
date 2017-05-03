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
   public void playerFlying(PlayerFlyingEvent e) {
     Player player = e.getPlayer();
     if ( player.isOnline() ) {
       player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You are flying with &f"+e.blocksToGround()+" &6blocks to the ground, which is of material type: "+e.getBlockBelow().getType()));
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

   @EventHandler
   public void playerRising(PlayerRisingEvent e) {
     Player player = e.getPlayer();
     if ( player.isOnline() ) {
       player.sendMessage(ChatColor.translateAlternateColorCodes('&', "&6You are rising &f"+e.getJumpedBlocks()+" &6blocks."));
     }
   }
```
