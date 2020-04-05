package io.skippi.overeat;

import net.minecraft.item.Food;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
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

    // MinecraftForge.EVENT_BUS.addListener(this::serverStarting);
    MinecraftForge.EVENT_BUS.register(this);
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

      LOGGER.info(String.format("%s: %s", item.getRegistryName(), food.canEatWhenFull()));
    }
  }
}
