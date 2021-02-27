package com.ptsmods.morecommands.mixin.common;

import com.ptsmods.morecommands.MoreCommands;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilScreenHandler.class)
public class MixinAnvilScreenHandler {

    private PlayerInventory mc_playerInv;

    @Inject(at = @At("TAIL"), method = "<init>(ILnet/minecraft/entity/player/PlayerInventory;Lnet/minecraft/screen/ScreenHandlerContext;)V")
    public void init(int syncId, PlayerInventory inventory, ScreenHandlerContext context, CallbackInfo cbi) {
        mc_playerInv = inventory;
    }

    @Redirect(at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack; getRepairCost()I"), method = "updateResult()V")
    public int updateResult_getRepairCost(ItemStack stack) {
        return mc_playerInv.player.world.getGameRules().getBoolean(MoreCommands.doPriorWorkPenaltyRule) ? stack.getRepairCost() : 0;
    }

}
