package alloyfek.fusemod.events;

import alloyfek.fusemod.blocks.IIgnitable;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.ProjectileImpactEvent.Arrow;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;

public class EventEntity {
    @SubscribeEvent
    public void onProjectileImpact(Arrow arrow)
    {
        EntityArrow arrow2 = arrow.getArrow();
        RayTraceResult result = arrow.getRayTraceResult();
        if (arrow.getArrow().isBurning() && !arrow2.world.isRemote && result.entityHit == null)
        {
            BlockPos blockpos = result.getBlockPos().offset(result.sideHit);

            if (arrow2.world.isAirBlock(blockpos))
            {
                arrow2.world.setBlockState(blockpos, Blocks.FIRE.getDefaultState());
            }
        }
        arrow.setResult(Result.DEFAULT);
    }

    @SubscribeEvent
    public void onEntityDeath(LivingDeathEvent event)
    {
        EntityLivingBase living = event.getEntityLiving();
        if (!living.world.isRemote && living instanceof EntityGhast)
        {
            EntityGhast ghast = (EntityGhast)event.getEntityLiving();
            ghast.world.newExplosion(ghast, ghast.posX, ghast.posY, ghast.posZ, 4.0F, true, true);
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onRightClickBlockEvent(RightClickBlock clickItem) {
        EntityPlayer player = clickItem.getEntityPlayer();
        World world = clickItem.getWorld();
        ItemStack stack1 = clickItem.getItemStack();
        BlockPos pos = clickItem.getPos();
        ResourceLocation name1 = stack1.getItem().getRegistryName();
        Block block = world.getBlockState(pos).getBlock();

        if (name1 == null) return;

        if (name1.equals(new ResourceLocation("minecraft:flint_and_steel")) && block instanceof IIgnitable) {
            ((IIgnitable)block).ignit(world, pos);
            stack1.damageItem(1, player);
            player.swingArm(clickItem.getHand());
            player.playSound(SoundEvents.ITEM_FLINTANDSTEEL_USE, 1, 1);
            clickItem.setCanceled(true);
            clickItem.setResult(Result.ALLOW);
        }
        
    }
}
