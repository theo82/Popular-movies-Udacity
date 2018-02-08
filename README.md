
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


**PART 2**

Stage 2: Trailers, Reviews, and Favorites
User Experience
In this stage I adding additional functionality to the app you built in Stage 1.


I adding more information to your movie details view:

I am allowing users to view and play trailers ( either in the youtube app or a web browser).
I am allowing users to read reviews of a selected movie.
I’ll also allow users to mark a movie as a favorite in the details view by tapping a button(star). This is for a local movies collection that you will maintain and does not require an API request*.
I’ll modify the existing sorting criteria for the main view to include an additional pivot to show their favorites collection.

![screenshot_1518070381](https://user-images.githubusercontent.com/7755518/35958222-469e064e-0ca8-11e8-9c4e-8e014917c39b.png)


![screenshot_1518070397](https://user-images.githubusercontent.com/7755518/35958245-64b79c80-0ca8-11e8-9143-167c1892261a.png)


![screenshot_1518070423](https://user-images.githubusercontent.com/7755518/35958261-7bfee218-0ca8-11e8-830f-f375e4a7fbaa.png)


![screenshot_1518070486](https://user-images.githubusercontent.com/7755518/35958274-8ced7eb8-0ca8-11e8-9573-db38392177c1.png)


![screenshot_1518070677](https://user-images.githubusercontent.com/7755518/35958288-a142cf80-0ca8-11e8-96ff-7bccc60211a5.png)

