package com.sid_ali_tech.todoapptask.recivers

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.sid_ali_tech.todoapptask.common.Constants.TAG

class ReminderReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        Toast.makeText(context, "Reminder!", Toast.LENGTH_SHORT).show()
        Log.e(TAG, "onReceive: Reminder date ", )
    }
}