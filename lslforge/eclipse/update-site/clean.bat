pushd "%~dp0"

@echo off

rmdir  /s /q "features" 2>nul
rmdir /s /q "plugins" 2>nul
del "artifacts.jar" 2>nul
del "content.jar" 2>nul
popd