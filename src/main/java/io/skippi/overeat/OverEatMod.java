package io.skippi.overeat;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("overeat")
public class OverEatMod {
  private static final Logger LOGGER = LogManager.getLogger();

  public OverEatMod() {
    FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);

    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, getServerSpec());

    MinecraftForge.EVENT_BUS.register(this);
  }

  private void setup(final FMLCommonSetupEvent event) {
    CapabilityManager.INSTANCE.register(BlockUsage.class, new BlockUsageStorage(), BlockUsage::new);
  }

  public static boolean canEatWithConfig(final PlayerEntity player) {
    final ResourceLocation itemId = player.getHeldItemMainhand().getItem().getRegistryName();
    if (itemId == null) return true;

    final Set<String> blackList = parseCsvBag(getServerConfig().blackList.get());
    final boolean foodIsBlackListed = blackList.contains(itemId.toString());

    return player.abilities.disableDamage || !foodIsBlackListed || player.getFoodStats().needFood();
  }

  private static Set<String> parseCsvBag(final String str) {
    return Arrays.stream(str.split(",")).map(String::trim).collect(Collectors.toSet());
  }

  static class ServerConfig {
    public final ForgeConfigSpec.ConfigValue<String> blackList;

    ServerConfig(ForgeConfigSpec.Builder builder) {
      builder.comment("overeat configuration settings");

      blackList =
          builder
              .comment("Comma separated list of blocks/items that overeat shouldn't affect.")
              .translation("overeat.config.food")
              .worldRestart()
              .define("blackList", "");
    }
  }

  private static final Pair<ServerConfig, ForgeConfigSpec> serverSpecPair =
      new ForgeConfigSpec.Builder().configure(ServerConfig::new);

  public static ForgeConfigSpec getServerSpec() {
    return serverSpecPair.getRight();
  }

  public static ServerConfig getServerConfig() {
    return serverSpecPair.getLeft();
  }
}
