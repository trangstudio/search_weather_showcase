#NAB framework

## Overview

NAB Automation Testing Framework, it supports us to cover Automation testing for feature Search weather

Test case cover for: 
- Call current weather data for one location: By city name, By city ID, By geographic coordinates, By ZIP code
- Call current weather data for several cities: Cities within a rectangle zone, Cities in circle

## Install and Get started

Three way for run a test case: 
-Run by NAB runner(JUnit) with configuration: VM option = -ea -Dcucumber.features=src/test/resources/features -Dcucumber.filter.tags=@SmokeTest -Dtest=NABRunner
- Select class = core.NabRunner
- Report will be export to folder: target/test-reports/cucumber/cucumber.html

-Fast run by Cucumber in TestWeather.feature file

-Run by this command: mvn clean test -Dtest=core.NabRunner -Dcucumber.features=src/test/resources/features/searchweather/TestWeather.feature

## Code Branching Model

Code branching model support better collaboration in term of tracking, review, resolve conflict, revert and trial new idea. There are engineering standard for Code Branching Model , however, Pikachu has simplified branching rules.


## Convention

Naming Convention :
- Package Folder & Resource File 
- Classes Name Convention
- Method convention

Apply clean code architecture
# search_weather_showcase
# search_weather_showcase
