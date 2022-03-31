package tvakot.entities.abilities

import arc.struct.ObjectMap
import arc.struct.Seq
import arc.util.Time
import mindustry.Vars
import mindustry.content.Fx
import mindustry.content.UnitTypes
import mindustry.entities.abilities.Ability
import mindustry.gen.Unit
import mindustry.type.UnitType
import tvakot.ai.DroneAI
import tvakot.entities.units.DroneUnitEntity

open class DroneSpawnAbility : Ability {
    var drone = UnitTypes.alpha
    var count = 3
    var spawnEffect = Fx.spawn
    var rallyDst = 50f
    var reloadTime = 30f

    constructor(drone: UnitType, count: Int, rallyDst: Float, reloadTime: Float){
        this.drone = drone
        this.count = count
        this.rallyDst = rallyDst
        this.reloadTime = reloadTime
    }

    protected val drones = ObjectMap<Unit, Seq<Unit>>()
    protected val reload = ObjectMap<Unit, Float>()

    override fun update(unit: Unit) {
        if (reload[unit] == null) reload.put(unit, 0f) else if(reload[unit] / reloadTime <= count) reload.put(unit, reload[unit] + Time.delta * Vars.state.rules.unitBuildSpeed(unit.team))
        if (drones[unit] == null) drones.put(unit, Seq.with()) else {
            if (unit.isShooting && drones[unit].size < count && reload[unit] >= reloadTime) {
                val drone = drone.spawn(unit.team, unit.getX(), unit.getY()) as? DroneUnitEntity ?: throw IllegalStateException("Drone type does not create units of type DroneUnitEntity!")
                drones[unit].add(drone)
                drone.vel.trns(drones[unit].size * 360f / count, drone.type.hitSize)
                drone.spawnerUnit = unit
                drone.add()
                if(drone.controller is DroneAI) (drone.controller as DroneAI).rallyDst = rallyDst
                spawnEffect.at(drone)
                reload.put(unit, reload[unit] - reloadTime)
            }

            if (drones[unit].size > 0) {
                val ar = drones[unit]
                ar.filter { u -> u.isValid }
            }
        }
    }

}

