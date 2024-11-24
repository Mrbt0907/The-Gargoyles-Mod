package net.endermanofdoom.necromancer.client.renderer;

import net.endermanofdoom.necromancer.client.model.ModelZombieTame;
import net.endermanofdoom.necromancer.entity.friendly.EntityZombieTame;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.layers.LayerBipedArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderZombieTame extends RenderBiped<EntityZombieTame>
{
    private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");

    public RenderZombieTame(RenderManager renderManagerIn)
    {
        super(renderManagerIn, new ModelZombieTame(), 0.5F);
        LayerBipedArmor layerbipedarmor = new LayerBipedArmor(this)
        {
            protected void initArmor()
            {
                this.modelLeggings = new ModelZombieTame(0.5F, true);
                this.modelArmor = new ModelZombieTame(1.0F, true);
            }
        };
        this.addLayer(layerbipedarmor);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityZombieTame entity)
    {
        return ZOMBIE_TEXTURES;
    }
}