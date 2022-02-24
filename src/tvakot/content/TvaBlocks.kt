package tvakot.content

import mindustry.ai.types.MinerAI
import mindustry.ai.types.RepairAI
import mindustry.content.Bullets
import mindustry.content.Fx
import mindustry.content.Items
import mindustry.content.Liquids
import mindustry.ctype.ContentList
import mindustry.gen.Sounds
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
import tvakot.world.blocks.distribution.*
import tvakot.world.blocks.effect.LaserTower
import tvakot.world.blocks.power.*
import tvakot.world.blocks.units.UnitSpawner
import tvakot.world.draw.*

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
        novem = object : OverdriveTurret("novem"){
            init {
                requirements(
                    Category.turret,
                    with(TvaItems.denseIngot, 34, Items.silicon, 35, Items.graphite, 45, Items.plastanium, 32)
                )
                ammo(
                    TvaItems.xaopnenBar, TvaBullets.homingMissleXaonpen,
                    TvaItems.denseIngot, TvaBullets.homingMissleDense
                )
                health = 750
                maxAmmo = 60
                reloadTime = 2.1f
                heatPerShot = 0.45f
                range = 180f
                size = 2
                shootLength = 4.5f
                alternate = true
                shots = 2
                spread = 4.5f
                inaccuracy = 50f
                overdriveMax = 14f
                coolingAmount = 0.03f
                shootSound = Sounds.missile
            }
        }
        //endregion
        //region production
        //endregion
        //region distribution
        heatNode = object : HeatNode("heat-node"){}.apply {
            requirements(
                Category.distribution,
                with(Items.metaglass, 15, Items.titanium, 15, Items.graphite, 8)
            )
            health = 180
            buildCostMultiplier = 0.3f
            heatCapacity = 500f
            heatLoss = 0.0005
        }
        heatPipe = object : HeatPipe("heat-pipe"){}.apply {
            requirements(
                Category.distribution,
                with(Items.metaglass, 4, Items.graphite, 4)
            )
            health = 80
            heatCapacity = 600f
            heatLoss = 0.0005
            dumpSpeed = 0.2f
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
            floating = true
            heatLoss = 0.005
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
                with(TvaItems.xaopnenBar, 95, Items.silicon, 65, Items.metaglass, 55, Items.thorium, 75)
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
            shatterBullet = TvaBullets.shatterBullet
        }
        xaopexWall = object : ShatterWall("xaopex-wall"){}.apply {
            requirements(
                Category.defense,
                with(TvaItems.xaopnenBar, 12, TvaItems.xeopnax, 12)
            )
            shatterChance = 0.7
            maxShatter = 1
            health = 3950
            size = 2
            shatterInaccuracy = 360f
            shatterBullet = TvaBullets.pulseWallBullet
        }
        //endregion
        //region crafting
        boiler = object : HeatCrafter("boiler"){}.apply {
            requirements(
                Category.crafting,
                with(Items.copper, 45, Items.lead, 35, Items.graphite, 25)
            )
            size = 2
            heatLoss = 0.005
            health = 240
            customConsume.heat = 2f
            craftTime = 6f
            liquidCapacity = 180f
            drawerCustom = DrawBoiler().apply{
                spread = 5.6f
            }
            consumes.liquid(Liquids.water, 0.2f)
            outputLiquid = LiquidStack(TvaLiquids.steam, 1.4f)
            heatCapacity = 350f
            craftEffect = Fx.none
        }
        methanolCompressor = object : HeatCrafter("methanol-compressor"){}.apply {
            requirements(
                Category.crafting,
                with(Items.copper, 45, Items.lead, 35, Items.graphite, 25)
            )
            size = 2
            heatLoss = 0.005
            health = 350
            craftTime = 180f
            liquidCapacity = 180f
            drawerCustom = DrawBoiler().apply{
                spread = 4.25f
                mistColor = TvaLiquids.methanol.color
            }
            consumes.liquid(TvaLiquids.steam, 0.1f)
            consumes.item(Items.sporePod, 2)
            customConsume.heat = 3f
            outputLiquid = LiquidStack(TvaLiquids.methanol, 33f)
            heatCapacity = 350f
            craftEffect = Fx.none
        }
        heater = object : BurnerHeatGenerator("heater"){}.apply {
            requirements(
                Category.crafting,
                with(Items.copper, 18, Items.lead, 22)
            )
            health = 90
            heatLoss = 0.005
            generateTime = 100f
            heatGenerate = 4f
            heatCapacity = 350f
            squareSprite = false
        }
        heatVent = object : HeatRegulator("heat-vent"){}.apply {
            requirements(
                Category.crafting,
                with(Items.copper, 42, Items.lead, 32, Items.graphite, 25)
            )
            health = 190
            size = 2
            heatLoss = 0.05
            heatCapacity = 300f
        }
        xaopenForge = object : HeatCrafter("xaopen-forge"){}.apply {
            requirements(
                Category.crafting,
                with(Items.lead, 45,Items.graphite, 35, Items.titanium, 25)
            )
            size = 2
            health = 350
            heatLoss = 0.005
            customConsume.heat = 4f
            drawerCustom = DrawHeatSmelter()
            minHeatRequire = 150f
            heatCapacity = 350f
            updateEffectRange = 1.5f
            updateEffect = TvaFx.xaoForgeSmoke
            updateEffectChance = 1.0
            craftTime = 30f
            consumes.items(*with(TvaItems.xaopnen, 1, Items.sand, 1))
            craftEffect = Fx.smeltsmoke
            outputItems = arrayOf(*with(TvaItems.xaopnenBar, 1))
        }
        smelter = object : HeatCrafter("smelter"){}.apply {
            requirements(
                Category.crafting,
                with(Items.lead, 45,Items.graphite, 35, Items.titanium, 25)
            )
            size = 3
            health = 980
            heatLoss = 0.005
            customConsume.heat = 7f
            drawerCustom = DrawHeatSmelter()
            minHeatRequire = 200f
            heatCapacity = 400f
            updateEffectRange = 1.5f
            updateEffect = TvaFx.smelterSmoke
            updateEffectChance = 1.0
            craftTime = 40f
            craftEffect = Fx.smeltsmoke
            consumes.items(*with(Items.lead, 2, Items.copper, 2))
            outputItems = arrayOf(*with(TvaItems.denseIngot, 1))
        }
        //endregion
        //region Payload
        draugConstructor = object : UnitSpawner("draug-miner-port"){}.apply {
            requirements(
                Category.units,
                with(Items.copper, 75, Items.lead, 22, Items.graphite, 45)
            )
            consumes.power(1.2f)
            size = 2
            health = 570
            unitAmount = 1
            timeConstruct = 1332f
            constructUnit = TvaUnitTypes.draugMiner
            droneAI = MinerAI()
            range = 0f
        }
        healerConstructor = object : UnitSpawner("healing-station"){}.apply {
            requirements(
                Category.units,
                with(Items.titanium, 45, Items.silicon, 65, TvaItems.denseIngot, 45)
            )
            consumes.power(2.9f)
            consumes.items(*with(Items.silicon, 30, Items.lead, 15))
            size = 2
            itemCapacity = 30
            range = 0f
            timeConstruct = 2100f
            health = 570
            constructUnit = TvaUnitTypes.healDrone
            unitAmount = 2
            droneAI = RepairAI()
        }
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
        pulseTower = object : LaserTower("pulse-tower"){}.apply {
            requirements(
                Category.effect,
                with(Items.titanium, 45, Items.silicon, 65, TvaItems.xaopnenBar, 45, Items.thorium, 25)
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
        //endregion
    }
    companion object {
        lateinit var rtgCell: Block
        lateinit var pulseTower: Block
        lateinit var pulseTowerSmall: Block
        lateinit var metaglassWall: Block
        lateinit var xaopexWall: Block
        lateinit var laxo: Block
        lateinit var novem: Block
        lateinit var buildingDisassembler: Block
        lateinit var heater: Block
        lateinit var boiler: Block
        lateinit var methanolCompressor: Block
        lateinit var turbine: Block
        lateinit var geothermalCollector: Block
        lateinit var thermalTurbine: Block
        lateinit var heatNode: Block
        lateinit var heatPipe: Block
        lateinit var xaopenForge: Block
        lateinit var smelter: Block
        lateinit var heatVent: Block
        lateinit var healerConstructor: Block
        lateinit var draugConstructor: Block
    }
}