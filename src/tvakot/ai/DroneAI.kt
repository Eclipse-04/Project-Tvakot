package tvakot.ai

import arc.math.Angles
import mindustry.Vars
import mindustry.entities.Units
import mindustry.entities.units.AIController
import mindustry.gen.Building
import mindustry.gen.Teamc
import mindustry.gen.Unit
import mindustry.world.meta.BlockFlag
import tvakot.entities.units.DroneUnitEntity
import tvakot.entities.units.UnitState

//special AI used by drone
open class DroneAI : AIController() {
    var rallyDst = 50f
    var attackDst = 130f
    var retreat = false
    var retreatHealth = -1f
    lateinit var u: DroneUnitEntity
    override fun init() {
        if(unit !is DroneUnitEntity) throw IllegalStateException("DroneAI are only assignable to DroneUnitEntity!") else {
            u = unit as DroneUnitEntity
        }
        attackDst = unit.range()
        if(retreatHealth <= 0f) retreatHealth = unit.maxHealth / 2
    }
    override fun updateMovement() {
        if (retreat && unit.health <= retreatHealth || u.state == UnitState.retreat) u.setUnitState(UnitState.retreat) else if(target == null) u.setUnitState(UnitState.idle) else u.setUnitState(UnitState.working)
        when(u.state){
            UnitState.working ->
                if (!unit.type.circleTarget) {
                    moveTo(target, unit.type.range * 0.8f)
                    unit.lookAt(target)
                } else {
                    attack(unit.range())
                }
            UnitState.idle -> idle()
            UnitState.retreat -> retreat()
        }
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
        if(u.spawnerUnit != null) return Units.closestTarget(unit.team, x, y, range,
            { u1: Unit -> u1.checkTarget(air, ground) && u1.within(u.spawnerUnit, attackDst) }
        ) { t: Building -> ground && t.within(u.spawnerUnit, attackDst) }
        if (u.spawnerBuilding != null){
            //possibility of returning null which cause crashes
            val b = Vars.world.build(u.spawnerBuilding!!) ?: return null
            return Units.closestTarget(unit.team, x, y, range,
                { u1: Unit -> u1.checkTarget(air, ground) && u1.within(b, attackDst) }
            ) { t: Building -> ground && t.within(b, attackDst) }
        }
        return null
    }
    fun idle(){
        if(u.spawnerBuilding != null) circle(Vars.world.build(u.spawnerBuilding!!), rallyDst)
        if(u.spawnerUnit != null) circle(u.spawnerUnit, rallyDst)
    }
    fun retreat() {
        val t = targetFlag(unit.x, unit.y, BlockFlag.repair, false)
        if(unit.health < unit.maxHealth && t != null) {
            circle(t, rallyDst)
        } else {
            u.setUnitState(UnitState.idle)
            idle()
        }
    }

}