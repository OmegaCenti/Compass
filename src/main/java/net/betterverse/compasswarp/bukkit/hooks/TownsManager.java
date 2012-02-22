package net.betterverse.compasswarp.bukkit.hooks;

import net.betterverse.towns.Towns;
import net.betterverse.towns.object.Resident;
import net.betterverse.towns.object.Town;
import net.betterverse.towns.object.TownsUniverse;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class TownsManager
{
  private static Towns plugin;

  public static void setTowns(Plugin plugin)
  {
    if (plugin == null)
      plugin = null;
    else
      plugin = (Towns)plugin;
  }

  public static boolean canWarpFrom(Player player) {
    if (plugin == null)
      return true;
    Town pTown = null;
    Town cTown = null;
    try {
      Resident r = plugin.getTownsUniverse().getResident(player.getName());
      if (r.hasTown())
        try {
          pTown = r.getTown();
        } catch (Exception ex) {
          pTown = null;
        }
      String cTownName = plugin.getTownsUniverse().getTownName(player.getLocation());
      if (cTownName != null)
        cTown = plugin.getTownsUniverse().getTown(cTownName);
    } catch (Exception ex) {
      return false;
    }

    return (cTown == null) || (cTown.equals(pTown));
  }

  public static boolean canWarpTo(Player player, Location location)
  {
    if (plugin == null)
      return true;
    Town pTown = null;
    Town lTown = null;
    try {
      Resident r = plugin.getTownsUniverse().getResident(player.getName());
      if (r.hasTown())
        try {
          pTown = r.getTown();
        } catch (Exception ex) {
          pTown = null;
        }
      String lTownName = plugin.getTownsUniverse().getTownName(location);
      if (lTownName != null)
        lTown = plugin.getTownsUniverse().getTown(lTownName);
    } catch (Exception ex) {
      return false;
    }

    return (lTown == null) || (lTown.equals(pTown));
  }
}