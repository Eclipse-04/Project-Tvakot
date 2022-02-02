package tvakot.content

import mindustry.content.Bullets
import mindustry.content.Fx
import mindustry.content.Items
import mindustry.content.Liquids
import mindustry.ctype.ContentList
import mindustry.type.Category
import mindustry.type.ItemStack.with
import mindustry.type.LiquidStack
import mindustry.world.Block
import mindustry.world.blocks.payloads.PayloadDeconstructor
import mindustry.world.blocks.power.DecayGenerator
import mindustry.world.meta.BuildVisibility
import tvakot.world.blocks.crafting.HeatCrafter
import tvakot.world.blocks.defensive.ShatterWall
import tvakot.world.blocks.defensive.turret.OverdriveTurret
import tvakot.world.blocks.distribution.HeatPipe
import tvakot.world.blocks.distribution.HeatRouter
import tvakot.world.blocks.effect.LaserTower
import tvakot.world.blocks.power.BurnerHeatGenerator
import tvakot.world.blocks.power.ThermalHeatGenerator
import tvakot.world.blocks.power.TurbineGenerator
import tvakot.world.draw.DrawBoiler

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
        //region distribution
        heatPipe = object : HeatPipe("heat-pipe"){}.apply {
            requirements(
                Category.distribution,
                with(Items.metaglass, 8, Items.titanium, 10, Items.graphite, 8)
            )
            health = 80
            buildCostMultiplier = 0.3f
            heatCapacity = 500f
            heatLoss = 0.0005f
        }
        heatRouter = object : HeatRouter("heat-router"){}.apply {
            requirements(
                Category.distribution,
                with(Items.metaglass, 16, Items.titanium, 8, Items.graphite, 14)
            )
            health = 160
            buildCostMultiplier = 0.3f
            heatCapacity = 500f
            heatLoss = 0.0005f
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
        geothermalCollector = object : ThermalHeatGenerator("thermal-heat-gen"){}.apply{
            requirements(
                Category.power,
                with(Items.lead, 65, Items.graphite, 45, Items.silicon, 32, Items.metaglass, 28)
            )
            health = 850
            size = 2
            heatLoss = 0.005f
            heatCapacity = 500f
            heatGenerate = 3f
            generateEffect = Fx.redgeneratespark
        }
        turbine = object : TurbineGenerator("turbine"){}.apply {
            requirements(
                Category.power,
                with(Items.copper, 55, Items.lead, 25, Items.graphite, 35)
            )
            size = 2
            health = 750
            minLiquidRequired = 12f
            liquidCapacity = 20f
            consumes.liquid(TvaLiquids.steam, 0.1f)
            powerProduction = 7.5f
        }
        thermalTurbine = object : TurbineGenerator("thermal-turbine"){}.apply {
            requirements(
                Category.power,
                with(Items.graphite, 95, Items.silicon, 65, Items.metaglass, 55, Items.thorium, 75)
            )
            rotorAmount = 2
            size = 3
            health = 2750
            spread = 6f
            minLiquidRequired = 75f
            liquidCapacity = 100f
            consumes.liquid(TvaLiquids.steam, 0.37f)
            powerProduction = 24f
            bubbles = 35
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
        //region crafting
        boiler = object : HeatCrafter("boiler"){}.apply {
            requirements(
                Category.crafting,
                with(Items.copper, 45, Items.lead, 35, Items.graphite, 25)
            )
            size = 2
            heatLoss = 0.005f
            health = 240
            customConsume.heat = 2f
            craftTime = 6f
            drawerCustom = DrawBoiler()
            consumes.liquid(Liquids.water, 0.2f)
            outputLiquid = LiquidStack(TvaLiquids.steam, 1.4f)
            heatCapacity = 300f
            craftEffect = Fx.none
        }
        heater = object : BurnerHeatGenerator("heater"){}.apply {
            requirements(
                Category.crafting,
                with(Items.copper, 18, Items.lead, 22)
            )
            health = 90
            heatLoss = 0.005f
            generateTime = 150f
            heatGenerate = 4f
            heatCapacity = 300f
            squareSprite = false
        }
        //endregion
        //region Payload
        buildingDisassembler = object : PayloadDeconstructor("building-dis"){}.apply{
            requirements(
                Category.units,
                with(Items.titanium, 55, Items.silicon, 45, Items.graphite, 25)
            )
            consumes.power(3.5f)
            size = 3
            deconstructSpeed = 4f
            maxPayloadSize = 2f
            health = 720
        }
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
        lateinit var buildingDisassembler: Block
        lateinit var heater: Block
        lateinit var boiler: Block
        lateinit var turbine: Block
        lateinit var geothermalCollector: Block
        lateinit var thermalTurbine: Block
        lateinit var heatPipe: Block
        lateinit var heatRouter: Block
    }
}