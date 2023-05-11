package ie.wit.a20090170_mobile_app_2_assignment_1.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.*
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import ie.wit.a20090170_mobile_app_2_assignment_1.R
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.HomeBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.databinding.NavHeaderBinding
import ie.wit.a20090170_mobile_app_2_assignment_1.utils.customTransformation

class Home : AppCompatActivity() {

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var homeBinding : HomeBinding
    private lateinit var navHeaderBinding : NavHeaderBinding
    private lateinit var appBarConfiguration: AppBarConfiguration

    private lateinit var auth: FirebaseAuth

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
            R.id.campaignFragment, R.id.questFragment, R.id.aboutFragment
        ), drawerLayout)
        setupActionBarWithNavController(navController, appBarConfiguration)

        val navView = homeBinding.navView
        navView.setupWithNavController(navController)

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
        }
    }

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

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    fun signOut() {

    }
}