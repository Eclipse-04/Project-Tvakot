package tvakot.ai

import arc.math.Angles
import mindustry.Vars
import mindustry.entities.Units
import mindustry.entities.units.AIController
import mindustry.gen.Building
import mindustry.gen.Teamc
import mindustry.gen.Unit
import tvakot.entities.units.DroneUnitEntity

//special AI used by drone
class DroneAI : AIController() {
    var rallyDst = 45f
    var attackDst = 140f
    lateinit var u: DroneUnitEntity
    override fun init() {
        if(unit !is DroneUnitEntity) throw IllegalStateException("DroneAI are only assignable to DroneUnitEntity!") else {
            u = unit as DroneUnitEntity
        }
    }
    override fun updateMovement() {
        if (target != null && unit.hasWeapons()) {
            if (!unit.type.circleTarget) {
                moveTo(target, unit.type.range * 0.8f)
                unit.lookAt(target)
            } else {
                attack(60f)
            }
        } else idle()
    }
    fun attack(circleLength: Float) {
        vec.set(target).sub(unit)
        val ang = unit.angleTo(target)
        val diff = Angles.angleDist(ang, unit.rotation())
        if (diff > 70f && vec.len() < circleLength) {
            vec.setAngle(unit.vel().angle())
        } else {
            vec.setAngle(Angles.moveToward(unit.vel().angle(), vec.angle(), 26f))
        }
        vec.setLength(unit.speed())
        unit.moveAt(vec)
    }
    override fun target(x: Float, y: Float, range: Float, air: Boolean, ground: Boolean): Teamc? {
        return Units.closestTarget(unit.team, x, y, range,
            { u1: Unit -> u1.checkTarget(air, ground) && u1.within(u.spawnerUnit, attackDst) }
        ) { t: Building -> ground && t.within(u.spawnerUnit, attackDst) }
    }
    fun idle(){
        //funny kode
        if(unit !is DroneUnitEntity) return
        if(u.spawnerBuilding != null) circle(Vars.world.build(u.spawnerBuilding!!), rallyDst)
        if(u.spawnerUnit != null) circle(u.spawnerUnit, rallyDst)
    }
}