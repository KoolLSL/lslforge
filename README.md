# LSLForge

An LSL Script Editor/compiler for Second Life's Scripting Language.

LSLForge is a fork of the popular LSLPlus editing environment, to provide Linden Scripting Language (LSL) support in Eclipse.

## Description

LSLForge is a plugin for the Eclipse platform. The plugin allows editing, "compiling" (gathering code included in modules), executing, and unit testing your Second Life® Linden Scripting Language (LSL) code.

Because it is an Eclipse plugin, when you use LSLForge you can take advantage of many of the useful features of Eclipse. Integrated support of a source code control system such as Git, CVS and Subversion comes for "free", and there are many other features &mdash; task list management, integration with bug tracking tools, etc. You will of course need to make sure you get the appropriate plugins to use these features.

## Forks Purposes

* [`KoolLSL`](https://github.com/KoolLSL/lslforge): Updating since 0.1.9.10, cloned from raysilent
* [`raysilent`](https://github.com/raysilent/lslforge): This was a fork where the most activity is happening, which is maintaining the latest LSL definitions for functions and constants of `LSL` according to the recent [Release Notes](https://releasenotes.secondlife.com/categories/simulator.html), also bug fixes, and keeping the Eclipse Plugin Update Site up-to-date.
* [`elnewfie`](https://github.com/elnewfie/lslforge): After all the tests are accomplished, the repo is merged into the [`elnewfie`](https://github.com/elnewfie/lslforge)'s original repo, to avoid confusion.

### Changelog

* **0.1.9.12**
    * Added functions and constants till Simulator Release 2024-02-21.7995320426 (llGetNotecardLineSync, llComputeHash, llRezObjectWithParams...) thanks to [@ChloeConstantine](https://github.com/ChloeConstantine)
    * Renamed some folders (eclipse to update-site, lslforge to lslforge-dev, lslforge/eclipse to lslforge-dev/eclipse-project)
    * Updated all_compile_haskell_win32.bat
    * Compiled only for win32 (not mac, linux)
     
* **0.1.9.11**
    * Added constants: CLICK_ACTION_IGNORE, PRIM_SCULPT_TYPE_MESH, PRIM_SCULPT_FLAG_ANIMESH, SIM_STAT_* 
    * Added functions: llLinksetDataDeleteFound, llLinksetDataCountFound, llList2ListSlice, llListSortStrided, llListFindStrided

* **0.1.9.10**
    * Still under developement. Trying to add the lastest LSL functions. Tested only in Windows.
    * Cloned from https://github.com/raysilent/lslforge 0.1.9.9 the 16/01/2023.
    * Adding updates made by [@ChloeConstantine](https://github.com/ChloeConstantine). 
        
* **0.1.9.9**
    * [@PellSmit](https://github.com/PellSmit) did an amazing job bringing this version up-to-date with the Mac Silicone, newest Java and Eclipse:
        * Java update to 11 (`JavaSE-11`), which allows to run the latest Eclipse editions.
        * Update to `ghc-8.10.7`.
    * Simulator up to [2022-01-06.567269](https://releasenotes.secondlife.com/simulator/2022-01-06.567269.html)
        * [2021-10-25.565008](https://releasenotes.secondlife.com/simulator/2021-10-25.565008.html)
            - [x] [`PRIM_PROJECTOR`](http://wiki.secondlife.com/wiki/PRIM_PROJECTOR)
            - [ ] (#66) - 1024 bytes returned from [llGetNotecardLine](http://wiki.secondlife.com/wiki/LlGetNotecardLine)
                  starting with above simulator version.

* **0.1.9.8**
    * Update to `ghc-8.6.5` (Thanks [@PellSmit](https://github.com/PellSmit))
    * Fixed bugs (Thanks [@PellSmit](https://github.com/PellSmit))
    * Simulator up to [2021-08-27.563375](https://releasenotes.secondlife.com/simulator/2021-08-27.563375.html):
        - [x] [`llOrd`](http://wiki.secondlife.com/wiki/LlOrd)
        - [x] [`llChar`](http://wiki.secondlife.com/wiki/LlChar)
        - [x] [`llHash`](http://wiki.secondlife.com/wiki/LlHash)
        - [x] [`llGetEnv()`](http://wiki.secondlife.com/wiki/LlGetEnv) constants: `"whisper_range"`, `"chat_range"` & `"shout_range"` (in comments)
        - [x] [`llGetInventoryAcquireTime`](http://wiki.secondlife.com/wiki/LlGetInventoryAcquireTime)
        - [x] [`llOpenFloater`](http://wiki.secondlife.com/wiki/LlOpenFloater)
            - [ ] (Postponed) Return error codes seem to be missing in the viewer.
        - [x] [`CLICK_ACTION_DISABLED`](http://wiki.secondlife.com/wiki/CLICK_ACTION_DISABLED)
        - [ ] (Postponed) [`TARGETED_EMAIL_ROOT_CREATOR`](http://wiki.secondlife.com/wiki/TARGETED_EMAIL_ROOT_CREATOR) is eliminated (exists in the viewer for backward compatibility most likely). It should be safer to remove it completely from the IntelliSense for early error discovery rather than keep it here.
        - [x] Mark [`llXorBase64StringsCorrect`](http://wiki.secondlife.com/wiki/LlXorBase64StringsCorrect) as deprecated in comments.

* **0.1.9.7**
    * Fixed bugs (Thanks [@PellSmit](https://github.com/PellSmit))
    * New functions and constants:
        * `llGetDayLength`
        * `llGetDayOffset`
        * `llGetMoonDirection`
        * `llGetMoonRotation`
        * `llGetRegionDayLength`
        * `llGetRegionDayOffset`
        * `llGetRegionMoonDirection`
        * `llGetRegionMoonRotation`
        * `llGetRegionSunDirection`
        * `llGetRegionSunRotation`
        * `llGetSunRotation`
        * `llReplaceAgentEnvironment`
        * `llSetAgentEnvironment`
        * `llTargetedEmail` and its constants:
            * `TARGETED_EMAIL_ROOT_CREATOR`
            * `TARGETED_EMAIL_OBJECT_OWNER`
        * `ENV_NOT_EXPERIENCE`
        * `ENV_NO_EXPERIENCE_PERMISSION`
        * `ENV_NO_ENVIRONMENT`
        * `ENV_INVALID_AGENT`
        * `ENV_NO_EXPERIENCE_LAND`
        * `ENV_VALIDATION_FAIL`
        * `ENV_NO_EXPERIENCE_LAND`
        * `ENV_THROTTLE`
        * `ENVIRONMENT_DAYINFO`
        * `INVENTORY_SETTING`
        * `SKY_CLOUD_TEXTURE`
        * `SKY_MOON_TEXTURE`
        * `SKY_SUN_TEXTURE`
        * `WATER_NORMAL_TEXTURE`
        * (Bakes on Mesh related):
            * `IMG_USE_BAKED_HEAD`
            * `IMG_USE_BAKED_UPPER`
            * `IMG_USE_BAKED_LOWER`
            * `IMG_USE_BAKED_EYES`
            * `IMG_USE_BAKED_SKIRT`
            * `IMG_USE_BAKED_HAIR`
            * `IMG_USE_BAKED_LEFTARM`
            * `IMG_USE_BAKED_LEFTLEG`
            * `IMG_USE_BAKED_AUX1`
            * `IMG_USE_BAKED_AUX2`
            * `IMG_USE_BAKED_AUX3`
* 2018-09-16 LSLForge **0.1.9.6** (**Windows**, **Linux** (Thanks [@Trapez](https://github.com/Trapez)), **macOS** (Thanks [@PellSmit](https://github.com/PellSmit)))
    * Animesh Functions and Constants:
        * `llStartObjectAnimation(string anim)`
        * `llStopObjectAnimation(string anim)`
        * `list llGetObjectAnimationNames()`
        * `integer OBJECT_CREATION_TIME = 36`
        * `integer OBJECT_SELECT_COUNT = 37`
        * `integer OBJECT_SIT_COUNT = 38`
* 2018-09-14 LSLForge **0.1.9.5** (**Windows**, **Linux** (Thanks [@Trapez](https://github.com/Trapez)), **macOS** (Thanks [@PellSmit](https://github.com/PellSmit)))
    * Experimental hot deploy of the newly selected native executable
    * "Generated" comment at the end of `*.lsl` file (easier to check what was copy-pasted) (**Windows** only)
* 2018-09-10 LSLForge **0.1.9.4** (**Windows**, **macOS** Only)
    * Fixed `Tuple*.java` disappearance (Thanks [@PellSmit](https://github.com/PellSmit))
    * Upgraded Haskell (Thanks [@simon-nicholls](https://github.com/simon-nicholls))
    * `HTTP_USER_AGENT`
    * `OBJECT_RENDER_WEIGHT`
    * `key llName2Key(string name)`
    * `key llRequestUserKey(string name)`
* 2017-02-10 LSLForge **0.1.9.3** (**Windows**, **Linux** (Thanks [@Trapez](https://github.com/Trapez)), **macOS** (Thanks [@PellSmit](https://github.com/PellSmit)))
    * `ATTACH_FACE_TONGUE` misspelled
* 2017-01-07 LSLForge **0.1.9.2** (**Windows**, **Linux** (Thanks [@Trapez](https://github.com/Trapez)), **macOS** (Thanks [@PellSmit](https://github.com/PellSmit)))
    * `OBJECT_ATTACHED_SLOTS_AVAILABLE`
    * `llGetEnv("region_object_bonus")` (in comments)
* 2016-11-13 LSLForge **0.1.9.1** (**Windows**, **Linux** (Thanks [@Trapez](https://github.com/Trapez)), **macOS** (Thanks [@PellSmit](https://github.com/PellSmit)))
    * `OBJECT_GROUP_TAG`, `OBJECT_TEMP_ATTACHED` added
    * Bug fixes (Thanks [@PellSmit](https://github.com/PellSmit)):
        * [#35](https://github.com/raysilent/lslforge/issues/35) (negative out of range index)
        * [#6](https://github.com/raysilent/lslforge/issues/6) (backslash in string)
        * [#26](https://github.com/raysilent/lslforge/issues/26) (multiline string bug)
    * Bug fix [#37](https://github.com/raysilent/lslforge/issues/37) (cannot Run -> Run as -> Launch in LSL Sim)
    * Bug fix some null pointer exceptions during recompiled
* 2016-11-08 LSLForge **0.1.9** (Windows, Linux, macOS)
    * `JSON_APPEND`, `CLICK_ACTION_ZOOM` (seems to be absent in the SL viewer, use `7` instead) added
* 2016-10-22 LSLForge **0.1.8** (Windows only)
    * `XP_ERROR_REQUEST_PERM_TIMEOUT` missing added
* 2016-10-20 LSLForge **0.1.7** (Windows only)

## Second Life® Group

The official group for LSLForge Editor tool is [LSLForge Users](secondlife:///app/group/381ff28c-1171-27ac-77f5-ded3471b6245/about). General announcements, questions and answers.

## Installing

* Java 17 JDK is currently used for generating the plugin. You may find that you need to update to Java 17 or later in order to use the plugin.

### Eclipse Plugin

#### Compatible Versions

The following [Eclipse Distributions](https://www.eclipse.org/downloads/packages/release) were found working:
* Eclipse IDE 2024-03 (4.31.0) since 0.1.9.10
* Eclipse IDE 2020-12 (4.18.0) till 0.1.9.9
* Eclipse IDE 2019-12 (4.14.0)
* Eclipse IDE 2019-06 (4.12.0)
* Eclipse Photon (4.8.0)
* Eclipse Oxygen

> NOTE: Oomph seems to restore LSLForge native setting despite attempts to overwrite the field. The only workaround for now is to check `[X] Skip automatic task execution at startup time` under Oomph > Setup Tasks in Preferences. 

Platforms that used to work but **not tested recently**:

* Eclipse Neon (4.6.0)
* Eclipse Mars.2 (4.5.2)
* Eclipse Mars.1 (4.5.1)
* Eclipse Luna (4.4.0)
* Eclipse Juno RC2 (4.2.2)

#### How to Install

To install a plugin into Eclipse, choose `Help` > `Install New Software`. Click `Add...` and enter the link for location:

* For the tested and reported to be working releases use `elnewfie`'s master repo:
    * `https://raw.githubusercontent.com/elnewfie/lslforge/master/eclipse/`

* For releases that are currently being tested use `raysilent`'s master repo:
    * `https://raw.githubusercontent.com/raysilent/lslforge/master/eclipse/`

    (In case of bugs please report right to the https://github.com/raysilent/lslforge/ repo.)

* Alternatively you may switch to a development fork and try a specific version since `0.1.8` (including work in progress branches):
    * `https://raw.githubusercontent.com/raysilent/lslforge/0.1.9.8/eclipse/`
    * `https://raw.githubusercontent.com/raysilent/lslforge/0.1.9.7/eclipse/` (Reinstall if you already have the version)
    * `https://raw.githubusercontent.com/raysilent/lslforge/0.1.9.6/eclipse/`
    * `https://raw.githubusercontent.com/raysilent/lslforge/0.1.9.5/eclipse/`
    * `https://raw.githubusercontent.com/raysilent/lslforge/0.1.9.4/eclipse/`
    * `https://raw.githubusercontent.com/raysilent/lslforge/0.1.9.3/eclipse/`
    * `https://raw.githubusercontent.com/raysilent/lslforge/0.1.9.2/eclipse/`
    * `https://raw.githubusercontent.com/raysilent/lslforge/0.1.9.1/eclipse/`
    * `https://raw.githubusercontent.com/raysilent/lslforge/0.1.9/eclipse/`
    * `https://raw.githubusercontent.com/raysilent/lslforge/0.1.8/eclipse/`

* For even older version, clone the whole repo and link your Eclipse to a particular folder under `eclipse\archive`.

> If you don't see any items for installing, try to uncheck "Group items by category"

> Run `eclipse -clean` to force it to forget cached downloads

Checkbox 2 items:

* "LSLForge"
* One of the native parts according to your environment.

Install, accept and restart Eclipse

Switch to **LSLForge Perspective** and create a new LSLForge Project

## Known Issues

* `*.lslp` files compilation issues although everything is correct. It may happen when a lot of `$import` keywords are used and at some point the compiler gets stuck. What may help is:

    * Adding a fake `*.lslm` module along the project, it could be called `Fake.lslm`. Opening it and adding a space, then removing it and hitting **Save** will force the project to be recompiled
    * Forcing recompilation of a module that is referenced by `*.lslp` file by opening it, doing some fake change, and hitting **Save**

## Tips & Tricks

### Importing Modules

This demonstrates:

* How to use folders when importing modules (dot notation)
* How to import a module with a paramater

**`Modules/Debug.lslm`** :

```
$module (integer DEBUG)
// pragma inline
bug(string place, string message) {
  if (DEBUG) llOwnerSay("["+llGetScriptName()+"."+place+"]: "+message);
}
```

**`Script.lslp`** :

```
integer DEBUG=TRUE; // has to be a variable
$import Modules.Debug.lslm(DEBUG=DEBUG) de;

do() {
  debug("do()", "This is a call of 'bug' function from 'de' module");
}

```

### Referencing Modules From Other Projects

Imagine you move `Modules` folder to a separate project called `ModulesProject` to use it from different other projects.
In the main project that uses `ModulesProject`, place a checkbox along its name under `Project settings > Project References`.

`ModulesProject` directory tree becomes part of the project's tree. It will still be imported as `$import Modules.Debug.lslm` without any additions.

## Native Executable Compilation

If **LSLForge** is behind from the newest `LSL` definitions of functions and constants, you may compile an executable for yourself, by git cloning the source and digging into `lslforge\haskell\src\Language\Lsl\Internal\FuncSigs.hs` and/or `lslforge\haskell\src\Language\Lsl\Internal\Constants.hs` and following the already existing examples. You will need to compile the code now with Haskell compiler and specify newly built file in Eclipse LSLForge Preferences. This should be enough for a while. Continue reading for details.

### Requirements

To compile the native LSLForge binary, you must have the cross-platform Haskell [`Stack`](https://www.haskellstack.org/) tool installed.

* Since LSLForge `0.1.9.7` Stack >= `1.11` is required.

Stack can then ensure that the correct compiler and dependencies for the project will be automatically downloaded and installed for you.

To install Stack, please visit the [Stack Homepage](https://www.haskellstack.org/) and follow the instructions. It is likely that you can find a package available at many package managers e.g. `chocolatey`, `homebrew` and `pacman`, but check before installing that their stack version is up-to-date.

#### Configure Stack if desired:
* `STACK_ROOT` environment variable if you do not want stack files appear under `C:\sr` (Windows).
* To configure downloaded programs location, open `Stack`'s root `config.yaml` and add a line `local-programs-path: <path>` with the path desired, after that commands like `stack ghci` will download files right into that folder
* To configure where `stack install` will place the files, add the following line to `config.yaml`: `local-bin-path: <path>` with the path desired. This folder may be added to the `PATH` environment variable. (The reminder will be given after `stack install` copies the file there).

### Compiling Haskell native LSLForge binary

In a terminal, change directory to the project's `lslforge/haskell` subdirectory.

Run `stack upgrade` to upgrade to the latest version.

You will need to enter `stack setup` if you have a freshly installed Stack, or don't have the relevant compiler already set up. (Stack will tell you if you need to run this additional step.)

Now run with admin terminal `stack install` in `lslforge/haskell` folder to build and install the **LSLForge** binary in one step. (Weird access denied errors are due to Antivirus.)

### Post-compilation

If your `stack install` is successful, an executable will appear at `%APPDATA%\local\bin` folder for Windows, or `$HOME/.local/bin` for other platforms (look at the message after install) - unless you changed the `local-bin-path` parameter in `config.yaml` to override default location.

To test the newly built file, you can specify it in the existing LSLForge Eclipse installation under `Preferences` > `LSLForge` settings. This will not make any new definitions show immediately, but they will after **restarting Eclipse**.

> If you enable Eclipse **Error Log** View, it will print out current version of the executable in use, as well as its actual location.  

## Building for Publishing

### Running Tests

* `cd` to `lslforge/haskell`
* Windows 
    * `set LSLFORGE_TEST_DATA=../testing/data` - enough to be set just for the terminal session.
    * `stack test`
* Linux / macOS on `bash`
    * `LSLFORGE_TEST_DATA=../testing/data stack test`

### Version Number Change

* TODO: Automate this task

* `lslforge\haskell\LSLForge.cabal`
* `lslforge\eclipse\lslforge-feature\feature.xml` (all instances)
* `lslforge\eclipse\update-site\site.xml` (all instances)
* All instances in every `feature.xml` and `MANIFEST.MF` files under `lslforge/eclipse`.

### Compile Haskell Executable & Move to Eclipse Plugin Source Location

Switch to `lslforge` subfolder.

* Windows
    * Run `all_compile_haskell_win32_as_admin.bat`
    * `eclipse\update-site\clean.bat`
* Linux
    * `(cd haskell; stack install --executable-stripping)`
    * `./copy_linux.sh` or `./copy_linux64.sh`
    * `./codegen.sh`
    * `(cd eclipse/update-site; ./clean.sh)`
* macOS
    * ``
    * `(cd haskell; stack install --executable-stripping)`
    * `./copy_mac_x86_64.sh`
    * `./codegen.sh`
    * `(cd eclipse/update-site; ./clean.sh)`

### Eclipse Plug-in Compilation

* Move existing plugin files from `eclipse\` to `eclipse\archive\x.x.x\` (keep `index.html`).

* Eclipse for RCP and RAP Developers - Using `Luna SR2`
    * Use empty workspace
    * Import projects from `lslforge\eclipse\` (without copying)
    * If you see artifacts in the `update-site`'s project folder (`features`, `plugins`, `artifacts.jar`, `content.jar`), run `clean.bat` or `clean.sh` to delete them.
    * Double-click `update-site\site.xml` and select `Build All`
        * Generated files are:
            * `features`
            * `plugins`
            * `artifacts.jar`
            * `content.jar`
        * Move generated files to `eclipse\`
        
        * Copy `index.html` (static file) to `eclipse\` if it is missing.

* Test generated update site with a fresh installation of Eclipse using Local path for plugin:
    * If you don't want fresh Eclipse, uninstall existing LSLForge plug-in and carefully remove every folder from the Eclipse cache.
        > Run `eclipse -clean` to force it to forget cached downloads.
    * Start with a fresh workspace.
    * Help > Install New Software
        * **Add..** update site, providing Local location of `eclipse` folder as a source.
        * Install newly made LSLForge plugin.
    * Restart Eclipse.
    * Switch Perspective to LSLForge.
        * Create new LSLForge Project.
        * Create `*.lslp` files and see **Error Log** View.
