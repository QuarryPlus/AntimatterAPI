package muramasa.antimatter.tools;

import com.google.common.collect.*;
import muramasa.antimatter.AntimatterAPI;
import muramasa.antimatter.Configs;
import muramasa.antimatter.Ref;
import muramasa.antimatter.materials.Material;
import muramasa.antimatter.util.Utils;
import net.minecraft.block.*;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.UnbreakingEnchantment;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.*;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.*;
import net.minecraft.util.math.*;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.ForgeEventFactory;

import javax.annotation.Nullable;
import java.util.*;

import static muramasa.antimatter.tools.AntimatterToolType.*;

public class MaterialTool extends ToolItem implements IAntimatterTool {

    protected String domain;
    protected String concatId;
    protected IItemTier tier;
    protected AntimatterToolType type;
    protected Material primary;
    @Nullable protected Material secondary;
    protected Set<ToolType> toolTypes;

    //TODO: make stripping map a thing in AntimatterToolType?
    private static final ImmutableMap<Block, Block> AXE_BLOCK_STRIPPING_MAP = new ImmutableMap.Builder<Block, Block>()
            .put(Blocks.OAK_WOOD, Blocks.STRIPPED_OAK_WOOD).put(Blocks.OAK_LOG, Blocks.STRIPPED_OAK_LOG).put(Blocks.DARK_OAK_WOOD, Blocks.STRIPPED_DARK_OAK_WOOD)
            .put(Blocks.DARK_OAK_LOG, Blocks.STRIPPED_DARK_OAK_LOG).put(Blocks.ACACIA_WOOD, Blocks.STRIPPED_ACACIA_WOOD).put(Blocks.ACACIA_LOG, Blocks.STRIPPED_ACACIA_LOG)
            .put(Blocks.BIRCH_WOOD, Blocks.STRIPPED_BIRCH_WOOD).put(Blocks.BIRCH_LOG, Blocks.STRIPPED_BIRCH_LOG).put(Blocks.JUNGLE_WOOD, Blocks.STRIPPED_JUNGLE_WOOD)
            .put(Blocks.JUNGLE_LOG, Blocks.STRIPPED_JUNGLE_LOG).put(Blocks.SPRUCE_WOOD, Blocks.STRIPPED_SPRUCE_WOOD).put(Blocks.SPRUCE_LOG, Blocks.STRIPPED_SPRUCE_LOG).build();

    //TODO: make hoe_lookup a thing in AntimatterToolType?
    private static final ImmutableMap<Block, BlockState> HOE_LOOKUP = ImmutableMap.of(Blocks.GRASS_BLOCK, Blocks.FARMLAND.getDefaultState(),
            Blocks.GRASS_PATH, Blocks.FARMLAND.getDefaultState(), Blocks.DIRT, Blocks.FARMLAND.getDefaultState(), Blocks.COARSE_DIRT, Blocks.DIRT.getDefaultState());

    public MaterialTool(String domain, AntimatterToolType type, IItemTier tier, Properties properties, Material primary, @Nullable Material secondary) {
        super(type.getBaseAttackDamage(), type.getBaseAttackSpeed(), tier, type.getEffectiveBlocks(), properties);
        this.domain = domain;
        this.type = type;
        this.tier = tier;
        this.primary = primary;
        this.secondary = secondary;
        this.toolTypes = type.getToolTypes();
        /*
        if (type.getUseAction() == UseAction.BOW) {
            this.addPropertyOverride(new ResourceLocation("pull"), (stack, world, entity) -> {
                if (entity == null) {
                    return 0.0F;
                } else {
                    return entity.getActiveItemStack().getItem() instanceof IAntimatterTool &&
                            ((IAntimatterTool) entity.getActiveItemStack().getItem()).getType().getUseAction() == UseAction.BOW ? 20.0F : 0.0F;
                }
            });
            this.addPropertyOverride(new ResourceLocation("pulling"),
                    (stack, world, entity) -> entity != null && entity.isHandActive() && entity.getActiveItemStack() == stack ? 1.0F : 0.0F);
        }
         */
        setRegistryName(domain, getId());
        AntimatterAPI.register(IAntimatterTool.class, this);
    }

