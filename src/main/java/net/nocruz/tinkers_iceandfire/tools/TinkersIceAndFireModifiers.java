package net.nocruz.tinkers_iceandfire.tools;

import net.minecraft.core.registries.Registries;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.RegisterEvent;
import net.nocruz.tinkers_iceandfire.TinkersIceAndFire;
import net.nocruz.tinkers_iceandfire.tools.modules.combat.BurnModule;
import net.nocruz.tinkers_iceandfire.tools.modules.combat.FreezeModule;
import net.nocruz.tinkers_iceandfire.tools.modules.combat.SummonLightningModule;
import slimeknights.mantle.data.loadable.record.RecordLoadable;
import slimeknights.tconstruct.library.modifiers.modules.ModifierModule;
import slimeknights.tconstruct.library.modifiers.util.ModifierDeferredRegister;

import static net.nocruz.tinkers_iceandfire.TinkersIceAndFire.getResource;

public class TinkersIceAndFireModifiers
{
    private static final ModifierDeferredRegister modifiers = ModifierDeferredRegister.create(TinkersIceAndFire.MOD_ID);

    public TinkersIceAndFireModifiers(IEventBus bus)
    {
        modifiers.register(bus);
    }

    @SubscribeEvent
    @SuppressWarnings("unchecked")
    void registerSerializers(RegisterEvent event) {
        if (event.getRegistryKey() == Registries.RECIPE_SERIALIZER) {
            ModifierModule.LOADER.register(getResource("freeze"), FreezeModule.LOADER);
            ModifierModule.LOADER.register(getResource("burn"), BurnModule.LOADER);
            ModifierModule.LOADER.register(getResource("summon_lightning"), (RecordLoadable<? extends ModifierModule>) SummonLightningModule.INSTANCE.getLoader());
        }
    }
}
