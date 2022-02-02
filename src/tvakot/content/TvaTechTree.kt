package tvakot.content

import arc.struct.Seq
import mindustry.content.Blocks
import mindustry.content.SectorPresets.*
import mindustry.content.TechTree
import mindustry.ctype.ContentList
import mindustry.ctype.UnlockableContent
import mindustry.game.Objectives
import mindustry.type.ItemStack

class TvaTechTree : ContentList {
    //totally not from acceleration mod what are you talking about
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

    override fun load() {
        node(Blocks.duo, TvaBlocks.laxo, null, Seq.with(Objectives.SectorComplete(stainedMountains)))
        node(Blocks.combustionGenerator, TvaBlocks.pulseTowerSmall, null, Seq.with(Objectives.SectorComplete(stainedMountains)))
        node(TvaBlocks.pulseTowerSmall, TvaBlocks.pulseTower, null, Seq.with(Objectives.SectorComplete(stainedMountains)))
        node(Blocks.constructor, TvaBlocks.buildingDisassembler, null, Seq.with(Objectives.SectorComplete(ruinousShores)))
    }
}