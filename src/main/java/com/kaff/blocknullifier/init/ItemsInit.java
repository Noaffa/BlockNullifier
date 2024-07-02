package com.kaff.blocknullifier.init;

import com.kaff.blocknullifier.BlockNullifier;
import com.kaff.blocknullifier.item.NullifierGunItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemsInit {
    public static final DeferredRegister<Item> ItemRegister = DeferredRegister.create(ForgeRegistries.ITEMS, BlockNullifier.MODID);
    public static final RegistryObject<Item> TheGun_Item = ItemRegister.register("nullifiergun",()->new NullifierGunItem(new Item.Properties()));
}
