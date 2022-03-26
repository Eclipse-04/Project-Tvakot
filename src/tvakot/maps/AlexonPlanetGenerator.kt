package tvakot.maps

import arc.graphics.Color
import arc.math.Angles
import arc.math.Mathf
import arc.math.Rand
import arc.math.geom.Geometry
import arc.math.geom.Point2
import arc.math.geom.Vec3
import arc.struct.FloatSeq
import arc.struct.ObjectMap
import arc.struct.ObjectSet
import arc.struct.Seq
import arc.util.Tmp
import arc.util.noise.Ridged
import arc.util.noise.Simplex
import mindustry.Vars.*
import mindustry.ai.Astar
import mindustry.ai.BaseRegistry.BasePart
import mindustry.content.Blocks
import mindustry.game.Schematics
import mindustry.game.Team
import mindustry.game.Waves
import mindustry.maps.generators.BaseGenerator
import mindustry.maps.planet.SerpuloPlanetGenerator
import mindustry.type.Sector
import mindustry.world.*
import tvakot.content.TvaBlocks


class AlexonPlanetGenerator : SerpuloPlanetGenerator() {

    var seed = 420
    var basegen = BaseGenerator()
    var scl = 1f
    var waterOffset = 0.08f
    var arr: Array<Array<Block>> =
    arrayOf(
        arrayOf(Blocks.slag, Blocks.magmarock, Blocks.hotrock, Blocks.stone, Blocks.grass, Blocks.darksand, Blocks.tar, Blocks.basalt, Blocks.stone, Blocks.grass, Blocks.snow, Blocks.ice, Blocks.ice),
        arrayOf(Blocks.slag, Blocks.hotrock, Blocks.basalt, Blocks.stone, Blocks.stone, Blocks.darksand, Blocks.tar, Blocks.dacite, Blocks.stone, Blocks.stone, Blocks.grass, Blocks.ice, Blocks.ice),
        arrayOf(Blocks.deepwater, Blocks.water, Blocks.sandWater, Blocks.sand, Blocks.stone, Blocks.sand, Blocks.sand, Blocks.basalt, Blocks.snow, Blocks.snow, Blocks.snow, Blocks.snow, Blocks.ice),
        arrayOf(Blocks.deepwater, Blocks.water, Blocks.sandWater, Blocks.sand, Blocks.stone, Blocks.dacite, Blocks.stone, Blocks.stone, Blocks.snow, Blocks.snow, Blocks.snow, Blocks.ice, Blocks.ice),
        arrayOf(Blocks.deepwater, Blocks.water, Blocks.sandWater, Blocks.sand, Blocks.stone, Blocks.grass, Blocks.grass, Blocks.dacite, Blocks.stone, Blocks.snow, Blocks.snow, Blocks.iceSnow, Blocks.ice),
        arrayOf(Blocks.ice, Blocks.snow, Blocks.dacite, Blocks.stone, Blocks.grass, Blocks.dirt, Blocks.grass, Blocks.water, Blocks.grass, Blocks.stone, Blocks.stone, Blocks.dacite, Blocks.ice, Blocks.ice),
        arrayOf(Blocks.deepwater, Blocks.darksandWater, Blocks.basalt, Blocks.basalt, Blocks.stone, Blocks.stone, Blocks.stone, Blocks.water, Blocks.dacite, Blocks.snow, Blocks.dacite, Blocks.ice, Blocks.ice),
    )

    var dec = ObjectMap.of<Block, Block>(
        Blocks.salt, Blocks.sand,
        Blocks.salt, Blocks.sand,
        Blocks.salt, Blocks.sand,
        Blocks.sandWater, Blocks.sandWater
    )

    var tars = ObjectMap.of<Block, Block>(
        Blocks.dirt, Blocks.dirt,
        Blocks.dirt, Blocks.dirt
    )

    var water: Float = 2f / arr[0].size

    fun rawHeight(position: Vec3): Float {
        var position = position
        position = Tmp.v33.set(position).scl(scl)
        return (Mathf.pow(
            Simplex.noise3d(seed, 10.0, 0.5, 0.5, position.x.toDouble(), position.y.toDouble(), position.z.toDouble()), 2.3f
        ) + waterOffset) / (1f + waterOffset)
    }

    override fun generateSector(sector: Sector) {
        super.generateSector(sector)
        sector.generateEnemyBase = false
    }

    override fun postGenerate(tiles: Tiles?) {
        if (sector.hasEnemyBase()) {
            basegen.postGenerate()
        }
    }

