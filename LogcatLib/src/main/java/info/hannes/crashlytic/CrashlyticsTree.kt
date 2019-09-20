package info.hannes.crashlytic

import android.util.Log
import com.crashlytics.android.Crashlytics
import timber.log.Timber
import java.util.concurrent.atomic.AtomicBoolean

@Suppress("unused")
class CrashlyticsTree : Timber.DebugTree() {

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        if (priority < Log.INFO) {
            return
        }

        super.log(priority, tag, message, throwable)

        CrashlyticsTrackerDelegate.setString("PRIORITY", when (priority) {
            2 -> "Verbose"
            3 -> "Debug"
            4 -> "Info"
            5 -> "Warn"
            6 -> "Error"
            7 -> "Assert"
            else -> priority.toString()
        })
        tag?.let { CrashlyticsTrackerDelegate.setString(KEY_TAG, it) }
        Crashlytics.setString(KEY_MESSAGE, message)
        Crashlytics.setString(KEY_UNIT_TEST, runUnitTest.toString())
        Crashlytics.setString(KEY_ESPRESSO, isRunningEspresso().toString())

        if (priority > Log.INFO) {
            Crashlytics.log(message)
        } else if (priority > Log.WARN) {
            var throwableLocal = throwable
            if (throwableLocal == null) {
                throwableLocal = Throwable(message)
            }
            Crashlytics.logException(throwableLocal)
        }
    }

    companion object {
        const val KEY_TAG = "TAG"
        const val KEY_MESSAGE = "message"
        const val KEY_ESPRESSO = "Espresso"
        const val KEY_UNIT_TEST = "UnitTest"

        private var runningTest: AtomicBoolean? = null
        private var runUnitTest: Boolean? = null

        val isRunningUnitTests: Boolean
            get() {
                if (runUnitTest == null) {
                    runUnitTest = "true" == System.getProperty("run-under-test", "false") || !System.getProperty("java.vendor")!!.contains("Android")
                }
                return runUnitTest!!
            }

        @Synchronized
        fun isRunningEspresso(): Boolean {
            if (runningTest == null) {
                var isTest: Boolean = try {
                    Class.forName("android.support.test.espresso.Espresso")
                    true
                } catch (e: ClassNotFoundException) {
                    false
                }

                if (!isTest) {
                    isTest = try {
                        Class.forName("androidx.test.espresso.Espresso")
                        true
                    } catch (e: ClassNotFoundException) {
                        false
                    }
                }

                runningTest = AtomicBoolean(isTest)
            }
            return runningTest!!.get()
        }
    }
}
