package wzl.android.ducksplash.api.login

import androidx.datastore.core.CorruptionException
import androidx.datastore.core.Serializer
import com.google.protobuf.InvalidProtocolBufferException
import wzl.android.ducksplash.LoginPreferences
import java.io.InputStream
import java.io.OutputStream

/**
 *Created on 2021/3/9
 *@author zhilin
 */
object LoginPreferencesSerializer: Serializer<LoginPreferences> {
    override val defaultValue: LoginPreferences
        get() = LoginPreferences.getDefaultInstance()

    override fun readFrom(input: InputStream): LoginPreferences {
        try {
            return LoginPreferences.parseFrom(input)
        } catch (e: InvalidProtocolBufferException) {
            throw CorruptionException("Cannot read proto.", e)
        }
    }

    override fun writeTo(t: LoginPreferences, output: OutputStream) {
        t.writeTo(output)
    }
}
