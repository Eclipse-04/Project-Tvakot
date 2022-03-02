package tvakot.entities.bullet

import arc.util.Time
import arc.util.Tmp
import mindustry.gen.Bullet

class VectorHomingBulletType : TvaBaseBulletType() {
    var homingMultiplier = 0.07f
    override fun update(b: Bullet) {
        super.update(b)
        b.vel.add(Tmp.v2.trns(b.angleTo(target(b)), homingMultiplier * Time.delta)).clamp(0f, speed)
    }
}