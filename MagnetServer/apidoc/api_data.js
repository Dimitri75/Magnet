define({ "api": [
  {
    "type": "post",
    "url": "/group/:id/user/:token",
    "title": "Add a User to a Group",
    "name": "AddUserTpGroup",
    "group": "Group",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "<p>Integer</p> ",
            "optional": false,
            "field": "id",
            "description": "<p>Id of the Group to update.</p> "
          },
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "token",
            "description": "<p>Token of the User.</p> "
          },
          {
            "group": "Parameter",
            "type": "<p>Integer</p> ",
            "optional": false,
            "field": "login",
            "description": "<p>Login of the User to add.</p> "
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "TokenNotValid",
            "description": "<p>The <code>token</code> given cannot authenticate the User.</p> "
          },
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "ErrorWhileAdding",
            "description": "<p>The user couldn't be added.</p> "
          },
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "UserNotInGroup",
            "description": "<p>Cannot add user to a group the user is not in.</p> "
          },
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "GroupNotValid",
            "description": "<p>The group doesn't exist.</p> "
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "src/Magnet/Controller/GroupControllerProvider.php",
    "groupTitle": "Group"
  },
  {
    "type": "get",
    "url": "/group/:token",
    "title": "Request the groups of the User",
    "name": "GetGroups",
    "group": "Group",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "token",
            "description": "<p>Token of the User.</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "<p>Array</p> ",
            "optional": false,
            "field": "groups",
            "description": "<p>The groups of the User</p> "
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "TokenNotValid",
            "description": "<p>The <code>token</code> given cannot authenticate the User.</p> "
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "src/Magnet/Controller/GroupControllerProvider.php",
    "groupTitle": "Group"
  },
  {
    "type": "post",
    "url": "/group/:token",
    "title": "Creates a new Group.",
    "name": "PostGroup",
    "group": "Group",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "token",
            "description": "<p>Token of the User.</p> "
          },
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "name",
            "description": "<p>Name of the Group.</p> "
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "TokenNotValid",
            "description": "<p>The <code>token</code> given cannot authenticate the User.</p> "
          },
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "ErrorWhileSaving",
            "description": "<p>The group couldn't be saved.</p> "
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "src/Magnet/Controller/GroupControllerProvider.php",
    "groupTitle": "Group"
  },
  {
    "type": "get",
    "url": "/pin/:token",
    "title": "Request Pins of the User.",
    "name": "GetPins",
    "group": "Pin",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "token",
            "description": "<p>Token of the User.</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "<p>Array</p> ",
            "optional": false,
            "field": "pins",
            "description": "<p>The pins of the User</p> "
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "TokenNotValid",
            "description": "<p>The <code>token</code> given cannot authenticate the User.</p> "
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "src/Magnet/Controller/PinControllerProvider.php",
    "groupTitle": "Pin"
  },
  {
    "type": "post",
    "url": "/pin/:token",
    "title": "Creates a new Pin.",
    "name": "PostPin",
    "group": "Pin",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "token",
            "description": "<p>Token of the User.</p> "
          },
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "name",
            "description": "<p>Name of the Pin.</p> "
          },
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "description",
            "description": "<p>Description of the Pin.</p> "
          },
          {
            "group": "Parameter",
            "type": "<p>Location</p> ",
            "optional": false,
            "field": "location",
            "description": "<p>Location of the Pin.</p> "
          },
          {
            "group": "Parameter",
            "type": "<p>Datetime</p> ",
            "optional": false,
            "field": "creation_time",
            "description": "<p>Creation Time of the Pin.</p> "
          },
          {
            "group": "Parameter",
            "type": "<p>Datetime</p> ",
            "optional": false,
            "field": "deletion_time",
            "description": "<p>Deletion Time of the Pin.</p> "
          },
          {
            "group": "Parameter",
            "type": "<p>Integer</p> ",
            "optional": false,
            "field": "group_id",
            "description": "<p>Id of the group associated with the Pin.</p> "
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "TokenNotValid",
            "description": "<p>The <code>token</code> given cannot authenticate the User.</p> "
          },
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "ErrorWhileSaving",
            "description": "<p>The pin couldn't be saved.</p> "
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "src/Magnet/Controller/PinControllerProvider.php",
    "groupTitle": "Pin"
  },
  {
    "type": "get",
    "url": "/user/",
    "title": "Request All Connected Users",
    "name": "GetConnectedUsers",
    "group": "User",
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "<p>Array</p> ",
            "optional": false,
            "field": "connected",
            "description": "<p>List of connected Users.</p> "
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "src/Magnet/Controller/UserControllerProvider.php",
    "groupTitle": "User"
  },
  {
    "type": "get",
    "url": "/user/:token",
    "title": "Request User information",
    "name": "GetUser",
    "group": "User",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "token",
            "description": "<p>Token of the User.</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "<p>Integer</p> ",
            "optional": false,
            "field": "id",
            "description": "<p>The id of the User</p> "
          },
          {
            "group": "Success 200",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "login",
            "description": "<p>The login of the User</p> "
          },
          {
            "group": "Success 200",
            "type": "<p>Location</p> ",
            "optional": false,
            "field": "location",
            "description": "<p>The location of the User</p> "
          },
          {
            "group": "Success 200",
            "type": "<p>Boolean</p> ",
            "optional": false,
            "field": "visible",
            "description": "<p>If the User is seen as connected</p> "
          },
          {
            "group": "Success 200",
            "type": "<p>Datetime</p> ",
            "optional": false,
            "field": "last_activity",
            "description": "<p>Last time of activity of the User</p> "
          },
          {
            "group": "Success 200",
            "type": "<p>Array</p> ",
            "optional": false,
            "field": "groups",
            "description": "<p>The groups of the User</p> "
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "TokenNotValid",
            "description": "<p>The <code>token</code> given cannot authenticate the User.</p> "
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "src/Magnet/Controller/UserControllerProvider.php",
    "groupTitle": "User"
  },
  {
    "type": "get",
    "url": "/user/:login",
    "title": "Request data about the User using a login.",
    "name": "GetUserInfo",
    "group": "User",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "login",
            "description": "<p>Login of the User.</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "<p>Integer</p> ",
            "optional": false,
            "field": "id",
            "description": "<p>The id of the User</p> "
          },
          {
            "group": "Success 200",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "login",
            "description": "<p>The login of the User</p> "
          },
          {
            "group": "Success 200",
            "type": "<p>Location</p> ",
            "optional": false,
            "field": "location",
            "description": "<p>The location of the User</p> "
          },
          {
            "group": "Success 200",
            "type": "<p>Datetime</p> ",
            "optional": false,
            "field": "last_activity",
            "description": "<p>Last time of activity of the User</p> "
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "LoginNotFound",
            "description": "<p>The <code>login</code> doesn't match for any User.</p> "
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "src/Magnet/Controller/UserControllerProvider.php",
    "groupTitle": "User"
  },
  {
    "type": "get",
    "url": "/user/:id",
    "title": "Request data about the User using an id.",
    "name": "GetUserInfo",
    "group": "User",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "login",
            "description": "<p>Login of the User.</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "<p>Integer</p> ",
            "optional": false,
            "field": "id",
            "description": "<p>The id of the User</p> "
          },
          {
            "group": "Success 200",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "login",
            "description": "<p>The login of the User</p> "
          },
          {
            "group": "Success 200",
            "type": "<p>Location</p> ",
            "optional": false,
            "field": "location",
            "description": "<p>The location of the User</p> "
          },
          {
            "group": "Success 200",
            "type": "<p>Datetime</p> ",
            "optional": false,
            "field": "last_activity",
            "description": "<p>Last time of activity of the User</p> "
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "IdNotFound",
            "description": "<p>The <code>id</code> doesn't match for any User.</p> "
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "src/Magnet/Controller/UserControllerProvider.php",
    "groupTitle": "User"
  },
  {
    "type": "get",
    "url": "/user/:login/:password",
    "title": "Requests a token for the User",
    "name": "GetUserToken",
    "group": "User",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "login",
            "description": "<p>Login of the User.</p> "
          },
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "password",
            "description": "<p>Password of the User.</p> "
          }
        ]
      }
    },
    "success": {
      "fields": {
        "Success 200": [
          {
            "group": "Success 200",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "token",
            "description": "<p>The token to authenticate the User</p> "
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "CredentialsNotValid",
            "description": "<p>The <code>login</code> or <code>password</code> given doesn't match for any User.</p> "
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "src/Magnet/Controller/UserControllerProvider.php",
    "groupTitle": "User"
  },
  {
    "type": "post",
    "url": "/user/",
    "title": "Creates a new User",
    "name": "PostUser",
    "group": "User",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "login",
            "description": "<p>Login of the User.</p> "
          },
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": false,
            "field": "password",
            "description": "<p>Password of the User.</p> "
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "LoginUsed",
            "description": "<p>The <code>login</code> is already used by another User.</p> "
          },
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "ErrorWhileSaving",
            "description": "<p>The User couldn't be saved.</p> "
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "src/Magnet/Controller/UserControllerProvider.php",
    "groupTitle": "User"
  },
  {
    "type": "put",
    "url": "/user/",
    "title": "Updates a User",
    "name": "PutUser",
    "group": "User",
    "parameter": {
      "fields": {
        "Parameter": [
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": true,
            "field": "password",
            "description": "<p>Password of the User.</p> "
          },
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": true,
            "field": "location",
            "description": "<p>Location of the User.</p> "
          },
          {
            "group": "Parameter",
            "type": "<p>String</p> ",
            "optional": true,
            "field": "visible",
            "description": "<p>Visibility of the User.</p> "
          }
        ]
      }
    },
    "error": {
      "fields": {
        "Error 4xx": [
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "TokenNotValid",
            "description": "<p>The <code>token</code> given cannot authenticate the User.</p> "
          },
          {
            "group": "Error 4xx",
            "optional": false,
            "field": "ErrorWhileSaving",
            "description": "<p>The User couldn't be saved.</p> "
          }
        ]
      }
    },
    "version": "0.0.0",
    "filename": "src/Magnet/Controller/UserControllerProvider.php",
    "groupTitle": "User"
  }
] });