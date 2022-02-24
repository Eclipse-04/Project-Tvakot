package tvakot.content

import arc.graphics.Color
import mindustry.content.Fx
import mindustry.ctype.ContentList
import mindustry.entities.bullet.BasicBulletType
import mindustry.entities.bullet.BulletType
import mindustry.graphics.Pal
import tvakot.entities.bullet.RicochetBulletType
import tvakot.entities.bullet.VectorHomingBulletType


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
        shatterBullet = BasicBulletType().apply {
            damage = 9f
            lifetime = 15f
            shrinkX = 1f
            shrinkY = 1f
            speed = 2f
            despawnEffect = Fx.none
            hitEffect = despawnEffect
        }
        pulseWallBullet = VectorHomingBulletType().apply {
            damage = 18f
            speed = 3.2f
            recoil = 0.4f
            shrinkY = 0f
            homingRange = 220f
            homingMultiplier = 0.3f
            lifetime = 140f
            backColor = Pal.lancerLaser
            frontColor = Color.white
            width = 8f
            height = 8f
            sprite = "circle-bullet"
            trailWidth = 4f
            trailLength = 4
            trailColor = backColor
            hitEffect = Fx.none
            despawnEffect = hitEffect
        }
        standardHomingMissle = VectorHomingBulletType().apply {
            damage = 18f
            speed = 3.2f
            recoil = 0.2f
            shrinkY = 0f
            homingRange = 320f
            homingMultiplier = 0.23f
            splashDamageRadius = 25f
            splashDamage = 8f
            lifetime = 140f
            width = 8f
            height = 8f
            sprite = "missile"
            trailChance = 0.05f
            trailEffect = Fx.smoke
            backColor = Pal.bulletYellowBack
            frontColor = Pal.bulletYellow
            hitEffect = Fx.blastExplosion
            despawnEffect = hitEffect
        }
        homingMissleXaonpen = VectorHomingBulletType().apply {
            damage = 18f
            speed = 3.2f
            shrinkY = 0f
            homingRange = 220f
            homingMultiplier = 0.35f
            splashDamageRadius = 25f
            splashDamage = 12f
            lifetime = 180f
            width = 8f
            height = 8f
            sprite = "missile"
            trailChance = 0.05f
            trailEffect = Fx.smoke
            backColor = Color.valueOf("5c567a")
            frontColor = Color.valueOf("7685aa")
            hitEffect = Fx.blastExplosion
            despawnEffect = hitEffect
        }
        homingMissleDense = VectorHomingBulletType().apply {
            damage = 24f
            speed = 4.2f
            reloadMultiplier = 0.84f
            shrinkY = 0f
            homingRange = 220f
            homingMultiplier = 0.23f
            splashDamageRadius = 25f
            splashDamage = 15f
            lifetime = 180f
            width = 8f
            height = 8f
            sprite = "missile"
            trailChance = 0.05f
            trailEffect = Fx.smoke
            backColor = Color.valueOf("6e7080")
            frontColor = Color.valueOf("989aa4")
            hitEffect = Fx.blastExplosion
            despawnEffect = hitEffect
        }
        standardRicochet = RicochetBulletType().apply{
            width = 7f
            height = 9f
            speed = 3.5f
            damage = 18f
            bounceTime = 4
            ammoMultiplier = 4f
            lifetime = 60f
            shootEffect = Fx.shootSmall
            smokeEffect = Fx.shootSmallSmoke
        }
    }
    companion object {
        lateinit var LaserTowerBulletType : BulletType
        lateinit var LaserTowerLargeBulletType : BulletType
        lateinit var shatterBullet : BasicBulletType
        lateinit var pulseWallBullet : BasicBulletType
        lateinit var homingMissleXaonpen : VectorHomingBulletType
        lateinit var homingMissleDense : VectorHomingBulletType
        lateinit var standardHomingMissle : VectorHomingBulletType
        lateinit var standardRicochet: RicochetBulletType
    }
}