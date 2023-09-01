package com.fit.kmm.kredux

import kotlin.test.Test

internal class StoreTests {

    /**
     * it dispatches an Init action when it doesn't receive an initial state
     */
    @Test
    fun testInit() {
        val reducer = MockReducer()
        Store(reducer::handleAction, null)

        //assertIs(reducer.calledWithAction[0], ReKotlinInit)
    }

    // testDeinit() is not relevant in JVM
}

internal data class CounterState(var count: Int = 0) : StateType

internal class MockReducer {

    val calledWithAction: MutableList<Action> = mutableListOf()

    fun handleAction(action: Action, state: CounterState?): CounterState {
        calledWithAction.add(action)

        return state ?: CounterState()
    }
}