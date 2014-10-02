(ns aws-datapipeline-test.core
  (:require
   [amazonica.aws.s3           :as s3]
   [amazonica.aws.datapipeline :as pipe]
   [amazonica.aws.sns          :as sns]))


(comment

  (def src-bucket-name "relay-datapipeline-test-src")
  (def dst-bucket-name "relay-datapipeline-test-dst")

  ;; create src/dest buckets for simple s3->s3 copy pipeline
  (s3/create-bucket src-bucket-name)
  (s3/create-bucket dst-bucket-name)

  ;; create topic for email notifications
  (sns/create-topic :name "relay-datapipeline-test")
)
