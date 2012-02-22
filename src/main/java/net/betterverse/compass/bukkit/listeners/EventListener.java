package net.betterverse.compass.bukkit.listeners;

import net.betterverse.compass.api.Compass;
import net.betterverse.compass.api.player.CompassPlayer;
import net.betterverse.compass.bukkit.BukkitCompass;
import net.betterverse.compass.bukkit.hooks.MyChunksManager;
import net.betterverse.compass.bukkit.hooks.TownsManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.event.server.PluginEnableEvent;

/**
 * This is a listener for a Bukkit-based server that includes all necessary
 * API hooks to make this plugin functional.
 * 
 * @modified 1/8/12
 * @author Julian Trust
 */
public final class EventListener implements Listener {
    
    private final BukkitCompass plugin;

    public EventListener(BukkitCompass plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        
    }
    
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        plugin.unregisterPlayer(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        plugin.registerPlayer(event.getPlayer());
    }
    
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if(!event.getPlayer().getItemInHand().getType().equals(Material.COMPASS))
            return;
        Action action = event.getAction();
        CompassPlayer pl = plugin.getPlayer(event.getPlayer().getName());
        if(pl == null) {
            plugin.registerPlayer(event.getPlayer());
            pl = plugin.getPlayer(event.getPlayer().getName());
        }
        boolean unlimited = pl.hasPermission(Compass.warpUnlimitedPermission);
        boolean use = pl.hasPermission(Compass.warpUsePermission);

        if(!unlimited && !use) return;

        if(action.equals(Action.LEFT_CLICK_AIR) || action.equals(Action.LEFT_CLICK_BLOCK))
            pl.warpToTarget();
        else if(action.equals(Action.RIGHT_CLICK_AIR) || action.equals(Action.RIGHT_CLICK_BLOCK))
            pl.warpThroughTarget();
        else
            return;

        if(!unlimited)
            pl.setCooldown(plugin.getCooldown());

        event.setCancelled(true);
    }
    
    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(event.isCancelled())
            return;
        if(event instanceof EntityDamageByEntityEvent && event.getEntity() instanceof Player) {
            EntityDamageByEntityEvent event2 = (EntityDamageByEntityEvent) event;
            if(event2.getDamager() instanceof Player) {
                Player player = (Player) event2.getDamager();
                CompassPlayer cwPlayer = plugin.getPlayer(player.getName());
                if(cwPlayer == null) {
                    plugin.registerPlayer(player);
                    cwPlayer = plugin.getPlayer(player.getName());
                }
                if(cwPlayer.getCooldown() > 0) {
                    event.setCancelled(true);
                    player.sendMessage(" ");
                    player.sendMessage("You cannot damage other players within " + plugin.getCooldown());
                    player.sendMessage("seconds of teleporting with your compass!");
                    player.sendMessage("You must wait " + cwPlayer.getCooldown() + " seconds.");
                }
            }
        }
    }
    
    @EventHandler
    public void onPluginEnable(PluginEnableEvent event) {
        if(event.getPlugin().getDescription().getName().equalsIgnoreCase("Towny"))
            TownsManager.setTowns(event.getPlugin());
        if(event.getPlugin().getDescription().getName().equalsIgnoreCase("MyChunks"))
            MyChunksManager.setMyChunks(event.getPlugin());
    }
    
    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        if(event.getPlugin().getDescription().getName().equalsIgnoreCase("Towny"))
            TownsManager.setTowns(null);
        if(event.getPlugin().getDescription().getName().equalsIgnoreCase("MyChunks"))
            MyChunksManager.setMyChunks(null);
    }
}