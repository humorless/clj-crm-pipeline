# clj-crm-pipeline

A continuous delivery pipeline for [clj-crm](https://github.com/humorless/clj-crm) and [react-redux-crm](https://github.com/humorless/react-redux-crm)

## Usage

* `lein run` will start your pipeline with a web-ui listening on port 8080

## Files

* `/`
    * `project.clj` contains dependencies and build configuration for Leiningen

* `/src/clj_crm_pipeline/`
    * `pipeline.clj` contains your pipeline-definition
    * `steps.clj` contains your custom build-steps
    * `core.clj` contains the `main` function that wires everything together

* `/resources/`
    * `logback.xml` contains a sample log configuration

## Note

When I wrote `deploy.sh` at backend, frontend project, I encounter certain problems with detaching process. I solved it with tmux, so use this pipeline by `nohup` to avoid nested tmux session.
