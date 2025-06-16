package net.nocruz.tinkers_iceandfire.items;

import net.minecraftforge.eventbus.api.IEventBus;
import net.nocruz.tinkers_iceandfire.TinkersIceAndFire;
import net.nocruz.tinkers_iceandfire.items.modifiers.traits.BlizzardModifier;
import net.nocruz.tinkers_iceandfire.items.modifiers.traits.InfernoModifier;
import net.nocruz.tinkers_iceandfire.items.modifiers.traits.TempestModifier;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;
import slimeknights.tconstruct.library.modifiers.util.StaticModifier;

public class TinkersIceAndFireModifiers
{
    private static final ModifierDeferredRegister modifiers = ModifierDeferredRegister.create(TinkersIceAndFire.MOD_ID);

    public TinkersIceAndFireModifiers(IEventBus bus)
    {
        modifiers.register(bus);
    }

    public static final StaticModifier<BlizzardModifier> blizzard = modifiers.register("blizzard", BlizzardModifier::new);
    public static final StaticModifier<InfernoModifier> inferno = modifiers.register("inferno", InfernoModifier::new);
    public static final StaticModifier<TempestModifier> tempest = modifiers.register("tempest", TempestModifier::new);
}
