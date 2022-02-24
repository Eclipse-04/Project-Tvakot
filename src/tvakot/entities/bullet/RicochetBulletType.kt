package tvakot.entities.bullet

import mindustry.entities.Units
import mindustry.entities.bullet.BasicBulletType
import mindustry.gen.Bullet
import mindustry.gen.Hitboxc
import mindustry.gen.Teamc

open class RicochetBulletType : BasicBulletType() {
    var bounceTime = 3
    var range = 110f
    init {
        pierce = true
        pierceCap = bounceTime
    }

    override fun hitEntity(b: Bullet, entity: Hitboxc, health: Float) {
        super.hitEntity(b, entity, health)
        val target: Teamc? = if (healPercent > 0) {
            Units.closestTarget(null, b.x, b.y, range,
                { e -> e.checkTarget(collidesAir, collidesGround) && e.team !== b.team && !b.hasCollided(e.id) }
            ) { t -> collidesGround && (t.team !== b.team || t.damaged()) && !b.hasCollided(t.id) }
        } else {
            Units.closestTarget( b.team,  b.x,  b.y, range,
                { e -> e.checkTarget(collidesAir, collidesGround) && !b.hasCollided(e.id) }) { t ->
                collidesGround && !b.hasCollided(t.id)
            }
        }
        if(target != null) b.vel.trns(b.angleTo(target), speed)
    }
}