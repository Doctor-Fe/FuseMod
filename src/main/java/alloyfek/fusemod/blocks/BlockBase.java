package alloyfek.fusemod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

public abstract class BlockBase extends Block {

    public BlockBase(String modID, String name, Material materialIn) {
        super(materialIn);
        setRegistryName(modID, name);
        setUnlocalizedName(name);
    }
}
