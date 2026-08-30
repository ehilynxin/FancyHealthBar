package dev.ehilynxin.fancyhealthbar.client.mixin;

import dev.ehilynxin.fancyhealthbar.client.accessor.ClearMethod;
import net.minecraft.client.gui.layouts.GridLayout;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GridLayout.RowHelper.class)
public abstract class GridAdderMixin implements ClearMethod {

    @Shadow
    private int index;

    @Shadow
    public abstract GridLayout getGrid();

    @Override
    public void clear() {
        ((ClearMethod) this.getGrid()).clear();
        this.index = 0;
    }

}
