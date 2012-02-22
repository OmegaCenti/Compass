package net.betterverse.compasswarp.api.player;

public abstract interface CompassWarpPlayer
{
  public abstract void setCooldown(int paramInt);

  public abstract int getCooldown();

  public abstract boolean hasPermission(String paramString);

  public abstract void teleport(int paramInt1, int paramInt2, int paramInt3);

  public abstract void warpToTarget();

  public abstract void warpThroughTarget();
}