package com.coddeaddict.githubrepositories.ui.repodetails

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.coddeaddict.githubrepositories.R
import com.coddeaddict.githubrepositories.constants.Constants
import com.coddeaddict.githubrepositories.databinding.FragmentRepoDetailsBinding
import com.coddeaddict.githubrepositories.model.RepositoryItem
import com.coddeaddict.githubrepositories.viewmodel.repodetails.RepoDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinApiExtension


@KoinApiExtension
class RepoDetailsFragment : Fragment() {
    companion object {
        const val REPOSITORY_ITEM_KEY = "REPOSITORY_ITEM_KEY"
    }

    private lateinit var binding: FragmentRepoDetailsBinding
    private lateinit var repositoryItem: RepositoryItem
    private val viewModel: RepoDetailsViewModel by viewModel()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        repositoryItem = arguments?.getSerializable(REPOSITORY_ITEM_KEY) as RepositoryItem
        setDataIntoFields(repositoryItem)

        setUpButtonsOnClick()
    }

    private fun setUpButtonsOnClick() {
        val repositoryUrl = "${Constants.GITHUB_BASE_URL}/${repositoryItem.full_name}"

        viewOnlineButtonOnClick(repositoryUrl)
        goBackButtonOnClick()
        shareRepoButtonOnClick(repositoryUrl)
    }

    private fun viewOnlineButtonOnClick(repositoryUrl: String) {

        binding.viewOnlineButton.setOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(repositoryUrl)))
        }
    }

    private fun goBackButtonOnClick() {
        binding.goBackButton.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun shareRepoButtonOnClick(repositoryUrl: String) {
        val intent = Intent(Intent.ACTION_SEND)
        intent.apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, repositoryUrl)
        }

        binding.shareRepoButton.setOnClickListener {
            startActivity(Intent.createChooser(intent, "Share Repo"))
        }

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRepoDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    private fun setDataIntoFields(repositoryItem: RepositoryItem) {
        binding.repositoryAuthorName.text = repositoryItem.owner.login
        val starsString = String.format(
            resources.getString(
                R.string.number_of_stars,
                repositoryItem.stargazers_count
            )
        )
        binding.repositoryStarsDetails.text = starsString
        binding.repoTitle.text = repositoryItem.name
        Glide.with(this)
            .load(repositoryItem.owner.avatar_url)
            .into(binding.repositoryImageDetails)
    }

}