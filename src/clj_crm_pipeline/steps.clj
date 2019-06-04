(ns clj-crm-pipeline.steps
  (:require [lambdacd.steps.shell :as shell]
            [lambdacd-git.core :as lambdacd-git]))

;(def repo-uri "https://github.com/humorless/clj-crm")

(def clj-repo-uri "https://github.com/humorless/clj-crm")

(def clj-repo-branch "master")

(defn wait-for-commit-on-master [args ctx]
  (lambdacd-git/wait-for-git ctx clj-repo-uri
                             ; how long to wait when polling. optional, defaults to 10000
                             :ms-between-polls 2000
                             ; which refs to react to. optional, defaults to refs/heads/master
                             :ref "refs/heads/master"))

(defn clone [args ctx]
  (let [revision (:revision args)
        cwd      (:cwd args)
        ref      (or revision clj-repo-branch)]
    (lambdacd-git/clone ctx clj-repo-uri ref cwd)))

(defn build [{cwd :cwd} ctx]
  (shell/bash ctx cwd
              "lein uberjar"))

(defn kill [{cwd :cwd} ctx]
  (shell/bash ctx cwd
              "kill -9 $(cat ~/backend/clj-crm.pid)"))

(defn deploy [{cwd :cwd} ctx]
  (shell/bash ctx cwd
              "cp target/uberjar/clj-crm.jar    ~/backend/target/uberjar/clj-crm.jar"
              "cp env/prod/resources/config.edn ~/backend/config.edn"
              "cd ~backend && nohup java -Dconf=~/backend/config.edn -Dcors-regex-str=\"http://10.20.30.40:5000\" -jar ~/backend/target/uberjar/clj-crm.jar & PID=$!; echo $PID > ~/backend/clj-crm.pid"))
