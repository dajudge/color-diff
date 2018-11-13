# color-diff
[![Pipeline status](https://gitlab.com/dajudge/color-diff/badges/master/pipeline.svg)](https://gitlab.com/dajudge/color-diff/commits/master)
[![Maven Central](https://img.shields.io/maven-central/v/com.dajudge.color-diff/color-diff.svg?maxAge=2592000)](http://search.maven.org/#search%7Cga%7C1%7Cg%3Acom.dajudge.color-diff%20a%3Acolor-diff)

__Disclaimer__: This project aims to be a 1:1 port of Markus Ekholm's great
[color-diff](https://github.com/markusn/color-diff) library which is
written in Javascript (hence the occasional untypical naming convention).

Implements the CIEDE2000 color difference algorithm, conversion between RGB and
LAB color and mapping all colors in palette X to the closest color in palette Y
based on the CIEDE2000 difference.

## Installation
color-diff is available on maven central. It works on JDK 8 and later. Use the following snipped in 
your `pom.xml` to add it as a dependency:
```xml
<dependency>
    <groupId>com.dajudge.color-diff</groupId>
    <artifactId>color-diff</artifactId>
    <version>0.0.4</version>
</dependency>
```
If you're using gradle then you'll want something like this:
```groovy
buildscript {
    repositories {
        mavenCentral()
    }
}

dependencies {
    compile 'com.dajudge.color-diff:color-diff:0.0.4'
}
```
color-diff has no transitive dependencies, so you won't face any compatiblity issues.
 
## Build / Tests
If you want to build it yourself just checkout the source and run the following command:
```
./gradlew clean build
```
Tests are implemented in JUnit 5 and run with the build.
## Usage

### ColorDiff.closest(color, palette, bc)

Returns the closest color. The parameter bc is optional and is used as
background color when the color and/or palette uses alpha channels.

```java
RgbColor color = new RgbColor(255, 1, 30);

List<RgbColor> palette = Arrays.asList(
        new RgbColor(255, 0, 0), // red
        new RgbColor(0, 255, 0), // green
        new RgbColor(0, 0, 255)  // blue
);

RgbColor closestMatch = ColorDiff.closest(color, palette);
assertEquals(new RgbColor(255, 0, 0), closestMatch); // red
```

The result above is obvious, but `ColorDiff.closest()` could deal with more complicated
cases.

### ColorDiff.furthest(color, palette, bc)

Returns the most different color. The parameter bc is optional and is used as
background color when the color and/or palette uses alpha channels.

```java
RgbColor color = new RgbColor(255, 255, 255); // white

List<RgbColor> palette = Arrays.asList(
        new RgbColor(0, 0, 0),      // black
        new RgbColor(255, 255, 255) // white
);

RgbColor furthestMatch = ColorDiff.furthest(color, palette);
assertEquals(new RgbColor(0,0,0), furthestMatch); // black
```

The result above is obvious, but `ColorDiff.furthest()` could deal with more
complicated cases.


### ColorDiff.map_palette(palette1, palette2)

Returns a mapping from the colors in `palette1` to `palette2`.


#### RgbColor
`RgbColor` is type containing 3 properties: `R`, `G`, `B`, such as:

```java
RgbColor color = new RgbColor(255, 1, 0);
```

There is an optional (i.e. nullable) property `A`, which specifies
the alpha channel between 0.0 and 1.0.

```java
RgbColor colorWithAlpha = new RgbColor(255, 1, 0, .5);
```

Each RGBA-color is transformed into a RGB-color before being used to calculate
the CIEDE2000 difference, using the specified background color (which defaults to white).

## Authors
Original Javascript version by Markus Ekholm

Java port by Alex Stockinger

## License
3-clause BSD. For details see `COPYING`.
