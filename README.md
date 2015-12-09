# IntelliJ Auto Align Plugin

This is an IntelliJ Plugin that works in all Jetbrains editors. The purpose of it is to align your code in a more readable fashion.
Yes I realize that readable code is a hot topic among, so take this plugin for what it is. If you like it great, if not that ok too.

## Shortcut
You can activate the shortcut by holding down (windows/linux) CONTROL+OPTION+I (mac) CMD+OPTION+I
The plugin will format either the current selection or the entire document if there is no selection.

## Auto Align Example

Source code
```javascript
var Channel={

  channelId: null,
  id: null,
  sentvType: null,

  // DESCRIPTIONS
  title: null,
  synopsis: null,

  currentProgram: null,

  // FLAGS
  isExpiring: false,
  isDVRExpiring: false,
  isNew: false,
  isFavorite: false,
  badgeCode: null,
  hasCollections: false,
  isVodOnly: false,

  // ARRAYS
  genres: null,        // []
  urls: null,        // []
  cast: null,        // []
  customCollections: null         // []
};
```
Output code
```javascript
var Channel            = {

  channelId            : null,
  id                   : null,
  sentvType            : null,

  // DESCRIPTIONS
  title                : null,
  synopsis             : null,

  currentProgram       : null,

  // FLAGS
  isExpiring           : false,
  isDVRExpiring        : false,
  isNew                : false,
  isFavorite           : false,
  badgeCode            : null,
  hasCollections       : false,
  isVodOnly            : false,

  // ARRAYS
  genres               : null,        // []
  urls                 : null,        // []
  cast                 : null,        // []
  customCollections    : null         // []
};
```
