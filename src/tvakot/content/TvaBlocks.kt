package tvakot.content

import arc.graphics.Color
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
import mindustry.world.blocks.defense.turrets.ItemTurret
import mindustry.world.blocks.defense.turrets.PowerTurret
import mindustry.world.blocks.environment.OreBlock
import mindustry.world.blocks.payloads.PayloadDeconstructor
import mindustry.world.blocks.power.DecayGenerator
import mindustry.world.blocks.production.GenericCrafter
import mindustry.world.blocks.storage.CoreBlock
import mindustry.world.draw.DrawGlow
import mindustry.world.meta.BuildVisibility
import tvakot.world.blocks.crafting.HeatCrafter
import tvakot.world.blocks.defensive.ShatterWall
import tvakot.world.blocks.defensive.turret.OverdriveTurret
import tvakot.world.blocks.distribution.HeatNode
import tvakot.world.blocks.distribution.HeatRegulator
import tvakot.world.blocks.power.BurnerHeatGenerator
import tvakot.world.blocks.power.ThermalHeatGenerator
import tvakot.world.blocks.power.TurbineGenerator
import tvakot.world.blocks.units.UnitSpawner
import tvakot.world.draw.DrawBoiler
import tvakot.world.draw.DrawHeatSmelter

class TvaBlocks : ContentList {

