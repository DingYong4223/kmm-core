package com.fit.kmm.kredux

typealias Reducer<ReducerStateType> = (action: Action, state: ReducerStateType?) -> ReducerStateType