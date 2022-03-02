package tvakot.entities.bullet

import mindustry.entities.Units
import mindustry.entities.bullet.BasicBulletType
import mindustry.gen.Bullet
import mindustry.gen.Teamc

open class TvaBaseBulletType : BasicBulletType() {
    var targetRangeMultiplier = 1f
    var range = 110f
    override fun range(): Float {
        return super.range() * targetRangeMultiplier
    }
    open fun target(b: Bullet): Teamc? {
        var target: Teamc? = if (healPercent > 0) {
            Units.closestTarget(null, b.x, b.y, homingRange,
                { e -> e.checkTarget(collidesAir, collidesGround) && e.team !== b.team && !b.hasCollided(e.id) }
            ) { t -> collidesGround && (t.team !== b.team || t.damaged()) && !b.hasCollided(t.id) }
        } else {
            Units.closestTarget( b.team,  b.x,  b.y,
                homingRange,
                { e -> e.checkTarget(collidesAir, collidesGround) && !b.hasCollided(e.id) }) { t ->
                collidesGround && !b.hasCollided(t.id)
            }
        }
        if(target == null) target = b.owner as Teamc?
        return target
    }
}