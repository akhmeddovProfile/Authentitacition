package testpincode

import android.app.KeyguardManager
import android.app.VoiceInteractor
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.net.sip.SipManager.newInstance
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.testpincode.Dashboarding
import com.example.testpincode.R
import kotlinx.android.synthetic.main.activity_main.*
import java.lang.reflect.Array.newInstance
import java.util.concurrent.Executor
import java.util.jar.Manifest

import javax.xml.datatype.DatatypeFactory.newInstance
import javax.xml.parsers.SAXParserFactory.newInstance

class MainActivity : AppCompatActivity() {
        private var cancellationSignal:CancellationSignal?=null
        private val authenticationCallback:BiometricPrompt.AuthenticationCallback
            get()=
                @RequiresApi(Build.VERSION_CODES.P)
                object:BiometricPrompt.AuthenticationCallback(){
                    override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                        super.onAuthenticationError(errorCode, errString)
                        notifyUser("Error message;$errString")
                    }

                    override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                        super.onAuthenticationSucceeded(result)
                        startActivity(Intent(this@MainActivity,Dashboarding::class.java))
                    }
                }
        lateinit var useTouch:TextView;
      @RequiresApi(Build.VERSION_CODES.P)
      override fun onCreate(savedInstanceState: Bundle?) {
          super.onCreate(savedInstanceState)
          setContentView(R.layout.activity_main)
            useTouch=findViewById(R.id.useTouchid)
        checkBiometricSupport()
        useTouchid.setOnClickListener {
            val biometricPrompt=BiometricPrompt.Builder(this)
                .setTitle("Title of Prompt")
                .setSubtitle("Authentication is required")
                .setDescription("This app uses fingerprint")
                .setNegativeButton("Cancel",this.mainExecutor,DialogInterface.OnClickListener { dialogInterface, i ->
                    notifyUser("Authentication is cancelled")

                    }).build()
        biometricPrompt.authenticate(getCancellationSignal(),mainExecutor,authenticationCallback)
        }

      }

    private fun checkBiometricSupport():Boolean {
            val keyguardmanager=getSystemService(Context.KEYGUARD_SERVICE)as KeyguardManager
        if(!keyguardmanager.isKeyguardSecure){
            notifyUser("Fingerprint authentication has not been enabled in Settings")
            return false
        }
        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.USE_BIOMETRIC)!=PackageManager.PERMISSION_GRANTED){
            notifyUser("Fingerprint authentication has not been enabled ")
            return false
        }
        return if(packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)){
            true
        }else true
        }

    private fun notifyUser(message:String){
        Toast.makeText(this,message,Toast.LENGTH_SHORT)
    }

    private fun getCancellationSignal():CancellationSignal{
        cancellationSignal= CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }
  }


