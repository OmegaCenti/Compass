package net.betterverse.compasswarp.bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import net.betterverse.compasswarp.api.CompassWarp;
import net.betterverse.compasswarp.api.player.CompassWarpPlayer;
import net.betterverse.compasswarp.bukkit.hooks.MyChunksManager;
import net.betterverse.compasswarp.bukkit.hooks.TownsManager;
import net.betterverse.compasswarp.bukkit.listeners.EventListener;
import net.betterverse.compasswarp.bukkit.player.BukkitCompassWarpPlayer;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.FileConfigurationOptions;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

public class BukkitCompassWarp extends JavaPlugin
  implements CompassWarp, Runnable
{
  public static CompassWarp instance;
  private Map<String, CompassWarpPlayer> players;
  private EventListener listener;

  public void onEnable()
  {
    instance = this;
    getConfig().options().copyDefaults(true);
    saveConfig();
    this.players = new HashMap();
    for (Player p : getServer().getOnlinePlayers())
      registerPlayer(p);
    this.listener = new EventListener(this);
    if (getServer().getPluginManager().isPluginEnabled("Towny"))
      TownsManager.setTowns(getServer().getPluginManager().getPlugin("Towny"));
    if (getServer().getPluginManager().isPluginEnabled("MyChunks"))
      MyChunksManager.setMyChunks(getServer().getPluginManager().getPlugin("MyChunks"));
    getServer().getScheduler().scheduleSyncRepeatingTask(this, this, 0L, 20L);
    getServer().getLogger().info("[CompassWarp] - Enabled");
  }

  public void onDisable()
  {
    getServer().getLogger().info("[CompassWarp] - Disabled");
  }

  public CompassWarpPlayer getPlayer(String name)
  {
    return (CompassWarpPlayer)this.players.get(name);
  }

  public int getCooldown()
  {
    return getConfig().getInt("cooldown", 30);
  }

  public void run()
  {
    for (CompassWarpPlayer player : this.players.values())
      if (player.getCooldown() > 0)
        player.setCooldown(player.getCooldown() - 1);
  }

  public void registerPlayer(Player player)
  {
    this.players.put(player.getName(), new BukkitCompassWarpPlayer(player));
  }

  public void unregisterPlayer(Player player) {
    this.players.remove(player.getName());
  }
}