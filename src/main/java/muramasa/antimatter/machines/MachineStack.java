package muramasa.antimatter.machines;

import muramasa.antimatter.machines.types.Machine;
import muramasa.antimatter.registration.IAntimatterObject;

public class MachineStack implements IAntimatterObject {

    private Machine type;
    private Tier tier;

    public MachineStack(Machine type, Tier tier) {
        this.type = type;
        this.tier = tier;
    }

    public Machine getType() {
        return type;
    }

    public Tier getTier() {
        return tier;
    }

    @Override
    public String getId() {
        return type.getId() + "_" + tier.getId();
    }
}
