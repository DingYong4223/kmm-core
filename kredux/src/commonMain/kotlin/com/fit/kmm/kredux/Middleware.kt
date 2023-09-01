package com.fit.kmm.kredux

typealias DispatchFunction = (Action) -> Unit

typealias Middleware<State> = (DispatchFunction, () -> State?) -> (DispatchFunction) -> DispatchFunction