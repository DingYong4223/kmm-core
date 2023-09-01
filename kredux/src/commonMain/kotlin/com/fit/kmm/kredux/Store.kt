package com.fit.kmm.kredux

class Store<State : StateType>(
    private val reducer: Reducer<State?>,
    state: State?
) : StoreType<State> {

    var middleware: List<Middleware<State>> = emptyList()
    var automaticallySkipRepeats: Boolean = true

    private var _state: State? = state
        set(value) {
            val oldValue = field
            field = value
            value?.let {
                subscriptions.forEach {
                    it.newValues(oldValue, value)
                }
            }
        }

    override val state: State
        get() {
            return _state!!
        }

    /**
     * 异步通知View接口
     * */
    fun dispatchState(s: State) {
        _state = s
    }

    @Suppress("NAME_SHADOWING")
    override var dispatchFunction: DispatchFunction = middleware
        .reversed()
        .fold({ action: Action -> this._defaultDispatch(action) }, { dispatchFunction, middleware ->
            val dispatch = { action: Action -> this.dispatch(action) }
            val getState = { this._state }
            middleware(dispatch, getState)(dispatchFunction)
        })

    val subscriptions: MutableList<SubscriptionBox<State, Any>> = ArrayList()

    private var isDispatching = false

    /**
     * Indicates if new subscriptions attempt to apply `skipRepeats` by default.
     */
    val subscribersAutomaticallySkipsRepeat: Boolean = automaticallySkipRepeats

    init {
        this._state?.let { this._state = state } ?: this.dispatch(ReKotlinInit())
    }

    override fun <S : StoreSubscriber<State>> subscribe(subscriber: S) {

        // if subscribersAutomaticallySkipsRepeat is set
        // skipRepeats will be applied with kotlin structural equality
        if (subscribersAutomaticallySkipsRepeat) {
            this.subscribe(subscriber) {
                it.skipRepeats()
            }
        } else {
            this.subscribe(subscriber, null)
        }
    }

    override fun <SelectedState, S : StoreSubscriber<SelectedState>> subscribe(
        subscriber: S,
        transform: ((Subscription<State>) -> Subscription<SelectedState>)?
    ) {
        // If the same subscriber is already registered with the store, replace the existing
        // subscription with the new one.
        val index = this.subscriptions.indexOfFirst { it.subscriber === subscriber }
        if (index != -1) {
            this.subscriptions.removeAt(index)
        }

        // Create a subscription for the new subscriber.
        val originalSubscription = Subscription<State>()
        // Call the optional transformation closure. This allows callers to modify
        // the subscription, e.g. in order to subselect parts of the store's state.
        val transformedSubscription = transform?.invoke(originalSubscription)

        val subscriptionBox = SubscriptionBox(originalSubscription, transformedSubscription, subscriber)

        // each subscriber has its own potentially different SelectedState that doesn't have to conform to StateType
        @Suppress("UNCHECKED_CAST")
        this.subscriptions.add(subscriptionBox as SubscriptionBox<State, Any>)

        this._state?.let {
            originalSubscription.newValues(null, it)
        }
    }

    override fun <SelectedState> unsubscribe(subscriber: StoreSubscriber<SelectedState>) {
        val index = this.subscriptions.indexOfFirst { it.subscriber === subscriber }
        if (index != -1) {
            this.subscriptions.removeAt(index)
        }
    }

    fun unsubscribe(blockSubscriptions: BlockSubscriptions) {
        blockSubscriptions.blockSubscriberList.forEach {
            unsubscribe(it)
        }
    }

    fun _defaultDispatch(action: Action) {
        if (isDispatching) {
            throw Exception(
                "ReKotlin:ConcurrentMutationError- Action has been dispatched while" +
                    " a previous action is action is being processed. A reducer" +
                    " is dispatching an action, or ReKotlin is used in a concurrent context" +
                    " (e.g. from multiple threads)."
            )
        }

        this.isDispatching = true
        val newState = reducer(action, this._state)
        this.isDispatching = false

        if (null != newState) {
            this._state = newState
        }
    }

    override fun dispatch(action: Action) {
        this.dispatchFunction(action)
    }

    override fun dispatch(actionCreator: ActionCreator<State, StoreType<State>>) {
        actionCreator(this.state, this)?.let {
            this.dispatch(it)
        }
    }

    override fun dispatch(asyncActionCreator: AsyncActionCreator<State, StoreType<State>>) {
        this.dispatch(asyncActionCreator, null)
    }

    override fun dispatch(
        asyncActionCreator: AsyncActionCreator<State, StoreType<State>>,
        callback: DispatchCallback<State>?
    ) {
        asyncActionCreator(this.state, this) { actionCreator ->
            val action = actionCreator(this.state, this)
            action?.let {
                this.dispatch(it)
                callback?.invoke(this.state)
            }
        }
    }
}

typealias DispatchCallback<State> = (State) -> Unit

typealias ActionCreator<State, Store> = (state: State, store: Store) -> Action?

typealias AsyncActionCreator<State, Store> = (state: State, store: Store, actionCreatorCallback: (ActionCreator<State, Store>) -> Unit) -> Unit