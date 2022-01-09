#!/bin/sh
rm -v eclipse/lslforge/src/lslforge/generated/*.java

~/.local/bin/LSLForge _CodeGen_ eclipse/lslforge/src/lslforge/generated lslforge.generated