    protected MaterialTool(String domain, String concatId, AntimatterToolType type, IItemTier tier, Properties properties, Material primary, @Nullable Material secondary) {
        super(type.getBaseAttackDamage(), type.getBaseAttackSpeed(), tier, type.getEffectiveBlocks(), properties);
        this.domain = domain;
        this.concatId = concatId;
        this.type = type;
        this.tier = tier;
        this.primary = primary;
        this.secondary = secondary;
        this.toolTypes = type.getToolTypes();
        setRegistryName(domain, getId());
        AntimatterAPI.register(IAntimatterTool.class, this);
    }

    @Override
    public String getDomain() { return domain; }

    @Override
    public AntimatterToolType getType() { return type; }

    @Override
    public IItemTier getTier() { return tier; }

    @Override
    public Material getPrimaryMaterial() { return primary; }

    @Nullable
    @Override
    public Material getSecondaryMaterial() { return secondary; }

    @Override
    public Item asItem() { return this; }

    @Override
    public Set<ToolType> getToolTypes(ItemStack stack) {
        return toolTypes;
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World world, List<ITextComponent> tooltip, ITooltipFlag flag) {
        if (type.getTooltip().size() != 0) {
            for (ITextComponent text : type.getTooltip()) {
                tooltip.add(text);
            }
        }
    }

    @Override
    public boolean shouldCauseReequipAnimation(ItemStack oldStack, ItemStack newStack, boolean slotChanged) {
        return false;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return type.getUseAction();
    }

    @Override
    public int getUseDuration(ItemStack stack) {
        return type.getUseAction() == UseAction.NONE ? super.getUseDuration(stack) : 72000;
    }

    @Override
    public boolean canHarvestBlock(BlockState state) {
        return Utils.isToolEffective(type, state) && tier.getHarvestLevel() >= state.getHarvestLevel();
    }

    @Override
    public int getHarvestLevel(ItemStack stack, net.minecraftforge.common.ToolType tool, @Nullable PlayerEntity player, @Nullable BlockState blockState) {
        return tier.getHarvestLevel();
    }

    @Override
    public boolean hitEntity(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        if (type.getUseSound() != null) target.getEntityWorld().playSound(null, target.getPosX(), target.getPosY(), target.getPosZ(), type.getUseSound(), SoundCategory.HOSTILE, 0.75F, 0.75F);
        stack.damageItem(type.getAttackDurability(), attacker, (onBroken) -> onBroken.sendBreakAnimation(EquipmentSlotType.MAINHAND));
        return true;
    }

