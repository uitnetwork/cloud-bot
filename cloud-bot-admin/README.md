# Cloud bot admin deployment

## Configuration
* Configure the dynamodb region and endpoint in `application.yml`
* IAM key with dynamodb adin access configured in default profile

## Build
* `./gradlew build`

## Deployment:
* `java -jar build/libs/cloud-bot-admin-1.0.jar`

## Cloud bot configuration examples
* Create a cloud-bot-permission for Slack user with id `U0T96K5Q9` with all permissions by post following request to /api/cloud-bot-permission

```
{
  "permissions": [
    "OVERVIEW",
    "START",
    "STOP"
  ],
  "source": "SLACK",
  "userId": "U0T96K5Q9"
}
```
