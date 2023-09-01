package com.fit.kmm.thread

import kotlinx.coroutines.CoroutineScope

/**
 * dispatcher for time cost task, eg: network request, io operation etc.
 * */
expect val ioScope: CoroutineScope

/**
 * dispatcher for ui task, eg: UI show etc.
 * */
expect val uiScope: CoroutineScope
