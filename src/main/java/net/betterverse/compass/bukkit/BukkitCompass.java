package net.betterverse.compass.bukkit;

import java.util.HashMap;
import java.util.Map;
import net.betterverse.compass.api.Compass;
import net.betterverse.compass.api.player.CompassPlayer;
import net.betterverse.compass.bukkit.hooks.MyChunksManager;
import net.betterverse.compass.bukkit.hooks.TownsManager;
import net.betterverse.compass.bukkit.listeners.EventListener;
import net.betterverse.compass.bukkit.player.BukkitCompassPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This is the Bukkit implementation of CompassWarp plugin.
 * 
 * @modified 1/8/12
 * @author Julian Trust
 */
public class BukkitCompass extends JavaPlugin implements Compass, Runnable {
    
    public static Compass instance;
    private Map<String, CompassPlayer> players;
    private EventListener listener;
    
    @Override
    public void onEnable() {
        instance = this;
        getConfig().options().copyDefaults(true);
        saveConfig();
        players = new HashMap<String, CompassPlayer>();
        for(Player p : getServer().getOnlinePlayers())
            registerPlayer(p);
        listener = new EventListener(this);
        if(getServer().getPluginManager().isPluginEnabled("Towny"))
            TownsManager.setTowns(getServer().getPluginManager().getPlugin("Towny"));
        if(getServer().getPluginManager().isPluginEnabled("MyChunks"))
            MyChunksManager.setMyChunks(getServer().getPluginManager().getPlugin("MyChunks"));
        getServer().getScheduler().scheduleSyncRepeatingTask(this, this, 0, 20);
        getServer().getLogger().info("[CompassWarp] - Enabled");
    }
    
    @Override
    public void onDisable() {
        getServer().getLogger().info("[CompassWarp] - Disabled");
    }
    
    @Override
    public CompassPlayer getPlayer(String name) {
        return players.get(name);
    }
    
    @Override
    public int getCooldown() {
        return getConfig().getInt("cooldown", 30);
    }
    
    @Override
    public void run() {
        for(CompassPlayer player : players.values()) {
            if(player.getCooldown() > 0) {
                player.setCooldown(player.getCooldown() - 1);
            }
        }
    }
    
    public void registerPlayer(Player player) {
        players.put(player.getName(), new BukkitCompassPlayer(player));
    }
    
    public void unregisterPlayer(Player player) {
        players.remove(player.getName());
    }
}