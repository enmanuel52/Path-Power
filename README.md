# Path Power

1- First that all add this in the settings.gradle.kts
```
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        maven { url = uri("https://jitpack.io") }
    }
}
```

2- Second add the dependency in your build.gradle.kts
```
implementation("com.github.enmanuel52:Path-Power:0.0.1-alpha01")
```

# Charts
```
// Line Chart on video down below

val earnings = listOf(15f, 45f, 18f, 20f, 21f, 35f, 25f)

ChartGridContainer(
      chartData = ChartUiModel(
          steps = 4,
          values = earnings.mapIndexed { index, value ->
              ChartValue(
                  value = value,
                  label = index.toString(),
              )
          }
      ),
      style = ChartStyle.Line,
      modifier = Modifier
          .aspectRatio(1.4f)
          .fillMaxWidth()
          .padding(6.dp)
  )
```



https://github.com/enmanuel52/Path-Power/assets/102194318/60ec5604-4308-41f3-b844-e768a85856fb



# Waves animation
```
AnimatedWavesIndicator(
  progress = animatedProgress,
  modifier = Modifier
    .size(300.dp, 270.dp)
    .clip(Heart)
    .border(4.dp, MaterialTheme.colorScheme.surfaceVariant, Heart),
    color = MaterialTheme.colorScheme.primary,
    waveForce = WaveForce.Custom(waveHeightPercent, 1100)
)
```


https://github.com/enmanuel52/Path-Power/assets/102194318/1511ad6a-9234-4339-8bab-b8399b1d08b1

# Comming soon

## Beehive Grid
```
    LazyBeehive(
        items = (1..120).toList(),
        columns = 3,
        spaceBetween = 8.dp,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        aspectRatio = 1.05f,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawRect(Color(Random.nextLong(0xFFFFFFFF)))
                }
        ) {
            Text(text = "Item $it", modifier = Modifier.align(Alignment.Center))
        }
    }
```

<div style="margin: 10px;">
  <img src="https://github.com/enmanuel52/Path-Power/assets/102194318/e71fe40b-feaa-4ce5-990f-82326cfd2254" style="display: flex; width: 40%; padding: 0% 3%;">
</div>

**🚧🚧 WORK IN PROGRESS 🚧🚧**
