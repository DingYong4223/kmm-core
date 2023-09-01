package com.fit.kmm.kredux

internal data class TestAppState(val testValue: Int? = null) : StateType

internal data class TestStringAppState(val testValue: String = "Initial") : StateType

internal data class TestCustomSubstate(val value: Int) : StateType

internal data class TestBlockState(
    val testAppState: TestAppState? = null,
    val testStringAppState: TestStringAppState? = null
) : StateType

internal data class TestCustomAppState(val substate: TestCustomSubstate) : StateType {
    constructor(substateValue: Int = 0) : this(TestCustomSubstate(substateValue))
}

internal data class NoOpAction(val unit: Unit = Unit) : Action(type = "NoOpAction")
internal data class SetValueAction(val value: Int?) : Action(type = "SetValueAction") {
    companion object {
        val type = "SetValueAction"
    }
}

internal data class SetValueStringAction(var value: String) : Action(type = "SetValueStringAction") {
    companion object {
        val type = "SetValueStringAction"
    }
}

internal data class SetCustomSubstateAction(val value: Int) : Action(type = "SetCustomSubstateAction") {
    companion object {
        val type = "SetCustomSubstateAction"
    }
}

internal class TestReducer {
    fun handleAction(action: Action, state: TestAppState?): TestAppState {
        @Suppress("NAME_SHADOWING")
        var state = state ?: TestAppState()
        when (action) {
            is SetValueAction -> {
                state = state.copy(testValue = action.value)
            }
        }
        return state
    }
}

internal class TestValueStringReducer {
    fun handleAction(action: Action, state: TestStringAppState?): TestStringAppState {
        @Suppress("NAME_SHADOWING")
        var state = state ?: TestStringAppState()

        when (action) {
            is SetValueStringAction -> {
                state = state.copy(testValue = action.value)
            }
        }

        return state
    }
}

internal class TestCustomAppStateReducer {
    fun handleAction(action: Action, state: TestCustomAppState?): TestCustomAppState {
        @Suppress("NAME_SHADOWING")
        var state = state ?: TestCustomAppState()

        when (action) {
            is SetCustomSubstateAction -> {
                state = state.copy(substate = state.substate.copy(action.value))
            }
        }

        return state
    }
}

internal fun blockStateReducer(action: Action, testBlockState: TestBlockState?): TestBlockState =
    TestBlockState(
        testAppState = TestReducer().handleAction(action, testBlockState?.testAppState),
        testStringAppState = TestValueStringReducer().handleAction(action, testBlockState?.testStringAppState)
    )

internal class TestStoreSubscriber<T> : StoreSubscriber<T> {
    var recievedStates: MutableList<T> = mutableListOf()

    override fun newState(state: T) {
        this.recievedStates.add(state)
    }
}

/**
 * A subscriber contains another sub-subscribers [StoreSubscriber], which could be subscribe/unsubscribe at some point
 */
internal class ViewSubscriberTypeA(var store: Store<TestStringAppState>) :
    StoreSubscriber<TestStringAppState> {
    private val viewB by lazy { ViewSubscriberTypeB(store) }
    private val viewC by lazy { ViewSubscriberTypeC() }

    override fun newState(state: TestStringAppState) {
        when (state.testValue) {
            "subscribe" -> store.subscribe(viewC)
            "unsubscribe" -> store.unsubscribe(viewB)
            else -> println(state.testValue)
        }
    }
}

internal class ViewSubscriberTypeB(store: Store<TestStringAppState>) :
    StoreSubscriber<TestStringAppState> {
    init {
        store.subscribe(this)
    }

    override fun newState(state: TestStringAppState) {
        // do nothing
    }
}

internal class ViewSubscriberTypeC : StoreSubscriber<TestStringAppState> {
    override fun newState(state: TestStringAppState) {
        // do nothing
    }
}

internal class DispatchingSubscriber(var store: Store<TestAppState>) :
    StoreSubscriber<TestAppState> {

    override fun newState(state: TestAppState) {
        // Test if we've already dispatched this action to
        // avoid endless recursion
        if (state.testValue != 5) {
            this.store.dispatch(SetValueAction(5))
        }
    }
}

internal class CallbackStoreSubscriber<T>(val handler: (T) -> Unit) : StoreSubscriber<T> {
    override fun newState(state: T) {
        handler(state)
    }
}
