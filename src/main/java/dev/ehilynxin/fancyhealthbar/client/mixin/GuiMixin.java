package dev.ehilynxin.fancyhealthbar.client.mixin;

//~if >=26.1 'GuiGraphics' -> 'GuiGraphicsExtractor' {

import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.ehilynxin.fancyhealthbar.client.accessor.CompatAccessor;
import dev.ehilynxin.fancyhealthbar.client.accessor.FhbOptionsAccessor;
import dev.ehilynxin.fancyhealthbar.client.accessor.InGameHudRenderingAccessor;
import dev.ehilynxin.fancyhealthbar.client.animation.AnimationController;
import dev.ehilynxin.fancyhealthbar.client.entity.HudEntityManager;
import dev.ehilynxin.fancyhealthbar.client.rendering.HealthBarRendering;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui./*?>=26.2{*//*Hud*//*?}else{*/Gui/*?}*/;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(/*?>=26.2{*//*Hud*//*?}else{*/Gui/*?}*/.class)
public abstract class GuiMixin implements InGameHudRenderingAccessor, CompatAccessor {

    @Shadow
    @Final
    private RandomSource random;

    @Shadow
    @Final
    private Minecraft minecraft;
    private int ticks;
    @Unique
    private final HudEntityManager manager = new HudEntityManager();
    @Unique
    private final AnimationController controller = new AnimationController();
    @Unique
    private HealthBarRendering rendering = ((FhbOptionsAccessor) Minecraft.getInstance()).fhb$getOptions().getRenderingOptions().createRendering().initialize(manager, controller, Minecraft.getInstance());

    @Override
    public void fhb$react() {
        rendering.react(manager, controller, minecraft);
    }

    @Override
    public HealthBarRendering fhb$getRendering() {
        return rendering;
    }

    @Override
    public void fhb$setRendering(HealthBarRendering rendering) {
        this.rendering = rendering;
        manager.reset();
        controller.reset();
        rendering.initialize(manager, controller, minecraft);
    }

    @Unique
    private int lastHealth = 0;

    @Inject(method = "tick()V", at = @At("TAIL"))
    public void tickTail(CallbackInfo ci) {
        manager.tick();
        controller.tick();
    }

    //~if>=26.1 'renderHearts' -> 'extractHearts'
    @WrapMethod(method = "renderHearts")
    public void renderHealthBar(GuiGraphics context, Player player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int a, int b, int absorption, boolean blinking, Operation<Void> original) {
        int health = (int) Math.ceil(Minecraft.getInstance().player.getHealth());
        rendering.draw(random, context, player, x, y, lines, regeneratingHeartIndex, maxHealth, a, health, absorption, blinking);
        manager.render(context);
        controller.draw(context);
    }


    @Unique
    private static final String renderMixinTarget =
            //? >=26.1
            //"Lnet/minecraft/client/gui/Hud;extractHearts(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V";
            //? < 26.1
            "Lnet/minecraft/client/gui/Gui;renderHearts(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/player/Player;IIIIFIIIZ)V";

    //~if>=26.1 'renderPlayerHealth' -> 'extractPlayerHealth'
    @Inject(method = "renderPlayerHealth", at = @At(value = "INVOKE", target = renderMixinTarget, shift = At.Shift.AFTER))
    public void render(GuiGraphics graphics, CallbackInfo ci,
                       @Local(name = /*?>=26.1{*//*"xLeft"*//*?}else{*/"m"/*?}*/) int x,
                       @Local(name = /*?>=26.1{*//*"yLineBase"*//*?}else{*/"o"/*?}*/) int y,
                       @Local(name = /*?>=26.1{*//*"healthRowHeight"*//*?}else{*/"r"/*?}*/) int healthRowHeight,
                       @Local(name = /*?>=26.1{*//*"heartOffsetIndex"*//*?}else{*/"v"/*?}*/) int heartOffsetIndex,
                       @Local(name = /*?>=26.1{*//*"maxHealth"*//*?}else{*/"f"/*?}*/) float maxHealth,
                       @Local(name = /*?>=26.1{*//*"totalAbsorption"*//*?}else{*/"i"/*?}*/) int totalAbsorption,
                       @Local(name = /*?>=26.1{*//*"blink"*//*?}else{*/"bl"/*?}*/) boolean blink) {
        int health = (int)Math.ceil(minecraft.player.getHealth());
        if (this.lastHealth != health) {
            if (health < this.lastHealth)
                rendering.onDamage(manager, x, y, healthRowHeight, heartOffsetIndex, maxHealth, this.lastHealth, health, totalAbsorption, blink);
            this.lastHealth = health;
        }
    }

    @Override
    public void fhb$drawExternal(GuiGraphics context) {
        manager.render(context);
        controller.draw(context);
    }

}

//~}