package net.betterverse.compasswarp.bukkit.player;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import net.betterverse.compasswarp.api.player.CompassWarpPlayer;
import net.betterverse.compasswarp.bukkit.BukkitCompassWarp;
import net.betterverse.compasswarp.bukkit.hooks.MyChunksManager;
import net.betterverse.compasswarp.bukkit.hooks.TownsManager;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

public final class BukkitCompassWarpPlayer
  implements CompassWarpPlayer
{
  private final Player player;
  private int cooldown;
  private static final HashSet trans = new HashSet();
  private static final HashSet unsafe;

  public BukkitCompassWarpPlayer(Player player)
  {
    this.player = player;
    this.cooldown = 0;
  }

  public void setCooldown(int cooldown)
  {
    this.cooldown = cooldown;
  }

  public int getCooldown()
  {
    return this.cooldown;
  }

  public boolean hasPermission(String permission)
  {
    return this.player.hasPermission(permission);
  }

  public void teleport(int x, int y, int z)
  {
    Location loc = new Location(this.player.getWorld(), x + 0.5D, y, z + 0.5D, this.player.getLocation().getYaw(), this.player.getLocation().getPitch());

    boolean myChunksWarpTo = MyChunksManager.canWarpTo(this.player, loc);
    boolean myChunksWarpFrom = MyChunksManager.canWarpFrom(this.player);

    if (!myChunksWarpFrom) {
      this.player.sendMessage("§eYou cannot warp from this chunk!");
      return;
    }

    if (!myChunksWarpTo) {
      this.player.sendMessage("§eYou cannot warp to this chunk!");
      return;
    }

    boolean townyWarpTo = TownsManager.canWarpTo(this.player, loc);
    boolean townyWarpFrom = TownsManager.canWarpFrom(this.player);

    if (!townyWarpFrom) {
      this.player.sendMessage("§eYou cannot warp from this town!");
      return;
    }

    if (!townyWarpTo) {
      this.player.sendMessage("§eYou cannot warp to this town!");
      return;
    }

    this.player.teleport(loc);
  }

  public void warpToTarget()
  {
    Location loc = getToLocation(this.player);

    if (loc == null) {
      this.player.sendMessage("§eNo block in sight (or too far)!");
      return;
    }

    CompassWarpPlayer cwPlayer = BukkitCompassWarp.instance.getPlayer(this.player.getName());

    cwPlayer.teleport(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
  }

  public void warpThroughTarget()
  {
    Location loc = getThroughLocation(this.player);

    if (loc == null) {
      this.player.sendMessage("§eNothing to pass through!");
      return;
    }

    CompassWarpPlayer cwPlayer = BukkitCompassWarp.instance.getPlayer(this.player.getName());

    cwPlayer.teleport(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
  }

  private static Location getThroughLocation(Player player) {
    Block bl = null;
		boolean solid,thru;
    try {
      solid = false;
      thru = false;
      List<Block> bls = new ArrayList();
      Location loc = player.getLocation();
      World w = loc.getWorld();
      double x = loc.getX();
      double y = loc.getY() + 1.0D;
      double z = loc.getZ();
      bls.add(loc.getBlock());
      double dist = loc.distance(new Location(w, x += loc.getDirection().getX(), y += loc.getDirection().getY(), z += loc.getDirection().getZ()));

      while (dist <= 100.0D) {
        if (!bls.contains(w.getBlockAt((int)x, (int)y, (int)z)))
          bls.add(w.getBlockAt((int)x, (int)y, (int)z));
        dist = loc.distance(new Location(w, x += loc.getDirection().getX(), y += loc.getDirection().getY(), z += loc.getDirection().getZ()));
      }

      if (!trans.contains(Byte.valueOf((byte)((Block)bls.get(0)).getTypeId())))
        solid = true;
      for (Block b : bls)
        if ((!thru) && (!solid)) {
          if (!trans.contains(Byte.valueOf((byte)b.getTypeId())))
            solid = true;
        } else if ((!thru) && (solid)) {
          if (trans.contains(Byte.valueOf((byte)b.getTypeId()))) {
            solid = false;
            thru = true;
            if ((trans.contains(Byte.valueOf((byte)b.getTypeId()))) && (trans.contains(Byte.valueOf((byte)b.getRelative(0, 1, 0).getTypeId()))) && (!trans.contains(Byte.valueOf((byte)b.getRelative(0, -1, 0).getTypeId()))))
            {
              bl = b;
              break;
            }
          }
        } else if ((thru) && (!solid)) {
          if ((trans.contains(Byte.valueOf((byte)b.getTypeId()))) && (trans.contains(Byte.valueOf((byte)b.getRelative(0, 1, 0).getTypeId()))) && (!trans.contains(Byte.valueOf((byte)b.getRelative(0, -1, 0).getTypeId()))))
          {
            bl = b;
            break;
          }if (!trans.contains(Byte.valueOf((byte)b.getTypeId()))) {
            solid = true;
            thru = false;
          }
        }
    }
    catch (Exception ex)
    {
      return null;
    }
    if (bl == null) return null;
    if (bl.getLocation().distance(player.getLocation()) > 100.0D) return null;
    if ((!trans.contains(Byte.valueOf((byte)bl.getTypeId()))) && (!trans.contains(Byte.valueOf((byte)bl.getRelative(0, 1, 0).getTypeId()))))
    {
      return null;
    }if (bl.getLocation().getBlockY() < 1) return null;
    if (trans.contains(Byte.valueOf((byte)bl.getRelative(0, -1, 0).getTypeId())))
      return null;
    if ((unsafe.contains(Byte.valueOf((byte)bl.getTypeId()))) || (unsafe.contains(Byte.valueOf((byte)bl.getRelative(0, 1, 0).getTypeId()))))
    {
      return null;
    }return new Location(bl.getLocation().getWorld(), bl.getLocation().getX(), bl.getLocation().getY(), bl.getLocation().getZ(), player.getLocation().getYaw(), player.getLocation().getPitch());
  }

  private static Location getToLocation(Player player)
  {
    Block bl;
    try
    {
      bl = player.getTargetBlock(trans, 100);
    } catch (Exception ex) {
      return null;
    }
    if (bl.getLocation().getBlockY() == 0) return null;
    if (bl == null) return null;
    if (bl.getLocation().distance(player.getLocation()) > 100.0D) return null;
    World w = bl.getWorld();
    int x = bl.getX();
    int y = bl.getY();
    int z = bl.getZ();
    while ((!trans.contains(Byte.valueOf((byte)w.getBlockAt(x, y, z).getTypeId()))) && (!trans.contains(Byte.valueOf((byte)w.getBlockAt(x, y + 1, z).getTypeId()))))
    {
      y++;
    }if (trans.contains(Byte.valueOf((byte)w.getBlockAt(x, y, z).getTypeId())))
      return null;
    if ((unsafe.contains(Byte.valueOf((byte)w.getBlockAt(x, y + 1, z).getTypeId()))) || (unsafe.contains(Byte.valueOf((byte)w.getBlockAt(x, y + 2, z).getTypeId()))))
    {
      return null;
    }return new Location(w, x, y + 1, z, player.getLocation().getYaw(), player.getLocation().getPitch());
  }

  static
  {
    trans.add(0);
    trans.add(8);
    trans.add(9);
    trans.add(10);
    trans.add(11);
    trans.add(78);

    unsafe = new HashSet();
    unsafe.add(10);
    unsafe.add(11);
  }
}