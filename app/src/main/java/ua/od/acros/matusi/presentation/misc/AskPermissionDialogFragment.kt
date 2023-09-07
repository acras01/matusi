package ua.od.acros.matusi.presentation.misc

import android.app.Dialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.preference.PreferenceManager
import ua.od.acros.matusi.Constants.Companion.SHOW_PERM_DIALOG
import ua.od.acros.matusi.R
import ua.od.acros.matusi.databinding.FragmentAskPermissionDialogBinding
import ua.od.acros.matusi.presentation.main.activity.MainActivity

class AskPermissionDialogFragment: DialogFragment() {

    private var _binding: FragmentAskPermissionDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = FragmentAskPermissionDialogBinding.inflate(layoutInflater)
        return activity?.let { activity ->
            val builder = AlertDialog.Builder(activity)
            val prefs = PreferenceManager.getDefaultSharedPreferences(activity)
            val editor = prefs.edit()
            with(builder) {
                setView(binding.root)
                setPositiveButton(android.R.string.ok
                ) { _, _ ->
                    editor.putBoolean(SHOW_PERM_DIALOG, binding.cbDoNotAsk.isChecked)
                    if (parentFragment is LocationPermissionManager)
                        (parentFragment as LocationPermissionManager).askForLocationPermission()
                }
                setNegativeButton(android.R.string.cancel
                ) { _, _ ->
                    val check = binding.cbDoNotAsk.isChecked
                    editor.putBoolean(SHOW_PERM_DIALOG, check)
                    if (check)
                        Toast.makeText(activity, R.string.not_functional_check, Toast.LENGTH_LONG).show()
                    else
                        Toast.makeText(activity, R.string.not_functional, Toast.LENGTH_LONG).show()
                }
            }
            editor.apply()
            builder.create()
        } ?: throw IllegalStateException(getString(R.string.null_activity))
    }
}
