package com.lelestacia.lelenimexml.feature.anime.ui.bottom_sheet_character

import android.os.Bundle
import android.transition.AutoTransition
import android.transition.Fade
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
                        val isContentVisible = binding.layoutContent.root.visibility == View.VISIBLE
                        if (isContentVisible) {
                            beginDelayedTransition(binding.root, Fade())
                            binding.progressCircularUpdate.visibility = View.GONE
                            Snackbar.make(
                                binding.root,
                                resourceOfCharacter.message ?: "",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            return@collect
                        }

                        //  Handle UI when data still exist but Error happened
                        val data: CharacterDetail? = resourceOfCharacter.data
                        data?.let { characterDetail ->
                            setContentView(characterDetail = characterDetail)
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
                        val errorMessage = resourceOfCharacter.message ?: UNKNOWN
                        showError(errorMessage = errorMessage)
                    }
                    is Resource.Success -> resourceOfCharacter.data?.let { characterDetail ->
                        //  Handle the data and display it properly
                        setContentView(characterDetail = characterDetail)
                    }
                    is Resource.Loading -> {
                        val isContentVisible = binding.layoutContent.root.visibility == View.VISIBLE
                        if (isContentVisible) {
                            beginDelayedTransition(binding.root, Fade())
                            binding.progressCircularUpdate.visibility = View.VISIBLE
                            return@collect
                        }

                        //  Start loading at this state
                        beginDelayedTransition(binding.root)
                        binding.layoutLoading.root.visibility = View.VISIBLE
                    }
                    is Resource.None -> Unit
                }
            }
        }
    }

    private fun showError(errorMessage: String) {
        binding.layoutError.apply {
            beginDelayedTransition(
                binding.root,
                AutoTransition()
            )
            beginDelayedTransition(
                binding.layoutLoading.root,
                AutoTransition()
            )
            binding.layoutLoading.root.visibility = View.GONE
            root.visibility = View.VISIBLE
            endTransitions(binding.root)

            beginDelayedTransition(root)
            tvErrorMessage.text = errorMessage
            btnRetryConnection.setOnClickListener {
                beginDelayedTransition(binding.root)
                binding.layoutLoading.root.visibility = View.VISIBLE
                root.visibility = View.GONE
                viewModel.getCharacterDetailByID(characterID = args.characterId)
            }
            endTransitions(root)
        }
    }

    private fun setContentView(characterDetail: CharacterDetail) {

        val isUpdateProgressPresent = binding.progressCircularUpdate.visibility == View.VISIBLE
        if(isUpdateProgressPresent) {
            beginDelayedTransition(binding.root, Fade())
            binding.progressCircularUpdate.visibility = View.GONE
        }

        binding.layoutContent.apply {
            beginDelayedTransition(
                binding.root,
                AutoTransition()
            )
            binding.layoutLoading.root.visibility = View.GONE
            root.visibility = View.VISIBLE
            endTransitions(binding.root)

            beginDelayedTransition(
                root, AutoTransition()
                    .excludeTarget(tvCharacterNickname, true)
            )
            ivCharacterImage.load(characterDetail.images) {
                transformations(RoundedCornersTransformation(15f))
                build()
            }

            tvCharacterName.text = characterDetail.name
            tvCharacterFavorite.text = characterDetail.favoriteBy.toString()
            ivFavorite.visibility = View.VISIBLE

            val characterRomajiName = characterDetail.characterKanjiName
            with(tvCharacterRomaji) {
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
            endTransitions(root)

            val length = characterDetail.characterInformation.length
            val duration: Long =
                if (length > 150) 500
                else 750
            beginDelayedTransition(
                tvCharacterInformation.rootView as ViewGroup,
                AutoTransition().setDuration(duration)
            )
            tvCharacterInformation.text =
                characterDetail
                    .characterInformation
                    .ifEmpty { getString(R.string.no_information_by_mal) }
        }
    }

    private fun setNickName(
        nickname: List<String>,
        characterInformation: String
    ) {
        binding.layoutContent.apply {
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
