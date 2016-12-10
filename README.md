# EC601Project -- LIFELINE
Repository for all the code and files concerning the Accident Prevention and Detection App (EC601 Product Design).
Check out the website at https://sbobhate.github.io/EC601Project/

## Developers

* **Shantanu Bobhate**
* **Sanya Kalra**
* **Jennifer Goodell**
* **Kexuan Cui**
* **Xianglin Zhou**

## Description

For our introuduction to product design course we decided to create an android app that would detect a car accident and alert first responders as well the users primary contacts. The product was aimed towards giving the users and their families a sense of security.
  
## Sceenshots

Here are a few screenshots of the final App

<img src="https://raw.githubusercontent.com/sbobhate/EC601Project/master/Resources/emergency_contacts.png" width="540" height="960">

![](https://raw.githubusercontent.com/sbobhate/EC601Project/master/Resources/notification.png)

![](https://raw.githubusercontent.com/sbobhate/EC601Project/master/Resources/alert.png)

![](https://raw.githubusercontent.com/sbobhate/EC601Project/master/Resources/message.png)
  
## Technologies Used

### Android Studio

We used Android Studio to develop the app. It can be downloaded from [here](https://developer.android.com/studio/index.html).

### Firebase
   
We used Google Firebase to help us conduct user authentication and maintain a backend database for our application. We used the following videos and resources to understand how to incorporate Firebase into our app.

Add the following dependency to the app-level **build.gradle** file:

```java
  compile 'com.google.firebase:firebase-auth:9.6.1'
  compile 'com.google.firebase:firebase-database:9.6.1'
```

**Code for Firebase Authentication:**

```java
  private FirebaseAuth firebaseAuth

  ...

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    ...

    firebaseAuth = FirebaseAuth.getInstance();

    /* To Create a new User */
    firebaseAuth.createUserWithEmailAndPassword(email, password)
          .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                  progressDialog.dismiss();
                  if (task.isSuccessful()) {
                      // Do something
                  }
                  else
                  {
                      // Display an Error Message
                  }
              }
          });

    ...

    /* To Sign In a User */
    firebaseAuth.signInWithEmailAndPassword(email, password)
          .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
              @Override
              public void onComplete(@NonNull Task<AuthResult> task) {
                  if (task.isSuccessful()) {
                      // Do something
                  }
                  else
                  {
                      // Display an Error Message
                  }
              }
          });
  }
```
    
**Code for Firebase Database:**

```java
  private FirebaseAuth firebaseAuth;
  private DatabaseReference databaseReference;

  ...
  @Override
  protected void onCreate(Bundle savedInstanceState) {
      super.onCreate(savedInstanceState);

      ...

      firebaseAuth = FirebaseAuth.getInstance();

      final FirebaseUser user = firebaseAuth.getCurrentUser();
      if (user == null) {
          finish();
          startActivity(new Intent(this, LoginScreenActivity.class));
      }

      databaseReference = FirebaseDatabase.getInstance().getReference();

      ...

      /* To retrieve data */
      databaseReference.child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
          @Override
          public void onDataChange(DataSnapshot dataSnapshot) {
              ArrayList<String> values = new ArrayList<String>(4);
              for (DataSnapshot child : dataSnapshot.getChildren()) {
                  values.add(child.getValue().toString());
              }

              if (!values.isEmpty()) {
                  // Do something with the values
              }
          }

          @Override
          public void onCancelled(DatabaseError databaseError) {
              // Display an Error Message
          }
      });

      /* Write Data */
      UserInformation userInformation = new UserInformation(firstName, lastName, policyNumber, phoneNumber);  
      databaseReference.child(user.getUid()).setValue(userInformation);

      ...
  }

  ...

  public class UserInformation {

      public String firstName, lastName, policyNumber, phoneNumber;

      public UserInformation() {
      }

      public UserInformation(String firstName, String lastName, String policyNumber, String phoneNumber) {
          this.firstName = firstName;
          this.lastName = lastName;
          this.policyNumber = policyNumber;
          this.phoneNumber = phoneNumber;
      }
  }
```

**A Demo for Firebase Authentication on Android:**

[![](http://img.youtube.com/vi/cibssj4-WMw/0.jpg)](http://www.youtube.com/watch?v=cibssj4-WMw)

### Google Api

We used the Google Maps Api to collect the users coordinates which we used to track the user's location as well as track the user's velocity. Our app's accident detection mechanism was activated at higher speeds. We also used this api to query the locations and the contacts of the closest hospitals.
  
## Contents

This repository contains 2 android projects:
* Lifeline
* DataToCSV
  
### Lifeline
  
This folder contains all the files for our final app.
    
### DataToCSV
  
This folder contains all the files for the testing app we created in order to collect acceleration data. We used this data to create the classification model.
