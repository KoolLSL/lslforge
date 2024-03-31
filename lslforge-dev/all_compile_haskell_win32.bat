@echo off
echo Started %date% %time%
echo ---- Clean Haskell directory...
echo.
cd haskell
rem remove Stack temp directory
rmdir ".stack-work" /S /Q

echo ---- Compiling Haskell LSLForge.exe...
rem --copy-bins: copy resulting LSLForge.exe to local binary directory %UserProfile%\AppData\Roaming\local\bin\ (see stack path --local-bin)
rem --executable-stripping: executable stripping for TARGETs and all its dependencies.
rem --force-dirty: force rebuild of packages even when it doesn't seem necessary based on file dirtiness.
rem --reconfigure: force reconfiguration even when it doesn't seem necessary based on file dirtiness.
stack install --executable-stripping --copy-bins
@echo off
cd ..

echo.
echo ---- Generating Java Code...
echo.
pushd "%~dp0"
echo on
del eclipse-project\lslforge\src\lslforge\generated\*.java
%UserProfile%\AppData\Roaming\local\bin\LSLForge.exe _CodeGen_ eclipse-project\lslforge\src\lslforge\generated lslforge.generated
@echo off
popd

echo.
echo ---- Copying Haskell LSLForge.exe to plugin folder
echo.
echo on
xcopy /y %UserProfile%\AppData\Roaming\local\bin\LSLForge.exe eclipse-project\lslforge-win32-x86\os\win32\x86\
@echo off

echo.
echo ---- Cleaning Eclipse Update-Site Directory...
echo.
call eclipse-project\update-site\clean.bat
echo ---- Next step: open eclipse-project in Eclipse IDE for RCP, open update-site/site.xml with Site Manifest Editor and [Build All]
popd

pause