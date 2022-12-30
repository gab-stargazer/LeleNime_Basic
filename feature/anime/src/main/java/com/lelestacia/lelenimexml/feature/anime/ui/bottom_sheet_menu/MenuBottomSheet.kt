package com.lelestacia.lelenimexml.feature.anime.ui.bottom_sheet_menu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lelestacia.lelenimexml.feature.anime.R
import com.lelestacia.lelenimexml.feature.anime.databinding.BottomSheetMenuBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MenuBottomSheet : BottomSheetDialogFragment(R.layout.bottom_sheet_menu),
    View.OnClickListener {

    private val args by navArgs<MenuBottomSheetArgs>()
    private val binding: BottomSheetMenuBinding by viewBinding()
    private val viewModel: MenuBottomSheetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val malID = args.anime.malID

        binding.apply {
            viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                viewModel.getAnimeNewestData(malID).collect { anime ->
                    if (anime.titleJapanese.isNullOrEmpty())
                        btnToDetail.text = anime.title
                    else
                        btnToDetail.text =
                            getString(R.string.menu_to_detail, anime.title, anime.titleJapanese)

                    btnAddRemoveFavorite.text =
                        if (anime.isFavorite) getString(R.string.remove_from_favorite)
                        else getString(R.string.add_to_favorite)

                    if (anime.isFavorite) {
                        btnAddRemoveFavorite.setCompoundDrawablesWithIntrinsicBounds(
                            ResourcesCompat.getDrawable(resources, R.drawable.ic_favorite, null),
                            null,
                            null,
                            null
                        )
                    } else {
                        btnAddRemoveFavorite.setCompoundDrawablesWithIntrinsicBounds(
                            ResourcesCompat.getDrawable(
                                resources,
                                R.drawable.ic_favorite_hollow,
                                null
                            ),
                            null,
                            null,
                            null
                        )
                    }
                }
            }

            btnToDetail.setOnClickListener(this@MenuBottomSheet)
            btnAddRemoveFavorite.setOnClickListener(this@MenuBottomSheet)
            btnShare.setOnClickListener(this@MenuBottomSheet)
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            binding.btnToDetail.id -> {
                lifecycleScope.launch {
                    val anime = args.anime
                    launch {
                        viewModel.insertOrUpdateAnime(anime)
                    }.join()
                    val action = MenuBottomSheetDirections.popupToDetail(anime.malID)
                    findNavController().navigate(action)
                }
            }

            binding.btnAddRemoveFavorite.id -> {
                viewModel.updateAnimeFavorite(args.anime.malID)
                requireActivity().onBackPressedDispatcher
            }

            binding.btnShare.id -> {
                val malID = args.anime.malID
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, "myanimelist.net/anime/$malID")
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }
        }
    }
}