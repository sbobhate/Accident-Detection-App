# EC601Project -- LIFELINE
Repository for all the code and files concerning the Accident Prevention and Detection App (EC601 Product Design).

## Developers

* **Shantanu Bobhate**
* **Sanya Kalra**
* **Jennifer Goodell**
* **Kexuan Cui**
* **Xianglin Zhou**

## Description

For our introuduction to product design course we decided to create an android app that would detect a car accident and alert first responders as well the users primary contacts. The product was aimed towards giving the users and their families a sense of security.
  
## Technologies Used

### Android Studio

We used Android Studio to develop the app. It can be downloaded from [here](https://developer.android.com/studio/index.html).

### Firebase
   
We used Google Firebase to help us conduct user authentication and maintain a backend database for our application. We used the following videos and resources to understand how to incorporate Firebase into our app.
    
### Google Api

We used the Google Maps Api to collect the users coordinates which we used to track the user's location as well as track the user's velocity. Our app's accident detection mechanism was activated at higher speeds. We also used this api to query the locations and the contacts of the closest hospitals.
  
## Contents

This repository contains 2 android projects:
..* Lifeline
..* DataToCSV
  
### Lifeline
  
This folder contains all the files for our final app.
    
### DataToCSV
  
This folder contains all the files for the testing app we created in order to collect acceleration data. We used this data to create the classification model.
