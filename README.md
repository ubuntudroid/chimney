# chimney
[WIP] Technologically a playground to test if it is possible to write a meaningful app using just Android Architecture Components where available, Chimney will help you to select a random game from your Steam library based on different criteria.

## State

This app is in a very early development stage. Right now you can't do any meaningful with it apart
from getting some meta data about your steam profile. For now I'm using it as a playground to experiment with
Android architecture components, mainly Lifecycle Aware Components, LiveData and Room as well as Koin for DI.

## Setup

At this stage no binaries are provided meaning you need to compile and deploy the app for yourself.
Use Android Studio 3.1.0 for that.

As no login has been implemented yet you need to define your steam API token and user ID in the
_local.properties_ file like so:

```
chimney.steam.api.token=<api token>
chimney.steam.user.id=<steam ID>
```

You can obtain a Steam API token [here](https://steamcommunity.com/dev/apikey).

Note, that the steam ID is not your steam handle/user name! However you can easily find various instructions and
web apps which can aid you in [finding your steam ID](http://bfy.tw/Hf4r).