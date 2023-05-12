# Caolan_Maher_20090170_Mobile_App_Development_2

## Personal Statement
I choose DND as the idea for this app as I enjoy playing DND myself with friends, but sometimes all the different quests going on can be confusing,
so I decided to use that to make this app for a college project.
This app makes it easy to track quests going on in your DND campaigns and can even be used to see quests created by other users.

## Overview
This is a DND campaign manager app. A user can sign into the app using email/password or with a google account.
A user can create, update, view and delete quests as they please.
A user can view other quests made by other users but can't edit or delete them.
The data created by the user is stored using firebase for easy access when the user launches the app.
The user can associate a location with a quest, and can view a map to see where their quests are located as well as other user's quests.
The user can mark quests as a favourite which will be displayed using an icon next to the quest object.

## All Functionality
- User can create, update, view and delete quests
- User can add a location to their quests using google maps
- User can create an account or sign into an existing one using an email and password or their google account
- User can view just their own quests or can view quests made by other users too
- User can look at a map that will show just their own quests or also quests made by other users
- User's quest data is stored using firebase
- User's profile picture is stored using firebase
- User can swipe left or right on a quest to delete or edit that quest respectively
- User can swipe down to refresh list of quests
- User can choose to turn on light or dark mode as they desire
- User can search for individual quests using a search bar
- User can mark quests as a favourite, which is displayed by an icon next to the quest
- User can update their profile picture with an image of their choosing

## 3rd Party Technologies Used
- Google Maps API (for setting locations of quests and showing where other user's quests are located)
- Firebase Firestore (for storing quest data)
- Firebase Authentication (for allowing users to sign in / register using email/password and google accounts)
- Firebase Cloud Storage (for object storage for user's profile pictures)

## UX / DX Approach
- Wanted all UI elements to be clear on their purpose and readable
- All UI elements have an appropriate icon to show their functionality, e.g filtering quests has a search icon to show it has a search function
- UI elements are chosen to give good UX, e.g in the login screen, there is TextInputLayouts which show the hint text at all times so a user knows what their inputting in that field
- Night Mode is available for users to activate if they desire, user can switch between dark and light mode
- Screens have transitions between each other to make the transition cleaner
- There is a nav drawer that the user can use to easily navigate between screens
- The user can swipe left and right on quest objects to delete and edit them respectively
- The user can also swipe down on the list of quests to refresh it
- Constraint layouts are used to easily format UI elements and to organise them neatly
- MVVM design pattern is used to seperate the front and back end of each screen

## Git Usage
- Git was used as a version control system and a cloud backup (if needed)
- Commits were used as an easy way to track progress and also to retrace steps if anything ever got broken
- Bracnhing was used to easily seperate out tasks and in case a branch ever broke or something goes wrong in that branch, I can easily go back to my main branch
- Releases were made whenever major functionality was added to the app

## References
- How to activate Dark Mode: https://developer.android.com/develop/ui/views/theming/darktheme
- Firebase Auth With Google: https://firebase.google.com/docs/auth/android/google-signin
