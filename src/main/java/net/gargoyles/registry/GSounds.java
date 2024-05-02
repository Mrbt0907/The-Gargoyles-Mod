/*     */ package net.gargoyles.registry;
/*     */ 
/*     */ import net.minecraft.util.ResourceLocation;
/*     */ import net.minecraft.util.SoundEvent;
import net.minecraftforge.registries.GameData;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class GSounds
/*     */ {
/*     */   public static SoundEvent gargoyleLiving;
/*     */   public static SoundEvent gargoyleGrunt;
/*     */   public static SoundEvent gargoyleDeath;
/*     */   
/*     */   public static void registerSounds()
/*     */   {
/*  62 */     gargoyleLiving = registerSound("gargoyleLiving");
/*  63 */     gargoyleGrunt = registerSound("gargoyleGrunt");
/*  64 */     gargoyleDeath = registerSound("gargoyleDeath");
/*     */   }
/*     */   
/*     */   public static SoundEvent registerSound(String soundName)
/*     */   {
/* 114 */     ResourceLocation soundID = new ResourceLocation("gargoyles", soundName);
/* 115 */     return (SoundEvent)GameData.register_impl(new SoundEvent(soundID).setRegistryName(soundID));
/*     */   }
/*     */ }