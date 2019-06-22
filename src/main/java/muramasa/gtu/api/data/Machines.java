package muramasa.gtu.api.data;

import muramasa.gtu.api.GregTechAPI;
import muramasa.gtu.api.machines.MachineFlag;
import muramasa.gtu.api.machines.MachineStack;
import muramasa.gtu.api.machines.Tier;
import muramasa.gtu.api.machines.types.*;
import muramasa.gtu.api.recipe.RecipeMap;
import muramasa.gtu.common.tileentities.multi.*;

import java.util.ArrayList;
import java.util.Collection;

import static muramasa.gtu.api.machines.MachineFlag.*;
import static muramasa.gtu.api.machines.Tier.*;

public class Machines {

    private static ArrayList<Machine> ID_LOOKUP = new ArrayList<>();

    public static Machine INVALID = new Machine("invalid");

    public static Machine ALLOY_SMELTER = new BasicMachine("alloy_smelter", ITEM);
    public static Machine ASSEMBLER = new BasicMachine("assembler", ITEM);
    public static Machine BENDER = new BasicMachine("bender", ITEM);
    public static Machine CANNER = new BasicMachine("canner", ITEM);
    public static Machine COMPRESSOR = new BasicMachine("compressor", ITEM);
    public static Machine CUTTER = new BasicMachine("cutter", ITEM);
    public static Machine FURNACE = new BasicMachine("furnace", ITEM);
    public static Machine EXTRACTOR = new BasicMachine("extractor", ITEM);
    public static Machine EXTRUDER = new BasicMachine("extruder", ITEM);
    public static Machine LATHE = new BasicMachine("lathe", ITEM);
    public static Machine PULVERIZER = new BasicMachine("pulverizer", ITEM);
    public static Machine RECYCLER = new BasicMachine("recycler", ITEM);
    public static Machine SCANNER = new BasicMachine("scanner", ITEM, FLUID);
    public static Machine WIRE_MILL = new BasicMachine("wire_mill", ITEM);
    public static Machine CENTRIFUGE = new BasicMachine("centrifuge", ITEM, FLUID);
    public static Machine ELECTROLYZER = new BasicMachine("electrolyzer", ITEM, FLUID);
    public static Machine THERMAL_CENTRIFUGE = new BasicMachine("thermal_centrifuge", ITEM);
    public static Machine ORE_WASHER = new BasicMachine("ore_washer", ITEM, FLUID);
    public static Machine CHEMICAL_REACTOR = new BasicMachine("chemical_reactor", ITEM, FLUID);
    public static Machine FLUID_CANNER = new BasicMachine("fluid_canner", ITEM, FLUID);
    public static Machine DISASSEMBLER = new BasicMachine("disassembler", ITEM);
    public static Machine MASS_FABRICATOR = new BasicMachine("mass_fabricator", ITEM, FLUID);
    public static Machine AMP_FABRICATOR = new BasicMachine("amp_fabricator", ITEM);
    public static Machine REPLICATOR = new BasicMachine("replicator", ITEM, FLUID);
    public static Machine FERMENTER = new BasicMachine("fermenter", ITEM, FLUID);
    public static Machine FLUID_EXTRACTOR = new BasicMachine("fluid_extractor", ITEM);
    public static Machine FLUID_SOLIDIFIER = new BasicMachine("fluid_solidifier", ITEM, FLUID);
    public static Machine DISTILLERY = new BasicMachine("distillery", ITEM, FLUID);
    public static Machine CHEMICAL_BATH = new BasicMachine("chemical_bath", ITEM, FLUID);
    public static Machine AUTOCLAVE = new BasicMachine("autoclave", ITEM, FLUID);
    public static Machine MIXER = new BasicMachine("mixer", ITEM, FLUID);
    public static Machine LASER_ENGRAVER = new BasicMachine("laser_engraver", ITEM);
    public static Machine FORMING_PRESS = new BasicMachine("forming_press", ITEM);
    public static Machine FORGE_HAMMER = new BasicMachine("forge_hammer", ITEM);
    public static Machine SIFTER = new BasicMachine("sifter", ITEM);
    public static Machine ARC_FURNACE = new BasicMachine("arc_furnace", ITEM, FLUID);
    public static Machine PLASMA_ARC_FURNACE = new BasicMachine("plasma_arc_furnace", ITEM, FLUID);

