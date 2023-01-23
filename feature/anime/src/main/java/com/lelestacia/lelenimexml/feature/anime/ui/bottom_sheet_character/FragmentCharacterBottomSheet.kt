package com.lelestacia.lelenimexml.feature.anime.ui.bottom_sheet_character

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.TransitionManager.beginDelayedTransition
import android.transition.TransitionManager.endTransitions
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import coil.load
import coil.transform.RoundedCornersTransformation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import com.lelestacia.lelenimexml.core.common.Constant.UNKNOWN
import com.lelestacia.lelenimexml.core.common.Resource
import com.lelestacia.lelenimexml.core.model.character.CharacterDetail
import com.lelestacia.lelenimexml.feature.anime.R
import com.lelestacia.lelenimexml.feature.anime.databinding.BottomSheetCharacterBinding
import com.lelestacia.lelenimexml.feature.anime.util.ListToString
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FragmentCharacterBottomSheet :
    BottomSheetDialogFragment(R.layout.bottom_sheet_character) {

    private val args: FragmentCharacterBottomSheetArgs by navArgs()
    private val binding: BottomSheetCharacterBinding by viewBinding()
    private val viewModel: CharacterBottomSheetViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getCharacterDetailByID(args.characterId)
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.character.collect { resourceOfCharacter ->
                when (resourceOfCharacter) {
                    is Resource.Error -> {
                        //  Handle UI when data still exist but Error occured
                        val data: CharacterDetail? = resourceOfCharacter.data
                        data?.let { characterDetail ->
                            showData(characterDetail = characterDetail)
                            Snackbar
                                .make(
                                    binding.root,
                                    resourceOfCharacter.message ?: UNKNOWN,
                                    Snackbar.LENGTH_SHORT
                                )
                                .show()
                            return@collect
                        }

                        //  Handle the Error
                        val message = resourceOfCharacter.message ?: UNKNOWN
                        showError(errorMessage = message)
                    }
                    is Resource.Success -> resourceOfCharacter.data?.let { characterDetail ->
                        showData(characterDetail = characterDetail)
                    }
                    else -> {
                        val isLoading = binding.layoutLoading.root.visibility == View.VISIBLE
                        if (isLoading) return@collect
                        reloadUI()
                    }
                }
            }
        }
    }

    private fun showData(characterDetail: CharacterDetail) {
        beginDelayedTransition(binding.root, AutoTransition())
        binding.apply {
            setContentView(characterDetail = characterDetail)
            layoutLoading.root.visibility = View.GONE
            layoutContent.root.visibility = View.VISIBLE
        }
    }

    private fun showError(errorMessage: String) {
        beginDelayedTransition(
            binding.root,
            AutoTransition()
        )
        binding.apply {
            setErrorView(errorMessage = errorMessage)
            layoutError.root.visibility = View.VISIBLE
            layoutLoading.root.visibility = View.GONE
        }
    }

    private fun reloadUI() {
        beginDelayedTransition(
            binding.root,
            AutoTransition()
        )
        binding.apply {
            layoutError.root.visibility = View.GONE
            endTransitions(binding.root)
            beginDelayedTransition(
                binding.root,
                AutoTransition()
            )
            layoutLoading.root.visibility = View.VISIBLE
        }
    }

    private fun BottomSheetCharacterBinding.setContentView(characterDetail: CharacterDetail) {
        layoutContent.apply {

            ivCharacterImage.load(characterDetail.images) {
                transformations(RoundedCornersTransformation(15f))
                build()
            }

            tvCharacterName.text = characterDetail.name
            tvCharacterFavorite.text = characterDetail.favoriteBy.toString()
            ivFavorite.visibility = View.VISIBLE

            val characterRomajiName = characterDetail.characterKanjiName
            with(layoutContent.tvCharacterRomaji) {
                if (characterRomajiName.isEmpty()) {
                    visibility = View.GONE
                } else {
                    text = characterRomajiName
                }
            }

            setNickName(
                characterDetail.characterNickNames,
                characterDetail.characterInformation
            )

            beginDelayedTransition(
                tvCharacterInformation.rootView as ViewGroup,
                AutoTransition()
            )
            tvCharacterInformation.text =
                characterDetail
                    .characterInformation
                    .ifEmpty { getString(R.string.no_information_by_mal) }
        }
    }

    private fun BottomSheetCharacterBinding.setErrorView(errorMessage: String) {
        layoutError.apply {
            val characterID = args.characterId
            tvErrorMessage.text = errorMessage
            btnRetryConnection.setOnClickListener {
                viewModel.getCharacterDetailByID(characterID)
            }
        }
    }

    private fun BottomSheetCharacterBinding.setNickName(
        nickname: List<String>,
        characterInformation: String
    ) {
        layoutContent.apply {
            val nicknameIsNotDifferent = characterInformation
                .contains(nickname.joinToString().toRegex())
            if (nickname.isEmpty() || nicknameIsNotDifferent) {
                tvCharacterNickname.visibility = View.GONE
                return
            }

            tvCharacterNickname.text = ListToString().invoke(nickname)
        }
    }
}
