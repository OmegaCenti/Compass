package net.betterverse.compasswarp.bukkit.listeners;

import net.betterverse.compasswarp.api.player.CompassWarpPlayer;
import net.betterverse.compasswarp.bukkit.BukkitCompassWarp;
import net.betterverse.compasswarp.bukkit.hooks.MyChunksManager;
import net.betterverse.compasswarp.bukkit.hooks.TownsManager;
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

public final class EventListener
{
  private final BukkitCompassWarp plugin;
  private PlayerListener player;
  private EntityListener entity;
  private ServerListener server;

  public EventListener(BukkitCompassWarp plugin)
  {
    this.plugin = plugin;
    this.player = new PlayerListener();
    registerEvent(player);
    this.entity = new EntityListener();
    registerEvent(this.entity);
    this.server = new ServerListener();
    registerEvent(this.server);
  }

  public void registerEvent(Listener listener) {
    this.plugin.getServer().getPluginManager().registerEvents(listener, plugin);
  }

  private class ServerListener implements Listener
  {
    private ServerListener()
    {
    }

		@EventHandler
    public void onPluginEnable(PluginEnableEvent event)
    {
      if (event.getPlugin().getDescription().getName().equalsIgnoreCase("Towny"))
        TownsManager.setTowns(event.getPlugin());
      if (event.getPlugin().getDescription().getName().equalsIgnoreCase("MyChunks"))
        MyChunksManager.setMyChunks(event.getPlugin());
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event)
    {
      if (event.getPlugin().getDescription().getName().equalsIgnoreCase("Towny"))
        TownsManager.setTowns(null);
      if (event.getPlugin().getDescription().getName().equalsIgnoreCase("MyChunks"))
        MyChunksManager.setMyChunks(null);
    }
  }

  private class EntityListener implements Listener
  {
    private EntityListener()
    {
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event)
    {
      if (event.isCancelled())
        return;
      if (((event instanceof EntityDamageByEntityEvent)) && ((event.getEntity() instanceof Player))) {
        EntityDamageByEntityEvent event2 = (EntityDamageByEntityEvent)event;
        if ((event2.getDamager() instanceof Player)) {
          Player player = (Player)event2.getDamager();
          CompassWarpPlayer cwPlayer = EventListener.this.plugin.getPlayer(player.getName());
          if (cwPlayer == null) {
            EventListener.this.plugin.registerPlayer(player);
            cwPlayer = EventListener.this.plugin.getPlayer(player.getName());
          }
          if (cwPlayer.getCooldown() > 0) {
            event.setCancelled(true);
            player.sendMessage(" ");
            player.sendMessage("You cannot damage other players within " + EventListener.this.plugin.getCooldown());
            player.sendMessage("seconds of teleporting with your compass!");
            player.sendMessage("You must wait " + cwPlayer.getCooldown() + " seconds.");
          }
        }
      }
    }
  }

  private class PlayerListener implements Listener
  {
    private PlayerListener()
    {
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
      EventListener.this.plugin.unregisterPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event)
    {
      EventListener.this.plugin.registerPlayer(event.getPlayer());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event)
    {
      if (!event.getPlayer().getItemInHand().getType().equals(Material.COMPASS))
        return;
      Action action = event.getAction();
      CompassWarpPlayer pl = EventListener.this.plugin.getPlayer(event.getPlayer().getName());
      if (pl == null) {
        EventListener.this.plugin.registerPlayer(event.getPlayer());
        pl = EventListener.this.plugin.getPlayer(event.getPlayer().getName());
      }
      boolean unlimited = pl.hasPermission("compasswarp.unlimited");
      boolean use = pl.hasPermission("compasswarp.use");

      if ((!unlimited) && (!use)) return;

      if ((action.equals(Action.LEFT_CLICK_AIR)) || (action.equals(Action.LEFT_CLICK_BLOCK)))
        pl.warpToTarget();
      else if ((action.equals(Action.RIGHT_CLICK_AIR)) || (action.equals(Action.RIGHT_CLICK_BLOCK)))
        pl.warpThroughTarget();
      else {
        return;
      }
      if (!unlimited) {
        pl.setCooldown(EventListener.this.plugin.getCooldown());
      }
      event.setCancelled(true);
    }
  }
}