package alloyfek.fusemod.blocks;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IIgnitable {
    public void ignit(World worldIn, BlockPos pos);
    public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos, EnumFacing facing);
}
