package alloyfek.fusemod.blocks;

import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public interface IIgnitable {
    /** 着火時の処理
     * @param worldIn ワールドのインスタンス
     * @param pos 着火先の座標
     */
    public void ignit(World worldIn, BlockPos pos);

    /** 接続可能か取得する関数
     * @param worldIn ワールドのインスタンス
     * @param pos 接続元の座標
     * @param facing 接続元から見た方向
     * @return
     */
    public boolean canConnectTo(IBlockAccess worldIn, BlockPos pos, EnumFacing facing);
}
