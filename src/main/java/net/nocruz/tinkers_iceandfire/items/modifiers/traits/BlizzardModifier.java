package net.nocruz.tinkers_iceandfire.items.modifiers.traits;

import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.impl.NoLevelsModifier;
import slimeknights.tconstruct.library.module.ModuleHookMap;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

public class BlizzardModifier extends NoLevelsModifier implements MeleeHitModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        freeze(context.getAttacker(), context.getLivingTarget());
    }

    private void freeze(LivingEntity attacker, LivingEntity target) {
        if (target == null) return;

        if (target instanceof EntityFireDragon)
            target.hurt(attacker.level().damageSources().drown(), 13.5F);

        // Exact formula for the freezing that Ice and Fire uses
        EntityDataProvider.getCapability(target).ifPresent((data) -> data.frozenData.setFrozen(target, 300));
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 300, 2));
        target.knockback((double)1.0F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
    }
}
