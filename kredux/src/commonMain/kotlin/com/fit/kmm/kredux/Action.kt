package com.fit.kmm.kredux

/**
 * All actions that want to be able to be dispatched to a store need to conform to this protocol
 * Currently it is just a marker protocol with no requirements.
 */
open class Action(val type: String)

/**
 * Initial Action that is dispatched as soon as the store is created.
 * Reducers respond to this action by configuring their initial state.
 */
class ReKotlinInit : Action(type = "init")