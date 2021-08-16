package com.example.firestore

import android.app.Activity
import android.net.Uri
import android.util.Log
import com.example.memoriae.ui.activities.*
import com.example.memoriae.ui.fragments.GlobalFragment
import com.example.memoriae.ui.fragments.PersonalFragment
import com.example.memoriae.ui.fragments.SearchFragment
import com.example.model.Favorites
import com.example.model.Memory
import com.example.model.User
import com.example.utils.Constants
import com.example.utils.ImageFileExtension
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class FireStoreClass {

    private val mFireStore =FirebaseFirestore.getInstance()

    fun getCurrentUserID(): String {
        val currentUser = FirebaseAuth.getInstance().currentUser
        return currentUser!!.uid
    }

    fun registerUser(activity: RegisterActivity, userInfo: User){
        mFireStore.collection(Constants.USERS)
            .document(userInfo.userID)
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener {
                activity.hideProgressBar()
                Log.e(activity.javaClass.simpleName,"Error while registering the user")
            }
    }

    fun getUserDetails(activity: LoginActivity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { userFromFirestore->
                val user=userFromFirestore.toObject(User::class.java)!!
                activity.userLoggedInSuccess(user)
            }
            .addOnFailureListener {
                activity.hideProgressBar()
                Log.e(activity.javaClass.simpleName,"Error while getting user details")
            }
    }

    fun uploadImageToCloudStorage(activity:AddEditMemory, imageFileUri: Uri?, imageType:String){
        val sRef : StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() + "." + ImageFileExtension(activity,imageFileUri)
        )

        sRef.putFile(imageFileUri!!)
            .addOnSuccessListener { taskSnapshot ->
                Log.e("Firebase Image URl",taskSnapshot.metadata!!.reference!!.downloadUrl.toString())
                taskSnapshot.metadata!!.reference!!.downloadUrl
                    .addOnSuccessListener { uri ->
                        Log.e("Downloadable Image Uri", uri.toString())
                        activity.imageUploadSuccess(uri.toString())
                    }
            }
            .addOnFailureListener{exception ->
                activity.hideProgressBar()
                Log.e(activity.javaClass.simpleName,exception.message,exception)
            }
    }

    fun uploadMemory(activity: AddEditMemory,memory:Memory){
        mFireStore.collection(Constants.MEMORIES)
            .document()
            .set(memory, SetOptions.merge())
            .addOnSuccessListener {
                activity.memoryUploadSuccess()
            }
            .addOnFailureListener{
                activity.hideProgressBar()
                Log.e(activity.javaClass.simpleName,"Error while uploading the product details")
            }
    }

    fun uploadFavoriteMemory(activity:Activity,favMemory:Favorites,memoryID:String){
        mFireStore.collection(Constants.FAVORITES)
            .document(memoryID)
            .set(favMemory, SetOptions.merge())
            .addOnSuccessListener {
                if(activity is MemoryDetailsActivity){
                    activity.favMemoryAdded()
                }else if (activity is FavoriteMemoryDetailsActivity){
                    activity.favMemoryAdded()
                }
            }
            .addOnFailureListener {
                if(activity is MemoryDetailsActivity){
                    activity.hideProgressBar()
                }else if (activity is FavoriteMemoryDetailsActivity){
                    activity.hideProgressBar()
                }
                Log.e(activity.javaClass.simpleName,"Error while uploading the product details")
            }
    }

    fun getMemoriesList(fragment : PersonalFragment){
        mFireStore.collection(Constants.MEMORIES)
            .whereEqualTo(Constants.USERID,getCurrentUserID())
            .get()
            .addOnSuccessListener { list->
                val memoryList : ArrayList<Memory> = ArrayList()
                for(i in list.documents){
                    val memory =i.toObject(Memory::class.java)!!

                    val memoryHashmap = HashMap<String,Any>()
                    memoryHashmap[Constants.MEMORY_ID]=i.id
                    mFireStore.collection(Constants.MEMORIES)
                        .document(i.id)
                        .update(memoryHashmap)

                    memoryList.add(memory)
                }

                fragment.listReceivedSuccessfully(memoryList)

            }
            .addOnFailureListener {
                fragment.hideProgressBar()
            }
    }


    fun getFavoriteMemoriesList(activity: FavoriteMemoriesActivity){
        mFireStore.collection(Constants.FAVORITES)
            .whereEqualTo(Constants.USERID,FireStoreClass().getCurrentUserID())
            .get()
            .addOnSuccessListener { list->
                val memoryList : ArrayList<Favorites> = ArrayList()
                for(i in list.documents){
                    val favMemory =i.toObject(Favorites::class.java)!!
                    memoryList.add(favMemory)
                }
                activity.listReceivedSuccessfully(memoryList)
            }
            .addOnFailureListener {
                activity.hideProgressBar()
            }
    }

    fun deleteMemory(fragment: PersonalFragment,memoryID:String){
        mFireStore.collection(Constants.MEMORIES)
            .document(memoryID)
            .delete()
            .addOnSuccessListener {
                Log.e("Deleted","DONE")
                fragment.deleteSuccessful()
            }
            .addOnFailureListener {
                fragment.hideProgressBar()
                Log.e(fragment.javaClass.simpleName, "Error while deleting the memory.")
            }
    }

    fun deleteFavoriteMemory(activity: Activity,memoryID: String){
        mFireStore.collection(Constants.FAVORITES)
            .document(memoryID)
            .delete()
            .addOnSuccessListener {
                Log.e("Deleted","DONE")
                if(activity is MemoryDetailsActivity){
                    activity.deleteFromFavorites()
                }else if (activity is FavoriteMemoryDetailsActivity){
                    activity.deleteFromFavorites()
                }

            }
            .addOnFailureListener {
                if(activity is MemoryDetailsActivity){
                    activity.hideProgressBar()
                }else if (activity is FavoriteMemoryDetailsActivity){
                    activity.hideProgressBar()
                }
                Log.e(activity.javaClass.simpleName, "Error while deleting the memory.")
            }

    }

    fun updateMemory(activity: Activity, memoryHashMap: HashMap<String,Any>, memoryID: String){
        mFireStore.collection(Constants.MEMORIES)
            .document(memoryID)
            .update(memoryHashMap)
            .addOnSuccessListener {
                if(activity is AddEditMemory){
                    activity.updationSuccess()
                }
            }
            .addOnFailureListener {
                if(activity is AddEditMemory){
                    activity.hideProgressBar()
                }

                Log.e(activity.javaClass.simpleName,"Error while updating the memory")
            }
    }

    fun getCityMemory(fragment: SearchFragment,cityName: String){
        mFireStore.collection(Constants.MEMORIES)
            .whereEqualTo(Constants.CITY_NAME,cityName)
            .whereEqualTo(Constants.PRIVACY,Constants.PUBLIC)
            .get()
            .addOnSuccessListener { list->
                val memoryCityList : ArrayList<Memory> = ArrayList()
                for(i in list.documents){
                    val memory =i.toObject(Memory::class.java)!!
                    memoryCityList.add(memory)
                }
                fragment.listReceivedSuccessfully(memoryCityList)
            }
            .addOnFailureListener {
                fragment.hideProgressBar()
            }
    }

    fun getLocalSearchMemory(fragment: SearchFragment,cityName: String){
        mFireStore.collection(Constants.MEMORIES)
            .whereEqualTo(Constants.CITY_NAME,cityName)
            .whereEqualTo(Constants.USERID,FireStoreClass().getCurrentUserID())
            .get()
            .addOnSuccessListener { list->
                val memoryCityList : ArrayList<Memory> = ArrayList()
                for(i in list.documents){
                    val memory =i.toObject(Memory::class.java)!!
                    memoryCityList.add(memory)
                }
                fragment.listReceivedSuccessfully(memoryCityList)
            }
            .addOnFailureListener {
                fragment.hideProgressBar()
            }

    }

    fun getAllMemoriesList(fragment : GlobalFragment){
        mFireStore.collection(Constants.MEMORIES)
            .whereEqualTo(Constants.PRIVACY,Constants.PUBLIC)
            .get()
            .addOnSuccessListener { list->
                val memoryList : ArrayList<Memory> = ArrayList()
                for(i in list.documents){
                    val memory =i.toObject(Memory::class.java)!!
                    memoryList.add(memory)
                }
                fragment.listReceivedSuccessfully(memoryList)

            }
            .addOnFailureListener {
                fragment.hideProgressBar()
            }
    }


}