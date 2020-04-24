package io.skippi.overeat;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("overeat")
public class OverEatMod {
  private static final Logger LOGGER = LogManager.getLogger();

  public OverEatMod() {
    ModLoadingContext.get().registerConfig(ModConfig.Type.SERVER, getServerSpec());

    MinecraftForge.EVENT_BUS.register(this);
    MinecraftForge.EVENT_BUS.addListener(this::onItemRightClick);
  }

  private void onItemRightClick(final PlayerInteractEvent.RightClickItem event) {
    final ItemStack itemStack = event.getItemStack();
    if (!itemStack.isFood()) return;

    final PlayerEntity player = event.getPlayer();

    if (isBlackListed(itemStack.getItem()) && !player.getFoodStats().needFood()) {
      event.setCanceled(true);
    }
  }

  private boolean isBlackListed(final Item item) {
    final ResourceLocation itemId = item.getRegistryName();
    if (itemId == null) return true;

    final String blackList = getServerConfig().blackList.get();
    final String[] separated = blackList.split(",");
    final Set<String> trimmed =
        Arrays.stream(separated).map(String::trim).collect(Collectors.toSet());

    return trimmed.contains(itemId.toString());
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
