package com.example.fitness.ui.signup

import android.Manifest.permission.CAMERA
import android.app.Activity
import android.app.Dialog
import android.content.ComponentName
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioGroup
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.fitness.R
import com.example.fitness.databinding.ActivitySignUpBinding
import com.example.fitness.ui.login.LoginActivity
import com.example.fitness.ui.login.LoginViewModel
import com.example.fitness.util.Utils
import com.example.fitness.util.Validations
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_sign_up.*
import kotlinx.android.synthetic.main.activity_sign_up.edit_email
import kotlinx.android.synthetic.main.activity_sign_up.edit_pass
import java.io.File
import java.io.IOException
import java.util.*


@AndroidEntryPoint
class SignUpActivity : AppCompatActivity() {

    private lateinit var activity: Activity
    val viewModel: LoginViewModel by viewModels()
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var pDialog: Dialog
    private var gender: String = "male"
    private var motive: String = "fitness"

    //  private var permissionsToRequest: ArrayList<String?> = ArrayList<String?>()
    //  private val permissionsRejected: ArrayList<String?> = ArrayList<String?>()
    //private val permissions: ArrayList<String?> = ArrayList<String?>()
    private val images = arrayOf<String>()
    private val ALL_PERMISSIONS_RESULT = 107
    private val permission = CAMERA

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity = this
        binding = DataBindingUtil.setContentView(this, R.layout.activity_sign_up)
        binding.executePendingBindings()
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }
        pDialog = Utils.generateProgressDialog(activity)!!


        onSignup()

        binding.editName.afterTextChanged {
            Validations.edtValidation(edit_name, "Enter Your Name")
        }
        binding.editMobile.afterTextChanged {
            Validations.mobileValidation(edit_mobile)
        }
        binding.editEmail.afterTextChanged {
            Validations.emailValidation(edit_email)
        }
        binding.editPass.afterTextChanged {
            Validations.edtValidation(edit_pass, "Enter Password")
        }
        binding.editAge.afterTextChanged {
            Validations.edtValidation(edit_age, "Enter Your Age")
        }
        binding.editHeight.afterTextChanged {
            Validations.edtValidation(edit_height, "Enter Your Height")
        }
        binding.editWt.afterTextChanged {
            Validations.edtValidation(edit_wt, "Enter Your Weight in Kg.")
        }
        binding.rbg1.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rbmale -> gender = "male"
                R.id.rbfemale -> gender = "female"
            }
        })
        binding.rbg2.setOnCheckedChangeListener(RadioGroup.OnCheckedChangeListener { radioGroup, i ->
            when (i) {
                R.id.rbfitness -> motive = "fitness"
                R.id.rbwtgain -> motive = "weight gain"
                R.id.rbwtloss -> motive = "weight loss"
            }
        })

        binding.btnUploadPhotos.setOnClickListener {
            checkpermissions()

            binding.llUploadPhotos.visibility = View.VISIBLE
            binding.svSignup.visibility = View.GONE
            btn_signup.text = "Upload"
        }

        binding.imgFront.setOnClickListener {
            ActivityCompat.startActivityForResult(
                activity,
                getPickImageChooserIntent()!!,
                200,
                null
            )
        }

        binding.imgSide.setOnClickListener {
            ActivityCompat.startActivityForResult(
                activity,
                getPickImageChooserIntent()!!,
                201,
                null
            )
        }

        binding.imgBack.setOnClickListener {
            ActivityCompat.startActivityForResult(
                activity,
                getPickImageChooserIntent()!!,
                202,
                null
            )
        }

        binding.btnSignup.setOnClickListener {
            if (btn_signup.text == "Upload") {
                binding.llUploadPhotos.visibility = View.GONE
                binding.svSignup.visibility = View.VISIBLE
                btn_signup.text = "Sign Up"
            } else {
                if (!Validations.edtValidation(edit_name, "Enter Your Name"))
                    return@setOnClickListener
                else if (!Validations.mobileValidation(edit_mobile))
                    return@setOnClickListener
                else if (!Validations.emailValidation(edit_email))
                    return@setOnClickListener
                else if (!Validations.edtValidation(edit_pass, "Enter Password"))
                    return@setOnClickListener
                else if (!Validations.edtValidation(edit_age, "Enter Your Age"))
                    return@setOnClickListener
                else if (!Validations.edtValidation(edit_height, "Enter Your Height"))
                    return@setOnClickListener
                else if (!Validations.edtValidation(edit_wt, "Enter Your Weight in Kg."))
                    return@setOnClickListener
                else
                    signUpUser()
            }
        }
    }

    private fun onSignup() {
        viewModel.isLoginSuccessful.observe(this, Observer { isSuccess ->
            if (isSuccess) {
                startActivity(Intent(activity, LoginActivity::class.java))
                finish()
            }
            pDialog.dismiss()
        })
    }

    private fun signUpUser() {
        if (Utils.isDataConnected(activity)) {
            viewModel.onSignUp(
                binding.editName.text.toString(),
                binding.editMobile.text.toString(),
                binding.editEmail.text.toString(),
                binding.editPass.text.toString(),
                gender,
                binding.editAge.text.toString(),
                binding.editHeight.text.toString(),
                binding.editWt.text.toString(), motive
            )
            pDialog.show()
        } else {
            Utils.showSnackBarNO_INTERNAET(activity, activity.localClassName)
        }
    }

    private fun checkpermissions() {
        //permissions.add(Manifest.permission.CAMERA)

        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        //get the permissions we have asked for before but are not granted..
        //we will store this in a global list to access later.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ActivityCompat.requestPermissions(
                activity, arrayOf(permission),
                ALL_PERMISSIONS_RESULT
            )
        }
    }

    private fun getPickImageChooserIntent(): Intent? {
        // Determine Uri of camera image to save.
        val outputFileUri = getCaptureImageOutputUri()
        val allIntents: MutableList<Intent?> = ArrayList<Intent?>()
        val packageManager: PackageManager = packageManager
        // collect all camera intents
        val captureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        val listCam = packageManager.queryIntentActivities(captureIntent, 0)
        for (res in listCam) {
            val intent = Intent(captureIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(res.activityInfo.packageName)
            /*if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
            }*/
            allIntents.add(intent)
        }
        // collect all gallery intents
        val galleryIntent = Intent(Intent.ACTION_GET_CONTENT)
        galleryIntent.type = "image/*"
        val listGallery = packageManager.queryIntentActivities(galleryIntent, 0)
        for (res in listGallery) {
            val intent = Intent(galleryIntent)
            intent.component = ComponentName(res.activityInfo.packageName, res.activityInfo.name)
            intent.setPackage(res.activityInfo.packageName)
            allIntents.add(intent)
        }
        // the main intent is the last in the list (fucking android) so pickup the useless one
        var mainIntent = allIntents[allIntents.size - 1]
        for (intent in allIntents) {
            if (intent!!.component!!.className == activity.getPackageName()) {
                mainIntent = intent
                break
            }
        }
        allIntents.remove(mainIntent)
        // Create a chooser from the main intent
        val chooserIntent = Intent.createChooser(mainIntent, "Select Image from")
        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toTypedArray())
        return chooserIntent
    }

    @Throws(IOException::class)
    private fun rotateImageIfRequired(img: Bitmap, selectedImage: Uri): Bitmap? {
        val ei = ExifInterface(selectedImage.path!!)
        val orientation =
            ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        return when (orientation) {
            ExifInterface.ORIENTATION_ROTATE_90 -> rotateImage(img, 90)
            ExifInterface.ORIENTATION_ROTATE_180 -> rotateImage(img, 180)
            ExifInterface.ORIENTATION_ROTATE_270 -> rotateImage(img, 270)
            else -> img
        }
    }

    private fun rotateImage(img: Bitmap, degree: Int): Bitmap? {
        val matrix = Matrix()
        matrix.postRotate(degree.toFloat())
        val rotatedImg = Bitmap.createBitmap(img, 0, 0, img.width, img.height, matrix, true)
        img.recycle()
        return rotatedImg
    }

    private fun hasPermission(permission: String): Boolean {
        if (canMakeSmores()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return ActivityCompat.checkSelfPermission(
                    activity,
                    permission
                ) == PackageManager.PERMISSION_GRANTED
            }
        }
        return true
    }

    private fun canMakeSmores(): Boolean {
        return Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                200 -> setImages(img_front, data)
                201 -> setImages(img_side, data)
                202 -> setImages(img_back, data)
            }
        }
    }

    private fun setImages(img: ImageView, data: Intent?) {
        var myBitmap: Bitmap? = null
        val picUri: Uri?
        if (getPickImageResultUri(data) != null) {
            picUri = getPickImageResultUri(data)
            try {
                myBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), picUri)
                //myBitmap = rotateImageIfRequired(myBitmap, picUri!!);
                myBitmap = getResizedBitmap(myBitmap, 500);
                img.setImageBitmap(myBitmap)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        } else {
            myBitmap = data?.extras!!["data"] as Bitmap?
            if (img != null) {
                img.setImageBitmap(myBitmap)
            }
        }
        for (i in images.indices) {
            images[i] = Utils.BitMaptoString(myBitmap!!)!!
        }
    }

    fun getPickImageResultUri(data: Intent?): Uri? {
        var isCamera = true
        if (data != null) {
            val action = data.action
            isCamera = action != null && action == MediaStore.ACTION_IMAGE_CAPTURE
        }
        return if (isCamera) getCaptureImageOutputUri() else data!!.data
    }

    private fun getCaptureImageOutputUri(): Uri? {
        var outputFileUri: Uri? = null
        val getImage: File = externalCacheDir!!
        if (getImage != null) {
            outputFileUri = Uri.fromFile(File(getImage.path, "profile.png"))
        }
        return outputFileUri
    }

    private fun getResizedBitmap(image: Bitmap?, maxSize: Int): Bitmap? {
        var width = image!!.width
        var height = image.height
        val bitmapRatio = width.toFloat() / height.toFloat()
        if (bitmapRatio > 0) {
            width = maxSize
            height = (width / bitmapRatio).toInt()
        } else {
            height = maxSize
            width = (height * bitmapRatio).toInt()
        }
        return Bitmap.createScaledBitmap(image, width, height, true)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            ALL_PERMISSIONS_RESULT -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    //permission granted
                } else {
                    //permission denied
                    Utils.showMessageOKDialog(activity,
                        "These permission is mandatory for the application. Please allow access.",
                        DialogInterface.OnClickListener { dialog, which ->
                            checkpermissions()
                        })
                }
            }
        }
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun afterTextChanged(p0: Editable?) {
            afterTextChanged.invoke(p0.toString())
        }
    })
}