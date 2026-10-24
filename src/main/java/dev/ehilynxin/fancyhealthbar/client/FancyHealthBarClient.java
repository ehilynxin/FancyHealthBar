package dev.ehilynxin.fancyhealthbar.client;

import dev.ehilynxin.fancyhealthbar.client.accessor.CompatAccessor;
import dev.ehilynxin.fancyhealthbar.client.compat.CompatConstants;
import dev.ehilynxin.fancyhealthbar.client.compat.CompatKey;
import dev.ehilynxin.fancyhealthbar.client.mixin.GuiMixin;
import net.fabricmc.api.ClientModInitializer;
//?>=1.21.10
//import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
//?>=1.21.10
//import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
//?<1.21.10
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class FancyHealthBarClient implements ClientModInitializer {

    public static final String MOD_ID = "fancyhealthbar";

    public static CompatKey id(String path) {
        return CompatKey.of(MOD_ID, path);
    }

    public static CompatKey vanillaId(String path){
         return CompatKey.minecraft(path);
    }

    @Override
    public void onInitializeClient() {

        if(FabricLoader.getInstance().isModLoaded("overflowingbars")){
            CompatConstants.noVanillaOverflow = true;
            CompatConstants.offsetWarning = Component.translatable("warning.fancyhealthbar.overflowingbars");

            //?>=1.21.10{
            /*HudElementRegistry.attachElementAfter(VanillaHudElements.HEALTH_BAR,id("animation").unwrap(),(guiGraphicsExtractor, deltaTracker) -> {
                CompatAccessor mixin = (CompatAccessor) Minecraft.getInstance().gui/^?>=26.2{^//^.hud^//^?}^/;
                mixin.fhb$drawExternal(guiGraphicsExtractor);
            });
            *///?}else{
            HudRenderCallback.EVENT.register((guiGraphics, v) -> {
                CompatAccessor accessor = (CompatAccessor) Minecraft.getInstance().gui;
                accessor.fhb$drawExternal(guiGraphics);
            });
            //?}
        }

    }

}