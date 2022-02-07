package tvakot.ai

import mindustry.entities.units.AIController
import mindustry.gen.Building


open class BuildingDroneAI : AIController() {
    var build: Building? = null
    var range: Float = 200f
    open fun addAI(build: Building, range: Float): BuildingDroneAI {
        return BuildingDroneAI().apply {
            this.build = build
            this.range = range
        }
    }
    override fun updateUnit() {
        if(build != null && !build!!.isValid) unit.destroy()
        super.updateUnit()
    }
    override fun updateMovement() {
        if(idle()){
            moveTo(build, unit.range() * 0.25f)
        }
    }
    open fun idle(): Boolean{
        return true
    }
}