# Weather Forecast App

### Features.
1. Open weather API integration.
2. Offline capability (caching).
3. Search places using **Google Places SDK**
4. Adding favourite locations and view on a Google maps.

## Architecture Demonstrated.
**MVVM architecture:** Used the *Model-View-ViewModel architecture design pattern* to take advantage of Jetpack ViewModel and LiveData or StateFlow for managing UI-related data and state. In an example
```Kotlin
class ViewModelCurrent(private val repository: CurrentRepository) : ViewModel() {
    private val tag = "ViewModelCurrent"
    val getAllCurrentWeather: LiveData<List<CurrentWeatherModel>> = repository.getAllCurrentWeather.asLiveData()
}
```
**MVI (Model-View-Intent):** I've used unidirectional data flow, where the UI sends intents to update the state, and the state is observed to render the UI.
```Kotlin
@Composable
fun HomeScreen(navController: NavHostController) {
    // other code
    val context = LocalContext.current

    // Observe the LiveData and convert it to a state
    val dataCurrent by viewModelCurrent.getAllCurrentWeather.observeAsState(initial = emptyList())
}
```

## Libraries - Android Jetpack
- [Retrofit](https://square.github.io/retrofit/) - A `REST` Client for our app which makes it relatively easy to retrieve `JSON` (or other structured data) via a *REST based webservice*. In Retrofit we configure which converter is used for the data serialization.<br>
```Kotlin
private val retrofit by lazy {
    Retrofit.Builder().baseUrl(
        BASE_URL
    ).addConverterFactory(
        GsonConverterFactory.create()
    ).build()
}
```
- [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-guide.html) - Used coroutines due to it's non blocking capability on the main thread while supporting many concurrent operations (API calls in this case and also retrieving data from room db) with fewer memory leaks.<br>
```kotlin
// Inserting into Room
fun insert(
    currentWeatherModel: CurrentWeatherModel
) = viewModelScope.launch {     
    try { repository.insert(currentWeatherModel) } catch (e: Exception) { Log.e(tag, "insert Error: ", e) } 
}

// Sample API call usage
fun fetchCurrentWeather(
    params: MutableMap<String, String>
): MutableLiveData<CurrentWeatherResponseModel?> {
    val fetchCurrentWeatherResponse : MutableLiveData<CurrentWeatherResponseModel?> = MutableLiveData()
    return try {
        viewModelScope.launch {
            repository.fetchCurrentWeather(
                params,
                object : IExecute<CurrentWeatherResponseModel> {
                    // other code
                }   
            )             
        }
    }
}
```
- [Flow from coroutines](https://developer.android.com/kotlin/flow) - Used this to receive live updates from our RoomDatabase e.g `val getAllCurrentWeather : Flow<List<CurrentWeatherModel>> = currentDao.getAllCurrentWeather()` .<br>
- [Room database](https://developer.android.com/training/data-storage/room) - Our use case is to cache relevant pieces of data so that when the device cannot access the network, the user can still access previous app content while they are offline.<br>
- [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Used this to take care of communications between **data source and the UI**. Our UI no longer needs to worry about the origin of the data.<br>
- [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - To build data objects that notify views when the underlying database changes in an active lifecycle state.<br>
- [Google Places SDK](https://developers.google.com/maps/documentation/places/android-sdk/overview) - Searching different locations using Android SDK and saving them to our favourites list for future reference<br>
- [Google Maps SDK](https://developers.google.com/maps/documentation/android-sdk/start) - In our use case we are displaying all favourite places on a map for quick visibility through Google location markers with some hint information when a marker is clicked.<br>

## Testing.
1. Unit Tests: Leveraged used of `espresso` and `junit` to write tests for my models and API tests.
2. Instrumental Android UI Tests: Used `uiautomator` to test instrumental `androidTest`

## Android CI (Continuous Integration)
Used `Android CI` from the GitHub marketplace for CI/CD
- Configured to run Unit Tests.
- We can easily track actions [from GitHub Actions of this repo](https://github.com/RocqJones/WeatherForecastApp/actions)