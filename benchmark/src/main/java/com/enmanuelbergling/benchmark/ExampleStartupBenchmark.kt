package com.enmanuelbergling.benchmark

import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@RunWith(AndroidJUnit4::class)
class ExampleStartupBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun scrollCompilationModeNone() = scrolling(CompilationMode.None())

    @Test
    fun scrollCompilationModePartial() = scrolling(CompilationMode.Partial())

    fun scrolling(compilationMode: CompilationMode) = benchmarkRule.measureRepeated(
        packageName = "com.enmanuelbergling.pathpower",
        metrics = listOf(FrameTimingMetric()),
        iterations = 10,
        startupMode = StartupMode.COLD
    ) {
        pressHome()
        startActivityAndWait()

        scrollDownAndUp()
    }
}

fun MacrobenchmarkScope.scrollDownAndUp() {

    device.wait(Until.findObject(By.text("Item 1")), 5000)

    val beehive = device.findObject(By.res("beehive"))

    beehive.setGestureMargin(device.displayWidth / 5)

    beehive.fling(Direction.DOWN, ONE_MILLION)

    beehive.fling(Direction.UP, ONE_MILLION)
}

const val ONE_MILLION = 1000000