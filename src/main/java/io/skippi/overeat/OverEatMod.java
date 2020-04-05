package io.skippi.overeat;

import net.minecraft.block.BlockState;
import net.minecraft.block.CakeBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraft.stats.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("overeat")
public class OverEatMod {
  private static final Logger LOGGER = LogManager.getLogger();

  public OverEatMod() {
    final IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::setup);

    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.addListener(this::onCakeBlockInteract);
  }

  private void setup(final FMLCommonSetupEvent event) {
    LOGGER.info("OverEat::setup");

    for (Item item : ForgeRegistries.ITEMS) {
      if (!item.isFood()) {
        continue;
      }

      final Food food = item.getFood();
      final String canAlwaysEatFieldName = "field_221473_d";
      ObfuscationReflectionHelper.setPrivateValue(Food.class, food, true, canAlwaysEatFieldName);

      LOGGER.info("{}#canAlwaysEat = {}", item.getRegistryName(), food.canEatWhenFull());
    }
  }

  private void onCakeBlockInteract(final RightClickBlock event) {
    final IWorld world = event.getWorld();
    final BlockPos pos = event.getPos();
    final BlockState state = world.getBlockState(pos);
    if (!(state.getBlock() instanceof CakeBlock)) {
      return;
    }

    final PlayerEntity player = event.getPlayer();
    if (player.canEat(false)) {
      return;
    }

    player.swingArm(Hand.MAIN_HAND);

    player.addStat(Stats.EAT_CAKE_SLICE);
    player.getFoodStats().addStats(2, 0.1F);

    int i = state.get(CakeBlock.BITES);
    if (i < 6) {
      world.setBlockState(pos, state.with(CakeBlock.BITES, Integer.valueOf(i + 1)), 3);
    } else {
      world.removeBlock(pos, false);
    }
  }
}
