package tvakot.content

import arc.func.Prov
import arc.graphics.Color
import mindustry.ai.types.MinerAI
import mindustry.ai.types.RepairAI
import mindustry.content.Bullets
import mindustry.content.Fx
import mindustry.content.Items
import mindustry.ctype.ContentList
import mindustry.entities.abilities.ShieldRegenFieldAbility
import mindustry.entities.bullet.LaserBoltBulletType
import mindustry.gen.MechUnit
import mindustry.gen.Sounds
import mindustry.gen.Unit
import mindustry.graphics.Pal
import mindustry.type.UnitType
import mindustry.type.Weapon
import mindustry.type.ammo.ItemAmmoType
import mindustry.type.ammo.PowerAmmoType
import tvakot.ai.DroneAI
import tvakot.entities.abilities.DroneSpawnAbility
import tvakot.entities.bullet.RicochetBulletType
import tvakot.entities.units.DroneUnitEntity


class TvaUnitTypes : ContentList{
    override fun load() {
        //ground attack
        castle = UnitType("castle").apply{
            speed = 0.7f
            hitSize = 8f
            health = 250f
            mechSideSway = 0.3f
            constructor = Prov{ MechUnit.create() }
            weapons.add(Weapon("tvakot-rocketlauncher").apply{
                reload = 40f
                x = 4.8f
                shootSound = Sounds.missile
                shots = 2
                shotDelay = 3f
                ejectEffect = Fx.shootBig
                inaccuracy = 10f
                bullet = TvaBullets.standardHomingMissle
            })
        }
        bastille = UnitType("bastille").apply{
            speed = 0.5f
            hitSize = 16f
            armor = 3f
            health = 550f
            mechSideSway = 0.4f
            rotateShooting = true
            constructor = Prov{ MechUnit.create() }
            weapons.add(Weapon("tvakot-mini-vulcan").apply{
                reload = 7f
                y = 4f
                x = 5.5f
                ejectEffect = Fx.shootBig
                rotate = true
                inaccuracy = 5f
                bullet = Bullets.standardCopper
            })

            weapons.add(Weapon("tvakot-heavy-artillery").apply{
                reload = 60f
                y = -0.75f
                x = 8f
                rotate = true
                ejectEffect = Fx.shootBig
                bullet = Bullets.artilleryDense
            })

            weapons.add(TvaWeapons.diarrhea)
        }
        citadel = UnitType("citadel").apply{
            speed = 0.36f
            hitSize = 20f
            health = 9520f
            armor = 12f
            mechFrontSway = 1f
            constructor = Prov{ MechUnit.create() }
            mechStepParticles = true
            mechStepShake = 0.15f
            singleTarget = true
            drownTimeMultiplier = 4f
            weapons.add(Weapon("tvakot-heavy-big-shot").apply{
                top = false
                reload = 25f
                x = 16f
                y = -0.75f
                shootY = 9f
                shots = 2
                shotDelay = 4f
                recoil = 5f
                shake = 2f
                ejectEffect = Fx.casing3
                shootSound = Sounds.bang
                ejectEffect = Fx.shootBig
                inaccuracy = 4f
                bullet = RicochetBulletType(4, 4).apply {
                    speed = 7f
                    damage = 30f
                    width = 11f
                    trailLength = 9
                    trailColor = Pal.bulletYellowBack
                    height = 25f
                    lifetime = 30f
                    hitEffect = Fx.flakExplosion
                    shootEffect = Fx.shootBig
                }
            })
            weapons.add(TvaWeapons.smallRapidRocket)
        }
        //endregion
        //drone
        healDrone = object : UnitType("heal-drone") {
            init {
                isCounted = false
                speed = 3.4f
                defaultController = Prov{ RepairAI() }
                accel = 0.07f
                drag = 0.1f
                flying = true
                rotateSpeed = 8.5f
                health = 105f
                engineOffset = 3f
                engineSize = 2f
                faceTarget = true
                constructor = Prov{ DroneUnitEntity() }
                engineColor = Pal.heal
                ammoType = PowerAmmoType(1000f)
                isCounted = false
                weapons.add(Weapon("drone-healer-cannon").apply {
                    reload = 20f
                    x = 0f
                    mirror = false
                    shootSound = Sounds.lasershoot
                    ejectEffect = Fx.none
                    bullet = LaserBoltBulletType(5.2f, 3f).apply{
                        lifetime = 18f
                        shootEffect = Fx.none
                        healPercent = 5f
                        collidesTeam = true
                        backColor = Pal.heal
                        frontColor = Color.white
                    }
                })
            }
        }
        alphaDrone = object : UnitType("alpha-drone") {
            init {
                circleTarget = true
                isCounted = false
                speed = 3.4f
                defaultController = Prov{ DroneAI() }
                accel = 0.07f
                drag = 0.1f
                flying = true
                rotateSpeed = 11.5f
                health = 105f
                engineOffset = 3.75f
                engineSize = 2.5f
                faceTarget = true
                constructor = Prov{ DroneUnitEntity() }
                ammoType = ItemAmmoType(Items.copper, 10)
                ammoCapacity = 60
                isCounted = false
                weapons.add(Weapon("tvakot-gun").apply {
                    reload = 15f
                    x = 0f
                    mirror = false
                    bullet = Bullets.standardCopper
                })
            }
        }
        draugMiner = object : UnitType("draug") {
            init {
                isCounted = false
                defaultController = Prov{ MinerAI() }
                speed = 2.85f
                accel = 0.09f
                drag = 0.1f
                mineTier = 2
                flying = true
                range = 65f
                mineSpeed = 4.2f
                rotateSpeed = 6.5f
                health = 250f
                constructor = Prov{ DroneUnitEntity() }
                mineItems.add(TvaItems.xaopnen)
                //weapons.add(Weapon("you have incurred my wrath. prepare to die."))
            }
        }
        transportDrone = object : UnitType("transport-drone") {
            init {
                isCounted = false
                speed = 2.85f
                accel = 0.3f
                drag = 0.08f
                flying = true
                range = 65f
                itemCapacity = 100
                rotateSpeed = 2.5f
                hitSize = 6.25f
                health = 650f
                engineOffset = 5.5f
                engineSize = 5.5f
                constructor = Prov{ DroneUnitEntity() }
                abilities.add(ShieldRegenFieldAbility(30f, 120f, 60f, 5f))
                mineTier = 3
                mineSpeed = 3f
            }
        }
        //endregion
        //core units
        epsilon = object : UnitType("epsilon"){
            init {
                isCounted = false
                speed = 0.95f
                health = 550f
                canBoost = true
                hitSize = 8.5f
                buildSpeed = 1.5f
                mineTier = 2
                mineSpeed = 2f
                boostMultiplier = 1.4f
                constructor = Prov{ MechUnit.create() }
                abilities.add(DroneSpawnAbility(alphaDrone, 3, 30f))
                mechSideSway = 0.3f
                weapons.add(Weapon("tvakot-heavy-ricochet").apply{
                    top = false
                    reload = 10f
                    x = 6.5f
                    inaccuracy = 5f
                    bullet = TvaBullets.standardRicochet.apply {
                        buildingDamageMultiplier = 0.01f
                    }
                })
            }
        }
        //endregion
    }
    companion object {
        lateinit var healDrone: UnitType
        lateinit var alphaDrone: UnitType
        lateinit var draugMiner: UnitType
        lateinit var transportDrone: UnitType
        lateinit var castle: UnitType
        lateinit var bastille: UnitType
        lateinit var citadel: UnitType
        lateinit var epsilon: UnitType
    }
}