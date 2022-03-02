package tvakot.entities.bullet

import mindustry.entities.Units
import mindustry.gen.Bullet
import mindustry.gen.Hitboxc
import mindustry.gen.Teamc

open class RicochetBulletType(var bounceTime: Int, pierce: Int) : TvaBaseBulletType() {
    init {
        this.bounceTime = bounceTime
        pierceCap = pierce
        this.pierce = true
    }
    override fun hitEntity(b: Bullet, entity: Hitboxc, health: Float) {
        super.hitEntity(b, entity, health)
        hit(b)
    }
    override fun target(b: Bullet): Teamc? {
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
        return target
    }
    override fun hit(b: Bullet) {
        super.hit(b)
        if(target(b) != null) b.vel.trns(b.angleTo(target(b)), speed)
    }
}