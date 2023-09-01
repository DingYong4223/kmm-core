package com.fit.kmm.kredux

import kotlin.test.Test
import kotlin.test.assertEquals

internal val firstMiddleware: Middleware<StateType> = { dispatch, getState ->
    { next ->
        { action ->
            (action as? SetValueStringAction)?.let {
                it.value += " First Middleware"
                next(action)
            } ?: next(action)
        }
    }
}

internal val secondMiddleware: Middleware<StateType> = { dispatch, getState ->
    { next ->
        { action ->
            (action as? SetValueStringAction)?.let {
                it.value += " Second Middleware"
                next(action)
            } ?: next(action)
        }
    }
}

internal val dispatchingMiddleware: Middleware<StateType> = { dispatch, getState ->
    { next ->
        { action ->
            (action as? SetValueAction)?.let {
                dispatch(SetValueStringAction("${it.value ?: 0}"))
            }

            next(action)
        }
    }
}

internal val stateAccessingMiddleware: Middleware<TestStringAppState> = { dispatch, getState ->
    { next ->
        { action ->

            val appState = getState()
            val stringAction = action as? SetValueStringAction

            // avoid endless recursion by checking if we've dispatched exactly this action
            if (appState?.testValue == "OK" && stringAction?.value != "Not OK") {
                // dispatch a new action
                dispatch(SetValueStringAction("Not OK"))

                // and swallow the current one
                dispatch(NoOpAction())
            } else {
                next(action)
            }
        }
    }
}

internal class StoreMiddlewareTests {

    /**
     * it can decorate dispatch function
     */
    @Test
    fun testDecorateDispatch() {

        val reducer = TestValueStringReducer()
        val store = Store(
            reducer = reducer::handleAction,
            state = TestStringAppState()
        ).apply {
            middleware = listOf(firstMiddleware, secondMiddleware)
        }

        val subscriber = TestStoreSubscriber<TestStringAppState>()
        store.subscribe(subscriber)

        val action = SetValueStringAction("OK")
        store.dispatch(action)

        assertEquals("OK First Middleware Second Middleware", store.state.testValue)
    }

    /**
     * it can dispatch actions
     */
    @Test
    fun testCanDispatch() {
        val reducer = TestValueStringReducer()
        val store = Store(
            reducer = reducer::handleAction,
            state = TestStringAppState()
        ).apply {
            middleware = listOf(firstMiddleware, secondMiddleware, dispatchingMiddleware)
        }

        val subscriber = TestStoreSubscriber<TestStringAppState>()
        store.subscribe(subscriber)

        val action = SetValueAction(10)
        store.dispatch(action)

        assertEquals("10 First Middleware Second Middleware", store.state.testValue)
    }

    /**
     * it middleware can access the store's state
     */
    @Test
    fun testMiddlewareCanAccessState() {
        val reducer = TestValueStringReducer()
        var state = TestStringAppState()
        state = state.copy(testValue = "OK")

        val store = Store(
            reducer = reducer::handleAction,
            state = state
        ).apply {
            middleware = listOf(stateAccessingMiddleware)
        }

        store.dispatch(SetValueStringAction("Action That Won't Go Through"))
        assertEquals("Not OK", store.state.testValue)
    }
}
