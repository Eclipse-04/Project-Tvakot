package tvakot.entities.bullet

import mindustry.gen.Bullet
import mindustry.gen.Hitboxc

open class RicochetBulletType(var bounceTime: Int, pierce: Int) : TvaBaseBulletType() {
    init {
        pierceCap = pierce
        this.pierce = true
    }
    override fun hitEntity(b: Bullet, entity: Hitboxc, health: Float) {
        super.hitEntity(b, entity, health)
        hit(b)
    }
    override fun hit(b: Bullet) {
        super.hit(b)
        if(target(b) != null && b.collided.size < bounceTime) b.vel.trns(b.angleTo(target(b)), speed)
    }
}