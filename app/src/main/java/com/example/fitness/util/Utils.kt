package com.example.fitness.util

import android.Manifest.permission
import android.R
import android.animation.ValueAnimator
import android.app.Activity
import android.app.ActivityManager
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.GradientDrawable
import android.location.Address
import android.location.Geocoder
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.text.Spannable
import android.text.SpannableString
import android.text.style.RelativeSizeSpan
import android.text.style.SuperscriptSpan
import android.util.Base64
import android.util.Log
import android.view.*
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.Transformation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import java.io.ByteArrayOutputStream
import java.math.RoundingMode
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import kotlin.text.Charsets.UTF_8

object Utils {
    private var myCalendar = Calendar.getInstance()
    var display_format2 = SimpleDateFormat("dd/MM/yyyy")
    val STR_RUP = "₹"
    val STR_RS = "Rs."
    private var gson: Gson? = null
    private var isSnackBarShowing = false
    private var msgQueue: Queue<String>? = null
    private val coordinatorLayout: CoordinatorLayout? = null
    var currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
    var currentTime = SimpleDateFormat("hh:mm a", Locale.getDefault()).format(Date())
    val MY_CAMERA_REQUEST_CODE = 100

    fun getGsonParser(): Gson? {
        if (gson == null) {
            val gsonBuilder = GsonBuilder()
            gson = gsonBuilder.create()
        }
        return gson
    }

