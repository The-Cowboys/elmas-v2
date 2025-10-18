# elmas-v2

🇺🇾 Averigüemos quién es el más tonto.

🇬🇧 Let's find out who is the dumbest.


...but this time in Scala using Scala CLI.

## Run in Production

Our server uses PM2 for managing running processes. 

PM2 allows configuring processes with configuration files.

The app includes a template file for starting its process.

### How-To

1) SSH into the server.

2) CD into the cloned repo: `cd elmas-v2`

3) Copy the template file: `cp template.process.yaml proccess.yaml`

4) Configure env vars by editing `proccess.yaml`

5) Setup the process for running the app: `pm2 start process.yaml`

## Release

1) Package with:
    ```
    scala-cli package . -o ./bin/Main -f
    ```

2) Update version

3) Commit changes.

### Rollout the update

1) SSH into the server.

2) CD into the repo: `cd elmas-v2`

3) Git pull

4) Reload with `pm2 reload process.yaml`
