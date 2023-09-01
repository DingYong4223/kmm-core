package com.fit.kmm.kredux

import kotlin.test.Test
import kotlin.test.assertEquals

internal class StoreSubscriberTests {

    /**
     * it allows to pass a state selector closure
     */
    @Test
    fun testAllowsSelectorClosure() {
        val reducer = TestReducer()
        val store = Store(reducer = reducer::handleAction, state = TestAppState())
        val subscriber = TestFilteredSubscriber<Int?>()

        store.subscribe(subscriber) {
            it.select { it.testValue }
        }

        store.dispatch(SetValueAction(3))

        assertEquals(3, subscriber.recievedValue)

        store.dispatch(SetValueAction(null))

        assertEquals(null, subscriber.recievedValue)
    }

    /**
     * it supports complex state selector closures
     */
    @Test
    fun testComplexStateSelector() {
        val reducer = TestComplexAppStateReducer()
        val store = Store(reducer = reducer::handleAction, state = TestComplexAppState())
        val subscriber = TestSelectiveSubscriber()

        store.subscribe(subscriber) {
            it.select { Pair(it.testValue, it.otherState?.name) }
        }
        store.dispatch(SetValueAction(5))
        store.dispatch(SetOtherStateAction(OtherState("TestName", 99)))

        assertEquals(5, subscriber.recievedValue.first)
        assertEquals("TestName", subscriber.recievedValue.second)
    }

    /**
     * it does not notify subscriber for unchanged substate state when using `skipRepeats`.
     */
    @Test
    fun testUnchangedStateSelector() {
        val reducer = TestReducer()
        val state = TestAppState(3)
        val store = Store(reducer = reducer::handleAction, state = state)
        val subscriber = TestFilteredSubscriber<Int?>()

        store.subscribe(subscriber) {
            it.select {
                it.testValue
            }.skipRepeats { oldState, newState ->
                oldState == newState
            }
        }

        assertEquals(3, subscriber.recievedValue)

        store.dispatch(SetValueAction(3))

        assertEquals(3, subscriber.recievedValue)
        assertEquals(1, subscriber.newStateCallCount)
    }

    /**
     * it does not notify subscriber for unchanged substate state when using the default
     * `skipRepeats` implementation.
     */
    @Test
    fun testUnchangedStateSelectorDefaultSkipRepeats() {
        val reducer = TestValueStringReducer()
        val state = TestStringAppState()
        val store = Store(reducer::handleAction, state)
        val subscriber = TestFilteredSubscriber<String>()

        store.subscribe(subscriber) {
            it.select { it.testValue }.skipRepeats()
        }

        assertEquals("Initial", subscriber.recievedValue)

        store.dispatch(SetValueStringAction("Initial"))

        assertEquals("Initial", subscriber.recievedValue)
        assertEquals(1, subscriber.newStateCallCount)
    }

    /**
     * it skips repeated state values by when `skipRepeats` returns `true`.
     */
    @Test
    fun testSkipsStateUpdatesForCustomEqualityChecks() {
        val reducer = TestCustomAppStateReducer()
        val state = TestCustomAppState(5)
        val store = Store(reducer::handleAction, state)
        val subscriber = TestFilteredSubscriber<TestCustomSubstate>()

        store.subscribe(subscriber) {
            it.select { it.substate }
                .skipRepeats { oldState, newState -> oldState.value == newState.value }
        }

        assertEquals(5, subscriber.recievedValue?.value)

        store.dispatch(SetCustomSubstateAction(5))

        assertEquals(5, subscriber.recievedValue?.value)
        assertEquals(1, subscriber.newStateCallCount)
    }

    @Test
    fun testPassesOnDuplicateSubstateUpdatesByDefault() {
        val reducer = TestValueStringReducer()
        val state = TestStringAppState()
        val store = Store(reducer::handleAction, state)
        val subscriber = TestFilteredSubscriber<String>()

        store.subscribe(subscriber) {
            it.select { it.testValue }
        }

        assertEquals("Initial", subscriber.recievedValue)

        store.dispatch(SetValueStringAction("Initial"))

        assertEquals("Initial", subscriber.recievedValue)
        assertEquals(2, subscriber.newStateCallCount)
    }

