package tvakot.content

import mindustry.world.blocks.power.DecayGenerator
import mindustry.world.Block
import mindustry.content.Items
import mindustry.ctype.ContentList
import mindustry.type.Category
import mindustry.type.ItemStack.with
import mindustry.world.meta.BuildVisibility
import tvakot.world.blocks.effect.LaserTower

class TvaBlocks : ContentList {

    private lateinit var rtgCell: Block
    private lateinit var pulseTower: Block

    override fun load() {
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
        //region Effect
        pulseTower = object : LaserTower("electrolizer"){}.apply {
            requirements(
                Category.effect,
                with(Items.titanium, 85, Items.silicon, 55, Items.graphite, 95)
            )
            consumes.power(7f)
            size = 2
            health = 850
            reloadTime = 10f
            damageHit = 15f
            range = 180f
        }
    }
}