    override fun generate() {
        class Room(var x: Int, var y: Int, var radius: Int) {
            var connected = ObjectSet<Room>()

            init {
                connected.add(this)
            }

            fun connect(to: Room) {
                if (connected.contains(to)) return
                connected.add(to)
                val nscl = rand.random(20f, 60f)
                val stroke = rand.random(4, 12)
                brush(pathfind(
                    x,
                    y, to.x, to.y,
                    { tile: Tile ->
                        (if (tile.solid()) 5f else 0f) + noise(
                            tile.x.toFloat(),
                            tile.y.toFloat(),
                            1.0,
                            1.0,
                            (1f / nscl).toDouble()
                        ) * 60
                    }, Astar.manhattan
                ), stroke
                )
            }
        }
        cells(4)
        distort(10f, 12f)
        val constraint = 1.3f
        val radius = width / 2f / Mathf.sqrt3
        val rooms = rand.random(2, 5)
        val roomseq = Seq<Room>()
        for (i in 0 until rooms) {
            Tmp.v1.trns(rand.random(360f), rand.random(radius / constraint))
            val rx = width / 2f + Tmp.v1.x
            val ry = height / 2f + Tmp.v1.y
            val maxrad = radius - Tmp.v1.len()
            val rrad = Math.min(rand.random(9f, maxrad / 2f), 30f)
            roomseq.add(Room(rx.toInt(), ry.toInt(), rrad.toInt()))
        }

        //check positions on the map to place the player spawn. this needs to be in the corner of the map
        var spawn: Room? = null
        val enemies = Seq<Room>()
        val enemySpawns = rand.random(1, Math.max((sector.threat * 4).toInt(), 1))
        val offset = rand.nextInt(360)
        val length = width / 2.55f - rand.random(13, 23)
        val angleStep = 5
        val waterCheckRad = 5
        run {
            var i = 0
            while (i < 360) {
                val angle = offset + i
                val cx = (width / 2 + Angles.trnsx(angle.toFloat(), length)).toInt()
                val cy = (height / 2 + Angles.trnsy(angle.toFloat(), length)).toInt()
                var waterTiles = 0

                //check for water presence
                for (rx in -waterCheckRad..waterCheckRad) {
                    for (ry in -waterCheckRad..waterCheckRad) {
                        val tile = tiles[cx + rx, cy + ry]
                        if (tile == null || tile.floor().liquidDrop != null) {
                            waterTiles++
                        }
                    }
                }
                if (waterTiles <= 4 || i + angleStep >= 360) {
                    roomseq.add(Room(cx, cy, rand.random(8, 15)).also { spawn = it })
                    for (j in 0 until enemySpawns) {
                        val enemyOffset = rand.range(60f)
                        Tmp.v1.set((cx - width / 2).toFloat(), (cy - height / 2).toFloat()).rotate(180f + enemyOffset)
                            .add((width / 2).toFloat(), (height / 2).toFloat())
                        val espawn = Room(Tmp.v1.x.toInt(), Tmp.v1.y.toInt(), rand.random(8, 15))
                        roomseq.add(espawn)
                        enemies.add(espawn)
                    }
                    break
                }
                i += angleStep
            }
        }
        for (room in roomseq) {
            erase(room.x, room.y, room.radius)
        }
        val connections = rand.random(Math.max(rooms - 1, 1), rooms + 3)
        for (i in 0 until connections) {
            roomseq.random(rand).connect(roomseq.random(rand))
        }
        for (room in roomseq) {
            spawn!!.connect(room)
        }
        cells(1)
        distort(10f, 6f)
        inverseFloodFill(tiles.getn(spawn!!.x, spawn!!.y))
        val ores = Seq.with(Blocks.oreCopper, Blocks.oreLead, TvaBlocks.oreXaopnen)
        val poles = Math.abs(sector.tile.v.y)
        val nmag = 0.5f
        val scl = 1f
        val addscl = 1.3f
        if (Simplex.noise3d(
                seed,
                2.0,
                0.5,
                scl.toDouble(),
                sector.tile.v.x.toDouble(),
                sector.tile.v.y.toDouble(),
                sector.tile.v.z.toDouble()
            ) * nmag + poles > 0.25f * addscl
        ) {
            ores.add(Blocks.oreCoal)
        }
        if (Simplex.noise3d(
                seed,
                2.0,
                0.5,
                scl.toDouble(),
                (sector.tile.v.x + 1).toDouble(),
                sector.tile.v.y.toDouble(),
                sector.tile.v.z.toDouble()
            ) * nmag + poles > 0.5f * addscl
        ) {
            ores.add(Blocks.oreTitanium)
        }
        if (Simplex.noise3d(
                seed,
                2.0,
                0.5,
                scl.toDouble(),
                (sector.tile.v.x + 2).toDouble(),
                sector.tile.v.y.toDouble(),
                sector.tile.v.z.toDouble()
            ) * nmag + poles > 0.7f * addscl
        ) {
            ores.add(Blocks.oreThorium)
        }
        val frequencies = FloatSeq()
        for (i in 0 until ores.size) {
            frequencies.add(rand.random(-0.1f, 0.01f) - i * 0.01f + poles * 0.04f)
        }
        pass { x: Int, y: Int ->
            if (!floor.asFloor().hasSurface()) return@pass
            val offsetX = x - 4
            val offsetY = y + 23
            for (i in ores.size - 1 downTo 0) {
                val entry = ores[i]
                val freq = frequencies[i]
                if (Math.abs(
                        0.5f - noise(
                            offsetX.toFloat(),
                            (offsetY + i * 999).toFloat(),
                            2.0,
                            0.7,
                            (40 + i * 2).toDouble()
                        )
                    ) > 0.22f + i * 0.01 &&
                    Math.abs(
                        0.5f - noise(
                            offsetX.toFloat(),
                            (offsetY - i * 999).toFloat(),
                            1.0,
                            1.0,
                            (30 + i * 4).toDouble()
                        )
                    ) > 0.37f + freq
                ) {
                    ore = entry
                    break
                }
            }
        }
        trimDark()
        median(2)
        tech()
        pass { x: Int, y: Int ->
            //random mud
            if (floor === Blocks.dirt) {
                if (Math.abs(0.5f - noise((x - 90).toFloat(), y.toFloat(), 4.0, 0.8, 65.0)) > 0.02) {
                    floor = Blocks.mud
                }
            }

            //salt
            if (floor === Blocks.sand) {
                if (Math.abs(0.5f - noise((x - 40).toFloat(), y.toFloat(), 2.0, 0.7, 80.0)) > 0.25f && Math.abs(
                        0.5f - noise(
                            x.toFloat(),
                            (y + sector.id * 10).toFloat(),
                            1.0,
                            1.0,
                            60.0
                        )
                    ) > 0.41f && !(roomseq.contains { r: Room ->
                        Mathf.within(x.toFloat(), y.toFloat(), r.x.toFloat(), r.y.toFloat(), 15f)
                    })
                ) {
                    floor = Blocks.salt
                    ore = Blocks.air
                }
            }

            //random stuff
            run {
                for (i in 0..3) {
                    val near = world.tile(x + Geometry.d4[i].x, y + Geometry.d4[i].y)
                    if (near != null && near.block() !== Blocks.air) {
                        break
                    }
                }
                if (rand.chance(0.01) && floor.asFloor().hasSurface() && block === Blocks.air) {
                    block = dec[floor, floor.asFloor().decoration]
                }
            }
        }
        val difficulty = sector.threat
        ints.clear()
        ints.ensureCapacity(width * height / 4)
        val ruinCount = rand.random(-2, 10)
        if (ruinCount > 0) {
            val padding = 25

            //create list of potential positions
            for (x in padding until width - padding) {
                for (y in padding until height - padding) {
                    val tile = tiles.getn(x, y)
                    if (!tile.solid() && (tile.drop() != null || tile.floor().liquidDrop != null)) {
                        ints.add(tile.pos())
                    }
                }
            }
            ints.shuffle(rand)
            var placed = 0
            val diffRange = 0.4f
            //try each position
            var i = 0
            while (i < ints.size && placed < ruinCount) {
                val `val` = ints.items[i]
                val x = Point2.x(`val`).toInt()
                val y = Point2.y(`val`).toInt()

                //do not overwrite player spawn
                if (Mathf.within(x.toFloat(), y.toFloat(), spawn!!.x.toFloat(), spawn!!.y.toFloat(), 18f)) {
                    i++
                    continue
                }
                val range = difficulty + rand.random(diffRange)
                val tile = tiles.getn(x, y)
                var part: BasePart? = null
                if (tile.overlay().itemDrop != null) {
                    part = bases.forResource(tile.drop()).getFrac(range)
                } else if (tile.floor().liquidDrop != null && rand.chance(0.05)) {
                    part = bases.forResource(tile.floor().liquidDrop).getFrac(range)
                } else if (rand.chance(0.05)) { //ore-less parts are less likely to occur.
                    part = bases.parts.getFrac(range)
                }

                //actually place the part
                if (part != null && BaseGenerator.tryPlace(part, x, y, Team.derelict) { cx: Int, cy: Int ->
                        val other = tiles.getn(cx, cy)
                        if (other.floor().hasSurface()) {
                            other.setOverlay(Blocks.oreScrap)
                            for (j in 1..2) {
                                for (p: Point2 in Geometry.d8) {
                                    val t = tiles[cx + p.x * j, cy + p.y * j]
                                    if ((t != null) && t.floor()
                                            .hasSurface() && rand.chance(if (j == 1) 0.4 else 0.2)
                                    ) {
                                        t.setOverlay(Blocks.oreScrap)
                                    }
                                }
                            }
                        }
                    }) {
                    placed++
                    val debrisRadius = Math.max(part.schematic.width, part.schematic.height) / 2 + 3
                    Geometry.circle(x, y, tiles.width, tiles.height, debrisRadius) { cx: Int, cy: Int ->
                        val dst = Mathf.dst(cx.toFloat(), cy.toFloat(), x.toFloat(), y.toFloat())
                        val removeChance = Mathf.lerp(0.05f, 0.5f, dst / debrisRadius)
                        val other = tiles.getn(cx, cy)
                        if (other.build != null && other.isCenter) {
                            if (other.team() === Team.derelict && rand.chance(removeChance.toDouble())) {
                                other.remove()
                            } else if (rand.chance(0.5)) {
                                other.build.health = other.build.health - rand.random(other.build.health * 0.9f)
                            }
                        }
                    }
                }
                i++
            }
        }
        Schematics.placeLaunchLoadout(spawn!!.x, spawn!!.y)
        for (espawn in enemies) {
            tiles.getn(espawn.x, espawn.y).setOverlay(Blocks.spawn)
        }
        if (sector.hasEnemyBase()) {
            basegen.generate(
                tiles, enemies.map { r: Room -> tiles.getn(r.x, r.y) },
                tiles[spawn!!.x, spawn!!.y], state.rules.waveTeam, sector, difficulty
            )
            sector.info.attack = true
            state.rules.attackMode = sector.info.attack
        } else {
            sector.info.winWave = 10 + 5 * Math.max(difficulty * 10, 1f).toInt()
            state.rules.winWave = sector.info.winWave
        }
        val waveTimeDec = 0.4f
        state.rules.waveSpacing =
            Mathf.lerp((60 * 65 * 2).toFloat(), 60f * 60f * 1f, Math.max(difficulty - waveTimeDec, 0f) / 0.8f)
        sector.info.waves = true
        state.rules.waves = sector.info.waves
        state.rules.enemyCoreBuildRadius = 600f
        state.rules.spawns = Waves.generate(difficulty, Rand(), state.rules.attackMode)
    }

