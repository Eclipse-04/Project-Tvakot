package tvakot.ai

import arc.util.Log
import mindustry.entities.Units
import mindustry.gen.Building
import mindustry.world.blocks.ConstructBlock

class RepairDroneAI : BuildingDroneAI() {
    override fun addAI(build: Building, range: Float): BuildingDroneAI {
        return RepairDroneAI().apply {
            this.build = build
            this.range = range
        }
    }
    override fun updateMovement() {
        if (target is Building && !idle()) {
            var shoot = false
            if (target.within(unit, unit.type.range)) {
                unit.aim(target)
                shoot = true
            }
            unit.controlWeapons(shoot)
        } else if (target == null) {
            unit.controlWeapons(false)
        }

        if(target != null && !idle()){
            if(!target.within(unit, unit.type.range * 0.65f) && target is Building && target!!.team() == unit.team){
                moveTo(target, unit.type.range * 0.65f)
            }
            unit.lookAt(target)
        }
        super.updateMovement()
    }
    override fun updateTargeting() {
        var target = Units.findDamagedTile(unit.team, unit.x, unit.y)
        Log.info(target.dst(build))
        if(target is ConstructBlock.ConstructBuild || (build != null && target != null && target.dst(build) > range)) target = null
        if(target == null){
            super.updateTargeting()
        }else{
            this.target = target
        }
    }
    override fun idle(): Boolean {
        return target !is Building
    }
}