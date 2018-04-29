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
import android.widget.CheckBox
import android.widget.TimePicker
import kotlinx.android.synthetic.main.activity_reminders.*
import java.util.Random

import javax.inject.Inject

import workshop.akbolatss.tagsnews.R
import workshop.akbolatss.tagsnews.base.BaseActivity
import workshop.akbolatss.tagsnews.di.component.DaggerRemindersComponent
import workshop.akbolatss.tagsnews.di.module.RemindersModule
import workshop.akbolatss.tagsnews.model.dao.ReminderItem

class RemindersActivity : BaseActivity(), RemindersView, RemindersAdapter.ReminderListener {

    @Inject
    lateinit var mPresenter: RemindersPresenter

    private var mAdapter: RemindersAdapter? = null

    override fun onViewReady(savedInstanceState: Bundle?, intent: Intent) {
        super.onViewReady(savedInstanceState, intent)
        setSupportActionBar(toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        initDagger()
        initRV()
        initListeners()
        mPresenter.onLoadReminders()
    }

    private fun initListeners() {
        fabAdd.setOnClickListener {
            onAddReminder()
        }
    }

    private fun initDagger() {
        DaggerRemindersComponent.builder()
                .appComponent(appComponent)
                .remindersModule(RemindersModule(this))
                .build()
                .inject(this)
    }

    override fun onReminderOptions(rItem: ReminderItem, view: View, position: Int) {
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
                            mPresenter.onUpdate(rItem)
                            dialogInterface.dismiss()
                        }
                        builder.setNegativeButton(R.string.tvCancel) { dialogInterface, i -> dialogInterface.cancel() }
                        builder.show()
                        return@OnMenuItemClickListener true
                    }
                    R.id.mRemove -> {
                        mPresenter.onRemoveReminder(rItem)
                        return@OnMenuItemClickListener true
                    }
                }
                false
            })
            popupMenu.show()
    }

    override fun onReminderSwitch(rItem: ReminderItem, isActive: Boolean, pos: Int) {
        if (isActive) {
            mPresenter.onActivateNotification(rItem, false)
        } else {
            mPresenter.onDeactivateNotification(rItem)
        }
    }

    override fun onUpdateReminders() {
        mPresenter.onLoadReminders()
    }

    override fun onAddReminder() {
        val layoutInflater = LayoutInflater.from(this@RemindersActivity)
        val subView = layoutInflater.inflate(R.layout.dialog_timepicker, null)
        val timePicker = subView.findViewById<TimePicker>(R.id.timePicker)

        val builder = AlertDialog.Builder(this, R.style.Dialog)

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

            mPresenter!!.onAddReminder(rItem)
            mAdapter!!.onAddItem(rItem)
            dialogInterface.dismiss()
        }
        builder.setNegativeButton(R.string.tvCancel) { dialogInterface, i -> dialogInterface.cancel() }
        builder.show()
    }

    override fun onShowReminders(reminderItems: List<ReminderItem>) {
        mAdapter!!.onAddItems(reminderItems)
    }

    private fun initRV() {
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        mAdapter = RemindersAdapter(this)
        recyclerView.adapter = mAdapter
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

    override fun getContentView(): Int {
        return R.layout.activity_reminders
    }
}