    public static Machine COAL_BOILER = new BasicMachine("coal_boiler", ITEM, FLUID).setTiers(Tier.getSteam()); //TODO
    public static Machine LAVA_BOILER = new BasicMachine("lava_boiler", ITEM, FLUID).setTiers(STEEL); //TODO
    public static Machine SOLAR_BOILER = new BasicMachine("solar_boiler", ITEM, FLUID).setTiers(BRONZE); //TODO
    public static Machine STEAM_FURNACE = new BasicMachine("steam_furnace", ITEM, FLUID).setTiers(Tier.getSteam());
    public static Machine STEAM_PULVERIZER = new BasicMachine("steam_pulverizer", ITEM, FLUID).setTiers(Tier.getSteam());
    public static Machine STEAM_EXTRACTOR = new BasicMachine("steam_extractor", ITEM, FLUID).setTiers(Tier.getSteam());
    public static Machine STEAM_FORGE_HAMMER = new BasicMachine("steam_forge_hammer", ITEM, FLUID).setTiers(Tier.getSteam());
    public static Machine STEAM_COMPRESSOR = new BasicMachine("steam_compressor", ITEM, FLUID).setTiers(Tier.getSteam());
    public static Machine STEAM_ALLOY_SMELTER = new BasicMachine("steam_alloy_smelter", ITEM, FLUID).setTiers(Tier.getSteam());

    public static Machine STEAM_GENERATOR = new BasicMachine("steam_generator", ITEM, FLUID).setTiers(LV, MV, HV).setRecipeMap(RecipeMap.STEAM_FUELS);
    public static Machine GAS_GENERATOR = new BasicMachine("gas_generator", ITEM, FLUID).setTiers(LV, MV, HV).setRecipeMap(RecipeMap.GAS_FUELS);
    public static Machine COMBUSTION_GENERATOR = new BasicMachine("combustion_generator", ITEM, FLUID).setTiers(LV, MV, HV).setRecipeMap(RecipeMap.COMBUSTION_FUELS);
    public static Machine NAQUADAH_GENERATOR = new BasicMachine("naquadah_generator", ITEM, FLUID).setTiers(EV, IV, LUV).setRecipeMap(RecipeMap.NAQUADAH_FUELS);
    public static Machine PLASMA_GENERATOR = new BasicMachine("plasma_generator", ITEM, FLUID).setTiers(IV, LUV, ZPM).setRecipeMap(RecipeMap.PLASMA_FUELS);

    public static Machine QUANTUM_TANK = new TankMachine("quantum_tank");

