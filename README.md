# Cloud bot

Cloud bot is a solution which allows us to start/stop EC2 instances and GCE instances via chatbot.
Cloud bot contains 3 main parts:
* **Cloud bot lamda**: This is the fulfillment webhook which will be requested whenever we interact with the chatbot. The lamda will execute the action based on our command.
* **Cloud bot admin**: This is a Spring application which is used to manage the permissions of individuals who are using the chatbot. Those permissions will be validated by cloud bot lamda before it makes the execution.
* **Dialogflow agent**: This is an agent in [Dialog](https://console.dialogflow.com) which allows us to communicate with the chatbot (Slack, Skype...) using natural language. Based on that, it will then make the fulfillment request to the fullfillment webhook which was the API gateway link of the cloud bot lamda

# Deployment

1. Deploy [cloud-bot-lamda](cloud-bot-lamda)
2. Deploy [cloud-bot-admin](cloud-bot-admin)
3. Deploy [cloud-bot-agent](cloud-bot-agent)

# Examples:

## Amazon Ec2
### To get an overview of all EC2 instances, type `Ec2 overview`:
```
> Ec2 overview
Ec2 Overview
Ec2Instance(id=i-050fd2b4cd3f386b2, name=test123, type=t2.micro, state=stopped)
Ec2Instance(id=i-023bdcd9a310ae47b, name=test123, type=t2.micro, state=stopped)
```

### To start an EC2 instance, type `Start ec2 with name test123`:
```
> Start ec2 with name test123
Starting EC2: i-050fd2b4cd3f386b2, i-023bdcd9a310ae47b
```

### To stop an EC2 instance, type `Stop ec2 with name test123`:
```
> Start ec2 with name test123
Stopping EC2: i-050fd2b4cd3f386b2, i-023bdcd9a310ae47b
```

## Google compute engine
### To get an overview of all GCE instances, type `Gce overview`:
```
> Gce overview
Compute Engine Overview
GceInstance(id=7004539054369853569, name=test-cos, machineType=n1-standard-1, status=TERMINATED)
GceInstance(id=358782599305738022, name=test-from-gcloud, machineType=n1-standard-1, status=TERMINATED)
```

### To start an GCE instance, type `Gce start test-from-gcloud`:
```
> gce start test-from-gcloud
Starting GCE: test-from-gcloud
```

### To stop an GCE instance, type `Gce stop test-from-gcloud`:
```
> Gce stop test-from-gcloud
Stopping GCE: test-from-gcloud
```
