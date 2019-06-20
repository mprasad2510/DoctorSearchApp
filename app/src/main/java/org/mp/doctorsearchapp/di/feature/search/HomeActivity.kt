package org.mp.doctorsearchapp.di.feature.search

import android.Manifest
import android.app.Activity
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.support.multidex.MultiDex
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AlertDialog
import android.support.v7.widget.DefaultItemAnimator
import dagger.android.AndroidInjector
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import javax.inject.Inject
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.OnSuccessListener
import com.jakewharton.rxbinding2.widget.RxSearchView
import dagger.android.HasActivityInjector
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.progressBar
import org.mp.doctorsearchapp.R
import org.mp.doctorsearchapp.di.base.BaseActivity
import org.mp.doctorsearchapp.di.mvibase.MviView
import org.mp.doctorsearchapp.utils.PaginationScrollListener
import org.mp.doctorsearchapp.utils.gone
import org.mp.doctorsearchapp.utils.visible
import java.util.concurrent.TimeUnit


class HomeActivity : BaseActivity(),
    MviView<HomeIntent, HomeViewState>,
    HasActivityInjector,
    GoogleApiClient.ConnectionCallbacks,
    GoogleApiClient.OnConnectionFailedListener,
    com.google.android.gms.location.LocationListener {

    //To get current location
    private lateinit var mGoogleApiClient: GoogleApiClient
    private var mLocationManager: LocationManager? = null
    lateinit var mLocation: Location
    private lateinit var locationManager: LocationManager
    private val TAG = "HomeActivity"
    internal lateinit var linearLayoutManager: LinearLayoutManager

    //Paginating the list
    private val PAGE_START = 1
    private var isLoading = false
    private var isLastPage = false
    private var TOTAL_PAGES = 0
    private var currentPage = PAGE_START

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MultiDex.install(this@HomeActivity)
        mGoogleApiClient = GoogleApiClient.Builder(this@HomeActivity)
            .addConnectionCallbacks(this@HomeActivity)
            .addOnConnectionFailedListener(this@HomeActivity)
            .addApi(LocationServices.API)
            .build()

        mLocationManager = this.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        checkLocation()
        mAccessToken = intent.getStringExtra("accessToken")
         Log.d("****ACCESSTOKEN****", "$mAccessToken")

    }

    override fun onStart() {
        super.onStart()
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect()
        }
    }

    override fun onStop() {
        super.onStop()
        if (mGoogleApiClient.isConnected) {
            mGoogleApiClient.disconnect()
        }
    }

    override fun bind() {
        viewModel.processIntents(intents())
        viewModel.states().observe(this,
            Observer {
                if (it != null)
                    render(it)
            }
        )
        linearLayoutManager = LinearLayoutManager(this)
        list_doctor.layoutManager = linearLayoutManager
        list_doctor.itemAnimator = DefaultItemAnimator()
        searchView.setOnQueryTextListener(object : android.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextChange(newText: String): Boolean {
                searchIntent.onNext(
                    HomeIntent.SearchIntent(
                        query = newText
                    )
                )
                list_doctor.adapter?.notifyDataSetChanged()
                return true
            }

            override fun onQueryTextSubmit(query: String): Boolean {
                return false
            }

        })
        disposable.add(
            RxSearchView.queryTextChanges(searchView).debounce(
                1,
                TimeUnit.MILLISECONDS
            ).subscribe { text -> searchIntent.onNext(HomeIntent.SearchIntent(text.toString())) })

        list_doctor.addOnScrollListener(object : PaginationScrollListener(linearLayoutManager) {
            override fun loadMoreItems() {
                this@HomeActivity.isLoading = true
                currentPage += 1

                loadNextPage()
            }

            override fun getTotalPageCount(): Int {
                return TOTAL_PAGES
            }

            override fun isLastPage(): Boolean {
                return this@HomeActivity.isLastPage
                // Log.d(TAG, this@MainActivity.isLastPage.toString())
            }

            override fun isLoading(): Boolean {
                return this@HomeActivity.isLoading
                //  Log.d(TAG, "$isLoading")
            }

        })

        loadFirstPage()
    }

    private fun loadNextPage() {
        Log.d(TAG, "loadNextPage: $currentPage")
        viewModel.processIntents(intents())
        viewModel.states().observe(this,
            Observer {
                if (it != null)
                    render(it)
            }
        )
    }

    private fun loadFirstPage() {
//        viewModel.processIntents(intents())
//        viewModel.states().observe(this,
//            Observer {
//                if (it != null)
//                    render(it)
//            }
//        )
    }

    override fun layoutId(): Int = R.layout.activity_main

    @Inject
    lateinit var factory: HomeViewmodelFactory

    private val clickIntent = PublishSubject.create<HomeIntent.ClickIntent>()

    private val searchIntent = PublishSubject.create<HomeIntent.SearchIntent>()

   // private val loadImageIntent = PublishSubject.create<HomeIntent.LoadImageIntent>()

    private val viewModel: HomeViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProviders.of(this, factory).get(HomeViewModel::class.java)
    }

    private fun initialIntent(): Observable<HomeIntent.InitialIntent> {
        return Observable.just(HomeIntent.InitialIntent)
    }

    override fun intents(): Observable<HomeIntent> {
        return Observable.merge(initialIntent(), searchIntent, clickIntent)
    }

    override fun render(state: HomeViewState) {
        with(state) {
            if (isLoading) {
                progressBar.visible()
            } else {
                progressBar.gone()
            }

            if (!doctors.isEmpty()) {
                list_doctor.adapter = DoctorListAdapter(this@HomeActivity,doctors,
                    { clickItem -> clickIntent.onNext(HomeIntent.ClickIntent(clickItem)) })
            }
        }
    }

    //static variables
    companion object {
        var lat: Double = 0.0
        var lng: Double = 0.0
        var mAccessToken: String = ""
    }

    override fun onConnected(p0: Bundle?) {
        //Check Permission from User to enable the location
        if (ActivityCompat.checkSelfPermission(this@HomeActivity, Manifest.permission.ACCESS_FINE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(this@HomeActivity, Manifest.permission.ACCESS_COARSE_LOCATION) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        val fusedLocationProviderClient:
                FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@HomeActivity)
        fusedLocationProviderClient.getLastLocation()
            .addOnSuccessListener(this, OnSuccessListener<Location> { location ->
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    // Logic to handle location object
                    mLocation = location
                    lat = mLocation.latitude
                    Log.d("***LOCATION***", "$lat")
                    lng = mLocation.longitude
                    Log.d("***LOCATION1***", "$lng")

                }
            })
    }

    override fun onConnectionSuspended(p0: Int) {
        Log.i(TAG, "Connection Suspended")
        mGoogleApiClient.connect()
    }

    override fun onConnectionFailed(connectionResult: ConnectionResult) {
        Log.i(TAG, "Connection failed. Error: " + connectionResult.getErrorCode())

    }

    override fun onLocationChanged(location: Location) {
        var msg = "Updated Location: Latitude " + location.longitude.toString() + location.longitude
        lat = location.latitude
        lng = location.longitude

    }

    private fun checkLocation(): Boolean {
        if (!isLocationEnabled())
            showAlert()
        return isLocationEnabled()
    }

    private fun isLocationEnabled(): Boolean {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun showAlert() {
        val dialog = AlertDialog.Builder(this)
        dialog.setTitle("Enable Location")
            .setMessage("Your Locations Settings is set to 'Off'.\nPlease Enable Location to " + "use this app")
            .setPositiveButton("Location Settings", DialogInterface.OnClickListener { paramDialogInterface, paramInt ->
                val myIntent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(myIntent)
            })
            .setNegativeButton("Cancel", DialogInterface.OnClickListener { paramDialogInterface, paramInt -> })
        dialog.show()
    }


    override fun activityInjector(): AndroidInjector<Activity> {
        return injector
    }
}
