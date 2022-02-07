package tvakot

import tvakot.content.*
import mindustry.mod.*

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
    }
}
