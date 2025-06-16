package net.nocruz.tinkers_iceandfire.items.modifiers.traits;

import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class TempestModifier extends NoLevelsModifier implements MeleeHitModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        smite(context.getAttacker(), context.getLivingTarget());
    }

    private void smite(LivingEntity attacker, LivingEntity target) {
        if (target == null) return;

        if (target instanceof EntityFireDragon || target instanceof EntityIceDragon)
            target.hurt(attacker.level().damageSources().lightningBolt(), 9.5F);

        // Exact formula for smiting that Ice and Fire uses
        boolean flag = !(attacker instanceof Player) || !((double) attacker.attackAnim > 0.2);

        if (!attacker.level().isClientSide && flag) {
            LightningBolt lightningboltentity = (LightningBolt) EntityType.LIGHTNING_BOLT.create(target.level());
            lightningboltentity.getTags().add(ServerEvents.BOLT_DONT_DESTROY_LOOT);
            lightningboltentity.getTags().add(attacker.getStringUUID());
            lightningboltentity.moveTo(target.position());
            if (!target.level().isClientSide) {
                target.level().addFreshEntity(lightningboltentity);
            }
        }

        target.knockback((double)1.0F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
    }
}