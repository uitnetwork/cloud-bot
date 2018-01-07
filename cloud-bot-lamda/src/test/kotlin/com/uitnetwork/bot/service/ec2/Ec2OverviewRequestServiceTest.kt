package com.uitnetwork.bot.service.ec2

import com.nhaarman.mockitokotlin2.whenever
import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.service.PermissionService
import com.uitnetwork.bot.service.ec2.Ec2OverviewRequestService.Companion.ACTION_EC2_OVERVIEW
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class Ec2OverviewRequestServiceTest {
    companion object {
        private val TEST_EC2_OVERVIEW = "TEST_EC2_OVERVIEW"
    }

    @Mock
    private lateinit var ec2Service: Ec2Service

    @Mock
    private lateinit var permissionService: PermissionService

    @Mock
    private lateinit var fulfillmentRequest: FulfillmentRequest

    @InjectMocks
    private lateinit var ec2OverviewRequestService: Ec2OverviewRequestService

    @Test
    fun shouldReturnEc2OverviewAction() {
        assertThat(ec2OverviewRequestService.getProcessableActionName()).isEqualTo(ACTION_EC2_OVERVIEW)
    }

    @Test
    fun shouldReturnEc2Overview() {
        whenever(ec2Service.getEc2Overview()).thenReturn(TEST_EC2_OVERVIEW)

        val fulfillmentResponse = ec2OverviewRequestService.doProcess(fulfillmentRequest)

        assertThat(fulfillmentResponse.speech).isEqualTo(TEST_EC2_OVERVIEW)
        assertThat(fulfillmentResponse.displayText).isEqualTo(TEST_EC2_OVERVIEW)
    }

}
