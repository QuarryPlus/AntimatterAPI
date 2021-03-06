package muramasa.antimatter.tools.behaviour;

import muramasa.antimatter.behaviour.IBehaviour;
import muramasa.antimatter.tools.base.MaterialTool;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemUseContext;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Rotation;

public class BehaviourBlockRotate implements IBehaviour<MaterialTool> {

    @Override
    public ActionResultType onItemUse(MaterialTool instance, ItemUseContext c) {
        BlockState state = c.getWorld().getBlockState(c.getPos());
        if (state.getBlock().getValidRotations(state, c.getWorld(), c.getPos()) != null && c.getPlayer() != null) {
            state.rotate(c.getWorld(), c.getPos(), c.getPlayer().isCrouching() ? Rotation.CLOCKWISE_90 : Rotation.COUNTERCLOCKWISE_90);
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }
}
