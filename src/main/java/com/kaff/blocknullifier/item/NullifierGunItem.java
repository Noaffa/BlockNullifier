package com.kaff.blocknullifier.item;

import com.kaff.blocknullifier.init.SoundsInit;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

public class NullifierGunItem extends Item {
    public NullifierGunItem(Properties p_41383_) {
        super(p_41383_);
    }

    Vec3 V3Min(Vec3 a, Vec3 b){
        return new Vec3(Math.min(a.x,b.x),
                Math.min(a.y,b.y),
                Math.min(a.z,b.z));
    }
    Vec3 V3Max(Vec3 a,Vec3 b){
        return new Vec3(Math.max(a.x,b.x),
                Math.max(a.y,b.y),
                Math.max(a.z,b.z));
    }

    boolean PointCheck(Vec3 pointA, Vec3 pointB,float width, Vec3 checkPT)
    {
        var dirA2B = pointB.subtract(pointA);
        var dotA2B = dirA2B.dot(dirA2B);
        var t = (checkPT.subtract(pointA)).dot(dirA2B) / dotA2B;
        var P_proj = pointA.add(dirA2B.multiply(t,t,t));
        if (t < 0 || t > 1) return false;
        var d = checkPT.distanceTo(P_proj);
        return d <= width;
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player player, InteractionHand hand) {
        var item = player.getItemInHand(hand);
        var radius = 3;
        var length = 50;
        if(!world.isClientSide){
            var lookDir = player.getLookAngle().normalize();
            //player.sendSystemMessage( Component.literal("执行了"+":"+new Random().nextInt(0, 3)));
            var start = player.getEyePosition().add(lookDir);
            var end = start.add(lookDir.multiply(length,length,length));
            world.playSound(null, BlockPos.containing(start), SoundsInit.triggerSound.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

            var min = V3Min(start, end).subtract(new Vec3(radius, radius, radius));
            var max = V3Max(start, end).add(new Vec3(radius, radius, radius));
            var box = new AABB(min.x,min.y,min.z,max.x,max.y,max.z);
            List<Entity> entities = world.getEntitiesOfClass(Entity.class,box);
            for (Entity entity : entities) {
                if (entity==player) continue;
                if (PointCheck(start,end,radius,entity.position())) {
                    player.sendSystemMessage( Component.literal("杀死了"+":"+entity.getScoreboardName()));
                    entity.remove(Entity.RemovalReason.KILLED);
                }
            }

            for (int x = (int)Math.floor(min.x); x <= (int)Math.ceil(max.x); x++)
            {
                for (int y = (int)Math.floor(min.y); y <= (int)Math.ceil(max.y); y++)
                {
                    for (int z = (int)Math.floor(min.z); z <= (int)Math.ceil(max.z); z++)
                    {
                        if(world.getBlockState(BlockPos.containing(x, y, z)).isAir())continue;
                        if(PointCheck(start,end,radius,new Vec3(x, y, z)))
                        {
                            world.setBlock(BlockPos.containing(x, y, z), Blocks.AIR.defaultBlockState(),3);
                            var serverWorld =((ServerLevel)world);
                            serverWorld.sendParticles(ParticleTypes.SMOKE, x, y, z, 10, 0.1, 0.1, 0.1, 0.02);
                        }
                    }
                }
            }
            RegistryObject<SoundEvent> sound = switch (world.random.nextInt(0,3)) {
                default -> SoundsInit.shoot0Sound;
                case 1 -> SoundsInit.shoot1Sound;
                case 2 -> SoundsInit.shoot2Sound;
            };
            world.playSound(null, BlockPos.containing(start), sound.get(), SoundSource.PLAYERS, 1.0F, 1.0F);

            return InteractionResultHolder.success(item);
        }
        return InteractionResultHolder.pass(item);
    }
}
