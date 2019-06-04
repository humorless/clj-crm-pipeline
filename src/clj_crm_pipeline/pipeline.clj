(ns clj-crm-pipeline.pipeline
  (:require
   [lambdacd.steps.control-flow :as control-flow]
   [lambdacd.steps.manualtrigger :as manualtrigger]
   [clj-crm-pipeline.steps :as steps]))

(def pipeline-def
  `((control-flow/either
     manualtrigger/wait-for-manual-trigger
     steps/wait-for-commit-on-master)
    (control-flow/with-workspace
      steps/clone
      steps/build
      (control-flow/in-parallel
       steps/kill
       steps/deploy))))
