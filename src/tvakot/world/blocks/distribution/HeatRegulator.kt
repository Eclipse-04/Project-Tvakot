package tvakot.world.blocks.distribution

import arc.scene.ui.Label
import arc.scene.ui.layout.Table
import arc.util.io.Reads
import arc.util.io.Writes
import mindustry.gen.Building
import tvakot.world.blocks.TvaHeatBlock


open class HeatRegulator(name: String) : TvaHeatBlock(name) {
    var step = 10f
    init {
        configurable = true
        saveConfig = true
        outputHeat = false
        config(Float::class.java) { tile: HeatRegulatorBuild?, f: Float? -> tile!!.minVent = f!! }
    }
    inner class HeatRegulatorBuild : TvaHeatBlockBuild(){
        var minVent = 0f
        override fun config(): Float {
            return minVent
        }
        override fun buildConfiguration(table: Table) {
            val label = Label(minVent.toString())
            table.table {
                table.slider(0f, heatCapacity, step, minVent, false) { f ->
                    configure(f)
                    minVent = f
                    label.setText(minVent.toString())
                }
                table.row()
                table.add(label)
            }
        }
        override fun appectHeat(source: TvaHeatBlockBuild?): Boolean {
            return if(source != null){
                source.heatModule.heat > minVent
            } else false
        }
        override fun onConfigureTileTapped(other: Building): Boolean {
            if (this === other) {
                deselect()
                return false
            }
            return true
        }
        override fun write(write: Writes) {
            super.write(write)
            write.f(minVent)
        }
        override fun read(read: Reads) {
            super.read(read)
        }
        override fun read(read: Reads, revision: Byte) {
            super.read(read, revision)
            minVent = read.f()
        }
    }
}