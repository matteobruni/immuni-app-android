/*
 * Copyright (C) 2020 Presidenza del Consiglio dei Ministri.
 * Please refer to the AUTHORS file for more information.
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package it.ministerodellasalute.immuni.ui.suggestions

import android.os.Bundle
import android.view.View
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import it.ministerodellasalute.immuni.R
import it.ministerodellasalute.immuni.extensions.view.setSafeOnClickListener
import it.ministerodellasalute.immuni.logic.exposure.ExposureManager
import kotlinx.android.synthetic.main.state_close_dialog.*
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class StateCloseDialogFragment : BaseStateDialogFragment(R.layout.state_close_dialog) {

    override val appBar: AppBarLayout
        get() = requireView().findViewById(R.id.appBar)
    override val backButton: View
        get() = navigationIcon
    override val scrollView: NestedScrollView
        get() = requireView().findViewById(R.id.scrollView)
    override val viewsToFadeInOnScroll: Array<View>
        get() = arrayOf(pageTitle, pageSubtitle)
    override val viewsToFadeOutOnScroll: Array<View>
        get() = arrayOf(toolbarTitle)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imInContactWithASL.setSafeOnClickListener {
            showHideAlert()
        }

        hideNotice.setSafeOnClickListener {
            showHideAlert()
        }
    }

    private fun showHideAlert() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(getString(R.string.contact_hide_confirm_alert_title))
            .setMessage(getString(R.string.contact_hide_confirm_alert_message))
            .setNegativeButton(getString(R.string.no)) { d, _ ->
                d.dismiss()
            }
            .setPositiveButton(getString(R.string.yes)) { d, _ ->
                executeHideNotice()
                d.dismiss()
            }
            .show()
    }

    private fun executeHideNotice() {
        lifecycleScope.launch {
            val exposureManager: ExposureManager by inject()
            exposureManager.resetExposureStatus()

            findNavController().popBackStack()
        }
    }
}
