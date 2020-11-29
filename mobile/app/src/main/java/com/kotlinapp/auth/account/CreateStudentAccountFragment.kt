package com.kotlinapp.auth.account


import android.app.Activity.RESULT_OK
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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kotlinapp.R
import com.kotlinapp.auth.login.afterTextChanged
import com.kotlinapp.fragments.AbstractAccountFragment
import com.kotlinapp.model.AvatarHolder
import com.kotlinapp.utils.TAG
import com.kotlinapp.utils.ImageUtils
import com.kotlinapp.utils.ImageUtils.FILE_SELECTED
import com.kotlinapp.utils.ImageUtils.REQUEST_CAMERA
import com.kotlinapp.utils.ImageUtils.galleryIntent
import com.kotlinapp.utils.Permissions
import kotlinx.android.synthetic.main.create_student_account_fragment.*
import java.io.IOException


class CreateStudentAccountFragment : AbstractAccountFragment() {
    private lateinit var viewModel: AccountViewModel

    private var userChoose: String = ""

    private  var avatar: AvatarHolder = AvatarHolder()

    override fun provideFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.create_student_account_fragment, parent, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.v(TAG, "onActivityCreated")
        viewModel = ViewModelProvider(this).get(AccountViewModel::class.java)

        setupViewModel()
        setupValidation()

        avatarImage.setOnClickListener{
            avatarChooser()
        }

        saveStudentAccountBtn.isEnabled = false

        saveStudentAccountBtn.setOnClickListener {
            Log.v(TAG, "save item")

            val email = emailField.text.toString()
            val username = nameField.text.toString()
            val country = countryField.selectedCountryName + "-" + countryField.selectedCountryNameCode

            val password = passwordField.text.toString()

            avatar.data = ImageUtils.bitmapToArray((avatarImage.drawable as BitmapDrawable).bitmap)

            //TODO: save accout
//            viewModel.saveAccount( User(email, password),
//                Company(avatar, country)
//            )
        }
    }

    private fun setupValidation(){
        viewModel.emailExists.observe(viewLifecycleOwner, Observer {
            if (it){
                viewModel.validateCreateAccount(
                    emailField.text.toString(),
                    passwordField.text.toString(),
                    validateEmail = true
                )
            }
        })

        viewModel.userNameExists.observe(viewLifecycleOwner, Observer {
            if(it){
                viewModel.validateCreateAccount(
                    emailField.text.toString(),
                    passwordField.text.toString(),
                    validateEmail = false
                )
            }
        })

        viewModel.validFormState.observe(viewLifecycleOwner, Observer { validState->
            //TODO: uncomment
//            saveAccountBtn.isEnabled = validState.isDataValid
//
//            if(validState.emailError != null){
//                emailField.error = getString(validState.emailError)
//            }
//            if(validState.usernameError != null){
//                usernameField.error = getString(validState.usernameError)
//            }
//            if(validState.passwordError != null){
//                passwordField.error = getString(validState.passwordError)
//            }
        })

        // If is done typing validate
        emailField.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus){
                Log.d(TAG, "has focus stopped..")
                viewModel.validateCreateAccount(
                    emailField.text.toString(),
                    passwordField.text.toString(),
                    validateEmail = true
                )
            }
        }

        passwordField.afterTextChanged {
            viewModel.validateCreateAccount(
                emailField.text.toString(),
                passwordField.text.toString(),
                validateEmail = false
            )
        }
    }

    private fun setupViewModel() {
        viewModel.completed.observe(viewLifecycleOwner, Observer { completed ->
            if (completed) {
                Log.v(TAG, "Completed, navigate back")
                Toast.makeText(this.activity, "Create account succeed", Toast.LENGTH_SHORT).show()
                findNavController().popBackStack()
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK) {
            if (requestCode == FILE_SELECTED)
                onSelectFromGalleryResult(data)
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data!!)
        }
    }


    private fun avatarChooser() {
        val option = arrayOf<CharSequence>(
            "Choose from Gallery", "Take a Photo", "Cancel"
        )
        val builder: AlertDialog.Builder = AlertDialog.Builder(this.activity)
        builder.setTitle("Choose Photo from...")
        builder.setItems(option) { dialog, item ->
            val result: Boolean = Permissions.checkPermission(this.activity)
            if (option[item] == "Take a Photo") {
                userChoose = "Take a Photo"
                if (result) cameraIntent()
            } else if (option[item] == "Choose from Gallery") {
                userChoose = "Choose from Gallery"
                if (result) galleryIntent(this)
            } else if (option[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun cameraIntent(){
        Log.d(TAG,"Starting intent...")

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
                Toast.makeText(this.context, "Something went wrong with permissions", Toast.LENGTH_LONG).show()
            }
    }

    private fun onCaptureImageResult(data: Intent){
        Log.d(TAG, "start set bitmap")
        val bitmap = data.extras!!["data"] as Bitmap?

//        ImageUtils.saveImageToFile(bitmap!!)
        Log.d(TAG, "set bitmap")
        avatarImage.setImageBitmap(bitmap)
    }


    private fun onSelectFromGalleryResult(data: Intent?){
        var bitmap: Bitmap? = null
        if (data != null) {
            try {
                bitmap = ImageDecoder.decodeBitmap(ImageDecoder.createSource(requireContext().contentResolver, data.data!!))
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
        avatarImage.setImageBitmap(bitmap)
    }
}