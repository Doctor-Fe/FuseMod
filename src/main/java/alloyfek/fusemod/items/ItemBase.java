package alloyfek.fusemod.items;

import net.minecraft.item.Item;

public abstract class ItemBase extends Item{
    public ItemBase(String modID, String registryName, String unlocalizedName)
    {
        setRegistryName(modID, registryName);
        setUnlocalizedName(unlocalizedName);
    }

    public ItemBase(String modId, String name)
    {
        this(modId, name, name);
    }
}
