# Cloud bot lambda deployment

## Requirement:
* [JDK8](http://www.oracle.com/technetwork/java/javase/downloads/index.html) installed 
* [Serverless](https://serverless.com/) installed
* IAM key with admin access configured in default profile for deployment
* IAM role with permission to list/stop/start EC2 instances, Cloud Watch write permission and DynamoDB table `CloudBotPermission` read permission. That table will be created by cloud-bot-admin
* GCP service key with permission to list/stop/start GCE instances

## Configuration:
* Create and configure `cloud-bot-lambda.yml` with the template which is from `cloud-bot-lambda-samples.yml`
* Export the absolute path of GCP service key to CLOUD_BOT_GOOLD_ACCOUNT_SERVICE_FILE environment (`export CLOUD_BOT_GOOLD_ACCOUNT_SERVICE_FILE=/some-path/service-file.json`)


## Deployment
* `./gradlew deploy`

## API key
* After deployed, the api gateway endpoint and api key will be printed out. Copy those values to configure the dialogflow-agent.
