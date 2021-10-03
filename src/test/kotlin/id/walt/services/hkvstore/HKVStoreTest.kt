package id.walt.services.hkvstore

import id.walt.servicematrix.ServiceRegistry
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import java.nio.file.Path

class HKVStoreTest : StringSpec({

    fun hkvTest() {
        val hkvStore = HKVStoreService.getService()
        val testData = "test data"

        println("Adding items...")
        hkvStore.put(Paths.get("parent", "child", "leaf"), testData)
        hkvStore.put(Paths.get("parent", "child2", "leaf2"), testData)

        println("Retrieving items...")
        hkvStore.listChildKeys(Paths.get("parent"), true) shouldHaveSize 2
        hkvStore.listChildKeys(Paths.get("parent", "child")) shouldHaveSize 1

        println("Retrieving data...")
        hkvStore.getAsString(Paths.get("parent", "child", "leaf")) shouldBe testData

        println("Deleting items...")
        hkvStore.delete(Paths.get("parent"), true)
        hkvStore.listChildKeys(Paths.get("parent"), true) shouldHaveSize 0
    }

    "InMemoryStore test" {
        ServiceRegistry.registerService<HKVStoreService>(InMemoryHKVStore())
        hkvTest()
    }
    "FileSystemStore test" {
        ServiceRegistry.registerService<HKVStoreService>(FileSystemHKVStore("./fsStore.conf"))
        hkvTest()
    }
})
