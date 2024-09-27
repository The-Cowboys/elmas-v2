# elmas-v2

🇺🇾 Averigüemos quién es el más tonto.

🇬🇧 Let's find out who is the dumbest.


...but this time in Scala using Scala CLI.

## Run in Production

Our server uses PM2 for managing running processes. PM2 allows configuring the process with a configuration file.

The app includes a template file for starting its procces.

### How To

1) SSH into the server.

2) Go to the location of the cloned repo.

3) Copy the template file: `cp template.process.yaml proccess.yaml`

4) Add env vars to the file.

5) Setup the process for running the app: `pm2 start process.yaml`
