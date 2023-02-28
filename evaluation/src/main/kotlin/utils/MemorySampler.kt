package utils

import java.util.LinkedList
import java.util.concurrent.CompletableFuture
import kotlin.math.max

class MemorySampler(period: Long) : Thread() {

    private val period: Long = max(period, 0L)

    data class Stats(val max: Long, val sum: Long, val size: Int) {
        val avg: Double
            get() = sum.toDouble() / size.toDouble()

        /**
         * adds the new sample to the statistic.
         */
        operator fun plus(sample: Long) = Stats(max(max, sample), sum + sample, size + 1)
    }

    private fun sampleNow(): Long =
        Runtime.getRuntime().let { it.totalMemory() - it.freeMemory() }

    private val samples: MutableList<Long> = LinkedList()
    private val result: CompletableFuture<Stats> = CompletableFuture()

    @Volatile
    private var shouldStop: Boolean = false

    override fun start() {
        samples.add(sampleNow())
        super.start()
    }

    override fun run() {
        while (!shouldStop) {
            samples.add(sampleNow())
            if (period > 0) {
                sleep(period)
            }
        }
        result.complete(computeStats(samples))
    }

    /**
     * termines the execution of the operations performed by the thread.
     */
    fun terminate(): Stats {
        shouldStop = true
        val result = result.get() + sampleNow()
        if (result.size > 1) {
            return result
        } else {
            error("You should decrease the period")
        }
    }

    private fun computeStats(samples: List<Long>): Stats = Stats(
        max = samples.max(),
        sum = samples.sum(),
        size = samples.size,
    )
}
