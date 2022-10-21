# mpp-without-gradle
MVP of Kotlin Multiplatform library/app built with barebones without Gradle

## Environment

1. Download the latest Kotlin Distribution `kotlin-compiler-<version>` (I've checked everything on 1.7.20 version) from [GitHub Releases](https://github.com/JetBrains/kotlin/releases) page 
   and unpack its content (`kotlinc`) to `buildInfra/tools`. 

   Note that there are `kotlinc-metadata` and `kotlinc-metadata.bat` shellscripts in `buildInfra/tools/kotlinc/bin` already. This is because we don't distribute them for now. This will be fixed
   in future updates. 

2. Currently. Kotlin Distribution doesn't ship with `kotlin-stdlib-common.jar`. This will be fixed in further updates. For now, just download it manually from 
   [Maven](https://mvnrepository.com/artifact/org.jetbrains.kotlin/kotlin-stdlib-common) and put into `buildInfra/tools/kotlinc/lib`, removing the verstion suffix (so the final `jar` has name `kotlin-stdlib-common.jar`)

3. From the same release, download the `kotlin-native-<your-host>-<version>.zip` (I've used `macos-aarch64` for testing), unpack its contents to `buildInfra/tools` and **rename it to 
	just `kotlin-native`**. 

There's the resulting file structure you should get:

```
buildInfra
├── logic
├── output
└── tools
    ├── kotlin-native
    └── kotlinc
        ├── bin
        │   ├── ...
        │   ├── kotlinc-metadata
        │   ├── kotlinc-metadata.bat
        │   └── ...
        ├── build.txt
        ├── lib
        │   ├── ...
        │   ├── kotlin-stdlib-common.jar
        │   └── ...
        └── license
```

## Use-case description

- explain iosMain
- explain limitation with jvm (in TransitiveDependency)

## Sample structure, how to read it

## Kotlin compilation model

- Kotlin Source Sets
- Targets
- Dependencies:
	- usual dependencies
	- friend-dependencies
	- dependsOn graph. expect/actuals are closed.
- Metadata artifacts and Platform Artifacts, their purpose
- Metadata compilations and Platform compilations
