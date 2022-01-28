package tvakot.content

import mindustry.content.Bullets
import mindustry.content.Items
import mindustry.ctype.ContentList
import mindustry.type.Category
import mindustry.type.ItemStack.with
import mindustry.world.Block
import mindustry.world.blocks.power.DecayGenerator
import mindustry.world.meta.BuildVisibility
import tvakot.world.blocks.defensive.ShatterWall
import tvakot.world.blocks.defensive.turret.OverdriveTurret
import tvakot.world.blocks.effect.LaserTower

class TvaBlocks : ContentList {

    override fun load() {
        //region Turret
        laxo = object : OverdriveTurret("laxo"){
            init {
                requirements(
                    Category.turret,
                    with(Items.copper, 25, Items.lead, 15, Items.graphite, 12)
                )
                ammo(
                    Items.copper, Bullets.standardCopper,
                    Items.graphite, Bullets.standardDense,
                    Items.pyratite, Bullets.standardIncendiary,
                    Items.silicon, Bullets.standardHoming
                )
                health = 240
                maxAmmo = 60
                reloadTime = 3.5f
                heatPerShot = 0.3f
                range = 80f
                alternate = true
                shots = 2
                spread = 4f
                inaccuracy = 4f
                overdriveMax = 10f
                coolingAmount = 0.02f
            }
        }
        //endregion
        //region Power
        rtgCell = object : DecayGenerator("rtg-cell"){}.apply {
            requirements(
                Category.power,
                with(Items.lead, 56, Items.silicon, 23, Items.scrap, 50)
            )
            buildVisibility = BuildVisibility.sandboxOnly
            powerProduction = 1.75f
            health = 75
            itemDuration = 1840f
        }
        //endregion
        //region Defensive
        metaglassWall = object : ShatterWall("metaglass-wall"){}.apply {
            requirements(
                Category.defense,
                with(Items.metaglass, 24, Items.copper, 12)
            )
            damagePerShatter = 3f
            shatterChance = 0.7
            maxShatter = 7
            health = 1850
            size = 2
            shatterInaccuracy = 30f
            shatterBullet = TvaBullets.ShatterBullet
        }
        //endregion
        //region Effect
        pulseTower = object : LaserTower("pulse-tower"){}.apply {
            requirements(
                Category.effect,
                with(Items.titanium, 105, Items.silicon, 85, Items.graphite, 95, Items.thorium, 25)
            )
            consumes.power(7f)
            size = 2
            health = 2850
            reloadTime = 10f
            damageHit = 45f
            range = 180f
            sectorOffset = 3f
            laserBullet = TvaBullets.LaserTowerLargeBulletType
        }
        pulseTowerSmall = object : LaserTower("small-pulse-tower"){}.apply {
            requirements(
                Category.effect,
                with(Items.copper, 25, Items.silicon, 15, Items.graphite, 95)
            )
            consumes.power(2.5f)
            health = 650
            reloadTime = 25f
            damageHit = 12f
            range = 110f
            sectors = 2
            fulPerSector = 0.7f
            orbRadius = 0.8f
            sectorOffset = 1.6f
            rotDrawSpeed = 10f
            sectorStroke = 0.7f
        }
        //endregion
    }
    companion object {
        lateinit var rtgCell: Block
        lateinit var pulseTower: Block
        lateinit var pulseTowerSmall: Block
        lateinit var metaglassWall: Block
        lateinit var laxo: Block
    }
}