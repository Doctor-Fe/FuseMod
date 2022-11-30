package alloyfek.fusemod.register;

import alloyfek.fusemod.events.EventEntity;
import net.minecraftforge.common.MinecraftForge;

public class EventRegister {
    public static void register() {
        MinecraftForge.EVENT_BUS.register(new EventEntity());
    }
}