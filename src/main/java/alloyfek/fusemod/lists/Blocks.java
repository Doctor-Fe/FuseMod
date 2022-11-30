package alloyfek.fusemod.lists;

import alloyfek.fusemod.blocks.BlockFuse;
import alloyfek.fusemod.blocks.BlockGunpowder;
import net.minecraft.block.Block;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Blocks {
	public static final Block FUSE = new BlockFuse();
	public static final Block GUNPOWDER = new BlockGunpowder();

	public static void register() {
		ForgeRegistries.BLOCKS.registerAll(FUSE, GUNPOWDER);
	}
}