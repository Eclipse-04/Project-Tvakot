package tvakot.entities.abilities

import arc.struct.ObjectMap
import arc.struct.Seq
import mindustry.content.Fx
import mindustry.entities.abilities.Ability
import mindustry.gen.Unit
import mindustry.type.UnitType
import tvakot.content.TvaUnitTypes
import tvakot.entities.units.DroneUnitEntity

open class DroneSpawnAbility : Ability {
    var drone = TvaUnitTypes.healDrone
    var count = 3
    var spawnEffect = Fx.spawn
    constructor(drone: UnitType, count: Int, rallyDst: Float){
        this.drone = drone
        this.count = count
    }

    protected val drones = ObjectMap<Unit, Seq<Unit>>()

    override fun update(unit: Unit) {
        if (drones[unit] == null) drones.put(unit, Seq.with()) else {
            if (unit.isShooting && drones[unit].size < count) {
                val drone = drone.spawn(unit.team, unit.getX(), unit.getY()) as? DroneUnitEntity ?: throw IllegalStateException("Drone type does not create units of type DroneUnitEntity!")
                drones[unit].add(drone)
                drone.vel.trns(drones[unit].size * 360f / count, drone.type.hitSize)
                drone.spawnerUnit = unit
                drone.add()
                spawnEffect.at(drone)
            }

            if (drones[unit].size > 0) {
                val ar = drones[unit]
                ar.filter { u -> u.isValid }
            }
        }
    }
}

