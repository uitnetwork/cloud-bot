package com.uitnetwork.bot.service

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import com.uitnetwork.bot.model.Source.SLACK
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.test.util.ReflectionTestUtils

@RunWith(MockitoJUnitRunner::class)
class AbstractRequestServiceTest {
    companion object {
        private const val TEST_USER_ID = "TEST_USER_ID"
        private val TEST_SOURCE = SLACK
        private const val TEST_ACTION = "TEST_ACTION"
    }

    @Mock
    private lateinit var permissionService: PermissionService

    @Mock
    private lateinit var abstractRequestService: AbstractRequestService

    @Mock
    private lateinit var fulfillmentRequest: FulfillmentRequest

    @Mock
    private lateinit var fulfillmentResponse: FulfillmentResponse

    @Before
    fun setup() {
        ReflectionTestUtils.setField(abstractRequestService, "permissionService", permissionService)
        whenever(abstractRequestService.validatePermissionThenProcess(any())).thenCallRealMethod()
        whenever(fulfillmentRequest.userId).thenReturn(TEST_USER_ID)
        whenever(fulfillmentRequest.source).thenReturn(TEST_SOURCE)
        whenever(fulfillmentRequest.action).thenReturn(TEST_ACTION)
    }

    @Test
    fun shouldDoProcessIfHasPermission() {
        whenever(permissionService.hasPermissionToExecute(TEST_USER_ID, TEST_SOURCE, TEST_ACTION)).thenReturn(true)
        whenever(abstractRequestService.doProcess(fulfillmentRequest)).thenReturn(fulfillmentResponse)

        val returnFulfillmentResponse = abstractRequestService.validatePermissionThenProcess(fulfillmentRequest)

        assertThat(returnFulfillmentResponse === fulfillmentResponse).isTrue()
        verify(permissionService).hasPermissionToExecute(TEST_USER_ID, TEST_SOURCE, TEST_ACTION)
        verify(abstractRequestService).validatePermissionThenProcess(fulfillmentRequest)
        verify(abstractRequestService).doProcess(fulfillmentRequest)
        verifyNoMoreInteractions(permissionService, abstractRequestService)
    }

    @Test
    fun shouldDoNothingIfHasNoPermission() {
        whenever(permissionService.hasPermissionToExecute(TEST_USER_ID, TEST_SOURCE, TEST_ACTION)).thenReturn(false)

        val returnFulfillmentResponse = abstractRequestService.validatePermissionThenProcess(fulfillmentRequest)

        assertThat(returnFulfillmentResponse.speech).isEqualTo("Sorry. You don't have permission")
        assertThat(returnFulfillmentResponse.displayText).isEqualTo("Sorry. You don't have permission")
        verify(abstractRequestService).validatePermissionThenProcess(fulfillmentRequest)
        verify(permissionService).hasPermissionToExecute(TEST_USER_ID, TEST_SOURCE, TEST_ACTION)
        verifyNoMoreInteractions(permissionService, abstractRequestService)
    }
}
