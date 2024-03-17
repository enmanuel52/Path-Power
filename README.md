
```

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

# Beehive Grid
```
LazyBeehiveVerticalGrid(
  items = (1..40).toList(),
  gridCells = beehiveCells,
  key = {rowIndex, _ -> rowIndex },
  spaceEvenly = 6.dp,
  modifier = Modifier
    .fillMaxSize()
  ) { item ->
      ElevatedCard(
        modifier = Modifier.fillMaxSize(),
        shape = LayDownHexagon,
        colors = CardDefaults.elevatedCardColors(LighterHoney)
        ) {
          Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                  painter = painterResource(id = R.drawable.bee),
                  contentDescription = "bee image",
                  Modifier.size(50.dp)
                )

                  Spacer(modifier = Modifier.height(8.dp))

                  Text(text = "Item $item")
                }
            }
        }
```

<div style="margin: 10px;">
  <img src="https://github.com/enmanuel52/Path-Power/assets/102194318/97304bae-fa56-4ef1-90c6-f1cfdec55d49" style="display: flex; width: 30%; padding: 0% 3%;">
</div>


**🚧🚧 WORK IN PROGRESS 🚧🚧**