    @Override
    public float getDestroySpeed(ItemStack stack, BlockState state) {
        return Utils.isToolEffective(type, state) ? tier.getEfficiency() : 1.0F;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity livingEntity) {
        Block block = state.getBlock();
        if (livingEntity instanceof PlayerEntity) {
            PlayerEntity player = (PlayerEntity) livingEntity;
            if (type.getUseSound() != null) player.playSound(type.getUseSound(), SoundCategory.BLOCKS, 0.84F, 0.75F);
            boolean isToolEffective = Utils.isToolEffective(type, state);
            if (state.getBlockHardness(world, pos) != 0.0F) {
                stack.damageItem(isToolEffective ? type.getUseDurability() : type.getUseDurability() + 1,
                        livingEntity, (onBroken) -> onBroken.sendBreakAnimation(EquipmentSlotType.MAINHAND));
            }
            if (isToolEffective && !player.isCrouching()) { // Only when player isn't shifting/crouching this ability activates
                if (type == CHAINSAW || tier.getHarvestLevel() > 1 && (type == AXE || type.getToolTypes().contains("axe"))) {
                    if (!Configs.GAMEPLAY.AXE_TIMBER) return true;
                    if (block.isIn(BlockTags.LOGS)) {
                        Utils.treeLogging(this, stack, pos, player, world);
                    }
                }
                if (type.getMultiBlockBreakability()) {
                    Iterator<BlockPos> positions = Utils.getHarvestableBlocksToBreak(world, player, this,
                            type.getMultiBlockBreakColumn(), type.getMultiBlockBreakRow(), type.getMultiBlockBreakDepth()).iterator();
                    while (positions.hasNext()) {
                        if (stack.getDamage() < type.getUseDurability()) return true;
                        if (!Utils.breakBlock(world, player, stack, positions.next(), type.getUseDurability())) break;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean canPlayerBreakBlockWhileHolding(BlockState state, World world, BlockPos pos, PlayerEntity player) {
        return type.getBlockBreakability();
    }

    /*
    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EquipmentSlotType slotType) {
        Multimap<String, AttributeModifier> modifiers = super.getAttributeModifiers(slotType);
        if (slotType == EquipmentSlotType.MAINHAND) {
            modifiers.put(SharedMonsterAttributes.ATTACK_DAMAGE.getName(), new AttributeModifier(ATTACK_DAMAGE_MODIFIER, "Tool modifier", tier.getAttackDamage(), AttributeModifier.Operation.ADDITION));
            modifiers.put(SharedMonsterAttributes.ATTACK_SPEED.getName(), new AttributeModifier(ATTACK_SPEED_MODIFIER, "Tool modifier", type.getBaseAttackSpeed(), AttributeModifier.Operation.ADDITION));
        }
        return modifiers;
    }
     */

    @Override
    public ActionResultType onItemUse(ItemUseContext context) {
        World world = context.getWorld();
        BlockPos pos = context.getPos();
        BlockState state = world.getBlockState(pos);
        PlayerEntity player = context.getPlayer();
        if (type == AXE || type.getToolTypes().contains("axe")) {
            Block block = AXE_BLOCK_STRIPPING_MAP.get(state.getBlock());
            if (block != null) {
                world.playSound(player, pos, SoundEvents.ITEM_AXE_STRIP, SoundCategory.BLOCKS, 1.0F, 1.0F);
                world.setBlockState(pos, block.getDefaultState().with(RotatedPillarBlock.AXIS, state.get(RotatedPillarBlock.AXIS)), 11);
                if (player != null) context.getItem().damageItem(type.getUseDurability(), player, (onBroken) -> onBroken.sendBreakAnimation(context.getHand()));
                return ActionResultType.SUCCESS;
            } else return ActionResultType.PASS;
        }
        else if (type == SHOVEL || type.getToolTypes().contains("shovel")) {
            if (context.getFace() == Direction.DOWN) return ActionResultType.PASS;
            else {
                BlockState changedState = null;
                if (state.getBlock() == Blocks.GRASS_BLOCK && world.isAirBlock(pos.up())) {
                    world.playSound(player, pos, SoundEvents.ITEM_SHOVEL_FLATTEN, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    changedState = Blocks.GRASS_PATH.getDefaultState();
                }
                else if (state.getBlock() instanceof CampfireBlock && state.get(CampfireBlock.LIT)) {
                    world.playEvent(player, 1009, pos, 0);
                    changedState = state.with(CampfireBlock.LIT, false);
                }
                if (changedState != null) {
                    world.setBlockState(pos, changedState, 11);
                    if (player != null) context.getItem().damageItem(type.getUseDurability(), player, (onBroken) -> onBroken.sendBreakAnimation(context.getHand()));
                    return ActionResultType.SUCCESS;
                }
                else return ActionResultType.PASS;
            }
        }
        else if (type == HOE || type.getToolTypes().contains("hoe")) {
            int hook = ForgeEventFactory.onHoeUse(context);
            if (hook != 0) return hook > 0 ? ActionResultType.SUCCESS : ActionResultType.FAIL;
            if (context.getFace() != Direction.DOWN && world.isAirBlock(pos.up())) {
                BlockState blockstate = HOE_LOOKUP.get(world.getBlockState(pos).getBlock());
                if (blockstate != null) {
                    world.playSound(player, pos, SoundEvents.ITEM_HOE_TILL, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    world.setBlockState(pos, blockstate, 11);
                    if (player != null) context.getItem().damageItem(type.getUseDurability(), player, (onBroken) -> onBroken.sendBreakAnimation(context.getHand()));
                    return ActionResultType.SUCCESS;
                }
            }
        }
        else if (type == WRENCH || type.getToolTypes().contains("wrench")) {
            if (state.getBlock().getValidRotations(state, world, pos) != null) {
                if (!player.isCrouching()) state.rotate(world, pos, Rotation.CLOCKWISE_90);
                else state.rotate(world, pos, Rotation.COUNTERCLOCKWISE_90);
            }
        }
        else if (type == PLUNGER || type.getToolTypes().contains("plunger")) {
            if (state.has(BlockStateProperties.WATERLOGGED)) {
                if (state.get(BlockStateProperties.WATERLOGGED)) {
                    world.setBlockState(pos, state.with(BlockStateProperties.WATERLOGGED, false), 11);
                    world.playSound(player, pos, SoundEvents.ITEM_BUCKET_EMPTY, SoundCategory.BLOCKS, 1.0F, 1.0F);
                    context.getItem().damageItem(type.getUseDurability(), player, (onBroken) -> onBroken.sendBreakAnimation(context.getHand()));
                    return ActionResultType.SUCCESS;
                }
            }
        }
        //TODO functionality moved to BlockMachine.onBlockActivated
        //TODO determine if other mods need smart interaction on
        //TODO blocks that *don't* extend BlockMachine
//        TileEntity tile = Utils.getTile(world, pos);
//        if (tile == null) return EnumActionResult.PASS;
//        EnumActionResult result = EnumActionResult.PASS;
//        if (tile.hasCapability(GTCapabilities.CONFIGURABLE, facing)) {
//            Direction targetSide = Utils.getInteractSide(facing, hitX, hitY, hitZ);
//            IConfigHandler configHandler = tile.getCapability(GTCapabilities.CONFIGURABLE, targetSide);
//            if (configHandler != null) {
//                if (type != null && configHandler.onInteract(player, hand, targetSide, type)) {
//                    damage(stack, type.getDamageCrafting(), player, true);
//                    result = EnumActionResult.SUCCESS;
//                }
//            }
//        }
//        if (tile.hasCapability(GTCapabilities.COVERABLE, facing)) {
//            Direction targetSide = Utils.getInteractSide(facing, hitX, hitY, hitZ);
//            ICoverHandler coverHandler = tile.getCapability(GTCapabilities.COVERABLE, targetSide);
//            if (coverHandler != null) {
//                if (type != null && coverHandler.onInteract(player, hand, targetSide, type)) {
//                    damage(stack, type.getDamageCrafting(), player, true);
//                    result = EnumActionResult.SUCCESS;
//                }
//            }
//        }
//        return result;
        return ActionResultType.PASS;
    }

    @Override
    public boolean hasContainerItem(ItemStack stack) {
        return true;
    }

    @Override
    public ItemStack getContainerItem(ItemStack stack) {
        stack = stack.copy();
        int amount = type.getCraftingDurability();
        int level = EnchantmentHelper.getEnchantmentLevel(Enchantments.UNBREAKING, stack), j = 0;
        for (int k = 0; level > 0 && k < amount; k++) {
            if (UnbreakingEnchantment.negateDamage(stack, level, Ref.RNG)) j++;
        }
        amount -= j;
        if (amount > 0) {
            int stackDamage = stack.getDamage();
            int damage = stackDamage + amount;
            // if (damage >= stackDamage) return ItemStack.EMPTY;
            stack.setDamage(damage);
            return stack;
        }
    	return stack;
    }

    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

}