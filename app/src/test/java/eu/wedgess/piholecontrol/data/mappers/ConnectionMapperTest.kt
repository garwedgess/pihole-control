package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.Connection
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.http.URLProtocol
import org.junit.Test

class ConnectionMapperTest {

    @Test
    fun `toConnectionInfo - maps Connection to ConnectionEntity correctly`() {
        val connection = Connection(
            Id = 1,
            Name = "My Pi-hole",
            Protocol = URLProtocol.HTTP,
            Host = "pi.hole",
            Port = 80,
            ApiPath = "/admin/api.php",
            Token = "my-token",
            AuthUserName = "admin",
            AuthPassword = "password",
            AuthRealm = "MyRealm",
            TrustAllCerts = true,
            IsDeleted = false,
            Active = true
        )

        val connectionEntity = connection.toConnectionInfo()

        assertThat(connectionEntity.id).isEqualTo(1)
        assertThat(connectionEntity.name).isEqualTo("My Pi-hole")
        assertThat(connectionEntity.protocol).isEqualTo(URLProtocol.HTTP)
        assertThat(connectionEntity.host).isEqualTo("pi.hole")
        assertThat(connectionEntity.port).isEqualTo(80)
        assertThat(connectionEntity.apiPath).isEqualTo("/admin/api.php")
        assertThat(connectionEntity.token).isEqualTo("my-token")
        assertThat(connectionEntity.authUsername).isEqualTo("admin")
        assertThat(connectionEntity.authPassword).isEqualTo("password")
        assertThat(connectionEntity.authRealm).isEqualTo("MyRealm")
        assertThat(connectionEntity.trustAllCerts).isTrue()
        assertThat(connectionEntity.isDeleted).isFalse()
        assertThat(connectionEntity.isActive).isTrue()
    }

    @Test
    fun `toConnectionInfo - maps Connection with null token to ConnectionEntity with empty token`() {
        val connection = Connection(
            Id = 1,
            Name = "My Pi-hole",
            Protocol = URLProtocol.HTTP,
            Host = "pi.hole",
            Port = 80,
            ApiPath = "/admin/api.php",
            Token = null,
            AuthUserName = "admin",
            AuthPassword = "password",
            AuthRealm = "MyRealm",
            TrustAllCerts = true,
            IsDeleted = false,
            Active = true
        )

        val connectionEntity = connection.toConnectionInfo()

        assertThat(connectionEntity.token).isEmpty()
    }

    @Test
    fun `toConnection - maps ConnectionEntity to Connection correctly`() {
        val connectionEntity = ConnectionEntity(
            id = 2,
            name = "Another Pi-hole",
            protocol = URLProtocol.HTTPS,
            host = "192.168.1.10",
            port = 443,
            apiPath = "/admin/api.php",
            token = "another-token",
            authUsername = "user",
            authPassword = "secret",
            authRealm = "AnotherRealm",
            trustAllCerts = false,
            isDeleted = true,
            isActive = false
        )

        val connection = connectionEntity.toConnection()

        assertThat(connection.Id).isEqualTo(2)
        assertThat(connection.Name).isEqualTo("Another Pi-hole")
        assertThat(connection.Protocol).isEqualTo(URLProtocol.HTTPS)
        assertThat(connection.Host).isEqualTo("192.168.1.10")
        assertThat(connection.Port).isEqualTo(443)
        assertThat(connection.ApiPath).isEqualTo("/admin/api.php")
        assertThat(connection.Token).isEqualTo("another-token")
        assertThat(connection.AuthUserName).isEqualTo("user")
        assertThat(connection.AuthPassword).isEqualTo("secret")
        assertThat(connection.AuthRealm).isEqualTo("AnotherRealm")
        assertThat(connection.TrustAllCerts).isFalse()
        assertThat(connection.IsDeleted).isTrue()
        assertThat(connection.Active).isFalse()
    }

    @Test
    fun `toConnection - maps ConnectionEntity with empty token to Connection with empty token`() {
        val connectionEntity = ConnectionEntity(
            id = 2,
            name = "Another Pi-hole",
            protocol = URLProtocol.HTTPS,
            host = "192.168.1.10",
            port = 443,
            apiPath = "/admin/api.php",
            token = "",
            authUsername = "user",
            authPassword = "secret",
            authRealm = "AnotherRealm",
            trustAllCerts = false,
            isDeleted = true,
            isActive = false
        )

        val connection = connectionEntity.toConnection()

        assertThat(connection.Token).isEmpty()
    }
}
