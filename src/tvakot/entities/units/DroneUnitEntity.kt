package tvakot.entities.units

import mindustry.gen.UnitEntity

class DroneUnitEntity : UnitEntity() {
    override fun cap(): Int {
        return count() + 3
    }
}