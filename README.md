# WeatherCheckerToLearn
Android final course project for HackerU by Lev Shtern

Weather info supplied by http://www.openweathermap.org/</br>
In this project was used external library for internationalization: https://github.com/TakahikoKawasaki/nv-i18n.git

This app goes to the internet where check the weather in a specific country and city or at your GPS coordinate. 
Contens only one activity and no fragment have only three classes at all.</br>

<b>Classes</b></br>


<b>MainActivity</b> - the one and only activity in this app</br>
<b>Fields</b></br>
<i>spnCountry</i> - the sppiner with the country names. In onCreate() the Array and ViewAdapters are inserted into him
<i>txtCity</i> - text field(EditText) that gets from the user the city name he needs(the input must be in English and accurate) as String. Find by ID in onCreate().</br>
<i>displayLayout</i> - LinearLayout that dispays the output of the app. Gets his content in DownloadTextTask after processing the respond from the internet.</br>
<i>btnChecker, btnGPSChecker</i> - the instance of the buttons of the app used only to block/unblock the UI in the blockUI()/unblockUI() methods. The onClick methods of the buttons are have the same name of the reference to the buttons (btnChecker() and btnGPSChecker()).</br>
<i>countryShortName, cityName</i> - String type fields that used to contein the input from the User and form he URL
<i>countryNameList</i> - String array whith the names of the countries that been set into ArrayAdapter of the spnCountry.By this String the code of the country has been found in the internationalization library.</br>
<i>locationManager</i> - the instance of the LocationManager of Android in this app.</br>
<i>locationListener</i> - locationListener object of this app that deploid to get the location.</br>
<i>locationOn</i> - boolean that states if the locationListener started to listen to location update of the system or not. Used in onCreate() onResume() onPause() onDestroy() to check if the listener is off or on and to act according to it.

<b>Methods</b></br>
<i>onCreate()</i> -  in this method initiated or find by ID all the non constan fields then supply the needed info or logic into them  and start the LoctionListener location update. </br>
<i>onResume()</i> - if the app was resumed after onStop()/onPause() or in onCreate() the LocationListener was not started, onResume() start or restart it. In case it operationl this method done nothing.</br>
<i>onPause()</i> - stop the LocationListener.</br>
<i>onStop()</i> - stop the LocationListener if he was not stopped erlier.</br>
<i>onDestroy()</i> -  stop the LocationListener and the app.</br>
<i>btnChecker()</i> - onClikc method for btnChecker. Extract the String out of txtCity and check that it not null and not a blanc space if the string pass the checks the method checks that the City name don't contein white spaces in it(two word city name) if so then the splitter() metod called. Then extract the String out of spnCountry and get a CountryCode object from whom get the two-letter-country-code. After it the URL has been assembled and DownloadTextTask executed.</br>
<i>btnGPSChecker()</i> - onClick method for btnGPSChecker. Get the coordinates out of the LocationListener and assemble the URL to the DownloadTextTask. In case that the coordinates is 0.0 for latitude and longitude(that meens thet the GPS have been enebale but no signal and don't throws exeption) the locationManager.getLastKnownLocation() has been used to get the coordinates.</br> 
<i>blockUI()/unblockUI()</i> - block the UI when starts DownloadTextTask in the end of it unblock the UI </br>
<i>setupCountryNameList()</i> - returns String array whith all the country names that the internationalization library have.</br>
<i>splitter()</i> - splites the given stringby white space in it by the method of Sting.split and then return it as one string.

<b>DownloadTextTask</b> - extends AsyncTask - this class were all the magic is happens. Here opens the HTTP connection to dowload the respond for the specific URL in JSON format and process it. The result of the process of the JSON has been displed on the screen. In the end unblock the UI by the method of MainActivity unblockUI()</br>
<b>Fields</b></br>
<i>context</i> - the reference to the MainActivity. Need when a Contex object is needed and to call unblockUI() method.</br>
<i>displayLayout</i> - LinearLayout that dispays the output of the app. Gets his content in DownloadTextTask after processing the respond from the internet.</br>
<b>Methods</b></br>
<i>WeatherChecker()</i> - process the responce of the server in JSON format.</br> 
<i>openHttpGetConnection()</i> - opens the HTTP Get type connection to some URL.</br> 
<i>downloadText()</i> - first call the openHttpGetConnection() then download the response of the server to the given URL.</br> 
<i>doInBackground()</i> - here is the downloadText() has been called.</br> 
<i>onPostExecute()</i> - here is the WeatherChecker() has been called.</br>

<b>WeatherLocationListener</b> - implements LocationListener - this class gets the location out of the GPS of the phone and store it in the private double fields named latitude and longitude.
