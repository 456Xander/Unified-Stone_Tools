package at.xander.unifiedstone;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.ShapedOreRecipe;

@Mod(name = "Unified Stone Tools", modid = "unifiedstone", version = "1.0")
public class UnifiedStoneTools {
	private MyCfg cfg;
	@Mod.Instance
	public static UnifiedStoneTools instance;

	@Mod.EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		File cfgFile = e.getSuggestedConfigurationFile();
		this.cfg = new MyCfg(cfgFile);
	}

	@Mod.EventHandler
	public void init(FMLInitializationEvent e) {
		try {
			this.cfg.createCfgIfNotPresent();
			// Register OreDict
			this.cfg.readConfig();
		} catch (IOException ex) {
			System.err.println(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
