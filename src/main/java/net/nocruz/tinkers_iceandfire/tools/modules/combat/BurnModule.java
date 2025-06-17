package net.nocruz.tinkers_iceandfire.tools.modules.combat;

import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
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

public record BurnModule(int time) implements ModifierModule, MeleeHitModifierHook {
    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider.<BurnModule>defaultHooks(ModifierHooks.MELEE_HIT);
    public static final RecordLoadable<BurnModule> LOADER = RecordLoadable.create(
            IntLoadable.FROM_ZERO.requiredField("seconds", BurnModule::time),
            BurnModule::new);

    @Override
    public @NotNull RecordLoadable<? extends IHaveLoader> getLoader() {
        return LOADER;
    }

    @Override
    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
    }

    private void burn(LivingEntity attacker, LivingEntity target) {
        if (target == null) return;

        if (target instanceof EntityIceDragon)
            target.hurt(attacker.level().damageSources().inFire(), 13.5F);

        // Exact formula for the burning that Ice and Fire uses
        target.setSecondsOnFire(time);
        target.knockback((double)1.0F, attacker.getX() - target.getX(), attacker.getZ() - target.getZ());
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        burn(context.getAttacker(), context.getLivingTarget());
    }
}
