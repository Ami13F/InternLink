package com.kotlinapp.auth.account

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.kotlinapp.R
import com.kotlinapp.auth.data.User
import com.kotlinapp.auth.login.afterTextChanged
import com.kotlinapp.fragments.AbstractAccountFragment
import com.kotlinapp.model.AvatarHolder
import com.kotlinapp.model.Company
import com.kotlinapp.utils.ImageUtils
import com.kotlinapp.utils.Permissions
import com.kotlinapp.utils.TAG
import kotlinx.android.synthetic.main.create_company_account_fragment.*
import java.io.IOException

class CreateCompanyAccountFragment: AbstractAccountFragment() {
    private lateinit var viewModel: AccountViewModel

    private var userChoose: String = ""

    private  var avatar: AvatarHolder = AvatarHolder()

    override fun provideFragmentView(
        inflater: LayoutInflater,
        parent: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.create_company_account_fragment, parent, false)
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

        saveCompanyAccountBtn.isEnabled = false

        saveCompanyAccountBtn.setOnClickListener {
            Log.v(TAG, "save item")

            val email = emailField.text.toString()
            val password = passwordField.text.toString()
            val name = nameField.text.toString()
            val description = descriptionField.text.toString()

            avatar.data = ImageUtils.bitmapToArray((avatarImage.drawable as BitmapDrawable).bitmap)

            viewModel.saveAccount( User(email, password),
                Company(name=name, description=description, avatar=avatar)
            )
        }
    }

    private fun setupValidation(){
        viewModel.emailExists.observe(viewLifecycleOwner, Observer {
            if (it){
                viewModel.validateCreateAccount(
                    emailField.text.toString(),
                    passwordField.text.toString(),
                    validateEmail = true)
            }
        })

        viewModel.userNameExists.observe(viewLifecycleOwner, Observer {
            if(it){
                viewModel.validateCreateAccount(
                    emailField.text.toString(),
                    passwordField.text.toString(),
                    validateEmail = false)
            }
        })

        viewModel.validFormState.observe(viewLifecycleOwner, Observer { validState->
            saveCompanyAccountBtn.isEnabled = validState.isDataValid

            if(validState.emailError != null){
                emailField.error = getString(validState.emailError!!)
            }
            if(validState.passwordError != null){
                passwordField.error = getString(validState.passwordError!!)
            }
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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ImageUtils.FILE_SELECTED)
                onSelectFromGalleryResult(data)
            else if (requestCode == ImageUtils.REQUEST_CAMERA)
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
                if (result) ImageUtils.galleryIntent(this)
            } else if (option[item] == "Cancel") {
                dialog.dismiss()
            }
        }
        builder.show()
    }

    private fun cameraIntent(){
        Log.d(TAG,"Starting intent...")

        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, ImageUtils.REQUEST_CAMERA)
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
                    ImageUtils.galleryIntent(this)
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