    override fun load() {
        //region environment
        oreXaopnen = object : OreBlock(TvaItems.xaopnen) {
            init {
                oreDefault = true
                oreThreshold = 0.828f
                oreScale = 20.952381f
            }
        }
        //endregion
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
        railGun = object : ItemTurret("rail-gun"){
            init {
                requirements(
                    Category.turret,
                    with(Items.copper, 55, Items.lead, 20)
                )
                health = 350
                range = 210f
                ammoPerShot = 2
                reloadTime = 40f
                shootLength = 3.75f
                ammo(
                    Items.lead, TvaBullets.standardrailBullet,
                    Items.graphite, TvaBullets.denserailBullet,
                    TvaItems.xaopnenBar, TvaBullets.flakrailBullet
                )
                limitRange()
            }
        }
        novem = object : OverdriveTurret("novem"){
            init {
                requirements(
                    Category.turret,
                    with(TvaItems.denseIngot, 34, Items.silicon, 35, Items.graphite, 25, TvaItems.xaopnen, 25, Items.plastanium, 32)
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
        penetrate = object : ItemTurret("penetrate"){
            init {
                ammo(
                    Items.titanium, TvaBullets.lightningRicochet,
                    Items.surgeAlloy, TvaBullets.surgeLightningRicochet
                )
            }
        }.apply {
            requirements(
                Category.turret,
                with(Items.titanium, 35, Items.silicon, 25, Items.graphite, 30)
            )
            health = 760
            size = 2
            reloadTime = 60f
            range = 232f
            shots = 2
            shootLength = 2.5f
            shootSound = Sounds.shootBig
            burstSpacing = 5f
            inaccuracy = 5f
        }
        ignis = object : PowerTurret("ignis"){}.apply {
            requirements(
                Category.turret,
                with(Items.lead, 25, Items.silicon, 20, Items.titanium, 32)
            )
            range = 180f
            chargeTime = 40f
            chargeMaxDelay = 30f
            chargeEffects = 7
            recoilAmount = 2f
            reloadTime = 90f
            cooldown = 0.03f
            shootLength = 1f
            powerUse = 8f
            shootShake = 2f
            shootEffect = Fx.lancerLaserShoot
            smokeEffect = Fx.none
            chargeEffect = Fx.lancerLaserCharge
            chargeBeginEffect = Fx.lancerLaserChargeBegin
            heatColor = Color.red
            size = 2
            health = 280 * size * size
            shootSound = Sounds.laser
            shootType = TvaBullets.standardOrbLightning
        }
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
        //endregion
        //region Power
        rtgCell = object : DecayGenerator("rtg-cell"){}.apply {
            requirements(
                Category.power,
                with(Items.lead, 56, Items.silicon, 23, Items.scrap, 50)
            )
            buildVisibility = BuildVisibility.sandboxOnly
            powerProduction = 1.75f
            health = 175
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
            radius = 2.3f
        }
        thermalTurbine = object : TurbineGenerator("thermal-turbine"){}.apply {
            requirements(
                Category.power,
                with(TvaItems.xaopnenBar, 95, Items.silicon, 65, Items.metaglass, 55, Items.thorium, 75)
            )
            rotorAmount = 2
            size = 3
            health = 2750
            spread = 4f
            minLiquidRequired = 75f
            liquidCapacity = 100f
            rotorSpeed[1] = 8f
            consumes.liquid(TvaLiquids.steam, 0.37f)
            powerProduction = 24f
            bubbles = 35
            spinAmount = 180f
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
            health = 3250
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
            hasItems = true
            health = 90
            heatLoss = 0.005
            generateTime = 100f
            heatGenerate = 4f
            heatCapacity = 350f
            squareSprite = false
        }
        combustionHeater = object : BurnerHeatGenerator("comburstion-burner"){}.apply {
            requirements(
                Category.crafting,
                with(Items.lead, 75, Items.graphite, 25, Items.metaglass, 25, Items.silicon, 25)
            )
            hasItems = true
            hasLiquids = true
            size = 2
            health = 350
            liquidCapacity = 90f
            heatLoss = 0.005
            generateTime = 75f
            heatGenerate = 10f
            heatGenerateLiquid = 12f
            liquidUsage = 0.15f
            heatCapacity = 600f
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
                with(Items.lead, 45,Items.graphite, 35)
            )
            size = 2
            health = 350
            heatLoss = 0.005
            customConsume.heat = 4f
            drawerCustom = DrawHeatSmelter()
            minHeatRequire = 150f
            heatCapacity = 350f
            updateEffectRange = 1.5f
            updateEffect = TvaFx.forgeSmoke
            updateEffectChance = 1.0
            craftTime = 30f
            consumes.items(*with(TvaItems.xaopnen, 1, Items.sand, 1))
            craftEffect = Fx.smeltsmoke
            outputItems = arrayOf(*with(TvaItems.xaopnenBar, 1))
        }
        xaopenInfuser = object : HeatCrafter("xaopnen-infuser"){}.apply {
            requirements(
                Category.crafting,
                with(Items.titanium, 25,Items.graphite, 35, TvaItems.denseIngot, 25)
            )
            size = 2
            health = 350
            heatLoss = 0.005
            customConsume.heat = 1.5f
            drawerCustom = DrawHeatSmelter().apply {
                flameRadius = 4f
            }
            heatCapacity = 350f
            minHeatRequire = 150f
            craftTime = 60f
            consumes.items(*with(Items.titanium, 1, Items.graphite, 1))
            craftEffect = Fx.smeltsmoke
            outputItems = arrayOf(*with(TvaItems.xaopnen, 1))
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
        electrolizer = object : GenericCrafter("electrolizer"){}.apply {
            requirements(
                Category.crafting,
                with(TvaItems.xaopnenBar, 45,Items.silicon, 25, Items.titanium, 25)
            )
            size = 2
            health = 980
            craftTime = 40f
            ambientSound = Sounds.spark
            drawer = DrawGlow()
            craftEffect = Fx.smeltsmoke
            consumes.power(2.4f)
            consumes.items(*with(TvaItems.xaopnenBar, 2, Items.thorium, 1))
            outputItems = arrayOf(*with(TvaItems.xeopnax, 1))
        }
        //endregion
        //region Payload
        draugConstructor = object : UnitSpawner("draug-miner-port"){}.apply {
            requirements(
                Category.units,
                with(Items.copper, 70, Items.lead, 30)
            )
            consumes.power(1.2f)
            size = 2
            health = 570
            unitAmount = 1
            timeConstruct = 1332f
            constructUnit = TvaUnitTypes.draugMiner
            range = 0f
        }
        healerConstructor = object : UnitSpawner("healing-station"){}.apply {
            requirements(
                Category.units,
                with(Items.lead, 45, Items.silicon, 25, Items.metaglass, 25)
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
        }
        defenderPort = object : UnitSpawner("defender-port"){}.apply {
            requirements(
                Category.units,
                with(TvaItems.denseIngot, 30, Items.titanium, 35, Items.graphite, 20, Items.silicon, 20)
            )
            consumes.power(1.2f)
            consumes.items(*with(Items.silicon, 20, Items.titanium, 25))
            itemCapacity = 50
            size = 2
            health = 570
            unitAmount = 2
            timeConstruct = 900f
            constructUnit = TvaUnitTypes.defender
            range = 0f
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
        coreFragment = object : CoreBlock("core-fragment"){
            init {
                requirements(
                    Category.effect,
                    BuildVisibility.editorOnly,
                    with(Items.copper, 800, Items.lead, 1200)
                )
                alwaysUnlocked = true
                health = 2500
                itemCapacity = 7000
                size = 3
                unitCapModifier = 12
                unitType = TvaUnitTypes.epsilon
            }
        }
        //endregion
    }
    companion object {
        //env
        lateinit var oreXaopnen: Block
        //building
        lateinit var rtgCell: Block
        lateinit var metaglassWall: Block
        lateinit var xaopexWall: Block
        lateinit var laxo: Block
        lateinit var railGun: Block
        lateinit var novem: Block
        lateinit var penetrate: Block
        lateinit var ignis: Block
        lateinit var buildingDisassembler: Block
        lateinit var heater: Block
        lateinit var combustionHeater: Block
        lateinit var boiler: Block
        lateinit var methanolCompressor: Block
        lateinit var turbine: Block
        lateinit var geothermalCollector: Block
        lateinit var thermalTurbine: Block
        lateinit var heatNode: Block
        lateinit var xaopenInfuser: Block
        lateinit var xaopenForge: Block
        lateinit var smelter: Block
        lateinit var electrolizer: Block
        lateinit var heatVent: Block
        lateinit var healerConstructor: Block
        lateinit var draugConstructor: Block
        lateinit var defenderPort: Block
        lateinit var coreFragment: Block
    }
}