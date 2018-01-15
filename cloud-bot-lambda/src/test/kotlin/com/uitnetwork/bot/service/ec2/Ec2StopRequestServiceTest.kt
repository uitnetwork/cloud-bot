package com.uitnetwork.bot.service.ec2

import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.uitnetwork.bot.model.FulfillmentRequest
import com.uitnetwork.bot.service.PermissionService
import com.uitnetwork.bot.service.ec2.Ec2StartRequestService.Companion.ACTION_EC2_START
import com.uitnetwork.bot.service.ec2.Ec2StartRequestService.Companion.PARAM_EC2_ID
import com.uitnetwork.bot.service.ec2.Ec2StartRequestService.Companion.PARAM_EC2_NAME
import com.uitnetwork.bot.service.ec2.Ec2StopRequestService.Companion.ACTION_EC2_STOP
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class Ec2StopRequestServiceTest {
    companion object {
        private const val TEST_EC2_ID_1 = "TEST_EC2_ID_1"
        private const val TEST_EC2_ID_2 = "TEST_EC2_ID_2"
        private const val TEST_EC2_NAME = "TEST_EC2_NAME"
    }

    @Mock
    private lateinit var ec2Service: Ec2Service

    @Mock
    private lateinit var permissionService: PermissionService

    @Mock
    private lateinit var fulfillmentRequest: FulfillmentRequest

    @InjectMocks
    private lateinit var ec2StopRequestService: Ec2StopRequestService

    @Test
    fun shouldReturnEc2StartAction() {
        assertThat(ec2StopRequestService.getProcessableActionName()).isEqualTo(ACTION_EC2_STOP)
    }

    @Test
    fun shouldStartEc2ByIdFirst() {
        whenever(fulfillmentRequest.params).thenReturn(mapOf(
                PARAM_EC2_ID to TEST_EC2_ID_1,
                PARAM_EC2_NAME to TEST_EC2_NAME
        ))

        val fulfillmentResponse = ec2StopRequestService.doProcess(fulfillmentRequest)

        assertThat(fulfillmentResponse.speech).isEqualTo("Stopping EC2: $TEST_EC2_ID_1")
        assertThat(fulfillmentResponse.displayText).isEqualTo("Stopping EC2: $TEST_EC2_ID_1")
        verify(ec2Service).stopEc2Instance(TEST_EC2_ID_1)
        verifyNoMoreInteractions(ec2Service)
    }

    @Test
    fun shouldStartEc2ByNameWhenThereIsNoId() {
        whenever(fulfillmentRequest.params).thenReturn(mapOf(
                PARAM_EC2_ID to "",
                PARAM_EC2_NAME to TEST_EC2_NAME
        ))
        whenever(ec2Service.getEc2InstancesByInstanceName(TEST_EC2_NAME)).thenReturn(listOf(
                Ec2Instance(id = TEST_EC2_ID_1, name = TEST_EC2_NAME, type = "t2.large", state = "stopped"),
                Ec2Instance(id = TEST_EC2_ID_2, name = TEST_EC2_NAME, type = "t2.large", state = "stopped")
        ))

        val fulfillmentResponse = ec2StopRequestService.doProcess(fulfillmentRequest)

        assertThat(fulfillmentResponse.speech).isEqualTo("Stopping EC2: $TEST_EC2_ID_1, $TEST_EC2_ID_2")
        assertThat(fulfillmentResponse.displayText).isEqualTo("Stopping EC2: $TEST_EC2_ID_1, $TEST_EC2_ID_2")
        verify(ec2Service).getEc2InstancesByInstanceName(TEST_EC2_NAME)
        verify(ec2Service).stopEc2Instance(TEST_EC2_ID_1, TEST_EC2_ID_2)
        verifyNoMoreInteractions(ec2Service)
    }

    @Test
    fun shouldDoNothingWhenThereIsNoEc2InstanceWithProvidedName() {
        whenever(fulfillmentRequest.params).thenReturn(mapOf(
                PARAM_EC2_ID to "",
                PARAM_EC2_NAME to TEST_EC2_NAME
        ))
        whenever(ec2Service.getEc2InstancesByInstanceName(TEST_EC2_NAME)).thenReturn(emptyList())

        val fulfillmentResponse = ec2StopRequestService.doProcess(fulfillmentRequest)

        assertThat(fulfillmentResponse.speech).isEqualTo("There is no EC2 instances with name: $TEST_EC2_NAME")
        assertThat(fulfillmentResponse.displayText).isEqualTo("There is no EC2 instances with name: $TEST_EC2_NAME")
        verify(ec2Service).getEc2InstancesByInstanceName(TEST_EC2_NAME)
        verifyNoMoreInteractions(ec2Service)
    }

    @Test
    fun shouldDoNothingWhenThereIsNoIdOrName() {
        whenever(fulfillmentRequest.params).thenReturn(mapOf(
                PARAM_EC2_ID to "",
                PARAM_EC2_NAME to ""
        ))

        val fulfillmentResponse = ec2StopRequestService.doProcess(fulfillmentRequest)

        assertThat(fulfillmentResponse.speech).isEqualTo("Please specify the name or id of the EC2 instance")
        assertThat(fulfillmentResponse.displayText).isEqualTo("Please specify the name or id of the EC2 instance")
        verifyNoMoreInteractions(ec2Service)
    }
}
