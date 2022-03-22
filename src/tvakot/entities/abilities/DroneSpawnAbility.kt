package tvakot.entities.abilities

import arc.struct.Seq
import mindustry.entities.abilities.Ability
import mindustry.gen.Unit
import mindustry.type.UnitType
import tvakot.content.TvaUnitTypes
import tvakot.entities.units.DroneUnitEntity

open class DroneSpawnAbility() : Ability(){
    //TODO WIP
    var drone: UnitType = TvaUnitTypes.healDrone
    var droneCount = 3
    protected var drones = Seq<Unit>()
    constructor(unit: UnitType, count: Int) : this() {
        drone = unit
        droneCount = count
    }

    override fun update(unit: Unit) {
        if(unit.isShooting && drones.size < droneCount){
            val u = drone.spawn(unit.team, unit.getX(), unit.getY()) as DroneUnitEntity
            u.spawnerUnit = unit
            drones.add(u)
            u.add()
        }
        if(drones.size > 0) for(i in drones){
            if(!i.isValid) drones.remove(i)
        }
    }
}