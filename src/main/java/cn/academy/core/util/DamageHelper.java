/**
* Copyright (c) Lambda Innovation, 2013-2016
* This file is part of the AcademyCraft mod.
* https://github.com/LambdaInnovation/AcademyCraft
* Licensed under GPLv3, see project root for more information.
*/
package cn.academy.core.util;

import cn.academy.core.AcademyCraft;
import cn.academy.core.event.BlockDestroyEvent;
import cn.academy.core.event.ConfigModifyEvent;
import cn.lambdalib.annoreg.core.Registrant;
import cn.lambdalib.annoreg.mc.RegEventHandler;
import cn.lambdalib.annoreg.mc.RegEventHandler.Bus;
import cn.lambdalib.annoreg.mc.RegInitCallback;
import cn.lambdalib.util.generic.MathUtils;
import cn.lambdalib.util.mc.WorldUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.DamageSource;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

import java.util.List;

/**
 * @author WeAthFolD
 */
@Registrant
public class DamageHelper {
    
    // PROPERTIES
    public static boolean ATTACK_PLAYER;
    public static boolean DESTROY_BLOCKS;
    
    public static final IEntitySelector dmgSelector = new IEntitySelector() {

        @Override
        public boolean isEntityApplicable(Entity entity) {
            return !(entity instanceof EntityPlayer) || ATTACK_PLAYER;
        }
        
    };
    
    @RegInitCallback
    public static void init() {
        loadConfigProps();
        MinecraftForge.EVENT_BUS.register(new DamageHelper());
    }
    
    @SubscribeEvent
    public void onConfigModified(ConfigModifyEvent event) {
        loadConfigProps();
    }
    
    private static void loadConfigProps() {
        Configuration conf = AcademyCraft.config;
        
        ATTACK_PLAYER = conf.getBoolean("attackPlayer", "generic", true, "Whether the skills are effective on players.");
        DESTROY_BLOCKS = conf.getBoolean("destroyBlocks", "generic", true, "Whether the skills will destroy blocks in the world.");
    }
    
    /**
     * Apply a range attack on a specific point and range. The damage attenuates linearly. 
     * At the center, the damage is [damage], at the edge the damage is 0.
     * @param world
     * @param x
     * @param y
     * @param z
     * @param range
     * @param damage
     * @param dmgSrc
     * @param entitySelector
     */
    public static void applyRangeAttack(
        World world, double x, double y, double z, double range, 
        float damage, DamageSource dmgSrc, 
        IEntitySelector entitySelector) {
        List<Entity> list = WorldUtils.getEntities(world, x, y, z, range, entitySelector);
        for(Entity ent : list) {
            double dist = MathUtils.distance(x, y, z, ent.posX, ent.posY, ent.posZ);
            float factor = 1 - MathUtils.clampf(0, 1, (float) (dist / range));
            float appliedDamage = MathUtils.lerpf(0, damage, factor);
            attack(ent, dmgSrc, appliedDamage);
        }
    }

    @Deprecated
    public static void attack(Entity e, EntityPlayer player, float dmg) {
        attack(e, DamageSource.causePlayerDamage(player), dmg);
    }
    
    /**
     * A delegation to the normal attack function. if e is a player and the config doesn't allow PvP the attack will be ignored.
     *     ALL skills should use this approach when applying damage.
     *     TODO integrate to ability pipeline
     */
    @Deprecated
    public static void attack(Entity e, DamageSource src, float damage) {
        if(e instanceof EntityPlayer && !ATTACK_PLAYER)
            return;
        e.attackEntityFrom(src, damage);
    }
    
    @RegEventHandler(Bus.Forge)
    public static class Events {
        @SubscribeEvent
        public void onBlockDestroy(BlockDestroyEvent event) {
            if(!DESTROY_BLOCKS)
                event.setCanceled(true);
        }
    }
    
}
