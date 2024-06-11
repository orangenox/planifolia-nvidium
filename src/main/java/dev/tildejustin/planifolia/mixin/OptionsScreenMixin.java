package dev.tildejustin.planifolia.mixin;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.*;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = OptionsScreen.class, priority = 1005)
public abstract class OptionsScreenMixin extends Screen {
    protected OptionsScreenMixin(Text title) {
        super(title);
    }

    @Dynamic
    @Group(min = 1, max = 1)
    @TargetHandler(mixin = "me.jellysquid.mods.sodium.mixin.features.options.MixinOptionsScreen", name = "open")
    @ModifyArg(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable;setReturnValue(Ljava/lang/Object;)V"), require = 0)
    private Object openVanillaMenu1(Object original) {
        return MinecraftClient.getInstance().world != null ? new VideoOptionsScreen(this, this.client.options) : original;
    }

    @Dynamic
    @Group
    @TargetHandler(mixin = "me.jellysquid.mods.sodium.mixin.features.gui.hooks.settings.OptionsScreenMixin", name = "open")
    @Inject(method = "@MixinSquared:Handler", at = @At("HEAD"), require = 0, cancellable = true)
    private void openVanillaMenu2(CallbackInfo ci) {
        if (MinecraftClient.getInstance().world != null) {
            ci.cancel();
        }
    }

    @Dynamic
    @Group
    @TargetHandler(mixin = "me.jellysquid.mods.sodium.mixin.features.options.MixinOptionsScreen", name = "open")
    @ModifyArg(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"), require = 0)
    private Screen openVanillaMenu3(Screen original) {
        return MinecraftClient.getInstance().world != null ? new VideoOptionsScreen(this, this.client.options) : original;
    }
}
