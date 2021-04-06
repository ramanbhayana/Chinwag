package com.app.chinwag.view

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.app.chinwag.commonUtils.common.CommonUtils
import com.app.chinwag.dagger.components.ActivityComponent
import com.app.chinwag.mvvm.BaseActivity
import com.app.chinwag.viewModel.MainActivityViewModel
import com.app.chinwag.R
import com.app.chinwag.commonUtils.utility.IConstants
import com.app.chinwag.databinding.ActivityMainBinding
import com.github.gcacace.signaturepad.views.SignaturePad
import kotlinx.android.synthetic.main.activity_form.*


class FormActivity : BaseActivity<MainActivityViewModel>() {
    var dataBinding: ActivityMainBinding? = null
    override fun setDataBindingLayout() {
        dataBinding = DataBindingUtil.setContentView(this, R.layout.activity_form)
        dataBinding?.mainActivityViewModel = viewModel
        dataBinding?.lifecycleOwner = this
    }

    override fun injectDependencies(activityComponent: ActivityComponent) {
        activityComponent.inject(this)
        //disable both buttons at start


    }

    override fun setupView(savedInstanceState: Bundle?) {
        setFireBaseAnalyticsData("id-formScreen","view_formScreen","view_formScreen")
        CommonUtils.bitmap = null
        //disable both buttons at start
        saveButton.isEnabled = false
        clearButton.isEnabled = false

        //change screen orientation to landscape mode

        //change screen orientation to landscape mode
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE

        signaturePad.setOnSignedListener(object : SignaturePad.OnSignedListener {
            override fun onStartSigning() {}
            override fun onSigned() {
                saveButton.isEnabled = true
                clearButton.isEnabled = true
            }

            override fun onClear() {
                saveButton.isEnabled = false
                clearButton.isEnabled = false
            }
        })

        saveButton.setOnClickListener {
            logger.dumpCustomEvent(IConstants.EVENT_CLICK,"Save Button Click")
            CommonUtils.bitmap = signaturePad.transparentSignatureBitmap;
            Toast.makeText(this@FormActivity, "Signature Saved", Toast.LENGTH_SHORT).show()
            finish()

        }
        clearButton.setOnClickListener {
            logger.dumpCustomEvent(IConstants.EVENT_CLICK,"Clear Button Click")
            CommonUtils.bitmap = null
            signaturePad.clear()
        }
    }


}

