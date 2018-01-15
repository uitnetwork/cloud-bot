package com.uitnetwork.bot.service.gce

import com.nhaarman.mockitokotlin2.whenever
import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.service.PermissionService
import com.uitnetwork.bot.service.gce.GceOverviewRequestService.Companion.ACTION_GCE_OVERVIEW
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class GceOverviewRequestServiceTest {
    companion object {
        private val TEST_GCE_OVERVIEW="TEST_GCE_OVERVIEW"
    }

    @Mock
    private lateinit var gceService: GceService

    @Mock
    private lateinit var permissionService: PermissionService

    @Mock
    private lateinit var fulfillmentRequest: FulfillmentRequest

    @InjectMocks
    private lateinit var gceOverviewRequestService: GceOverviewRequestService

    @Test
    fun shouldReturnGceOverviewAction() {
        assertThat(gceOverviewRequestService.getProcessableActionName()).isEqualTo(ACTION_GCE_OVERVIEW)
    }

    @Test
    fun shouldReturnGceOverview() {
        whenever(gceService.getGceOverview()).thenReturn(TEST_GCE_OVERVIEW)

        val fulfillmentResponse = gceOverviewRequestService.doProcess(fulfillmentRequest)

        assertThat(fulfillmentResponse.speech).isEqualTo(TEST_GCE_OVERVIEW)
        assertThat(fulfillmentResponse.displayText).isEqualTo(TEST_GCE_OVERVIEW)
    }
}
