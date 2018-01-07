package com.uitnetwork.bot.service.ec2

import com.amazonaws.services.ec2.AmazonEC2
import com.amazonaws.services.ec2.model.*
import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.invocation.InvocationOnMock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.stubbing.Answer

@RunWith(MockitoJUnitRunner::class)
class Ec2ServiceTest {
    companion object {
        private val TEST_INSTANCE_ID_1 = "TEST_INSTANCE_ID_1"
        private val TEST_INSTANCE_TYPE_1 = "TEST_INSTANCE_TYPE_1"
        private val TEST_STATE_1 = "TEST_STATE_1"
        private val TEST_INSTANCE_ID_2 = "TEST_INSTANCE_ID_2"
        private val TEST_INSTANCE_TYPE_2 = "TEST_INSTANCE_TYPE_2"
        private val TEST_STATE_2 = "TEST_STATE_2"
        private val TEST_NAME_2 = "TEST_NAME_2"
        private val TEST_NEXT_TOKEN = "TEST_NEXT_TOKEN"
    }

    @Mock
    private lateinit var amazonEc2: AmazonEC2

    @Mock
    private lateinit var stopInstancesResult: StopInstancesResult

    @Mock
    private lateinit var startInstancesResult: StartInstancesResult

    @Mock
    private lateinit var describeInstancesResult1: DescribeInstancesResult

    @Mock
    private lateinit var reservation1: Reservation

    @Mock
    private lateinit var instance1: Instance

    @Mock
    private lateinit var instanceState1: InstanceState

    @Mock
    private lateinit var tag1: Tag

    @Mock
    private lateinit var describeInstancesResult2: DescribeInstancesResult

    @Mock
    private lateinit var reservation2: Reservation

    @Mock
    private lateinit var instance2: Instance

    @Mock
    private lateinit var instanceState2: InstanceState

    @Mock
    private lateinit var tag2: Tag

    @InjectMocks
    private lateinit var ec2Service: Ec2Service

    @Before
    fun setup() {
        whenever(amazonEc2.startInstances(any())).thenReturn(startInstancesResult)
        whenever(amazonEc2.stopInstances(any())).thenReturn(stopInstancesResult)
        whenever(amazonEc2.describeInstances(any())).thenAnswer(object : Answer<DescribeInstancesResult> {
            private var count = 0
            override fun answer(invocation: InvocationOnMock): DescribeInstancesResult {
                if (count++ == 0) {
                    return describeInstancesResult1
                }
                return describeInstancesResult2
            }
        })

        whenever(describeInstancesResult1.nextToken).thenReturn(TEST_NEXT_TOKEN)
        whenever(describeInstancesResult1.reservations).thenReturn(listOf(reservation1))
        whenever(describeInstancesResult2.reservations).thenReturn(listOf(reservation2))
        whenever(reservation1.instances).thenReturn(listOf(instance1))
        whenever(reservation2.instances).thenReturn(listOf(instance2))

        whenever(instance1.instanceId).thenReturn(TEST_INSTANCE_ID_1)
        whenever(tag1.key).thenReturn("something_else")
        whenever(instance1.tags).thenReturn(listOf(tag1))
        whenever(instance1.instanceType).thenReturn(TEST_INSTANCE_TYPE_1)
        whenever(instanceState1.name).thenReturn(TEST_STATE_1)
        whenever(instance1.state).thenReturn(instanceState1)

        whenever(instance2.instanceId).thenReturn(TEST_INSTANCE_ID_2)
        whenever(tag2.key).thenReturn("Name")
        whenever(tag2.value).thenReturn(TEST_NAME_2)
        whenever(instance2.tags).thenReturn(listOf(tag2))
        whenever(instance2.instanceType).thenReturn(TEST_INSTANCE_TYPE_2)
        whenever(instanceState2.name).thenReturn(TEST_STATE_2)
        whenever(instance2.state).thenReturn(instanceState2)
    }


    @Test
    fun shouldReturnEc2OverviewForAll() {
        val ec2Overview = ec2Service.getEc2Overview()

        assertThat(ec2Overview).contains("Ec2 Overview")
        assertThat(ec2Overview).contains(Ec2Instance(id = TEST_INSTANCE_ID_1, name = "NO_NAME", type = TEST_INSTANCE_TYPE_1, state = TEST_STATE_1).toString())
        assertThat(ec2Overview).contains(Ec2Instance(id = TEST_INSTANCE_ID_2, name = TEST_NAME_2, type = TEST_INSTANCE_TYPE_2, state = TEST_STATE_2).toString())
    }

    @Test
    fun shouldReturnEc2InstanceWithSpecificName() {
        whenever(amazonEc2.describeInstances(any())).thenReturn(describeInstancesResult2)

        val ec2Intances = ec2Service.getEc2InstancesByInstanceName(TEST_NAME_2)

        assertThat(ec2Intances).containsExactly(Ec2Instance(id = TEST_INSTANCE_ID_2, name = TEST_NAME_2, type = TEST_INSTANCE_TYPE_2, state = TEST_STATE_2))
    }

    @Test
    fun shouldStartEc2InstanceWithIds() {
        ec2Service.startEc2Instance(TEST_INSTANCE_ID_1, TEST_INSTANCE_ID_2)

        val startInstancesRequestArgumentCaptor = argumentCaptor<StartInstancesRequest>()
        verify(amazonEc2).startInstances(startInstancesRequestArgumentCaptor.capture())
        assertThat(startInstancesRequestArgumentCaptor.firstValue.instanceIds).containsExactlyInAnyOrder(TEST_INSTANCE_ID_1, TEST_INSTANCE_ID_2)
        verifyNoMoreInteractions(amazonEc2)
    }

    @Test
    fun shouldStopEc2InstanceWithIds() {
        ec2Service.stopEc2Instance(TEST_INSTANCE_ID_1, TEST_INSTANCE_ID_2)

        val stopInstancesRequestArgumentCaptor = argumentCaptor<StopInstancesRequest>()
        verify(amazonEc2).stopInstances(stopInstancesRequestArgumentCaptor.capture())
        assertThat(stopInstancesRequestArgumentCaptor.firstValue.instanceIds).containsExactlyInAnyOrder(TEST_INSTANCE_ID_1, TEST_INSTANCE_ID_2)
        verifyNoMoreInteractions(amazonEc2)
    }
}
