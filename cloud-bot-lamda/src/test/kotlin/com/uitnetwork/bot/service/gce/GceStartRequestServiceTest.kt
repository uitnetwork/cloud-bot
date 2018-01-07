package com.uitnetwork.bot.service.gce

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.service.PermissionService
import com.uitnetwork.bot.service.gce.GceStartRequestService.Companion.ACTION_GCE_START
import com.uitnetwork.bot.service.gce.GceStartRequestService.Companion.PARAM_GCE_NAME
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GceStartRequestServiceTest {
    companion object {
        private val TEST_GCE_OVERVIEW = "TEST_GCE_OVERVIEW"

        private val TEST_GCE_NAME = "TEST_GCE_NAME"
    }

    @Mock
    private lateinit var gceService: GceService

    @Mock
    private lateinit var permissionService: PermissionService

    @Mock
    private lateinit var fulfillmentRequest: FulfillmentRequest

    @InjectMocks
    private lateinit var gceStartRequestService: GceStartRequestService

    @Test
    fun shouldReturnGceStartAction() {
        assertThat(gceStartRequestService.getProcessableActionName()).isEqualTo(ACTION_GCE_START)
    }

    @Test
    fun shouldStopTheGceInstanceByName() {
        whenever(fulfillmentRequest.params).thenReturn(mapOf(
                PARAM_GCE_NAME to TEST_GCE_NAME
        ))

        val fulfillmentResponse = gceStartRequestService.doProcess(fulfillmentRequest)

        assertThat(fulfillmentResponse.speech).isEqualTo("Starting GCE: $TEST_GCE_NAME")
        assertThat(fulfillmentResponse.displayText).isEqualTo("Starting GCE: $TEST_GCE_NAME")
        verify(gceService).startGceInstance(TEST_GCE_NAME)
        verifyNoMoreInteractions(gceService)
    }

    @Test
    fun shouldNotDoAnythingIfThereIsNoGceName() {
        whenever(fulfillmentRequest.params).thenReturn(mapOf(
                PARAM_GCE_NAME to ""
        ))

        val fulfillmentResponse = gceStartRequestService.doProcess(fulfillmentRequest)

        assertThat(fulfillmentResponse.speech).isEqualTo("Please specify the name of the GCE instance")
        assertThat(fulfillmentResponse.displayText).isEqualTo("Please specify the name of the GCE instance")
        verifyNoMoreInteractions(gceService)
    }
}
