package alloyfek.fusemod.blocks;

import javax.annotation.Nullable;

import alloyfek.fusemod.FuseMod;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockGunpowder extends BlockBase implements IIgnitable {

    private int depth;

    public BlockGunpowder() {
        super(FuseMod.MODID, "gunpowder_block", Material.SAND);
        this.depth = 0;
    }

    @Override
    public void onBlockDestroyedByExplosion(World worldIn, BlockPos pos, Explosion explosionIn) {
        depth++;
        if (depth <= 64)
        {
            explode(worldIn, pos, null);
        } else {
            depth = 0;
        }
    }

    protected void explode(World world, BlockPos pos, @Nullable Entity entity)
    {
        if (!world.isRemote)
        {
            world.newExplosion(entity, pos.getX(), pos.getY(), pos.getZ(), 4, false, true);
        }
    }

    @Override
    public void ignit(World worldIn, BlockPos pos) {
        explode(worldIn, pos, null);
    }

    @Override
    public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
        return true;
    }
}
