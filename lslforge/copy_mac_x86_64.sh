#!/bin/sh
(cd haskell;stack install)
strip ~/.local/bin/LSLForge
cp ~/.local/bin/LSLForge eclipse/lslforge-macosx-x86_64/os/macosx/x86_64
