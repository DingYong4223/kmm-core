package com.fit.kmm.kredux

interface StoreSubscriber<StoreSubscriberStateType> {
    fun newState(state: StoreSubscriberStateType)
}