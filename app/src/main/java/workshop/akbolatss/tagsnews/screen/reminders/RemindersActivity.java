package workshop.akbolatss.tagsnews.screen.reminders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TimePicker;

import java.util.List;
import java.util.Random;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;
import workshop.akbolatss.tagsnews.R;
import workshop.akbolatss.tagsnews.base.BaseActivity;
import workshop.akbolatss.tagsnews.di.component.DaggerRemindersComponent;
import workshop.akbolatss.tagsnews.di.module.RemindersModule;
import workshop.akbolatss.tagsnews.repositories.source.ReminderItem;

public class RemindersActivity extends BaseActivity implements RemindersView, RemindersAdapter.OnReminderClickInterface {


    @BindView(R.id.toolbar)
    protected Toolbar mToolbar;

    @Inject
    protected RemindersPresenter mPresenter;

    @BindView(R.id.recyclerView)
    protected RecyclerView mRecyclerView;
    private RemindersAdapter mAdapter;

    @Override
    protected void onViewReady(Bundle savedInstanceState, Intent intent) {
        super.onViewReady(savedInstanceState, intent);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        DaggerRemindersComponent.builder()
                .appComponent(getAppComponent())
                .remindersModule(new RemindersModule(this))
                .build()
                .inject(this);

        initRV();
        mPresenter.onLoadReminders();
    }

    @Override
    public void onReminderOptions(final ReminderItem rItem, View view, int position) {
        if (view instanceof CheckBox) {
            CheckBox cb = (CheckBox) view;
            if (!cb.isChecked()) {
                mPresenter.onDeactivateNotification(rItem);
            } else {
                mPresenter.onActivateNotification(rItem, false);
            }
        } else {
            PopupMenu popupMenu = new PopupMenu(this, view);
            popupMenu.inflate(R.menu.menu_popup_reminder);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.mRetime:
                            LayoutInflater layoutInflater = LayoutInflater.from(RemindersActivity.this);
                            final View subView = layoutInflater.inflate(R.layout.dialog_timepicker, null);
                            final TimePicker timePicker = subView.findViewById(R.id.timePicker);

                            if (Build.VERSION.SDK_INT >= 23) {
                                timePicker.setHour(rItem.getHour());
                                timePicker.setMinute(rItem.getMinute());
                            } else {
                                timePicker.setCurrentHour(rItem.getHour());
                                timePicker.setCurrentMinute(rItem.getMinute());
                            }

                            AlertDialog.Builder builder = new AlertDialog.Builder(RemindersActivity.this, R.style.Dialog);

                            builder.setView(subView);
                            builder.setPositiveButton(R.string.tvAdd, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    ReminderItem rItem = new ReminderItem();
                                    Random random = new Random();
                                    rItem.setRequestCode(random.nextInt());
                                    if (Build.VERSION.SDK_INT >= 23) {
                                        rItem.setHour(timePicker.getHour());
                                        rItem.setMinute(timePicker.getMinute());
                                        if (timePicker.getHour() < 12) {
                                            rItem.setPM_AM("AM");
                                        } else {
                                            rItem.setPM_AM("PM");
                                        }
                                    } else {
                                        rItem.setHour(timePicker.getCurrentHour());
                                        rItem.setMinute(timePicker.getCurrentMinute());
                                        if (timePicker.getCurrentHour() < 12) {
                                            rItem.setPM_AM("AM");
                                        } else {
                                            rItem.setPM_AM("PM");
                                        }
                                    }
                                    mPresenter.onUpdate(rItem);

                                    dialogInterface.dismiss();
                                }
                            });
                            builder.setNegativeButton(R.string.tvCancel, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.cancel();
                                }
                            });
                            builder.show();
                            return true;
                        case R.id.mRemove:
                            mPresenter.onRemoveReminder(rItem);
                            return true;
                    }
                    return false;
                }
            });
            popupMenu.show();
        }
    }

    @Override
    public void onUpdateReminders() {
        mPresenter.onLoadReminders();
    }

    @OnClick(R.id.btnFabAdd)
    @Override
    public void onAddReminder() {
        LayoutInflater layoutInflater = LayoutInflater.from(RemindersActivity.this);
        final View subView = layoutInflater.inflate(R.layout.dialog_timepicker, null);
        final TimePicker timePicker = subView.findViewById(R.id.timePicker);

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.Dialog);

        builder.setView(subView);
        builder.setPositiveButton(R.string.tvAdd, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ReminderItem rItem = new ReminderItem();
                Random random = new Random();
                rItem.setRequestCode(random.nextInt());
                rItem.setIsActive(true);
                if (Build.VERSION.SDK_INT >= 23) {
                    rItem.setHour(timePicker.getHour());
                    rItem.setMinute(timePicker.getMinute());
                    if (timePicker.getHour() < 12) {
                        rItem.setPM_AM("AM");
                    } else {
                        rItem.setPM_AM("PM");
                    }
                } else {
                    rItem.setHour(timePicker.getCurrentHour());
                    rItem.setMinute(timePicker.getCurrentMinute());
                    if (timePicker.getCurrentHour() < 12) {
                        rItem.setPM_AM("AM");
                    } else {
                        rItem.setPM_AM("PM");
                    }
                }

                mPresenter.onAddReminder(rItem);
                mAdapter.onAddItem(rItem);
                dialogInterface.dismiss();
            }
        });
        builder.setNegativeButton(R.string.tvCancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        builder.show();
    }

    @Override
    public void onShowReminders(List<ReminderItem> reminderItems) {
        mAdapter.onAddItems(reminderItems);
    }

    private void initRV() {
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        mAdapter = new RemindersAdapter(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_reminders;
    }
}
