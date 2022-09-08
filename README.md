# dapla-team-api
API for interacting with Dapla Team specific information, such as member and group constellations


## API spec

[work in progress, here be dragons]

### List users (all of SSB)

```
GET /users{?fields}
```
Response
```json
[
  {
    "name": "Donald Duck",
    "email": "donald.duck@ssb.no",
    "email_short": "kons-don@ssb.no",
    "_links": {
      "self": {
        "href": "http://localhost:8080/users/kons-don@ssb.no"
      },
      "teams": {
        "href": "http://localhost:8080/users/kons-don@ssb.no/teams"
      },
      "groups": {
        "href": "http://localhost:8080/users/kons-don@ssb.no/groups"
      }
    }
  },
  {
    "name": "Mikke Mus",
    "email": "mikke.mus@ssb.no",
    "email_short": "mm@ssb.no",
    "_links": {
      "self": {
        "href": "http://localhost:8080/users/mm@ssb.no"
      },
      "teams": {
        "href": "http://localhost:8080/users/mm@ssb.no/teams"
      },
      "groups": {
        "href": "http://localhost:8080/users/mm@ssb.no/groups"
      }
    }
  }
]
```

### List all Dapla teams

```
GET /teams
```
Response
```json
[
  {
    "uniform_team_name": "demo-enhjoern-a",
    "display_team_name": "Demo Enhjørning A",
    "_links": {
      "self": {
        "href": "http://localhost:8080/teams/demo-enhjoern-a"
      },
      "members": {
        "href": "http://localhost:8080/teams/{team_name}/members{?fields}",
        "templated": true
      },
      "groups": {
        "href": "http://localhost:8080/teams/{team_name}/groups"
      },
      "iac_repo": {
        "href": "https://github.com/statisticsnorway/demo-enhjoern-a-iac"
      }
    }
  }
]
```

### List members of a specific team

```
GET /teams/{team_name}/members{?fields}
```
Response
```json
[
  {
    "name": "Donald Duck",
    "email": "donald.duck@ssb.no",
    "email_short": "kons-don@ssb.no",
    "_links": {
      "self": {
        "href": "http://localhost:8080/members/kons-don@ssb.no"
      },
      "teams": {
        "href": "http://localhost:8080/members/kons-don@ssb.no/teams"
      },
      "groups": {
        "href": "http://localhost:8080/members/kons-don@ssb.no/groups"
      }
    }
  },
  {
    "name": "Mikke Mus",
    "email": "mikke.mus@ssb.no",
    "email_short": "mm@ssb.no",
    "_links": {
      "self": {
        "href": "http://localhost:8080/members/mm@ssb.no"
      },
      "teams": {
        "href": "http://localhost:8080/members/mm@ssb.no/teams"
      },
      "groups": {
        "href": "http://localhost:8080/members/mm@ssb.no/groups"
      }
    }
  }
]
```

### List groups of a specific team

```
GET /teams/{team_name}/groups{?fields}
```
`fields=members` will include the members of each group, and if `fields` is omitted this list is not included

Response (with members included)
```json
[
  {
    "name": "demo-enhjoern-a-managers",
    "members": [
      {
        "name": "Mikke Mus",
        "email": "mikke.mus@ssb.no",
        "email_short": "mm@ssb.no"
      }
    ],
    "_links": {
      "self": {
        "href": "http://localhost:8080/teams/demo-enhjoern-a/groups/demo-enhjoern-a-managers"
      }
    }
  },
  {
    "name": "demo-enhjoern-a-data-admins",
    "members": [
      {
        "name": "Donald Duck",
        "email": "donald.duck@ssb.no",
        "email_short": "kons-don@ssb.no"
      }
    ],
    "_links": {
      "self": {
        "href": "http://localhost:8080/teams/demo-enhjoern-a/groups/demo-enhjoern-a-data-admins"
      }
    }
  }
]
```