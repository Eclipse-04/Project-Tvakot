package tvakot.entities.bullet

import arc.util.Time
import arc.util.Tmp
import mindustry.entities.Units
import mindustry.entities.bullet.BasicBulletType
import mindustry.gen.Bullet
import mindustry.gen.Teamc

class HomingAccelBullet : BasicBulletType() {
    var homingMultiplier = 0.07f
    override fun update(b: Bullet) {
        super.update(b)
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
        b.vel.add(Tmp.v2.trns(b.angleTo(target), homingMultiplier * Time.delta)).clamp(0f, speed)
    }
}