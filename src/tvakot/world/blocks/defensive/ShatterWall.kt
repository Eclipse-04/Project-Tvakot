package tvakot.world.blocks.defensive

import arc.math.Mathf
import mindustry.content.Bullets
import mindustry.entities.bullet.BulletType
import mindustry.gen.Bullet
import mindustry.world.blocks.defense.Wall

open class ShatterWall(name: String) : Wall(name){
    var damagePerShatter = 1f
    var shatterChance: Double = 0.0
    var maxShatter = 0
    var shatterInaccuracy = 5f
    var velInaccuracy = 0.2f
    var shatterBullet: BulletType = Bullets.fragGlass
    inner class ShatterWallBuild : WallBuild() {
        override fun collision(bullet: Bullet): Boolean{
            super.collision(bullet)
            if(bullet.type.hittable) {
                val shatterAmount = Mathf.clamp((bullet.damage() / damagePerShatter).toInt(), 1, maxShatter)
                for (i in 0 until shatterAmount) {
                    if (Mathf.chance(shatterChance)) {
                        shatterBullet.create(
                            this, this.team, bullet.x, bullet.y, bullet.rotation()
                            + Mathf.range(shatterInaccuracy) + 180, Mathf.random(velInaccuracy) + 1
                        )
                    }
                }
            }
            return true
        }
    }
}