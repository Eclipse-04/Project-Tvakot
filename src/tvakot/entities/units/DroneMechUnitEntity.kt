package tvakot.entities.units

import mindustry.Vars.world
import mindustry.gen.Building
import mindustry.gen.MechUnit

class DroneMechUnitEntity : MechUnit() {
    var spawnerBuilding: Int? = null
    override fun cap(): Int {
        return count() + 3
    }
    //totally allowed by MEEP (real)
    fun getPad(): Building?{
        return if(spawnerBuilding != null) world.build(spawnerBuilding!!) else null
    }
    override fun update() {
        super.update()
        if(getPad() == null) destroy()
    }
}