package com.lelestacia.lelenimexml.feature.anime.ui.bottom_sheet_menu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.viewbinding.library.fragment.viewBinding
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.lelestacia.lelenimexml.feature.anime.R
import com.lelestacia.lelenimexml.feature.anime.databinding.BottomSheetMenuBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MenuBottomSheet :
    BottomSheetDialogFragment(R.layout.bottom_sheet_menu),
    View.OnClickListener {

    private val args by navArgs<MenuBottomSheetArgs>()
    private val binding: BottomSheetMenuBinding by viewBinding()
    private val viewModel: MenuBottomSheetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val malID = args.anime.animeID

        binding.apply {
            viewModel
                .getAnimeNewestData(malID)
                .asLiveData()
                .observe(viewLifecycleOwner) { anime ->
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
                            ContextCompat.getDrawable(requireContext(), R.drawable.ic_favorite),
                            null,
                            null,
                            null
                        )
                    } else {
                        btnAddRemoveFavorite.setCompoundDrawablesWithIntrinsicBounds(
                            ContextCompat.getDrawable(
                                requireContext(),
                                R.drawable.ic_favorite_hollow
                            ),
                            null,
                            null,
                            null
                        )
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
                    viewModel.insertOrUpdateAnime(anime).join()
                    val action = MenuBottomSheetDirections.popupToDetail(anime.animeID)
                    findNavController().navigate(action)
                }
            }

            binding.btnAddRemoveFavorite.id -> {
                viewModel.updateAnimeFavorite(args.anime.animeID)
                requireActivity().onBackPressedDispatcher
            }

            binding.btnShare.id -> {
                val malID = args.anime.animeID
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
