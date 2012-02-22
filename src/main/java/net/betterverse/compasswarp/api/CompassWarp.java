package net.betterverse.compasswarp.api;

import net.betterverse.compasswarp.api.player.CompassWarpPlayer;

public abstract interface CompassWarp
{
  public static final String warpUsePermission = "compasswarp.use";
  public static final String warpUnlimitedPermission = "compasswarp.unlimited";

  public abstract CompassWarpPlayer getPlayer(String paramString);

  public abstract int getCooldown();
}