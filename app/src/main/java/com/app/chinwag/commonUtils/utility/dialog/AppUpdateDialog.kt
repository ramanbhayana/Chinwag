package com.app.chinwag.commonUtils.utility.dialog

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.app.chinwag.R
import com.app.chinwag.databinding.DialogAppUpdateBinding
import com.app.chinwag.dataclasses.VersionConfigResponse
import com.hb.logger.Logger

class AppUpdateDialog(
    private val activity: Activity,
    private val version: VersionConfigResponse
) : DialogFragment() {

    val logger by lazy {
        Logger(this::class.java.simpleName)
    }

    @SuppressLint("ResourceType")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        isCancelable = false
        val binding = DataBindingUtil.inflate<DialogAppUpdateBinding>(
            inflater,
            R.layout.dialog_app_update,
            container,
            false
        )

        binding.tvMessage.text = version.versionCheckMessage
            ?: getString(R.string.label_new_version_available_text)

        binding.tvUpdate.setOnClickListener {
            val appPackageName = activity.packageName
            try {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )
            } catch (anfe: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }
            this.dismiss()
        }

        binding.tvNotNow.setOnClickListener {
            this.dismiss()
        }

        if (version.isOptionalUpdate()) {
            binding.tvNotNow.visibility = View.VISIBLE
        } else {
            binding.tvNotNow.visibility = View.GONE
        }

        return binding.root
    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }
}