package com.hamza.task.data.repoImpl

import android.util.Log
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.hamza.task.domain.models.Player
import com.hamza.task.domain.repo.PlayersRepo
import com.hamza.task.utils.Constants.PLAYERS_PATH
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class PlayersRepoImpl : PlayersRepo {

    private val database = FirebaseDatabase.getInstance().getReference(PLAYERS_PATH)

    override suspend fun getPlayers(): List<Player> = suspendCancellableCoroutine { continuation ->

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val players = mutableListOf<Player>()
                for (playerSnapshot in dataSnapshot.children) {
                    val player = playerSnapshot.getValue(Player::class.java)
                    player?.let { players.add(it) }
                }
                continuation.resume(players)
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("getPlayers", "loadPlayers:onCancelled", databaseError.toException())
                continuation.resumeWithException(databaseError.toException())
            }
        }

        database.addValueEventListener(postListener)
    }


}