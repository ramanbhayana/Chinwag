package com.app.chinwag.view

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.app.chinwag.commonUtils.common.CommonUtils
import com.app.chinwag.dagger.components.ActivityComponent
import com.app.chinwag.mvvm.BaseActivity
import com.app.chinwag.viewModel.MainActivityViewModel
import com.app.chinwag.R
import com.app.chinwag.commonUtils.utility.IConstants
import com.app.chinwag.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_form_fill.*


@Suppress("DEPRECATION")
class FormFillActivity : BaseActivity<MainActivityViewModel>() {
    var bitmapMain: Bitmap? = null
    var dataBinding: ActivityMainBinding? = null


    override fun setDataBindingLayout() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_form_fill)
        dataBinding?.mainActivityViewModel = viewModel
        dataBinding?.lifecycleOwner = this
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)

    }

    override fun setupView(savedInstanceState: Bundle?) {
        setFireBaseAnalyticsData("id-formFillScreen","view_formFillScreen","view_formFillScreen")
        btn_sign.setOnClickListener {
            logger.dumpCustomEvent(IConstants.EVENT_CLICK,"Sign Button Click")
            val intent = Intent(this, FormActivity::class.java)
            startActivity(intent)
        }
        img_pick_btn.setOnClickListener {
            logger.dumpCustomEvent(IConstants.EVENT_CLICK,"Pick Image Button Click")
            //check runtime permission
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) ==
                    PackageManager.PERMISSION_DENIED
                ) {
                    //permission denied
                    val permissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE);
                    //show popup to request runtime permission
                    requestPermissions(permissions, PERMISSION_CODE);
                } else {
                    //permission already granted
                    pickImageFromGallery();
                }
            } else {
                //system OS is < Marshmallow
                pickImageFromGallery();
            }
        }
    }

    private fun pickImageFromGallery() {
        //Intent to pick image
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, IMAGE_PICK_CODE)
    }

    companion object {
        //image pick code
        private val IMAGE_PICK_CODE = 1000;

        //Permission code
        private val PERMISSION_CODE = 1001;
    }

    //handle requested permission result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSION_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    //permission from popup granted
                    pickImageFromGallery()
                } else {
                    //permission from popup denied
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //handle result of picked image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == IMAGE_PICK_CODE) {
            if (intent != null) {
                // Get the URI of the selected file
                var uri = data?.data
                useImage(uri)


            }
            ivBoarding.setImageURI(data?.data)
        }
    }

    override fun onResume() {
        super.onResume()
        if (bitmapMain !== null) {
            if(CommonUtils.bitmap!= null)
            {
                ivBoarding.setImageBitmap(overlay(bitmapMain!!, CommonUtils.bitmap!!))
            }


        }
    }

    fun useImage(uri: Uri?) {
        bitmapMain = MediaStore.Images.Media.getBitmap(this.contentResolver, uri)
        //use the bitmap as you like

    }

     fun overlay (bmp1: Bitmap, bmp2: Bitmap): Bitmap? {
        val bmOverlay =
            Bitmap.createBitmap(bmp1.width, bmp1.height, bmp1.config)
        val canvas = Canvas(bmOverlay)
        canvas.drawBitmap(bmp1, Matrix(), null)
        canvas.drawBitmap(bmp2, 10f,990f, null)
        return bmOverlay
    }
}