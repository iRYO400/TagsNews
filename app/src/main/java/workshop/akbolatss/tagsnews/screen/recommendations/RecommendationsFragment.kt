package workshop.akbolatss.tagsnews.screen.recommendations

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import java.util.Random
import java.util.concurrent.TimeUnit

import javax.inject.Inject

import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.fragment_recommendations.*
import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.application.App
import workshop.akbolatss.tagsnews.di.component.DaggerSourcesComponent
import workshop.akbolatss.tagsnews.di.module.SourcesModule
import workshop.akbolatss.tagsnews.model.dao.RssSource
import workshop.akbolatss.tagsnews.screen.sources.SourcesPresenter
import workshop.akbolatss.tagsnews.screen.sources.SourcesView

/**
 * Fragment to search new RSS sources. Creates in #BoardActivity
 * @see BoardActivity
 */
class RecommendationsFragment : Fragment(), SourcesView, RecommendationsAdapter.RecommendationsListener {

    @Inject
    lateinit var mContext: Context

    @Inject
    lateinit var mPresenter: SourcesPresenter

    private var mAdapter: RecommendationsAdapter? = null
    private var mListener: OnFragmentInteractionListener? = null
    private var searchSubject: BehaviorSubject<String>? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_recommendations, container, false)
    }

    private fun initDagger() {
        DaggerSourcesComponent.builder()
                .appComponent((context!!.applicationContext as App).appComponent)
                .sourcesModule(SourcesModule(this))
                .build()
                .inject(this)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initDagger()
        initRV()
        initEditText()
    }

    private fun initRV() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        recyclerView.isNestedScrollingEnabled = false
        mAdapter = RecommendationsAdapter(this)
        recyclerView.adapter = mAdapter
    }

    /**
     * Init TextInput View
     */
    private fun initEditText() {
        searchSubject = BehaviorSubject.create()
        etQuery.setOnEditorActionListener(TextView.OnEditorActionListener { _, actionId, event ->
            if (actionId != 0 || event.action == KeyEvent.ACTION_DOWN) {
                onSubmitQuery()
                val view = activity!!.findViewById<View>(android.R.id.content)
                if (view != null) {
                    val imm = activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                return@OnEditorActionListener true
            }
            false
        })

        etQuery.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (!s.toString().isEmpty()) {
                    searchSubject!!.onNext(s.toString())
                } else {
                    mAdapter!!.onClearItems()
                }
            }
        })

        val suggestions = resources.getStringArray(R.array.suggestions)
        etQuery.setText(suggestions[Random().nextInt(9)])
        onSubmitQuery()

        //Query for new result after typing within 1 second
        etQuery.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                searchSubject!!.debounce(1000, TimeUnit.MILLISECONDS)
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe { s ->
                            if (s.trim { it <= ' ' }.isNotEmpty()) {
                                mPresenter.searchSources(s)
                                btnSearch.visibility = View.GONE
                                progressBar.visibility = View.VISIBLE
                            }
                        }
            }
        }
    }

    override fun onAddNewSource() {
        //not used in this class
    }

    /**
     * Submit query make search API call
     */
    private fun onSubmitQuery() {
        if (etQuery.text.toString().trim { it <= ' ' }.isNotEmpty()) {
            mPresenter.searchSources(etQuery.text.toString().trim { it <= ' ' })
        }
    }

    /**
     * Load items to RecyclerView.Adapter
     */
    override fun onLoadSources(rssSourceList: List<RssSource>) {
        mAdapter!!.onAddItems(rssSourceList)
    }

    override fun onNoContent(isEmpty: Boolean) {
        if (isEmpty) {
            recyclerView.visibility = View.GONE
            tvNoContent.visibility = View.VISIBLE
        } else {
            recyclerView.visibility = View.VISIBLE
            tvNoContent.visibility = View.GONE
        }
    }

    /**
     * Adds/Removes RSS source from DB
     * @param rssSource to save or delete
     */
    override fun onSourceClick(rssSource: RssSource, toDelete: Boolean) {
        if (toDelete) {
            mPresenter.removeSourceByLink(rssSource)
        } else {
            mPresenter.addNewSource(rssSource)
        }
        mListener!!.onUpdateRSS()
    }

    /**
     * Check in Database if exists
     */
    override fun checkIfExists(rssSource: RssSource): Boolean {
        return mPresenter.checkInSources(rssSource)
    }

    /**
     * Show loading state
     */
    override fun onShowLoading() {
        btnSearch.visibility = View.GONE
        progressBar.visibility = View.VISIBLE
    }

    /**
     * Hide loading state
     */
    override fun onHideLoading() {
        btnSearch.visibility = View.VISIBLE
        progressBar.visibility = View.GONE
    }

    /**
     * Attach listener
     */
    override fun onAttach(context: Context?) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            mListener = context
        } else {
            throw RuntimeException(context!!.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    /**
     * Detach listener
     */
    override fun onDetach() {
        super.onDetach()
        mListener = null
    }

    override fun onUnknownError(errorMessage: String) {
        Toast.makeText(mContext, R.string.unknown_error, Toast.LENGTH_LONG).show()
    }

    override fun onTimeout() {
        Toast.makeText(mContext, R.string.timeout_error, Toast.LENGTH_LONG).show()
    }

    override fun onNetworkError() {
        Toast.makeText(mContext, R.string.network_error, Toast.LENGTH_LONG).show()
    }

    interface OnFragmentInteractionListener {
        fun onUpdateRSS()
    }
}
