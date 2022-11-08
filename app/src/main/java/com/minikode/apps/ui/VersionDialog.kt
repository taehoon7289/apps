package com.minikode.apps.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.minikode.apps.BuildConfig
import com.minikode.apps.R
import com.minikode.apps.databinding.FragmentVersionDialogBinding
import com.minikode.apps.vo.NotificationInfoVo

class VersionDialog : DialogFragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        isCancelable = true
    }

    lateinit var binding: FragmentVersionDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_version_dialog, container, false)
        initView()
        return binding.root
    }

//    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//        val dialog = super.onCreateDialog(savedInstanceState)
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        return dialog
//    }

    private fun initView() {
        binding.model = notificationInfoVo
        with(binding) {

            textViewServerVersion.text = "최신버전: ${notificationInfoVo.title}"
            textViewAppVersion.text = "현재버전: ${BuildConfig.VERSION_NAME}"
            textViewUpdateComment.text = getString(R.string.version_update_comment)

            buttonConfirm.setOnClickListener {
                confirmCallback(notificationInfoVo)
                dismiss()
            }
            buttonCancel.setOnClickListener {
                cancelCallback()
                dismiss()
            }
        }
    }

    companion object {
        lateinit var notificationInfoVo: NotificationInfoVo
        lateinit var confirmCallback: (NotificationInfoVo) -> Unit
        lateinit var cancelCallback: () -> Unit

        fun show(
            notificationInfoVo: NotificationInfoVo,
            confirmCallback: (NotificationInfoVo) -> Unit,
            cancelCallback: () -> Unit,
            supportFragmentManager: FragmentManager,
        ) {
            val versionDialog = VersionDialog()
            with(versionDialog) {
                this@Companion.notificationInfoVo = notificationInfoVo
                this@Companion.confirmCallback = confirmCallback
                this@Companion.cancelCallback = cancelCallback
                this@with.show(supportFragmentManager, versionDialog.tag)
            }
        }

    }

}