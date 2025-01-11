package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.Connection
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.http.URLProtocol
import org.junit.Test
import java.util.UUID

class ConnectionMapperTest {

    @Test
    fun `toEntity - maps Connection to Version5`() {
        val connection = Connection(
            Id = UUID.randomUUID(),
            Name = "My Pi-hole",
            Protocol = URLProtocol.HTTP,
            Host = "pi.hole",
            Port = 80,
            ApiPath = "/admin/api.php",
            Token = "token",
            AuthUserName = "admin",
            AuthPassword = "password",
            AuthRealm = "MyRealm",
            TrustAllCerts = true,
            IsDeleted = false,
            Active = true,
            Sid = null,
            Password = null,
            ApiVersion = 1
        )

        val connectionEntity = connection.toEntity()

        assertThat(connectionEntity).isInstanceOf(ConnectionEntity.Version5::class.java)
    }

    @Test
    fun `toEntityVersion5 - maps Connection to Version5 entity correctly`() {
        val connection = Connection(
            Id = UUID.randomUUID(),
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
            Active = true,
            Sid = null,
            Password = null,
            ApiVersion = 1
        )

        val connectionEntity = connection.toEntityVersion5()

        assertThat(connectionEntity).isInstanceOf(ConnectionEntity.Version5::class.java)
        assertThat(connectionEntity.id).isEqualTo(connection.Id)
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
    fun `toEntity - maps Connection to Version6`() {
        val connection = Connection(
            Id = UUID.randomUUID(),
            Name = "My Pi-hole",
            Protocol = URLProtocol.HTTPS,
            Host = "pi.hole",
            Port = 80,
            ApiPath = null,
            Token = null,
            AuthUserName = "admin",
            AuthPassword = "password",
            AuthRealm = "MyRealm",
            TrustAllCerts = true,
            IsDeleted = false,
            Active = true,
            Sid = "session-id",
            Password = "password",
            ApiVersion = 2
        )

        val connectionEntity = connection.toEntity()

        assertThat(connectionEntity).isInstanceOf(ConnectionEntity.Version6::class.java)
    }

    @Test
    fun `toEntityVersion6 - maps Connection to Version6 entity correctly`() {
        val connection = Connection(
            Id = UUID.randomUUID(),
            Name = "My Pi-hole",
            Protocol = URLProtocol.HTTP,
            Host = "pi.hole",
            Port = 80,
            ApiPath = null,
            Token = null,
            AuthUserName = "admin",
            AuthPassword = "password",
            AuthRealm = "MyRealm",
            TrustAllCerts = true,
            IsDeleted = false,
            Active = true,
            Sid = "session-id",
            Password = "password",
            ApiVersion = 2
        )

        val connectionEntity = connection.toEntityVersion6()

        assertThat(connectionEntity).isInstanceOf(ConnectionEntity.Version6::class.java)
        assertThat(connectionEntity.id).isEqualTo(connection.Id)
        assertThat(connectionEntity.name).isEqualTo("My Pi-hole")
        assertThat(connectionEntity.protocol).isEqualTo(URLProtocol.HTTP)
        assertThat(connectionEntity.host).isEqualTo("pi.hole")
        assertThat(connectionEntity.port).isEqualTo(80)
        assertThat(connectionEntity.sid).isEqualTo("session-id")
        assertThat(connectionEntity.password).isEqualTo("password")
        assertThat(connectionEntity.authUsername).isEqualTo("admin")
        assertThat(connectionEntity.authPassword).isEqualTo("password")
        assertThat(connectionEntity.authRealm).isEqualTo("MyRealm")
        assertThat(connectionEntity.trustAllCerts).isTrue()
        assertThat(connectionEntity.isDeleted).isFalse()
        assertThat(connectionEntity.isActive).isTrue()
    }
}
