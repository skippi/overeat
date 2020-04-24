package io.skippi.overeat;

import java.util.Optional;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class BlockUsageHandler {
  @SubscribeEvent
  public void attachBlockUsage(final AttachCapabilitiesEvent<Entity> event) {
    if (!(event.getObject() instanceof PlayerEntity)) return;

    event.addCapability(BlockUsageStorage.RESOURCE, new BlockUsageProvider());
  }

  @SubscribeEvent
  public void onBlockRightClick(final PlayerInteractEvent.RightClickBlock event) {
    final BlockPos blockPos = event.getPos();
    final World world = event.getWorld();
    final PlayerEntity player = event.getPlayer();

    player
        .getCapability(BlockUsageProvider.CAPABILITY)
        .ifPresent(
            usage -> {
              usage.currentBlockInteraction = Optional.of(world.getBlockState(blockPos));
            });
  }

  @SubscribeEvent
  public void onItemRightClick(final PlayerInteractEvent.RightClickItem event) {
    final BlockPos blockPos = event.getPos();
    final World world = event.getWorld();
    final PlayerEntity player = event.getPlayer();

    player
        .getCapability(BlockUsageProvider.CAPABILITY)
        .ifPresent(
            usage -> {
              usage.currentBlockInteraction = Optional.empty();
            });
  }
}
