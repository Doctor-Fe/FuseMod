package alloyfek.fusemod.blocks;

import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import alloyfek.fusemod.FuseMod;
import net.minecraft.block.Block;
import net.minecraft.block.BlockTNT;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockFuse extends BlockBase implements IIgnitable {

    public static final PropertyBool BOTTOM = PropertyBool.create("bottom");
    public static final PropertyBool TOP = PropertyBool.create("top");
    public static final PropertyBool NORTH = PropertyBool.create("north");
    public static final PropertyBool SOUTH = PropertyBool.create("south");
    public static final PropertyBool WEST = PropertyBool.create("west");
    public static final PropertyBool EAST = PropertyBool.create("east");
    public static final PropertyBool IGNITED = PropertyBool.create("ignited");
    public static final PropertyBool[] FACING_PROPERTIES = { BOTTOM, TOP, NORTH, SOUTH, WEST, EAST };

    private static final AxisAlignedBB[] AA_BB = {
            new AxisAlignedBB(0.375, 0, 0.375, 0.625, 0.375, 0.625), // BOTTOM
            new AxisAlignedBB(0.375, 0.625, 0.375, 0.625, 1, 0.625), // TOP
            new AxisAlignedBB(0.375, 0.375, 0, 0.625, 0.625, 0.375), // NORTH
            new AxisAlignedBB(0.375, 0.375, 0.625, 0.625, 0.625, 1), // SOUTH
            new AxisAlignedBB(0, 0.375, 0.375, 0.375, 0.625, 0.625), // WEST
            new AxisAlignedBB(0.625, 0.375, 0.375, 1, 0.625, 0.625), // EAST
    };

    private static final AxisAlignedBB CENTER = new AxisAlignedBB(0.375, 0.375, 0.375, 0.625, 0.625, 0.625);

    public BlockFuse() {
        super(FuseMod.MODID, "fuse", Material.CIRCUITS);
        setUnlocalizedName("fuse");
        setDefaultState(blockState.getBaseState()
            .withProperty(NORTH, false)
            .withProperty(SOUTH, false)
            .withProperty(EAST, false)
            .withProperty(WEST, false)
            .withProperty(TOP, false)
            .withProperty(BOTTOM, false)
            .withProperty(IGNITED, false)
        );
    }

    // ---- ブロックの接触判定に関する処理 ----

    @Override
    public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
        state = state.getActualState(source, pos);
        return new AxisAlignedBB(
            state.getValue(WEST) ? 0 : 0.375,
            state.getValue(BOTTOM) ? 0 : 0.375,
            state.getValue(NORTH) ? 0 : 0.375,
            state.getValue(EAST) ? 1 : 0.625,
            state.getValue(TOP) ? 1 : 0.625,
            state.getValue(SOUTH) ? 1 : 0.625
        );
    }

    @Override
    public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState)
    {
        if (entityBox.intersects(CENTER)) {
            collidingBoxes.add(CENTER);
        }

        if (!isActualState)
        {
            state = this.getActualState(state, worldIn, pos);
        }

        for (int i = 0; i < 6; i++)
        {
            if (state.getValue(FACING_PROPERTIES[i])) {
                AxisAlignedBB axisalignedbb = AA_BB[i].offset(pos);
    
                if (entityBox.intersects(axisalignedbb))
                {
                    collidingBoxes.add(axisalignedbb);
                }
            }
        }
    }

    // ---- ブロックの見た目に関する処理 ----

    @Override
    @Deprecated
    public boolean isOpaqueCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean isFullCube(IBlockState state) {
        return false;
    }

    @Override
    @Deprecated
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos,
            EnumFacing side) {
        return true;
    }

    // ---- ブロックの動作に関する処理 ----

    @Override
    @Deprecated
    public IBlockState getActualState(IBlockState state, IBlockAccess worldIn, BlockPos pos) {
        return state
            .withProperty(BOTTOM, isConnectedTo(worldIn, pos, EnumFacing.DOWN))
            .withProperty(TOP, isConnectedTo(worldIn, pos, EnumFacing.UP))
            .withProperty(NORTH, isConnectedTo(worldIn, pos, EnumFacing.NORTH))
            .withProperty(SOUTH, isConnectedTo(worldIn, pos, EnumFacing.SOUTH))
            .withProperty(WEST, isConnectedTo(worldIn, pos, EnumFacing.WEST))
            .withProperty(EAST, isConnectedTo(worldIn, pos, EnumFacing.EAST));
    }

    // ---- ブロックの更新時・追加時の処理 ----

    @Override
    public void updateTick(World worldIn, BlockPos pos, IBlockState state, Random rand) {
        if (state.getValue(IGNITED)) {
            for (int i = 0; i < 6; i++) {
                BlockPos pos2 = pos.offset(EnumFacing.getFront(i));
                IBlockState state2 = worldIn.getBlockState(pos2);
                Block block = state2.getBlock();
                if (block instanceof BlockFuse) {
                    ((BlockFuse)block).beginPropagation(worldIn, pos2);
                } else if (isConnectedTo(worldIn, pos, EnumFacing.getFront(i))) {
                    if (block instanceof IIgnitable) {
                        ((IIgnitable)block).ignit(worldIn, pos2);
                    } else if (block instanceof BlockTNT) {
                        worldIn.setBlockToAir(pos2);
                        BlockTNT tnt = (BlockTNT)block;
                        tnt.explode(worldIn, pos2, state2.withProperty(BlockTNT.EXPLODE, true), null);
                    }
                }
            }
            worldIn.setBlockToAir(pos);
        } else {
            updateConnection(worldIn, pos, state);
        }
    }

    @Override
    public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
        updateConnection(worldIn, pos, state);
    }

    @Override
    public void observedNeighborChange(IBlockState observerState, World world, BlockPos observerPos, Block changedBlock, BlockPos changedBlockPos) {
        updateConnection((World) world, observerPos, observerState);
    }

    protected void updateConnection(World worldIn, BlockPos pos, IBlockState state) {
        boolean[] data = new boolean[6];
        for (EnumFacing facing : EnumFacing.values()) {
            data[facing.getIndex()] = worldIn.getBlockState(pos.offset(facing)).getBlock() instanceof BlockFuse;
        }
        worldIn.setBlockState(pos, state
                .withProperty(NORTH, data[2])
                .withProperty(SOUTH, data[3])
                .withProperty(EAST, data[5])
                .withProperty(WEST, data[4])
                .withProperty(TOP, data[1])
                .withProperty(BOTTOM, data[0]));
    }

    public void beginPropagation(World worldIn, BlockPos pos) {
        worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(IGNITED, true));
        worldIn.scheduleUpdate(pos, this, 2);
    }

    // ---- BlockStateの管理に関する処理 ----

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[] { NORTH, SOUTH, EAST, WEST, TOP, BOTTOM, IGNITED });
    }

    @Override
    @Deprecated
    public IBlockState getStateFromMeta(int meta) {
        return this.blockState.getBaseState()
            .withProperty(IGNITED, (meta & 1) == 1);
    }

    @Override
    @Deprecated
    public int getMetaFromState(IBlockState state) {
        return (state.getValue(IGNITED) ? 1 : 0);
    }

    protected boolean isConnectedTo(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
        Block block = worldIn.getBlockState(pos.offset(facing)).getBlock();
        if (block instanceof IIgnitable) {
            return ((IIgnitable)block).canConnectTo(worldIn, pos, facing);
        } else if (block instanceof BlockTNT) {
            return true;
        } else {
            return false;
        }
    }

    // ---- IIgnitable の実装 ----

    @Override
    public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos, EnumFacing facing) {
        return true;
    }

    @Override
    public void ignit(World worldIn, BlockPos pos) {
        worldIn.playSound(null, pos, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 1, 1);
        worldIn.setBlockState(pos, worldIn.getBlockState(pos).withProperty(IGNITED, true));
        worldIn.scheduleUpdate(pos, this, 1);
    }
}
