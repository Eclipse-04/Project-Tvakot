package tvakot

import mindustry.content.Blocks
import mindustry.content.Items
import mindustry.mod.Mod
import mindustry.type.ItemStack
import mindustry.world.blocks.units.Reconstructor
import mindustry.world.blocks.units.UnitFactory
import tvakot.content.*

class Tvakot : Mod(){

    init{

    }

    override fun loadContent(){
        TvaFx().load()
        TvaStatusEffects().load()
        TvaBullets().load()
        TvaItems().load()
        TvaLiquids().load()
        TvaUnitTypes().load()
        TvaBlocks().load()
        TvaTechTree().load()

        (Blocks.groundFactory as UnitFactory).plans.add(
            UnitFactory.UnitPlan(TvaUnitTypes.castle, 2100f, ItemStack.with(Items.silicon, 20, Items.graphite, 20)),
        )
        (Blocks.additiveReconstructor as Reconstructor).upgrades.addAll(
            arrayOf(TvaUnitTypes.castle, TvaUnitTypes.bastille)
        )
    }
}
