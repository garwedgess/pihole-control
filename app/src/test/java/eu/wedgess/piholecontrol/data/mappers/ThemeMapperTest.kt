package eu.wedgess.piholecontrol.data.mappers

import com.google.common.truth.Truth.assertThat
import eu.wedgess.piholecontrol.data.model.UserPreferences
import eu.wedgess.piholecontrol.domain.model.AppThemeEntity
import org.junit.Test

class ThemeMapperTest {

    @Test
    fun `toEntity - maps SYSTEM correctly`() {
        val result = UserPreferences.Theme.SYSTEM.toEntity()
        assertThat(result).isEqualTo(AppThemeEntity.SYSTEM)
    }

    @Test
    fun `toEntity - maps DARK correctly`() {
        val result = UserPreferences.Theme.DARK.toEntity()
        assertThat(result).isEqualTo(AppThemeEntity.DARK)
    }

    @Test
    fun `toEntity - maps LIGHT correctly`() {
        val result = UserPreferences.Theme.LIGHT.toEntity()
        assertThat(result).isEqualTo(AppThemeEntity.LIGHT)
    }

    @Test
    fun `toEntity - maps UNRECOGNIZED correctly`() {
        val result = UserPreferences.Theme.UNRECOGNIZED.toEntity()
        assertThat(result).isEqualTo(AppThemeEntity.SYSTEM)
    }

    @Test
    fun `toEntity - maps null correctly`() {
        val result = (null as UserPreferences.Theme?).toEntity()
        assertThat(result).isEqualTo(AppThemeEntity.SYSTEM)
    }
}
