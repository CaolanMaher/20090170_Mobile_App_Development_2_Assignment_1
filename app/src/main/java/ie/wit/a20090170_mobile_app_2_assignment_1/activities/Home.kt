package ie.wit.a20090170_mobile_app_2_assignment_1.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import ie.wit.a20090170_mobile_app_2_assignment_1.R
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.HomeBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.NavHeaderBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.firebase.FirebaseImageManager
import ie.wit.a20090170_mobile_app_2_assignment_1.ui.map.GoogleMapsViewModel
import ie.wit.a20090170_mobile_app_2_assignment_1.ui.map.MapsViewModel
import ie.wit.a20090170_mobile_app_2_assignment_1.utils.checkLocationPermissions
import ie.wit.a20090170_mobile_app_2_assignment_1.utils.customTransformation
import ie.wit.a20090170_mobile_app_2_assignment_1.utils.isPermissionGranted
import timber.log.Timber

class Home : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding : HomeBinding
    private lateinit var navHeaderBinding : NavHeaderBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var headerView : View

    private lateinit var auth: FirebaseAuth
    lateinit var googleSignInClient: GoogleSignInClient

    private val googleMapsViewModel : GoogleMapsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        auth = Firebase.auth

        homeBinding = HomeBinding.inflate(layoutInflater)
        setContentView(homeBinding.root)
        drawerLayout = homeBinding.drawerLayout
        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        //val fab: FloatingActionButton = findViewById(R.id.fab)
        //fab.setOnClickListener { view ->
        //    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
        //            .setAction("Action", null).show()
        //}

        val navHostFragment = supportFragmentManager.
        findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.

        appBarConfiguration = AppBarConfiguration(setOf(
            //R.id.donateFragment, R.id.reportFragment, R.id.aboutFragment
            R.id.campaignFragment, R.id.questFragment, R.id.aboutFragment, R.id.googleMapsFragment
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView = homeBinding.navView
        navView.setupWithNavController(navController)

        initNavHeader()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(application!!.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        //googleSignInClient.value = GoogleSignIn.getClient(application!!.applicationContext,gso)
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        if(checkLocationPermissions(this)) {
            googleMapsViewModel.updateCurrentLocation()
        }


//        navController.addOnDestinationChangedListener { _, destination, arguments ->
//            when(destination.id) {
//                R.id.reportFragment -> {
//                    val argument = NavArgument.Builder().setDefaultValue(totalDonated).build()
//                    destination.addArgument("totalDonated", argument)
//
//                }
//            }
//        }
    }

    public override fun onStart() {
        super.onStart()

        if(auth.currentUser != null) {
            updateNavHeader(auth.currentUser!!)
            // test
        }
    }

    private fun updateNavHeader(currentUser: FirebaseUser) {
        FirebaseImageManager.imageUri.observe(this) { result ->
            if (result == Uri.EMPTY) {
                Timber.i("DX NO Existing imageUri")
                if (currentUser.photoUrl != null) {
                    //if you're a google user
                    FirebaseImageManager.updateUserImage(
                        currentUser.uid,
                        currentUser.photoUrl,
                        navHeaderBinding.imageView,
                        false
                    )
                } else {
                    Timber.i("DX Loading Existing Default imageUri")
                    FirebaseImageManager.updateDefaultImage(
                        currentUser.uid,
                        R.drawable.ic_launcher_d20,
                        navHeaderBinding.imageView
                    )
                }        } else // load existing image from firebase
            {
                Timber.i("DX Loading Existing imageUri")
                FirebaseImageManager.updateUserImage(
                    currentUser.uid,
                    FirebaseImageManager.imageUri.value,
                    navHeaderBinding.imageView, false
                )
            }    }
        navHeaderBinding.textView.text = currentUser.email
        if(currentUser.displayName != null)
            navHeaderBinding.username.text = currentUser.displayName
    }


    /*
    private fun updateNavHeader(currentUser: FirebaseUser) {
        var headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)
        //navHeaderBinding.username.text = currentUser.displayName
        navHeaderBinding.textView.text = currentUser.email

        if(currentUser.photoUrl != null && currentUser.displayName != null) {
            navHeaderBinding.username.text = currentUser.displayName

            Picasso.get().load(auth.currentUser?.photoUrl)
                .resize(200, 200)
                .transform(customTransformation())
                .centerCrop()
                .into(navHeaderBinding.imageView)
            //.into(navHeaderBinding.navHeaderImage)
        }
    }
     */

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun initNavHeader() {
        Timber.i("DX Init Nav Header")
        headerView = homeBinding.navView.getHeaderView(0)
        navHeaderBinding = NavHeaderBinding.bind(headerView)
    }

    fun signOut(item: MenuItem) {
        auth.signOut()
        googleSignInClient.signOut()

        val launcherIntent = Intent(this, SignInActivity::class.java)
        launcherIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
        listIntentLauncher.launch(launcherIntent)
    }

    private val listIntentLauncher =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (isPermissionGranted(requestCode, grantResults))
            googleMapsViewModel.updateCurrentLocation()
        else {
            // permissions denied, so use a default location
            googleMapsViewModel.currentLocation.value = Location("Default").apply {
                latitude = 52.245696
                longitude = -7.139102
            }
        }
        Timber.i("LOC : %s", googleMapsViewModel.currentLocation.value)
    }

}