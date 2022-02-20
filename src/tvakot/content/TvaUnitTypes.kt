package tvakot.content

import arc.func.Prov
import arc.graphics.Color
import mindustry.ai.types.MinerAI
import mindustry.ai.types.RepairAI
import mindustry.content.Fx
import mindustry.ctype.ContentList
import mindustry.entities.bullet.LaserBoltBulletType
import mindustry.gen.Sounds
import mindustry.graphics.Pal
import mindustry.type.UnitType
import mindustry.type.Weapon
import mindustry.type.ammo.PowerAmmoType
import tvakot.entities.units.DroneUnitEntity
import tvakot.entities.units.DroneUnitType


class TvaUnitTypes : ContentList{
    override fun load() {
        healDrone = object : DroneUnitType("heal-drone") {
            init {
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

        draugMiner = object : DroneUnitType("draug") {
            init {
                defaultController = Prov{ MinerAI() }
                speed = 2.85f
                accel = 0.09f

                drag = 0.1f
                flying = true
                mineTier = 2
                range = 65f
                mineSpeed = 4.2f
                rotateSpeed = 6.5f
                health = 250f
                engineOffset = 4.5f
                engineSize = 2.7f
                constructor = Prov{ DroneUnitEntity() }
                //weapons.add(Weapon("you have incurred my wrath. prepare to die."))
            }
        }
    }
    companion object {
        lateinit var healDrone: UnitType
        lateinit var draugMiner: UnitType
    }
}