package net.betterverse.compass.bukkit.hooks;

import net.betterverse.towns.Towns;
import net.betterverse.towns.object.Resident;
import net.betterverse.towns.object.Town;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

/**
 * Static class for any sort of region checking required
 * of Towny (Bukkit plugin).
 *
 * @modified 1/8/12
 * @author Julian Trust
 */
public class TownsManager {
    
    private static Towns plugin;
    
    public static void setTowns(Plugin plugin) {
        if(plugin == null)
            TownsManager.plugin = null;
        else
            TownsManager.plugin = (Towns) plugin;
    }
    
    public static boolean canWarpFrom(Player player) {
        if(plugin == null)
            return true;
        Town pTown = null;
        Town cTown = null;
        try {
            Resident r = plugin.getTownsUniverse().getResident(player.getName());
            if(r.hasTown())
                try {
                    pTown = r.getTown();
                } catch (Exception ex) {
                    pTown = null;
                }
            String cTownName = plugin.getTownsUniverse().getTownName(player.getLocation());
            if(cTownName != null)
                cTown = plugin.getTownsUniverse().getTown(cTownName);
        } catch (Exception ex) {
            return false;
        }
        if(cTown != null && !cTown.equals(pTown))
            return false;
        return true;
    }
    
    public static boolean canWarpTo(Player player, Location location) {
        if(plugin == null)
            return true;
        Town pTown = null;
        Town lTown = null;
        try {
            Resident r = plugin.getTownsUniverse().getResident(player.getName());
            if(r.hasTown())
                try {
                    pTown = r.getTown();
                } catch (Exception ex) {
                    pTown = null;
                }
            String lTownName = plugin.getTownsUniverse().getTownName(location);
            if(lTownName != null)
                lTown = plugin.getTownsUniverse().getTown(lTownName);
        } catch (Exception ex) {
            return false;
        }
        if(lTown != null && !lTown.equals(pTown))
            return false;
        return true;
    }
}