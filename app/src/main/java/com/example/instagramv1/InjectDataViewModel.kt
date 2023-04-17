package com.example.instagramv1

import android.content.Context
import android.graphics.BitmapFactory
import androidx.lifecycle.ViewModel
import com.example.instagramv1.data.repository.InjectDataRepository
import com.example.instagramv1.data.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

@HiltViewModel
class InjectDataViewModel @Inject constructor(
    private val injectDataRepository: InjectDataRepository,
    private val postRepository: PostRepository
) : ViewModel(){

    lateinit var context : Context

    suspend fun addUsers(){
        val emails = arrayOf("naruto@gmail.com",
                             "itachi@gmail.com",
                                "virat@gmail.com",
                                "rock@gmail.com",
                                "anadearmas@gmail.com",
                                "familyguy@gmail.com",
                                "memetamil@gmail.com",
                                "ronaldo@gmail.com")

        val phones = arrayOf("1231231231","1241241241","1251251251","1261261261","1271271271","1281281281","1291291291","1311311311")
        val password = "Pass@123"
        var createdDate = Date()
        val usernames = arrayOf("naruto10","itachi77","virat18","therock","anadearmas","familyguy","meme.tamil","cristiano")
        val fullnames = arrayOf("Naruto Uzumaki","Itachi Uchiha","Virat Kohli","Dwayne Johnson","Ana De Armas","Family Guy","Memes Tamil","Cristiano Ronaldo")
        val privateaccounts = arrayOf(false,false,false,false,false,true,true,false)
        val userIds = arrayOf(1,2,3,4,5,6,7,8)
        val accountIds = arrayOf(1,2,3,4,5,6,7,8)
        val profilePictures = arrayOf(R.drawable.narutocropped,R.drawable.itachicropped,
                                R.drawable.viratcropped,R.drawable.rockcropped,R.drawable.anacropped,
                                R.drawable.familyguycropped,R.drawable.dankcropped,R.drawable.ronaldocropped)

        for(i in emails.indices){
            injectDataRepository.addAccount(emails[i],
                phones[i],
                password,
                Date(),
                usernames[i],
                fullnames[i],
                BitmapFactory.decodeResource(context.getResources(),
                profilePictures[i]),
                privateaccounts[i],
                userIds[i],
                accountIds[i])

        }

        val postUserIds = arrayOf(1,1,1,1,1,2,2,2,2,3,3,3,4,4,5,5,5,6,6,7,8,8,8)
        val postIds = arrayOf(1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23)
        val descriptions = arrayOf(
            "👊 I don't care who I have to fight! If he rips my arms out, I'll kick him to death! If he rips my legs off, I'll bite him to death! If he rips my head off, I'll stare him to death! And if he gouges out my eyes, I'll curse him from the grave! 😠💪 Even if I'm torn to shreds, I'm taking Sasuke back from Orochimaru! 🦾👀💀",
            "💔 The pain of being alone is completely out of this world, isn't it? I don't know why, but I understand your feelings so much, it actually hurts. 😢",
            "💪 Believe It! 💥",
            null,
            "I won't run away anymore... I won't go back on my word... That is my ninja way!",
            "When people are protecting something truly special to them, they truly can become...as strong as they can be.",
            "Becoming the Hokage doesn't mean people will acknowledge you.",
            "He who forgives and acknowledges himself, that is what it truly means to be strong! ",
            null,
            "Self-belief and hard work will always earn you success.",
            "Never give up, never lose hope. Always have faith, it allows you to cope.",
            "I like to lead from the front and set an example for others to follow.",
            "Success at anything will always come down to this: focus and effort. And we control both.",
            "Be humble. Be hungry. And always be the hardest worker in the room.",
            "The sound of the waves, the sun on my skin, and the sand between my toes...pure bliss ☀️🌊 ",
            "Food is always the way to my heart ❤️ Enjoying a delicious meal with great company 😋 ",
            "Spending time with loved ones is always the best medicine ❤️",
            "Do you know what's funnier than 24? Twenty-five! 😂🤣",
            "You know what really grinds my gears? 😤🦾",
            null,
            "🙌🏽 Nothing worth having comes easy. Hard work pays off. 💪🏽🔥",
            "👀 Check out my new kicks! Thanks to nike for keeping me fresh on the pitch. 🙏🏽👟",
            "💪🏽 Putting in the work to be my best self. It's not about being perfect, it's about progress. 🔥👊🏽"
        )

        val locations = arrayOf(
            "New York",
            "Paris",
            "Tokyo",
            "London",
            "Los Angeles",
            "Sydney",
            "Rio de Janeiro",
            "Amsterdam",
            "Bangkok",
            "Barcelona",
            "Berlin",
            "Cairo",
            "Dubai",
            "Hong Kong",
            "Montreal",
            "Mumbai",
            "San Francisco",
            "Seoul",
            "Singapore",
            "Vancouver",
            "Spain",
            "Spain",
            "Spain"
        )

        var images = arrayOf(
            R.drawable.narutopost1,
            R.drawable.narutopost2,
            R.drawable.narutopost3,
            R.drawable.narutopost4,
            R.drawable.narutopost5,
            R.drawable.itachipost1,
            R.drawable.itachipost2,
            R.drawable.itachipost3,
            R.drawable.itachipost4,
            R.drawable.viratpost1,
            R.drawable.viratpost2,
            R.drawable.viratpost3,
            R.drawable.rockpost1,
            R.drawable.rockpost2,
            R.drawable.anapost1,
            R.drawable.anapost2,
            R.drawable.anapost3,
            R.drawable.fampost1,
            R.drawable.fampost2,
            R.drawable.dankpost1,
            R.drawable.ronaldopost1,
            R.drawable.ronaldopost2,
            R.drawable.ronaldopost3
        )

        for(i in postIds.indices){
            injectDataRepository.addPost(
                postUserIds[i],Date(),
                BitmapFactory.decodeResource(context.getResources(),
                    images[i]),descriptions[i],locations[i],postIds[i])
        }

        for(u in userIds){
            postRepository.addReaction(u,1)
        }


    }
}