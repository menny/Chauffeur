# Chauffeur
A very simple fragments driver mechanism.
In addition, it provides a simple Android Marshmallow Runtime Permissions handling.

# Dependency
I'm using [![JitPack](https://img.shields.io/github/release/menny/Chauffeur.svg?label=JitPack)](https://jitpack.io/#menny/Chauffeur) to publish this library.

Add it in your build.gradle at the end of repositories:
```
repositories {
    //...
    maven { url "https://jitpack.io" }
}
```
Step 2. Add the dependency in the form
```
dependencies {
    compile 'com.github.menny.Chauffeur:lib:-SNAPSHOT'
}
```
Alternative Step 2. Add the dependency to the permissions handling library:
```
dependencies {
    compile 'com.github.menny.Chauffeur:permissions:-SNAPSHOT'
}
```

# Usage

[Storynory](https://github.com/menny/ironhenry) and [AnySoftKeyboard](https://github.com/AnySoftKeyboard/AnySoftKeyboard) are using this library. Reference to those for real world usage.

# License

Copyright 2016 Menny Even-Danan

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
