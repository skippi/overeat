package io.skippi.overeat;

import javax.annotation.Nullable;
import net.minecraft.nbt.INBT;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;

public class BlockUsageStorage implements Capability.IStorage<BlockUsage> {
  public static ResourceLocation RESOURCE = new ResourceLocation(OverEatMod.MOD_ID, "block_usage");

  @Nullable
  @Override
  public INBT writeNBT(Capability<BlockUsage> capability, BlockUsage instance, Direction side) {
    return null;
  }

  @Override
  public void readNBT(
      Capability<BlockUsage> capability, BlockUsage instance, Direction side, INBT nbt) {
    // Dummy class
  }
}
