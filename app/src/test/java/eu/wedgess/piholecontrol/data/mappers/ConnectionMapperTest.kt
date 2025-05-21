package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.Connection
import eu.wedgess.piholecontrol.domain.model.ConnectionEntity
import io.ktor.http.URLProtocol
import org.junit.Test
import java.util.UUID

class ConnectionMapperTest {

    @Test
    fun `toEntity - maps Connection`() {
        val connection = Connection(
            Id = UUID.randomUUID(),
            Name = "My Pi-hole",
            Protocol = URLProtocol.HTTPS,
            Host = "pi.hole",
            Port = 80,
            AuthUserName = "admin",
            AuthPassword = "password",
            AuthRealm = "MyRealm",
            TrustAllCerts = true,
            IsDeleted = false,
            Active = true,
            Sid = "session-id",
            Password = "password",
            ApiPath = "/api"
        )

        val connectionEntity = connection.toEntity()

        assertThat(connectionEntity).isInstanceOf(ConnectionEntity::class.java)
    }

    @Test
    fun `toEntity - maps Connection to entity correctly`() {
        val connection = Connection(
            Id = UUID.randomUUID(),
            Name = "My Pi-hole",
            Protocol = URLProtocol.HTTP,
            Host = "pi.hole",
            Port = 80,
            AuthUserName = "admin",
            AuthPassword = "password",
            AuthRealm = "MyRealm",
            TrustAllCerts = true,
            IsDeleted = false,
            Active = true,
            Sid = "session-id",
            Password = "password",
            ApiPath = "/api"
        )

        val connectionEntity = connection.toEntity()

        assertThat(connectionEntity).isInstanceOf(ConnectionEntity::class.java)
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
