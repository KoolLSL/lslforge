@echo off
pushd "%~dp0"

echo.
echo ---- Compiling Haskell...
echo.
cd haskell
stack install --executable-stripping

cd ..

echo.
echo ---- Generating Java Code...
echo.
call codegen.bat

echo.
echo ---- Copying Haskell Executable...
echo.
call copy_win32.bat

echo.
echo ---- Cleaning Eclipse Update-Site Directory...
echo.
call eclipse\update-site\clean.bat

rem timeout /t 60
popd