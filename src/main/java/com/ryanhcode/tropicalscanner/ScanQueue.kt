package com.ryanhcode.tropicalscanner

class ScanQueue {

    private val queue = mutableListOf<PlayerScanJob>()

    fun add(job: PlayerScanJob) {
        if(queue.filter { it.username == job.username }.size > 0) return
        queue.add(job)
    }

    fun hasJob() = !queue.isEmpty()

    fun next(): PlayerScanJob {
        val toScan = queue[0]
        queue.removeAt(0)
        return toScan
    }


    fun size() = queue.size

}
