package workshop.akbolatss.tagsnews.screen.reminders

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PopupMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.widget.TimePicker
import kotlinx.android.synthetic.main.activity_reminders.*
import java.util.Random

import javax.inject.Inject

import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.base.BaseActivity
import workshop.akbolatss.tagsnews.di.component.DaggerRemindersComponent
import workshop.akbolatss.tagsnews.di.module.RemindersModule
import workshop.akbolatss.tagsnews.model.dao.ReminderItem

/**
 * Activity where user can add, edit, remove #Reminders
 * @see ReminderItem
 */
class RemindersActivity : BaseActivity(), RemindersView, RemindersAdapter.ReminderListener {

    @Inject
    lateinit var mPresenter: RemindersPresenter

    private var mAdapter: RemindersAdapter? = null

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        super.onViewReady(savedInstanceState, intent)

        initToolbar()
        initRV()
        initListeners()
        mPresenter.onLoadReminders()
    }

    private fun initToolbar(){
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
    }

    private fun initRV() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        mAdapter = RemindersAdapter(this)
        recyclerView.adapter = mAdapter
    }

    private fun initListeners() {
        fabAdd.setOnClickListener {
            onAddReminder()
        }
    }

    /**
     * Show Reminder options. Edit timing and remove
     */
    override fun onReminderOptions(rItem: ReminderItem, view: View, pos: Int) {
            val popupMenu = PopupMenu(this, view)
            popupMenu.inflate(R.menu.menu_popup_reminder)
            popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.mRetime -> {
                        val layoutInflater = LayoutInflater.from(this@RemindersActivity)
                        val subView = layoutInflater.inflate(R.layout.dialog_timepicker, null)
                        val timePicker = subView.findViewById<TimePicker>(R.id.timePicker)

                        if (Build.VERSION.SDK_INT >= 23) {
                            timePicker.hour = rItem.hour!!
                            timePicker.minute = rItem.minute!!
                        } else {
                            timePicker.currentHour = rItem.hour!!
                            timePicker.currentMinute = rItem.minute!!
                        }

                        val builder = AlertDialog.Builder(this@RemindersActivity, R.style.Dialog)

                        builder.setView(subView)
                        builder.setPositiveButton(R.string.tvAdd) { dialogInterface, i ->
                            //                                    ReminderItem rItem = new ReminderItem();
                            val random = Random()
                            rItem.requestCode = random.nextInt()
                            if (Build.VERSION.SDK_INT >= 23) {
                                rItem.hour = timePicker.hour
                                rItem.minute = timePicker.minute
                                if (timePicker.hour < 12) {
                                    rItem.pM_AM = "AM"
                                } else {
                                    rItem.pM_AM = "PM"
                                }
                            } else {
                                rItem.hour = timePicker.currentHour
                                rItem.minute = timePicker.currentMinute
                                if (timePicker.currentHour < 12) {
                                    rItem.pM_AM = "AM"
                                } else {
                                    rItem.pM_AM = "PM"
                                }
                            }
                            mAdapter!!.updateItem(rItem, pos)
                            mPresenter.onUpdate(rItem)
                            dialogInterface.dismiss()
                        }
                        builder.setNegativeButton(R.string.tvCancel) { dialogInterface, i -> dialogInterface.cancel() }
                        builder.show()
                        return@OnMenuItemClickListener true
                    }
                    R.id.mRemove -> {
                        mAdapter!!.removeItem(rItem, pos)
                        mPresenter.onRemoveReminder(rItem)
                        return@OnMenuItemClickListener true
                    }
                }
                false
            })
            popupMenu.show()
    }

    /**
     * Switch Reminder state
     */
    override fun onReminderSwitch(rItem: ReminderItem, isActive: Boolean, pos: Int) {
        if (isActive) {
            mPresenter.onActivateNotification(rItem, false)
        } else {
            mPresenter.onDeactivateNotification(rItem)
        }
    }

    /**
     * Add new Reminder
     */
    override fun onAddReminder() {
        val layoutInflater = LayoutInflater.from(this@RemindersActivity)
        val subView = layoutInflater.inflate(R.layout.dialog_timepicker, null)
        val timePicker = subView.findViewById<TimePicker>(R.id.timePicker)

        val builder = AlertDialog.Builder(this, R.style.TimePickerDialog)

        builder.setView(subView)
        builder.setPositiveButton(R.string.tvAdd) { dialogInterface, i ->
            val rItem = ReminderItem()
            val random = Random()
            rItem.requestCode = random.nextInt()
            rItem.isActive = true
            if (Build.VERSION.SDK_INT >= 23) {
                rItem.hour = timePicker.hour
                rItem.minute = timePicker.minute
                if (timePicker.hour < 12) {
                    rItem.pM_AM = "AM"
                } else {
                    rItem.pM_AM = "PM"
                }
            } else {
                rItem.hour = timePicker.currentHour
                rItem.minute = timePicker.currentMinute
                if (timePicker.currentHour < 12) {
                    rItem.pM_AM = "AM"
                } else {
                    rItem.pM_AM = "PM"
                }
            }

            mPresenter.onAddReminder(rItem)
            mAdapter!!.onAddItem(rItem)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(R.string.tvCancel) { dialogInterface, i -> dialogInterface.cancel() }
        builder.show()
    }

    /**
     * Load items to RecyclerView.Adapter
     */
    override fun onShowReminders(reminderItems: List<ReminderItem>) {
        mAdapter!!.onAddItems(reminderItems)
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

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onInitDagger() {
        DaggerRemindersComponent.builder()
                .appComponent(appComponent)
                .remindersModule(RemindersModule(this))
                .build()
                .inject(this)
    }

    override fun getContentView(): Int {
        return R.layout.activity_reminders
    }
}