    override fun getHeight(position: Vec3?): Float {
        val height = rawHeight(position!!)
        return Math.max(height, water)
    }

    override fun getColor(position: Vec3): Color? {
        val block = getBlock(position)
        return Tmp.c1.set(block.mapColor).a(1f - block.albedo)
    }

    fun getBlock(position: Vec3): Block {
        var position = position
        var height = rawHeight(position)
        Tmp.v31.set(position)
        position = Tmp.v33.set(position).scl(scl)
        val rad = scl
        var temp = Mathf.clamp(Math.abs(position.y * 2f) / rad)
        temp = Mathf.lerp(
            temp,
            Simplex.noise3d(
                seed,
                7.0,
                0.56,
                (1f / 3f).toDouble(),
                position.x.toDouble(),
                (position.y + 999f).toDouble(),
                position.z.toDouble()
            ), 0.5f
        )
        height *= 1.2f
        height = Mathf.clamp(height)
        val tar = Simplex.noise3d(
            seed,
            4.0,
            0.55,
            (1f / 2f).toDouble(),
            position.x.toDouble(),
            (position.y + 999f).toDouble(),
            position.z.toDouble()
        ) * 0.3f + Tmp.v31.dst(0f, 0f, 1f) * 0.2f
        val res = arr[Mathf.clamp(
            (temp * arr.size).toInt(),
            0,
            arr[0].size - 1
        )][Mathf.clamp((height * arr[0].size).toInt(), 0, arr[0].size - 1)]
        return if (tar > 0.5f) {
            tars[res, res]
        } else {
            res
        }
    }

    override fun genTile(position: Vec3, tile: TileGen) {
        tile.floor = getBlock(position)
        tile.block = tile.floor.asFloor().wall
        if (Ridged.noise3d(1, position.x.toDouble(), position.y.toDouble(), position.z.toDouble(), 2, 22f) > 0.31) {
            tile.block = Blocks.air
        }
    }
}