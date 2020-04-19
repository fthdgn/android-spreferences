package dev.fatihdogan.android.spreferences

import android.content.Context
import android.content.SharedPreferences
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

abstract class SPreferences(
    context: Context,
    name: String,
    val saveMode: SaveMode = SaveMode.APPLY
) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    enum class SaveMode {
        APPLY, COMMIT
    }

    abstract class SPreferencesReadWriteProperty<T>() : ReadWriteProperty<SPreferences, T> {

        final override fun getValue(thisRef: SPreferences, property: KProperty<*>): T =
            getValue(thisRef, property, null)

        final override fun setValue(thisRef: SPreferences, property: KProperty<*>, value: T) =
            setValue(thisRef, property, null, value)

        fun getValue(thisRef: SPreferences, property: KProperty<*>, key: String?): T {
            val name = if (key == null) property.name else "${property.name}[$key]"
            return getValue(thisRef.sharedPreferences, name)
        }

        fun setValue(thisRef: SPreferences, property: KProperty<*>, key: String?, value: T) {
            val name = if (key == null) property.name else "${property.name}[$key]"
            val editor = thisRef.sharedPreferences.edit()
            setValue(editor, name, value)
            when (thisRef.saveMode) {
                SaveMode.APPLY -> editor.apply()
                SaveMode.COMMIT -> editor.commit()
            }
        }

        abstract fun getValue(pref: SharedPreferences, key: String): T
        abstract fun setValue(editor: SharedPreferences.Editor, key: String, value: T)
    }

    class Map<T>(
        private val thisRef: SPreferences,
        private val property: KProperty<*>,
        private val readWritePropertyS: SPreferencesReadWriteProperty<T>
    ) {
        operator fun get(key: String): T {
            return readWritePropertyS.getValue(thisRef, property, key)
        }

        operator fun set(key: String, value: T) {
            readWritePropertyS.setValue(thisRef, property, key, value)
        }
    }

    class MapDelegate<T>(private val readWritePropertyS: SPreferencesReadWriteProperty<T>) :
        ReadOnlyProperty<SPreferences, Map<T>> {

        override fun getValue(thisRef: SPreferences, property: KProperty<*>): Map<T> =
            Map(thisRef, property, readWritePropertyS)
    }

    class StringDelegate(private val defValue: String = "") :
        SPreferencesReadWriteProperty<String>() {
        override fun getValue(pref: SharedPreferences, key: String): String {
            return pref.getString(key, defValue) ?: defValue
        }

        override fun setValue(editor: SharedPreferences.Editor, key: String, value: String) {
            editor.putString(key, value)
        }
    }

    class NullableStringDelegate : SPreferencesReadWriteProperty<String?>() {
        override fun getValue(pref: SharedPreferences, key: String): String? {
            return pref.getString(key, null)
        }

        override fun setValue(editor: SharedPreferences.Editor, key: String, value: String?) {
            editor.putString(key, value)
        }
    }

    class IntDelegate(private val defValue: Int = 0) : SPreferencesReadWriteProperty<Int>() {
        override fun getValue(pref: SharedPreferences, key: String): Int {
            return pref.getInt(key, defValue)
        }

        override fun setValue(editor: SharedPreferences.Editor, key: String, value: Int) {
            editor.putInt(key, value)
        }
    }

    class NullableIntDelegate() : SPreferencesReadWriteProperty<Int?>() {
        override fun getValue(pref: SharedPreferences, key: String): Int? {
            return pref.getString(key, null)?.toInt()
        }

        override fun setValue(editor: SharedPreferences.Editor, key: String, value: Int?) {
            editor.putString(key, value.toString())
        }
    }

    class FloatDelegate(private val defValue: Float = 0F) : SPreferencesReadWriteProperty<Float>() {
        override fun getValue(pref: SharedPreferences, key: String): Float {
            return pref.getFloat(key, defValue)
        }

        override fun setValue(editor: SharedPreferences.Editor, key: String, value: Float) {
            editor.putFloat(key, value)
        }
    }

    class NullableFloatDelegate : SPreferencesReadWriteProperty<Float?>() {
        override fun getValue(pref: SharedPreferences, key: String): Float? {
            return pref.getString(key, null)?.toFloat()
        }

        override fun setValue(editor: SharedPreferences.Editor, key: String, value: Float?) {
            editor.putString(key, value.toString())
        }
    }

    class BooleanDelegate(private val defValue: Boolean = false) :
        SPreferencesReadWriteProperty<Boolean>() {

        override fun getValue(pref: SharedPreferences, key: String): Boolean {
            return pref.getBoolean(key, defValue)
        }

        override fun setValue(editor: SharedPreferences.Editor, key: String, value: Boolean) {
            editor.putBoolean(key, value)
        }
    }

    class NullableBooleanDelegate : SPreferencesReadWriteProperty<Boolean?>() {
        override fun getValue(pref: SharedPreferences, key: String): Boolean? {
            return pref.getString(key, null)?.toBoolean()
        }

        override fun setValue(editor: SharedPreferences.Editor, key: String, value: Boolean?) {
            editor.putString(key, value.toString())
        }
    }


    class LongDelegate(private val defValue: Long = 0) : SPreferencesReadWriteProperty<Long>() {
        override fun getValue(pref: SharedPreferences, key: String): Long {
            return pref.getLong(key, defValue)
        }

        override fun setValue(editor: SharedPreferences.Editor, key: String, value: Long) {
            editor.putLong(key, value)
        }
    }

    class NullableLongDelegate(private val defValue: Long = 0) :
        SPreferencesReadWriteProperty<Long?>() {

        override fun getValue(pref: SharedPreferences, key: String): Long? {
            return pref.getString(key, null)?.toLong()
        }

        override fun setValue(editor: SharedPreferences.Editor, key: String, value: Long?) {
            editor.putString(key, value.toString())
        }
    }

    class StringSet(private val defValue: Set<String>) :
        SPreferencesReadWriteProperty<Set<String>>() {

        override fun getValue(pref: SharedPreferences, key: String): Set<String> {
            return pref.getStringSet(key, defValue) ?: defValue
        }

        override fun setValue(editor: SharedPreferences.Editor, key: String, value: Set<String>) {
            editor.putStringSet(key, value)
        }
    }

    class DateDelegate(private val defValue: Date = Date(0)) :
        SPreferencesReadWriteProperty<Date>() {

        override fun getValue(pref: SharedPreferences, key: String): Date {
            return Date(pref.getLong(key, defValue.time))
        }

        override fun setValue(editor: SharedPreferences.Editor, key: String, value: Date) {
            editor.putLong(key, value.time)
        }
    }

    class NullableDateDelegate : SPreferencesReadWriteProperty<Date?>() {
        override fun getValue(pref: SharedPreferences, key: String): Date? {
            return pref.getString(key, null)?.let {
                Date(it.toLong())
            }
        }

        override fun setValue(editor: SharedPreferences.Editor, key: String, value: Date?) {
            editor.putString(key, value?.time.toString())
        }
    }
}
