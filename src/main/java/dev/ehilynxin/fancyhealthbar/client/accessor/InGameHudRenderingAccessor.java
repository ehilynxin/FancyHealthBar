package dev.ehilynxin.fancyhealthbar.client.accessor;

import dev.ehilynxin.fancyhealthbar.client.rendering.HealthBarRendering;

public interface InGameHudRenderingAccessor {

    void fhb$setRendering(HealthBarRendering rendering);
    HealthBarRendering fhb$getRendering();
    void fhb$react();

}
