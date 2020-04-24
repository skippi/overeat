package io.skippi.overeat;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod("overeat")
public class OverEatMod {
  private static final Logger LOGGER = LogManager.getLogger();

  public OverEatMod() {
    MinecraftForge.EVENT_BUS.register(this);
  }
}