    public static Machine COKE_OVEN = new MultiMachine("coke_oven", TileEntityCokeOven.class, LV).addFlags(ITEM);
    public static Machine PRIMITIVE_BLAST_FURNACE = new MultiMachine("primitive_blast_furnace", TileEntityPrimitiveBlastFurnace.class, LV).addFlags(ITEM);
    public static Machine CHARCOAL_PIT = new MultiMachine("charcoal_pit", TileEntityCharcoalPit.class, LV);
    public static Machine BRONZE_BLAST_FURNACE = new MultiMachine("bronze_blast_furnace", TileEntityBronzeBlastFurnace.class, LV).addFlags(ITEM);
    public static Machine BLAST_FURNACE = new MultiMachine("electric_blast_furnace", TileEntityElectricBlastFurnace.class, LV).addFlags(ITEM, FLUID);
    public static Machine IMPLOSION_COMPRESSOR = new MultiMachine("implosion_compressor", TileEntityImplosionCompressor.class, HV).addFlags(ITEM);
    public static Machine VACUUM_FREEZER = new MultiMachine("vacuum_freezer", TileEntityVacuumFreezer.class, HV).addFlags(ITEM, FLUID);
    public static Machine MULTI_SMELTER = new MultiMachine("multi_smelter", TileEntityMultiSmelter.class, HV).addFlags(ITEM);
    public static Machine LARGE_BOILER = new MultiMachine("large_boiler", TileEntityLargeBoiler.class, LV, MV, HV, EV).addFlags(ITEM, FLUID);
    public static Machine LARGE_TURBINE = new MultiMachine("large_turbine", TileEntityLargeTurbine.class, HV, EV, IV, UV).addFlags(FLUID);
    public static Machine HEAT_EXCHANGER = new MultiMachine("heat_exchanger", TileEntityHeatExchanger.class, EV).addFlags(FLUID);
    public static Machine OIL_DRILLING_RIG = new MultiMachine("oil_drilling_rig", TileEntityOilDrillingRig.class, EV, IV, LUV, ZPM).addFlags(FLUID);
    public static Machine OIL_CRACKING_UNIT = new MultiMachine("oil_cracking_unit", TileEntityOilCrackingUnit.class, HV).addFlags(FLUID);
    public static Machine ADVANCED_MINER = new MultiMachine("advanced_miner", TileEntityAdvancedMiner.class, LV).addFlags(ITEM);
    public static Machine PYROLYSIS_OVEN = new MultiMachine("pyrolysis_oven", TileEntityPyrolysisOven.class, MV).addFlags(ITEM, FLUID);
    public static Machine COMBUSTION_ENGINE = new MultiMachine("combustion_engine", TileEntityCombustionEngine.class, EV).addFlags(FLUID);
    public static Machine FUSION_REACTOR = new MultiMachine("fusion_reactor", TileEntityFusionReactor.class, LUV, ZPM, UV).addFlags(FLUID);

    public static Machine HATCH_ITEM_I = new HatchMachine("hatch_item_input", GUI, ITEM);
    public static Machine HATCH_ITEM_O = new HatchMachine("hatch_item_output", GUI, ITEM);
    public static Machine HATCH_FLUID_I = new HatchMachine("hatch_fluid_input", GUI, FLUID);
    public static Machine HATCH_FLUID_O = new HatchMachine("hatch_fluid_output", GUI, FLUID);
    public static Machine HATCH_MUFFLER = new HatchMachine("hatch_muffler", GUI, ITEM);
    public static Machine HATCH_DYNAMO = new HatchMachine("hatch_dynamo", ENERGY);
    public static Machine HATCH_ENERGY = new HatchMachine("hatch_energy", ENERGY);

    public static void init() {

    }

    public static void add(Machine machine) {
        GregTechAPI.register(Machine.class, machine);
        GregTechAPI.register(machine.getBlock());
        ID_LOOKUP.add(machine.getInternalId(), machine);
    }

    public static Machine get(String name) {
        Machine machine = GregTechAPI.get(Machine.class, name);
        return machine != null ? machine : INVALID;
    }

    public static Machine get(int id) {
        Machine machine = ID_LOOKUP.get(id);
        return machine != null ? machine : INVALID;
    }

    public static MachineStack get(Machine type, Tier tier) {
        return new MachineStack(type, tier);
    }

    public static Collection<Machine> getTypes(MachineFlag... flags) {
        ArrayList<Machine> types = new ArrayList<>();
        for (MachineFlag flag : flags) {
            types.addAll(flag.getTypes());
        }
        return types;
    }

    public static Collection<MachineStack> getStacks(MachineFlag... flags) {
        ArrayList<MachineStack> stacks = new ArrayList<>();
        for (MachineFlag flag : flags) {
            stacks.addAll(flag.getStacks());
        }
        return stacks;
    }
}
