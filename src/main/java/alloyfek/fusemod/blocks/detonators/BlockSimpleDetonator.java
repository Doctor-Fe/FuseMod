package alloyfek.fusemod.blocks.detonators;

import javax.annotation.Nullable;

import alloyfek.fusemod.FuseMod;
import alloyfek.fusemod.blocks.IIgnitable;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockSimpleDetonator extends BlockHorizontal implements IIgnitable {

    // public static final PropertyBool IGNITED = PropertyBool.create("ignited");

    private static final AxisAlignedBB AA_BB = new AxisAlignedBB(0, 0, 0, 1, 0.125, 1);

    public BlockSimpleDetonator() {
        super(Material.IRON);
        setRegistryName(FuseMod.MODID, "simple_detonator");
        setUnlocalizedName("simple_detonator");
        setDefaultState(blockState.getBaseState().withRotation(Rotation.NONE));
    }

    @Override
    @Nullable
    public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
        return AA_BB;
    }
    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        return AA_BB;
    }

    @Override
    public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn,
            EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        EnumFacing connectable = state.getValue(FACING).getOpposite();
        IBlockState s = worldIn.getBlockState(pos.offset(connectable));
        if (s.getBlock() instanceof IIgnitable) {
            IIgnitable i = (IIgnitable)s.getBlock();
            if (i.canConnectTo(worldIn, pos, connectable)) {
                i.ignit(worldIn, pos.offset(connectable));
            }
        }
        return true;
    }

    @Override
    public void ignit(World worldIn, BlockPos pos) {}

    @Override
    public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
        IBlockState detonator = worldIn.getBlockState(pos.offset(facing));
        return detonator.getValue(FACING).equals(facing);
    }
    
    @Override
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
            EnumFacing side) {
        return side != EnumFacing.DOWN;
    }

    @Override
    public IBlockState withRotation(IBlockState state, Rotation rot) {
        return state.withProperty(FACING, rot.rotate((EnumFacing)state.getValue(FACING)));
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return this.getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { FACING });
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        EnumFacing facing = state.getValue(FACING);
        switch (facing) {
            case NORTH:
                return 1;
            case SOUTH:
                return 2;
            case WEST:
                return 3;
            default:
                return 0;
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        switch (meta) {
            case 1:
                return getDefaultState().withProperty(FACING, EnumFacing.NORTH);
            case 2:
                return getDefaultState().withProperty(FACING, EnumFacing.SOUTH);
            case 3:
                return getDefaultState().withProperty(FACING, EnumFacing.WEST);
            default:
                return getDefaultState().withProperty(FACING, EnumFacing.EAST);
        }
    }
}
