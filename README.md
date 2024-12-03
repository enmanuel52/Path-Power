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
implementation("com.github.enmanuel52:Path-Power:0.0.3")
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


## Beehive Grid
```
    LazyBeehiveVerticalGrid(
        items = (1..120).toList(),
        gridCells = BeehiveGridCells.Adaptive(90.dp),
        spaceBetween = 8.dp,
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues),
        aspectRatio = 1.05f,
    ) { item: Int ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    drawRect(Color(Random.nextLong(0xFFFFFFFF)))
                }
        ) {
            Text(text = "Item $item", modifier = Modifier.align(Alignment.Center))
        }
    }
```

<div style="margin: 10px;">
  <img src="https://github.com/enmanuel52/Path-Power/assets/102194318/7d0df884-b2b4-469d-8712-3bad1921af34" style="display: flex; width: 30%; padding: 0% 5%;">

  <img src="https://github.com/enmanuel52/Path-Power/assets/102194318/3863e57d-2422-4007-917e-b5d7908bcc74" style="display: flex; width: 60%; padding: 0% 5%;">
</div>

## Jump Bottom Bar
```
val ITEMS = listOf(
    JumpingItem(Icons.Rounded.Add),
    JumpingItem(Icons.Rounded.Done),
    JumpingItem(Icons.Rounded.Build),
    JumpingItem(Icons.Rounded.Phone),
    JumpingItem(Icons.Rounded.Delete),
)

@Composable
fun JumpBottomBarSample(modifier: Modifier = Modifier) {

    var selected by remember {
        mutableStateOf(ITEMS.first())
    }

    JumpingBottomBar(items = ITEMS, selected = selected, modifier) {
        selected = it
    }
}
```


https://github.com/user-attachments/assets/880e42ad-8417-4dc0-9bf8-b2081e93729f

## Lazy Heap
[Source Code](https://github.com/enmanuel52/Path-Power/blob/master/app/src/main/java/com/enmanuelbergling/pathpower/CardStack.kt "code")



https://github.com/user-attachments/assets/313a43a8-67b8-4706-9b77-ed8585c28555



https://github.com/user-attachments/assets/9587531b-1587-4d64-8615-4e9e347be651



**🚧🚧 WORK IN PROGRESS 🚧🚧**
