package tvakot.content

import mindustry.content.Fx
import mindustry.ctype.ContentList
import mindustry.entities.bullet.BasicBulletType
import mindustry.entities.bullet.BulletType


class TvaBullets : ContentList {
    override fun load() {
        LaserTowerBulletType = BasicBulletType().apply {
            lifetime = 10f
            speed = 0f
            damage = 0f
            hitEffect = Fx.hitLancer
        }
        LaserTowerLargeBulletType = BasicBulletType().apply {
            lifetime = 10f
            speed = 0f
            damage = 0f
            hitEffect = Fx.hitLancer
            status = TvaStatusEffects.pulseShock
        }
        ShatterBullet = BasicBulletType().apply {
            damage = 9f
            lifetime = 15f
            shrinkX = 1f
            shrinkY = 1f
            speed = 2f
            despawnEffect = Fx.none
            hitEffect = despawnEffect
        }
    }
    companion object {
        lateinit var LaserTowerBulletType : BulletType
        lateinit var LaserTowerLargeBulletType : BulletType
        lateinit var ShatterBullet : BasicBulletType
    }
}