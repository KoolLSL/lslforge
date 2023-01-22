@echo off
echo Started %date% %time%
echo ---- Compiling Haskell...
echo.
cd haskell
rem remove Stack temp directory
rmdir ".stack-work" /S /Q

rem --copy-bins: copy resulting LSLForge.exe to local binary directory %UserProfile%\AppData\Roaming\local\bin\ (see stack path --local-bin)
rem --executable-stripping: executable stripping for TARGETs and all its dependencies.
rem --force-dirty: force rebuild of packages even when it doesn't seem necessary based on file dirtiness.
rem --reconfigure: force reconfiguration even when it doesn't seem necessary based on file dirtiness.
stack install --executable-stripping --reconfigure --copy-bins
@echo off
cd ..

echo.
echo ---- Generating Java Code...
echo.
pushd "%~dp0"
echo on
del eclipse\lslforge\src\lslforge\generated\*.java
%UserProfile%\AppData\Roaming\local\bin\LSLForge.exe _CodeGen_ eclipse\lslforge\src\lslforge\generated lslforge.generated
@echo off
popd

echo.
echo ---- Cleaning Eclipse Update-Site Directory...
echo.
call eclipse\update-site\clean.bat
popd

pause
