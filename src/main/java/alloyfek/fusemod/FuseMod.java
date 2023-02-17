package alloyfek.fusemod;

import org.apache.logging.log4j.Logger;

import alloyfek.fusemod.proxy.CommonProxy;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid = FuseMod.MODID, name = FuseMod.NAME, version = FuseMod.VERSION)
public class FuseMod {
    public static final String MODID = "fusemod";
    public static final String NAME = "Modding Test";
    public static final String VERSION = "0.2.0";

    public static Logger logger;

    @SidedProxy(clientSide = "alloyfek.fusemod.proxy.ClientProxy", serverSide = "alloyfek.fusemod.proxy.CommonProxy")
    public static CommonProxy proxy;

    @Instance(FuseMod.MODID)
    public static FuseMod fusemod;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        logger = event.getModLog();
        logger.info("FuseMod.preInit was called.");
        proxy.registerPre();
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.register();
    }
}
