package com.androiddevs.mvvmnewsapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.androiddevs.mvvmnewsapp.R
import com.androiddevs.mvvmnewsapp.models.Article
import com.androiddevs.mvvmnewsapp.utils.Constants.Companion.NO_IMAGE_URL
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.item_article_preview.view.*

class NewsAdapter: RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {
    inner class ArticleViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){}

    private val differCallback = object : DiffUtil.ItemCallback<Article>() {
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }

    val differ =AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_article_preview,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val article= differ.currentList[position]
        holder.itemView.apply {
            if(article.urlToImage != null){
                Glide.with(this).load(article.urlToImage).into(ivArticleImage)
            }
            else{
                Glide.with(this).load(NO_IMAGE_URL).into(ivArticleImage)
            }
            tvSource.text= article.source?.name
            tvTitle.text= article.title
            tvDescription.text= article.description
            tvPublishedAt.text= (article.publishedAt?.let {it.subSequence(11,16).toString() + " " + it.subSequence(0,10).toString() })
            setOnClickListener {
                article?.let { onItemClickListener?.let { it(article) } }
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((Article)-> Unit)? = null

    fun setOnItemClickListener(listener: (Article) -> Unit){
        onItemClickListener = listener
    }
}