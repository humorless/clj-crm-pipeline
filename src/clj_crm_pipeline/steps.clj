(ns clj-crm-pipeline.steps
  (:require [lambdacd.steps.shell :as shell]
            [lambdacd-git.core :as lambdacd-git]))

(def backend-repo-uri "https://github.com/humorless/clj-crm")
(def frontend-repo-uri "https://github.com/humorless/react-redux-crm")

(def repo-branch "master")

(defn wait-for-backend-repo [args ctx]
  (lambdacd-git/wait-for-git ctx backend-repo-uri
                             ; how long to wait when polling. optional, defaults to 10000
                             :ms-between-polls 2000
                             ; which refs to react to. optional, defaults to refs/heads/master
                             :ref "refs/heads/master"))

(defn wait-for-frontend-repo [args ctx]
  (lambdacd-git/wait-for-git ctx frontend-repo-uri
                             ; how long to wait when polling. optional, defaults to 10000
                             :ms-between-polls 2000
                             ; which refs to react to. optional, defaults to refs/heads/master
                             :ref "refs/heads/master"))

(defn clone-backend [args ctx]
  (let [revision (:revision args)
        cwd      (:cwd args)
        ref      (or revision repo-branch)]
    (lambdacd-git/clone ctx backend-repo-uri ref cwd)))

(defn clone-frontend [args ctx]
  (let [revision (:revision args)
        cwd      (:cwd args)
        ref      (or revision repo-branch)]
    (lambdacd-git/clone ctx frontend-repo-uri ref cwd)))

(defn build-backend [{cwd :cwd} ctx]
  (shell/bash ctx cwd
              "sed -i 's/10.20.30.40/10.113.214.181/g' env/prod/resources/config.edn"
              "lein deps"
              "lein uberjar"))

(defn build-frontend [{cwd :cwd} ctx]
  (shell/bash ctx cwd
              "sed -i 's/127.0.0.1/10.113.214.181/g' src/agent.js"
              "npm install"
              "npm run build"))

(defn deploy [{cwd :cwd} ctx]
  (shell/bash ctx cwd
              "./service/deploy.sh"))
