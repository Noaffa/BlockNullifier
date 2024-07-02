package com.kaff.blocknullifier.init;

import com.kaff.blocknullifier.BlockNullifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class SoundsInit {

    public static final DeferredRegister<SoundEvent> SoundRegister = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, BlockNullifier.MODID);
    public static RegistryObject<SoundEvent> triggerSound = SoundRegister.register("triggersound", () ->SoundEvent.createFixedRangeEvent(new ResourceLocation(BlockNullifier.MODID,"trigger"),50));
    public static RegistryObject<SoundEvent> shoot0Sound = SoundRegister.register("shootsound0", () ->SoundEvent.createFixedRangeEvent(new ResourceLocation(BlockNullifier.MODID,"shoot0"),50));
    public static RegistryObject<SoundEvent> shoot1Sound = SoundRegister.register("shootsound1", () ->SoundEvent.createFixedRangeEvent(new ResourceLocation(BlockNullifier.MODID,"shoot1"),50));
    public static RegistryObject<SoundEvent> shoot2Sound = SoundRegister.register("shootsound2", () ->SoundEvent.createFixedRangeEvent(new ResourceLocation(BlockNullifier.MODID,"shoot2"),50));
}
