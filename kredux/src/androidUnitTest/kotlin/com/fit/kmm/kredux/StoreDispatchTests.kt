package com.fit.kmm.kredux

import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

internal typealias TestSubscriber = TestStoreSubscriber<TestAppState>

internal typealias CallbackSubscriber = CallbackStoreSubscriber<TestAppState>

internal class StoreDispatchTests {

    var reducer = TestReducer()
    var store = Store(reducer::handleAction, TestAppState())

    @BeforeTest
    fun setUp() {
    }

    @AfterTest
    fun tearDown() {
    }

    /**
     * it throws an exception when a reducer dispatches an action
     */
    @Test
    fun testThrowsExceptionWhenReducersDispatch() {
        // TODO: testThrowsExceptionWhenReducersDispatch
    }

    /**
     * it accepts action creators
     */
    @Test
    fun testAcceptsActionCreators() {
        store.dispatch(SetValueAction(5))

        val doubleValueActionCreator: ActionCreator<TestAppState, StoreType<TestAppState>> = { state, store ->
            SetValueAction(state.testValue!! * 2)
        }

        store.dispatch(doubleValueActionCreator)
        assertEquals(10, store.state.testValue)
    }

    /**
     * it accepts async action creators
     */
    @Test
    fun testAcceptsAsyncActionCreators() {
        val awaitEntity = CountDownLatch(1)
        val asyncActionCreator: AsyncActionCreator<TestAppState, StoreType<TestAppState>> = { _, _, callback ->
            thread {
                callback { _, _ ->
                    SetValueAction(5)
                }
            }
        }
        val subscriber = CallbackStoreSubscriber<TestAppState> { state ->
            this.store.state.testValue?.let {
                assertEquals(5, it)
                awaitEntity.countDown()
            }
        }
        store.subscribe(subscriber)
        store.dispatch(asyncActionCreator)
        assertTrue(awaitEntity.await(1, TimeUnit.SECONDS))
    }

    /**
     * it calls the callback once state update from async action is complete
     */
    @Test
    fun testCallsCallbackOnce() {
        val awaitEntity = CountDownLatch(1)
        val asyncActionCreator: AsyncActionCreator<TestAppState, StoreType<TestAppState>> = { _, _, callback ->
            thread { // Provide the callback with an action creator
                callback { _, _ ->
                    SetValueAction(5)
                }
            }
        }
        store.dispatch(asyncActionCreator) { newState ->
            assertEquals(5, this.store.state.testValue)
            if (newState.testValue == 5) {
                awaitEntity.countDown()
            }
        }
        assertTrue(awaitEntity.await(1, TimeUnit.SECONDS))
    }
}
