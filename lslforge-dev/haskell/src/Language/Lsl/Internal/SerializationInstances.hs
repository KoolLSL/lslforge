{-# LANGUAGE TemplateHaskell #-}
{-# LANGUAGE ScopedTypeVariables #-}module Language.Lsl.Internal.SerializationInstances where

import Language.Lsl.Internal.SerializationGenerator

$(deriveJavaRepTups [2..10])

$(deriveJavaRep ''Either)
$(deriveJavaRep ''Maybe)
