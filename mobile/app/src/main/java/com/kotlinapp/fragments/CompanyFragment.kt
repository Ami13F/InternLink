package com.kotlinapp.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kotlinapp.R
import com.kotlinapp.auth.data.AuthRepository
import com.kotlinapp.core.Api
import com.kotlinapp.core.AppPreferences
import com.kotlinapp.model.AvatarHolder
import com.kotlinapp.model.Company
import com.kotlinapp.model.Internship
import com.kotlinapp.utils.ImageUtils
import com.kotlinapp.utils.ImageUtils.FILE_SELECTED
import com.kotlinapp.utils.ImageUtils.REQUEST_CAMERA
import com.kotlinapp.utils.ImageUtils.galleryIntent
import com.kotlinapp.utils.Permissions
import com.kotlinapp.utils.TAG
import kotlinx.android.synthetic.main.company_profile_fragment.*
import kotlinx.android.synthetic.main.create_company_account_fragment.progress
import java.io.IOException
import java.util.*
import android.R.style.*
import android.graphics.Color
import com.hbb20.CountryCodePicker
import kotlinx.android.synthetic.main.company_profile_fragment.avatarEdit
import kotlinx.android.synthetic.main.company_profile_fragment.logoutBtn
import kotlinx.android.synthetic.main.company_profile_fragment.usernameText
import kotlinx.android.synthetic.main.student_profile_fragment.*

