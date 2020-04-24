package io.skippi.overeat;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;

public class BlockUsageProvider implements ICapabilityProvider {
  @CapabilityInject(BlockUsage.class)
  public static Capability<BlockUsage> CAPABILITY;

  private BlockUsage instance = CAPABILITY.getDefaultInstance();

  @Nonnull
  @Override
  public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
    return CAPABILITY.orEmpty(cap, LazyOptional.of(() -> instance));
  }
}
