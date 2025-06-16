package net.nocruz.tinkers_iceandfire.items.modifiers.traits;

import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
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

public class InfernoModifier extends NoLevelsModifier implements MeleeHitModifierHook {

    @Override
    protected void registerHooks(ModuleHookMap.Builder hookBuilder) {
        hookBuilder.addHook(this, ModifierHooks.MELEE_HIT);
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        burn(context.getAttacker(), context.getLivingTarget());
    }

    private void burn(LivingEntity attacker, LivingEntity target) {
        if (target == null) return;

        if (target instanceof EntityIceDragon)
            target.hurt(attacker.level().damageSources().inFire(), 13.5F);

        // Exact formula for the burning that Ice and Fire uses
        target.setSecondsOnFire(15);
        target.knockback((double)1.0F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
    }
}
