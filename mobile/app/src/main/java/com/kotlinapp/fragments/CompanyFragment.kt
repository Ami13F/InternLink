package com.kotlinapp.fragments

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
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
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kotlinapp.R
import com.kotlinapp.auth.data.AuthRepository
import com.kotlinapp.auth.data.UserRole
import com.kotlinapp.auth.login.afterTextChanged
import com.kotlinapp.core.Api
import com.kotlinapp.core.AppPreferences
import com.kotlinapp.model.AvatarHolder
import com.kotlinapp.model.Company
import com.kotlinapp.utils.ImageUtils
import com.kotlinapp.utils.ImageUtils.FILE_SELECTED
import com.kotlinapp.utils.ImageUtils.REQUEST_CAMERA
import com.kotlinapp.utils.ImageUtils.galleryIntent
import com.kotlinapp.utils.Permissions
import com.kotlinapp.utils.TAG
import kotlinx.android.synthetic.main.company_profile_fragment.*
import kotlinx.android.synthetic.main.create_company_account_fragment.progress
import java.io.IOException


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

        setupPasswordState()

        saveEditBtn.setOnClickListener {
            Log.v(TAG, "Update Password")
            viewModel.changePassword(
                oldPass = oldPassword.text.toString(),
                newPass = newPassword1.text.toString()
            )
        }

    }

    private fun setupPasswordState(){
        viewModel.passwordState.observe(viewLifecycleOwner, Observer { passState ->
            saveEditBtn.isEnabled = passState.isValid
            if (passState.oldPasswordError != null) {
                oldPassword.error = getString(passState.oldPasswordError!!)
            }
            if (passState.newPassword1Error != null) {
                newPassword1.error = getString(passState.newPassword1Error!!)
            }
            if (passState.newPassword2Error != null) {
                newPassword2.error = getString(passState.newPassword2Error!!)
            }
        })

        oldPassword.afterTextChanged { viewModel.validatePasswords(
            oldPassword.text.toString(),
            newPassword1.text.toString(),
            newPassword2.text.toString()
        )
        }
        newPassword1.afterTextChanged { viewModel.validatePasswords(
            oldPassword.text.toString(),
            newPassword1.text.toString(),
            newPassword2.text.toString()
        )
        }
        newPassword2.afterTextChanged { viewModel.validatePasswords(
            oldPassword.text.toString(),
            newPassword1.text.toString(),
            newPassword2.text.toString()
        )
        }
    }

    private fun setupViewModel() {
//        val score = company!!.score
        val username = AppPreferences.email
//        scoreTotal.text = "Your score: $score"
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
        var avatar = AvatarHolder()
        avatar.data = ImageUtils.bitmapToArray((avatarEdit.drawable as BitmapDrawable).bitmap)
        company!!.avatar = avatar
        //TODO: uncomment
//        viewModel.updateProfile(student!!)
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
