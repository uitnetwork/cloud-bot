package com.uitnetwork.bot.service.gce

import com.google.api.services.compute.Compute
import com.google.api.services.compute.model.Instance
import com.google.api.services.compute.model.InstanceList
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.uitnetwork.bot.service.gce.GceService.Companion.GCP_GCE_ZONE
import com.uitnetwork.bot.service.gce.GceService.Companion.GCP_PROJECT_ID
import org.assertj.core.api.Assertions.assertThat
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.springframework.core.env.Environment
import java.math.BigInteger

@RunWith(MockitoJUnitRunner::class)
class GceServiceTest {
    companion object {
        private val TEST_ID = BigInteger.valueOf(1L)
        private val TEST_NAME = "TEST_NAME"
        private val TEST_PROJECT_ID = "TEST_PROJECT_ID"
        private val TEST_ZONE = "asia-southeast1-a"
        private val TEST_MACHINE_TYPE = "n1-standard-1"
        private val TEST_MACHINE_TYPE_URL = "https://www.googleapis.com/compute/v1/projects/$TEST_PROJECT_ID/zones/$TEST_ZONE/machineTypes/$TEST_MACHINE_TYPE"
        private val TEST_STATUS = "TERMINATED"
    }

    @Mock
    private lateinit var compute: Compute

    @Mock
    private lateinit var instances: Compute.Instances

    @Mock
    private lateinit var list: Compute.Instances.List

    @Mock
    private lateinit var instanceList: InstanceList

    @Mock
    private lateinit var instance: Instance

    @Mock
    private lateinit var env: Environment

    @Mock
    private lateinit var start: Compute.Instances.Start

    @Mock
    private lateinit var stop: Compute.Instances.Stop

    @InjectMocks
    private lateinit var gceService: GceService

    @Before
    fun setup() {
        whenever(env.getRequiredProperty(GCP_PROJECT_ID)).thenReturn(TEST_PROJECT_ID)
        whenever(env.getRequiredProperty(GCP_GCE_ZONE)).thenReturn(TEST_ZONE)

        whenever(compute.instances()).thenReturn(instances)
    }

    @Test
    fun shouldGetGceOverviewFromCorrectProjectAndZone() {
        mockListRequestAndResponse()

        val gceOverview = gceService.getGceOverview()

        assertThat(gceOverview).contains("Compute Engine Overview")
        assertThat(gceOverview).contains(GceInstance(id = TEST_ID, name = TEST_NAME, machineType = TEST_MACHINE_TYPE, status = TEST_STATUS).toString())

        verify(instances).list(TEST_PROJECT_ID, TEST_ZONE)
        verifyNoMoreInteractions(instances)
    }

    private fun mockListRequestAndResponse() {
        whenever(instances.list(any(), any())).thenReturn(list)
        whenever(list.execute()).thenReturn(instanceList)
        whenever(instanceList.items).thenReturn(listOf(instance))
        whenever(instance.id).thenReturn(TEST_ID)
        whenever(instance.name).thenReturn(TEST_NAME)
        whenever(instance.machineType).thenReturn(TEST_MACHINE_TYPE_URL)
        whenever(instance.status).thenReturn(TEST_STATUS)
    }

    @Test
    fun shouldStartGceInstanceByName() {
        whenever(instances.start(any(), any(), any())).thenReturn(start)

        gceService.startGceInstance(TEST_NAME)

        verify(instances).start(TEST_PROJECT_ID, TEST_ZONE, TEST_NAME)
        verify(start).execute()
        verifyNoMoreInteractions(instances, start)
    }

    @Test
    fun shouldStopGceInstanceByName() {
        whenever(instances.stop(any(), any(), any())).thenReturn(stop)

        gceService.stopGceInstance(TEST_NAME)

        verify(instances).stop(TEST_PROJECT_ID, TEST_ZONE, TEST_NAME)
        verify(stop).execute()
        verifyNoMoreInteractions(instances, stop)
    }
}
