package tvakot.ai

import mindustry.entities.Units
import mindustry.world.blocks.ConstructBlock.ConstructBuild

class RepairDroneAI : DroneAI() {
    override fun updateTargeting() {
        var target = Units.findDamagedTile(unit.team, unit.x, unit.y)
        if (target is ConstructBuild) target = null
        if (target == null) super.updateTargeting() else this.target = target
    }
}