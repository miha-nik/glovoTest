# Glovo Test project 
Shows a map with working area of Glovo cities on it alongside some
additional information

## StartActivity 
Shows the download screen and asks the user permission to use his location. Loads data from server.

## MainActivity
The main screen of the application. Implements a map — shows cities markers or work areas depending on scale. 
In the absence of permission to determine the location or if the location is not included in any of the work areas, 
the user is shown a window with a choice of cities.

## MainPresenter 
Work with MainActivity logic

## Repository 
Gives data from a network or database

## MainPresenterTest 
Tests for main presenter
