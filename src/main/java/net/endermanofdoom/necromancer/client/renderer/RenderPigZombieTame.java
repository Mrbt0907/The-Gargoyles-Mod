package net.endermanofdoom.necromancer.client.renderer;

import net.endermanofdoom.necromancer.entity.friendly.EntityZombieTame;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPigZombieTame extends RenderZombieTame
{
    private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie_pigman.png");

    public RenderPigZombieTame(RenderManager renderManagerIn)
    {
        super(renderManagerIn);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityZombieTame entity)
    {
        return ZOMBIE_TEXTURES;
    }
}