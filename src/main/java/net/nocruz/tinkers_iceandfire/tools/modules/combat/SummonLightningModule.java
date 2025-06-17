package net.nocruz.tinkers_iceandfire.tools.modules.combat;

import com.github.alexthe666.iceandfire.entity.EntityFireDragon;
import com.github.alexthe666.iceandfire.entity.EntityIceDragon;
import com.github.alexthe666.iceandfire.event.ServerEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.mantle.data.loadable.record.SingletonLoader;
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

public enum SummonLightningModule implements ModifierModule, MeleeHitModifierHook {
    INSTANCE;

    private static final List<ModuleHook<?>> DEFAULT_HOOKS = HookProvider.<SummonLightningModule>defaultHooks(ModifierHooks.MELEE_HIT);
    private final SingletonLoader<SummonLightningModule> LOADER = new SingletonLoader<>(this);

    @Override
    public @NotNull RecordLoadable<? extends IHaveLoader> getLoader() {
        return LOADER;
    }

    @Override
    public @NotNull List<ModuleHook<?>> getDefaultHooks() {
        return DEFAULT_HOOKS;
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
    }

    @Override
    public void afterMeleeHit(IToolStackView tool, ModifierEntry modifier, ToolAttackContext context, float damageDealt) {
        smite(context.getAttacker(), context.getLivingTarget());
    }
}
