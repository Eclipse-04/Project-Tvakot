package tvakot.content

import arc.graphics.Color
import arc.graphics.g2d.Draw
import arc.graphics.g2d.Fill
import arc.graphics.g2d.Lines
import arc.math.Interp
import mindustry.content.Fx
import mindustry.content.StatusEffects
import mindustry.ctype.ContentList
import mindustry.entities.Effect
import mindustry.entities.bullet.BasicBulletType
import mindustry.entities.bullet.BulletType
import mindustry.gen.Bullet
import mindustry.gen.Sounds
import mindustry.graphics.Pal
import tvakot.entities.bullet.EnergyOrbBulletType
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
            lifetime = 140f
            homingRange = 320f
            homingMultiplier = 0.23f
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
            targetRangeMultiplier = 0.5f
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
        standardRicochet = RicochetBulletType(3, 3).apply{
            width = 9f
            height = 12f
            speed = 3.8f
            damage = 18f
            ammoMultiplier = 4f
            lifetime = 90f
            shootEffect = Fx.shootSmall
            smokeEffect = Fx.shootSmallSmoke
            pierceBuilding = true
            trailLength = 4
            trailColor = Pal.bulletYellowBack
        }
        lightningRicochet = RicochetBulletType(5, 5).apply{
            width = 9f
            height = 22f
            speed = 5.8f
            damage = 27f
            ammoMultiplier = 4f
            lifetime = 40f
            shootEffect = Fx.lancerLaserShoot
            hitEffect = Fx.hitLancer
            despawnEffect = hitEffect
            smokeEffect = Fx.shootBigSmoke
            trailWidth = 3f
            pierceBuilding = true
            trailLength = 9
            frontColor = Color.white
            backColor = Pal.lancerLaser
            trailColor = backColor
        }
        surgeLightningRicochet = RicochetBulletType(5, 5).apply{
            width = 9f
            height = 22f
            speed = 5.8f
            damage = 18f
            reloadMultiplier = 0.8f
            ammoMultiplier = 4f
            lifetime = 40f
            lightning = 1
            lightningDamage = 14f
            splashDamage = 11f
            splashDamageRadius = 5f
            shootEffect = TvaFx.surgeRicochetShoot
            hitEffect = Fx.flakExplosion
            despawnEffect = hitEffect
            smokeEffect = Fx.shootBigSmoke
            trailWidth = 3f
            pierceBuilding = true
            trailLength = 9
            frontColor = Color.white
            backColor = Pal.surge
            trailColor = backColor
        }
        standardOrbLightning = object : EnergyOrbBulletType(){
            override fun draw(b: Bullet) {
                super.draw(b)
                val lerp = b.time / b.lifetime
                val interp = Interp.exp10Out
                Lines.stroke(lerp * 2, backColor)
                for (i in 0 until 3){
                    Lines.swirl(
                        b.x, b.y,
                        8f,
                        0.24f,
                        i * 120 + b.time * 4
                    )
                }
            }
            init {
                despawnEffect = Effect(30f){ e ->
                    Lines.stroke(e.fout() * 3, backColor)
                    Lines.circle(e.x, e.y, splashDamageRadius)
                    Draw.alpha(e.fout() / 2)
                    Fill.circle(e.x, e.y, splashDamageRadius)
                    Fill.circle(e.x, e.y, splashDamageRadius / 4 * e.fout())
                }
                trailLength = 12
                shrinkY = 0f
                trailColor = Pal.lancerLaser
                orbColor = trailColor
                collides = false
                frontColor = Color.white
                backColor = orbColor
                lightningReload = 20f
                orbDamage = 25f
                lifetime = 190f
                sprite = "circle-bullet"
                width = 10f
                height = 10f
                status = StatusEffects.shocked
                splashDamage = 23f
                splashDamageRadius = 30f
                hitSound = Sounds.plasmaboom
            }
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
        lateinit var lightningRicochet: RicochetBulletType
        lateinit var surgeLightningRicochet: RicochetBulletType
        lateinit var standardOrbLightning: EnergyOrbBulletType
    }
}