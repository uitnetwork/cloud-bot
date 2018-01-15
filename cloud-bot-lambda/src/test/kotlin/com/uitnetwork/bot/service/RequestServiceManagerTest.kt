package com.uitnetwork.bot.service

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.verifyZeroInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.model.FulfillmentResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RequestServiceManagerTest {
    companion object {
        private const val TEST_ACTION_NAME = "TEST_ACTION_NAME"
    }

    @Mock
    private lateinit var abstractRequestService: AbstractRequestService

    @Mock
    private lateinit var fulfillmentRequest: FulfillmentRequest

    @Mock
    private lateinit var fulfillmentResponse: FulfillmentResponse

    private lateinit var requestServiceManager: RequestServiceManager

    @Before
    fun setup() {
        whenever(abstractRequestService.getProcessableActionName()).thenReturn(TEST_ACTION_NAME)
        requestServiceManager = RequestServiceManager(listOf(abstractRequestService))
        whenever(abstractRequestService.validatePermissionThenProcess(fulfillmentRequest)).thenReturn(fulfillmentResponse)
    }

    @Test
    fun shouldUseTheServiceWithCorrectNameToProcess() {
        whenever(fulfillmentRequest.action).thenReturn(TEST_ACTION_NAME.toLowerCase())

        val returnedFulfillmentResponse = requestServiceManager.process(fulfillmentRequest)

        assertThat(returnedFulfillmentResponse === fulfillmentResponse).isTrue()
        verify(abstractRequestService).validatePermissionThenProcess(fulfillmentRequest)
    }

    @Test
    fun shouldDoNothingIfThereIsNoServiceMappedToTheAction() {
        whenever(fulfillmentRequest.action).thenReturn("unexpected_action")

        val returnedFulfillmentResponse = requestServiceManager.process(fulfillmentRequest)

        assertThat(returnedFulfillmentResponse.speech).isEqualTo("Sorry. I can not handle the request with action unexpected_action")
        assertThat(returnedFulfillmentResponse.displayText).isEqualTo("Sorry. I can not handle the request with action unexpected_action")
        verify(abstractRequestService).getProcessableActionName()
        verifyNoMoreInteractions(abstractRequestService)
    }
}
