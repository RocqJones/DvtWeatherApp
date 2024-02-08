package com.rocqjones.dvt.weatherapp.model

import com.rocqjones.dvt.weatherapp.logic.models.response.City
import com.rocqjones.dvt.weatherapp.logic.models.response.ForeCastList
import com.rocqjones.dvt.weatherapp.logic.models.response.ForecastWeatherResponseModel
import com.rocqjones.dvt.weatherapp.logic.models.response.MainDetails
import com.rocqjones.dvt.weatherapp.logic.models.response.UserCoordinates
import com.rocqjones.dvt.weatherapp.logic.models.response.WeatherDetails
import org.junit.Assert
import org.junit.Before
import org.junit.Test

internal class TestForecastResponseModel {

    private lateinit var response: ForecastWeatherResponseModel

    @Before
    fun setUp() {
        response = ForecastWeatherResponseModel(
            200,
            0,
            city = City(
                34567,
                "Mombasa",
                "Kenya",
                userCoordinates = UserCoordinates(
                    -1.2240624585163389,
                    36.919931302314055
                )
            ),
            foreCastList = listOf(
                ForeCastList(
                    4453,
                    mainDetails = MainDetails(
                        342.6,
                        132.38,
                        491.62,
                        492.78
                    ),
                    weatherDetails = listOf(
                        WeatherDetails(
                            "Rainy",
                            "Lightening and thunderstorm"
                        )
                    ),
                    "2022-12-19 22:38:00"
                )
            )
        )
    }

    @Test
    fun getCode() {
        Assert.assertEquals(200, response.code)
    }

    @Test
    fun getCoordinates() {
        Assert.assertEquals(
            UserCoordinates(
                -1.2240624585163389,
                36.919931302314055
            ), response.city?.userCoordinates
        )
    }

    @Test
    fun getTemperatureDetails() {
        Assert.assertEquals(
            MainDetails(
                342.6,
                132.38,
                491.62,
                492.78
            ), response.foreCastList?.get(0)?.mainDetails
        )
    }

    @Test
    fun getWeatherDetails() {
        Assert.assertEquals(
            "Lightening and thunderstorm",
            response.foreCastList?.get(0)?.weatherDetails?.get(0)?.description
        )
    }
}