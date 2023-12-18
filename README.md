# Client and Account Management using Docker MySQL
My third Java project on GitHub made in collaboration with @(redacted). It simulates a very simple bank application in which a client needs to log in to take a look at their accounts and know their combined balance.
It applies the MVC design philosophy and structures the project in Model, View and Controller packages.
All the clients and their respective accounts are stored in a MySQL Docker Container database. Modifications upon them are reflected in the database in real time.

_Note:_ Program and screenshots are in Spanish.

<p align="center">
  <h1><ins>General Features</ins><h2>

  <h2>Landing screen that varies depending on whether a client has logged in or not:</h2><br>
  <img src="https://github.com/MaBerGal/Docker_MySQL_Practice/assets/148444718/547bb65a-b056-4e11-82dc-0a2ccfb4c57d" /><br>
  <img src="https://github.com/MaBerGal/Docker_MySQL_Practice/assets/148444718/16a82f32-d397-478d-b0ee-3acbc8a58bea" /> <br>
  <h2>Different menu bar options and items within them that activate/deactivate dynamically and have icons next to them:</h2><br>
  <img src="https://github.com/MaBerGal/Docker_MySQL_Practice/assets/148444718/7072362f-95a1-4280-8661-07743edd92db" /><br>
  <img src="https://github.com/MaBerGal/Docker_MySQL_Practice/assets/148444718/180aa228-95fc-4c54-a0e4-ebd7939bea60" /><br>
  <img src="https://github.com/MaBerGal/Docker_MySQL_Practice/assets/148444718/dc2dfc2c-9274-4b5c-a3ac-d19e616ef641" /><br>
  <h2>Custom modal JDialog that shows "About Me" pop-up window:</h2><br>
  <img src="https://github.com/MaBerGal/Docker_MySQL_Practice/assets/148444718/76adc394-cfb2-4e30-a455-c7e8d191b3ee" /><br>
  <h2>Login handled via dialog windows:</h2>
  <img src="https://github.com/MaBerGal/Docker_MySQL_Practice/assets/148444718/1fd9a66b-f93e-41f3-bca9-9a71425dabb3" >/<br>
  <h2>Details panel that allows for navigation between the different accounts associated with the client, as well as modifying them (plus JDatePicker):</h2><br>
  <img src="https://github.com/MaBerGal/Docker_MySQL_Practice/assets/148444718/8d855b9f-8ba1-4919-ba7a-ea1103c506f2" /><br>
  <h2>Summary panel that shows the client's most pertinent information as well as the possibility to calculate and update their combined balance:</h2><br>
  <img src="https://github.com/MaBerGal/Docker_MySQL_Practice/assets/148444718/57279753-6a4b-4e11-a1e9-094812706f9f" /><br>
</p>

### Version History
* _V1.0.0:_
  - Initial release. Options for logging in, logging out, viewing and modifying accounts, viewing and calculating/updating client info.
