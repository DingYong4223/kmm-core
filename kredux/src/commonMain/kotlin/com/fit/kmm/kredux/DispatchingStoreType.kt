package com.fit.kmm.kredux

/**
 * Defines the interface of a dispatching, stateless Store in ReKotlin. `StoreType` is
 * the default usage of this interface. Can be used for store variables where you don't
 * care about the state, but want to be able to dispatch actions.
 */
interface DispatchingStoreType {

    /**
     * Dispatches an action. This is the simplest way to modify the stores state.
     *
     * Example of dispatching an action:
     * <pre>
     * <code>
     * store.dispatch( CounterAction.IncreaseCounter )
     * </code>
     * </pre>
     *
     * @param action The action that is being dispatched to the store
     * @return By default returns the dispatched action, but middlewares can change the type, e.g. to return promises
     */
    fun dispatch(action: Action)
}