@SuppressLint("SetTextI18n")
class CompanyFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel

    private var company: Company? = null

    private var userChoose: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.company_profile_fragment, container, false)
    }

    override fun onResume() {
        super.onResume()
        company = AppPreferences.getCurrentCompanyUser()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        Check if the user is logged

        viewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
        company = AppPreferences.getCurrentCompanyUser()
        Api.tokenInterceptor.token = AppPreferences.token

        logoutBtn.setOnClickListener{
            Log.d(TAG, "Logout")
            AuthRepository.logout()
            findNavController().navigate(R.id.login_fragment)
        }
        setupViewModel()
        Log.d(TAG, "Setting initial values...")
        avatarEdit.setImageBitmap(ImageUtils.arrayToBitmap(company!!.avatar!!.data))

        addInternshipBtn.setOnClickListener {
            showInternshipDialog()
        }

    }

    private lateinit var alertDialog: AlertDialog
    private fun showInternshipDialog() {
        val inflater: LayoutInflater = this.layoutInflater
        val dialogView: View = inflater.inflate(R.layout.internship_dialog, null)

        val title = dialogView.findViewById<EditText>(R.id.titleField).text

        val location = dialogView.findViewById<CountryCodePicker>(R.id.locationField)
        val description = dialogView.findViewById<EditText>(R.id.descriptionField).text
        val startDate = dialogView.findViewById<TextView>(R.id.startDateField)
        val endDate = dialogView.findViewById<TextView>(R.id.endDateField)
        val saveInternshipBtn: Button = dialogView.findViewById(R.id.saveInternshipBtn)
        val closeBtn: Button = dialogView.findViewById(R.id.closeBtn)
        val applyToInternshipBtn: Button = dialogView.findViewById(R.id.applyToInternshipBtn)

        applyToInternshipBtn.isVisible = false

        setCountry(location)
        setDatePicker(startDate)
        setDatePicker(endDate)

        saveInternshipBtn.setOnClickListener {
            viewModel.saveInternship(Internship(null, AppPreferences.currentUserId, title.toString(), isPaid = false , deadline = endDate.text.toString(),
                location = location.selectedCountryName + "-" + location.selectedCountryNameCode, description =  description.toString(), startDate =  startDate.text.toString(), endDate = endDate.text.toString()))
            alertDialog.hide()
        }

        closeBtn.setOnClickListener{
            alertDialog.hide()
        }

        val dialogBuilder: AlertDialog.Builder = AlertDialog.Builder(this.context)
        dialogBuilder.setOnDismissListener { }
        dialogBuilder.setView(dialogView)

        alertDialog = dialogBuilder.create();
        alertDialog.show()
    }

    private fun setCountry(location: CountryCodePicker){
        var country: String
        location.setOnCountryChangeListener{
            country = location.selectedCountryName + "-" + location.selectedCountryNameCode
            Log.d(TAG, "Selected country... $country")
        }
    }

    private fun setDatePicker(date: TextView){
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        date.setOnClickListener {
            val datePickerDialog = DatePickerDialog(this.requireContext(), ThemeOverlay_Material_Dark, DatePickerDialog.OnDateSetListener { _, year, month, day ->
                date.text = "$year-$month-${day}T00:00:00.000Z"
            }, year, month,day)
            datePickerDialog.show()
        }
    }

    private fun setupViewModel() {
        val username = AppPreferences.email
        usernameText.text = "Hello, $username"

        viewModel.companyUpdate.observe(viewLifecycleOwner, Observer { company ->
            avatarEdit.setImageBitmap(ImageUtils.arrayToBitmap(company!!.avatar!!.data))
            this.company = company
            AppPreferences.setCurrentUser(company)
        })

        viewModel.fetching.observe(viewLifecycleOwner, Observer { fetching ->
            Log.v(TAG, "update fetching")
            progress.visibility = if (fetching) View.VISIBLE else View.GONE
        })

        viewModel.fetchingError.observe(viewLifecycleOwner, Observer { exception ->
            if (exception != null) {
                Log.v(TAG, "update fetching error")
                val message = "Fetching exception ${exception.message}"
                val parentActivity = activity?.parent
                if (parentActivity != null) {
                    Toast.makeText(parentActivity, message, Toast.LENGTH_SHORT).show()
                }
            }
        })
        viewModel.completed.observe(viewLifecycleOwner, Observer { completed ->
            if (completed) {
                Log.v(TAG, "completed, navigate back")
                if (this.activity != null) {
                    Log.d(TAG, "Change succeed")
                    Toast.makeText(this.activity, "Password changed!", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }
        })
    }

    private fun setAvatar(){
        Log.d(TAG, "Saving avatar")
        val avatar = AvatarHolder()
        avatar.data = ImageUtils.bitmapToArray((avatarEdit.drawable as BitmapDrawable).bitmap)
        company!!.avatar = avatar
        viewModel.updateCompany(company!!)
        AppPreferences.setCurrentUser(company!!)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FILE_SELECTED)
                onSelectFromGalleryResult(data)
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data!!)
            setAvatar()
        }
    }

    private fun avatarChooser() {
        val types = arrayOf<CharSequence>(
            "Choose from Gallery", "Take a Photo", "Cancel"
        )
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.activity)
        builder.setTitle("Choose Photo from...")
        builder.setItems(types) { dialog, item ->
            val result: Boolean = Permissions.checkPermission(this.activity)
            if (types[item] == "Take a Photo") {
                userChoose = "Take a Photo"
                if (result) cameraIntent()
            } else if (types[item] == "Choose from Gallery") {
                userChoose = "Choose from Gallery"
                if (result) galleryIntent(this)
            } else if (types[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun cameraIntent(){
        Log.d(TAG, "Starting intent...")
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == Permissions.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE)
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                if(userChoose == "Take a Photo")
                    cameraIntent()
                else if(userChoose == "Choose from Gallery")
                    galleryIntent(this)
            } else {
                Toast.makeText(
                    this.context,
                    "Something went wrong with permissions",
                    Toast.LENGTH_LONG
                ).show()
            }
    }
    private fun onCaptureImageResult(data: Intent){
        val bitmap = data.extras!!["data"] as Bitmap?

        avatarEdit.setImageBitmap(bitmap)
        setAvatar()
    }

    private fun onSelectFromGalleryResult(data: Intent?){
        var bitmap: Bitmap? = null
        if (data != null) {
            try {
                bitmap = ImageDecoder.decodeBitmap(
                    ImageDecoder.createSource(
                        requireContext().contentResolver,
                        data.data!!
                    )
                )
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        avatarEdit.setImageBitmap(bitmap)
        setAvatar()
    }
}
