# homes | [![Codacy Badge](https://app.codacy.com/project/badge/Grade/2e78bb17e58a4487b38de7dd515dd5f1)](https://app.codacy.com/gh/aivruu/homes/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)
![GitHub Actions Workflow Status](https://img.shields.io/github/actions/workflow/status/aivruu/homes/build.yml)
![GitHub License](https://img.shields.io/github/license/aivruu/homes)
![GitHub commit activity](https://img.shields.io/github/commit-activity/t/aivruu/homes)

`AldrHomes` is a modern-plugin that provides the functionality to create multiple home-points that you can use to travel to any place
such as farms, houses, 

This plugin was made to run with `Paper (or forks)` on versions 1.21.1 and newer.

## Features
* Create up to five home-points at any place.
* Home-points management such as, teleporting, deletion or modification.
* Async, the teleports are processed asynchronously by the server (for chunks loading).
* Easy to use.

## Building
The plugin's uses Gradle and requires Java 21+ for building.
```
git clone git@github.com:aivruu/homes
cd homes
./gradlew shadowJar
```
