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

4. Sometimes, macOS might be over-paranoid and forbid to use .dylibs in the `buildInfra/tools/kotlin-native/konan/nativeLib`. You will see that as macOS warning `<Binary name> can't be opened because
Apple cannot check it for malicious software. This software needs to be updated. Contact the developer for more information`. All the generic fixes from internet should work, the one I used: open binary
 manually from Finder, macOS will properly prompt you if you want to open it. Choose "Open" and it will be remembered as an exception

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

## Repository structure, how to read it

`sources` folder contains several connected simple projects (two Kotlin Multiplatform libraries, and Android/iOS apps) that will be used to demonstrate how Kotlin Multiplatform is compiled,
packaged, distributed and consumed in Kotlin Multiplatform-agnostic clients.

`buildInfra` contains stuff which is usually of a build system concern. `buildInfra/tools` contains various "external" tools like compilers or libraries, necessary for compiling projects
in subject. `buildInfra/logic` contains a logic that descirbes the whole process of compilation and distribution. This logic is written in form of a Kotlin program - reasons for that
are purely subjective, and it should be trivial to convert this logic to any scripting language (like Python), plain shell (like bash), or even into a plugin for a different build system.

## Use-case description

There are two Kotlin Multiplatform libraries in `sources/`-folder: `DirectDependency` and `TransitiveDependency`, as well as Android-only app and iOS-app. 

Both apps depend on `DirectDependency` directly. `DirectDependency`, in turn, depends on `TranstiviveDependency`, so apps see `TransitiveDependency` ... transitively (you probably 
see the pattern in naming at this point :) ) as well.

Both libraries have the same set of targets: `jvm` (read why not `android()` below), `iosArm64`, `iosX64` and `iosSimulatorArm64`. 

Using `jvm` target disallows using Android SDK in those libraries. This was done mostly to simplify the environment and implementation and avoid installing Android SDK.

## Kotlin compilation model

- Kotlin Source Sets
- Targets
- Dependencies:
	- usual dependencies
	- friend-dependencies
	- dependsOn graph. expect/actuals are closed.
- Metadata artifacts and Platform Artifacts, their purpose
- Metadata compilations and Platform compilations
- Is .kotlin_metadata needed? (no, but yes)
- iosMain - looks like a leaf, but it's not (and that's why we're not compiling it)
