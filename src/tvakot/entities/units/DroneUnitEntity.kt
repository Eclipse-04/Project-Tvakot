package tvakot.entities.units

import mindustry.Vars
import mindustry.content.Fx
import mindustry.gen.UnitEntity

class DroneUnitEntity : UnitEntity() {
    var spawnerBuilding: Int? = null
    var spawnerUnit: mindustry.gen.Unit? = null
    var state: UnitState = UnitState.working
    override fun cap(): Int {
        return count() + 3
    }
    fun setUnitState(state: UnitState){
        this.state = state
    }
    fun getOwner(): Any?{
        return if(spawnerBuilding != null) {
            Vars.world.build(spawnerBuilding!!)
        } else if(spawnerUnit != null && spawnerUnit!!.isValid){
            spawnerUnit
        } else null
    }
    override fun update() {
        super.update()
        if(getOwner() == null) {
            destroy()
            Fx.unitCapKill.at(this)
        }
    }
}