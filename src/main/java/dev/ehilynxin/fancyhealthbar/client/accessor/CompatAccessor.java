package dev.ehilynxin.fancyhealthbar.client.accessor;

//~if >=26.1 'GuiGraphics' -> 'GuiGraphicsExtractor' {
import net.minecraft.client.gui.GuiGraphics;

public interface CompatAccessor {

    void fhb$drawExternal(GuiGraphics context);

}
//~}