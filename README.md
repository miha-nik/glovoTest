# glovoTest
GlovoTest

Glovo Test project

StartActivity - shows the download screen and asks the user permission to use his location. Loads data from server.

MainActivity - the main screen of the application. Implements a map — shows cities markers or work areas depending on scale. 
In the absence of permission to determine the location or if the location is not included in any of the work areas, 
the user is shown a window with a choice of cities.

MainPresenter - work with MainActivity logic

Repository - Gives data from a network or database

MainPresenterTest - test for main presenter
