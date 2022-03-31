package tvakot.content

import arc.struct.Seq
import mindustry.content.Blocks
import mindustry.content.TechTree
import mindustry.ctype.ContentList
import mindustry.ctype.UnlockableContent
import mindustry.game.Objectives
import mindustry.type.ItemStack

class TvaTechTree : ContentList {
    private fun node(parent: UnlockableContent, child: UnlockableContent, requirements: ItemStack?, objectives: Seq<Objectives.Objective>?) {
        val requirementsIn = if(requirements != null) arrayOf(requirements) else child.researchRequirements()
        val newNode = TechTree.TechNode(TechTree.get(parent) , child, requirementsIn)

        requirementsIn.forEach{i ->
            newNode.objectives.add(Objectives.Research(i.item))
        }

        if (objectives != null) newNode.objectives.addAll(objectives)
    }
    private fun node(parent: UnlockableContent, child: UnlockableContent, requirements: ItemStack?) {
        node(parent, child, requirements, null)
    }

    private fun node(parent: UnlockableContent, child: UnlockableContent) {
        node(parent, child, null)
    }

    override fun load() {
        //sector
        node(Blocks.coreShard, TvaSectorPresets.surface)
        node(TvaSectorPresets.surface, TvaSectorPresets.xaopnenOutpost)
        //turret
        node(Blocks.duo, TvaBlocks.laxo, null, Seq.with(Objectives.SectorComplete(TvaSectorPresets.surface)))
        node(TvaBlocks.laxo, TvaBlocks.penetrate, null, Seq.with(Objectives.SectorComplete(TvaSectorPresets.xaopnenOutpost), Objectives.Research(Blocks.salvo)))
        node(TvaBlocks.laxo, TvaBlocks.ignis, null, Seq.with(Objectives.SectorComplete(TvaSectorPresets.xaopnenOutpost), Objectives.Research(Blocks.lancer)))
        node(TvaBlocks.laxo, TvaBlocks.novem, null, Seq.with(Objectives.SectorComplete(TvaSectorPresets.xaopnenOutpost), Objectives.Research(Blocks.swarmer)))
        //distribute drug to kids
        node(TvaBlocks.boiler, TvaBlocks.heatNode)
        node(TvaBlocks.heatNode, TvaBlocks.heatVent)
        //crafter
        node(Blocks.graphitePress, TvaBlocks.boiler)
        node(TvaBlocks.boiler, TvaBlocks.heater)
        node(TvaBlocks.boiler, TvaBlocks.turbine)
        node(Blocks.graphitePress, TvaBlocks.xaopenForge, null, Seq.with(Objectives.SectorComplete(TvaSectorPresets.xaopnenOutpost)))
        node(TvaBlocks.xaopenForge, TvaBlocks.xaopenInfuser)
        //t h e   w a l l
        node(Blocks.copperWallLarge, TvaBlocks.metaglassWall)
        node(TvaBlocks.metaglassWall, TvaBlocks.xaopexWall)
        //le unit
        node(Blocks.combustionGenerator, TvaBlocks.draugConstructor)
        node(TvaBlocks.draugConstructor, TvaBlocks.healerConstructor)
        node(TvaBlocks.draugConstructor, TvaBlocks.defenderPort)
    }
}