    @Test
    fun testSkipsStateUpdatesForEquatableStateByDefault() {
        val reducer = TestValueStringReducer()
        val state = TestStringAppState()
        val store = Store(reducer::handleAction, state)
        val subscriber = TestFilteredSubscriber<TestStringAppState>()

        store.subscribe(subscriber)

        assertEquals("Initial", subscriber.recievedValue?.testValue)

        store.dispatch(SetValueStringAction("Initial"))

        assertEquals("Initial", subscriber.recievedValue?.testValue)
        assertEquals(1, subscriber.newStateCallCount)
    }

    @Test
    fun testPassesOnDuplicateStateUpdatesInCustomizedStore() {
        val reducer = TestValueStringReducer()
        val state = TestStringAppState()
        val store = Store(reducer::handleAction, state).apply {
            automaticallySkipRepeats = false
        }
        val subscriber = TestFilteredSubscriber<TestStringAppState>()

        store.subscribe(subscriber)

        assertEquals("Initial", subscriber.recievedValue?.testValue)

        store.dispatch(SetValueStringAction("Initial"))

        assertEquals("Initial", subscriber.recievedValue?.testValue)
        assertEquals(2, subscriber.newStateCallCount)
    }

    @Test
    fun testSkipWhen() {
        val reducer = TestCustomAppStateReducer()
        val state = TestCustomAppState(5)
        val store = Store(reducer::handleAction, state)
        val subscriber = TestFilteredSubscriber<TestCustomSubstate>()

        store.subscribe(subscriber) {
            it.select { it.substate }
                .skip { oldState, newState -> oldState.value == newState.value }
        }

        assertEquals(5, subscriber.recievedValue?.value)

        store.dispatch(SetCustomSubstateAction(5))

        assertEquals(5, subscriber.recievedValue?.value)
        assertEquals(1, subscriber.newStateCallCount)
    }

    @Test
    fun testOnlyWhen() {
        val reducer = TestCustomAppStateReducer()
        val state = TestCustomAppState(5)
        val store = Store(reducer::handleAction, state)
        val subscriber = TestFilteredSubscriber<TestCustomSubstate>()

        store.subscribe(subscriber) {
            it.select { it.substate }
                .only { oldState, newState -> oldState.value != newState.value }
        }

        assertEquals(5, subscriber.recievedValue?.value)

        store.dispatch(SetCustomSubstateAction(5))

        assertEquals(5, subscriber.recievedValue?.value)
        assertEquals(1, subscriber.newStateCallCount)
    }
}

internal class TestFilteredSubscriber<T> : StoreSubscriber<T> {
    var recievedValue: T? = null
    var newStateCallCount = 0

    override fun newState(state: T) {
        recievedValue = state
        newStateCallCount += 1
    }
}

/**
 * Example of how you can select a substate. The return value from
 *`selectSubstate` and the argument for `newState` need to match up.
 */
class TestSelectiveSubscriber : StoreSubscriber<Pair<Int?, String?>> {
    var recievedValue: Pair<Int?, String?> = Pair(null, null)

    override fun newState(state: Pair<Int?, String?>) {
        recievedValue = state
    }
}

internal data class TestComplexAppState(val testValue: Int?, val otherState: OtherState?) :
    StateType {
    constructor() : this(null, null)
}

internal data class OtherState(val name: String?, val age: Int?)

internal class TestComplexAppStateReducer {
    fun handleAction(action: Action, state: TestComplexAppState?): TestComplexAppState {
        var state = state ?: TestComplexAppState()

        when (action) {
            is SetValueAction -> {
                state = state.copy(testValue = action.value)
            }
            is SetOtherStateAction -> {
                state = state.copy(otherState = action.otherState)
            }
        }

        return state
    }
}

internal data class SetOtherStateAction(val otherState: OtherState) : Action(type = "SetOtherStateAction")