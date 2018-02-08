
**PART 1**

This is the first project of Udacity's android nanodegree program. 

The  app will:

*Present the user with a grid arrangement of movie posters upon launch.*

Allow your user to change sort order via a setting:

*The sort order can be by most popular or by highest-rated*

*Allow the user to tap on a movie poster and transition to a details screen with additional information such as:
original title*

*movie poster image thumbnail*

*A plot synopsis (called overview in the api)*

*user rating (called vote_average in the api)*

*release date*

**Required Tasks**

*Build a UI layout for multiple Activities.*

*Launch these Activities via Intent.*

*Fetch data from themovieDB API*

To run the project go in build.gradle file and add your api key obtained by moviesdb.

**buildTypes.each {
        it.buildConfigField 'String', 'MOVIESDB_API_KEY', "\"YOUR_API_KEY_GOES_HERE\""
    }**

![screenshot_1514611641](https://user-images.githubusercontent.com/7755518/34451579-f3aa9c5c-ed32-11e7-9d9c-1138ac04da70.png)

![screenshot_1514611648](https://user-images.githubusercontent.com/7755518/34451580-f6073a78-ed32-11e7-955b-30a36b398b73.png)

![screenshot_1514611656](https://user-images.githubusercontent.com/7755518/34451581-f815972e-ed32-11e7-8e2d-f43a3acc60fb.png)
