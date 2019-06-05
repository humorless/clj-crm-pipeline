(ns clj-crm-pipeline.pipeline
  (:require
   [lambdacd.steps.control-flow :as control-flow]
   [lambdacd.steps.manualtrigger :as manualtrigger]
   [clj-crm-pipeline.steps :as steps]))

(def pipeline-def
  `((control-flow/in-parallel
     (control-flow/run
      (control-flow/either
       manualtrigger/wait-for-manual-trigger
       steps/wait-for-backend-repo)
      (control-flow/with-workspace
        steps/clone-backend
        steps/build-backend
        steps/deploy))
     (control-flow/run
      (control-flow/either
       manualtrigger/wait-for-manual-trigger
       steps/wait-for-frontend-repo)
      (control-flow/with-workspace
        steps/clone-frontend
        steps/build-frontend
        steps/deploy)))))
