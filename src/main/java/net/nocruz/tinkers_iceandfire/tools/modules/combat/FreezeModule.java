package net.nocruz.tinkers_iceandfire.tools.modules.combat;

import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.props.EntityDataProvider;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.primitive.IntLoadable;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.registry.GenericLoaderRegistry.IHaveLoader;
import slimeknights.tconstruct.library.modifiers.ModifierEntry;
import slimeknights.tconstruct.library.modifiers.ModifierHooks;
import slimeknights.tconstruct.library.modifiers.hook.combat.MeleeHitModifierHook;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.module.HookProvider;
import slimeknights.tconstruct.library.module.ModuleHook;
import slimeknights.tconstruct.library.tools.context.ToolAttackContext;
import slimeknights.tconstruct.library.tools.nbt.IToolStackView;

import java.util.List;

public record FreezeModule(int time, int slowness) implements ModifierModule, MeleeHitModifierHook {
    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider.<FreezeModule>defaultHooks(ModifierHooks.MELEE_HIT);
    public static final RecordLoadable<FreezeModule> LOADER = RecordLoadable.create(
            IntLoadable.FROM_ZERO.requiredField("seconds", FreezeModule::time),
            IntLoadable.FROM_ONE.requiredField("slowness", FreezeModule::slowness),
            FreezeModule::new);

    @Override
    public @NotNull RecordLoadable<? extends IHaveLoader> getLoader() {
        return LOADER;
    }

    @Override
    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    private void freeze(LivingEntity attacker, LivingEntity target, ModifierEntry modifier) {
        if (target == null) return;

        if (target instanceof EntityFireDragon)
            target.hurt(attacker.level().damageSources().drown(), 13.5F);

        // Exact formula for the freezing that Ice and Fire uses
        EntityDataProvider.getCapability(target).ifPresent((data) -> data.frozenData.setFrozen(target, 300));
        target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, time * 20, Math.max(0, Math.min(4, slowness - 1))));
        target.knockback((double)1.0F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        freeze(context.getAttacker(), context.getLivingTarget(), modifier);
    }
}
