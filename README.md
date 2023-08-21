[![](https://jitpack.io/v/nachogoro/fen2img.svg)](https://jitpack.io/#nachogoro/fen2img)
# FEN to Image Library
Convert FEN ([Forsyth–Edwards Notation](https://en.wikipedia.org/wiki/Forsyth%E2%80%93Edwards_Notation)) strings into visual representations, either in SVG (Scalable Vector Graphics) or PNG (Portable Network Graphics) formats.

## Features
- Convert FEN strings directly to SVG or PNG format.
- Customizable board colors, orientation, and piece SVGs.
- Provides default piece images, but allows for easy overrides with custom SVGs.
- Efficient caching mechanism for SVG data retrieval.

## Installation
You may follow the instructions in [jitpack.io](https://jitpack.io/#nachogoro/fen2img/0.1.0)

**Step 1: Add the JitPack repository to your build.gradle file**
Add it in your root `build.gradle` at the end of repositories:
```groovy
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

Alternatively, you may need to add it to `settings.gradle` instead:
```groovy
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```

**Step 2: Add the dependency to your project**
```groovy
dependencies {
    implementation 'com.github.nachogoro:fen2img:0.1.0'
}
```
### Packaging issue in Android
This project depends on Apache Commons' Batik, which itself pulls `xml-apis` and `xml-apis-ext`. Both of those dependencies have similarly-named license files. If you pull this library into an Android application and attempt to build it, you may get some conflicts regarding those files.

You may need to resolve that via `ResourcesPackagingOptions` in your `build.gradle` file. You may decide, for example, to only keep one the first of them:
```groovy
android {
    packagingOptions {
        pickFirst("license/NOTICE")
        pickFirst("license/LICENSE.dom-documentation.txt")
        pickFirst("license/README.dom.txt")
        pickFirst("license/LICENSE.dom-software.txt")
        pickFirst("license/LICENSE")
    }
}
```

## Usage
**1. Create the image generator object:**
   ```kotlin
   val fenConverter = Fen2Img()
   ```
   The `Config` class can be used to customize the images, see section [Customizing the board](#customizing-the-board).

**2. Convert FEN to SVG:**
   ```kotlin
   val svgString = fenConverter.Fen2Svg("[YOUR_FEN_STRING]")
   ```
   
**3. Convert FEN to PNG:**
The following code converts a FEN string to a 400x400 pixels PNG image:
   ```kotlin
   val pngString = fenConverter.Fen2Png("[YOUR_FEN_STRING]", 400)
   ```

## Customizing the board
You can pass your own `Config` object to the constructor of `Fen2Img`, to customize your images.

### Player Orientation
You can set the orientation of the board to be from either the white or black player's perspective:
```kotlin
val config = Config(orientation = Player.BLACK)
```

### Board Colors
Customize the colors of the chessboard's light and dark squares:
```kotlin
val config = Config(lightSquareColor = "#FFFFFF", darkSquareColor = "#000000")
```

### Custom SVGs for Pieces
Override the default SVGs for pieces by providing a map of piece characters to their custom SVG string representations:
```kotlin
val customSvgMap: Map<Char, String> = mapOf('K' to customKingSvg, 'Q' to customQueenSvg)
val config = Config(svgData = customSvgMap)
```

## Examples
The following examples show different ways of displaying the board after:
`1. e4..c5    2. Nf3`

### Default image (SVG)
```kotlin
val fenConverter = Fen2Img()
val svgData = fenConverter.Fen2Svg("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2")
```
![default](https://github.com/nachogoro/fen2img/assets/15671779/5134e61f-ccfc-4409-9b4c-4b287652de64)


### From black's perspective (SVG)
```kotlin
val fenConverter = Fen2Img(Config(orientation=Player.BLACK))
val svgData = fenConverter.Fen2Svg("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2")
```
![black](https://github.com/nachogoro/fen2img/assets/15671779/df6b7c21-f46a-4fd2-84ba-0aafb9e8bc1c)

### Using chess.com colorscheme (500x500 PNG)
```kotlin
val fenConverter = Fen2Img(Config(lightSquareColor = "#ebecd0", darkSquareColor = "#779556"))
val svgData = fenConverter.Fen2Png("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2", 500)
```
<img src="https://github.com/nachogoro/fen2img/assets/15671779/a4463f7f-49c5-4f8f-a141-d59d51916a15" width="400" height="400">


### Using chess.com colorscheme and the black pawns are ducks (SVG)
```kotlin
val duckData: String = """<?xml version="1.0" encoding="iso-8859-1"?>
    <!-- Uploaded to: SVG Repo, www.svgrepo.com, Generator: SVG Repo Mixer Tools -->
    <!DOCTYPE svg PUBLIC "-//W3C//DTD SVG 1.1//EN" "http://www.w3.org/Graphics/SVG/1.1/DTD/svg11.dtd">
    <svg fill="#000000" version="1.1" id="Capa_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" 
	     width="800px" height="800px" viewBox="0 0 209.322 209.322"
	     xml:space="preserve">
    <g>
	    <path d="M105.572,101.811c9.889-6.368,27.417-16.464,28.106-42.166c0.536-20.278-9.971-49.506-49.155-50.878
		    C53.041,7.659,39.9,28.251,36.071,46.739l-0.928-0.126c-1.932,0-3.438,1.28-5.34,2.889c-2.084,1.784-4.683,3.979-7.792,4.308
		    c-3.573,0.361-8.111-1.206-11.698-2.449c-4.193-1.431-6.624-2.047-8.265-0.759c-1.503,1.163-2.178,3.262-2.028,6.226
		    c0.331,6.326,4.971,18.917,16.016,25.778c7.67,4.765,16.248,5.482,20.681,5.482c0.006,0,0.006,0,0.006,0
		    c2.37,0,4.945-0.239,7.388-0.726c2.741,4.218,5.228,7.476,6.037,9.752c2.054,5.851-27.848,25.087-27.848,55.01
		    c0,29.916,22.013,48.475,56.727,48.475h55.004c30.593,0,70.814-29.908,75.291-92.48C180.781,132.191,167.028,98.15,105.572,101.811
		    z M18.941,77.945C8.775,71.617,4.992,58.922,5.294,55.525c0.897,0.24,2.194,0.689,3.228,1.042
		    c4.105,1.415,9.416,3.228,14.068,2.707c4.799-0.499,8.253-3.437,10.778-5.574c0.607-0.509,1.393-1.176,1.872-1.491
    		c0.87,0.315,0.962,0.693,1.176,3.14c0.196,2.26,0.473,5.37,2.362,9.006c1.437,2.761,3.581,5.705,5.646,8.542
	    	c1.701,2.336,4.278,5.871,4.535,6.404c-0.445,1.184-4.907,3.282-12.229,3.282C30.177,82.591,23.69,80.904,18.941,77.945z
		     M56.86,49.368c0-4.938,4.001-8.943,8.931-8.943c4.941,0,8.942,4.005,8.942,8.943c0,4.931-4.001,8.942-8.942,8.942
		    C60.854,58.311,56.86,54.299,56.86,49.368z M149.159,155.398l-20.63,11.169l13.408,9.293c0,0-49.854,15.813-72.198-6.885
		    c-11.006-11.16-13.06-28.533,4.124-38.84c17.184-10.312,84.609,3.943,84.609,3.943L134.295,147.8L149.159,155.398z"/>
    </g>
    </svg>
    """

val fenConverter = Fen2Img(Config(
   lightSquareColor = "#ebecd0",
   darkSquareColor = "#779556",
    svgData=mapOf('p' to duckData)))
val svgData = fenConverter.Fen2Svg("rnbqkbnr/pp1ppppp/8/2p5/4P3/5N2/PPPP1PPP/RNBQKB1R b KQkq - 1 2")
```
![chesscom-ducks](https://github.com/nachogoro/fen2img/assets/15671779/675db32c-e140-4e13-ac80-dcdff012f7c6)


## Contributing
Pull requests are welcome!
