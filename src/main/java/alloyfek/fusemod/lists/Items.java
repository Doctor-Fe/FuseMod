package alloyfek.fusemod.lists;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

public class Items {
	public static final Item FUSE = getItemFromBlock(Blocks.FUSE);
	public static final Item GUNPOWDER = getItemFromBlock(Blocks.GUNPOWDER);

	private static Item getItemFromBlock(Block block)
	{
		return new ItemBlock(block).setRegistryName(block.getRegistryName()).setUnlocalizedName(block.getUnlocalizedName());
	}

	private static Item getItemFromName(String modID, String name)
	{
		return new Item().setRegistryName(modID, name).setUnlocalizedName(name);
	}

	public static void register()
	{
		ForgeRegistries.ITEMS.registerAll(FUSE, GUNPOWDER);
	}
}