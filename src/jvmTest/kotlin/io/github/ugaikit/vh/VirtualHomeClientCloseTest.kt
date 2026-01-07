package io.github.ugaikit.vh

import io.ktor.client.HttpClient
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Test

class VirtualHomeClientCloseTest {
    @Test
    fun `close delegates to underlying http client`() {
        val mockHttpClient = mockk<HttpClient>()
        every { mockHttpClient.close() } just runs

        val client =
            VirtualHomeClient(
                host = "localhost",
                port = 8080,
                timeout = 1000L,
                httpClient = mockHttpClient,
            )

        client.close()

        verify(exactly = 1) { mockHttpClient.close() }
    }
}