    fun setStatusBarLollipop(activity: Activity) {
        val window = activity.window
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = activity.resources.getColor(R.color.white)
        }
    }

    fun isDataConnected(context: Context?): Boolean {
        val connectivityManager =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (connectivityManager != null) {
            Log.d("1st : ", "connectivityManager")
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
                Log.d("2nd : ", "connectivityManager")
                val capabilities =
                    connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
                if (capabilities != null) {
                    if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)) {
                        return true
                    } else if (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET)) {
                        return true
                    }
                }
            } else {
                Log.d("3rd : ", "else")
                val activeNetworkInfo = connectivityManager.activeNetworkInfo
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected) {
                    return true
                }
            }
        }
        return false
    }

    fun isWiFiConnection(context: Context?): Boolean {
        val connectMan =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = connectMan.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.type == ConnectivityManager.TYPE_WIFI
    }

    fun hideKeyboard(view: View, context: Context?) {
        val `in` = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        `in`.hideSoftInputFromWindow(view.applicationWindowToken, 0)
    }

    fun getTopActivity(context: Context?): String? {
        val am = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val cn = am.getRunningTasks(1)[0].topActivity
        return cn!!.className
    }

    fun showValueAniamtion(textView: TextView, value: Int) {
        val animator = ValueAnimator.ofInt(0, value)
        animator.duration = 1500
        animator.addUpdateListener { animation ->
            val currency: String = numberFormatSymbols()!!.format(animation.animatedValue)
            textView.text = currency
        }
        animator.start()
    }

    fun numberFormatSymbols(): NumberFormat? {
        val numberFormat = NumberFormat.getCurrencyInstance(Locale("en", "in"))
        numberFormat.roundingMode = RoundingMode.HALF_DOWN
        numberFormat.maximumFractionDigits = 0
        numberFormat.minimumIntegerDigits = 0
        return numberFormat
    }

    fun BitMaptoString(bmp: Bitmap): String? {
        val baos = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 70, baos)
        val imageBytes = baos.toByteArray()
        return Base64.encodeToString(imageBytes, Base64.DEFAULT)
    }

    fun StringToBitMap(encodedString: String?): Bitmap? {
        return try {
            val encodeByte =
                Base64.decode(encodedString, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.size)
        } catch (e: Exception) {
            e.message
            null
        }
    }

    fun generateProgressDialog(context: Context?): Dialog? {
        val pDialog = Dialog(context!!, com.example.fitness.R.style.ProgressTheme)
        pDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view: View =
            LayoutInflater.from(context)
                .inflate(com.example.fitness.R.layout.progress_layout, null)
        pDialog.setContentView(view)
        pDialog.setCancelable(false)
        pDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return pDialog
    }

    fun showMessageOKDialog(
        c: Context?,
        message: String?,
        okListener: DialogInterface.OnClickListener?,
    ) {
        AlertDialog.Builder(c!!)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton("OK", okListener)
            .create()
            .show()
    }

    fun customDialog(
        c: Context?,
        message: String?,
        okText: String?,
        cancelText: String?,
        okListener: DialogInterface.OnClickListener?,
        cancelListener: DialogInterface.OnClickListener?,
    ) {
        AlertDialog.Builder(c!!)
            .setMessage(message)
            .setCancelable(false)
            .setPositiveButton(okText, okListener)
            .setNegativeButton(cancelText, cancelListener)
            .create()
            .show()
    }

    fun timeDifference_Hours(start_time: String?, end_time: String?): Double {
        var hours = 0
        val date1: Date
        val date2: Date
        val sdf: SimpleDateFormat
        sdf = SimpleDateFormat("HH:mm a")
        try {
            date1 = sdf.parse(start_time)
            date2 = sdf.parse(end_time)
            val difference = date2.time - date1.time
            val days = (difference / (24 * 60 * 60 * 1000)).toInt()
            hours = (difference / (60 * 60 * 1000) % 24).toInt()
            val min = (difference / (60 * 1000) % 60).toInt()
            hours = if (hours < 0) -hours else hours
            Log.i("======= Hours", " :: $hours")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return hours.toDouble()
    }

    fun timeDifference_HoursBookings(start_time: String?, end_time: String?): String? {
        var hours = 0
        val date1: Date
        val date2: Date
        val sdf: SimpleDateFormat
        sdf = SimpleDateFormat("HH:mm:ss")
        try {
            date1 = sdf.parse(start_time)
            date2 = sdf.parse(end_time)
            val difference = date2.time - date1.time
            val days = (difference / (24 * 60 * 60 * 1000)).toInt()
            hours = (difference / (60 * 60 * 1000) % 24).toInt()
            val min = (difference / (60 * 1000) % 60).toInt()
            hours = if (hours < 0) -hours else hours
            Log.i("======= Hours", " :: $hours")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return hours.toString()
    }

    fun getAddressFromLatLon(context: Context?, lat: Double, lon: Double): String? {
        val addresses: List<Address>
        var city = ""
        var address: String? = ""
        var postalCode = ""
        var state = ""
        var country: String? = ""
        var knownName: String? = ""
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            addresses = geocoder.getFromLocation(
                lat,
                lon,
                1
            ) // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            address =
                addresses[0].getAddressLine(0) // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses[0].locality
            state = addresses[0].adminArea
            country = addresses[0].countryName
            postalCode = addresses[0].postalCode
            knownName = addresses[0].featureName // Only if available else return NULL
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return if (knownName != null) {
            "$knownName,$city,$state,$postalCode"
        } else {
            "$city,$state,$postalCode"
        }
    }

    fun timeDifference_Minutes(start_time: String?, end_time: String?): Int {
        var hours = 0
        val date1: Date
        val date2: Date
        val sdf: SimpleDateFormat
        sdf = SimpleDateFormat("hh:mm a")
        var min = 0
        try {
            date1 = sdf.parse(start_time)
            date2 = sdf.parse(end_time)
            val difference = date2.time - date1.time
            val days = (difference / (24 * 60 * 60 * 1000)).toInt()
            hours = (difference / (60 * 60 * 1000) % 24).toInt()
            min = (difference / (60 * 1000) % 60).toInt()
            hours = if (hours < 0) -hours else hours
            Log.i("======= Hours", " :: $hours")
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return min
    }

    fun changeStrokeColor(view: TextView, color: Int) {
        val bgShape = view.background as GradientDrawable
        bgShape.setStroke(5, color)
    }

    var COLORS = arrayOf(
        "#445844",
        "#4ec42f",
        "#063ea7",
        "#0cd386",
        "#f8a614",
        "#8f06a7",
        "#5224ff",
        "#3fb2cd",
        "#06a778",
        "#6e6e0e",
        "#15a6e5"
    )

    fun expand(v: View) {
        v.measure(
            ConstraintLayout.LayoutParams.MATCH_PARENT,
            ConstraintLayout.LayoutParams.WRAP_CONTENT
        )
        val targetHeight = v.measuredHeight

        // Older versions of android (pre ApiClient 21) cancel animations for views with a height of 0.
        v.layoutParams.height = 1
        v.visibility = View.VISIBLE
        val a: Animation = object : Animation() {
            override fun applyTransformation(
                interpolatedTime: Float,
                t: Transformation,
            ) {
                v.layoutParams.height =
                    if (interpolatedTime == 1f) LinearLayout.LayoutParams.WRAP_CONTENT else (targetHeight * interpolatedTime).toInt()
                v.requestLayout()
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

        // 1dp/ms
//        a.setDuration(700);
        a.setDuration(((targetHeight / v.context.resources.displayMetrics.density) as Int).toLong())
        v.startAnimation(a)
    }

    fun Encrypt(text: String): String? {
        try {
            val key = "ioewfhjufw48ghgyfubf"
            val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
            val keyBytes = ByteArray(16)
            val b = key.toByteArray(charset("UTF-8"))
            var len = b.size
            if (len > keyBytes.size) len = keyBytes.size
            System.arraycopy(b, 0, keyBytes, 0, len)
            val keySpec =
                SecretKeySpec(keyBytes, "AES")
            val ivSpec =
                IvParameterSpec(keyBytes)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            val results = cipher.doFinal(text.toByteArray(charset("UTF-8")))
            //            BASE64Encoder encoder = new BASE64Encoder();
            return Base64.encodeToString(
                results,
                Base64.DEFAULT
            ) // it returns the result as a String
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun Decrypt(text: String): String? {
        try {
            val key = "ioewfhjufw48ghgyfubf"
            val cipher =
                Cipher.getInstance("AES/CBC/PKCS5Padding") //this parameters should not be changed
            val keyBytes = ByteArray(16)
            val b = key.toByteArray(charset("UTF-8"))
            var len = b.size
            if (len > keyBytes.size) len = keyBytes.size
            System.arraycopy(b, 0, keyBytes, 0, len)
            val keySpec = SecretKeySpec(keyBytes, "AES")
            val ivSpec = IvParameterSpec(keyBytes)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            var results: ByteArray? = ByteArray(text.length)
            //            BASE64Decoder decoder = new BASE64Decoder();
            try {
                results = cipher.doFinal(Base64.decode(text, Base64.DEFAULT))
            } catch (e: Exception) {
                Log.i("Erron in Decryption", e.toString())
            }
            Log.i("Data", String(results!!, UTF_8))
            return String(results, UTF_8) // it returns the result as a String
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }

    fun collapse(v: View) {
        val initialHeight = v.measuredHeight
        val a: Animation = object : Animation() {
            override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                if (interpolatedTime == 1f) {
                    v.visibility = View.GONE
                } else {
                    v.layoutParams.height =
                        initialHeight - (initialHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }
            }

            override fun willChangeBounds(): Boolean {
                return true
            }
        }

//        a.setDuration(700);
        a.setDuration(((initialHeight / v.context.resources.displayMetrics.density) as Int).toLong())
        v.startAnimation(a)
    }

    fun arrayAdpter(context: Context?, list: List<String?>?): ArrayAdapter<String?> {
        val adapter: ArrayAdapter<String?> =
            object : ArrayAdapter<String?>(
                context!!,
                com.example.fitness.R.layout.spinner_item,
                list!!
            ) {
                override fun isEnabled(position: Int): Boolean {
                    return if (position == 0) false else true
                }

                override fun getDropDownView(
                    position: Int, convertView: View,
                    parent: ViewGroup,
                ): View {
                    // TODO Auto-generated method stub
                    val mView =
                        super.getDropDownView(position, convertView, parent)
                    val mTextView = mView as TextView
                    if (position == 0) {
                        mTextView.setTextColor(Color.parseColor("#adadad"))
                    } else {
                        mTextView.setTextColor(Color.BLACK)
                    }
                    return mView
                }
            }
        adapter.setDropDownViewResource(com.example.fitness.R.layout.spinner_item)
        return adapter
    }

    fun setBackground(view: View) {
        view.setBackgroundColor(Color.BLACK)
    }

    fun selectDate(view: View) {
        myCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            view.context, datePicker2(view), myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.show()
    }

    fun datePicker(context: Context?, view: View) {
        myCalendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            context!!, datePicker2(view), myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        )
        datePickerDialog.datePicker.calendarViewShown = false
        val c = Calendar.getInstance()
        c[c[Calendar.YEAR] - 18, c[Calendar.MONTH]] = c[Calendar.DAY_OF_MONTH]
        datePickerDialog.datePicker.maxDate = c.timeInMillis
        datePickerDialog.show()
    }

    private fun datePicker2(view1: View): OnDateSetListener? {
        return OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            if (view1 is TextView) {
                view1.text = display_format2.format(myCalendar.time)
            } else if (view1 is EditText) {
                view1.setText(display_format2.format(myCalendar.time))
            }
        }
    }

/*    public static String getAuthToken(String userId, String PWD) {
        byte[] data = new byte[0];
        try {
            Log.d("userId", Utils.Encrypt(userId));
            Log.d("password", Utils.Encrypt(PWD));
            data = (Utils.Encrypt(userId) + ":" + Utils.Encrypt(PWD) + ":3").getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);
//        return "Basic bnFvS1NHNW96Q1VEWk1XQml2SWsrQT09OmQ3R2dFam9HaFpuSUlVRVR4aVAxMFE9PToz";
    }*/

/*    public static void notAthorized(Activity activity, String msg) {
        if (msg != null && msg.equalsIgnoreCase("Not Autherised")) {
            Toast.makeText(activity, "Session Expired.", Toast.LENGTH_SHORT).show();
            SettingsMy.setActiveUser(null);
            activity.startActivity(new Intent(activity, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            activity.finish();
        }
    }*/

    /*    public static String getAuthToken(String userId, String PWD) {
        byte[] data = new byte[0];
        try {
            Log.d("userId", Utils.Encrypt(userId));
            Log.d("password", Utils.Encrypt(PWD));
            data = (Utils.Encrypt(userId) + ":" + Utils.Encrypt(PWD) + ":3").getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return "Basic " + Base64.encodeToString(data, Base64.NO_WRAP);
//        return "Basic bnFvS1NHNW96Q1VEWk1XQml2SWsrQT09OmQ3R2dFam9HaFpuSUlVRVR4aVAxMFE9PToz";
    }*/
    /*    public static void notAthorized(Activity activity, String msg) {
        if (msg != null && msg.equalsIgnoreCase("Not Autherised")) {
            Toast.makeText(activity, "Session Expired.", Toast.LENGTH_SHORT).show();
            SettingsMy.setActiveUser(null);
            activity.startActivity(new Intent(activity, LoginActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            activity.finish();
        }
    }*/
    fun showBlinkingAnimation(
        v: View,
        duration: Int,
        repeateCount: Int,
    ) {
        val anim: Animation = AlphaAnimation(0.0f, 1.0f)
        anim.duration = duration.toLong() //You can manage the blinking time with this parameter
        anim.startOffset = 20
        anim.repeatMode = Animation.REVERSE
        anim.repeatCount = repeateCount
        v.startAnimation(anim)
    }

    fun showToast(context: Context?, msg: String?) {
        val toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT)
        toast.setGravity(Gravity.CENTER, 0, 0)
        toast.show()
    }

    fun getRandom(): String? {
        val number: MutableList<String> =
            ArrayList()
        for (i in 0..20) {
            number.add(Random().nextInt(9).toString())
        }
        val sb = StringBuilder()
        for (s in number) {
            sb.append(s)
        }
        return sb.toString()
    }

/*    public static void cancelProcess(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Are you sure you want to cancel the process ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.startActivity(new Intent(activity, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                activity.finish();
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog alert = builder.create();
        alert.show();
    }*/

    /*    public static void cancelProcess(final Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setMessage("Are you sure you want to cancel the process ?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.startActivity(new Intent(activity, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK));
                activity.finish();
            }
        });
        builder.setNegativeButton("No", null);
        AlertDialog alert = builder.create();
        alert.show();
    }*/

    fun setSpannableSuperscript(
        txt_view: TextView,
        str: String?,
        start: Int,
        end: Int,
    ) {
        try {
            val str1 = txt_view.text.toString()
            var startPos = 0
            var endPos = 0
            if (str1.contains(STR_RUP)) {
                startPos = str1.indexOf(STR_RUP)
                endPos = str1.indexOf(STR_RUP) + 1
            } else if (str1.contains(STR_RS)) {
                startPos = str1.indexOf("R")
                endPos = str1.indexOf(".") + 1
            }
            val ss1 = SpannableString(str1)
            ss1.setSpan(
                RelativeSizeSpan(0.75f),
                startPos,
                endPos,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
            ss1.setSpan(SuperscriptSpan(), startPos, endPos, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            txt_view.text = ss1
        } catch (e: Exception) {
            e.printStackTrace()
            e.message?.let { Log.e("ERROR", it) }
        }
    }

    @Synchronized
    fun showSnackBar(activity: Activity?, msg: String) {
        if (null != activity) {
            if (msgQueue == null) {
                msgQueue = LinkedList()
            }
            msgQueue!!.add(msg)
            showSnackBar(activity)
        }
    }

    fun showSnackBar(activity: Activity) {
        if (!isSnackBarShowing) {
            val sb = Snackbar.make(
                activity.findViewById(R.id.content),
                msgQueue!!.poll(),
                Snackbar.LENGTH_LONG
            ).setCallback(object : Snackbar.Callback() {
                override fun onDismissed(snackbar: Snackbar, event: Int) {
                    super.onDismissed(snackbar, event)
                    isSnackBarShowing = false
                    if (msgQueue!!.size > 0) {
                        showSnackBar(activity)
                    }
                }
            })
            val sbView = sb.view
            //set background color
            // sbView.setBackgroundColor(getResources().getColor(R.color.custom_bg));
            //Get the textview of the snackbar text
            val textView =
                sbView.findViewById<TextView>(com.google.android.material.R.id.snackbar_text)
            //set text color
            textView.setTextColor(activity.resources.getColor(R.color.white))
            //increase max lines of text in snackbar. default is 2.
            textView.maxLines = 10
            sb.show()
            isSnackBarShowing = true
        }
    }


    fun showSnackBarNO_INTERNAET(activity: Activity, str_class_name: String) {
        Snackbar.make(
            activity.findViewById<View>(R.id.content),
            "No internet connection !",
            Snackbar.LENGTH_INDEFINITE
        ).setAction(
            "Retry"
        ) { v: View? ->
            try {
                activity.startActivity(
                    Intent(
                        activity,
                        Class.forName(str_class_name)
                    )
                )
                activity.finish()
            } catch (e: ClassNotFoundException) {
                // Toast.makeText(activity, s + " does not exist yet", Toast.LENGTH_SHORT).show();
            }
        }.show()
    }

    fun checkPermissionCamera(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun requestPermissionCamera(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(permission.CAMERA),
            MY_CAMERA_REQUEST_CODE
        )
    }


    fun getDayState(): String? {
        val c: Calendar = Calendar.getInstance()
        val timeOfDay: Int = c.get(Calendar.HOUR_OF_DAY)
        if (timeOfDay in 0..11) {
            return "Good Morning"
        } else if (timeOfDay in 12..15) {
            return "Good Afternoon"
        } else if (timeOfDay in 16..20) {
            return "Good Evening"
        } else if (timeOfDay in 21..23) {
            return "Good Evening"
        } else {
            return null
        }
    }
}