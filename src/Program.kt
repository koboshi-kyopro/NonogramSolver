import java.util.*

fun main() {
    var iterate = 0
    val a = mutableListOf(
        mutableListOf(3),
        mutableListOf(1),
        mutableListOf(3),
        mutableListOf(1),
        mutableListOf(3),
    )
    val b = mutableListOf(
        mutableListOf(1, 3),
        mutableListOf(1, 1, 1),
        mutableListOf(3, 1),
    )
    val n = a.size
    val m = b.size
    val state = Array(n) { IntArray(m) { 0 } }
    // 0:未確定, 1:■, -1:×
    var finish: Boolean
    do {
        finish = true
        for (i in 0 until n) {
            if (!state[i].contains(0)) continue
            val cands = mutableListOf<Array<Int>>()
            bitSearch@ for (bits in 0 until 1.shl(m)) {
                val choice = Array(m) { 0 }
                for (j in 0 until m) {
                    choice[j] = if ((1.shl(j) and bits) != 0) 1 else -1
                }
                for (j in 0 until m) {
                    if ((state[i][j] == 1 && choice[j] == -1) || (state[i][j] == -1 && choice[j] == 1)) {
                        continue@bitSearch
                    }
                }
                if (check(a[i], choice)) cands.add(choice)
            }
            for (j in 0 until m) {
                if (cands.all { it[j] == 1 }) {
                    state[i][j] = 1
                    finish = false
                } else if (cands.all { it[j] == -1 }) {
                    state[i][j] = -1
                    finish = false
                }
            }
        }

        for (i in 0 until m) {
            if (!(0 until n).map { state[it][i] }.contains(0)) continue
            val cands = mutableListOf<Array<Int>>()
            bitSearch@ for (bits in 0 until 1.shl(n)) {
                val choice = Array(n) { 0 }
                for (j in 0 until n) {
                    choice[j] = if ((1.shl(j) and bits) != 0) 1 else -1
                }
                for (j in 0 until n) {
                    if ((state[j][i] == 1 && choice[j] == -1) || (state[j][i] == -1 && choice[j] == 1)) {
                        continue@bitSearch
                    }
                }
                if (check(b[i], choice)) cands.add(choice)
            }
            for (j in 0 until n) {
                if (cands.all { it[j] == 1 }) {
                    state[j][i] = 1
                    finish = false
                } else if (cands.all { it[j] == -1 }) {
                    state[j][i] = -1
                    finish = false
                }
            }
        }
        iterate++
    } while (!finish && iterate < 100)
    for (line in state) {
        println(line.map { if (it == 1) '■' else if (it == 0) '？' else '×' }.joinToString(""))
    }
}

fun check(a: List<Int>, choice:Array<Int>):Boolean {
    val b = mutableListOf<Int>()
    var streak = 0
    val size = choice.count()
    for (i in 0 until size) {
        if (choice[i] == 1) streak++
        else if (streak > 0) {
            b.add(streak)
            streak = 0
        }
    }
    if (streak > 0) b.add(streak)
    return a.count() == b.count() && (0 until a.count()).all { a[it] == b[it] }
}