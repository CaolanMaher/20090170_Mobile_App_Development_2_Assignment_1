package ie.wit.a20090170_mobile_app_2_assignment_1.activities

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import ie.wit.a20090170_mobile_app_2_assignment_1.R
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.ActivitySignInBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.firebase.FirebaseImageManager
import ie.wit.a20090170_mobile_app_2_assignment_1.ui.campaign.CampaignFragment
import timber.log.Timber
import timber.log.Timber.Forest.i

//import timber.log.Timber.i

class SignInActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignInBinding
    private lateinit var auth: FirebaseAuth
    //var googleSignInClient = MutableLiveData<GoogleSignInClient>()
    lateinit var googleSignInClient: GoogleSignInClient

    val RC_SIGN_IN = 9001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        binding.emailSignInNew.setText("")
        binding.passwordSignInNew.setText("")
        binding.emailRegisterNew.setText("")
        binding.passwordRegisterNew.setText("")

        setContentView(binding.root)

        auth = Firebase.auth

        binding.googlButtonSignIn.setSize(SignInButton.SIZE_WIDE)
        binding.googlButtonSignIn.setColorScheme(0)

        binding.buttonSignIn.setOnClickListener {
            try {
                val email = binding.emailSignInNew.text.toString()
                val password = binding.passwordSignInNew.text.toString()
                if(email.isNotEmpty() && password.isNotEmpty()) {
                    signIn(email, password)

                    binding.emailSignInNew.setText("")
                    binding.passwordSignInNew.setText("")
                    binding.emailRegisterNew.setText("")
                    binding.passwordRegisterNew.setText("")
                }
                else {
                    Toast.makeText(baseContext, "Please fill in necessary fields",
                        Toast.LENGTH_SHORT).show()
                }
            }
            catch(e : Exception) {
                i(e)
            }
        }

        binding.buttonRegister.setOnClickListener {
            try {
                val email = binding.emailRegisterNew.text.toString()
                val password = binding.passwordRegisterNew.text.toString()
                if(email.isNotEmpty() && password.isNotEmpty()) {
                    createAccount(email, password)

                    binding.emailSignInNew.setText("")
                    binding.passwordSignInNew.setText("")
                    binding.emailRegisterNew.setText("")
                    binding.passwordRegisterNew.setText("")
                }
                else {
                    Toast.makeText(baseContext, "Please fill in necessary fields",
                        Toast.LENGTH_SHORT).show()
                }
            }
            catch(e : Exception) {
                i(e)
            }
        }

        binding.googlButtonSignIn.setOnClickListener {
            signIn()
        }

        //if(auth.currentUser != null) {
        //    FirebaseImageManager.checkStorageForExistingProfilePic(auth.currentUser!!.uid)
        //}

        configureGoogleSignIn()
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser

                    FirebaseImageManager.checkStorageForExistingProfilePic(auth.currentUser!!.uid)

                    val launcherIntent = Intent(this, Home::class.java)
                    listIntentLauncher.launch(launcherIntent)
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    // If sign in fails, display a message to the user.
                    //Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }

    private fun createAccount(email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser

                    FirebaseImageManager.checkStorageForExistingProfilePic(auth.currentUser!!.uid)

                    val launcherIntent = Intent(this, Home::class.java)
                    listIntentLauncher.launch(launcherIntent)
                    //overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                } else {
                    // If sign in fails, display a message to the user.
                    //Log.w(TAG, "createUserWithEmail:failure", task.exception)
                    i(task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    //updateUI(null)
                }
            }
    }

    private fun configureGoogleSignIn() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(application!!.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //googleSignInClient.value = GoogleSignIn.getClient(application!!.applicationContext,gso)
        googleSignInClient = GoogleSignIn.getClient(this, gso)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                //Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                //Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    //Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser

                    FirebaseImageManager.checkStorageForExistingProfilePic(auth.currentUser!!.uid)

                    val launcherIntent = Intent(this, Home::class.java)
                    listIntentLauncher.launch(launcherIntent)

                    //updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    //Log.w(TAG, "signInWithCredential:failure", task.exception)
                    //updateUI(null)
                }
            }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private val listIntentLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { }

}