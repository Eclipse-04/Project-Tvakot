package tvakot.entities.bullet

import arc.util.Time
import arc.util.Tmp
import mindustry.gen.Bullet
import mindustry.gen.Teamc

class VectorHomingBulletType : TvaBaseBulletType() {
    var homingMultiplier = 0.07f
    init {
        rally = true
    }
    override fun update(b: Bullet) {
        super.update(b)
        b.vel.add(Tmp.v2.trns(b.angleTo(target(b)), homingMultiplier * Time.delta))
    }
}