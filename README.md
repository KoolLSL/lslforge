# LSLForge

LSLForge is a plugin for the Eclipse IDE. The plugin allows editing, compiling and, optionaly, executing and unit testing offline your Second Life® Linden Scripting Language (LSL) code.

Compared to simple editors which mainly do syntax check (Visual Studio Code, Sublime Text, LSL Editor...), it offers useful features for large projects made of multiple scripts: modules import (to include your frequent code library), source code control (Git, CVS, Subversion...), task list, bug tracking, file history & difference, etc. You may need to install appropriate plugins to use certain features.

The aim is to integrate the latest LSL definitions for functions, constants and events LSL according to the recent [Release Notes](https://releasenotes.secondlife.com/categories/simulator.html)

## Changelog

* **0.1.9.12**
    * Added functions and constants till Simulator Release 2024-02-21.7995320426 (llGetNotecardLineSync, llComputeHash, llRezObjectWithParams...) thanks to [@ChloeConstantine](https://github.com/ChloeConstantine)
    * Renamed some folders (lslforge to lslforge-dev, lslforge/eclipse to lslforge-dev/eclipse-project)
    * Updated all_compile_haskell_win32.bat
    * Compiled only for win32 (not mac, linux)
     
* **0.1.9.11**
    * Added constants: CLICK_ACTION_IGNORE, PRIM_SCULPT_TYPE_MESH, PRIM_SCULPT_FLAG_ANIMESH, SIM_STAT_* 
    * Added functions: llLinksetDataDeleteFound, llLinksetDataCountFound, llList2ListSlice, llListSortStrided, llListFindStrided

* **0.1.9.10**
    * Still under developement. Trying to add the lastest LSL functions. Tested only in Windows.
    * Cloned from https://github.com/raysilent/lslforge 0.1.9.9 the 16/01/2023.
    * Adding updates made by [@ChloeConstantine](https://github.com/ChloeConstantine). 
        
* **Previous versions**
    * See https://github.com/raysilent/lslforge and https://github.com/elnewfie/lslforge

## How to Install the Eclipse Plugin

1. The following [Eclipse Distributions](https://www.eclipse.org/downloads/packages/release) were found working:
   * Eclipse IDE 2024-03 (4.31.0) since 0.1.9.12, with Java 17 JRE
   > NOTE: Oomph seems to restore LSLForge native setting despite attempts to overwrite the field. The only workaround for now is to check `[X] Skip automatic task execution at startup time` under Oomph  Setup Tasks in Preferences. 


2. To install a plugin into Eclipse, choose `Help` > `Install New Software`. Click `Add...` and enter the link for location:

	For the tested and reported to be working releases use `KoolLSL`'s __main__ repo:
    * `https://raw.githubusercontent.com/koollsl/lslforge/main/lslforge-dev/eclipse-project/update-site/`
   
	Alternatively you may try a specific version (including work in progress branches). Example:
    * `https://raw.githubusercontent.com/koollsl/lslforge/0.1.9.12/lslforge-dev/eclipse-project/update-site/`
    

	* For even older version, clone the whole repo and link your Eclipse to a particular folder under `eclipse\archive`.

	> If you don't see any items for installing, try to uncheck "Group items by category"
 
   > Run `eclipse -clean` to force it to forget cached downloads

3. Select two items:

	* "LSLForge"
	
	* One of the native parts according to your environment, ex: "lslforge_part_win32_x86" for Windows

4. Install, accept and restart Eclipse

5. Switch to **LSLForge Perspective** and create a new LSLForge Project

## Known Issues

* `*.lslp` files compilation issues although everything is correct. It may happen when a lot of `$import` keywords are used and at some point the compiler gets stuck. What may help is:
    * Adding a fake `*.lslm` module along the project, it could be called `Fake.lslm`. Opening it and adding a space, then removing it and hitting **Save** will force the project to be recompiled
    * Forcing recompilation of a module that is referenced by `*.lslp` file by opening it, doing some fake change, and hitting **Save**
* The official Second Life® Group for LSLForge Editor tool is [LSLForge Users](secondlife:///app/group/381ff28c-1171-27ac-77f5-ded3471b6245/about). General announcements, questions and answers.
* In case of bugs please report right to the https://github.com/KoolLSL/lslforge/ repo.

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

### Other Interesting Plugins to install

* Antenna plugin to use directives like #ifdef #endif in your LSL code: https://antenna.sourceforge.io/update

* EditBox plugin for a better block visualization: http://editbox.sourceforge.net/updates

* Highlight currently selected word: https://marketplace.eclipse.org/content/highlight-selection

  
  
## Native Executable Compilation

If **LSLForge** is behind from the newest `LSL` definitions of functions and constants, you may compile an executable for yourself, by git cloning the source and digging into `lslforge-dev\haskell\src\Language\Lsl\Internal\FuncSigs.hs`, `lslforge-dev\haskell\src\Language\Lsl\Internal\Constants.hs` and/or `lslforge-dev\haskell\src\Language\Lsl\Internal\EventSigs.hs` and following the already existing examples. You will need to compile the code now with Haskell compiler and specify newly built file in Eclipse LSLForge Preferences. This should be enough for a while. Continue reading for details.

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

In a terminal, change directory to the project's `lslforge-dev/haskell` subdirectory.

Run `stack upgrade` to upgrade to the latest version.

You will need to enter `stack setup` if you have a freshly installed Stack, or don't have the relevant compiler already set up. (Stack will tell you if you need to run this additional step.)

Now run with admin terminal `stack install` in `lslforge-dev/haskell` folder to build and install the **LSLForge** binary in one step. (Weird access denied errors are due to Antivirus.)

### Post-compilation

If your `stack install` is successful, an executable will appear at `%APPDATA%\local\bin` folder for Windows, or `$HOME/.local/bin` for other platforms (look at the message after install) - unless you changed the `local-bin-path` parameter in `config.yaml` to override default location.

To test the newly built file, you can specify it in the existing LSLForge Eclipse installation under `Preferences` > `LSLForge` settings. This will not make any new definitions show immediately, but they will after **restarting Eclipse**.

> If you enable Eclipse **Error Log** View, it will print out current version of the executable in use, as well as its actual location.  

## Building for Publishing

### Running Tests

* `cd` to `lslforge-dev/haskell`
* Windows 
    * `set LSLFORGE_TEST_DATA=../testing/data` - enough to be set just for the terminal session.
    * `stack test`
* Linux / macOS on `bash`
    * `LSLFORGE_TEST_DATA=../testing/data stack test`

### Version Number Change

* TODO: Automate this task

* `lslforge-dev\haskell\LSLForge.cabal`
* `lslforge-dev\eclipse-project\lslforge-feature\feature.xml` (all instances)
* `lslforge-dev\eclipse-project\update-site\site.xml` (all instances)
* All instances in every `feature.xml` and `MANIFEST.MF` files under `lslforge-dev/eclipse-project`.

### Compile Haskell Executable & Move to Eclipse Plugin Source Location

Switch to `lslforge-dev` subfolder.

* Windows
    * Run `all_compile_haskell_win32_as_admin.bat`
    * `eclipse-project\update-site\clean.bat`
* Linux
    * `(cd haskell; stack install --executable-stripping)`
    * `./copy_linux.sh` or `./copy_linux64.sh`
    * `./codegen.sh`
    * `(cd eclipse-project/update-site; ./clean.sh)`
* macOS
    * ``
    * `(cd haskell; stack install --executable-stripping)`
    * `./copy_mac_x86_64.sh`
    * `./codegen.sh`
    * `(cd eclipse-project/update-site; ./clean.sh)`

### Eclipse Plug-in Compilation

* Move existing plugin files from `eclipse\` to `eclipse\archive\x.x.x\` (keep `index.html`).

* Eclipse for RCP and RAP Developers (using version 2024-03)
    * Use empty workspace
    * Import > Projects from Folder or Archive `lslforge-dev\eclipse-project\` (without copying)
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
