package alloyfek.fusemod.register;

import alloyfek.fusemod.lists.Items;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;

public class ModelRegister {
    public static void register() {
        register(Items.FUSE);
        register(Items.GUNPOWDER);
        register(Items.SIMPLE_DETONATOR);
    }

    protected static void register(Item item) {
        ModelLoader.setCustomModelResourceLocation(item, 0,
                new ModelResourceLocation(item.getRegistryName(), "inventory"));
    }
}
