package tvakot.content

import arc.graphics.Color
import mindustry.content.Fx
import mindustry.ctype.ContentList
import mindustry.entities.bullet.BasicBulletType
import mindustry.entities.bullet.BulletType
import tvakot.entities.bullet.HomingAccelBullet


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
        homingMissleXaonpen = HomingAccelBullet().apply {
            damage = 18f
            speed = 4.2f
            shrinkY = 0f
            homingRange = 220f
            homingMultiplier = 0.34f
            splashDamageRadius = 25f
            splashDamage = 10f
            lifetime = 120f
            width = 8f
            height = 8f
            sprite = "missile"
            trailChance = 0.05f
            trailEffect = Fx.smoke
            hitEffect = despawnEffect
            backColor = Color.valueOf("5c567a")
            frontColor = Color.valueOf("7685aa")
            hitEffect = Fx.blastExplosion
            despawnEffect = Fx.blastExplosion
        }
        homingMissleDense = HomingAccelBullet().apply {
            damage = 24f
            speed = 5.2f
            reloadMultiplier = 0.84f
            shrinkY = 0f
            homingRange = 220f
            homingMultiplier = 0.45f
            splashDamageRadius = 25f
            splashDamage = 10f
            lifetime = 180f
            width = 8f
            height = 8f
            sprite = "missile"
            trailChance = 0.05f
            trailEffect = Fx.smoke
            hitEffect = despawnEffect
            backColor = Color.valueOf("6e7080")
            frontColor = Color.valueOf("989aa4")
            hitEffect = Fx.blastExplosion
            despawnEffect = Fx.blastExplosion
        }
    }
    companion object {
        lateinit var LaserTowerBulletType : BulletType
        lateinit var LaserTowerLargeBulletType : BulletType
        lateinit var ShatterBullet : BasicBulletType
        lateinit var homingMissleXaonpen : HomingAccelBullet
        lateinit var homingMissleDense : HomingAccelBullet
    }
}