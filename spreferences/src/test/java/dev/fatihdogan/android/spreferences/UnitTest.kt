package dev.fatihdogan.android.spreferences

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import java.util.*

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class UnitTest {
    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Test
    fun stringDelegate() {
        class Preference(context: Context) : SPreferences(context, UUID.randomUUID().toString()) {
            var property by StringDelegate(defValue = "defValue")
        }

        val preference = Preference(context)
        assert(preference.property == "defValue")
        preference.property = "value"
        assert(preference.property == "value")
    }

    @Test
    fun nullableStringDelegate() {
        class Preference(context: Context) : SPreferences(context, UUID.randomUUID().toString()) {
            var property by NullableStringDelegate()
        }

        val preference = Preference(context)
        assert(preference.property == null)
        preference.property = "value"
        assert(preference.property == "value")
        preference.property = null
        assert(preference.property == null)
    }

    @Test
    fun intDelegate() {
        class Preference(context: Context) : SPreferences(context, UUID.randomUUID().toString()) {
            var property by IntDelegate(defValue = 50)
        }

        val preference = Preference(context)
        assert(preference.property == 50)
        preference.property = 100
        assert(preference.property == 100)
    }

    @Test
    fun nullableIntDelegate() {
        class Preference(context: Context) : SPreferences(context, UUID.randomUUID().toString()) {
            var property by NullableIntDelegate()
        }

        val preference = Preference(context)
        assert(preference.property == null)
        preference.property = 100
        assert(preference.property == 100)
        preference.property = null
        assert(preference.property == null)
    }

    @Test
    fun longDelegate() {
        class Preference(context: Context) : SPreferences(context, UUID.randomUUID().toString()) {
            var property by LongDelegate(defValue = 50L)
        }

        val preference = Preference(context)
        assert(preference.property == 50L)
        preference.property = 100L
        assert(preference.property == 100L)
    }

    @Test
    fun nullableLongDelegate() {
        class Preference(context: Context) : SPreferences(context, UUID.randomUUID().toString()) {
            var property by NullableLongDelegate()
        }

        val preference = Preference(context)
        assert(preference.property == null)
        preference.property = 100L
        assert(preference.property == 100L)
        preference.property = null
        assert(preference.property == null)
    }

    @Test
    fun booleanDelegate() {
        class Preference(context: Context) : SPreferences(context, UUID.randomUUID().toString()) {
            var property by BooleanDelegate(defValue = true)
        }

        val preference = Preference(context)
        assert(preference.property)
        preference.property = false
        assert(!preference.property)
    }

    @Test
    fun nullableBooleanDelegate() {
        class Preference(context: Context) : SPreferences(context, UUID.randomUUID().toString()) {
            var property by NullableBooleanDelegate()
        }

        val preference = Preference(context)
        assert(preference.property == null)
        preference.property = true
        assert(preference.property == true)
        preference.property = false
        assert(preference.property == false)
        preference.property = null
        assert(preference.property == null)
    }

    @Test
    fun floatDelegate() {
        class Preference(context: Context) : SPreferences(context, UUID.randomUUID().toString()) {
            var property by FloatDelegate(defValue = 50F)
        }

        val preference = Preference(context)
        assert(preference.property == 50F)
        preference.property = 100F
        assert(preference.property == 100F)
    }

    @Test
    fun nullableFloatDelegate() {
        class Preference(context: Context) : SPreferences(context, UUID.randomUUID().toString()) {
            var property by NullableFloatDelegate()
        }

        val preference = Preference(context)
        assert(preference.property == null)
        preference.property = 100F
        assert(preference.property == 100F)
        preference.property = null
        assert(preference.property == null)
    }

    @Test
    fun dateDelegate() {
        val defaultValue = Date(50)

        class Preference(context: Context) : SPreferences(context, UUID.randomUUID().toString()) {
            var property by DateDelegate(defaultValue)
        }

        val preference = Preference(context)
        assert(preference.property == defaultValue)
        val newValue = Date(500)
        preference.property = newValue
        assert(preference.property == newValue)
        assert(preference.property != defaultValue)
    }

    @Test
    fun nullableDateDelegate() {
        class Preference(context: Context) : SPreferences(context, UUID.randomUUID().toString()) {
            var property by NullableDateDelegate()
        }

        val preference = Preference(context)
        assert(preference.property == null)
        val newValue = Date(500)
        preference.property = newValue
        assert(preference.property == newValue)
        preference.property = null
        assert(preference.property == null)
    }

    @Test
    fun mapDelegate() {
        val defaultValue = "default"

        class Preference(context: Context) : SPreferences(context, UUID.randomUUID().toString()) {
            val map by MapDelegate(StringDelegate(defaultValue))
        }

        val preference = Preference(context)
        val key1 = "key1"
        val key2 = "key2"
        assert(preference.map[key1] == defaultValue)
        assert(preference.map[key2] == defaultValue)

        val newValue1 = "newValue1"
        preference.map[key1] = newValue1
        assert(preference.map[key1] == newValue1)
        assert(preference.map[key2] == defaultValue)

        val newValue2 = "newValue2"
        preference.map[key2] = newValue2
        assert(preference.map[key1] == newValue1)
        assert(preference.map[key2] == newValue2)
    }

    @Test
    fun nullableMapDelegate() {
        class Preference(context: Context) : SPreferences(context, UUID.randomUUID().toString()) {
            val map by MapDelegate(NullableStringDelegate())
        }

        val preference = Preference(context)
        val key1 = "key1"
        val key2 = "key2"
        assert(preference.map[key1] == null)
        assert(preference.map[key2] == null)

        val newValue1 = "newValue1"
        preference.map[key1] = newValue1
        assert(preference.map[key1] == newValue1)
        assert(preference.map[key2] == null)

        val newValue2 = "newValue2"
        preference.map[key2] = newValue2
        assert(preference.map[key1] == newValue1)
        assert(preference.map[key2] == newValue2)
    }

    @Test
    fun objectDelegate() {
        val defaultValue = Person("defaultName", "defaultSurname")
        val defaultValueCopy = Person("defaultName", "defaultSurname")

        class Preference(context: Context) : SPreferences(context, UUID.randomUUID().toString()) {
            var property by ObjectDelegate(Person::class.java, defaultValue)
        }

        val preference = Preference(context)
        assert(preference.property == defaultValue)
        assert(preference.property == defaultValueCopy)

        val newValue = Person("newName", "newSurname")
        val newValueCopy = Person("newName", "newSurname")
        preference.property = newValue

        assert(preference.property == newValue)
        assert(preference.property == newValueCopy)
    }

    @Test
    fun nullableObjectDelegate() {
        class Preference(context: Context) : SPreferences(context, UUID.randomUUID().toString()) {
            var property by NullableObjectDelegate(Person::class.java)
        }

        val preference = Preference(context)
        assert(preference.property == null)

        val newValue = Person("newName", "newSurname")
        val newValueCopy = Person("newName", "newSurname")
        preference.property = newValue

        assert(preference.property == newValue)
        assert(preference.property == newValueCopy)

        preference.property = null
        assert(preference.property == null)
    }

    @Test(expected = AnonymousClassSerializationException::class)
    fun objectDelegateAnonymousClassException() {
        data class AnonymousPerson(val name: String, val surname: String)

        val defaultValue = AnonymousPerson("defaultName", "defaultSurname")

        class Preference(context: Context) : SPreferences(context, UUID.randomUUID().toString()) {
            var property by ObjectDelegate(AnonymousPerson::class.java, defaultValue)
        }

        val preference = Preference(context)
        assert(preference.property == defaultValue)

        val newValue = AnonymousPerson("newName", "newSurname")
        preference.property = newValue //Exception
    }
}

data class Person(val name: String, val surname: String)
