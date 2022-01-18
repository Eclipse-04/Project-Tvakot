package tvakot.content

import mindustry.content.Fx
import mindustry.ctype.ContentList
import mindustry.entities.bullet.BasicBulletType
import mindustry.entities.bullet.BulletType

class TvaBullets : ContentList{
    override fun load() {
        LaserTowerBulletType = BasicBulletType().apply {
            damage = 15f
            lifetime = 10f
            speed = 0f
            hitEffect = Fx.hitLancer
        }
    }
    companion object {
        lateinit var LaserTowerBulletType : BulletType
    }
}