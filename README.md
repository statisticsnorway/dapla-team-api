# dapla-team-api
API for interacting with Dapla Team specific information, such as member and group constellations


## API spec

[work in progress, here be dragons]

### List users (all of SSB)

```
GET /users{?fields}
```
Response: 200 (OK)
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

### Get a specific user

```
GET /users/{email_short}
```
Response: 200 (OK)
```json
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
}
```

### List all Dapla teams

```
GET /teams
```
Response: 200 (OK)
```json
[
  {
    "uniform_team_name": "demo-enhjoern-a",
    "display_team_name": "Demo Enhjørning A",
    "_links": {
      "self": {
        "href": "http://localhost:8080/teams/demo-enhjoern-a"
      },
      "users": {
        "href": "http://localhost:8080/teams/{team_name}/users{?fields}",
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

Implementation:
* Use GitHub as master system for this. Git IaC repos tagged with a specific topic. Information for each team
is fetched from the team's `terraform.tfvars` file.
* The list of repos should be cached

### List members of a specific team

```
GET /teams/{team_name}/users{?fields}
```
Response: 200 (OK)
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

### Clear and optianally refresh the teams cache
```
DELETE /teams/cache{?refresh}
```

### List groups of a specific team

```
GET /teams/{team_name}/groups{?fields}
```
`fields=users` will include the members of each group, and if `fields` is omitted this list is not included

Response: 200 (OK)
```json
[
  {
    "id": "demo-enhjoern-a-managers",
    "shortname": "managers",
    "users": [
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
    "users": [
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

### Add member to a specific group

```
PATCH /teams/{team_name}/groups/{group_name}
```

Payload
```json
{
  "users": [
    {
      "email_short": "kons-don@ssb.no"
    }
  ]
}
```

*Either* 

Response: 201 (Created)
```json
{
  "name": "demo-enhjoern-a-managers",
  "users": [
    {
      "name": "Mikke Mus",
      "email": "mikke.mus@ssb.no",
      "email_short": "mm@ssb.no"
    },
    {
      "name": "Donald Duck",
      "email": "donald.duck@ssb.no",
      "email_short": "kons-don@ssb.no"
    }
  ],
  "_links": {
    "self": {
      "href": "http://localhost:8080/teams/demo-enhjoern-a/groups/demo-enhjoern-a-managers"
    }
  }
}
```

*or*

Response: 202 (Accepted)

```json
{
  "message": "Request has been forwarded to Kundeservice for manual processing. Patience you must have, my young Padawan"
}
```
