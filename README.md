# Java-dictionary.bot.Bot

A sql client build for Drawers. Now run your sql on chat itself without any pain of login
. Works well on slow network :). A private bot to be used by your entire team.

```
How to make it work:
1. Get your developer key, secret and drawers no. (equivalent of contact phone no.) from Drawers developers page.
2. Deploy it on your server. It is a simple maven project, just create a fatjar. 
3. Arguments - java -jar fatjar ReadArgs key secret monitoringemailid@gmail.com jdbc:mysql://dbaddress/db dbusername dbpwd
4. Share the drawers no. to your team. Add it to the contact list and start chatting.
```
![Sql](https://lh5.googleusercontent.com/-rqc6O9sdeZg/V0KpA6YRSqI/AAAAAAAAC70/_wpd96pDNCEWjAaUPxQpMPQEe6Ju2feiwCL0B/w328-h583-no/2016-05-22.jpg)

```
It currently only replies messages with size < 4kb, chunking is in plan.
Doesn't work on commands with sudo, supporting it is in plan.
It is using drawers java sdk to connect to Drawers. System requirement involves Java-7+.
```

