# Android SPreferences / SharedPreferences with Kotlin Property Delegates

## How to install
Add repository to the root build.gradle
```
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
        ...
    }
}
```
Add dependency to the build.gradle of the module. Replace ```{VERSION}``` by the latest version on
Releases page. For ```SNAPSHOT``` versions use ```{branchname}-SNAPSHOT```
```
dependencies {
    ...
    implementation 'dev.fatihdogan:android-spreferences:{VERSION}'
    ...
}
```

## Usage
Instead of directly accessing SharedPreferences object with string keys, extends a class to
SPreferences and delegate its properties according to their types.

```kotlin
class Preferences(context: Context) : SPreferences(context, "NAME", SaveMode.APPLY) {
    var aDateProperty by DateDelegate(defValue = Date())
    var aNullableDateProperty by NullableDateDelegate()
    var aBooleanProperty by BooleanDelegate(defValue = false)
    var aIntProperty by IntDelegate(defValue = 5)
    var aFloatProperty by FloatDelegate(defValue = 0F)
}

fun demo(preferences: Preferences) {
    preferences.aDateProperty = Date()  //underlying key is aDateProperty
    val time = preferences.aDateProperty.time //underlying key is aDateProperty
    preferences.aNullableDateProperty = null //underlying key is aNullableDateProperty
}
```

## Delegates
Delegates read and write their values to the shared preferences by using property name as key.

Classes with built-in delegates and default defValues
* **kotlin.String** = **StringDelegate** default ""
* **kotlin.String?** = **NullableStringDelegate**
* **kotlin.Int** = **IntDelegate** default 0
* **kotlin.Int?** = **NullableIntDelegate**
* **kotlin.Long** = **LongDelegate** default 0
* **kotlin.Long?** = **NullableLongDelegate**
* **kotlin.Boolean** = **BooleanDelegate** default false
* **kotlin.Boolean?** = **NullableBooleanDelegate**
* **kotlin.Float** = **FloatDelegate** default 0F
* **kotlin.Float?** = **NullableFloatDelegate**
* **java.util.Date** = **DateDelegate** default Date(0)
* **java.util.Date?** = **NullableDateDelegate**

## MapDelegate
MapDelegate allows to create a map with string key. Value type of map is defined by the delegate parameter of MapDelegate.
It concatenate the property name of itself and the key of the map to determine underlying shared preferences name.

```kotlin
class Preferences(context: Context) : SPreferences(context, "NAME", SaveMode.APPLY) {
    val aDateMapProperty by MapDelegate(DateDelegate(defValue = Date()))
    val aIntMapProperty by MapDelegate(IntDelegate(defValue = 0))
}

fun demo(preferences: Preferences) {
    preferences.aDateMapProperty["key"] = Date() //underlying key is aDateMapProperty[key]
    val keyTime = preferences.aDateMapProperty["key"].time

    preferences.aIntMapProperty["numberA"] = 10 //underlying key is aIntMapProperty[numberA]
    preferences.aIntMapProperty["numberB"] = 20 //underlying key is aIntMapProperty[numberB]
    val aPlusB = preferences.aIntMapProperty["numberA"] + preferences.aIntMapProperty["numberB"]
}
```

## ObjectDelegate
ObjectDelegate and NullableObjectDelegate converts POJO to string by using Gson and saves it to
SharedPreferences. ObjectDelegate requires a default value.

```kotlin
data class Person(val name: String, val surname: String)

class Preferences(context: Context) : SPreferences(context, "NAME", SaveMode.APPLY) {
    var aPersonProperty by ObjectDelegate(Person::class.java, Person("defName", "defSurname"))
    var aNullablePersonProperty by NullableObjectDelegate(Person::class.java)
}

fun demo(preferences: Preferences) {
    preferences.aPersonProperty = Person("name", "surname")
    val name = preferences.aPersonProperty.name

    preferences.aNullablePersonProperty = Person("name", "surname")
    val nullableName = preferences.aNullablePersonProperty?.name
}
```

## Custom delegates
Custom delegates can be created for unsupported classes.

```kotlin
data class Person(val name: String, val age: Int)

class PersonDelegate : SPreferences.SPreferencesReadWriteProperty<Person?>() {
    override fun getValue(pref: SharedPreferences, key: String): Person? {
        return pref.getString(key, null)?.let {
            val splits = it.split("|")
            Person(splits[0], splits[1].toInt())
        }
    }

    override fun setValue(editor: SharedPreferences.Editor, key: String, value: Person?) {
        if (value == null) {
            editor.putString(key, null)
        } else {
            editor.putString(key, "${value.name}|${value.age}")
        }
        //don't apply or commit
    }
}

class Preferences(context: Context) : SPreferences(context, "NAME", SaveMode.APPLY) {
    var aPersonDelegate by PersonDelegate()
}

fun demo(preferences: Preferences) {
    preferences.aPersonDelegate = Person("name", 30)
    val user = preferences.aPersonDelegate
}
```
